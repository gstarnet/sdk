package com.reconinstruments.demo.hudconnectivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.reconinstruments.os.HUDOS;
import com.reconinstruments.os.connectivity.HUDConnectivityManager;
import com.reconinstruments.os.connectivity.IHUDConnectivity;

public class TestActivity extends Activity implements OnClickListener, IHUDConnectivity {

    private static final String TAG = "TestActivity";

    Button mConnectDevice;
    Button mDownloadFile;
    Button mUploadFile;
    Button mLoadImage;
    Button mHUDConnectedTV;
    Button mLocalWebTV, mRemoteWebTV;

    private HUDConnectivityManager mHUDConnectivityManager = null;

    private final int MRED = 0xFFFF0000;
    private final int MORANGE = 0xFFFF6600;
    private final int MGREEN = 0xFFFFFF00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        mDownloadFile = (Button) findViewById(R.id.download_file_button);
        mUploadFile = (Button) findViewById(R.id.upload_file_button);
        mLoadImage = (Button) findViewById(R.id.load_image_button);
        mConnectDevice = (Button) findViewById(R.id.connected_device);
        mHUDConnectedTV = (Button) findViewById(R.id.hud_connected);
        mLocalWebTV = (Button) findViewById(R.id.local_web_connected);
        mRemoteWebTV = (Button) findViewById(R.id.remote_web_connected);

        mDownloadFile.setOnClickListener(this);
        mUploadFile.setOnClickListener(this);
        mLoadImage.setOnClickListener(this);
        mConnectDevice.setOnClickListener(this);

        mDownloadFile.setEnabled(false);
        mUploadFile.setEnabled(false);
        mLoadImage.setEnabled(false);

        mHUDConnectedTV.setText("HUD Disconnected");
        mHUDConnectedTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MRED));

        mLocalWebTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MRED));
        mRemoteWebTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MRED));

        //Note: This following line is necessary for HUDConnectivityManager to run properly
        System.load("/system/lib/libreconinstruments_jni.so");

        //Get an instance of HUDConnectivityManager
        mHUDConnectivityManager = (HUDConnectivityManager) HUDOS.getHUDService(HUDOS.HUD_CONNECTIVITY_SERVICE);
        if(mHUDConnectivityManager == null){
            Log.e(TAG, "Failed to get HUDConnectivityManager");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        //registering the IHUDConnectivity to HUDConnectivityManager
        mHUDConnectivityManager.register(this);
    }

    @Override
    public void onStop(){
        //unregistering the IHUDConnectivity from HUDConnectivityManager
        mHUDConnectivityManager.unregister(this);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == mLoadImage) {
            intent = new Intent(this, WebImageActivity.class);
            intent.putExtra(WebImageActivity.EXTRA_IMAGE_URL, "http://www.reconinstruments.com/wp-content/themes/recon/img/jet/slide-3.jpg");
            startActivity(intent);
        } else if (v == mDownloadFile) {
            intent = new Intent(this, DownloadFileActivity.class);
            startActivity(intent);
        } else if (v == mUploadFile) {
            intent = new Intent(this, UploadFileActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onConnectionStateChanged(ConnectionState state) {
        Log.d(TAG,"onConnectionStateChanged : state:" + state);
        switch (state) {
            case LISTENING:
                mHUDConnectedTV.setText("HUD Listening");
                mHUDConnectedTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MRED));
                break;
            case CONNECTED:
                mHUDConnectedTV.setText("Phone Connected");
                mHUDConnectedTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MGREEN));
                break;
            case CONNECTING:
                mHUDConnectedTV.setText("Phone Connecting");
                mHUDConnectedTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MORANGE));
                break;
            case DISCONNECTED:
                mHUDConnectedTV.setText("Phone Disconnected");
                mHUDConnectedTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MRED));
                break;
            default:
                Log.e(TAG,"onConnectionStateChanged() with unknown state:" + state);
                break;
        }
    }

    @Override
    public void onNetworkEvent(NetworkEvent networkEvent, boolean hasNetworkAccess) {
        Log.d(TAG, "onNetworkEvent : networkEvent:" + networkEvent + " hasNetworkAccess:" + hasNetworkAccess);
        switch (networkEvent) {
            case LOCAL_WEB_GAINED:
                mLocalWebTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MGREEN));
                break;
            case LOCAL_WEB_LOST:
                mLocalWebTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MRED));
                break;
            case REMOTE_WEB_GAINED:
                mRemoteWebTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MGREEN));
                break;
            case REMOTE_WEB_LOST:
                mRemoteWebTV.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, MRED));
                break;
            default:
                Log.e(TAG,"onNetworkEvent() with unknown networkEvent:" + networkEvent);
                break;
        }
        mDownloadFile.setEnabled(hasNetworkAccess);
        mUploadFile.setEnabled(hasNetworkAccess);
        mLoadImage.setEnabled(hasNetworkAccess);
    }

    @Override
    public void onDeviceName(String deviceName) {
        Log.d(TAG,"onDeviceName : deviceName:" + deviceName);
        mConnectDevice.setText(deviceName);
    }
}
