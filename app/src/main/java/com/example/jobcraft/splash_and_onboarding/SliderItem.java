package com.example.jobcraft.splash_and_onboarding;

public class SliderItem {
    private int image;
    private String title;
    private String subTitle;

    public SliderItem(int image, String title, String subTitle) {
        this.image = image;
        this.title = title;
        this.subTitle = subTitle;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }
}
