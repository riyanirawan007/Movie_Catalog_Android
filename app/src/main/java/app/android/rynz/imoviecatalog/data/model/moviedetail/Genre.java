package app.android.rynz.imoviecatalog.data.model.moviedetail;

public class Genre
{
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    private int id;
    private String name;

    public Genre(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
