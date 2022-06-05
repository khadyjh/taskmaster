package com.codeFellow.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
//import com.codeFellow.taskmaster.data.State;
import com.codeFellow.taskmaster.data.TaskAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements TaskAdapter.CustomClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
//        Button mBtnTask1;
//        Button mBtnTask2;
//        Button mBtnTask3;
    TextView mUserNameView;
    RecyclerView mRecyclerView;
    List<Task> taskList=new ArrayList<>();
    List<Task> taskListFromDatabase=new ArrayList<>();
    int number;
    // handler
    private Handler handler;
    private String mTeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserNameView=findViewById(R.id.text_view_my_task);

        // amplify configuration
//        amplifyConfigure();
        //
//        getDataFromCloud();

        authAttribute();
        costumeTeam();
        handler=new Handler(Looper.getMainLooper(),msg->{
//            Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "onCreate: "+msg.getData().get("task"));
//            Log.i(TAG, "onCreate: "+msg.getData().get("list"));
            Log.i(TAG, "onCreate:************************************** "+msg.getData().get("name"));
            mUserNameView.setText((String)msg.getData().get("name"));
            taskListFromDatabase.add((Task) msg.getData().get("task"));

            sitRecyclerView();
            return true;
        });


        //method to set task number
        setTaskNumber();

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
                Intent addTaskIntent=new Intent(getApplicationContext(), AddActivity.class);
                startActivity(addTaskIntent);
            }
        });

//        getDataFromCloud();
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



        // method to set recycler view adapter and to set the data from database




        // create 3 teams
//        test();


        setTeamName();
