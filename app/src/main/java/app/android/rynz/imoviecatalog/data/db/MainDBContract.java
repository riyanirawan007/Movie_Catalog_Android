package app.android.rynz.imoviecatalog.data.db;

import android.provider.BaseColumns;

import app.android.rynz.imoviecatalog.data.model.FavoriteMovie;

public class MainDBContract
{
    public final static String DB_NAME = "imoviecatalog.db";
    public final static int DB_VERSION = 1;

    private static final String TYPE_TEXT=" TEXT ";
    private static final String TYPE_INT=" INTEGER ";
    private static final String TYPE_REAL=" REAL ";
    private static final String EXTRA_AI=" AUTOINCREMENT ";
    private static final String EXTRA_PK=" PRIMARY KEY ";

    public static class TableFavorite implements BaseColumns
    {
        public static final String TABLE_NAME="tb_favorite";
        public static final String COLUMN_MOVIE_ID = FavoriteMovie.KEY_MOVIE_ID;
        public static final String COLUMN_MOVIE_FOR_ADULT = FavoriteMovie.KEY_MOVIE_FOR_ADULT;
        public static final String COLUMN_MOVIE_BACKDROP_PATH = FavoriteMovie.KEY_MOVIE_BACKDROP_PATH;
        public static final String COLUMN_MOVIE_BELONG_COLLECTION = FavoriteMovie.KEY_MOVIE_BELONG_COLLECTION;
        public static final String COLUMN_MOVIE_BUDGET = FavoriteMovie.KEY_MOVIE_BUDGET;
        public static final String COLUMN_MOVIE_GENRES = FavoriteMovie.KEY_MOVIE_GENRES;
        public static final String COLUMN_MOVIE_HOMEPAGE = FavoriteMovie.KEY_MOVIE_HOMEPAGE;
        public static final String COLUMN_MOVIE_IMDB_ID = FavoriteMovie.KEY_MOVIE_IMDB_ID;
        public static final String COLUMN_MOVIE_ORI_LANGUAGE = FavoriteMovie.KEY_MOVIE_ORI_LANGUAGE;
        public static final String COLUMN_MOVIE_ORI_TITLE = FavoriteMovie.KEY_MOVIE_ORI_TITLE;
        public static final String COLUMN_MOVIE_OVERVIEW = FavoriteMovie.KEY_MOVIE_OVERVIEW;
        public static final String COLUMN_MOVIE_POPULARITY = FavoriteMovie.KEY_MOVIE_POPULARITY;
        public static final String COLUMN_MOVIE_POSTER_PATH = FavoriteMovie.KEY_MOVIE_POSTER_PATH;
        public static final String COLUMN_MOVIE_PRODUCTION_COMPANIES = FavoriteMovie.KEY_MOVIE_PRODUCTION_COMPANIES;
        public static final String COLUMN_MOVIE_PRODUCTION_COUNTRIES = FavoriteMovie.KEY_MOVIE_PRODUCTION_COUNTRIES;
        public static final String COLUMN_MOVIE_REALEASE_DATE = FavoriteMovie.KEY_MOVIE_REALEASE_DATE;
        public static final String COLUMN_MOVIE_REVENUE = FavoriteMovie.KEY_MOVIE_REVENUE;
        public static final String COLUMN_MOVIE_RUNTIME = FavoriteMovie.KEY_MOVIE_RUNTIME;
        public static final String COLUMN_MOVIE_SPOKEN_LANGUAGE = FavoriteMovie.KEY_MOVIE_SPOKEN_LANGUAGE;
        public static final String COLUMN_MOVIE_STATUS = FavoriteMovie.KEY_MOVIE_STATUS;
        public static final String COLUMN_MOVIE_TAGLINE = FavoriteMovie.KEY_MOVIE_TAGLINE;
        public static final String COLUMN_MOVIE_TITLE = FavoriteMovie.KEY_MOVIE_TITLE;
        public static final String COLUMN_MOVIE_VIDEO = FavoriteMovie.KEY_MOVIE_VIDEO;
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = FavoriteMovie.KEY_MOVIE_VOTE_AVERAGE;
        public static final String COLUMN_MOVIE_VOTE_COUNT = FavoriteMovie.KEY_MOVIE_VOTE_COUNT;

        public static final String SQL_CREATE="CREATE TABLE "+TABLE_NAME+" ("+
                _ID+TYPE_INT+EXTRA_PK+EXTRA_AI
                +", "+COLUMN_MOVIE_ID+TYPE_TEXT
                +", "+COLUMN_MOVIE_FOR_ADULT+TYPE_TEXT
                +", "+COLUMN_MOVIE_BACKDROP_PATH+TYPE_TEXT
                +", "+COLUMN_MOVIE_BELONG_COLLECTION+TYPE_TEXT
                +", "+COLUMN_MOVIE_BUDGET+TYPE_INT
                +", "+COLUMN_MOVIE_GENRES+TYPE_TEXT
                +", "+COLUMN_MOVIE_HOMEPAGE+TYPE_TEXT
                +", "+COLUMN_MOVIE_IMDB_ID+TYPE_TEXT
                +", "+COLUMN_MOVIE_ORI_LANGUAGE+TYPE_TEXT
                +", "+COLUMN_MOVIE_ORI_TITLE+TYPE_TEXT
                +", "+COLUMN_MOVIE_OVERVIEW+TYPE_TEXT
                +", "+COLUMN_MOVIE_POPULARITY+TYPE_REAL
                +", "+COLUMN_MOVIE_POSTER_PATH+TYPE_TEXT
                +", "+COLUMN_MOVIE_PRODUCTION_COMPANIES+TYPE_TEXT
                +", "+COLUMN_MOVIE_PRODUCTION_COUNTRIES+TYPE_TEXT
                +", "+COLUMN_MOVIE_REALEASE_DATE+TYPE_TEXT
                +", "+COLUMN_MOVIE_REVENUE+TYPE_INT
                +", "+COLUMN_MOVIE_RUNTIME+TYPE_INT
                +", "+COLUMN_MOVIE_SPOKEN_LANGUAGE+TYPE_TEXT
                +", "+COLUMN_MOVIE_STATUS+TYPE_TEXT
                +", "+COLUMN_MOVIE_TAGLINE+TYPE_TEXT
                +", "+COLUMN_MOVIE_TITLE+TYPE_TEXT
                +", "+COLUMN_MOVIE_VIDEO+TYPE_TEXT
                +", "+COLUMN_MOVIE_VOTE_AVERAGE+TYPE_REAL
                +", "+COLUMN_MOVIE_VOTE_COUNT+TYPE_INT
                +" )";
        public static final String SQL_DROP="DROP IF EXISTS "+TABLE_NAME;
    }
}
