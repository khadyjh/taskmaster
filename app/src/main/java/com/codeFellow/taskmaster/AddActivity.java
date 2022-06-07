package com.codeFellow.taskmaster;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

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

public class AddActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "AddActivity";
    private static String URL;
    private static final int REQUEST_CODE = 123;

    private Handler handler;
    private List<Team> teamsList=new ArrayList<>();
    private List<String> teams=new ArrayList<>();

    Boolean flag=false;

    Spinner teamSelector;
    Button mUpload;
    Button mLocation;

    private EditText titleEditText;

    FusedLocationProviderClient mFusedLocationClient;

    private int PERMISSION_ID = 44;

    private double latitude;
    private double longitude;

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        titleEditText = findViewById(R.id.taskTitleEditTxt);
        EditText descriptionEditText=findViewById(R.id.taskDescriptionEditTxt);

        Button addTaskButton=findViewById(R.id.addButton);

        Spinner spinner=findViewById(R.id.spinner);

        teamSelector = findViewById(R.id.spinnerTeam);

        mUpload=findViewById(R.id.btn_upload);

        mLocation=findViewById(R.id.btn_location);

        TextView uploadState=findViewById(R.id.textView_upload);

        spinner();

        // need to fix
        if(URL!=null){
            uploadState.setText("image uploaded successfully");
        }

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
            Log.i(TAG, "onCreate: flag " + flag);
        });


        // action bar
        ActionBar actionBar=getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);


        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

        mLocation.setOnClickListener(view -> {
            getLastLocation();
            flag=true;
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


    @Override
    protected void onResume() {
        super.onResume();
        flag=false;

    }

    public void saveDataAmplify(String title, String description , String state, String team){

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
                                  .latitude(latitude)
                                  .longitude(longitude)
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

////////////////////////////////////////////////////////////////////////////////google map /////////////////////////
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){
               mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                   @Override
                   public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                       Location location=task.getResult();
                       if(location==null){
                           requestNewLocationData();
                       }else {
                           latitude=location.getLatitude();
                           longitude=location.getLongitude();

                           Log.i(TAG, "onComplete: latitude ==============================  "+ latitude);
                           Log.i(TAG, "onComplete: longitude ==============================  "+ longitude);
                       }
                   }
               });
            }else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
    }

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
}