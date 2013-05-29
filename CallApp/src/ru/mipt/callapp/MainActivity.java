package ru.mipt.callapp;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private Uri contacts;
	private static Cursor cur;
	private static final String LOG_TAG = "myLogs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	protected void onResume() {
		super.onResume();

	}

	private void getCallDetails() {
		int number = cur.getColumnIndex(CallLog.Calls.NUMBER);
		int type = cur.getColumnIndex(CallLog.Calls.TYPE);
		int date = cur.getColumnIndex(CallLog.Calls.DATE);
		int duration = cur.getColumnIndex(CallLog.Calls.DURATION);
		while (cur.moveToNext()) {
			String phNumber = cur.getString(number);
			String callType = cur.getString(type);
			String callDate = cur.getString(date);
			Date callDayTime = new Date(Long.valueOf(callDate));
			String callDuration = cur.getString(duration);
			Log.d(LOG_TAG, phNumber + " " + callDayTime);
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i1 = new Intent(this, Prefs.class);
			startActivity(i1);
			break;
		}
		return true;
	}

	public int getMinutesCalls(Context context) {
		Time now = new Time();
		now.setToNow();
		contacts = CallLog.Calls.CONTENT_URI;
		String[] proj = new String[] { CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE };
		cur = context.getContentResolver().query(contacts, proj, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
		SharedPreferences sharp = PreferenceManager.getDefaultSharedPreferences(context);
		String phNumber = sharp.getString(context.getString(R.string.phNumber), "+79176035165");
		Date date = new Date(now.year - 1900, now.month, now.monthDay);
		String bil = sharp.getString(context.getString(R.string.billing), "");
		boolean isMinute = true;
		if (bil.contains("per-second"))
			isMinute = false;
		String type = sharp.getString(context.getString(R.string.type), "Incoming");
		int intType = 0;
		if (type.contains("Incoming"))
			intType = CallLog.Calls.INCOMING_TYPE;
		if (type.contains("Outgoing"))
			intType = CallLog.Calls.OUTGOING_TYPE;
		if (type.contains("All"))
			intType = 0;
		int leftSeconds = getTodayCalls(phNumber, date, intType, isMinute);
		return leftSeconds / 60;
	}

	@SuppressWarnings("deprecation")
	public int getTodayCalls(String phNumber, Date date, int type, boolean IsMin) {
		Log.d(LOG_TAG, "date " + date);
		int duration = 0;
		int dateColumn = cur.getColumnIndex(CallLog.Calls.DATE);
		int durationColumn = cur.getColumnIndex(CallLog.Calls.DURATION);
		int number = cur.getColumnIndex(CallLog.Calls.NUMBER);
		int typeColumn = cur.getColumnIndex(CallLog.Calls.TYPE);
		int curDuration;
		while (cur.moveToNext()) {
			Date curDate = new Date(Long.valueOf(cur.getString(dateColumn)));
			if ((date.getDate() == curDate.getDate()) && (date.getDay() == curDate.getDay()) && (date.getMonth() == curDate.getMonth())) {
				if (phNumber.equals(cur.getString(number)))
					if ((type == 0) || (type == Integer.parseInt(cur.getString(typeColumn)))) {
						curDuration = Integer.parseInt(cur.getString(durationColumn));
						if (curDuration > 3) {
							if (IsMin) {
								if (curDuration % 60 == 0)
									duration = duration + curDuration;
								else
									duration = duration + ((curDuration + 60) / 60) * 60;
							} else {
								duration = duration + curDuration;
							}
						}
					}
			}
		}
		return duration;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
