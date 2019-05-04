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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import app.android.rynz.imoviecatalog.BuildConfig;
import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.model.NowPlaying;
import app.android.rynz.imoviecatalog.data.model.TopRated;
import app.android.rynz.imoviecatalog.data.model.UpComing;
import app.android.rynz.imoviecatalog.data.model.params.NowPlayingParams;
import app.android.rynz.imoviecatalog.data.model.params.TopRatedParams;
import app.android.rynz.imoviecatalog.data.model.params.UpComingParams;
import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;
import app.android.rynz.imoviecatalog.util.ExtraKeys;
import app.android.rynz.imoviecatalog.util.lib.FragmentSwitcher;
import app.android.rynz.imoviecatalog.util.lib.SimpleAlertDialog;
import app.android.rynz.imoviecatalog.view.adapter.HomeBannerAdapter;
import app.android.rynz.imoviecatalog.view.adapter.HomePosterAdapter;
import app.android.rynz.imoviecatalog.view.service.ItemMovieService;
import app.android.rynz.imoviecatalog.viewmodel.NowPlayingVM;
import app.android.rynz.imoviecatalog.viewmodel.TopRatedVM;
import app.android.rynz.imoviecatalog.viewmodel.UpComingVM;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener
{
    @BindView(R.id.label_top_rated_more)
    TextView tvTopRatedMore;
    @BindView(R.id.label_now_playing_more)
    TextView tvNowPlayingMore;
    @BindView(R.id.label_up_coming_more)
    TextView tvUpComingMore;
    @BindView(R.id.rv_home_top_rated)
    RecyclerView rvTopRated;
    @BindView(R.id.rv_home_now_playing)
    RecyclerView rvNowPlaying;
    @BindView(R.id.rv_home_up_coming)
    RecyclerView rvUpComing;
    @BindView(R.id.fragment_home_refresh)
    SwipeRefreshLayout refreshLayout;

    public HomeFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,v);
        tvTopRatedMore.setOnClickListener(this);
        tvNowPlayingMore.setOnClickListener(this);
        tvUpComingMore.setOnClickListener(this);
        if(getActivity()!=null) {
            ((HomeActivity)getActivity()).getSupportActBar().setTitle(R.string.app_title);
            ((HomeActivity)getActivity()).getSupportActBar().setDisplayHomeAsUpEnabled(false);
            ((HomeActivity)getActivity()).getSupportActBar().setDisplayShowHomeEnabled(false);
            ((HomeActivity)getActivity()).enableDrawerNavigationMenu(true);

        }
        if(getContext()!=null) refreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimaryDark),getContext().getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getData();
            }
        });
        getData();
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    private void getData()
    {
        //Top Rated Movie
        TopRatedParams topRatedParams =new TopRatedParams();
        topRatedParams.requiredParams(BuildConfig.TMDBApiKey);
        TopRatedVM topRatedVM;
        if(getActivity()!=null)
            topRatedVM = ViewModelProviders.of(getActivity()).get(TopRatedVM.class);
        else
            topRatedVM = ViewModelProviders.of(this).get(TopRatedVM.class);
        Observer<TopRated> topRatedObserver=new Observer<TopRated>()
        {
            @Override
            public void onChanged(@Nullable TopRated topRatedMovieModel)
            {
                if(topRatedMovieModel==null)
                {
                    SimpleAlertDialog.getInstance()
                            .BuildAlert(getContext(),getString(R.string.dialog_title_information),getString(R.string.dialog_msg_cannot_load_data),false,false)
                            .Basic(getString(R.string.dialog_confirm_ok));
                }
                else
                {
                    renderTopRatedList(topRatedMovieModel);
                }
            }
        };
        topRatedVM.topRatedMovieLiveData(topRatedParams).observe(getActivity(),topRatedObserver);

        //Now Playing
        NowPlayingParams nowPlayingParams=new NowPlayingParams();
        nowPlayingParams.requiredParams(BuildConfig.TMDBApiKey);
        NowPlayingVM nowPlayingVM = ViewModelProviders.of(getActivity()).get(NowPlayingVM.class);
        Observer<NowPlaying> nowPlayingObserver=new Observer<NowPlaying>()
        {
            @Override
            public void onChanged(@Nullable NowPlaying nowPlayingMovieModel)
            {
                if(nowPlayingMovieModel==null)
                {
                    SimpleAlertDialog.getInstance()
                            .BuildAlert(getContext(),getString(R.string.dialog_title_information),getString(R.string.dialog_msg_cannot_load_data),false,false)
                            .Basic(getString(R.string.dialog_confirm_ok));
                }
                else
                {
                    renderNowPlayingList(nowPlayingMovieModel);
                }
            }
        };
        nowPlayingVM.nowPlayingMovieLiveData(nowPlayingParams).observe(getActivity(),nowPlayingObserver);

        //UpComing
        UpComingParams upComingParams =new UpComingParams();
        upComingParams.requiredParams(BuildConfig.TMDBApiKey);
        UpComingVM upComingVM =ViewModelProviders.of(getActivity()).get(UpComingVM.class);
        Observer<UpComing> upComingObserver=new Observer<UpComing>()
        {
            @Override
            public void onChanged(@Nullable UpComing upComingMovieModel)
            {
                if(upComingMovieModel==null)
                {
                    SimpleAlertDialog.getInstance()
                            .BuildAlert(getContext(),getString(R.string.dialog_title_information),getString(R.string.dialog_msg_cannot_load_data),false,false)
                            .Basic(getString(R.string.dialog_confirm_ok));
                }
                else
                {
                    renderUpComingPlayingList(upComingMovieModel);
                }
            }
        };
        upComingVM.upComingMovieLiveData(upComingParams).observe(getActivity(),upComingObserver);

        if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
    }

    private void renderTopRatedList(TopRated model)
    {
        if(getContext()!=null)
        {
            LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            if(getActivity()!=null)
            {
                int maxLimit=5;
                ArrayList<ResultMovie> movieList=new ArrayList<>();
                if(model.getMovieList().size()>maxLimit)
                {
                    for(int i=0;i<maxLimit;i++)
                    {
                        movieList.add(model.getMovieList().get(i));
                    }
                }
                TopRated topRatedModel =new TopRated(model.getPage(),
                        model.getTotal_results()
                        ,model.getTotal_pages()
                        ,movieList);
                HomeBannerAdapter topRatedAdapter=new HomeBannerAdapter(getActivity().getApplicationContext(), topRatedModel);
                rvTopRated.setLayoutManager(layoutManager);
                rvTopRated.setAdapter(topRatedAdapter);
                final Context context=getActivity().getApplicationContext();
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
    }

    private void renderNowPlayingList(NowPlaying model)
    {
        if(getContext()!=null)
        {
            LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            if(getActivity()!=null)
            {
                int maxLimit=6;
                ArrayList<ResultMovie> movieList=new ArrayList<>();
                if(model.getMovieList().size()>maxLimit)
                {
                    for(int i=0;i<maxLimit;i++)
                    {
                        movieList.add(model.getMovieList().get(i));
                    }
                }
                NowPlaying nowPlaying =new NowPlaying(model.getPage()
                        ,model.getTotal_results()
                        ,model.getTotal_pages()
                        ,model.getDates()
                        ,movieList);
                HomePosterAdapter nowPlayingAdapter=new HomePosterAdapter(getActivity().getApplicationContext());
                nowPlayingAdapter.withNowPlayingMovie(nowPlaying);
                rvNowPlaying.setLayoutManager(layoutManager);
                rvNowPlaying.setAdapter(nowPlayingAdapter);
                final Context context=getActivity().getApplicationContext();
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
    }

    private void renderUpComingPlayingList(UpComing model)
    {
        if(getContext()!=null)
        {
            //Compare to get real movie upcoming release date from current date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date currentDate = Calendar.getInstance().getTime();
            Date compareDate = null;
            ArrayList<ResultMovie> movieList=new ArrayList<>();

            for(int i=0;i<model.getMovieList().size();i++)
            {
                try
                {
                    compareDate = simpleDateFormat.parse(model.getMovieList().get(i).getRealeseDate());
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }

                if (currentDate != null && compareDate != null)
                {
                    if (currentDate.compareTo(compareDate) <= 0)
                    {
                        movieList.add(model.getMovieList().get(i));
                    }
                }
                compareDate = null;
            }

            Collections.sort(movieList,Collections.reverseOrder(ResultMovie.Comparators.RELEASE_DATE));
            model.setMovieList(movieList);
//            movieList.clear();
            if(getActivity()!=null)
            {
                LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                HomePosterAdapter upComingAdapter=new HomePosterAdapter(getActivity().getApplicationContext());
                upComingAdapter.withUpComingMovie(model);
                rvUpComing.setLayoutManager(layoutManager);
                rvUpComing.setAdapter(upComingAdapter);
                final Context context=getActivity().getApplicationContext();
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
    }

    @Override
    public void onClick(View view)
    {
        if(view==tvTopRatedMore)
        {
            if(getActivity()!=null)
            {
                Bundle bundle=new Bundle();
                bundle.putString(ExtraKeys.EXTRA_MOVIE_LIST_STATE,ExtraKeys.MOVIE_LIST_STATE_TOP_RATED);
                FragmentSwitcher.getInstance()
                        .withContext(getActivity().getApplicationContext())
                        .withContainer(R.id.home_fragment_container)
                        .withFragmentManager(getActivity().getSupportFragmentManager())
                        .withFragment(new MovieListFragment())
                        .withExtraBundle(bundle,null)
                        .setToBackStack(true)
                        .commitReplace();
            }

        }
        else if(view==tvNowPlayingMore)
        {
            if(getActivity()!=null)
            {
                Bundle bundle=new Bundle();
                bundle.putString(ExtraKeys.EXTRA_MOVIE_LIST_STATE,ExtraKeys.MOVIE_LIST_STATE_NOW_PLAYING);
                FragmentSwitcher.getInstance()
                        .withContext(getActivity().getApplicationContext())
                        .withContainer(R.id.home_fragment_container)
                        .withFragmentManager(getActivity().getSupportFragmentManager())
                        .withFragment(new MovieListFragment())
                        .withExtraBundle(bundle,null)
                        .setToBackStack(true)
                        .commitReplace();
            }
        }
        else if(view==tvUpComingMore)
        {
            if(getActivity()!=null)
            {
                Bundle bundle=new Bundle();
                bundle.putString(ExtraKeys.EXTRA_MOVIE_LIST_STATE,ExtraKeys.MOVIE_LIST_STATE_UP_COMING);
                FragmentSwitcher.getInstance()
                        .withContext(getActivity().getApplicationContext())
                        .withContainer(R.id.home_fragment_container)
                        .withFragmentManager(getActivity().getSupportFragmentManager())
                        .withFragment(new MovieListFragment())
                        .withExtraBundle(bundle,null)
                        .setToBackStack(true)
                        .commitReplace();
            }
        }
    }
}
