package app.android.rynz.imoviecatalog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.android.rynz.imoviecatalog.data.model.TopRated;
import app.android.rynz.imoviecatalog.data.model.params.TopRatedParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBRepository;
import app.android.rynz.imoviecatalog.view.service.TMDBService;

public class TopRatedVM extends AndroidViewModel implements TMDBService.TopRatedMovies.TopRatedMovieLiveData
{
    TMDBRepository tmdbRepository;
    MutableLiveData<TopRated> mutableLiveData=new MutableLiveData<>();

    public TopRatedVM(@NonNull Application application)
    {
        super(application);
        tmdbRepository=new TMDBRepository();
    }


    @Override
    public MutableLiveData<TopRated> topRatedMovieLiveData(@NonNull TopRatedParams params)
    {
        tmdbRepository.topRatedMovies(params, true, new TMDBService.TopRatedMovies.TopRatedListener()
        {
            @Override
            public void onCompleted(@NonNull TopRated topRatedMovies)
            {
                mutableLiveData.setValue(topRatedMovies);
            }

            @Override
            public void onFailed(@Nullable String strError, @Nullable String apiRespons)
            {

            }
        });
        return mutableLiveData;
    }
}
