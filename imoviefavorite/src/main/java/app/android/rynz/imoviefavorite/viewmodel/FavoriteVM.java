package app.android.rynz.imoviefavorite.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import app.android.rynz.imoviefavorite.data.contentmanager.FavoriteManager;
import app.android.rynz.imoviefavorite.data.model.FavoriteMovie;
import app.android.rynz.imoviefavorite.view.service.MainService;

public class FavoriteVM extends AndroidViewModel implements MainService.FavoriteMovies.getAllData
{
    private MutableLiveData<ArrayList<FavoriteMovie>> mutableLiveData = new MutableLiveData<>();
    private Application application;
    public FavoriteVM(@NonNull Application application)
    {
        super(application);
        this.application=application;
    }

    @Override
    public MutableLiveData<ArrayList<FavoriteMovie>> getAllFavoriteData()
    {
        FavoriteManager.getAllFavorite(application.getApplicationContext(), new MainService.FavoriteContent.onGetAllListener()
        {
            @Override
            public void onStart()
            {
                //Do Nothing
            }

            @Override
            public void onCompleted(@NonNull ArrayList<FavoriteMovie> favoriteMovies)
            {
                mutableLiveData.setValue(favoriteMovies);
            }
        });
        return mutableLiveData;
    }
}
