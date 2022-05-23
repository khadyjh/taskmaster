package com.codeFellow.taskmaster;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddActivity";

    private Handler handler;
    private List<Team> teamsList=new ArrayList<>();
    private List<String> teams=new ArrayList<>();

    Spinner teamSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

//        getDataFromCloud();
        // handler
//        handler=new Handler(Looper.getMainLooper(), msg->{
//            Log.i(TAG, "onCreate: "+msg.getData().get("team")+"*********");
//            teamsList.add((Team) msg.getData().get("team"));
//            teams.add(msg.getData().get("name").toString());
//            return true;
//        });



        EditText titleEditText=findViewById(R.id.taskTitleEditTxt);
        EditText descriptionEditText=findViewById(R.id.taskDescriptionEditTxt);

        Button addTaskButton=findViewById(R.id.addButton);

        Spinner spinner=findViewById(R.id.spinner);

        teamSelector = findViewById(R.id.spinnerTeam);

        spinner();

        // set data to the spinner
//        Spinner teamSpinner=findViewById(R.id.spinnerTeam);
//
//        ArrayAdapter<String> teamAdapter= new ArrayAdapter<>(
//                getBaseContext(),
//                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
//                teams
//        );
//        teamSpinner.setAdapter(teamAdapter);

//        String test=teamSpinner.getSelectedItem().toString();
//        Log.i(TAG, "onCreate: "+ test+"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

                ///////
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast submitted=Toast.makeText(getApplicationContext(),"SUBMITTED",Toast.LENGTH_SHORT);
                submitted.show();

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String state = spinner.getSelectedItem().toString();
                String team = teamSelector.getSelectedItem().toString();


                // method to save task to the backend cloud
                saveDataAmplify(title,description,state,team);

                // save to room code
//                Task task=new Task(title,description,Enum.valueOf(State.class,state));
//                System.out.println("******************************************  "+task);

//               long newTask = AppDatabase.getInstance(getApplicationContext()).taskDao().insertStudent(task);
//
//                Log.i(TAG, "onClick: new task added"+newTask);

                // back to main page after task is saved
                Intent backToMain=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(backToMain);

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


    public void saveDataAmplify(String title, String description , String state,String team){

        // lab 32

        // get the data from ui
//       Task taskAmplify= Task
//                .builder().title(title).description(description).status(state).build();
//
//        // save the data
//        Amplify.DataStore.save(taskAmplify,
//                successful->{
//                    Log.i(TAG, "test: saved");
//                },
//                fail->{
//                    Log.e(TAG, "test: fail to save " );
//                });
//
//        // save to backend
//        Amplify.API.mutate(
//                ModelMutation.create(taskAmplify),
//                success -> Log.i(TAG, "Saved item: " + success.getData()),
//                error -> Log.e(TAG, "Could not save item to API", error)
//        );

        // lab 33
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.contains("t")),
                response -> {
                    for (Team todo : response.getData()) {
                        //
                      if(todo.getName().equals(team)){
                          Task taskAmplify1= Task
                                  .builder().title(title)
                                  .description(description)
                                  .status(state)
                                  .teamListOfTasksId(todo.getId())
                                  .build();

                          // save the data
                          Amplify.DataStore.save(taskAmplify1,
                                  successful->{
//                                      Log.i(TAG, "test: saved");
                                  },
                                  fail->{
//                                      Log.e(TAG, "test: fail to save " );
                                  });

                          // save to backend
                          Amplify.API.mutate(
                                  ModelMutation.create(taskAmplify1),
                                  success -> {
//                                          Log.i(TAG, "Saved item: " + success.getData())
                                  },
                                  error -> {
//                                          Log.e(TAG, "Could not save item to API", error)
                                  }
                          );
                      }
                        //
//                        Log.i("WELCOME", todo.getName()+"55555555555555555555555555555555555");

                    }
                },
                error -> {
//                        Log.e("MyAmplifyApp", "Query failure", error)
                }
        );
    }


    // for handler
    public void getDataFromCloud(){
        // data from local datastore
//        Amplify.DataStore.query(Team.class,
//                teams -> {
//                    while (teams.hasNext()) {
//                        Team team = teams.next();
//                        //
//                        Bundle bundle=new Bundle();
//                        bundle.putString("name",team.getName());
//                        bundle.putParcelable("team",team);
//                        Message message=new Message();
//                        message.setData(bundle);
//                        handler.sendMessage(message);
//                        //
//                        Log.i(TAG, "Name : -> " + team.getName());
//                    }
//                },
//                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
//        );


//        return taskListFromDatabase;

        // data from api
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.contains("t")),
                response -> {
                    for (Team todo : response.getData()) {
                        //
                        Bundle bundle=new Bundle();
                        bundle.putString("name",todo.getName());
                        bundle.putParcelable("team",todo);
                        Message message=new Message();
                        message.setData(bundle);
                        handler.sendMessage(message);
                        //
//                        Log.i("MyAmplifyApp", todo.getName()+"55555555555555555555555555555555555");

                    }
                },
                error -> {
//                        Log.e("MyAmplifyApp", "Query failure", error)
                }
        );

    }

    // configure spinner and git teams data from cloud
    public void spinner(){
        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    for (Team team : response.getData()) {
                        teamsList.add(team);
                    }

                    runOnUiThread(() -> {
                        String[] teamsName = new String[teamsList.size()];

                        for (int i = 0; i < teamsList.size(); i++) {
                            teamsName[i] = teamsList.get(i).getName();
                        }
                        // create adapter
                        ArrayAdapter<String> spinnerAdapterTeam = new ArrayAdapter<String>(
                                this,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                teamsName
                        );

                        // set adapter
                        teamSelector.setAdapter(spinnerAdapterTeam);
                    });
                },
                error -> {
//                        Log.e(TAG, "Query failure", error)
                }
        );
    }
}