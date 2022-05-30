package com.codeFellow.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = VerificationActivity.class.getSimpleName();
    private EditText verificationCode;
    private Button verifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        Intent intent = getIntent();
        String email = intent.getStringExtra(SignupActivity.EMAIL);

        verificationCode = findViewById(R.id.edit_txt_verification_code);
        verifyButton = findViewById(R.id.btn_verify_button);

        verifyButton.setOnClickListener(view -> {

            verify(verificationCode.getText().toString(),email);
        });
    }



    private void verify(String code, String email) {
        Amplify.Auth.confirmSignUp(
                email,
                code,
                result -> {
                    Log.i(TAG, result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");

                    startActivity(new Intent(VerificationActivity.this, LoginActivity.class));
                    finish();
                },
                error -> Log.e(TAG, error.toString())
        );
    }
}