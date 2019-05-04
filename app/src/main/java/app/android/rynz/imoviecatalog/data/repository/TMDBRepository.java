package app.android.rynz.imoviecatalog.data.repository;

import android.support.annotation.NonNull;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.android.rynz.imoviecatalog.data.model.DetailMovie;
import app.android.rynz.imoviecatalog.data.model.NowPlaying;
import app.android.rynz.imoviecatalog.data.model.SearchMovie;
import app.android.rynz.imoviecatalog.data.model.TopRated;
import app.android.rynz.imoviecatalog.data.model.UpComing;
import app.android.rynz.imoviecatalog.data.model.moviedetail.BelongToCollection;
import app.android.rynz.imoviecatalog.data.model.moviedetail.Genre;
import app.android.rynz.imoviecatalog.data.model.moviedetail.ProductionCompany;
import app.android.rynz.imoviecatalog.data.model.moviedetail.ProductionCountry;
import app.android.rynz.imoviecatalog.data.model.moviedetail.SpokenLanguage;
import app.android.rynz.imoviecatalog.data.model.params.DetailParams;
import app.android.rynz.imoviecatalog.data.model.params.NowPlayingParams;
import app.android.rynz.imoviecatalog.data.model.params.SearchMovieParams;
import app.android.rynz.imoviecatalog.data.model.params.TopRatedParams;
import app.android.rynz.imoviecatalog.data.model.params.UpComingParams;
import app.android.rynz.imoviecatalog.data.model.results.ResultDate;
import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;
import app.android.rynz.imoviecatalog.view.service.TMDBService;
import cz.msebera.android.httpclient.Header;

