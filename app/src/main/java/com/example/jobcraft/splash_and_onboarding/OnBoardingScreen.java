package com.example.jobcraft.splash_and_onboarding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jobcraft.R;
import com.example.jobcraft.registration.SignInScreen;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingScreen extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private SliderAdapter sliderAdapter;
    private List<SliderItem> sliderItems;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private TabLayout tabLayout;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_and_onboarding_on_boarding_screen);
        viewPager2 = findViewById(R.id.onboardingSlider);
        tabLayout = findViewById(R.id.onBoardingTabIndicator);
        startBtn = findViewById(R.id.onBoardingBtn);
        sliderItems = new ArrayList<>();

        sliderItems.add(new SliderItem(R.drawable.on_boarding_ic_1,"Search Job Easier\nand More Effective","Make your experience of searching job\nmore easier and more effective"));
        sliderItems.add(new SliderItem(R.drawable.on_boarding_ic_2,"Apply for job\nanywhere & anytime","Jobfil makes you can apply for job from\nanywhere and anytime"));
        sliderItems.add(new SliderItem(R.drawable.on_boarding_ic_3,"Help Find the Right Job\nWith Your Desire","Jobfil can help you find the right\njob with your desire "));

        sliderAdapter = new SliderAdapter(sliderItems);
        viewPager2.setAdapter(sliderAdapter);

        viewPager2.setPageTransformer(new DepthPageTransformer());

        for (int i = 0; i < sliderItems.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
        }

        int startPosition = Integer.MAX_VALUE / 2;
        viewPager2.setCurrentItem(startPosition - (startPosition % sliderItems.size()), false);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);

                int realPosition = position % sliderItems.size();
                tabLayout.selectTab(tabLayout.getTabAt(realPosition));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                int currentItem = viewPager2.getCurrentItem();
                int currentRealItem = currentItem % sliderItems.size();
                int diff = tabPosition - currentRealItem;

                if (diff != 0) {
                    viewPager2.setCurrentItem(currentItem + diff, true);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoardingScreen.this, SignInScreen.class);
                startActivity(intent);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            animateViewPagerScroll();
        }
    };
    private void animateViewPagerScroll() {
        final int nextItem = viewPager2.getCurrentItem() + 1;

        ValueAnimator animator = ValueAnimator.ofInt(0, viewPager2.getWidth());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private int oldDragPosition = 1;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int dragPosition = (int) animation.getAnimatedValue();
                int dragOffset = dragPosition - oldDragPosition;
                oldDragPosition = dragPosition;

                if (viewPager2.isFakeDragging()) {
                    viewPager2.fakeDragBy(-dragOffset);
                }
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                viewPager2.beginFakeDrag();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (viewPager2.isFakeDragging()) {
                    viewPager2.endFakeDrag();
                }
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(800);
        animator.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}