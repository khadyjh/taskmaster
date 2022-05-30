package com.codeFellow.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.amplifyframework.core.Amplify;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText email;
    EditText password;
    Button login;

    ImageView img;
    ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email=findViewById(R.id.edit_text_email_log_in);
        password=findViewById(R.id.edit_text_password_login);
        login=findViewById(R.id.btn_log_in);

//        mLoadingProgressBar=findViewById(R.id.loading);

        img=findViewById(R.id.imageView);

        Picasso.get().load("https://www.industrialempathy.com/img/remote/ZiClJf-1920w.jpg").into(img);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login(email.getText().toString(),password.getText().toString());
            }
        });

    }

    public void login(String email, String password){
        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    Log.i(TAG, result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
//                    mLoadingProgressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                },
                error -> Log.e(TAG, error.toString())
        );
    }
}