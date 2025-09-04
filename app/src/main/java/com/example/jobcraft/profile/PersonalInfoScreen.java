package com.example.jobcraft.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.jobcraft.R;
import com.example.jobcraft.databinding.ProfilePersonalInfoScreenBinding;
import com.example.jobcraft.utils.CustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PersonalInfoScreen extends AppCompatActivity {
    private ProfilePersonalInfoScreenBinding binding;
    private String userId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfilePersonalInfoScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        if(intent!=null&& intent.hasExtra("USER_ID")){
            userId = intent.getStringExtra("USER_ID");
            loadUserData();
        }else {
            CustomToast.showToast(this, "Error: User ID not found.", CustomToast.ERROR);
            finish();
            return;
        }
        setDatePicker();
        setupGenderDropdown();
        binding.profilePersonalBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfoScreen.this,InterestScreen.class);
                startActivity(intent);
            }
        });
        binding.profilePersonalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
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
    private void loadUserData(){
        showLoading(true);
        db.collection("user").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String fullName = documentSnapshot.getString("fullName");
                    String email = documentSnapshot.getString("email");
                    if (fullName!=null && email!=null){
                        binding.profilePersonalName.setText(fullName);
                        binding.profilePersonalEmail.setText(email);
                    }
                }else {
                    Log.d("Firestore","No Such Document");
                }
                showLoading(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showLoading(false);
                Log.w("Firestore", "Error getting document", e);
                CustomToast.showToast(PersonalInfoScreen.this, "Failed to load user data.", CustomToast.ERROR);
            }
        });
    }
    private void saveUserData(){
        String fullname = binding.profilePersonalName.getText().toString().trim();
        String email = binding.profilePersonalEmail.getText().toString().trim();
        String contact = binding.profilePersonalContact.getText().toString().trim();
        String dob = binding.profilePersonalDob.getText().toString().trim();
        String gender = binding.profilePersonalGender.getText().toString().trim();
        String contactRegex = "^[6-9]\\d{9}$";
        if (fullname.isEmpty()){
            binding.profilePersonalNameInputLayout.setError("Name cannot be empty");
            return;
        }else {
            binding.profilePersonalNameInputLayout.setError(null);
        }
        if (email.isEmpty()) {
            binding.profilePersonalEmailInputLayout.setError("Email cannot be empty");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.profilePersonalEmailInputLayout.setError("Please enter a valid email address");
            return;
        } else {
            binding.profilePersonalEmailInputLayout.setError(null);
        }
        if (contact.isEmpty()){
            binding.profilePersonalContactInputLayout.setError("Contact number cannot be empty");
            return;
        }else if (!contact.matches(contactRegex)) {
            binding.profilePersonalContactInputLayout.setError("Please enter a valid 10-digit number");
            return;
        }else {
            binding.profilePersonalContactInputLayout.setError(null);
        }
        if(dob.isEmpty()){
            binding.profilePersonalDobInputLayout.setError("Date of birth is required");
            return;
        }else {
            binding.profilePersonalDobInputLayout.setError(null);
        }
        if (gender.isEmpty()){
            binding.profilePersonalGenderInputLayout.setError("Gender is required");
            return;
        }else {
            binding.profilePersonalGenderInputLayout.setError(null);
        }
        showLoading(true);
        Map<String,Object> personInfo = new HashMap<>();
        personInfo.put("fullName",fullname);
        personInfo.put("contactNumber",contact);
        personInfo.put("dateOfBirth",dob);
        personInfo.put("gender",gender);
        db.collection("user").document(userId).update(personInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showLoading(false);
                Log.d("Firestore", "User personal info successfully updated!");
                CustomToast.showToast(PersonalInfoScreen.this, "Profile Updated!", CustomToast.SUCCESS);
                Intent intent = new Intent(PersonalInfoScreen.this, InterestScreen.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showLoading(false);
                Log.w("Firestore", "Error updating document", e);
                CustomToast.showToast(PersonalInfoScreen.this, "Failed to update profile.", CustomToast.ERROR);
            }
        });
    }
    private void setDatePicker(){
        binding.profilePersonalDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year  = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PersonalInfoScreen.this,
                        new DatePickerDialog.OnDateSetListener(){
                            public void onDateSet(DatePicker view, int selectYear, int selectMonth, int selectDay){
                                Calendar selectDate=Calendar.getInstance();
                                selectDate.set(selectYear,selectMonth,selectDay);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String formattedDate = dateFormat.format(selectDate.getTime());
                                binding.profilePersonalDob.setText(formattedDate);
                            }
                        },
                        year,
                        month,
                        day
                );
                Calendar  maxDateCalendar = Calendar.getInstance();
                maxDateCalendar.add(Calendar.YEAR,-16);
                datePickerDialog.getDatePicker().setMaxDate(maxDateCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }
    private void setupGenderDropdown() {
        List<String> genders = Arrays.asList("Male", "Female", "Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                genders
        );

        binding.profilePersonalGender.setAdapter(adapter);
    }
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.dimOverlay.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.dimOverlay.setVisibility(View.GONE);
        }
    }
}