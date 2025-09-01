package com.example.jobcraft.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.jobcraft.R;
import com.example.jobcraft.databinding.ProfileInterestScreenBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InterestScreen extends AppCompatActivity {
    private ProfileInterestScreenBinding binding;
    private InterestsAdapter adapter;
    private List<InterestItem> interestItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfileInterestScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpRecyclerView();
        setupClickListeners();
    }
    private void setUpRecyclerView(){
        interestItems = new ArrayList<>();
        interestItems.add(new InterestItem(R.drawable.ic_brush,"Design",true));
        interestItems.add(new InterestItem(R.drawable.ic_message_programme,"Develop",false));
        interestItems.add(new InterestItem(R.drawable.ic_cpu,"Information Tech",false));
        interestItems.add(new InterestItem(R.drawable.ic_task,"Research & Analytic",true));
        interestItems.add(new InterestItem(R.drawable.ic_status_up,"Marketing",false));

        adapter = new InterestsAdapter(interestItems);
        binding.interestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.interestRecyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        binding.interesbackButton.setOnClickListener(v -> finish());

        binding.interestBtn.setOnClickListener(v -> {
            List<String> selectedInterests = interestItems.stream()
                    .filter(InterestItem::isSelected)
                    .map(InterestItem::getItemName)
                    .collect(Collectors.toList());

            String message;
            if (selectedInterests.isEmpty()) {
                message = "Please select at least one interest.";
            } else {
                message = "Selected: " + String.join(", ", selectedInterests);
                Intent intent = new Intent(InterestScreen.this,CnfmNewAccScreen.class);
                startActivity(intent);
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }
}