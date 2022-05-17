package com.codeFellow.taskmaster;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.codeFellow.taskmaster.data.State;
import com.codeFellow.taskmaster.data.Task;
import com.codeFellow.taskmaster.repo.AppDatabase;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        EditText titleEditText=findViewById(R.id.taskTitleEditTxt);
        EditText descriptionEditText=findViewById(R.id.taskDescriptionEditTxt);

        Button addTaskButton=findViewById(R.id.addButton);

        Spinner spinner=findViewById(R.id.spinner);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast submitted=Toast.makeText(getApplicationContext(),"SUBMITTED",Toast.LENGTH_SHORT);
                submitted.show();

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String state = spinner.getSelectedItem().toString();

                // method to save task to the backend cloud
                saveDataAmplify(title,description,state);

                // save to room code
//                Task task=new Task(title,description,Enum.valueOf(State.class,state));
//                System.out.println("******************************************  "+task);

//               long newTask = AppDatabase.getInstance(getApplicationContext()).taskDao().insertStudent(task);
//
//                Log.i(TAG, "onClick: new task added"+newTask);

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


    public void saveDataAmplify(String title, String description , String state){

        // get the data from ui
        com.amplifyframework.datastore.generated.model.Task taskAmplify= com.amplifyframework.datastore.generated.model.Task
                .builder().title(title).description(description).status(state).build();

        // save the data
        Amplify.DataStore.save(taskAmplify,
                successful->{
                    Log.i(TAG, "test: saved");
                },
                fail->{
                    Log.e(TAG, "test: fail to save " );
                });

        // save to backend
        Amplify.API.mutate(
                ModelMutation.create(taskAmplify),
                success -> Log.i(TAG, "Saved item: " + success.getData()),
                error -> Log.e(TAG, "Could not save item to API", error)
        );
    }
}