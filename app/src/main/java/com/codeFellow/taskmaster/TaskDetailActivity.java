package com.codeFellow.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {
    TextView mTitle;
    TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        mTitle=findViewById(R.id.text_view_title);
        mDescription=findViewById(R.id.text_view_lorem);

        Intent titleIntent=getIntent();
        String title=titleIntent.getStringExtra("title");
        String description=titleIntent.getStringExtra("description");
        mTitle.setText(title);
        mDescription.setText(description);


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