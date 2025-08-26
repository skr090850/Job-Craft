package com.example.jobcraft.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jobcraft.R;

public class OtpVerificationScreen extends AppCompatActivity {
    private EditText otpDigit1, otpDigit2, otpDigit3, otpDigit4;
    private TextView userEmail;
    ImageButton backBtn;
    private Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registration_otp_verification_screen);
        otpDigit1 = findViewById(R.id.otpDigit1);
        otpDigit2 = findViewById(R.id.otpDigit2);
        otpDigit3 = findViewById(R.id.otpDigit3);
        otpDigit4 = findViewById(R.id.otpDigit4);
        userEmail = findViewById(R.id.verificationEmail);
        resetBtn = findViewById(R.id.otpScreenResetBtn);
        backBtn = findViewById(R.id.verificationBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpVerificationScreen.this,EnterPasswordScreen.class);
                startActivity(intent);
                finish();
            }
        });
        String email = getIntent().getStringExtra("USER_EMAIL");
        userEmail.setText(email);

        setupOtpEditTexts();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void setupOtpEditTexts() {
        // TextWatcher for moving focus forward
        otpDigit1.addTextChangedListener(new OtpTextWatcher(otpDigit1, otpDigit2));
        otpDigit2.addTextChangedListener(new OtpTextWatcher(otpDigit2, otpDigit3));
        otpDigit3.addTextChangedListener(new OtpTextWatcher(otpDigit3, otpDigit4));
        otpDigit4.addTextChangedListener(new OtpTextWatcher(otpDigit4, null));

        // KeyListener for moving focus backward on backspace
        otpDigit2.setOnKeyListener(new OtpKeyListener(otpDigit2, otpDigit1));
        otpDigit3.setOnKeyListener(new OtpKeyListener(otpDigit3, otpDigit2));
        otpDigit4.setOnKeyListener(new OtpKeyListener(otpDigit4, otpDigit3));
    }
    private class OtpTextWatcher implements TextWatcher {
        private View currentView;
        private View nextView;

        public OtpTextWatcher(View currentView, View nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            if (text.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }

    }
    private class OtpKeyListener implements View.OnKeyListener {
        private EditText currentEditText;
        private EditText previousEditText;

        public OtpKeyListener(EditText currentEditText, EditText previousEditText) {
            this.currentEditText = currentEditText;
            this.previousEditText = previousEditText;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // Check if the key pressed is backspace and the action is key down
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                // If the current EditText is empty, move focus to the previous one
                if (currentEditText.getText().toString().isEmpty()) {
                    previousEditText.requestFocus();
                    return true;
                }
            }
            return false;
        }
    }
}