package br.com.marcelo.mcare.dal;

import android.app.Application;
import android.util.Log;

import br.com.marcelo.mcare.BuildConfig;
import br.com.marcelo.mcare.model.MyObjectBox;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * Created by rogermac on 22/01/18.
 */

public class App extends Application {

    public static final String TAG = "ObjectBoxExample";
    public static final boolean EXTERNAL_DIR = false;

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(App.this).build();

        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(boxStore).start(this);
        }

        Log.d("App", "Using ObjectBox " + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")");
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
