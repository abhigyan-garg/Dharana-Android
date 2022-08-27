package com.example.dharana.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dharana.DecimalPlaceFilter;
import com.example.dharana.R;
import com.example.dharana.fragments.ChakraFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ChakraSettingsDialog extends Dialog {
    private LinearLayout allChakrasLayout;
    private LinearLayout individualChakrasLayout;

    private EditText holdField;
    private EditText sahasraraField;
    private EditText ajnaField;
    private EditText vishuddiField;
    private EditText anahataField;
    private EditText manipuraField;
    private EditText swadhisthanaField;
    private EditText muladharaField;
    private EditText allField;

    private Button cancelButton;
    private Button setButton;
    private Button resetButton;

    private SwitchMaterial individualToggle;

    private boolean modifyIndividualChakras = false;

    public ChakraSettingsDialog(@NonNull Context context, ChakraFragment chakraFragment) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.dialog_settings_chakra);

        allChakrasLayout = findViewById(R.id.allChakrasLayout);
        individualChakrasLayout = findViewById(R.id.individualChakrasLayout);
        holdField = findViewById(R.id.holdField);
        sahasraraField = findViewById(R.id.sahasraraField);
        ajnaField = findViewById(R.id.ajnaField);
        vishuddiField = findViewById(R.id.vishuddiField);
        anahataField = findViewById(R.id.anahataField);
        manipuraField = findViewById(R.id.manipuraField);
        swadhisthanaField = findViewById(R.id.swadhisthanaField);
        muladharaField = findViewById(R.id.muladharaField);
        allField = findViewById(R.id.allField);
        cancelButton = findViewById(R.id.cancelButton);
        setButton = findViewById(R.id.setButton);
        resetButton = findViewById(R.id.resetButton);
        individualToggle = findViewById(R.id.individualToggle);

        individualToggle.setChecked(false);
        allChakrasLayout.setVisibility(View.VISIBLE);
        individualChakrasLayout.setVisibility(View.GONE);

        holdField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        sahasraraField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        ajnaField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        vishuddiField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        anahataField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        manipuraField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        swadhisthanaField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        muladharaField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});
        allField.setFilters(new InputFilter[] {new DecimalPlaceFilter()});

        individualToggle.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked) {
                allChakrasLayout.setVisibility(View.GONE);
                individualChakrasLayout.setVisibility(View.VISIBLE);
            }
            else {
                allChakrasLayout.setVisibility(View.VISIBLE);
                individualChakrasLayout.setVisibility(View.GONE);
                sahasraraField.setText(allField.getText());
                ajnaField.setText(allField.getText());
                vishuddiField.setText(allField.getText());
                anahataField.setText(allField.getText());
                manipuraField.setText(allField.getText());
                swadhisthanaField.setText(allField.getText());
                muladharaField.setText(allField.getText());
            }
        });

        cancelButton.setOnClickListener(view -> {
            holdField.setText(chakraFragment.getAllTime());
            sahasraraField.setText(chakraFragment.getSahasraraTime());
            ajnaField.setText(chakraFragment.getAjnaTime());
            vishuddiField.setText(chakraFragment.getVishuddiTime());
            anahataField.setText(chakraFragment.getAnahataTime());
            manipuraField.setText(chakraFragment.getManipuraTime());
            swadhisthanaField.setText(chakraFragment.getSwadhisthanaTime());
            muladharaField.setText(chakraFragment.getMuladharaTime());
            allField.setText(chakraFragment.getMuladharaTime());
            individualToggle.setChecked(modifyIndividualChakras);
            dismiss();
        });

        resetButton.setOnClickListener(view -> {
            holdField.setText("2");
            sahasraraField.setText("2");
            ajnaField.setText("2");
            vishuddiField.setText("2");
            anahataField.setText("2");
            manipuraField.setText("2");
            swadhisthanaField.setText("2");
            muladharaField.setText("2");
            allField.setText("2");
        });

        allField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!modifyIndividualChakras) {
                    sahasraraField.setText(charSequence);
                    ajnaField.setText(charSequence);
                    vishuddiField.setText(charSequence);
                    anahataField.setText(charSequence);
                    manipuraField.setText(charSequence);
                    swadhisthanaField.setText(charSequence);
                    muladharaField.setText(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        muladharaField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (modifyIndividualChakras) {
                    allField.setText(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setButton.setOnClickListener(view -> {
            modifyIndividualChakras = individualToggle.isChecked();
            if(holdField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for all chakras", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(modifyIndividualChakras && sahasraraField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Sahasrara", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(modifyIndividualChakras && ajnaField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Ajna", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(modifyIndividualChakras && vishuddiField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Vishuddi", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(modifyIndividualChakras && anahataField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Anahata", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(modifyIndividualChakras && manipuraField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Manipura", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(modifyIndividualChakras && swadhisthanaField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Swadhisthana", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(modifyIndividualChakras && muladharaField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for Muladhara", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(!modifyIndividualChakras && allField.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getContext(), "Please enter the number of seconds for the chakras", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                chakraFragment.setAllTime(holdField.getText().toString());
                if(modifyIndividualChakras) {
                    chakraFragment.setSahasraraTime(sahasraraField.getText().toString());
                    chakraFragment.setAjnaTime(ajnaField.getText().toString());
                    chakraFragment.setVishuddiTime(vishuddiField.getText().toString());
                    chakraFragment.setAnahataTime(anahataField.getText().toString());
                    chakraFragment.setManipuraTime(manipuraField.getText().toString());
                    chakraFragment.setSwadhisthanaTime(swadhisthanaField.getText().toString());
                    chakraFragment.setMuladharaTime(muladharaField.getText().toString());
                }
                else {
                    chakraFragment.setSahasraraTime(allField.getText().toString());
                    chakraFragment.setAjnaTime(allField.getText().toString());
                    chakraFragment.setVishuddiTime(allField.getText().toString());
                    chakraFragment.setAnahataTime(allField.getText().toString());
                    chakraFragment.setManipuraTime(allField.getText().toString());
                    chakraFragment.setSwadhisthanaTime(allField.getText().toString());
                    chakraFragment.setMuladharaTime(allField.getText().toString());
                }
                dismiss();
            }
        });
    }
}
