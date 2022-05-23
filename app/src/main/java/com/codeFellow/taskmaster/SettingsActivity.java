package com.codeFellow.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String NUMBER = "number";
    public static final String TEAM_NAME = "teamName";
    private static final String TAG = SettingsActivity.class.getSimpleName();

    private Handler handler;
    private List<Team> teamsList=new ArrayList<>();
    private List<String> teams=new ArrayList<>();

    EditText mNameEditText;
    EditText mTaskNumEditText;
    Spinner mTeamSpinner;
    Button mSubmitButton;
    Button mSubmitNumberButton;
    Button mSubmitTeamButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mNameEditText=findViewById(R.id.edit_text_name);
        mTaskNumEditText=findViewById(R.id.edit_text_task_num);
        mTeamSpinner=findViewById(R.id.spinnerTeam);
        mSubmitButton=findViewById(R.id.btn_submit);
        mSubmitNumberButton=findViewById(R.id.btn_submit_num);
        mSubmitTeamButton=findViewById(R.id.btn_team);

//        getDataFromCloud();
        // handler
//        handler=new Handler(Looper.getMainLooper(), msg->{
//            Log.i(TAG, "onCreate: "+msg.getData().get("team")+"*********");
//            teamsList.add((Team) msg.getData().get("team"));
//            teams.add(msg.getData().get("name").toString());
//            return true;
//        });
//
//        // set data to the spinner
//        Spinner teamSpinner=findViewById(R.id.spinnerTeam);
//
//        ArrayAdapter<String> teamAdapter= new ArrayAdapter<String>(
//                this,
//                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
//                teams
//        );
//
//        teamAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//        teamSpinner.setAdapter(teamAdapter);


        ///

        mSubmitButton.setOnClickListener(view -> {
            saveName();
        });

        mSubmitNumberButton.setOnClickListener(view -> {
            saveTaskNumber();
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

        mSubmitTeamButton.setOnClickListener(view -> {
            saveTeamName();
        });
        // action bar
        ActionBar actionBar=getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        spinner();
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


        //create sharedPreference object
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor=sharedPreferences.edit();

        preferenceEditor.putString(NAME,name);
        preferenceEditor.apply();

        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    }

    public void saveTaskNumber(){
        // get the number
        String taskNum=mTaskNumEditText.getText().toString();

        //create sharedPreference object
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor=sharedPreferences.edit();

        preferenceEditor.putString(NUMBER,taskNum);
        preferenceEditor.apply();

        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    }

    public void saveTeamName(){
        // get the name
        String teamName=mTeamSpinner.getSelectedItem().toString();


        //create sharedPreference object
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor=sharedPreferences.edit();

        preferenceEditor.putString(TEAM_NAME,teamName);
        preferenceEditor.apply();

        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    }

    public void getDataFromCloud(){
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
                error ->{
//                Log.e("MyAmplifyApp", "Query failure", error)
               }
        );


//        return taskListFromDatabase;

    }


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
                        Spinner stateSelectorTeam = findViewById(R.id.spinnerTeam);

                        // set adapter
                        stateSelectorTeam.setAdapter(spinnerAdapterTeam);
                    });
                },
                error -> {
//                        Log.e(TAG, "Query failure", error)
                }
        );
    }
}