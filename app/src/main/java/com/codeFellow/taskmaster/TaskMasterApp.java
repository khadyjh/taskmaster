package com.codeFellow.taskmaster;

import android.app.Application;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;


public class TaskMasterApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        amplifyConfigure();
    }

    public void amplifyConfigure(){
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());

//            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
//            Log.e(TAG, "Could not initialize Amplify", e);
        }
    }
}
