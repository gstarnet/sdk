package com.reconinstruments.demo.hudconnectivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.reconinstruments.os.HUDOS;
import com.reconinstruments.os.connectivity.HUDConnectivityManager;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest.RequestMethod;
import com.reconinstruments.os.connectivity.http.HUDHttpResponse;

public class DownloadFileActivity extends Activity {

    public static TextView mNumberOfRequest;
    public static TextView mNumberOfGoodResponse;
    public static TextView mNumberOfBadResponse;
    public static TextView mCommentTextView;

    public static int mRequestCounter = 0;
    public static int mGoodCounter = 0;
    public static int mBadCounter = 0;

    private HUDConnectivityManager mHUDConnectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.file_view);

        mNumberOfRequest = (TextView) findViewById(R.id.numberOfRequest);
        mNumberOfGoodResponse = (TextView) findViewById(R.id.numberOfGoodResponse);
        mNumberOfBadResponse = (TextView) findViewById(R.id.numberOfBadResponse);
        mCommentTextView = (TextView) findViewById(R.id.commentTextView);

        mHUDConnectivityManager = (HUDConnectivityManager) HUDOS.getHUDService(HUDOS.HUD_CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String url = "http://www.reconinstruments.com/wp-content/themes/recon/img/jet/slide-3.jpg";

        mRequestCounter = 0;
        mGoodCounter = 0;
        mBadCounter = 0;

        mNumberOfRequest.setText("0");
        mNumberOfGoodResponse.setText("0");
        mNumberOfBadResponse.setText("0");

        new DownloadFileTask(url).execute();
    }

    private class DownloadFileTask extends AsyncTask<Void, Void, Boolean> {

        String mUrl;
        String mComment;

        public DownloadFileTask(String url) {
            mUrl = url;
            mRequestCounter ++;
            mNumberOfRequest.setText(Integer.toString(mRequestCounter));
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            try {
                
                //Get Request
                HUDHttpRequest request = new HUDHttpRequest(RequestMethod.GET, mUrl);
                HUDHttpResponse response = mHUDConnectivityManager.sendWebRequest(request);
                if (response.hasBody()) {
                    mComment = "response bodySize:" + response.getBody().length;
                    result = true;
                }
            } catch (Exception e) {
                mComment = "failed to download file: " + e.getMessage();
                e.printStackTrace();
                return false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                mGoodCounter ++;
            }else {
                mBadCounter ++;
            }
            mNumberOfGoodResponse.setText(Integer.toString(mGoodCounter));
            mNumberOfBadResponse.setText(Integer.toString(mBadCounter));
            mCommentTextView.setText(mComment);
        }
    }
}
