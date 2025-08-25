package com.example.jobcraft.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jobcraft.R;
import com.example.jobcraft.splash_and_onboarding.OnBoardingScreen;

public class SignInScreen extends AppCompatActivity {
    TextView signUpTxtBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_sign_in_screen);

        signUpTxtBtn = findViewById(R.id.signUpTextBtn);
        signUpTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInScreen.this, SignUpScreen.class);
                startActivity(intent);
            }
        });
    }
}