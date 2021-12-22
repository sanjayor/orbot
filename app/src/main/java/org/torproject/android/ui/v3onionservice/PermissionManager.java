package org.torproject.android.ui.v3onionservice;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.torproject.android.R;

public class PermissionManager {

    public static boolean isLollipopOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestBatteryPermissions(FragmentActivity activity) {
        final String packageName = activity.getPackageName();
        PowerManager pm = (PowerManager) activity.getApplicationContext().getSystemService(Context.POWER_SERVICE);

        if (pm.isIgnoringBatteryOptimizations(packageName))
            return;

        Snackbar.make(activity.findViewById(android.R.id.content),
                R.string.consider_disable_battery_optimizations,
                BaseTransientBottomBar.LENGTH_INDEFINITE).setAction(R.string.disable,
                v -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    activity.startActivity(intent);
                }).show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestDropBatteryPermissions(FragmentActivity activity) {
        PowerManager pm = (PowerManager) activity.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(activity.getPackageName()))
            return;

        Snackbar.make(activity.findViewById(android.R.id.content),
                R.string.consider_enable_battery_optimizations,
                BaseTransientBottomBar.LENGTH_INDEFINITE).setAction(R.string.enable,
                v -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    activity.startActivity(intent);
                }).show();
    }
}

