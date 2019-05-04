package app.android.rynz.imoviecatalog.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.contentmanager.FavoriteManager;
import app.android.rynz.imoviecatalog.data.model.FavoriteMovie;
import app.android.rynz.imoviecatalog.data.repository.TMDBApiReference;
import app.android.rynz.imoviecatalog.view.service.MainService;

public class FavoriteWidgetFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context context;
    private int appWidgetID;
    private ArrayList<FavoriteMovie> items;

    public FavoriteWidgetFactory(Context context, Intent intent)
    {
        this.context = context;
        this.appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate()
    {
        FavoriteManager.getAllFavorite(context,false, new MainService.FavoriteContent.onGetAllListener()
        {
            @Override
            public void onStart()
            {
                //Do nothing
            }

            @Override
            public void onCompleted(@NonNull ArrayList<FavoriteMovie> favoriteMovies)
            {
                items=favoriteMovies;
            }
        });
    }

    @Override
    public void onDataSetChanged()
    {
        final long token= Binder.clearCallingIdentity();
        try
        {
            FavoriteManager.getAllFavorite(context.getApplicationContext(),false, new MainService.FavoriteContent.onGetAllListener()
            {
                @Override
                public void onStart()
                {
                    //Do nothing
                }

                @Override
                public void onCompleted(@NonNull ArrayList<FavoriteMovie> favoriteMovies)
                {
                    items=favoriteMovies;
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            Binder.restoreCallingIdentity(token);
        }
    }

    @Override
    public void onDestroy()
    {

    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        if(getCount()>0 && position<getCount())
        {
            RemoteViews view=new RemoteViews(context.getPackageName(), R.layout.widget_fav_item);
            view.setImageViewBitmap(R.id.iv_widget_fav, BitmapFactory.decodeResource(context.getResources(),R.drawable.no_images));
            try
            {
                Bitmap bitmap=Glide.with(context)
                        .asBitmap()
                        .load(TMDBApiReference.TMDB_POSTER_500px+items.get(position).getBackdropPath())
                        .apply(new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.no_images))
                                .error(context.getResources().getDrawable(R.drawable.no_images)))
                        .thumbnail(0.9f)
                        .submit()
                        .get();
                view.setImageViewBitmap(R.id.iv_widget_fav,bitmap);

            } catch (Exception e)
            {
                e.printStackTrace();
            }

            String title=items.get(position).getTitle();
            if(title.length()>25)
            {
                title=title.substring(0,25).concat("...");
            }
            view.setTextViewText(R.id.tv_widget_fav,title);

            //OnItemClick
            Bundle bundle=new Bundle();
            bundle.putString(FavoriteWidget.EXTRA_ITEM_TITLE,items.get(position).getTitle());
            bundle.putInt(FavoriteWidget.EXTRA_ITEM_ID,items.get(position).getID());
            Intent fillInIntent=new Intent();
            fillInIntent.putExtras(bundle);
            view.setOnClickFillInIntent(R.id.master_widget_fav,fillInIntent);

            return view;
        }
        else
        {
            return null;
        }
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }
}
