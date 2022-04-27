package com.codeFellow.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        mTitle=findViewById(R.id.text_view_title);

        Intent titleIntent=getIntent();
        String title=titleIntent.getStringExtra("task1");
        mTitle.setText(title);
    }
}