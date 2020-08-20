package ru.geekbrains.weatherapp_2.ui.googleAuth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import java.util.Objects;
import ru.geekbrains.weatherapp_2.MainActivity;
import ru.geekbrains.weatherapp_2.R;
import static android.content.Context.LOCATION_SERVICE;

public class SignIn extends Fragment {
    private int RC_SIGN_IN = 100;
    private String lat;
    private String lng;
    private String TAG1 = "locationLogMain";
    private GoogleSignInClient googleSignInClient;
    private Location loc;
    LocationManager mLocManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signin_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSignInParameters();
        initSignInBtn();
        locationDetect();
    }

    private void locationDetect() {
        LocationManager mLocManager;
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            mLocManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            mLocManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
            try {
                loc = Objects.requireNonNull(mLocManager)
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc != null) {
                    lat = Double.toString(loc.getLatitude());
                    lng = Double.toString(loc.getLongitude()); // Долгота
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            loc = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = Double.toString(loc.getLatitude());
                lng = Double.toString(loc.getLongitude()); // Долгота
                Log.d(TAG1, "Долгота GPS такое сожерит: " + lng);
                Log.d(TAG1, "Широта GPS такое сожерит: " + lat);
            }
            loc = mLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (loc != null) {
                lat = Double.toString(loc.getLatitude());
                lng = Double.toString(loc.getLongitude()); // Долгота
                Log.d("LOCATION-CHECK", "Долгота in locationDetect такое сожерит: " + lng);
                Log.d("LOCATION-CHECK", "Широта  in locationDetect такое сожерит: " + lat);
            }
        }
    }

    private void openView() {
        locationDetect();
        Log.d("LOCATION-CHECK", "openView FIRST: широта" + lng);
        Log.d("LOCATION-CHECK", "openView FIRST: долгота " + lat);
        if (lat != null && lng != null ) {
            Bundle bundle = new Bundle();
            bundle.putString("lat_key", lat);
            bundle.putString("lng_key", lng);
            Log.d(TAG1, "onCreate: пытаемся передать в WeatherToday " + lng + " " + lat);
            Navigation.findNavController(requireView()).navigate(R.id.action_signin_to_weather, bundle);
        } else {
            Log.d("LOCATION-CHECK", "openView: широта" + lng);
            Log.d("LOCATION-CHECK", "openView: долгота " + lat);
            Navigation.findNavController(requireView()).navigate(R.id.action_signin_to_citychoice);
        }
    }

    private void initSignInParameters() {
        locationDetect();
        String serverClientId = "27334653409-g7pp02fes6t6kiggci811vpuv3dr0nhl.apps.googleusercontent.com";
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(serverClientId)
                .requestServerAuthCode(serverClientId, false)
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    private void initSignInBtn() {
        SignInButton signInButton = requireView().findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(v -> signIn());
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireActivity());
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null || account.isExpired() || account.getIdToken() == null) {
            Log.d("TAG", "updateUI: " + account);
        } else {
            Toast.makeText(requireActivity(), account.getIdToken(), Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(MainActivity.class.getSimpleName(), "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            SignInButton signInButton = requireView().findViewById(R.id.sign_in_button);
            signInButton.setVisibility(View.GONE);
            openView();
        }
    }
}
