package com.codeFellow.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();
    public static final String EMAIL = "email";

    EditText mEmail;
    EditText mPassword;
    EditText mNickName;
    Button mSignup;
    ProgressBar mLoadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activety);

        TextView loginLink=findViewById(R.id.text_view_link);

        mEmail=findViewById(R.id.edit_text_email);
        mPassword=findViewById(R.id.edit_text_password);
        mNickName=findViewById(R.id.edit_txt_nick_name);
        mSignup=findViewById(R.id.btn_signup);
//        mLoadingProgressBar =findViewById(R.id.loading);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(mEmail.getText().toString(),mPassword.getText().toString(),mNickName.getText().toString());
            }
        });

        mNickName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE && mEmail.getText().length()!=0 &&
                        mPassword.getText().length()!=0 && mNickName.getText().length()!=0) {
                    mSignup.setEnabled(true);
                    Log.i(TAG, "onEditorAction: in************ ");
                }
                Log.i(TAG, "onEditorAction: logged in ************");
                return false;
            }
        });
    }

    public void signup(String email, String password , String nickName){
        // add as many attributes as you wish
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .userAttribute(AuthUserAttributeKey.nickname(), nickName)
                .build();

        Amplify.Auth.signUp(email, password, options,
                result -> {
                    Log.i(TAG, "Result: " + result.toString());
//                    mLoadingProgressBar.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(SignupActivity.this, VerificationActivity.class);
                    intent.putExtra(EMAIL, email);
                    startActivity(intent);

                    finish();
                },
                error -> {
                    Log.e(TAG, "Sign up failed", error);
                    // show a dialog of the error below
                    // error.getMessage()

                    new AlertDialog.Builder(SignupActivity.this)
                            .setTitle("Error")
                            .setMessage(error.getMessage()).show();
                }
        );
    }


}