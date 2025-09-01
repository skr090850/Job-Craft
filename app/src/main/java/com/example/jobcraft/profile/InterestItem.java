package com.example.jobcraft.profile;

public class InterestItem {
    private final int iconResId;
    private final String itemName;
    private boolean isSelected;

    public InterestItem(int iconResId, String itemName, boolean isSelected) {
        this.iconResId = iconResId;
        this.itemName = itemName;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getItemName() {
        return itemName;
    }
}
