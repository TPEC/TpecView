package pers.tpec.tpecview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public class ViewManager {
    private Class<? extends AppCompatActivity> activityNow;

    private ViewManager() {
        activityNow = null;
    }

    public void setActivity(@NonNull Class<? extends AppCompatActivity> activity) {
        this.activityNow = activity;
    }

    public void switchActivity(@NonNull Context context, @NonNull Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        this.activityNow = activity;
    }

    private static final ViewManager ourInstance = new ViewManager();

    public static ViewManager getInstance() {
        return ourInstance;
    }
}
