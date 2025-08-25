package com.example.jobcraft.registration;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.jobcraft.R;
import com.example.jobcraft.splash_and_onboarding.OnBoardingScreen;

public class SignInScreen extends AppCompatActivity {
    TextView signUpTxtBtn,forgotTxtBtn;
    ImageButton signInBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_sign_in_screen);

        signUpTxtBtn = findViewById(R.id.signUpTextBtn);
        signInBackBtn = findViewById(R.id.signInBackBtn);
        forgotTxtBtn = findViewById(R.id.forgotTextBtn);
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