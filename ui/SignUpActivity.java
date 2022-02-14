package vn.hust.soict.project.iotcommunication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import vn.hust.soict.project.iotcommunication.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtTxtEmail, edtTxtPassword, edtTxtConfirmPassword, edtTxtName;
    private TextView btnSignUp, txtViewNotification;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUi();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void initUi() {
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        edtTxtConfirmPassword = findViewById(R.id.edtTxtConfirmPassword);
        edtTxtName = findViewById(R.id.edtTxtName);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtViewNotification = findViewById(R.id.txtViewNotification);
        progressDialog = new ProgressDialog(this);
    }

    private void createUser() {
        String email = edtTxtEmail.getText().toString().trim();
        String name = edtTxtName.getText().toString().trim();
        String password = edtTxtPassword.getText().toString().trim();
        String confirmPassword = edtTxtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(name)) {
            txtViewNotification.setText("You must enter all the fields!");
            txtViewNotification.setVisibility(View.VISIBLE);
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtViewNotification.setText("Email invalidate!");
            txtViewNotification.setVisibility(View.VISIBLE);
        } else if (password.length() < 6) {
            txtViewNotification.setText("Password must be at least 6 characters!");
            txtViewNotification.setVisibility(View.VISIBLE);
        } else if (password.compareTo(confirmPassword) != 0) {
            txtViewNotification.setText("Confirm password wrong!");
            txtViewNotification.setVisibility(View.VISIBLE);
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                txtViewNotification.setText("Register failed");
                                txtViewNotification.setVisibility(View.VISIBLE);
                            }
                        }

                    });
        }
    }
}