package com.example.dharana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton, registrationButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private boolean loginOrRegistration = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = firebaseAuth -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseFirestore.getInstance().collection("Version").document("version").get().addOnCompleteListener(task -> {
                    if (task.getResult() != null && task.isSuccessful()) {
                        if(loginOrRegistration) {
                            if (task.getResult().get("open").equals(true)) {
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                return;
                            } else if (user.getUid().equals("mRw0elfH1HaIQlDjjNFanHnHYGN2")) {
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                return;
                            } else {
                                Toast.makeText(this, "The app is not available to the public yet. Please wait until the official release of the app.", Toast.LENGTH_LONG);
                                FirebaseAuth.getInstance().signOut();
                            }
                        }
                        else if(task.getResult().get("open").equals(true)) {
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                            return;
                        }
                        else {
                            Toast.makeText(this, "Your account has been registered but the app is not available to the public yet. Please wait until the official release of the app and then login.", Toast.LENGTH_LONG);
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                });
            }
            else {
                setContentView(R.layout.activity_login);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                emailField = (EditText) findViewById(R.id.emailField);

                passwordField = (EditText) findViewById(R.id.passwordField);

                loginButton = (Button) findViewById(R.id.loginButton);
                registrationButton = (Button) findViewById(R.id.registrationButton);

                registrationButton.setOnClickListener(v -> {
                    loginOrRegistration = false;
                    FirebaseAuth.getInstance().signOut();
                    final String email = emailField.getText().toString();
                    final String password = passwordField.getText().toString();
                    if(email.isEmpty())
                        Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG).show();
                    else if(password.isEmpty())
                        Toast.makeText(this, "Please enter a password", Toast.LENGTH_LONG).show();
                    else if(password.length() < 8)
                        Toast.makeText(getApplicationContext(), "Your password must be at least 8 characters",Toast.LENGTH_LONG).show();
                    else {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                            if (!task.isSuccessful()) {
                                if(task.getException() instanceof  FirebaseAuthInvalidCredentialsException)
                                    Toast.makeText(LoginActivity.this, "The email address is invalid", Toast.LENGTH_LONG).show();
                                else if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(LoginActivity.this, "There is already an account associated with this email", Toast.LENGTH_LONG).show();
                                else if(task.getException() instanceof FirebaseAuthWeakPasswordException)
                                    Toast.makeText(LoginActivity.this, "Your password must be at least 8 characters", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(LoginActivity.this, "Sign up error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                loginButton.setOnClickListener(v -> {
                    loginOrRegistration = true;
                    FirebaseAuth.getInstance().signOut();
                    final String email = emailField.getText().toString();
                    final String password = passwordField.getText().toString();
                    if(email.isEmpty())
                        Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG).show();
                    else if(password.isEmpty())
                        Toast.makeText(this, "Please enter a password", Toast.LENGTH_LONG).show();

                    else {
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                            if (!task.isSuccessful()) {
                                if(task.getException() instanceof FirebaseAuthInvalidCredentialsException && ((FirebaseAuthInvalidCredentialsException) task.getException()).getErrorCode().equals("ERROR_WRONG_PASSWORD"))
                                    Toast.makeText(LoginActivity.this, "The entered email and password do not match", Toast.LENGTH_LONG).show();
                                else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException && ((FirebaseAuthInvalidCredentialsException) task.getException()).getErrorCode().equals("ERROR_INVALID_EMAIL"))
                                    Toast.makeText(LoginActivity.this, "The entered email is invalid", Toast.LENGTH_LONG).show();
                                else if(task.getException() instanceof FirebaseAuthInvalidUserException)
                                    Toast.makeText(LoginActivity.this, "This email does not have an associated account", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(LoginActivity.this, "Sign in error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        };

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
