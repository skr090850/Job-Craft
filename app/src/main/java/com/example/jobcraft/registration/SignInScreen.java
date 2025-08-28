package com.example.jobcraft.registration;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignInScreen extends AppCompatActivity {
    private TextView signUpTxtBtn, forgotTxtBtn;
    private ImageButton signInBackBtn, facebookBtn, googleBtn, appleBtn;
    private Button signInBtn;
    private EditText etEmail, etPassword;
    private TextInputLayout tilEmail, tilPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private View dimOverlay;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_sign_in_screen);
        signUpTxtBtn = findViewById(R.id.signUpTextBtn);
        signInBackBtn = findViewById(R.id.signInBackBtn);
        forgotTxtBtn = findViewById(R.id.forgotTextBtn);
        etEmail = findViewById(R.id.signInEmail);
        etPassword = findViewById(R.id.signInPass);
        signInBtn = findViewById(R.id.signInBtn);
        tilEmail = findViewById(R.id.signInemailInputLayout);
        tilPassword = findViewById(R.id.signInPasswordInputLayout);
        facebookBtn = findViewById(R.id.signInFacebook);
        googleBtn = findViewById(R.id.signInGoogle);
        appleBtn = findViewById(R.id.signInApple);
        progressBar = findViewById(R.id.progressBar);
        dimOverlay = findViewById(R.id.dimOverlay);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    tilEmail.setError("Email cannot be empty");
                    return;
                } else {
                    tilEmail.setError(null);
                }
                if (password.isEmpty()) {
                    tilPassword.setError("Password cannot be empty");
                    return;
                } else {
                    tilPassword.setError(null);
                }
                showLoading(true);
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showLoading(false);
                        if (task.isSuccessful()) {
                            Log.d("FirebaseAuth", "SignInWithEmail:Success");
                            CustomToast.showToast(SignInScreen.this, "Sign In Successful!", CustomToast.SUCCESS);
                            Intent intent = new Intent(SignInScreen.this, DashboardScreen.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("FirebaseAuth", "SignInWithEmail:Failure", task.getException());
                            CustomToast.showToast(SignInScreen.this, "Authentication failed: " + task.getException().getMessage(), CustomToast.ERROR);
                        }
                    }
                });
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
                CustomToast.showToast(SignInScreen.this, "Apple Sign in is not working", CustomToast.ERROR);
            }
        });
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomToast.showToast(SignInScreen.this, "Facebook Sign in is not working", CustomToast.ERROR);
            }
        });
        forgotTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInScreen.this, ResetPasswordScreen.class);
                startActivity(intent);
            }
        });
        signUpTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInScreen.this, SignUpScreen.class);
                startActivity(intent);
            }
        });
        signInBackBtn.setOnClickListener(new View.OnClickListener() {
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

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            dimOverlay.setVisibility(View.VISIBLE);
            signInBtn.setEnabled(false);
            googleBtn.setEnabled(false);
            facebookBtn.setEnabled(false);
            appleBtn.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            dimOverlay.setVisibility(View.GONE);
            signInBtn.setEnabled(true);
            googleBtn.setEnabled(true);
            facebookBtn.setEnabled(true);
            appleBtn.setEnabled(true);
        }
    }

    // Google sign in
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                showLoading(false);
                Log.w("GoogleSignIn", "Google sign in failed", e);
                CustomToast.showToast(this, "Google Sign In Failed", CustomToast.ERROR);
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
                    CustomToast.showToast(SignInScreen.this, "Authentication failed:" + task.getException().getMessage(), CustomToast.ERROR);
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
                    CustomToast.showToast(SignInScreen.this, "Sign In Successful!", CustomToast.SUCCESS);
                    Intent intent = new Intent(SignInScreen.this, DashboardScreen.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Log.w("Firestore", "Error saving Google user details", e);
                    CustomToast.showToast(SignInScreen.this, "Failed to save user details.", CustomToast.ERROR);
                });
    }
}