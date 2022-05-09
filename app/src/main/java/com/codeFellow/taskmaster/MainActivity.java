package com.codeFellow.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.codeFellow.taskmaster.AddActivity;
import com.codeFellow.taskmaster.data.State;
import com.codeFellow.taskmaster.data.Task;
import com.codeFellow.taskmaster.data.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.CustomClickListener {

//    Button mBtnTask1;
//    Button mBtnTask2;
//    Button mBtnTask3;
    TextView mUserNameView;

    List<Task> taskList=new ArrayList<>();

    int number;

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
                Intent addTaskIntent=new Intent(getApplicationContext(), AddActivity.class);
                startActivity(addTaskIntent);
            }
        });

        ///////////////////////////////////////////////////////lab27////////////////////////////////////
//        mBtnTask1=findViewById(R.id.btn_task_1);
//        mBtnTask2=findViewById(R.id.btn_task_2);
//        mBtnTask3=findViewById(R.id.btn_task_3);


//        mBtnTask1.setOnClickListener(view -> {
//            task1();
//        });
//
//        mBtnTask2.setOnClickListener(view -> {
//            task2();
//        });
//
//        mBtnTask3.setOnClickListener(view -> {
//            task3();
//        });
        ///////////////////////////////////////////////////////lab28////////////////////////////////////

        fitchData();
        RecyclerView recyclerView=findViewById(R.id.recycler_view_task);

        //////
        TaskAdapter taskAdapter=new TaskAdapter(taskList,this,number);

        recyclerView.setAdapter(taskAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    ///////////////////////////////////////////////////////lab27////////////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();
        setName();

        //get task number
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        number= Integer.parseInt(sharedPreferences.getString(SettingsActivity.NUMBER,"2"));
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

//    public void task1(){
//        Intent task1Intent=new Intent(this,TaskDetailActivity.class);
//        task1Intent.putExtra("task1",mBtnTask1.getText().toString());
//        startActivity(task1Intent);
//    }
//
//    public void task2(){
//        Intent task1Intent=new Intent(this,TaskDetailActivity.class);
//        task1Intent.putExtra("task1",mBtnTask2.getText().toString());
//        startActivity(task1Intent);
//    }
//
//    public void task3(){
//        Intent task1Intent=new Intent(this,TaskDetailActivity.class);
//        task1Intent.putExtra("task1",mBtnTask3.getText().toString());
//        startActivity(task1Intent);
//    }

    @SuppressLint("SetTextI18n")
    public void setName(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        mUserNameView.setText(sharedPreferences.getString(SettingsActivity.NAME,"My tasks")+"'s Tasks");


    }

    ///////////////////////////////////////////////////////lab28////////////////////////////////////
    public void fitchData(){
        taskList.add(new Task("task1","hello from task1 ", State.NEW));
        taskList.add(new Task("task2","hello from task2", State.ASSIGNED));
        taskList.add(new Task("task3","hello from task3", State.IN_PROGRESS));
        taskList.add(new Task("task4","hello from task4", State.COMPLETE));
        taskList.add(new Task("task5","hello from task5", State.ASSIGNED));
        taskList.add(new Task("task6","hello from task6", State.NEW));
        taskList.add(new Task("task7","hello from task7", State.NEW));
        taskList.add(new Task("task8","hello from task8", State.NEW));
        taskList.add(new Task("task9","hello from task9", State.NEW));
        taskList.add(new Task("task10","hello from task10", State.NEW));
    }

    @Override
    public void onOneTaskClicked(int position) {
        Intent recycleIntent=new Intent(getApplicationContext(),TaskDetailActivity.class);
        recycleIntent.putExtra("title",taskList.get(position).getTitle());
        recycleIntent.putExtra("description",taskList.get(position).getBody());
        startActivity(recycleIntent);
    }
}