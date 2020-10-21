package com.logicwind.darkmode;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.provider.Settings;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

public class DarkmodeModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private final ReactApplicationContext reactContext;
    private static final String DARK = "DARK";
    private static final String LIGHT = "LIGHT";
    private static final String INITIAL_MODE_KEY = "initialMode";
    private static final String SUPPORTS_DARK_MODE_KEY = "supportsDarkMode";
    private static final String ON_MODE_CHANGE_KEY = "onModeChange";

    //OxygenOS string
    private static final String OEM_BLACK_MODE = "oem_black_mode";

    private int initialMode;
    private int oxygenOsTheme;
    private int recentMode;

    public DarkmodeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        initialMode = reactContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        oxygenOsTheme = getOxygenOSTheme(OEM_BLACK_MODE);
        if(oxygenOsTheme == 1){
            initialMode = Configuration.UI_MODE_NIGHT_YES;
        }
        recentMode = initialMode;

        reactContext.addLifecycleEventListener(this);
        reactContext.registerReceiver(new Receiver(this), new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"));
    }

    @Override
    public String getName() {
        return "DarkMode";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(INITIAL_MODE_KEY, initialMode == Configuration.UI_MODE_NIGHT_YES ? DARK : LIGHT);
        constants.put(SUPPORTS_DARK_MODE_KEY, initialMode != Configuration.UI_MODE_NIGHT_UNDEFINED);
        return constants;
    }

    private class Receiver extends BroadcastReceiver {
        private DarkmodeModule module;

        public Receiver(DarkmodeModule module) {
            super();
            this.module = module;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            this.module.notifyConfigChange();
        }
    }

    private void notifyConfigChange() {
        if (reactContext.hasActiveCatalystInstance()) {
            Configuration configuration = reactContext.getResources().getConfiguration();
            int currentMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentMode == recentMode){
                return;
            }
            recentMode = currentMode;
            String emittedMode = currentMode == Configuration.UI_MODE_NIGHT_YES ? DARK : LIGHT;
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(ON_MODE_CHANGE_KEY, emittedMode);
        }
    }

    @Override
    public void onHostResume() {
        this.notifyConfigChange();
    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }

    /**
     *  OxygenOS Theme status
     * @param setting
     * @return Theme type - int
     * Theme
     * 0 = light
     * 1 = dark
     * 2 = default
     */
    public int getOxygenOSTheme(String setting) {
        ContentResolver resolver = reactContext.getContentResolver();
        try {
            return Settings.System.getInt(resolver, setting);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
