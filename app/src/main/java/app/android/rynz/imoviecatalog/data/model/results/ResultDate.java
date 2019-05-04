package app.android.rynz.imoviecatalog.data.model.results;

public class ResultDate
{
    public static final String KEY_MINIMUM ="minimum";
    public static final String KEY_MAXIMUM="maximum";

    private String minimum,maximum;

    public ResultDate(String minimum, String maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public String getMinimum()
    {
        return minimum;
    }

    public String getMaximum()
    {
        return maximum;
    }
}
