package app.android.rynz.imoviefavorite.data.contentmanager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import app.android.rynz.imoviefavorite.data.model.FavoriteMovie;
import app.android.rynz.imoviefavorite.view.service.MainService;

import static app.android.rynz.imoviefavorite.data.db.MainDBContract.CONTENT_URI;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.PATH_BY_MOVIEID;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_BACKDROP_PATH;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_BELONG_COLLECTION;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_BUDGET;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_FOR_ADULT;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_GENRES;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_HOMEPAGE;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_ID;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_IMDB_ID;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_ORI_LANGUAGE;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_ORI_TITLE;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_OVERVIEW;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_POPULARITY;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_POSTER_PATH;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_PRODUCTION_COMPANIES;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_PRODUCTION_COUNTRIES;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_REALEASE_DATE;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_REVENUE;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_RUNTIME;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_SPOKEN_LANGUAGE;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_STATUS;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_TAGLINE;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_TITLE;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_VIDEO;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_VOTE_AVERAGE;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite.COLUMN_MOVIE_VOTE_COUNT;
import static app.android.rynz.imoviefavorite.data.db.MainDBContract.TableFavorite._ID;


public class FavoriteManager
{
    /*
     * Favorite manager using content resolver (content provider) running asyncronously
     * Using static method for preventing memory leaks may occur
     * */

    public static void getAllFavorite(@NonNull final Context context, @Nullable final MainService.FavoriteContent.onGetAllListener listener)
    {
        final ContentResolver contentResolver = context.getContentResolver();
        class doTask extends AsyncTask<Void, Void, ArrayList<FavoriteMovie>>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                if (listener != null) listener.onStart();
            }

