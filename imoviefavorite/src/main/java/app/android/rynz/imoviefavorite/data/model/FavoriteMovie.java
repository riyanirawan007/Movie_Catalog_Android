package app.android.rynz.imoviefavorite.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class FavoriteMovie implements Parcelable
{

    public static final String KEY_MOVIE_FOR_ADULT = "adult";
    public static final String KEY_MOVIE_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_MOVIE_BELONG_COLLECTION = "belongs_to_collection";
    public static final String KEY_MOVIE_BUDGET = "budget";
    public static final String KEY_MOVIE_GENRES = "genres";
    public static final String KEY_MOVIE_HOMEPAGE = "homepage";
    public static final String KEY_MOVIE_ID = "id";
    public static final String KEY_MOVIE_IMDB_ID = "imdb_id";
    public static final String KEY_MOVIE_ORI_LANGUAGE = "original_language";
    public static final String KEY_MOVIE_ORI_TITLE = "original_title";
    public static final String KEY_MOVIE_OVERVIEW = "overview";
    public static final String KEY_MOVIE_POPULARITY = "popularity";
    public static final String KEY_MOVIE_POSTER_PATH = "poster_path";
    public static final String KEY_MOVIE_PRODUCTION_COMPANIES = "production_companies";
    public static final String KEY_MOVIE_PRODUCTION_COUNTRIES = "production_countries";
    public static final String KEY_MOVIE_REALEASE_DATE = "release_date";
    public static final String KEY_MOVIE_REVENUE = "revenue";
    public static final String KEY_MOVIE_RUNTIME = "runtime";
    public static final String KEY_MOVIE_SPOKEN_LANGUAGE = "spoken_languages";
    public static final String KEY_MOVIE_STATUS = "status";
    public static final String KEY_MOVIE_TAGLINE = "tagline";
    public static final String KEY_MOVIE_TITLE = "title";
    public static final String KEY_MOVIE_VIDEO = "video";
    public static final String KEY_MOVIE_VOTE_AVERAGE = "vote_average";
    public static final String KEY_MOVIE_VOTE_COUNT = "vote_count";

    private boolean isAdult;
    private @Nullable
    String backdropPath;
    private int buget;
    private @Nullable String homePage;
    private String movieID;
    private @Nullable String imdbID;
    private String oriLanguage;
    private String oriTitle;
    private @Nullable String overview;
    private double popularity;
    private @Nullable String posterPath;
    private String releaseDate;
    private int revenue;
    private int runtime;
    private String status;
    private @Nullable String tagline;
    private String title;
    private boolean video;
    private int voteCount;
    private double voteAverage;

    private int ID;
    //Modifier multi values field
    private String strBelongToCollection;
    private String strGenres;
    private String strProductionCompany;
    private String strProductionCountry;
    private String strSpokenLanguage;

    public FavoriteMovie(String movieID, String isAdult, @Nullable String backdropPath, @Nullable String strBelongToCollection, int buget,
                         @Nullable String strGenres, @Nullable String homePage, @Nullable String imdbID,
                         String oriLanguage, String oriTitle, @Nullable String overview, double popularity,
                         @Nullable String posterPath, @Nullable String strProductionCompany,
                         @Nullable String strProductionCountry, String releaseDate, int revenue, int runtime,
                         @Nullable String strSpokenLanguage, String status, @Nullable String tagline, String title,
                         String video, int voteCount, double voteAverage)
    {
        this.movieID=movieID;
        this.isAdult=Boolean.valueOf(isAdult);
        this.backdropPath=backdropPath;
        this.strBelongToCollection=strBelongToCollection;
        this.buget=buget;
        this.strGenres=strGenres;
        this.homePage=homePage;
        this.imdbID=imdbID;
        this.oriLanguage=oriLanguage;
        this.oriTitle=oriTitle;
        this.overview=overview;
        this.popularity=popularity;
        this.posterPath=posterPath;
        this.strProductionCompany=strProductionCompany;
        this.strProductionCountry=strProductionCountry;
        this.releaseDate=releaseDate;
        this.revenue=revenue;
        this.runtime=runtime;
        this.strSpokenLanguage=strSpokenLanguage;
        this.status=status;
        this.tagline=tagline;
        this.title=title;
        this.video=Boolean.valueOf(video);
        this.voteCount=voteCount;
        this.voteAverage=voteAverage;

    }

    public boolean isAdult()
    {
        return isAdult;
    }

    @Nullable
    public String getBackdropPath()
    {
        return backdropPath;
    }

    public int getBuget()
    {
        return buget;
    }

    @Nullable
    public String getHomePage()
    {
        return homePage;
    }

    public String getMovieID()
    {
        return movieID;
    }

    @Nullable
    public String getImdbID()
    {
        return imdbID;
    }

    public String getOriLanguage()
    {
        return oriLanguage;
    }

    public String getOriTitle()
    {
        return oriTitle;
    }

    @Nullable
    public String getOverview()
    {
        return overview;
    }

    public double getPopularity()
    {
        return popularity;
    }

    @Nullable
    public String getPosterPath()
    {
        return posterPath;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public int getRevenue()
    {
        return revenue;
    }

    public int getRuntime()
    {
        return runtime;
    }

    public String getStatus()
    {
        return status;
    }

    @Nullable
    public String getTagline()
    {
        return tagline;
    }

    public String getTitle()
    {
        return title;
    }

    public boolean isVideo()
    {
        return video;
    }

    public int getVoteCount()
    {
        return voteCount;
    }

    public double getVoteAverage()
    {
        return voteAverage;
    }

    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public String getStrBelongToCollection()
    {
        return strBelongToCollection;
    }

    public String getStrGenres()
    {
        return strGenres;
    }

    public String getStrProductionCompany()
    {
        return strProductionCompany;
    }

    public String getStrProductionCountry()
    {
        return strProductionCountry;
    }

    public String getStrSpokenLanguage()
    {
        return strSpokenLanguage;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeByte(this.isAdult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdropPath);
        dest.writeInt(this.buget);
        dest.writeString(this.homePage);
        dest.writeString(this.movieID);
        dest.writeString(this.imdbID);
        dest.writeString(this.oriLanguage);
        dest.writeString(this.oriTitle);
        dest.writeString(this.overview);
        dest.writeDouble(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.revenue);
        dest.writeInt(this.runtime);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeInt(this.voteCount);
        dest.writeDouble(this.voteAverage);
        dest.writeInt(this.ID);
        dest.writeString(this.strBelongToCollection);
        dest.writeString(this.strGenres);
        dest.writeString(this.strProductionCompany);
        dest.writeString(this.strProductionCountry);
        dest.writeString(this.strSpokenLanguage);
    }

    protected FavoriteMovie(Parcel in)
    {
        this.isAdult = in.readByte() != 0;
        this.backdropPath = in.readString();
        this.buget = in.readInt();
        this.homePage = in.readString();
        this.movieID = in.readString();
        this.imdbID = in.readString();
        this.oriLanguage = in.readString();
        this.oriTitle = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.revenue = in.readInt();
        this.runtime = in.readInt();
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.voteCount = in.readInt();
        this.voteAverage = in.readDouble();
        this.ID = in.readInt();
        this.strBelongToCollection = in.readString();
        this.strGenres = in.readString();
        this.strProductionCompany = in.readString();
        this.strProductionCountry = in.readString();
        this.strSpokenLanguage = in.readString();
    }

    public static final Parcelable.Creator<FavoriteMovie> CREATOR = new Parcelable.Creator<FavoriteMovie>()
    {
        @Override
        public FavoriteMovie createFromParcel(Parcel source)
        {
            return new FavoriteMovie(source);
        }

        @Override
        public FavoriteMovie[] newArray(int size)
        {
            return new FavoriteMovie[size];
        }
    };
}
