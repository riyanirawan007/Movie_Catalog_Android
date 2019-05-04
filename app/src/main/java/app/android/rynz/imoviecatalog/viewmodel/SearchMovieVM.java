package app.android.rynz.imoviecatalog.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.android.rynz.imoviecatalog.data.model.SearchMovie;
import app.android.rynz.imoviecatalog.data.model.params.SearchMovieParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBRepository;
import app.android.rynz.imoviecatalog.view.service.TMDBService;

public class SearchMovieVM extends AndroidViewModel implements TMDBService.SearchMovies.SearchMovieLiveData
{
    private TMDBRepository tmdbRepository;
    private MutableLiveData<SearchMovie> mutableLiveData = new MutableLiveData<>();

    public SearchMovieVM(@NonNull Application application)
    {
        super(application);
        this.tmdbRepository = new TMDBRepository();
    }

    @Override
    public MutableLiveData<SearchMovie> searchMoviesLiveData(@NonNull SearchMovieParams params)
    {
        tmdbRepository.searchMovies(params, true, new TMDBService.SearchMovies.SearchListener()
        {
            @Override
            public void onCompleted(@Nullable SearchMovie searchResult)
            {
                mutableLiveData.setValue(searchResult);
            }

            @Override
            public void onFailed(@Nullable String strError, @Nullable String apiRespons)
            {

            }
        });

        return mutableLiveData;
    }
}
