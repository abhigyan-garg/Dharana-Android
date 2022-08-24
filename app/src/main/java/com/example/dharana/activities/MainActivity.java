package com.example.dharana.activities;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH;
import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;
import static android.bluetooth.BluetoothGatt.GATT_WRITE_NOT_PERMITTED;
import static android.content.pm.PackageManager.PERMISSION_DENIED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dharana.dialogs.BluetoothDevicesDialog;
import com.example.dharana.fragments.ChakraFragment;
import com.example.dharana.fragments.GuruMantraFragment;
import com.example.dharana.fragments.KriyaFragment;
import com.example.dharana.R;
import com.example.dharana.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private BottomNavigationView bottomNavigationView;
    private MenuItem previousMenuItem;
    private GuruMantraFragment guruMantraFragment;
    private ChakraFragment chakraFragment;
    private KriyaFragment kriyaFragment;
    private boolean bottomMarginSet = false;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevicesDialog bluetoothDevicesDialog;
    private ScanCallback scanCallback;
    private BluetoothGatt gatt;
    BluetoothGattCharacteristic motorOne;
    BluetoothGattCharacteristic motorTwo;
    BluetoothGattCharacteristic motorThree;
    BluetoothGattCharacteristic motorFour;
    BluetoothGattCharacteristic motorFive;
    BluetoothGattCharacteristic motorSix;
    BluetoothGattCharacteristic motorSeven;

    private final int ENABLE_BLUETOOTH_REQUEST_CODE = 1;
    private final int ENABLE_LOCATION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);

        bluetoothDevicesDialog = new BluetoothDevicesDialog(this);
        bluetoothDevicesDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bluetoothDevicesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        linearLayout = findViewById(R.id.linearLayout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ViewCompat.setOnApplyWindowInsetsListener(linearLayout, (v, insets) -> {
            if(!bottomMarginSet) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomNavigationView.getLayoutParams();
                params.bottomMargin = Math.round(insets.getSystemWindowInsetBottom() - 6*getResources().getDisplayMetrics().density);
                bottomNavigationView.setLayoutParams(params);
                bottomMarginSet = true;
            }
            return insets.consumeSystemWindowInsets();
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(previousMenuItem != null)
                    previousMenuItem.setChecked(false);

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                previousMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        kriyaFragment.switchedFrom();
                        chakraFragment.switchedFrom();
                        guruMantraFragment.switchedTo();
                        break;

                    case 1:
                        guruMantraFragment.switchedFrom();
                        chakraFragment.switchedFrom();
                        kriyaFragment.switchedTo();
                        break;
                    case 2:
                        kriyaFragment.switchedFrom();
                        guruMantraFragment.switchedFrom();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.guru);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.guru:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.kriya:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.chakra:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.guru);
        setupViewPager(viewPager);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            promptEnableBluetooth();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_DENIED) {
            promptEnableLocation();
        }

        if(!connected()) {
            scanDevices();
        }
        else {
            for (BluetoothDevice bluetoothDevice: bluetoothAdapter.getBondedDevices()) {
                if(bluetoothDevice.getName().equals("Dharana Device")) {
                    bluetoothDevice.connectGatt(this, true, bluetoothGattCallback);
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        guruMantraFragment = new GuruMantraFragment();
        kriyaFragment = new KriyaFragment();
        chakraFragment = new ChakraFragment(this);
        adapter.addFragment(guruMantraFragment);
        adapter.addFragment(kriyaFragment);
        adapter.addFragment(chakraFragment);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        switch(viewPager.getCurrentItem()) {
            case 0:
                guruMantraFragment.switchedFrom();
                break;
            case 1:
                kriyaFragment.switchedFrom();
                break;
            case 2:
                chakraFragment.switchedFrom();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch(viewPager.getCurrentItem()) {
            case 0:
                if(guruMantraFragment != null)
                    guruMantraFragment.switchedTo();
                break;
            case 1:
                if(kriyaFragment != null)
                    kriyaFragment.switchedTo();
                break;
        }
    }

    private boolean connected() {
        for (BluetoothDevice bluetoothDevice: bluetoothAdapter.getBondedDevices()) {
            if(bluetoothDevice.getName().equals("Dharana")) {
                return true;
            }
        }

        return false;
    }

    private void promptEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, ENABLE_BLUETOOTH_REQUEST_CODE);
        }
    }

    private void promptEnableLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ENABLE_LOCATION_REQUEST_CODE);
        }
    }

    private void scanDevices() {
        ScanFilter scanFilter = new ScanFilter.Builder().setDeviceName("Dharana").build();
        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                bluetoothDevicesDialog.deviceFound();
                stopScanning();
                result.getDevice().connectGatt(MainActivity.this, true, bluetoothGattCallback);
                super.onScanResult(callbackType, result);
            }
        };

        bluetoothAdapter.getBluetoothLeScanner().startScan(new ArrayList<>(Arrays.asList(scanFilter)), scanSettings, scanCallback);
        bluetoothDevicesDialog.show();
    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            MainActivity.this.gatt = gatt;

            if(status == GATT_SUCCESS) {
                if(newState == STATE_CONNECTED) {
                    gatt.discoverServices();
                    bluetoothDevicesDialog.deviceConnected();
                }
                else {
                    bluetoothDevicesDialog.deviceDisconnected();
                    scanDevices();
                    bluetoothDevicesDialog.show();
                }
            }
            else {
                bluetoothDevicesDialog.deviceDisconnected();
                scanDevices();
                bluetoothDevicesDialog.show();
            }
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            for(BluetoothGattCharacteristic characteristic: gatt.getServices().get(2).getCharacteristics()) {
                if(characteristic.getUuid().toString().equals("00002102-0000-1000-8000-00805f9b34fb")) {
                    motorOne = characteristic;
                }
                else if(characteristic.getUuid().toString().equals("00002103-0000-1000-8000-00805f9b34fb")) {
                    motorTwo = characteristic;
                }
                else if(characteristic.getUuid().toString().equals("00002104-0000-1000-8000-00805f9b34fb")) {
                    motorThree = characteristic;
                }
                else if(characteristic.getUuid().toString().equals("00002105-0000-1000-8000-00805f9b34fb")) {
                    motorFour = characteristic;
                }
                else if(characteristic.getUuid().toString().equals("00002106-0000-1000-8000-00805f9b34fb")) {
                    motorFive = characteristic;
                }
                else if(characteristic.getUuid().toString().equals("00002107-0000-1000-8000-00805f9b34fb")) {
                    motorSix = characteristic;
                }
                else if(characteristic.getUuid().toString().equals("00002108-0000-1000-8000-00805f9b34fb")) {
                    motorSeven = characteristic;
                }
            }
            super.onServicesDiscovered(gatt, status);
        }
    };

    public void stopScanning() {
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }

    public void setMotor(int motorNumber, boolean on) {
        BluetoothGattCharacteristic motor;

        switch (motorNumber) {
            case 1:
                motor = motorOne;
                break;
            case 2:
                motor = motorTwo;
                break;
            case 3:
                motor = motorThree;
                break;
            case 4:
                motor = motorFour;
                break;
            case 5:
                motor = motorFive;
                break;
            case 6:
                motor = motorSix;
                break;
            default:
                motor = motorSeven;
                break;
        }

        if(motor != null) {
            motor.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            Integer x;
            if (on) {
                x = 1;
            } else {
                x = 0;
            }
            motor.setValue(new byte[]{x.byteValue()});
            gatt.writeCharacteristic(motor);
        }
    }
}