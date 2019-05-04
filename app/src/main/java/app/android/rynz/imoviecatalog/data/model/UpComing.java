package app.android.rynz.imoviecatalog.data.model;

import java.util.ArrayList;

import app.android.rynz.imoviecatalog.data.model.results.ResultDate;
import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;

public class UpComing extends NowPlaying
{
    //UpComing has exactly same structure as NowPlaying
    public UpComing(int page, int total_results, int total_pages, ResultDate dates, ArrayList<ResultMovie> movieList)
    {
        super(page, total_results, total_pages, dates, movieList);
    }
}
