package com.example.activityrecognition;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognitionClient;


public class MainActivity extends Activity implements
ConnectionCallbacks, OnConnectionFailedListener {
	public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int DETECTION_INTERVAL_SECONDS = 20;
    public static final int DETECTION_INTERVAL_MILLISECONDS = 3000;
    //public static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;
	
    Context mContext;
	TextView tv;
	ActivityRecognitionClient mActivityClient;
	PendingIntent pendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d("ALEC", "Start onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView)findViewById(R.id.textView2);
		mActivityClient = new ActivityRecognitionClient(this, this, this);
		mActivityClient.connect();
		
        Intent intent = new Intent(MainActivity.this, ActivityRecognitionIntentService.class);
        pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);   

        // Alec's added code: Listen to the broadcast and display the status.
        IntentFilter mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent intent) {
						String activityStatus = intent.getExtras().getString(Constants.ACTIVITY_STATUS);
						Log.d("ALEC", "Received activity recognition status: " + activityStatus);					
						tv.setText(SimpleDateFormat.getDateTimeInstance().format(new Date()) + ": " + activityStatus);
					}
                }, mStatusIntentFilter);
        Log.d("ALEC", "End onCreate");
	}
	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data){
//		 // display activity name
//		if (requestCode == 0) {
//			tv.setText(data.getStringExtra("activityName"));
//		}
//	}
	
	public void onConnected(Bundle connectionHint){
        Log.d("ALEC", "onConnected");
		mActivityClient.requestActivityUpdates(DETECTION_INTERVAL_MILLISECONDS, pendingIntent);
		mActivityClient.disconnect();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onDisconnected() {
		mActivityClient = null;
	}
	
	class ActivityBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
		}
	}

}
