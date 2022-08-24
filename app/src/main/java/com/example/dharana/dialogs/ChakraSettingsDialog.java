package com.example.dharana.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dharana.DecimalPlaceFilter;
import com.example.dharana.R;
import com.example.dharana.fragments.ChakraFragment;

public class ChakraSettingsDialog extends Dialog {
    private EditText allField;
    private EditText sahasraraField;
    private EditText ajnaField;
    private EditText vishuddiField;
    private EditText anahataField;
    private EditText manipuraField;
    private EditText swadhisthanaField;
    private EditText muladharaField;

    private Button cancelButton;
    private Button setButton;
    private Button resetButton;

    public ChakraSettingsDialog(@NonNull Context context, ChakraFragment chakraFragment) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.dialog_settings_chakra);

        allField = findViewById(R.id.allField);
        sahasraraField = findViewById(R.id.sahasraraField);
        ajnaField = findViewById(R.id.ajnaField);
        vishuddiField = findViewById(R.id.vishuddiField);
        anahataField = findViewById(R.id.anahataField);
        manipuraField = findViewById(R.id.manipuraField);
        swadhisthanaField = findViewById(R.id.swadhisthanaField);
        muladharaField = findViewById(R.id.muladharaField);
        cancelButton = findViewById(R.id.cancelButton);
        setButton = findViewById(R.id.setButton);
        resetButton = findViewById(R.id.resetButton);

        allField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        sahasraraField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        ajnaField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        vishuddiField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        anahataField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        manipuraField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        swadhisthanaField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        muladharaField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});


        cancelButton.setOnClickListener(view -> {
            allField.setText(chakraFragment.getAllTime());
            sahasraraField.setText(chakraFragment.getSahasraraTime());
            ajnaField.setText(chakraFragment.getAjnaTime());
            vishuddiField.setText(chakraFragment.getVishuddiTime());
            anahataField.setText(chakraFragment.getAnahataTime());
            manipuraField.setText(chakraFragment.getManipuraTime());
            swadhisthanaField.setText(chakraFragment.getSwadhisthanaTime());
            muladharaField.setText(chakraFragment.getMuladharaTime());
            dismiss();
        });

        resetButton.setOnClickListener(view -> {
            allField.setText("15");
            sahasraraField.setText("15");
            ajnaField.setText("15");
            vishuddiField.setText("15");
            anahataField.setText("15");
            manipuraField.setText("15");
            swadhisthanaField.setText("15");
            muladharaField.setText("15");
        });

        setButton.setOnClickListener(view -> {
            if(allField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for all chakras", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(sahasraraField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Sahasrara", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(ajnaField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Ajna", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(vishuddiField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Vishuddi", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(anahataField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Anahata", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(manipuraField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Manipura", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(swadhisthanaField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Swadhisthana", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(muladharaField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Muladhara", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                chakraFragment.setAllTime(allField.getText().toString());
                chakraFragment.setSahasraraTime(sahasraraField.getText().toString());
                chakraFragment.setAjnaTime(ajnaField.getText().toString());
                chakraFragment.setVishuddiTime(vishuddiField.getText().toString());
                chakraFragment.setAnahataTime(anahataField.getText().toString());
                chakraFragment.setManipuraTime(manipuraField.getText().toString());
                chakraFragment.setSwadhisthanaTime(swadhisthanaField.getText().toString());
                chakraFragment.setMuladharaTime(muladharaField.getText().toString());
                dismiss();
            }
        });
    }
}
