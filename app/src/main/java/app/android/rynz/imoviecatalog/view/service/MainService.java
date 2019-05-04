package app.android.rynz.imoviecatalog.view.service;

import android.arch.lifecycle.MutableLiveData;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import app.android.rynz.imoviecatalog.data.model.FavoriteMovie;

public interface MainService
{
    interface FavoriteContent
    {
        interface onGetAllListener
        {
            void onStart();
            void onCompleted(@NonNull ArrayList<FavoriteMovie> favoriteMovies);
        }
        interface onGetByIDListener
        {
            void onStart();
            void onCompleted(@Nullable FavoriteMovie favoriteMovie);
        }
        interface onDeleteListener
        {
            void onStart();
            void onCompleted(int returnValue);
        }
        interface onInsertListener
        {
            void onStart();
            void onCompleted(Uri returnValue);
        }
    }

    interface FavoriteProvider
    {
        long insertFavProvider(final ContentValues item);
        int deleteFavProvider(final String id);
        int updateFavProvider(final String id,final ContentValues item);
        @Nullable
        Cursor getFavByIDProvider(final String id);
        @Nullable
        Cursor getFavByMovieIDProvider(final String MovieID);
        @Nullable
        Cursor getFavProvider();
    }

    interface FavoriteMovies
    {
        interface getAllData
        {
            MutableLiveData<ArrayList<FavoriteMovie>> getAllFavoriteData();
        }
    }
}
