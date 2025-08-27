package com.example.jobcraft.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobcraft.R;

public class CustomToast {

    public static final int SUCCESS = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;

    public static void showToast(Context context, String message, int type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);

        LinearLayout toastContainer = layout.findViewById(R.id.custom_toast_container);
        ImageView icon = layout.findViewById(R.id.toast_icon);
        TextView text = layout.findViewById(R.id.toast_text);

        text.setText(message);

        switch (type) {
            case SUCCESS:
                toastContainer.setBackgroundResource(R.drawable.toast_success_bg);
                icon.setImageResource(R.drawable.ic_success);
                break;
            case WARNING:
                toastContainer.setBackgroundResource(R.drawable.toast_warning_bg);
                icon.setImageResource(R.drawable.ic_error);
                break;
            case ERROR:
                toastContainer.setBackgroundResource(R.drawable.toast_error_bg);
                icon.setImageResource(R.drawable.ic_error);
                break;
        }

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 150);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