            @Override
            protected ArrayList<FavoriteMovie> doInBackground(Void... voids)
            {
                Cursor c = contentResolver.query(CONTENT_URI
                        , null
                        , null
                        , null
                        , null);
                ArrayList<FavoriteMovie> items = new ArrayList<>();
                if (c != null)
                {
                    FavoriteMovie item;
                    if (c.getCount() > 0)
                    {
                        c.moveToFirst();
                        do
                        {
                            item = new FavoriteMovie(
                                    c.getString(c.getColumnIndex(COLUMN_MOVIE_ID))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_FOR_ADULT))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_BACKDROP_PATH))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_BELONG_COLLECTION))
                                    , c.getInt(c.getColumnIndex(COLUMN_MOVIE_BUDGET))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_GENRES))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_HOMEPAGE))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_IMDB_ID))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_ORI_LANGUAGE))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_ORI_TITLE))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_OVERVIEW))
                                    , c.getDouble(c.getColumnIndex(COLUMN_MOVIE_POPULARITY))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_POSTER_PATH))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_PRODUCTION_COMPANIES))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_PRODUCTION_COUNTRIES))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_REALEASE_DATE))
                                    , c.getInt(c.getColumnIndex(COLUMN_MOVIE_REVENUE))
                                    , c.getInt(c.getColumnIndex(COLUMN_MOVIE_RUNTIME))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_SPOKEN_LANGUAGE))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_STATUS))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_TAGLINE))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_TITLE))
                                    , c.getString(c.getColumnIndex(COLUMN_MOVIE_VIDEO))
                                    , c.getInt(c.getColumnIndex(COLUMN_MOVIE_VOTE_COUNT))
                                    , c.getDouble(c.getColumnIndex(COLUMN_MOVIE_VOTE_AVERAGE))
                            );
                            item.setID(c.getInt(c.getColumnIndex(_ID)));
                            items.add(item);
                            c.moveToNext();
                        } while (!c.isAfterLast());
                    }
                    c.close();
                }
                return items;
            }

            @Override
            protected void onPostExecute(ArrayList<FavoriteMovie> favoriteMovies)
            {
                super.onPostExecute(favoriteMovies);
                if (listener != null) listener.onCompleted(favoriteMovies);
            }
        }
        new doTask().execute();
    }

    public static void getFavoriteByID(@NonNull final Context context,@NonNull final String id,@Nullable final MainService.FavoriteContent.onGetByIDListener listener)
    {
        final ContentResolver contentResolver = context.getContentResolver();
        class doTask extends AsyncTask<Void, Void, FavoriteMovie>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                if (listener != null) listener.onStart();
            }

            @Override
            protected FavoriteMovie doInBackground(Void... voids)
            {
                Uri uri= Uri.parse(CONTENT_URI.toString()
                        .concat("/"+id));
                Cursor c=contentResolver.query(uri,null
                        ,null
                        ,null
                        ,null);
                FavoriteMovie item=null;
                if(c!=null)
                {
                    if(c.getCount()>0)
                    {
                        c.moveToFirst();
                        item= new FavoriteMovie(
                                c.getString(c.getColumnIndex(COLUMN_MOVIE_ID))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_FOR_ADULT))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_BACKDROP_PATH))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_BELONG_COLLECTION))
                                , c.getInt(c.getColumnIndex(COLUMN_MOVIE_BUDGET))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_GENRES))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_HOMEPAGE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_IMDB_ID))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_ORI_LANGUAGE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_ORI_TITLE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_OVERVIEW))
                                , c.getDouble(c.getColumnIndex(COLUMN_MOVIE_POPULARITY))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_POSTER_PATH))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_PRODUCTION_COMPANIES))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_PRODUCTION_COUNTRIES))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_REALEASE_DATE))
                                , c.getInt(c.getColumnIndex(COLUMN_MOVIE_REVENUE))
                                , c.getInt(c.getColumnIndex(COLUMN_MOVIE_RUNTIME))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_SPOKEN_LANGUAGE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_STATUS))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_TAGLINE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_TITLE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_VIDEO))
                                , c.getInt(c.getColumnIndex(COLUMN_MOVIE_VOTE_COUNT))
                                , c.getDouble(c.getColumnIndex(COLUMN_MOVIE_VOTE_AVERAGE))
                        );
                        item.setID(c.getInt(c.getColumnIndex(_ID)));
                    }
                    c.close();
                }
                return item;
            }

            @Override
            protected void onPostExecute(FavoriteMovie favoriteMovie)
            {
                super.onPostExecute(favoriteMovie);
                if(listener!=null) listener.onCompleted(favoriteMovie);
            }
        }
        new doTask().execute();
    }

    public static void getFavoriteByMovieID(@NonNull final Context context, @NonNull final String id, @Nullable final MainService.FavoriteContent.onGetByIDListener listener)
    {
        final ContentResolver contentResolver = context.getContentResolver();
        class doTask extends AsyncTask<Void, Void, FavoriteMovie>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                if (listener != null) listener.onStart();
            }

            @Override
            protected FavoriteMovie doInBackground(Void... voids)
            {
                Uri uri = Uri.parse(CONTENT_URI.toString()
                        .concat("/" + PATH_BY_MOVIEID)
                        .concat("/" + id));
                Cursor c = contentResolver.query(uri, null, null, null, null);
                FavoriteMovie item = null;
                if (c != null)
                {
                    if (c.getCount() > 0)
                    {
                        c.moveToFirst();
                        item = new FavoriteMovie(
                                c.getString(c.getColumnIndex(COLUMN_MOVIE_ID))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_FOR_ADULT))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_BACKDROP_PATH))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_BELONG_COLLECTION))
                                , c.getInt(c.getColumnIndex(COLUMN_MOVIE_BUDGET))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_GENRES))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_HOMEPAGE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_IMDB_ID))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_ORI_LANGUAGE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_ORI_TITLE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_OVERVIEW))
                                , c.getDouble(c.getColumnIndex(COLUMN_MOVIE_POPULARITY))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_POSTER_PATH))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_PRODUCTION_COMPANIES))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_PRODUCTION_COUNTRIES))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_REALEASE_DATE))
                                , c.getInt(c.getColumnIndex(COLUMN_MOVIE_REVENUE))
                                , c.getInt(c.getColumnIndex(COLUMN_MOVIE_RUNTIME))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_SPOKEN_LANGUAGE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_STATUS))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_TAGLINE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_TITLE))
                                , c.getString(c.getColumnIndex(COLUMN_MOVIE_VIDEO))
                                , c.getInt(c.getColumnIndex(COLUMN_MOVIE_VOTE_COUNT))
                                , c.getDouble(c.getColumnIndex(COLUMN_MOVIE_VOTE_AVERAGE))
                        );
                        item.setID(c.getInt(c.getColumnIndex(_ID)));
                    }
                    c.close();
                }
                return item;
            }

            @Override
            protected void onPostExecute(FavoriteMovie favoriteMovie)
            {
                super.onPostExecute(favoriteMovie);
                if (listener != null) listener.onCompleted(favoriteMovie);
            }
        }
        new doTask().execute();
    }

    public static void deleteFavorite(@NonNull final Context context, @NonNull final String id, @Nullable final MainService.FavoriteContent.onDeleteListener listener)
    {
        final ContentResolver contentResolver = context.getContentResolver();
        class doTask extends AsyncTask<Void, Void, Integer>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                if (listener != null) listener.onStart();
            }

            @Override
            protected Integer doInBackground(Void... voids)
            {
                Uri uri = Uri.parse(CONTENT_URI.toString()
                        .concat("/" + id));
                return contentResolver.delete(uri, null, null);
            }

            @Override
            protected void onPostExecute(Integer integer)
            {
                super.onPostExecute(integer);
                if (listener != null) listener.onCompleted(integer);
            }
        }
        new doTask().execute();
    }

    public static void insertFavorite(@NonNull final Context context, @NonNull final FavoriteMovie item, @Nullable final MainService.FavoriteContent.onInsertListener listener)
    {
        final ContentResolver contentResolver = context.getContentResolver();

        class doTask extends AsyncTask<Void, Void, Uri>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                if (listener != null) listener.onStart();
            }

            @Override
            protected Uri doInBackground(Void... voids)
            {
                ContentValues val = new ContentValues();
                val.put(COLUMN_MOVIE_ID, item.getMovieID());
                val.put(COLUMN_MOVIE_FOR_ADULT, item.isAdult());
                val.put(COLUMN_MOVIE_BACKDROP_PATH, item.getBackdropPath());
                val.put(COLUMN_MOVIE_BELONG_COLLECTION, item.getStrBelongToCollection());
                val.put(COLUMN_MOVIE_BUDGET, item.getBuget());
                val.put(COLUMN_MOVIE_GENRES, item.getStrGenres());
                val.put(COLUMN_MOVIE_HOMEPAGE, item.getHomePage());
                val.put(COLUMN_MOVIE_IMDB_ID, item.getImdbID());
                val.put(COLUMN_MOVIE_ORI_LANGUAGE, item.getOriLanguage());
                val.put(COLUMN_MOVIE_ORI_TITLE, item.getOriTitle());
                val.put(COLUMN_MOVIE_OVERVIEW, item.getOverview());
                val.put(COLUMN_MOVIE_POPULARITY, item.getPopularity());
                val.put(COLUMN_MOVIE_POSTER_PATH, item.getPosterPath());
                val.put(COLUMN_MOVIE_PRODUCTION_COMPANIES, item.getStrProductionCompany());
                val.put(COLUMN_MOVIE_PRODUCTION_COUNTRIES, item.getStrProductionCountry());
                val.put(COLUMN_MOVIE_REALEASE_DATE, item.getReleaseDate());
                val.put(COLUMN_MOVIE_REVENUE, item.getRevenue());
                val.put(COLUMN_MOVIE_RUNTIME, item.getRuntime());
                val.put(COLUMN_MOVIE_SPOKEN_LANGUAGE, item.getStrSpokenLanguage());
                val.put(COLUMN_MOVIE_STATUS, item.getStatus());
                val.put(COLUMN_MOVIE_TAGLINE, item.getTagline());
                val.put(COLUMN_MOVIE_TITLE, item.getTitle());
                val.put(COLUMN_MOVIE_VIDEO, item.isVideo());
                val.put(COLUMN_MOVIE_VOTE_AVERAGE, item.getVoteAverage());
                val.put(COLUMN_MOVIE_VOTE_COUNT, item.getVoteCount());
                return contentResolver.insert(CONTENT_URI, val);
            }

            @Override
            protected void onPostExecute(Uri uri)
            {
                super.onPostExecute(uri);
                if (listener != null) listener.onCompleted(uri);
            }
        }
        new doTask().execute();

    }
}