//        Log.i(TAG, "onCreate: start => " + taskList);


    }

    ///////////////////////////////////////////////////////lab27////////////////////////////////////



    @Override
    protected void onResume() {
        super.onResume();
        // method to set username
        setName();
        // method to set team name
        setTeamName();
        // method to set recycler view adapter and to set the data from database

        //method to set task number
        setTaskNumber();

        costumeTeam();
        sitRecyclerView();

//        Log.i(TAG, "onResume:hello => "+ mTeam +" " + taskList);

        authAttribute();
        handler=new Handler(Looper.getMainLooper(),msg->{
            Log.i(TAG, "onCreate:************************************** "+msg.getData().get("name"));
            mUserNameView.setText((String)msg.getData().get("name"));
            sitRecyclerView();
            return true;
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        setTaskNumber();
        costumeTeam();
        sitRecyclerView();
//        Log.i(TAG, "onStart:start => "+ taskList);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        costumeTeam();
        sitRecyclerView();
//        Log.i(TAG, "onRestart: start => " + taskList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homeminue, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                navigateToSettings();
                return true;
            case R.id.action_copyright:
                Toast.makeText(this, "Copyright 2022", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_log_out:
                logout();
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
        mUserNameView.setText(sharedPreferences.getString(SettingsActivity.NAME,"My")+" Tasks");
    }

    ///////////////////////////////////////////////////////lab28////////////////////////////////////
    public void fitchData(){
//        taskList.add(new Task("task1","hello from task1 ", State.NEW));
//        taskList.add(new Task("task2","hello from task2", State.ASSIGNED));
//        taskList.add(new Task("task3","hello from task3", State.IN_PROGRESS));
//        taskList.add(new Task("task4","hello from task4", State.COMPLETE));
//        taskList.add(new Task("task5","hello from task5", State.ASSIGNED));
//        taskList.add(new Task("task6","hello from task6", State.NEW));
//        taskList.add(new Task("task7","hello from task7", State.NEW));
//        taskList.add(new Task("task8","hello from task8", State.NEW));
//        taskList.add(new Task("task9","hello from task9", State.NEW));
//        taskList.add(new Task("task10","hello from task10", State.NEW));
    }

    @Override
    public void onOneTaskClicked(int position) {
        Intent recycleIntent=new Intent(getApplicationContext(),TaskDetailActivity.class);
        recycleIntent.putExtra("title",taskList.get(position).getTitle());
        recycleIntent.putExtra("description",taskList.get(position).getDescription());
        recycleIntent.putExtra("state",taskList.get(position).getStatus());
        recycleIntent.putExtra("image",taskList.get(position).getImage());
        recycleIntent.putExtra("Latitude",taskList.get(position).getLatitude());
        recycleIntent.putExtra("Longitude",taskList.get(position).getLongitude());
        startActivity(recycleIntent);
    }

    public void sitRecyclerView(){
//        taskList = AppDatabase.getInstance(getApplicationContext()).taskDao().getAll();
        mRecyclerView=findViewById(R.id.recycler_view_task);
        TaskAdapter taskAdapter=new TaskAdapter(taskList,this,number);
        mRecyclerView.setAdapter(taskAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setTaskNumber(){
        //get task number
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        number= Integer.parseInt(sharedPreferences.getString(SettingsActivity.NUMBER,"2"));
    }

    @SuppressLint("SetTextI18n")
    public void setTeamName(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        mTeam = sharedPreferences.getString(SettingsActivity.TEAM_NAME,"team1");
//        Log.i(TAG, "setTeamName: team name =>"+mTeam);
    }

    public void setTeams(){
        //team 1
        // get the data
        Team team=Team.builder().name("team1").build();


        // save the data
        Amplify.DataStore.save(team,
                successful->{
                    Log.i(TAG, "test: saved");
                },
                fail->{
                    Log.e(TAG, "test: fail to save " );
                });

        // save to backend
                Amplify.API.mutate(
                ModelMutation.create(team),
                success -> Log.i(TAG, "Saved item: " + success.getData().getName()),
                error -> Log.e(TAG, "Could not save item to API", error)
        );

        //team 2
        // get the data
        Team team2=Team.builder().name("team2").build();

        // save the data
        Amplify.DataStore.save(team2,
                successful->{
                    Log.i(TAG, "test: saved");
                },
                fail->{
                    Log.e(TAG, "test: fail to save " );
                });

        // save to backend
        Amplify.API.mutate(
                ModelMutation.create(team2),
                success -> Log.i(TAG, "Saved item: " + success.getData().getName()),
                error -> Log.e(TAG, "Could not save item to API", error)
        );

        //team 3
        // get the data
        Team team3=Team.builder().name("team3").build();

        // save the data
        Amplify.DataStore.save(team3,
                successful->{
                    Log.i(TAG, "test: saved");
                },
                fail->{
                    Log.e(TAG, "test: fail to save " );
                });

        // save to backend
        Amplify.API.mutate(
                ModelMutation.create(team3),
                success -> Log.i(TAG, "Saved item: " + success.getData().getName()),
                error -> Log.e(TAG, "Could not save item to API", error)
        );
    }

    public void getDataFromCloud(){
        Amplify.DataStore.query(Task.class,
                tasks -> {
                    while (tasks.hasNext()) {
                        Task task = tasks.next();
                        //
                        Bundle bundle=new Bundle();
//                        bundle.putString("title",task.getTitle());
                        bundle.putParcelable("task",task);
                        Message message=new Message();
                        message.setData(bundle);
                        handler.sendMessage(message);

//                        Log.i(TAG, "Title: " + task.getTitle());
//                        runOnUiThread(()->{
//                            taskListFromDatabase.add(task);
//                        });
                    }
                },
                failure -> Log.e(TAG, "Query failed.", failure)
        );
    }

    public void costumeTeam(){
        if(mTeam==null){
            mTeam="team1";
        }
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.eq(mTeam)),
                response -> {
                    for (Team todo : response.getData()) {
                        //
                      //  Bundle bundle=new Bundle();
                    //    bundle.putString("name",todo.getName());
                     //   bundle.putParcelable("team",todo);
                       // bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) todo.getListOfTasks());
                       // Message message=new Message();
                       //message.setData(bundle);
                     //   handler.sendMessage(message);
                        //

                        taskList=todo.getListOfTasks();
//                        System.out.println(taskList+"===========================================");

                        Log.i(TAG, todo.getName()+"^^^^^^^**************************************");

                    }

                },
                error -> Log.e(TAG, "Query failure", error)

        );
    }

    // Logout AWS
    private void logout() {
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    authSession("logout");
                    finish();
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    private void authSession(String method) {
        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i(TAG, "Auth Session => " + method +" "+ result.toString());
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    // Get Auth Attribute
    private void authAttribute(){
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "User attributes = " + attributes.get(2).getValue());
                    //  Send message to the handler to show the User name >>
                    Bundle bundle = new Bundle();
                    bundle.putString("name",  attributes.get(2).getValue());

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);
                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
    }

}