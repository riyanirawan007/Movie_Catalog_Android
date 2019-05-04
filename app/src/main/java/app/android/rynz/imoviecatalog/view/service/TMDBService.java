package app.android.rynz.imoviecatalog.view.service;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import app.android.rynz.imoviecatalog.data.model.FavoriteMovie;
import app.android.rynz.imoviecatalog.data.model.NowPlaying;
import app.android.rynz.imoviecatalog.data.model.SearchMovie;
import app.android.rynz.imoviecatalog.data.model.TopRated;
import app.android.rynz.imoviecatalog.data.model.UpComing;
import app.android.rynz.imoviecatalog.data.model.params.DetailParams;
import app.android.rynz.imoviecatalog.data.model.params.NowPlayingParams;
import app.android.rynz.imoviecatalog.data.model.params.SearchMovieParams;
import app.android.rynz.imoviecatalog.data.model.params.TopRatedParams;
import app.android.rynz.imoviecatalog.data.model.params.UpComingParams;

public interface TMDBService
{
    interface SearchMovies
    {
        void searchMovies(@NonNull SearchMovieParams params, boolean isAsyncProcess, @NonNull final SearchListener listener);

        interface SearchListener
        {
            void onCompleted(@Nullable SearchMovie searchResult);

            void onFailed(@Nullable String strError, @Nullable String apiRespons);
        }

        interface SearchMovieLiveData
        {
            MutableLiveData<SearchMovie> searchMoviesLiveData(@NonNull SearchMovieParams params);
        }
    }

    interface DetailMovie
    {
        void detailMovie(@NonNull DetailParams params, boolean isAsyncProcess, @NonNull final DetailMovieListener listener);

        interface DetailMovieListener
        {
            void onCompleted(@Nullable app.android.rynz.imoviecatalog.data.model.DetailMovie movieDetail);

            void onFailed(@Nullable String strError, @Nullable String apiRespons);
        }

        interface DetailMovieLiveData
        {
            MutableLiveData<app.android.rynz.imoviecatalog.data.model.DetailMovie> detailMovieLiveData(@NonNull DetailParams params);
        }
    }

    interface NowPlayingMovies
    {
        void nowPlayingMovies(@NonNull NowPlayingParams params, boolean isAsyncProcess, @NonNull final NowPlayingListener listener);

        interface NowPlayingListener
        {
            void onCompleted(@NonNull NowPlaying nowPlayingMovies);

            void onFailed(@Nullable String strError, @Nullable String apiRespons);
        }

        interface NowPlayingMovieLiveData
        {
            MutableLiveData<NowPlaying> nowPlayingMovieLiveData(@NonNull NowPlayingParams params);
        }
    }

    interface UpComingMovies
    {
        void upComingMovies(@NonNull UpComingParams params, boolean isAsyncProcess, @NonNull final UpComingListener listener);

        interface UpComingListener
        {
            void onCompleted(@Nullable UpComing upComingMovies);

            void onFailed(@Nullable String strError, @Nullable String apiRespons);
        }

        interface UpComingMovieLiveData
        {
            MutableLiveData<UpComing> upComingMovieLiveData(@NonNull UpComingParams params);
        }
    }

    interface TopRatedMovies
    {
        void topRatedMovies(@NonNull TopRatedParams params, boolean isAsyncProcess, @NonNull final TopRatedListener listener);

        interface TopRatedListener
        {
            void onCompleted(@NonNull TopRated topRatedMovies);
            void onFailed(@Nullable String strError,@Nullable String apiRespons);
        }

        interface TopRatedMovieLiveData
        {
            MutableLiveData<TopRated> topRatedMovieLiveData(@NonNull TopRatedParams params);
        }
    }



}
