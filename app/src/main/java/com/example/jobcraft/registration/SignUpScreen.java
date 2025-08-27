package com.example.jobcraft.registration;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.jobcraft.splash_and_onboarding.OnBoardingScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpScreen extends AppCompatActivity {
    private TextView signInTxtBtn;
    private ImageButton signUpBackBtn;
    private Button signUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextInputLayout tilName,tilEmail,tilPass,tilConfPass;
    private EditText etName,etEmail,etPass,etConfPass;
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
        signUpBtn = findViewById(R.id.signUpBtn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                registerUser();
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
    private void registerUser(){
        String name = etName.getText().toString().trim(),
                email = etEmail.getText().toString().trim(),
                password = etPass.getText().toString().trim(),
                confirmPassword = etConfPass.getText().toString().trim();

        if(name.isEmpty()){
            tilName.setError("Name cannot be empty");
        } else if (email.isEmpty()) {
            tilEmail.setError("Email cannot be empty");
        } else if (password.isEmpty()) {
            tilPass.setError("Password cannot be empty");
        } else {
            tilName.setError(null);
            tilEmail.setError(null);
            tilPass.setError(null);
        }
        if (!password.equals(confirmPassword)){
            tilConfPass.setError("Password do not match");
        }else {
            tilConfPass.setError(null);
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("FirebaseAuth","createUserWithEmail: Success");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    saveUserDetails(firebaseUser,name,email);
                }else{
                    Log.w("FirebaseAuth","createUserWithEmail: failure",task.getException());
                    Toast.makeText(SignUpScreen.this,"Authentication Failed.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void saveUserDetails(FirebaseUser firebaseUser,String fullName,String email){
        String userId = firebaseUser.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("fullName",fullName);
        user.put("email",email);
        db.collection("user").document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("FireStore","DocumentSnapshot successfully written");
                Toast.makeText(SignUpScreen.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpScreen.this, SignInScreen.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("FireStore","Error Writing document",e);
                Toast.makeText(SignUpScreen.this,"Error saving user details",Toast.LENGTH_SHORT).show();
            }
        });

    }
}