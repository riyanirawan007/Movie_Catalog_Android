package app.android.rynz.imoviecatalog.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.util.ExtraKeys;
import app.android.rynz.imoviecatalog.util.lib.PendingFragment;
import app.android.rynz.imoviecatalog.view.ui.DetailMovieFragment;
import app.android.rynz.imoviecatalog.view.ui.HomeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider
{
    public static final String ACTION_OPEN_DETAIL="app.android.rynz.imoviecatalog.WIDGET_FAV_ACTION_OPEN_DETAIL";
    public static final String EXTRA_ITEM_ID ="app.android.rynz.imoviecatalog.WIDGET_FAV_EXTRA_ITEM_ID";
    public static final String EXTRA_ITEM_TITLE ="app.android.rynz.imoviecatalog.WIDGET_FAV_EXTRA_ITEM_TITLE";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId)
    {

        //Service Intent
        Intent service=new Intent(context,FavoriteWidgetService.class);
        service.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        service.setData(Uri.parse(service.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        appWidgetManager.updateAppWidget(appWidgetId,null);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setEmptyView(R.id.sv_widget_fav,R.id.tv_widget_fav_empty);
        views.setRemoteAdapter(R.id.sv_widget_fav,service);
        appWidgetManager.updateAppWidget(appWidgetId,views);


        //Action Toast From Broadcast
        Intent actionIntent=new Intent(context,FavoriteWidget.class);
        actionIntent.setAction(FavoriteWidget.ACTION_OPEN_DETAIL);
        actionIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        actionIntent.setData(Uri.parse(actionIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent= PendingIntent.getBroadcast(context,7,actionIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.sv_widget_fav,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void notifyWidgetFavoriteChanged(@NonNull Context context)
    {
        AppWidgetManager widgetManager=AppWidgetManager.getInstance(context);
        int widgetIDs[]=widgetManager
                .getAppWidgetIds(new ComponentName(context,FavoriteWidget.class));
        widgetManager.notifyAppWidgetViewDataChanged(widgetIDs,R.id.sv_widget_fav);
        Intent intent=new Intent(context,FavoriteWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,widgetIDs);
        context.sendBroadcast(intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction()!=null)
        {
            if(intent.getAction().equals(FavoriteWidget.ACTION_OPEN_DETAIL))
            {
                int itemID=intent.getIntExtra(FavoriteWidget.EXTRA_ITEM_ID,0);
                String itemTitle=intent.getStringExtra(FavoriteWidget.EXTRA_ITEM_TITLE);
                Intent home=new Intent(context, HomeActivity.class);
                home.putExtra(ExtraKeys.EXTRA_FAV_ID,itemID);
                home.putExtra(ExtraKeys.EXTRA_MOVIE_TITLE,itemTitle);
                home.putExtra(PendingFragment.EXTRA_TAG_PENDING_FRAGMENT, DetailMovieFragment.class.getSimpleName());
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        |Intent.FLAG_ACTIVITY_CLEAR_TOP
                        |Intent.FLAG_ACTIVITY_SINGLE_TOP
                        |Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(home);

            }
        }
        super.onReceive(context, intent);
    }
}

