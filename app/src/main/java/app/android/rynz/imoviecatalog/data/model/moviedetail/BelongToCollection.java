package app.android.rynz.imoviecatalog.data.model.moviedetail;

import android.os.Parcel;
import android.os.Parcelable;

public class BelongToCollection implements Parcelable
{

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_POSTER_PATH = "poster_path";

    private int id;
    private String name, posterPath, backdropPath;

    public BelongToCollection(int id, String name, String posterPath, String backdropPath)
    {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getPosterPath()
    {
        return posterPath;
    }

    public String getBackdropPath()
    {
        return backdropPath;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
    }

    protected BelongToCollection(Parcel in)
    {
        this.id = in.readInt();
        this.name = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
    }

    public static final Parcelable.Creator<BelongToCollection> CREATOR = new Parcelable.Creator<BelongToCollection>()
    {
        @Override
        public BelongToCollection createFromParcel(Parcel source)
        {
            return new BelongToCollection(source);
        }

        @Override
        public BelongToCollection[] newArray(int size)
        {
            return new BelongToCollection[size];
        }
    };
}
