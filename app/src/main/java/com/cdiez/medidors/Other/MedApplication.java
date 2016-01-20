package com.cdiez.medidors.Other;

import android.app.Application;

import com.cdiez.medidors.Data.Lectura;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Carlos Diez
 * on 18/01/2016.
 */
public class MedApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Lectura.class);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}