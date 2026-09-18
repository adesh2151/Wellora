package com.adesh.wellora;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
    name = "StepCounter",
    permissions = { @Permission(alias = "activity", strings = { Manifest.permission.ACTIVITY_RECOGNITION }) }
)
public class StepCounterPlugin extends Plugin implements SensorEventListener {
    private SensorManager sm;
    private Sensor sensor;
    private float latest = -1f;

    @Override
    public void load() {
        sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if (sm != null) sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        registerIfAllowed();
    }

    private boolean hasPerm() {
        if (Build.VERSION.SDK_INT < 29) return true;
        return getPermissionState("activity") == PermissionState.GRANTED;
    }

    private void registerIfAllowed() {
        if (sensor != null && sm != null && hasPerm()) {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if (e.values != null && e.values.length > 0) latest = e.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor s, int a) {}

    @PluginMethod
    public void isAvailable(PluginCall call) {
        JSObject r = new JSObject();
        r.put("available", sensor != null);
        call.resolve(r);
    }

    @PluginMethod
    public void requestPerms(PluginCall call) {
        if (hasPerm()) {
            registerIfAllowed();
            JSObject r = new JSObject();
            r.put("granted", true);
            call.resolve(r);
        } else {
            requestPermissionForAlias("activity", call, "permCb");
        }
    }

    @PermissionCallback
    private void permCb(PluginCall call) {
        registerIfAllowed();
        JSObject r = new JSObject();
        r.put("granted", hasPerm());
        call.resolve(r);
    }

    @PluginMethod
    public void getCumulative(PluginCall call) {
        registerIfAllowed();
        JSObject r = new JSObject();
        r.put("cumulative", (double) latest);
        call.resolve(r);
    }
}
