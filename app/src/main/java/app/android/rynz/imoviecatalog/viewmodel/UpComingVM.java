package app.android.rynz.imoviecatalog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.android.rynz.imoviecatalog.data.model.UpComing;
import app.android.rynz.imoviecatalog.data.model.params.UpComingParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBRepository;
import app.android.rynz.imoviecatalog.view.service.TMDBService;

public class UpComingVM extends AndroidViewModel implements TMDBService.UpComingMovies.UpComingMovieLiveData
{
    private TMDBRepository tmdbRepository;
    private MutableLiveData<UpComing> mutableLiveData=new MutableLiveData<>();

    public UpComingVM(@NonNull Application application)
    {
        super(application);
        tmdbRepository=new TMDBRepository();
    }

    @Override
    public MutableLiveData<UpComing> upComingMovieLiveData(@NonNull UpComingParams params)
    {
        tmdbRepository.upComingMovies(params, true, new TMDBService.UpComingMovies.UpComingListener()
        {
            @Override
            public void onCompleted(@Nullable UpComing upComingMovies)
            {
                mutableLiveData.setValue(upComingMovies);
            }

            @Override
            public void onFailed(@Nullable String strError, @Nullable String apiRespons)
            {

            }
        });
        return mutableLiveData;
    }
}
