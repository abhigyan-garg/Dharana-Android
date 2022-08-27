package com.example.dharana.dialogs;

import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;

import com.example.dharana.R;
import com.example.dharana.activities.MainActivity;

public class BluetoothDevicesDialog extends Dialog {
    private final TextView promptTextView;
    private final Button cancelButton;
    private final MainActivity activity;

    public BluetoothDevicesDialog(MainActivity activity) {
        super(activity);
        setContentView(R.layout.dialog_bluetooth_scan);

        this.activity = activity;

        promptTextView = findViewById(R.id.promptTextView);
        cancelButton = findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(v -> {
            activity.stopScanning();
            dismiss();
        });
    }

    public void deviceFound() {
        promptTextView.setText(R.string.device_found);
        cancelButton.setText(R.string.cancel);
    }

    public void deviceConnected() {
        promptTextView.setText(R.string.device_connected);
        cancelButton.setText(R.string.ok);
    }

    public void deviceDisconnected() {
        promptTextView.setText(R.string.device_disconnected);
        cancelButton.setText(R.string.cancel);
    }
}
