package com.example.jobcraft.registration;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.jobcraft.R;
import com.example.jobcraft.home.DashboardScreen;
import com.example.jobcraft.splash_and_onboarding.OnBoardingScreen;
import com.example.jobcraft.utils.CustomToast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpScreen extends AppCompatActivity {
    private TextView signInTxtBtn;
    private ImageButton signUpBackBtn,facebookBtn,googleBtn,appleBtn;
    private Button signUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextInputLayout tilName, tilEmail, tilPass, tilConfPass;
    private EditText etName, etEmail, etPass, etConfPass;
    private ProgressBar progressBar;
    private View dimOverlay;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;   // this is the request code, we can also write another number.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_sign_up_screen);
        tilName = findViewById(R.id.signUpNameInputLayout);
        tilEmail = findViewById(R.id.signUpEmailInputLayout);
        tilPass = findViewById(R.id.signUpPasswordInputLayout);
        tilConfPass = findViewById(R.id.signUpConfPasswordInputLayout);
        etName = findViewById(R.id.signUpName);
        etEmail = findViewById(R.id.signUpEmail);
        etPass = findViewById(R.id.signUpPass);
        etConfPass = findViewById(R.id.signUpConfPass);
        signInTxtBtn = findViewById(R.id.signInTextBtn);
        signUpBackBtn = findViewById(R.id.signUpBackBtn);
        facebookBtn = findViewById(R.id.SignUpfacebookBtn);
        googleBtn = findViewById(R.id.SignUpGoogleBtn);
        appleBtn = findViewById(R.id.SignUpAppleBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        progressBar = findViewById(R.id.progressBar);
        dimOverlay = findViewById(R.id.dimOverlay);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(true);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        appleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomToast.showToast(SignUpScreen.this,"Apple Sign in is not working",CustomToast.ERROR);
            }
        });
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomToast.showToast(SignUpScreen.this,"Facebook Sign in is not working",CustomToast.ERROR);
            }
        });
        signInTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpScreen.this, SignInScreen.class);
                startActivity(intent);
                finish();
            }
        });
        signUpBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController != null) {
            int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                windowInsetsController.setAppearanceLightStatusBars(false);
            } else {
                windowInsetsController.setAppearanceLightStatusBars(true);
            }
        }
    }
    private void registerUser() {
        String name = etName.getText().toString().trim(),
                email = etEmail.getText().toString().trim(),
                password = etPass.getText().toString().trim(),
                confirmPassword = etConfPass.getText().toString().trim();

        if (name.isEmpty()) {
            tilName.setError("Name cannot be empty");
            return;
        } else {
            tilName.setError(null);
        }

        if (email.isEmpty()) {
            tilEmail.setError("Email cannot be empty");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Please enter a valid email address");
            return;
        } else {
            tilEmail.setError(null);
        }

        if (password.isEmpty()) {
            tilPass.setError("Password cannot be empty");
            return;
        } else if (password.length() < 6) {
            tilPass.setError("Password must be at least 6 characters");
            return;
        } else {
            tilPass.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            tilConfPass.setError("Password do not match");
            return;
        } else {
            tilConfPass.setError(null);
        }
        showLoading(true);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("FirebaseAuth", "createUserWithEmail: Success");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    saveUserDetails(firebaseUser, name, email);
                } else {
                    showLoading(false);
                    Log.w("FirebaseAuth", "createUserWithEmail: failure", task.getException());
                    CustomToast.showToast(SignUpScreen.this, "Authentication failed: " + task.getException().getMessage(), CustomToast.ERROR);
                }
            }
        });
    }

    private void saveUserDetails(FirebaseUser firebaseUser, String fullName, String email) {
        String userId = firebaseUser.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);
        db.collection("user").document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showLoading(false);
                Log.d("FireStore", "DocumentSnapshot successfully written");
                CustomToast.showToast(SignUpScreen.this, "Registration Successful", CustomToast.SUCCESS);
                Intent intent = new Intent(SignUpScreen.this, SignInScreen.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showLoading(false);
                Log.w("FireStore", "Error Writing document", e);
                CustomToast.showToast(SignUpScreen.this, "Error saving user details", CustomToast.ERROR);
            }
        });

    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            dimOverlay.setVisibility(View.VISIBLE);
            signUpBtn.setEnabled(false);
            googleBtn.setEnabled(false);
            facebookBtn.setEnabled(false);
            appleBtn.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            dimOverlay.setVisibility(View.GONE);
            signUpBtn.setEnabled(true);
            googleBtn.setEnabled(true);
            facebookBtn.setEnabled(true);
            appleBtn.setEnabled(true);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                showLoading(false);
                Log.w("GoogleSignIn", "Google sign in failed", e);
                CustomToast.showToast(this, "Google Sign In Failed",CustomToast.ERROR);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseAuth", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                saveUserDetailsWithGoogleSignInToFirestore(user);
                            }
                        } else {
                            showLoading(false);
                            Log.w("FirebaseAuth", "signInWithCredential:failure", task.getException());
                            CustomToast.showToast(SignUpScreen.this, "Authentication failed: "+task.getException().getMessage(), CustomToast.ERROR);
                        }
                    }
                });
    }
    private void saveUserDetailsWithGoogleSignInToFirestore(@NonNull FirebaseUser firebaseUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = firebaseUser.getUid();
        String fullName = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();

        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);

        db.collection("user").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    Log.d("Firestore", "Google user details saved successfully.");
                    CustomToast.showToast(SignUpScreen.this, "Sign In Successful!", CustomToast.SUCCESS);
                    Intent intent = new Intent(SignUpScreen.this, DashboardScreen.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Log.w("Firestore", "Error saving Google user details", e);
                    CustomToast.showToast(SignUpScreen.this, "Failed to save user details.", CustomToast.ERROR);
                });
    }
}