package com.codeFellow.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = TaskDetailActivity.class.getSimpleName();
    TextView mTitle;
    TextView mDescription;
    TextView mState;
    TextView mLong;
    TextView mLat;
    ImageView mImage;
    Button mListen;

    Handler handler;

    private final MediaPlayer mp = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        mTitle=findViewById(R.id.text_view_title);
        mDescription=findViewById(R.id.text_view_lorem);
        mLat=findViewById(R.id.txt_view_lat);
        mLong=findViewById(R.id.txt_view_long);
        mState=findViewById(R.id.text_view_state);
        mImage=findViewById(R.id.imageView3);
        mListen=findViewById(R.id.btn_voice);


        handler=new Handler(Looper.getMainLooper(),msg->{
            Toast.makeText(this, msg.getData().get("sentiment").toString(), Toast.LENGTH_SHORT).show();
            return true;
        });

        Intent titleIntent=getIntent();
        String title=titleIntent.getStringExtra("title");
        String description=titleIntent.getStringExtra("description");
        String state=titleIntent.getStringExtra("state");
        String image=titleIntent.getStringExtra("image");
        double latitude=titleIntent.getDoubleExtra("Latitude",0.0);
        double longitude=titleIntent.getDoubleExtra("Longitude",0.0);
        mTitle.setText(title);
        mDescription.setText(description);
        mState.setText(state);

        //get the image
        if(image!=null){
            Log.i(TAG, "onCreate:=> @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ "+ image);
            Picasso.get().load(image).into(mImage);
        }

        if(latitude!=0.0 && longitude!=0.0){
            mLong.setText(String.valueOf(longitude));
            mLat.setText(String.valueOf(latitude));
        }


        // action bar
        ActionBar actionBar=getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amplify.Predictions.convertTextToSpeech(
                        mDescription.getText().toString(),
                        result -> playAudio(result.getAudioData()),
                        error -> Log.e("MyAmplifyApp", "Conversion failed", error)
                );

                Amplify.Predictions.interpret(
                        mDescription.getText().toString(),
                        result -> {
                            assert result.getSentiment() != null;
                            Log.i("MyAmplifyApp", result.getSentiment().getValue().toString());
                            Bundle bundle=new Bundle();
                            bundle.putString("sentiment",result.getSentiment().getValue().toString());
                            Message message=new Message();
                            message.setData(bundle);
                            handler.sendMessage(message);

                        },
                        error -> Log.e("MyAmplifyApp", "Interpret failed", error)
                );
            }
        });



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


    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }
}