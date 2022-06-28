package com.meta.portal.sdk.app.fbns;

import android.content.Context;
import android.content.SharedPreferences;

public class FbnsStorage {
    private static final String FBNS_STORAGE = "FbnsStorage";
    
    public static boolean getFinished(final Context context, final int step) {
        return getSharedPreferences(context).getBoolean(String.valueOf(step), false);
    }

    public static void setFinished(final Context context, final int step, final boolean finished) {
        getSharedPreferences(context).edit().putBoolean(String.valueOf(step), finished).apply();
    }
    
    private static SharedPreferences getSharedPreferences(final Context context) {
        return context.getSharedPreferences(FBNS_STORAGE, Context.MODE_PRIVATE);
    }
}
