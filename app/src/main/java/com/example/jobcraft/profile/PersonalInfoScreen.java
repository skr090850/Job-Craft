package com.example.jobcraft.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.jobcraft.R;
import com.example.jobcraft.databinding.ProfilePersonalInfoScreenBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PersonalInfoScreen extends AppCompatActivity {
    private ProfilePersonalInfoScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfilePersonalInfoScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setDatePicker();
        setupGenderDropdown();
        binding.profilePersonalBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfoScreen.this,InterestScreen.class);
                startActivity(intent);
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
}