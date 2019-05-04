package app.android.rynz.imoviefavorite.view.service;

import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import app.android.rynz.imoviefavorite.data.model.FavoriteMovie;

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

    interface FavoriteMovies
    {
        interface getAllData
        {
            MutableLiveData<ArrayList<FavoriteMovie>> getAllFavoriteData();
        }
    }

}
