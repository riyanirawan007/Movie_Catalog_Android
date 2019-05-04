package app.android.rynz.imoviecatalog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.android.rynz.imoviecatalog.data.model.DetailMovie;
import app.android.rynz.imoviecatalog.data.model.params.DetailParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBRepository;
import app.android.rynz.imoviecatalog.view.service.TMDBService;

public class DetailVM extends AndroidViewModel implements TMDBService.DetailMovie.DetailMovieLiveData
{
    private TMDBRepository tmdbRepository;
    private MutableLiveData<DetailMovie> mutableLiveData = new MutableLiveData<>();

    public DetailVM(@NonNull Application application)
    {
        super(application);
        tmdbRepository = new TMDBRepository();
    }

    @Override
    public MutableLiveData<DetailMovie> detailMovieLiveData(@NonNull DetailParams params)
    {
        tmdbRepository.detailMovie(params, true, new TMDBService.DetailMovie.DetailMovieListener()
        {
            @Override
            public void onCompleted(@Nullable DetailMovie movieDetail)
            {
                mutableLiveData.setValue(movieDetail);
            }

            @Override
            public void onFailed(@Nullable String strError, @Nullable String apiRespons)
            {

            }
        });
        return mutableLiveData;
    }

}
