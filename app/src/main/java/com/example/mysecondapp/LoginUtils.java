package com.example.mysecondapp;

import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;

public class LoginUtils {
    public static boolean login = false;
    public static Runnable callback;

    public static void checkLogin(Context context, Runnable callback) {
        if (login)
            callback.run();
        else {
            LoginUtils.callback = () -> {
                if (login) {
                    callback.run();
                    LoginUtils.callback = null;
                }
            };
            WeakReference<Context> ref = new WeakReference<>(context);
            Context mContext = ref.get();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
}
