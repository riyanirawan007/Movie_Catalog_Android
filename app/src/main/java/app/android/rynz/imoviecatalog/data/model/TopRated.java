package app.android.rynz.imoviecatalog.data.model;

import java.util.ArrayList;

import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;

public class TopRated extends SearchMovie
{
    //UpComing has exactly same structure as SearchMovie
    public TopRated(int page, int total_results, int total_pages, ArrayList<ResultMovie> movieList)
    {
        super(page, total_results, total_pages, movieList);
    }
}
