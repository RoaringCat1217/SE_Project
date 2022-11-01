package com.example.mysecondapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

public class LoginUtils {
    private static boolean login = false;
    private static int reqCode = 0;
    private static SparseArray<Runnable> activityMap = new SparseArray<>();

    public static boolean checkForLogin(Activity activity, Runnable callback) {
        if (login)
            return true;
        activityMap.put(reqCode, callback);
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, reqCode);
        reqCode++;
        return false;
    }

    public static void loginSuccess(int reqCode) {
        login = true;
        Runnable callback = activityMap.get(reqCode);
        activityMap.remove(reqCode);
        callback.run();
    }
}
