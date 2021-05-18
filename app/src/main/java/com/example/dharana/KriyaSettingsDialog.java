package com.example.dharana;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class KriyaSettingsDialog extends Dialog {
    private EditText upField;
    private EditText topHoldField;
    private EditText downField;
    private EditText bottomHoldField;

    private Button cancelButton;
    private Button setButton;
    private Button resetButton;

    public KriyaSettingsDialog(@NonNull Context context, KriyaFragment kriyaFragment) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.dialog_settings_kriya);

        upField = findViewById(R.id.upField);
        topHoldField = findViewById(R.id.topHoldField);
        downField = findViewById(R.id.downField);
        bottomHoldField = findViewById(R.id.bottomHoldField);
        cancelButton = findViewById(R.id.cancelButton);
        setButton = findViewById(R.id.setButton);
        resetButton = findViewById(R.id.resetButton);

        upField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        topHoldField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        downField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});

        cancelButton.setOnClickListener(view -> {
            upField.setText(kriyaFragment.getUpTime());
            topHoldField.setText(kriyaFragment.getTopHoldTime());
            downField.setText(kriyaFragment.getDownTime());
            bottomHoldField.setText(kriyaFragment.getBottomHoldTime());
            dismiss();
        });

        resetButton.setOnClickListener(view -> {
            upField.setText("10");
            topHoldField.setText("5");
            downField.setText("10");
            bottomHoldField.setText("5");
        });

        setButton.setOnClickListener(view -> {
            if(upField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for all up time", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(topHoldField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for top hold time", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(downField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for down time", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(bottomHoldField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for bottom hold time", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                kriyaFragment.setUpTime(upField.getText().toString());
                kriyaFragment.setTopHoldTime(topHoldField.getText().toString());
                kriyaFragment.setDownTime(downField.getText().toString());
                kriyaFragment.setBottomHoldTime(bottomHoldField.getText().toString());
                dismiss();
            }
        });
    }
}
