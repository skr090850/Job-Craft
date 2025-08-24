package com.example.jobcraft.splash_and_onboarding;

import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jobcraft.R;

import java.util.ArrayList;
import java.util.List;

public class OnBoarding extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private SliderAdapter sliderAdapter;
    private List<SliderItem> sliderItems;
    private Handler sliderHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_and_onboarding_on_boarding);
        viewPager2 = findViewById(R.id.onboardingSlider);
        sliderItems = new ArrayList<>();

        sliderItems.add(new SliderItem(R.drawable.on_boarding_ic_1,"Search Job Easier\nand More Effective","Make your experience of searching job\nmore easier and more effective"));
        sliderItems.add(new SliderItem(R.drawable.on_boarding_ic_2,"Apply for job\nanywhere & anytime","Jobfil makes you can apply for job from\nanywhere and anytime"));
        sliderItems.add(new SliderItem(R.drawable.on_boarding_ic_3,"Help Find the Right Job\nWith Your Desire","Jobfil can help you find the right\njob with your desire "));

        sliderAdapter = new SliderAdapter(sliderItems);
        viewPager2.setAdapter(sliderAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            public void onPageScrolled(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager2.getCurrentItem();
            if (currentItem == sliderItems.size() - 1) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(currentItem + 1);
            }
            sliderHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}