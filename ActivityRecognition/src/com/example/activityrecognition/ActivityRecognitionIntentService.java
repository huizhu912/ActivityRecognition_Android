package com.example.activityrecognition;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionIntentService extends IntentService {

	public ActivityRecognitionIntentService() {
		super("ActivityRecognitionIntentService");
	}

	public ActivityRecognitionIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// If the incoming intent contains an update
		if (ActivityRecognitionResult.hasResult(intent)) {
			// Get the update
			ActivityRecognitionResult result =
					ActivityRecognitionResult.extractResult(intent);
			// Get the most probable activity
			DetectedActivity mostProbableActivity =
					result.getMostProbableActivity();
			/*
			 * Get the probability that this activity is the
			 * the user's actual activity
			 */
			int confidence = mostProbableActivity.getConfidence();
			/*
			 * Get an integer describing the type of activity
			 */
			int activityType = mostProbableActivity.getType();
			String activityName = getNameFromType(activityType);

			// Alec's added code: Send the local broadcast
			Log.d("ALEC", "Sending activity recognition status: " + activityName);
			Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
					.putExtra(Constants.ACTIVITY_STATUS, activityName);
			LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
		} else {
			/*
			 * This implementation ignores intents that don't contain
			 * an activity update. If you wish, you can report them as
			 * errors.
			 */
		}
	}

	private String getNameFromType(int activityType) {
		switch(activityType) {
		case DetectedActivity.IN_VEHICLE:
			return "in_vehicle";
		case DetectedActivity.ON_BICYCLE:
			return "on_bicycle";
		case DetectedActivity.ON_FOOT:
			return "on_foot";
		case DetectedActivity.STILL:
			return "still";
		case DetectedActivity.UNKNOWN:
			return "unknown";
		case DetectedActivity.TILTING:
			return "tilting";
		}
		return "unknown";
	}
}
