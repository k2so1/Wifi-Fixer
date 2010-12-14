/*Copyright [2010] [David Van de Ven]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.wahtod.wififixer;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FixerWidget extends AppWidgetProvider {
    public static final String W_INTENT = "org.wahtod.wififixer.WIDGET";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
	super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
	super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
	super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

	LogService.log(context, LogService.getLogTag(context), intent
		.toString());
	super.onReceive(context, intent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	    int[] appWidgetIds) {

	/*
	 * Send Update To Widgets
	 */
	LogService.log(context, LogService.getLogTag(context), context
		.getString(R.string.widget_update_called));
	context.startService(new Intent(context, UpdateService.class));
	super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static class UpdateService extends IntentService {
	public UpdateService() {
	    super("FixerWidget$UpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	    // Build the widget update for today
	    RemoteViews updateViews = doUpdate(this);

	    // Push update for this widget to the home screen
	    ComponentName thisWidget = new ComponentName(this,
		    FixerWidget.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(this);
	    manager.updateAppWidget(thisWidget, updateViews);

	}

    }

    public static RemoteViews doUpdate(Context context) {

	// Create an Intent to launch the service
	Intent intent = new Intent(W_INTENT);
	PendingIntent pendingIntent = PendingIntent.getBroadcast(context
		.getApplicationContext(), 0, intent, 0);

	// Get the layout for the App Widget and attach an on-click listener
	// to the button
	RemoteViews views = new RemoteViews(context.getPackageName(),
		R.layout.widget);
	views.setOnClickPendingIntent(R.id.Button, pendingIntent);

	return views;
    }
}
