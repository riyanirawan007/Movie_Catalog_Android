package app.android.rynz.imoviecatalog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.android.rynz.imoviecatalog.data.model.NowPlaying;
import app.android.rynz.imoviecatalog.data.model.params.NowPlayingParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBRepository;
import app.android.rynz.imoviecatalog.view.service.TMDBService;

public class NowPlayingVM extends AndroidViewModel implements TMDBService.NowPlayingMovies.NowPlayingMovieLiveData
{
    private TMDBRepository tmdbRepository;
    private MutableLiveData<NowPlaying> mutableLiveData=new MutableLiveData<>();

    public NowPlayingVM(@NonNull Application application)
    {
        super(application);
        tmdbRepository=new TMDBRepository();
    }

    @Override
    public MutableLiveData<NowPlaying> nowPlayingMovieLiveData(@NonNull NowPlayingParams params)
    {
        tmdbRepository.nowPlayingMovies(params, true, new TMDBService.NowPlayingMovies.NowPlayingListener()
        {
            @Override
            public void onCompleted(@NonNull NowPlaying nowPlayingMovies)
            {
                mutableLiveData.setValue(nowPlayingMovies);
            }

            @Override
            public void onFailed(@Nullable String strError, @Nullable String apiRespons)
            {

            }
        });
        return mutableLiveData;
    }

}
