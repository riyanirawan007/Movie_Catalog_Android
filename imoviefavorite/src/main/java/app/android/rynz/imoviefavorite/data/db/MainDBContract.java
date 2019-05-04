package app.android.rynz.imoviefavorite.data.db;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import static app.android.rynz.imoviefavorite.data.model.FavoriteMovie.*;

public class MainDBContract
{
    public final static String DB_NAME = "imoviecatalog.db";
    public final static int DB_VERSION = 1;

    public static final String AUTHORITY="app.android.rynz.imoviecatalog";
    public static final String BASE_PATH="favorite";
    public static final String PATH_BY_MOVIEID="by_movie_id";
    public static final Uri CONTENT_URI=new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(BASE_PATH)
            .build();
    public static final int TAG_FAVORITE =1;
    public static final int TAG_FAVORITE_ID =2;
    public static final int TAG_FAVORITE_MOVIE_ID =3;

    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, TAG_FAVORITE);
        uriMatcher.addURI(AUTHORITY, BASE_PATH+"/#", TAG_FAVORITE_ID);
        uriMatcher.addURI(AUTHORITY, BASE_PATH+"/"+PATH_BY_MOVIEID+"/#", TAG_FAVORITE_MOVIE_ID);
    }

    public static class TableFavorite implements BaseColumns
    {
        public static final String TABLE_NAME="tb_favorite";
        public static final String COLUMN_MOVIE_ID = KEY_MOVIE_ID;
        public static final String COLUMN_MOVIE_FOR_ADULT = KEY_MOVIE_FOR_ADULT;
        public static final String COLUMN_MOVIE_BACKDROP_PATH = KEY_MOVIE_BACKDROP_PATH;
        public static final String COLUMN_MOVIE_BELONG_COLLECTION = KEY_MOVIE_BELONG_COLLECTION;
        public static final String COLUMN_MOVIE_BUDGET = KEY_MOVIE_BUDGET;
        public static final String COLUMN_MOVIE_GENRES = KEY_MOVIE_GENRES;
        public static final String COLUMN_MOVIE_HOMEPAGE = KEY_MOVIE_HOMEPAGE;
        public static final String COLUMN_MOVIE_IMDB_ID = KEY_MOVIE_IMDB_ID;
        public static final String COLUMN_MOVIE_ORI_LANGUAGE = KEY_MOVIE_ORI_LANGUAGE;
        public static final String COLUMN_MOVIE_ORI_TITLE = KEY_MOVIE_ORI_TITLE;
        public static final String COLUMN_MOVIE_OVERVIEW = KEY_MOVIE_OVERVIEW;
        public static final String COLUMN_MOVIE_POPULARITY = KEY_MOVIE_POPULARITY;
        public static final String COLUMN_MOVIE_POSTER_PATH = KEY_MOVIE_POSTER_PATH;
        public static final String COLUMN_MOVIE_PRODUCTION_COMPANIES = KEY_MOVIE_PRODUCTION_COMPANIES;
        public static final String COLUMN_MOVIE_PRODUCTION_COUNTRIES = KEY_MOVIE_PRODUCTION_COUNTRIES;
        public static final String COLUMN_MOVIE_REALEASE_DATE = KEY_MOVIE_REALEASE_DATE;
        public static final String COLUMN_MOVIE_REVENUE = KEY_MOVIE_REVENUE;
        public static final String COLUMN_MOVIE_RUNTIME = KEY_MOVIE_RUNTIME;
        public static final String COLUMN_MOVIE_SPOKEN_LANGUAGE = KEY_MOVIE_SPOKEN_LANGUAGE;
        public static final String COLUMN_MOVIE_STATUS = KEY_MOVIE_STATUS;
        public static final String COLUMN_MOVIE_TAGLINE = KEY_MOVIE_TAGLINE;
        public static final String COLUMN_MOVIE_TITLE = KEY_MOVIE_TITLE;
        public static final String COLUMN_MOVIE_VIDEO = KEY_MOVIE_VIDEO;
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = KEY_MOVIE_VOTE_AVERAGE;
        public static final String COLUMN_MOVIE_VOTE_COUNT = KEY_MOVIE_VOTE_COUNT;
    }
}
