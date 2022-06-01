package com.codeFellow.taskmaster;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddActivity";
    private static String URL;
    private static final int REQUEST_CODE = 123;

    private Handler handler;
    private List<Team> teamsList=new ArrayList<>();
    private List<String> teams=new ArrayList<>();

    Boolean flag=false;

    Spinner teamSelector;
    Button mUpload;

    private EditText titleEditText;


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


        titleEditText = findViewById(R.id.taskTitleEditTxt);
        EditText descriptionEditText=findViewById(R.id.taskDescriptionEditTxt);

        Button addTaskButton=findViewById(R.id.addButton);

        Spinner spinner=findViewById(R.id.spinner);

        teamSelector = findViewById(R.id.spinnerTeam);

        mUpload=findViewById(R.id.btn_upload);

        TextView uploadState=findViewById(R.id.textView_upload);

        spinner();

        // need to fix
        if(URL!=null){
            uploadState.setText("image uploaded successfully");
        }
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



                if(!flag){
                    sharedImg();
                    getImgUrl();
                }

                Log.i(TAG, "onClick: flag" +flag );
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


        //upload image button
        mUpload.setOnClickListener(view -> {
            pictureUpload();
            getImgUrl();
            flag=true;
            Log.i(TAG, "onCreate: flag " +  flag);
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


    @Override
    protected void onResume() {
        flag=false;
        super.onResume();
    }

    public void saveDataAmplify(String title, String description , String state, String team){

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
                                  .image(URL)
                                  .teamListOfTasksId(todo.getId())
                                  .build();

                          Task taskAmplify2= Task
                                  .builder().title(title)
                                  .description(description)
                                  .status(state)
                                  .teamListOfTasksId(todo.getId())
                                  .build();

                          if(URL!=null){
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
                          }else {
                              // save the data
                              Amplify.DataStore.save(taskAmplify2,
                                      successful->{
//                                      Log.i(TAG, "test: saved");
                                      },
                                      fail->{
//                                      Log.e(TAG, "test: fail to save " );
                                      });

                              // save to backend
                              Amplify.API.mutate(
                                      ModelMutation.create(taskAmplify2),
                                      success -> {
//                                          Log.i(TAG, "Saved item: " + success.getData())
                                      },
                                      error -> {
//                                          Log.e(TAG, "Could not save item to API", error)
                                      }
                              );
                          }

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


    private void pictureUpload() {
        // Launches photo picker in single-select mode.
        // This means that the user can select one photo or video.
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            Log.e(TAG, "onActivityResult: Error getting image from device");
            return;
        }

        String title=titleEditText.getText().toString();

        switch(requestCode) {
            case REQUEST_CODE:
                // Get photo picker response for single select.
                Uri currentUri = data.getData();

                // Do stuff with the photo/video URI.
                Log.i(TAG, "onActivityResult: the uri is => " + currentUri);

                try {
                    Bitmap bitmap = getBitmapFromUri(currentUri);

                    File file = new File(getApplicationContext().getFilesDir(), title+".jpg");
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                    // upload to s3
                    // uploads the file
                    Amplify.Storage.uploadFile(
                            title+".jpg",
                            file,
                            result -> Log.i(TAG, "Successfully uploaded: " + result.getKey()),
                            storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
                    );

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
        }

    }

    /*
    https://stackoverflow.com/questions/2169649/get-pick-an-image-from-androids-built-in-gallery-app-programmatically
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }

    private void getImgUrl() {
//        Amplify.Storage.downloadFile(
//                "image.jpg",
//                new File(getApplicationContext().getFilesDir() + "/download.jpg"),
//                result -> {
//                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
//                    Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());
//                },
//                error -> Log.e(TAG,  "Download Failure", error)
//        );

        String title=titleEditText.getText().toString();

        Amplify.Storage.getUrl(
                title+".jpg",
                result -> {
                    Log.i(TAG, "Successfully generated: " + result.getUrl());
                            URL=result.getUrl().toString();
                },
                error -> Log.e(TAG, "URL generation failure", error)
        );
    }



    public void sharedImg(){
//        ImageView img=findViewById(R.id.imageViewtest);

        String title=titleEditText.getText().toString();

        Intent intent=getIntent();
        // the main code line
        /*
        https://medium.com/swlh/sharing-image-to-android-app-using-intent-filter-to-react-native-d112308328d5
         */
        Uri imgUri=intent.getParcelableExtra(Intent.EXTRA_STREAM);

//        if(imgUri!=null){
//            try {
//                InputStream is = getContentResolver().openInputStream(imgUri);
////                img.setImageBitmap(BitmapFactory.decodeStream(is));
//            }catch (FileNotFoundException e){
//                Log.e(TAG, "onCreate: "+e.getMessage() );
//            }
//        }else {
//            Log.e(TAG, "onCreate: out tttttttttttttttttttttttttttttt" );
//        }


        if(imgUri!=null) {
            try {
                Bitmap bitmap = getBitmapFromUri(imgUri);

                File file = new File(getApplicationContext().getFilesDir(), title + ".jpg");
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();

                // upload to s3
                // uploads the file
                Amplify.Storage.uploadFile(
                        title + ".jpg",
                        file,
                        result -> Log.i(TAG, "Successfully uploaded: " + result.getKey()),
                        storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
                );

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}