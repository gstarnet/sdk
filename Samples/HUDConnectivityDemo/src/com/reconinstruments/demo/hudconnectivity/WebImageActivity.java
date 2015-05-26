package com.reconinstruments.demo.hudconnectivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.reconinstruments.os.connectivity.http.HUDHttpRequest;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest.RequestMethod;
import com.reconinstruments.os.connectivity.http.HUDHttpResponse;

public class WebImageActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

    public static String EXTRA_IMAGE_URL = "IMAGE_URL";

    ImageView mImage = null;
    int mDialogRefCnt = 0;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Downloading...");
        setContentView(R.layout.image_view);
        mImage = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString(EXTRA_IMAGE_URL);
            new DownloadHUDImageTask(mImage).execute(url);
        } else {
            Log.e(TAG, "No url was received from Intent");
        }
    }

    private class DownloadHUDImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadHUDImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            synchronized (this) {
                if (mDialogRefCnt == 0) {
                    mDialog.show();
                }
                mDialogRefCnt++;
            }
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;

            try {
                //Http Get Request
                HUDHttpRequest request = new HUDHttpRequest(RequestMethod.GET, urlDisplay);
                HUDHttpResponse response = TestActivity.mHUDConnectivityManager.sendWebRequest(request);
                if (response.hasBody()) {
                    byte[] data = response.getBody();
                    mIcon11 = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            synchronized (this) {
                if (mDialogRefCnt <= 1) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mDialogRefCnt = 0;
                } else {
                    mDialogRefCnt--;
                }
            }
        }
    }
}
