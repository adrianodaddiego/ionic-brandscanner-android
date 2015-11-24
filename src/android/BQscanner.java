package cn.bqmart.plugin.bqscanner;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;


/**
 * 码枪
 * V1.2
 */
public class BQscanner extends CordovaPlugin {
    public static android.device.ScanDevice sScanDevice = new android.device.ScanDevice();
    private static final String SCAN_ACTION = "scan.rcv.message";
    private static final float BEEP_VOLUME = 0.10f;
    private static boolean sVisible = false;

    private String barcodeResult;
    private MediaPlayer mMediaPlayer;
    private CallbackContext mCallbackContext;
    private boolean flag_playBeep;

    private final BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            barcodeResult = new String(barocode, 0, barocodelen);
            android.util.Log.e("barcodeResult", "----codetype:" + temp + "--barcodeResult:" + barcodeResult);
            sScanDevice.stopScan();
            transmitReceive(barcodeResult);
            playBeepSound();
            coolMethod(barcodeResult, mCallbackContext);

        }
    };

    private final MediaPlayer.OnCompletionListener mBeepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        this.mCallbackContext = callbackContext;
        String message = args.getString(0);
        if (!android.text.TextUtils.isEmpty(message)) {
        }
        if (action.equals("registerReceiver")) {
            sVisible = true;
            initBeepSound();
            sScanDevice.setOutScanMode(0);
            registerReceiver();
            this.coolMethod("1", callbackContext);
            return true;
        } else if (action.equals("unRegisterReceiver")) {
            unRegisterReceiver();
            this.coolMethod("1", callbackContext);
            return true;
        } else if (action.equals("stop")) {
            sScanDevice.stopScan();
            return true;
        } else if (action.equals("start")) {
            sScanDevice.startScan();
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (!sVisible) return;
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    /**
     * Called after plugin construction and fields have been initialized.
     */
    protected void pluginInitialize() {
    }

    /**
     * 取消注册
     */
    protected void unRegisterReceiver() {
        if (sScanDevice != null) {
            sScanDevice.stopScan();
        }
        cordova.getActivity().unregisterReceiver(mScanReceiver);
    }

    /**
     * 注册
     */
    protected void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        cordova.getActivity().registerReceiver(mScanReceiver, filter);
    }

    /**
     * @param msg
     */
    void transmitReceive(String msg) {
        String js = String
                .format("window.plugins.bqscanner.receiveMessageInAndroidCallback('%s');",
                        msg);
        try {
            webView.sendJavascript(js);
        } catch (NullPointerException e) {

        } catch (Exception e) {

        }
    }

    /**
     * 初始化本地音频 void
     */
    private void initBeepSound() {
        flag_playBeep = true;
        AudioManager audioService = (AudioManager) cordova.getActivity().getSystemService(Activity.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            flag_playBeep = false;
        }
        if (mMediaPlayer == null) {
            cordova.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(mBeepListener);
            int resId = getResourceId(cordova.getActivity(), "beep", "raw");
            if (resId == 0) {
                return;
            }
            AssetFileDescriptor file = cordova.getActivity().getResources().openRawResourceFd(resId);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                        file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    /**
     *
     */
    void playBeepSound() {
        if (flag_playBeep && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    public void onStart() {
        sVisible = true;
        initBeepSound();
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    public void onStop() {
        sVisible = false;
        //Toast.makeText(cordova.getActivity(), sVisible + "onStop", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @param name
     * @param type
     * @return
     */
    private static int getResourceId(Context context, String name, String type) {
        Resources themeResources;
        PackageManager pm = context.getPackageManager();
        try {
            themeResources = pm.getResourcesForApplication(context
                    .getPackageName());
            return themeResources.getIdentifier(name, type,
                    context.getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
