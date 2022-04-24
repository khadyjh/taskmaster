package com.codeFellow.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button allTaskButton=findViewById(R.id.btnAllTask);
        Button addTaskButton=findViewById(R.id.btnAddTask);

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
    }
}