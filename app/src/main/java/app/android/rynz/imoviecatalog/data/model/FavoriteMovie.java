package app.android.rynz.imoviecatalog.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class FavoriteMovie extends DetailMovie
{


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
        super(Boolean.valueOf(isAdult), backdropPath, null, buget, null, homePage, movieID, imdbID,
                oriLanguage, oriTitle, overview, popularity, posterPath
                , null
                , null
                , releaseDate
                , revenue, runtime, null, status, tagline, title, Boolean.valueOf(video), voteCount, voteAverage);
        this.strBelongToCollection = strBelongToCollection;
        this.strGenres = strGenres;
        this.strProductionCompany = strProductionCompany;
        this.strProductionCountry = strProductionCountry;
        this.strSpokenLanguage = strSpokenLanguage;
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
        super.writeToParcel(dest, flags);
        dest.writeInt(this.ID);
        dest.writeString(this.strBelongToCollection);
        dest.writeString(this.strGenres);
        dest.writeString(this.strProductionCompany);
        dest.writeString(this.strProductionCountry);
        dest.writeString(this.strSpokenLanguage);
    }

    protected FavoriteMovie(Parcel in)
    {
        super(in);
        this.ID = in.readInt();
        this.strBelongToCollection = in.readString();
        this.strGenres = in.readString();
        this.strProductionCompany = in.readString();
        this.strProductionCountry = in.readString();
        this.strSpokenLanguage = in.readString();
    }

    public static final Creator<FavoriteMovie> CREATOR = new Creator<FavoriteMovie>()
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
