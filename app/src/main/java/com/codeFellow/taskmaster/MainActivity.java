package com.codeFellow.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mBtnTask1;
    Button mBtnTask2;
    Button mBtnTask3;
    TextView mUserNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button allTaskButton=findViewById(R.id.btnAllTask);
        Button addTaskButton=findViewById(R.id.btnAddTask);

        mUserNameView=findViewById(R.id.text_view_my_task);

        allTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent allTaskIntent=new Intent(getApplicationContext(),ThirdActivity.class);
                startActivity(allTaskIntent);
            }
        });


        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent=new Intent(getApplicationContext(),AddActivity.class);
                startActivity(addTaskIntent);
            }
        });

        ///////////////////////////////////////////////////////lab27////////////////////////////////////
        mBtnTask1=findViewById(R.id.btn_task_1);
        mBtnTask2=findViewById(R.id.btn_task_2);
        mBtnTask3=findViewById(R.id.btn_task_3);


        mBtnTask1.setOnClickListener(view -> {
            task1();
        });

        mBtnTask2.setOnClickListener(view -> {
            task2();
        });

        mBtnTask3.setOnClickListener(view -> {
            task3();
        });
    }

    ///////////////////////////////////////////////////////lab27////////////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();
        setName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homeminue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                navigateToSettings();
                return true;
            case R.id.action_copyright:
                Toast.makeText(this, "Copyright 2022", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void  navigateToSettings(){
        Intent settingIntent=new Intent(this,SettingsActivity.class);
        startActivity(settingIntent);
    }

    public void task1(){
        Intent task1Intent=new Intent(this,TaskDetailActivity.class);
        task1Intent.putExtra("task1",mBtnTask1.getText().toString());
        startActivity(task1Intent);
    }

    public void task2(){
        Intent task1Intent=new Intent(this,TaskDetailActivity.class);
        task1Intent.putExtra("task1",mBtnTask2.getText().toString());
        startActivity(task1Intent);
    }

    public void task3(){
        Intent task1Intent=new Intent(this,TaskDetailActivity.class);
        task1Intent.putExtra("task1",mBtnTask3.getText().toString());
        startActivity(task1Intent);
    }

    @SuppressLint("SetTextI18n")
    public void setName(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        mUserNameView.setText(sharedPreferences.getString(SettingsActivity.NAME,"My tasks")+"'s Tasks");
    }
}