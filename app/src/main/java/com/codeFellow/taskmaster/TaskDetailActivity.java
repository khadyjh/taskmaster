package com.codeFellow.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = TaskDetailActivity.class.getSimpleName();
    TextView mTitle;
    TextView mDescription;
    TextView mState;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        mTitle=findViewById(R.id.text_view_title);
        mDescription=findViewById(R.id.text_view_lorem);
        mState=findViewById(R.id.text_view_state);
        mImage=findViewById(R.id.imageView3);

        Intent titleIntent=getIntent();
        String title=titleIntent.getStringExtra("title");
        String description=titleIntent.getStringExtra("description");
        String state=titleIntent.getStringExtra("state");
        String image=titleIntent.getStringExtra("image");
        mTitle.setText(title);
        mDescription.setText(description);
        mState.setText(state);

        //get the image
        if(image!=null){
            Log.i(TAG, "onCreate:=> @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ "+ image);
            Picasso.get().load(image).into(mImage);
        }


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