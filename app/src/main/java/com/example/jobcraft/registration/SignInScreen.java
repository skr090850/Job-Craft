package com.example.jobcraft.registration;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.jobcraft.R;
import com.example.jobcraft.splash_and_onboarding.OnBoardingScreen;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInScreen extends AppCompatActivity {
    private TextView signUpTxtBtn,forgotTxtBtn;
    private ImageButton signInBackBtn;
    private Button signInBtn;
    private EditText etEmail,etPassword;
    private TextInputLayout tilEmail,tilPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (email.isEmpty()) {
                    tilEmail.setError("Email cannot be empty");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tilEmail.setError("Please enter a valid email address");
                } else {
                    tilEmail.setError(null);
                }
                if(password.isEmpty()){
                    tilPassword.setError("Password cannot be empty");
                }else{
                    tilPassword.setError(null);
                }
            }
        });

        forgotTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInScreen.this,ResetPasswordScreen.class);
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
}