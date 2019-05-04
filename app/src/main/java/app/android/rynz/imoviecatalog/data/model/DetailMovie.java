package app.android.rynz.imoviecatalog.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import app.android.rynz.imoviecatalog.data.model.moviedetail.BelongToCollection;
import app.android.rynz.imoviecatalog.data.model.moviedetail.Genre;
import app.android.rynz.imoviecatalog.data.model.moviedetail.ProductionCompany;
import app.android.rynz.imoviecatalog.data.model.moviedetail.ProductionCountry;
import app.android.rynz.imoviecatalog.data.model.moviedetail.SpokenLanguage;

public class DetailMovie implements Parcelable
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
    private @Nullable String backdropPath;
    private @Nullable
    BelongToCollection belongToCollection;
    private int buget;
    private ArrayList<Genre> genres;
    private @Nullable String homePage;
    private String movieID;
    private @Nullable String imdbID;
    private String oriLanguage;
    private String oriTitle;
    private @Nullable String overview;
    private double popularity;
    private @Nullable String posterPath;
    private ArrayList<ProductionCompany> productionCompanies;
    private ArrayList<ProductionCountry> productionCountries;
    private String releaseDate;
    private int revenue;
    private int runtime;
    private ArrayList<SpokenLanguage> spokenLanguages;
    private String status;
    private @Nullable String tagline;
    private String title;
    private boolean video;
    private int voteCount;
    private double voteAverage;

    public DetailMovie(boolean isAdult, @Nullable String backdropPath, @Nullable BelongToCollection belongToCollection, int buget, ArrayList<Genre> genres, @Nullable String homePage, String movieID, @Nullable String imdbID, String oriLanguage, String oriTitle, @Nullable String overview, double popularity, @Nullable String posterPath, ArrayList<ProductionCompany> productionCompanies, ArrayList<ProductionCountry> productionCountries, String releaseDate, int revenue, int runtime, ArrayList<SpokenLanguage> spokenLanguages, String status, @Nullable String tagline, String title, boolean video, int voteCount, double voteAverage)
    {
        this.isAdult = isAdult;
        this.backdropPath = backdropPath;
        this.belongToCollection = belongToCollection;
        this.buget = buget;
        this.genres = genres;
        this.homePage = homePage;
        this.movieID = movieID;
        this.imdbID = imdbID;
        this.oriLanguage = oriLanguage;
        this.oriTitle = oriTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.productionCompanies = productionCompanies;
        this.productionCountries = productionCountries;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.runtime = runtime;
        this.spokenLanguages = spokenLanguages;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.video = video;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
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

    @Nullable
    public BelongToCollection getBelongToCollection()
    {
        return belongToCollection;
    }

    public int getBuget()
    {
        return buget;
    }

    public ArrayList<Genre> getGenres()
    {
        return genres;
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

    public ArrayList<ProductionCompany> getProductionCompanies()
    {
        return productionCompanies;
    }

    public ArrayList<ProductionCountry> getProductionCountries()
    {
        return productionCountries;
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

    public ArrayList<SpokenLanguage> getSpokenLanguages()
    {
        return spokenLanguages;
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
        dest.writeParcelable(this.belongToCollection, flags);
        dest.writeInt(this.buget);
        dest.writeList(this.genres);
        dest.writeString(this.homePage);
        dest.writeString(this.movieID);
        dest.writeString(this.imdbID);
        dest.writeString(this.oriLanguage);
        dest.writeString(this.oriTitle);
        dest.writeString(this.overview);
        dest.writeDouble(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeList(this.productionCompanies);
        dest.writeList(this.productionCountries);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.revenue);
        dest.writeInt(this.runtime);
        dest.writeList(this.spokenLanguages);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeInt(this.voteCount);
        dest.writeDouble(this.voteAverage);
    }

    protected DetailMovie(Parcel in)
    {
        this.isAdult = in.readByte() != 0;
        this.backdropPath = in.readString();
        this.belongToCollection = in.readParcelable(BelongToCollection.class.getClassLoader());
        this.buget = in.readInt();
        this.genres = new ArrayList<Genre>();
        in.readList(this.genres, Genre.class.getClassLoader());
        this.homePage = in.readString();
        this.movieID = in.readString();
        this.imdbID = in.readString();
        this.oriLanguage = in.readString();
        this.oriTitle = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.posterPath = in.readString();
        this.productionCompanies = new ArrayList<ProductionCompany>();
        in.readList(this.productionCompanies, ProductionCompany.class.getClassLoader());
        this.productionCountries = new ArrayList<ProductionCountry>();
        in.readList(this.productionCountries, ProductionCountry.class.getClassLoader());
        this.releaseDate = in.readString();
        this.revenue = in.readInt();
        this.runtime = in.readInt();
        this.spokenLanguages = new ArrayList<SpokenLanguage>();
        in.readList(this.spokenLanguages, SpokenLanguage.class.getClassLoader());
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.voteCount = in.readInt();
        this.voteAverage = in.readDouble();
    }

}
