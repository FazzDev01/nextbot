package com.nextbot.termux;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class TermuxRunner extends CordovaPlugin {
    private static final String PERM = "com.termux.permission.RUN_COMMAND";
    private static final int REQ = 7001;
    private CallbackContext pendingCb;
    private String pendingCmd;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext cb) throws JSONException {
        if (!"run".equals(action)) return false;
        String cmd = args.getString(0);
        if (cordova.hasPermission(PERM)) {
            doRun(cmd, cb);
        } else {
            pendingCb = cb;
            pendingCmd = cmd;
            cordova.requestPermission(this, REQ, PERM);
        }
        return true;
    }

    @Override
    public void onRequestPermissionResult(int code, String[] perms, int[] results) throws JSONException {
        if (code != REQ || pendingCb == null) return;
        CallbackContext cb = pendingCb;
        String cmd = pendingCmd;
        pendingCb = null;
        pendingCmd = null;
        if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) doRun(cmd, cb);
        else cb.error("PERMISSION_DENIED");
    }

    private void doRun(String cmd, CallbackContext cb) {
        try {
            Context ctx = cordova.getActivity().getApplicationContext();
            Intent i = new Intent("com.termux.RUN_COMMAND");
            i.setClassName("com.termux", "com.termux.app.RunCommandService");
            i.putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash");
            i.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", new String[]{"-c", cmd});
            i.putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home");
            i.putExtra("com.termux.RUN_COMMAND_BACKGROUND", true);
            ComponentName c = (Build.VERSION.SDK_INT >= 26) ? ctx.startForegroundService(i) : ctx.startService(i);
            if (c == null) cb.error("TERMUX_NOT_FOUND");
            else cb.success("OK");
        } catch (Exception e) {
            cb.error(String.valueOf(e.getMessage()));
        }
    }
}
