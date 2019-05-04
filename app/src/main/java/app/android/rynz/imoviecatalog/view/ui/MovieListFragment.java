package app.android.rynz.imoviecatalog.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import app.android.rynz.imoviecatalog.BuildConfig;
import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.model.FavoriteMovie;
import app.android.rynz.imoviecatalog.data.model.NowPlaying;
import app.android.rynz.imoviecatalog.data.model.SearchMovie;
import app.android.rynz.imoviecatalog.data.model.TopRated;
import app.android.rynz.imoviecatalog.data.model.UpComing;
import app.android.rynz.imoviecatalog.data.model.params.NowPlayingParams;
import app.android.rynz.imoviecatalog.data.model.params.SearchMovieParams;
import app.android.rynz.imoviecatalog.data.model.params.TopRatedParams;
import app.android.rynz.imoviecatalog.data.model.params.UpComingParams;
import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;
import app.android.rynz.imoviecatalog.util.ExtraKeys;
import app.android.rynz.imoviecatalog.util.lib.DateFormatConverter;
import app.android.rynz.imoviecatalog.util.lib.FragmentSwitcher;
import app.android.rynz.imoviecatalog.util.lib.SimpleAlertDialog;
import app.android.rynz.imoviecatalog.view.adapter.MovieListAdapter;
import app.android.rynz.imoviecatalog.view.service.ItemMovieService;
import app.android.rynz.imoviecatalog.viewmodel.FavoriteVM;
import app.android.rynz.imoviecatalog.viewmodel.NowPlayingVM;
import app.android.rynz.imoviecatalog.viewmodel.SearchMovieVM;
import app.android.rynz.imoviecatalog.viewmodel.TopRatedVM;
import app.android.rynz.imoviecatalog.viewmodel.UpComingVM;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment
{
    @BindView(R.id.rv_movie_list)
    RecyclerView rvMovieList;
    @BindView(R.id.srl_movie_list)
    SwipeRefreshLayout refreshLayout;


    private MovieListAdapter searchAdapter;
    private MovieListAdapter nowPlayingAdapter;
    private MovieListAdapter topRatedAdapter;
    private MovieListAdapter upComingAdapter;
    private MovieListAdapter favComingAdapter;

    private SearchMovieVM searchMovieVM;
    private SearchMovieParams searchMovieParams=new SearchMovieParams();

    private TopRatedVM topRatedVM;
    private TopRatedParams topRatedParams =new TopRatedParams();

    private NowPlayingVM nowPlayingVM;
    private NowPlayingParams nowPlayingParams=new NowPlayingParams();

    private UpComingVM upComingVM;
    private UpComingParams upComingParams =new UpComingParams();

    private String movieListState;
    private String keywords;
    private Bundle savedState=null;

    public MovieListFragment()
    {
        // Required empty public constructor
    }


    private Bundle saveState()
    {
        Bundle bundle=new Bundle();
        bundle.putString(ExtraKeys.EXTRA_MOVIE_LIST_STATE,movieListState);
        if(movieListState!=null)
        {
            if(movieListState.equals(ExtraKeys.MOVIE_LIST_STATE_SEARCH))
            {
                bundle.putString(ExtraKeys.EXTRA_SEARCH_KEYWORDS,keywords);
            }
        }
        return bundle;
    }

    private void loadState(@Nullable Bundle savedInstanceState)
    {
        if(savedInstanceState!=null)
        {
            //LoadState after change
            savedState=savedInstanceState.getBundle(ExtraKeys.FRAGMENT_MOVIE_LIST_BUNDLE);
        }

        //Load State Arguments
        if(savedState!=null)
        {
            movieListState=savedState.getString(ExtraKeys.EXTRA_MOVIE_LIST_STATE);
            if(movieListState!=null)
            {
                if(movieListState.equals(ExtraKeys.MOVIE_LIST_STATE_SEARCH))
                {
                    keywords=savedState.getString(ExtraKeys.EXTRA_SEARCH_KEYWORDS);
                }
            }
        }
        else
        {
            if(getArguments()!=null)
            {
                movieListState=getArguments().getString(ExtraKeys.EXTRA_MOVIE_LIST_STATE);
            }
        }
        savedState=null;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        savedState=saveState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBundle(ExtraKeys.FRAGMENT_MOVIE_LIST_BUNDLE,savedState!=null?savedState:saveState());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        loadState(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this,v);
        loadState(savedInstanceState);
        setHasOptionsMenu(true);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimaryDark));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if(movieListState!=null)
                {
                    switch (movieListState)
                    {
                        case ExtraKeys.MOVIE_LIST_STATE_SEARCH:{
                            searchMovieParams.requiredParams(BuildConfig.TMDBApiKey,keywords);
                            searchMovieVM.searchMoviesLiveData(searchMovieParams);
                            break;
                        }
                        case ExtraKeys.MOVIE_LIST_STATE_TOP_RATED:{
                            topRatedVM.topRatedMovieLiveData(topRatedParams);
                            getTopRatedMovie();
                            break;
                        }
                        case ExtraKeys.MOVIE_LIST_STATE_NOW_PLAYING:{
                            nowPlayingVM.nowPlayingMovieLiveData(nowPlayingParams);
                            getNowPlayingMovie();
                            break;
                        }
                        case ExtraKeys.MOVIE_LIST_STATE_UP_COMING:{
                            upComingVM.upComingMovieLiveData(upComingParams);
                            getUpComingMovie();
                            break;
                        }
                        case ExtraKeys.MOVIE_LIST_STATE_FAVORITE:{
                            getFavoriteMovie();
                            break;
                        }
                    }
                }
            }
        });
        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        if(movieListState!=null)
        {
            switch (movieListState)
            {
                case ExtraKeys.MOVIE_LIST_STATE_SEARCH:{
                    inflater.inflate(R.menu.movie_list_menu,menu);
                    break;
                }
                case ExtraKeys.MOVIE_LIST_STATE_TOP_RATED:{
                    if(getActivity()!=null) {
                        ((HomeActivity)getActivity()).enableDrawerNavigationMenu(false);
                        ((HomeActivity)getActivity()).getSupportActBar().setTitle(R.string.label_top_rated);
                        ((HomeActivity)getActivity()).getSupportActBar().setDisplayHomeAsUpEnabled(true);
                        ((HomeActivity)getActivity()).getSupportActBar().setDisplayShowHomeEnabled(true);
                    }
                    break;
                }
                case ExtraKeys.MOVIE_LIST_STATE_NOW_PLAYING:{
                    if(getActivity()!=null) {
                        ((HomeActivity)getActivity()).enableDrawerNavigationMenu(false);
                        ((HomeActivity)getActivity()).getSupportActBar().setTitle(R.string.label_now_playing);
                        ((HomeActivity)getActivity()).getSupportActBar().setDisplayHomeAsUpEnabled(true);
                        ((HomeActivity)getActivity()).getSupportActBar().setDisplayShowHomeEnabled(true);
                    }
                    break;
                }
                case ExtraKeys.MOVIE_LIST_STATE_UP_COMING:{
                    if(getActivity()!=null) {
                        ((HomeActivity)getActivity()).enableDrawerNavigationMenu(false);
                        ((HomeActivity)getActivity()).getSupportActBar().setTitle(R.string.label_up_coming);
                        ((HomeActivity)getActivity()).getSupportActBar().setDisplayHomeAsUpEnabled(true);
                        ((HomeActivity)getActivity()).getSupportActBar().setDisplayShowHomeEnabled(true);
                    }
                    break;
                }
                case ExtraKeys.MOVIE_LIST_STATE_FAVORITE:{
                    if(getActivity()!=null) {
                        ((HomeActivity)getActivity()).enableDrawerNavigationMenu(false);
                        ((HomeActivity)getActivity()).getSupportActBar().setTitle(R.string.label_fav);
                        ((HomeActivity)getActivity()).getSupportActBar().setDisplayHomeAsUpEnabled(true);
                        ((HomeActivity)getActivity()).getSupportActBar().setDisplayShowHomeEnabled(true);
                    }
                    break;
                }
            }
        }
        else
        {
            if(getActivity()!=null) getActivity().onBackPressed();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        switch (movieListState)
        {
            case ExtraKeys.MOVIE_LIST_STATE_SEARCH:{
                searchMovieParams.requiredParams(BuildConfig.TMDBApiKey,"");
                getSearchMovie();
                MenuItem searchMenuItem=menu.findItem(R.id.action_search_movie);
                final SearchView searchView=(SearchView) searchMenuItem.getActionView();
                searchView.setQueryHint(getResources().getString(R.string.label_search_movie));
                searchMenuItem.expandActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
                {
                    @Override
                    public boolean onQueryTextSubmit(String s)
                    {
                        keywords=s;
                        if(!TextUtils.isEmpty(keywords))
                        {
                            searchMovieParams.requiredParams(BuildConfig.TMDBApiKey,keywords);
                            searchMovieVM.searchMoviesLiveData(searchMovieParams);
                            searchView.clearFocus();
                        }else
                        {
                            SimpleAlertDialog.getInstance()
                                    .BuildAlert(getContext(),getString(R.string.dialog_title_information)
                                            ,getString(R.string.search_tell_cant_empty_keywords),false,false)
                                    .Basic(getString(R.string.dialog_confirm_ok));
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s)
                    {
                        return false;
                    }
                });
                searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener()
                {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem)
                    {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem)
                    {
                        if(getActivity()!=null) getActivity().onBackPressed();
                        return true;
                    }
                });

                if(keywords!=null)
                {
                    searchView.setQuery(keywords,true);
                }
                break;
            }
            case ExtraKeys.MOVIE_LIST_STATE_TOP_RATED:
            {
                topRatedParams.requiredParams(BuildConfig.TMDBApiKey);
                getTopRatedMovie();
                break;
            }
            case ExtraKeys.MOVIE_LIST_STATE_NOW_PLAYING:
            {
                nowPlayingParams.requiredParams(BuildConfig.TMDBApiKey);
                getNowPlayingMovie();
                break;
            }
            case ExtraKeys.MOVIE_LIST_STATE_UP_COMING:
            {
                upComingParams.requiredParams(BuildConfig.TMDBApiKey);
                getUpComingMovie();
                break;
            }
            case ExtraKeys.MOVIE_LIST_STATE_FAVORITE:
            {
                getFavoriteMovie();
                break;
            }
        }
    }


    private void getSearchMovie()
    {
        if(getActivity()!=null)
        {
            final Context context=getActivity().getApplicationContext();
            searchMovieVM = ViewModelProviders.of(getActivity()).get(SearchMovieVM.class);
            Observer<SearchMovie> observer=new Observer<SearchMovie>()
            {
                @Override
                public void onChanged(@Nullable SearchMovie searchMovieModel)
                {
                    if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                    if(searchMovieModel!=null)
                    {
                        if(keywords!=null)
                        {
                            LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            searchAdapter=new MovieListAdapter(context).withSearchMovie(searchMovieModel);
                            rvMovieList.setLayoutManager(layoutManager);
                            rvMovieList.setAdapter(searchAdapter);
                            searchAdapter.setOnClickItemListener(new ItemMovieService.OnClickItemListener()
                            {
                                @Override
                                public void onClickItemListener(@NonNull ResultMovie movie, @NonNull View v, int position)
                                {
                                    Bundle bundle=new Bundle();
                                    bundle.putInt(ExtraKeys.EXTRA_MOVIE_ID,movie.getIdMovie());
                                    bundle.putString(ExtraKeys.EXTRA_MOVIE_TITLE,movie.getTitle());
                                    FragmentSwitcher.getInstance()
                                            .withContext(context)
                                            .withContainer(R.id.home_fragment_container)
                                            .withFragmentManager(getActivity().getSupportFragmentManager())
                                            .withFragment(new DetailMovieFragment())
                                            .withExtraBundle(bundle,null)
                                            .setToBackStack(true)
                                            .commitReplace();
                                }
                            });

                            searchAdapter.notifyDataSetChanged();
                        }
                    }
                }
            };
            if(getActivity()!=null)
                searchMovieVM.searchMoviesLiveData(searchMovieParams).observe(getActivity(),observer);
        }
    }

    private void getTopRatedMovie()
    {
        if(getActivity()!=null)
        {
            final Context context=getActivity().getApplicationContext();
            topRatedVM = ViewModelProviders.of(getActivity()).get(TopRatedVM.class);
            Observer<TopRated> observer=new Observer<TopRated>()
            {
                @Override
                public void onChanged(@Nullable TopRated topRatedMovieModel)
                {
                    if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                    if(topRatedMovieModel!=null)
                    {
                        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        topRatedAdapter=new MovieListAdapter(context).withTopRatedMovie(topRatedMovieModel);
                        rvMovieList.setLayoutManager(layoutManager);
                        rvMovieList.setAdapter(topRatedAdapter);
                        topRatedAdapter.setOnClickItemListener(new ItemMovieService.OnClickItemListener()
                        {
                            @Override
                            public void onClickItemListener(@NonNull ResultMovie movie, @NonNull View v, int position)
                            {
                                Bundle bundle=new Bundle();
                                bundle.putInt(ExtraKeys.EXTRA_MOVIE_ID,movie.getIdMovie());
                                bundle.putString(ExtraKeys.EXTRA_MOVIE_TITLE,movie.getTitle());
                                FragmentSwitcher.getInstance()
                                        .withContext(context)
                                        .withContainer(R.id.home_fragment_container)
                                        .withFragmentManager(getActivity().getSupportFragmentManager())
                                        .withFragment(new DetailMovieFragment())
                                        .withExtraBundle(bundle,null)
                                        .setToBackStack(true)
                                        .commitReplace();
                            }
                        });

                        topRatedAdapter.notifyDataSetChanged();
                    }
                }
            };
            if(getActivity()!=null)
                topRatedVM.topRatedMovieLiveData(topRatedParams).observe(getActivity(),observer);
        }
    }

    private void getNowPlayingMovie()
    {
        if(getActivity()!=null)
        {
            final Context context=getActivity().getApplicationContext();
            nowPlayingVM = ViewModelProviders.of(getActivity()).get(NowPlayingVM.class);
            Observer<NowPlaying> observer=new Observer<NowPlaying>()
            {
                @Override
                public void onChanged(@Nullable NowPlaying nowPlayingMovieModel)
                {
                    if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                    if(nowPlayingMovieModel!=null)
                    {
                        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        nowPlayingAdapter=new MovieListAdapter(context).withNowPlayingMovie(nowPlayingMovieModel);
                        rvMovieList.setLayoutManager(layoutManager);
                        rvMovieList.setAdapter(nowPlayingAdapter);
                        nowPlayingAdapter.setOnClickItemListener(new ItemMovieService.OnClickItemListener()
                        {
                            @Override
                            public void onClickItemListener(@NonNull ResultMovie movie, @NonNull View v, int position)
                            {
                                Bundle bundle=new Bundle();
                                bundle.putInt(ExtraKeys.EXTRA_MOVIE_ID,movie.getIdMovie());
                                bundle.putString(ExtraKeys.EXTRA_MOVIE_TITLE,movie.getTitle());
                                FragmentSwitcher.getInstance()
                                        .withContext(context)
                                        .withContainer(R.id.home_fragment_container)
                                        .withFragmentManager(getActivity().getSupportFragmentManager())
                                        .withFragment(new DetailMovieFragment())
                                        .withExtraBundle(bundle,null)
                                        .setToBackStack(true)
                                        .commitReplace();
                            }
                        });

                        nowPlayingAdapter.notifyDataSetChanged();
                    }
                }
            };
            if(getActivity()!=null)
                nowPlayingVM.nowPlayingMovieLiveData(nowPlayingParams).observe(getActivity(),observer);
        }
    }

    private void getUpComingMovie()
    {
        if(getActivity()!=null)
        {
            final Context context=getActivity().getApplicationContext();
            upComingVM = ViewModelProviders.of(getActivity()).get(UpComingVM.class);
            Observer<UpComing> observer=new Observer<UpComing>()
            {
                @Override
                public void onChanged(@Nullable UpComing upComingMovieModel)
                {
                    if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                    if(upComingMovieModel!=null)
                    {
                        //Compare to get real movie upcoming release date from current date
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date currentDate = new Date();
                        Date compareDate = null;
                        ArrayList<ResultMovie> movieList=new ArrayList<>();

                        for(int i=0;i<upComingMovieModel.getMovieList().size();i++)
                        {
                            try
                            {
                                compareDate = simpleDateFormat.parse(upComingMovieModel.getMovieList().get(i).getRealeseDate());
                            } catch (ParseException e)
                            {
                                e.printStackTrace();
                            }

                            if (compareDate != null)
                            {
                                //Compare only date
                                currentDate= DateFormatConverter.setZeroTime(currentDate);
                                compareDate=DateFormatConverter.setZeroTime(compareDate);
                                if (currentDate.compareTo(compareDate) < 0)
                                {
                                    movieList.add(upComingMovieModel.getMovieList().get(i));
                                }
                            }
                            compareDate = null;
                        }

                        Collections.sort(movieList,Collections.reverseOrder(ResultMovie.Comparators.RELEASE_DATE));
                        upComingMovieModel.setMovieList(movieList);


                        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        upComingAdapter=new MovieListAdapter(context).withUpComingMovie(upComingMovieModel);
                        rvMovieList.setLayoutManager(layoutManager);
                        rvMovieList.setAdapter(upComingAdapter);
                        upComingAdapter.setOnClickItemListener(new ItemMovieService.OnClickItemListener()
                        {
                            @Override
                            public void onClickItemListener(@NonNull ResultMovie movie, @NonNull View v, int position)
                            {
                                Bundle bundle=new Bundle();
                                bundle.putInt(ExtraKeys.EXTRA_MOVIE_ID,movie.getIdMovie());
                                bundle.putString(ExtraKeys.EXTRA_MOVIE_TITLE,movie.getTitle());
                                FragmentSwitcher.getInstance()
                                        .withContext(context)
                                        .withContainer(R.id.home_fragment_container)
                                        .withFragmentManager(getActivity().getSupportFragmentManager())
                                        .withFragment(new DetailMovieFragment())
                                        .withExtraBundle(bundle,null)
                                        .setToBackStack(true)
                                        .commitReplace();
                            }
                        });

                        upComingAdapter.notifyDataSetChanged();
                    }
                }
            };
            if(getActivity()!=null)
                upComingVM.upComingMovieLiveData(upComingParams).observe(getActivity(),observer);
        }
    }

    private void getFavoriteMovie()
    {
        if(getActivity()!=null)
        {
            final Context context=getActivity().getApplicationContext();
            FavoriteVM favoriteVM = ViewModelProviders.of(getActivity()).get(FavoriteVM.class);
            Observer<ArrayList<FavoriteMovie>> observer=new Observer<ArrayList<FavoriteMovie>>()
            {
                @Override
                public void onChanged(@Nullable final ArrayList<FavoriteMovie> favoriteMovieModels)
                {
                    if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                    if(favoriteMovieModels!=null)
                    {
                        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        favComingAdapter=new MovieListAdapter(context).withFavMovie(favoriteMovieModels);
                        rvMovieList.setLayoutManager(layoutManager);
                        rvMovieList.setAdapter(favComingAdapter);
                        favComingAdapter.setOnClickItemListener(new ItemMovieService.OnClickItemListener()
                        {
                            @Override
                            public void onClickItemListener(@NonNull ResultMovie movie, @NonNull View v, int position)
                            {
                                FavoriteMovie item =favoriteMovieModels.get(position);
                                Bundle bundle=new Bundle();
                                bundle.putInt(ExtraKeys.EXTRA_FAV_ID,item.getID());
                                bundle.putString(ExtraKeys.EXTRA_MOVIE_TITLE,item.getTitle());
                                FragmentSwitcher.getInstance()
                                        .withContext(context)
                                        .withContainer(R.id.home_fragment_container)
                                        .withFragmentManager(getActivity().getSupportFragmentManager())
                                        .withFragment(new DetailMovieFragment())
                                        .withExtraBundle(bundle,null)
                                        .setToBackStack(true)
                                        .commitReplace();
                            }
                        });

                        favComingAdapter.notifyDataSetChanged();
                    }
                }
            };
            if(getActivity()!=null)
                favoriteVM.getAllFavoriteData().observe(getActivity(),observer);
        }
    }

}
