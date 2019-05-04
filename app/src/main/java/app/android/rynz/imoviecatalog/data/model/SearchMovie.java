package app.android.rynz.imoviecatalog.data.model;

import java.util.ArrayList;

import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;

public class SearchMovie
{
    public static final String KEY_PAGE = "page";
    public static final String KEY_TOTAL_RESULT = "total_results";
    public static final String KEY_TOTAL_PAGES = "total_pages";
    public static final String KEY_SEARCH_RESULT = "results";

    private int page;
    private int total_results;
    private int total_pages;
    private ArrayList<ResultMovie> movieList;

    public SearchMovie(int page, int total_results, int total_pages, ArrayList<ResultMovie> movieList)
    {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.movieList = movieList;
    }

    public int getPage()
    {
        return page;
    }

    public int getTotal_results()
    {
        return total_results;
    }

    public int getTotal_pages()
    {
        return total_pages;
    }

    public ArrayList<ResultMovie> getMovieList()
    {
        return movieList;
    }
}
