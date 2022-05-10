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



                Task task=new Task(title,description,Enum.valueOf(State.class,state));
                System.out.println("******************************************  "+task);

               long newTask = AppDatabase.getInstance(getApplicationContext()).taskDao().insertStudent(task);

                Log.i(TAG, "onClick: new task added"+newTask);

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
}