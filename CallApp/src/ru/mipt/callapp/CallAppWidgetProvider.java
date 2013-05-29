package ru.mipt.callapp;

import java.sql.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

public class CallAppWidgetProvider extends AppWidgetProvider {
	private static final String MY_SETTINGS = "my_settings";
	private static final String LOG_TAG = "myLogs";
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_layout);
			MainActivity calls = new MainActivity();
			Log.d(LOG_TAG, "leftSeconds " + calls.getMinutesCalls(context));
			views.setTextViewText(R.id.textView1, String.valueOf(60 - calls.getMinutesCalls(context)));
			Intent intent = new Intent(context, CallAppWidgetProvider.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.buttonUpd, pendingIntent);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