public class TMDBRepository implements TMDBService.SearchMovies, TMDBService.DetailMovie, TMDBService.NowPlayingMovies
        , TMDBService.UpComingMovies, TMDBService.TopRatedMovies
{
    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private SyncHttpClient syncHttpClient = new SyncHttpClient();
    private RequestParams requestParams = new RequestParams();
    private SearchMovie searchModel;
    private DetailMovie detailMovie;
    private NowPlaying nowPlaying;
    private UpComing upComingMovieModel;
    private TopRated topRatedModel;

    @Override
    public void searchMovies(@NonNull SearchMovieParams params, boolean isAsyncProcess, @NonNull final SearchListener listener)
    {
        requestParams.put(SearchMovieParams.KEY_APIKey, params.getApiKey());
        requestParams.put(SearchMovieParams.KEY_QUERY, params.getQuery());

        if (params.getLanguageID() != null)
        {
            requestParams.put(SearchMovieParams.KEY_LANGUAGE, params.getLanguageID());
        }
        if (params.getPage() != 0)
        {
            requestParams.put(SearchMovieParams.KEY_PAGE, params.getPage());
        }
        if (params.isIncludeAdult())
        {
            requestParams.put(SearchMovieParams.KEY_INCLUDE_ADULT, params.isIncludeAdult());
        }
        if (params.getRegion() != null)
        {
            requestParams.put(SearchMovieParams.KEY_REGION, params.getRegion());
        }
        if (params.getYear() != 0)
        {
            requestParams.put(SearchMovieParams.KEY_YEAR, params.getYear());
        }
        if (params.getPrimaryReleaseYear() != 0)
        {
            requestParams.put(SearchMovieParams.KEY_PRIMARY_RELEASE_YEAR, params.getPrimaryReleaseYear());
        }

        requestParams.setAutoCloseInputStreams(true);
        if (isAsyncProcess)
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_SEARCH_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingSearchMovie(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    searchModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        } else
        {
            syncHttpClient.get(TMDBApiReference.TMDB_API_URL_SEARCH_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    setUseSynchronousMode(true);
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingSearchMovie(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    searchModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        }
    }

    private void parsingSearchMovie(int statusCode, Header[] headers, byte[] responseBody, @NonNull SearchListener listener)
    {
        String response = null;
        if (responseBody != null)
        {
            response = new String(responseBody);
        }
        int page, total_results, total_pages;

        try
        {
            JSONObject jsonObject = new JSONObject(response);
            page = jsonObject.getInt(SearchMovie.KEY_PAGE);
            total_results = jsonObject.getInt(SearchMovie.KEY_TOTAL_RESULT);
            total_pages = jsonObject.getInt(SearchMovie.KEY_TOTAL_PAGES);

            JSONArray resultMovies = jsonObject.getJSONArray(SearchMovie.KEY_SEARCH_RESULT);

            searchModel = new SearchMovie(page, total_results, total_pages, parsingResultMovies(resultMovies));
            listener.onCompleted(searchModel);
        } catch (JSONException e)
        {
            searchModel = null;
            e.printStackTrace();
            listener.onFailed(e.getMessage(), response);
        }
    }


    @Override
    public void detailMovie(@NonNull DetailParams params, boolean isAsyncProcess, @NonNull final DetailMovieListener listener)
    {
        requestParams.put(DetailParams.KEY_APIKey, params.getApiKey());
        requestParams.put(DetailParams.KEY_MOVIE_ID, params.getIdMovie());

        if (params.getLanguageID() != null)
        {
            requestParams.put(DetailParams.KEY_LANGUAGE, params.getLanguageID());
        }
        if (params.getAppendToRespond() != null)
        {
            requestParams.put(DetailParams.KEY_APPEND_TO_RESPONSE, params.getAppendToRespond());
        }

        requestParams.setAutoCloseInputStreams(true);
        if (isAsyncProcess)
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_MOVIE_DETAIL + String.valueOf(params.getIdMovie()), requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingDetailMovie(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    detailMovie = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);

                }
            });
        } else
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_MOVIE_DETAIL + String.valueOf(params.getIdMovie()), requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingDetailMovie(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    detailMovie = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        }
    }

    private void parsingDetailMovie(int statusCode, Header[] headers, byte[] responseBody, @NonNull DetailMovieListener listener)
    {
        String response = null;
        if (responseBody != null)
        {
            response = new String(responseBody);
        }

        try
        {
            BelongToCollection belongsToCollection = null;
            ArrayList<Genre> genreList = new ArrayList<>();
            ArrayList<ProductionCompany> productionCompanyList = new ArrayList<>();
            ArrayList<ProductionCountry> productionCountryList = new ArrayList<>();
            ArrayList<SpokenLanguage> spokenLanguageList = new ArrayList<>();


            JSONObject detail = new JSONObject(response);
            if (!detail.isNull(DetailMovie.KEY_MOVIE_BELONG_COLLECTION))
            {
                JSONObject belongCollection = detail.getJSONObject(DetailMovie.KEY_MOVIE_BELONG_COLLECTION);
                belongsToCollection = new BelongToCollection(
                        belongCollection.getInt(BelongToCollection.KEY_ID)
                        , belongCollection.getString(BelongToCollection.KEY_NAME)
                        , belongCollection.getString(BelongToCollection.KEY_POSTER_PATH)
                        , belongCollection.getString(BelongToCollection.KEY_BACKDROP_PATH));
            }
            if (!detail.isNull(DetailMovie.KEY_MOVIE_GENRES))
            {
                JSONArray genres = detail.getJSONArray(DetailMovie.KEY_MOVIE_GENRES);
                for (int i = 0; i < genres.length(); i++)
                {
                    JSONObject object = genres.getJSONObject(i);
                    Genre genreModel = new Genre(object.getInt(Genre.KEY_ID), object.getString(Genre.KEY_NAME));
                    genreList.add(genreModel);
                }
            }
            if (!detail.isNull(DetailMovie.KEY_MOVIE_PRODUCTION_COMPANIES))
            {
                JSONArray companies = detail.getJSONArray(DetailMovie.KEY_MOVIE_PRODUCTION_COMPANIES);
                for (int i = 0; i < companies.length(); i++)
                {
                    JSONObject object = companies.getJSONObject(i);
                    ProductionCompany productionCompany = new ProductionCompany(object.getInt(ProductionCompany.KEY_ID)
                            , object.getString(ProductionCompany.KEY_LOGO_PATH)
                            , object.getString(ProductionCompany.KEY_NAME)
                            , object.getString(ProductionCompany.KEY_ORIGIN_COUNTRY));
                    productionCompanyList.add(productionCompany);
                }
            }
            if (!detail.isNull(DetailMovie.KEY_MOVIE_PRODUCTION_COUNTRIES))
            {
                JSONArray countries = detail.getJSONArray(DetailMovie.KEY_MOVIE_PRODUCTION_COUNTRIES);
                for (int i = 0; i < countries.length(); i++)
                {
                    JSONObject object = countries.getJSONObject(i);
                    ProductionCountry productionCountry = new ProductionCountry(
                            object.getString(ProductionCountry.KEY_ISO3166v1)
                            , object.getString(ProductionCountry.KEY_NAME)
                    );
                    productionCountryList.add(productionCountry);
                }
            }
            if (!detail.isNull(DetailMovie.KEY_MOVIE_SPOKEN_LANGUAGE))
            {
                JSONArray languages = detail.getJSONArray(DetailMovie.KEY_MOVIE_SPOKEN_LANGUAGE);
                for (int i = 0; i < languages.length(); i++)
                {
                    JSONObject object = languages.getJSONObject(i);
                    SpokenLanguage spokenLanguage = new SpokenLanguage(
                            object.getString(SpokenLanguage.KEY_ISO639v1)
                            , object.getString(SpokenLanguage.KEY_NAME)
                    );
                    spokenLanguageList.add(spokenLanguage);
                }
            }

            int runtime = 0;
            if (!detail.isNull(DetailMovie.KEY_MOVIE_RUNTIME))
            {
                runtime = detail.getInt(DetailMovie.KEY_MOVIE_RUNTIME);
            }

            detailMovie = new DetailMovie(
                    detail.getBoolean(DetailMovie.KEY_MOVIE_FOR_ADULT)
                    , detail.getString(DetailMovie.KEY_MOVIE_BACKDROP_PATH)
                    , belongsToCollection
                    , detail.getInt(DetailMovie.KEY_MOVIE_BUDGET)
                    , genreList
                    , detail.getString(DetailMovie.KEY_MOVIE_HOMEPAGE)
                    , detail.getString(DetailMovie.KEY_MOVIE_ID)
                    , detail.getString(DetailMovie.KEY_MOVIE_IMDB_ID)
                    , detail.getString(DetailMovie.KEY_MOVIE_ORI_LANGUAGE)
                    , detail.getString(DetailMovie.KEY_MOVIE_ORI_TITLE)
                    , detail.getString(DetailMovie.KEY_MOVIE_OVERVIEW)
                    , detail.getDouble(DetailMovie.KEY_MOVIE_POPULARITY)
                    , detail.getString(DetailMovie.KEY_MOVIE_POSTER_PATH)
                    , productionCompanyList
                    , productionCountryList
                    , detail.getString(DetailMovie.KEY_MOVIE_REALEASE_DATE)
                    , detail.getInt(DetailMovie.KEY_MOVIE_REVENUE)
                    , runtime
                    , spokenLanguageList
                    , detail.getString(DetailMovie.KEY_MOVIE_STATUS)
                    , detail.getString(DetailMovie.KEY_MOVIE_TAGLINE)
                    , detail.getString(DetailMovie.KEY_MOVIE_TITLE)
                    , detail.getBoolean(DetailMovie.KEY_MOVIE_VIDEO)
                    , detail.getInt(DetailMovie.KEY_MOVIE_VOTE_COUNT)
                    , detail.getDouble(DetailMovie.KEY_MOVIE_VOTE_AVERAGE)
            );
            listener.onCompleted(detailMovie);
        } catch (JSONException e)
        {
            detailMovie = null;
            e.printStackTrace();
            listener.onFailed(e.getMessage(), response);
        }
    }


    @Override
    public void nowPlayingMovies(@NonNull NowPlayingParams params, boolean isAsyncProcess, @NonNull final NowPlayingListener listener)
    {
        requestParams.put(NowPlayingParams.KEY_APIKey, params.getApiKey());
        if (params.getLanguageID() != null)
        {
            requestParams.put(NowPlayingParams.KEY_LANGUAGE, params.getLanguageID());
        }
        if (params.getPage() != 0)
        {
            requestParams.put(NowPlayingParams.KEY_PAGE, params.getLanguageID());
        }
        if (params.getRegion() != null)
        {
            requestParams.put(NowPlayingParams.KEY_REGION, params.getRegion());
        }
        requestParams.setAutoCloseInputStreams(true);

        if (isAsyncProcess)
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_NOW_PLAYING_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingNowPlaying(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    nowPlaying = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        } else
        {
            syncHttpClient.get(TMDBApiReference.TMDB_API_URL_NOW_PLAYING_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    setUseSynchronousMode(true);
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingNowPlaying(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    nowPlaying = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        }
    }

    private void parsingNowPlaying(int statusCode, Header[] headers, byte[] responseBody, @NonNull NowPlayingListener listener)
    {
        String response = null;
        if (responseBody != null)
        {
            response = new String(responseBody);
        }
        int page, total_results, total_pages;

        try
        {
            JSONObject jsonObject = new JSONObject(response);
            page = jsonObject.getInt(NowPlaying.KEY_PAGE);
            total_results = jsonObject.getInt(NowPlaying.KEY_TOTAL_RESULT);
            total_pages = jsonObject.getInt(NowPlaying.KEY_TOTAL_PAGES);

            JSONObject resultDates = jsonObject.getJSONObject(NowPlaying.KEY_UPCOMING_DATE);
            ResultDate dates = new ResultDate(resultDates.getString(ResultDate.KEY_MINIMUM)
                    , resultDates.getString(ResultDate.KEY_MAXIMUM));

            JSONArray resultMovies = jsonObject.getJSONArray(NowPlaying.KEY_UPCOMING_RESULT);

            nowPlaying = new NowPlaying(page, total_results, total_pages, dates, parsingResultMovies(resultMovies));
            listener.onCompleted(nowPlaying);

        } catch (JSONException e)
        {
            nowPlaying = null;
            e.printStackTrace();
            listener.onFailed(e.getMessage(), response);
        }
    }


    @Override
    public void upComingMovies(@NonNull UpComingParams params, boolean isAsyncProcess, @NonNull final UpComingListener listener)
    {
        requestParams.put(UpComingParams.KEY_APIKey, params.getApiKey());
        if (params.getLanguageID() != null)
        {
            requestParams.put(UpComingParams.KEY_LANGUAGE, params.getLanguageID());
        }
        if (params.getPage() != 0)
        {
            requestParams.put(UpComingParams.KEY_PAGE, params.getPage());
        }
        if (params.getRegion() != null)
        {
            requestParams.put(UpComingParams.KEY_REGION, params.getRegion());
        }

        requestParams.setAutoCloseInputStreams(true);
        if (isAsyncProcess)
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_UPCOMING_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingUpComing(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    upComingMovieModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });

        } else
        {
            syncHttpClient.get(TMDBApiReference.TMDB_API_URL_UPCOMING_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    setUseSynchronousMode(true);
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingUpComing(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    upComingMovieModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        }
    }

    private void parsingUpComing(int statusCode, Header[] headers, byte[] responseBody, @NonNull UpComingListener listener)
    {
        String response = null;
        if (responseBody != null)
        {
            response = new String(responseBody);
        }
        int page, total_results, total_pages;

        try
        {
            JSONObject jsonObject = new JSONObject(response);
            page = jsonObject.getInt(UpComing.KEY_PAGE);
            total_results = jsonObject.getInt(UpComing.KEY_TOTAL_RESULT);
            total_pages = jsonObject.getInt(UpComing.KEY_TOTAL_PAGES);

            JSONObject resultDates = jsonObject.getJSONObject(UpComing.KEY_UPCOMING_DATE);
            ResultDate dates = new ResultDate(resultDates.getString(ResultDate.KEY_MINIMUM)
                    , resultDates.getString(ResultDate.KEY_MAXIMUM));

            JSONArray resultMovies = jsonObject.getJSONArray(UpComing.KEY_UPCOMING_RESULT);

            upComingMovieModel = new UpComing(page, total_results, total_pages, dates, parsingResultMovies(resultMovies));
            listener.onCompleted(upComingMovieModel);
        } catch (JSONException e)
        {
            upComingMovieModel = null;
            e.printStackTrace();
            listener.onFailed(e.getMessage(), response);
        }
    }


    @Override
    public void topRatedMovies(@NonNull TopRatedParams params, boolean isAsyncProcess, @NonNull final TopRatedListener listener)
    {
        requestParams.put(TopRatedParams.KEY_APIKey, params.getApiKey());
        if (params.getLanguageID() != null)
        {
            requestParams.put(TopRatedParams.KEY_LANGUAGE, params.getLanguageID());
        }
        if (params.getPage() != 0)
        {
            requestParams.put(TopRatedParams.KEY_PAGE, params.getPage());
        }
        if (params.getRegion() != null)
        {
            requestParams.put(TopRatedParams.KEY_REGION, params.getRegion());
        }

        requestParams.setAutoCloseInputStreams(true);
        if (isAsyncProcess)
        {
            asyncHttpClient.get(TMDBApiReference.TMDB_API_URL_TOP_RATED_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingTopRatedMovie(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    topRatedModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });

        } else
        {
            syncHttpClient.get(TMDBApiReference.TMDB_API_URL_TOP_RATED_MOVIE, requestParams, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    setUseSynchronousMode(true);
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    parsingTopRatedMovie(statusCode, headers, responseBody, listener);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    topRatedModel = null;
                    String apiResponse;
                    if (responseBody != null)
                    {
                        apiResponse = new String(responseBody);
                    } else
                    {
                        apiResponse = null;
                    }
                    listener.onFailed(error.getMessage(), apiResponse);
                }
            });
        }
    }

    private void parsingTopRatedMovie(int statusCode, Header[] headers, byte[] responseBody, @NonNull TopRatedListener listener)
    {
        String response = null;
        if (responseBody != null)
        {
            response = new String(responseBody);
        }
        int page, total_results, total_pages;

        try
        {
            JSONObject jsonObject = new JSONObject(response);
            page = jsonObject.getInt(TopRated.KEY_PAGE);
            total_results = jsonObject.getInt(TopRated.KEY_TOTAL_RESULT);
            total_pages = jsonObject.getInt(TopRated.KEY_TOTAL_PAGES);

            JSONArray resultMovies = jsonObject.getJSONArray(TopRated.KEY_SEARCH_RESULT);

            topRatedModel = new TopRated(page, total_results, total_pages, parsingResultMovies(resultMovies));
            listener.onCompleted(topRatedModel);

        } catch (JSONException e)
        {
            topRatedModel = null;
            e.printStackTrace();
            listener.onFailed(e.getMessage(), response);
        }
    }


    private List<Integer> parsingGenreIDs(@NonNull JSONArray genreids) throws JSONException
    {
        List<Integer> genreIDs = new ArrayList<>();
        if (genreids.length() > 0)
        {
            for (int j = 0; j < genreids.length(); j++)
            {
                genreIDs.add(Integer.valueOf(genreids.getString(j)));
            }
        }
        return genreIDs;
    }

    private ArrayList<ResultMovie> parsingResultMovies(@NonNull JSONArray jsonArray) throws JSONException
    {
        ArrayList<ResultMovie> movieList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject object = jsonArray.getJSONObject(i);

            ResultMovie movieModel = new ResultMovie(
                    object.getInt(ResultMovie.KEY_MOVIE_VOTE_COUNT)
                    , object.getInt(ResultMovie.KEY_MOVIE_ID)
                    , object.getDouble(ResultMovie.KEY_MOVIE_VOTE_AVERAGE)
                    , object.getDouble(ResultMovie.KEY_MOVIE_POPULARITY)
                    , object.getBoolean(ResultMovie.KEY_MOVIE_VIDEO)
                    , object.getBoolean(ResultMovie.KEY_MOVIE_FOR_ADULT)
                    , object.getString(ResultMovie.KEY_MOVIE_TITLE)
                    , object.getString(ResultMovie.KEY_MOVIE_POSTER_PATH)
                    , object.getString(ResultMovie.KEY_MOVIE_ORI_LANGUAGE)
                    , object.getString(ResultMovie.KEY_MOVIE_ORI_TITLE)
                    , object.getString(ResultMovie.KEY_MOVIE_BACKDROP_PATH)
                    , object.getString(ResultMovie.KEY_MOVIE_OVERVIEW)
                    , object.getString(ResultMovie.KEY_MOVIE_REALEASE_DATE)
                    , parsingGenreIDs(object.getJSONArray(ResultMovie.KEY_MOVIE_GENRE_IDs))
            );
            movieList.add(movieModel);
        }
        return movieList;
    }

}
