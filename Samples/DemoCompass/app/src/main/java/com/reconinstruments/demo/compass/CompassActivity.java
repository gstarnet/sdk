package com.reconinstruments.demo.compass;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.reconinstruments.os.HUDOS;
import com.reconinstruments.os.hardware.sensors.HUDHeadingManager;
import com.reconinstruments.os.hardware.sensors.HeadLocationListener;

public class CompassActivity extends Activity implements HeadLocationListener {
	private final String TAG = this.getClass().getSimpleName();

	private HUDHeadingManager mHUDHeadingManager = null;
	
	private static double PIXELS_PER_45_DEGREES = 190.0;

	float mUserHeading = 0.0f;
	ImageView mCompassBar = null;
	ImageView mCompassUnderline = null;

	boolean mIsResumed = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"onCreate");
		super.onCreate(savedInstanceState);

		mHUDHeadingManager = (HUDHeadingManager) HUDOS.getHUDService(HUDOS.HUD_HEADING_SERVICE);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_compass);

		mCompassBar = (ImageView) findViewById(R.id.compass_bar);
		mCompassUnderline = (ImageView) findViewById(R.id.compass_underline);

		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.postTranslate(-428, 0);

		mCompassBar.setScaleType(ScaleType.MATRIX);
		mCompassBar.setImageMatrix(matrix);
		
	}

	@Override
	public void onResume() {
		Log.i(TAG,"onResume");

		super.onResume();

		mHUDHeadingManager.register(this);

		mIsResumed = true;
	}

	@Override
	public void onPause()  {
		Log.d(TAG,"onPause");
		
		mHUDHeadingManager.unregister(this);

		mIsResumed = false;

		super.onPause();
	}

	@Override
	public void onDestroy(){
		Log.d(TAG,"onDestroy");

		super.onDestroy();
	}

	@Override
	public void onHeadLocation(float yaw, float pitch, float roll) {
		if(!this.mIsResumed || (Float.isNaN(yaw))) {
			return;
		}

		float newHeading = yaw;

		if(mUserHeading > 270.0f && newHeading < 90.0f) {
			mUserHeading = mUserHeading - 360.0f;// avoid aliasing in average when crossing North (angle = 0.0)
		} else if (mUserHeading < 90.0f && newHeading > 270.0f) {
			newHeading = newHeading - 360.0f; // avoid aliasing in average when crossing North (angle = 0.0)
		}

		mUserHeading = (float) ((4.0*mUserHeading + newHeading)/5.0); // smooth heading

		if(mUserHeading < 0.0f) mUserHeading += 360.0f;
		if(mUserHeading > 360.0f) mUserHeading -= 360.0f;


		int offset = (mUserHeading >= 315f && mUserHeading <= 360) ? -(int)PIXELS_PER_45_DEGREES*7 : (int)PIXELS_PER_45_DEGREES;
		int x = (int)(mUserHeading / 360.0 * (8.0*PIXELS_PER_45_DEGREES)) + offset; // TODO: What is this?

		mCompassBar.getImageMatrix().reset();
		mCompassBar.getImageMatrix().postTranslate(-x, 0);
		mCompassBar.invalidate(); // TODO: find out if we need this
	}
}
