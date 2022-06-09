package com.codeFellow.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;

import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
//import com.codeFellow.taskmaster.data.State;
import com.codeFellow.taskmaster.data.TaskAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


import java.util.ArrayList;
import java.util.List;

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
    private Handler handler2;
    private String mTeam;

    private InterstitialAd mInterstitialAd;

    private RewardedAd mRewardedAd;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserNameView=findViewById(R.id.text_view_my_task);

        handler=new Handler(Looper.getMainLooper(),msg->{

            Log.i(TAG, "onCreate: "+msg.getData().get("team"));
             Team team= (Team) msg.getData().get("team");
             taskList=team.getListOfTasks();
             sitRecyclerView();
             return true;
        });

        handler2=new Handler(Looper.getMainLooper(),msg->{
            mUserNameView.setText(msg.getData().get("name").toString());
            return true;
        });

        setTaskNumber();
        setTeamName();
        costumeTeam();
        authAttribute();



        Button allTaskButton=findViewById(R.id.btnAllTask);
        Button addTaskButton=findViewById(R.id.btnAddTask);
        Button loadADButton=findViewById(R.id.btnAd);
        Button loadRewordedADButton=findViewById(R.id.btnRew);

        TextView rewordTxt=findViewById(R.id.txtViewRew);

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


        loadADButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadInterstitialAd();
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });

        loadRewordedADButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadRewordedAd();
                if (mRewardedAd != null) {
                    Activity activityContext = MainActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            String rewardType = rewardItem.getType();
                            rewordTxt.setText(rewardType);
                        }
                    });
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }

            }
        });



        //banner Ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    ///////////////////////////////////////////////////////lab27////////////////////////////////////



    @Override
    protected void onResume() {
        super.onResume();
        // method to set username
//        setName();
        // method to set team name
        setTeamName();

        //method to set task number
        setTaskNumber();

        costumeTeam();
        sitRecyclerView();
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


    @SuppressLint("SetTextI18n")
    public void setName(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        mUserNameView.setText(sharedPreferences.getString(SettingsActivity.NAME,"My")+" Tasks");
    }

    ///////////////////////////////////////////////////////lab28////////////////////////////////////

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
//                        Bundle bundle=new Bundle();
////                        bundle.putString("title",task.getTitle());
//                        bundle.putParcelable("task",task);
//                        Message message=new Message();
//                        message.setData(bundle);
//                        handler.sendMessage(message);

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
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.eq(mTeam)),
                response -> {
                    Log.i(TAG, "costumeTeam: "+response.getData());
                    for (Team todo : response.getData()) {
                        //
                        Bundle bundle=new Bundle();
//                        bundle.putString("name",todo.getName());
                        bundle.putParcelable("team",todo);
//                        bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) todo.getListOfTasks());
                        Message message=new Message();
                       message.setData(bundle);
                        handler.sendMessage(message);
                        //

//                        taskList=todo.getListOfTasks();
//                        System.out.println(taskList+"===========================================");

                        Log.i(TAG, todo.getListOfTasks()+"^^^^^^^**************************************");

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

                    handler2.sendMessage(message);
                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
    }


    public void loadInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getApplicationContext(), "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            }
        });

    }

    public void loadRewordedAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(TAG, loadAdError.getMessage());
                mRewardedAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                mRewardedAd = rewardedAd;
                Log.d(TAG, "Ad was loaded.");

                mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Ad was shown.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.d(TAG, "Ad failed to show.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.
                        Log.d(TAG, "Ad was dismissed.");
                        mRewardedAd = null;
                    }
                });
            }
        });
    }

}