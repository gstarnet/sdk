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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadFileActivity extends Activity {

    public static final int UPLOAD_BYTE_SIZE = 10000;

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

        byte[] data = new byte[UPLOAD_BYTE_SIZE];
        Arrays.fill(data, "b".getBytes()[0]);
        String url = "http://httpbin.org/post";

        mRequestCounter = 0;
        mGoodCounter = 0;
        mBadCounter = 0;

        new UploadFileTask(data, url).execute();
    }

    private class UploadFileTask extends AsyncTask<String, Void, Boolean> {

        byte[] mData;
        String mUrl;
        String mComment;

        public UploadFileTask(byte[] data, String url) {
            mUrl = url;
            mData = data;
            mRequestCounter++;
            mNumberOfRequest.setText(Integer.toString(mRequestCounter));
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            boolean result = false;
            try {

                //Here is an example where you can add your header fields 
                Map<String, List<String>> headers = new HashMap<String, List<String>>();
                List<String> aList = new ArrayList<String>();
                aList.add("text");
                headers.put("Content-Type", aList);

                //Http Post Request
                HUDHttpRequest request = new HUDHttpRequest(RequestMethod.POST, new URL(mUrl), headers, mData);
                HUDHttpResponse response = mHUDConnectivityManager.sendWebRequest(request);
                
                if (response.hasBody()) {
                    // the following code confirms we uploaded correctly, not useful
                    byte[] data = response.getBody();
                    String dataString = new String(data);
                    int start = dataString.indexOf("\"data\": ") + 9;
                    dataString = dataString.substring(start);
                    int end = dataString.indexOf("\"");
                    dataString = dataString.substring(0, end);

                    if (dataString.length() == mData.length) {
                        mComment = "Successful uploading " + dataString.length() + " bytes";
                        result = true;
                    }
                    else {
                        mComment = "Error uploading data";
                    }
                }
            } catch (Exception e) {
                mComment = "Error uploading data :" + e.getMessage();
                e.printStackTrace();
                return false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                mGoodCounter++;
            } else {
                mBadCounter++;
            }
            mNumberOfGoodResponse.setText(Integer.toString(mGoodCounter));
            mNumberOfBadResponse.setText(Integer.toString(mBadCounter));
            mCommentTextView.setText(mComment);
        }
    }
}
