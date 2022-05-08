package com.codeFellow.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String NUMBER = "number";

    EditText mNameEditText;
    EditText mTaskNumEditText;
    Button mSubmitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mNameEditText=findViewById(R.id.edit_text_name);
        mTaskNumEditText=findViewById(R.id.edit_text_task_num);
        mSubmitButton=findViewById(R.id.btn_submit);

        mSubmitButton.setOnClickListener(view -> {
            saveName();
        });

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!mSubmitButton.isEnabled()) {
                    mSubmitButton.setEnabled(true);
                }

                if (editable.toString().length() == 0){
                    mSubmitButton.setEnabled(false);
                }

            }
        });


        // action bar
        ActionBar actionBar=getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // action bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveName(){
        // get the name
        String name=mNameEditText.getText().toString();
        String taskNum=mTaskNumEditText.getText().toString();

        //create sharedPreference object
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor=sharedPreferences.edit();

        preferenceEditor.putString(NAME,name);
        preferenceEditor.putString(NUMBER,taskNum);
        preferenceEditor.apply();

        Toast.makeText(this, "saved"+taskNum, Toast.LENGTH_SHORT).show();
    }
}