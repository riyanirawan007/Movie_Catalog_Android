package app.android.rynz.imoviecatalog.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Locale;

import app.android.rynz.imoviecatalog.BuildConfig;
import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.contentmanager.FavoriteManager;
import app.android.rynz.imoviecatalog.data.model.DetailMovie;
import app.android.rynz.imoviecatalog.data.model.FavoriteMovie;
import app.android.rynz.imoviecatalog.data.model.params.DetailParams;
import app.android.rynz.imoviecatalog.data.repository.TMDBApiReference;
import app.android.rynz.imoviecatalog.util.ExtraKeys;
import app.android.rynz.imoviecatalog.util.lib.DateFormatConverter;
import app.android.rynz.imoviecatalog.util.lib.SimpleAlertDialog;
import app.android.rynz.imoviecatalog.view.service.MainService;
import app.android.rynz.imoviecatalog.viewmodel.DetailVM;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailMovieFragment extends Fragment
{

    @BindView(R.id.srl_detail)
    SwipeRefreshLayout srlDetail;
    @BindView(R.id.tv_detail_load_info)
    TextView tvLoadInfo;
    @BindView(R.id.ll_detail_container)
    LinearLayout llDetailContainer;

    @BindView(R.id.iv_detail_banner)
    ImageView ivBanner;
    @BindView(R.id.iv_detail_share)
    ImageView ivShare;
    @BindView(R.id.iv_detail_fav)
    ImageView ivFav;
    @BindView(R.id.Iv_detail_poster)
    ImageView ivPoster;
    @BindView(R.id.tv_detail_title)
    TextView tvTitle;
    @BindView(R.id.tv_detail_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_detail_score)
    TextView tvScore;
    @BindView(R.id.tv_detail_runtime)
    TextView tvRuntime;
    @BindView(R.id.tv_detail_status)
    TextView tvStatus;
    @BindView(R.id.tv_detail_budget)
    TextView tvBudget;
    @BindView(R.id.tv_detail_genres)
    TextView tvGenres;
    @BindView(R.id.tv_detail_companies)
    TextView tvCompanies;
    @BindView(R.id.tv_detail_countries)
    TextView tvCountries;
    @BindView(R.id.tv_detail_languages)
    TextView tvLanguages;
    @BindView(R.id.tv_detail_overview)
    TextView tvOverview;

    private DetailParams params = new DetailParams();
    private DetailVM detailViewModel;

    private int movieID = 0, favID = 0;
    private String movieTitle = "Movie";
    private Bundle savedState;
    private boolean isFavorite;

    public DetailMovieFragment()
    {
        // Required empty public constructor
    }

    private Bundle saveState()
    {
        Bundle bundle = new Bundle();
        if (bundle.containsKey(ExtraKeys.EXTRA_FAV_ID))
        {
            bundle.putInt(ExtraKeys.EXTRA_FAV_ID, favID);
        }
        if (bundle.containsKey(ExtraKeys.EXTRA_MOVIE_ID))
        {
            bundle.putInt(ExtraKeys.EXTRA_MOVIE_ID, movieID);
        }
        bundle.putString(ExtraKeys.EXTRA_MOVIE_TITLE, movieTitle);
        return bundle;
    }

    private void loadState(@Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            savedState = savedInstanceState.getBundle(ExtraKeys.FRAGMENT_MOVIE_DETAIL_BUNDLE);
        }
        if (savedState != null)
        {
            if (savedState.containsKey(ExtraKeys.EXTRA_MOVIE_ID))
            {
                movieID = savedState.getInt(ExtraKeys.EXTRA_MOVIE_ID);
            }
            if (savedState.containsKey(ExtraKeys.EXTRA_FAV_ID))
            {
                favID = savedState.getInt(ExtraKeys.EXTRA_FAV_ID);
            }
            movieTitle = savedState.getString(ExtraKeys.EXTRA_MOVIE_TITLE);
        } else
        {
            if (getArguments() != null)
            {
                if (getArguments().containsKey(ExtraKeys.EXTRA_MOVIE_ID))
                {
                    movieID = getArguments().getInt(ExtraKeys.EXTRA_MOVIE_ID);
                }
                if (getArguments().containsKey(ExtraKeys.EXTRA_FAV_ID))
                {
                    favID = getArguments().getInt(ExtraKeys.EXTRA_FAV_ID);
                }
                if (getArguments().containsKey(ExtraKeys.EXTRA_MOVIE_TITLE))
                {
                    movieTitle = getArguments().getString(ExtraKeys.EXTRA_MOVIE_TITLE);
                }
            }
        }

        savedState = null;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        savedState = saveState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBundle(ExtraKeys.FRAGMENT_MOVIE_DETAIL_BUNDLE, savedState != null ? savedState : saveState());
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
        View v = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        ButterKnife.bind(this, v);
        loadState(savedInstanceState);
        setHasOptionsMenu(true);

        setRetainInstance(true);

        srlDetail.setRefreshing(true);
        srlDetail.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        llDetailContainer.setVisibility(View.GONE);
        tvLoadInfo.setVisibility(View.VISIBLE);
        tvLoadInfo.setText(R.string.loading_movie_detail);

        srlDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                llDetailContainer.setVisibility(View.GONE);
                tvLoadInfo.setVisibility(View.VISIBLE);
                tvLoadInfo.setText(R.string.loading_movie_detail);
                if (favID != 0)
                {
                    setUpDetailMovieDataObserver();
                } else
                {
                    params.requiredParams(BuildConfig.TMDBApiKey, movieID);
                    if (detailViewModel != null)
                    {
                        detailViewModel.detailMovieLiveData(params);
                    }
                }
            }
        });
        if (favID == 0)
        {
            params.requiredParams(BuildConfig.TMDBApiKey, movieID);
        }
        setUpDetailMovieDataObserver();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        if (getActivity() != null)
        {
            ((HomeActivity) getActivity()).enableDrawerNavigationMenu(false);
            ((HomeActivity) getActivity()).getSupportActBar().setTitle(movieTitle);
            ((HomeActivity) getActivity()).getSupportActBar().setDisplayHomeAsUpEnabled(true);
            ((HomeActivity) getActivity()).getSupportActBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setUpDetailMovieDataObserver()
    {
        if (getActivity() != null)
        {
            if (favID != 0)
            {

                if (getContext() != null)
                {
                    FavoriteManager.getFavoriteByID(getContext(), String.valueOf(favID), new MainService.FavoriteContent.onGetByIDListener()
                    {
                        @Override
                        public void onStart()
                        {
                            // Do Nothing
                        }

                        @Override
                        public void onCompleted(@Nullable FavoriteMovie favoriteMovie)
                        {
                            displayDetailMovieFav(favoriteMovie);
                        }
                    });
                }
            } else
            {
                detailViewModel = ViewModelProviders.of(this).get(DetailVM.class);
                Observer<DetailMovie> observer = new Observer<DetailMovie>()
                {
                    @Override
                    public void onChanged(@Nullable DetailMovie detailMovieModel)
                    {
                        displayDetailMovie(detailMovieModel);
                    }
                };
                detailViewModel.detailMovieLiveData(params).observe(this, observer);
            }
        }
    }

    private void displayDetailMovieFav(final FavoriteMovie model)
    {
        if (srlDetail.isRefreshing())
        {
            srlDetail.setRefreshing(false);
        }
        if (model != null)
        {
            llDetailContainer.setVisibility(View.VISIBLE);
            tvLoadInfo.setVisibility(View.GONE);
            if (getActivity() != null)
            {
                String banner = "";
                if (model.getBackdropPath() != null)
                {
                    banner = model.getBackdropPath();
                }
                Glide.with(getActivity().getApplicationContext())
                        .load(TMDBApiReference.TMDB_POSTER_500px.concat(banner))
                        .apply(new RequestOptions().placeholder(R.drawable.no_images)
                                .error(R.drawable.no_images))
                        .thumbnail(0.9f)
                        .into(ivBanner);
                Glide.with(getActivity().getApplicationContext())
                        .load(TMDBApiReference.TMDB_POSTER_500px + model.getPosterPath())
                        .apply(new RequestOptions().placeholder(R.drawable.no_images)
                                .error(R.drawable.no_images))
                        .thumbnail(0.9f)
                        .into(ivPoster);
            }

            tvTitle.setText(model.getTitle());
            tvReleaseDate.setText(
                    new DateFormatConverter().withDate(model.getReleaseDate())
                            .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                            .doConvert());
            final String score = model.getVoteAverage() + "/10 (" + model.getVoteCount() + " votes)";
            tvScore.setText(score);
            String runtime = getString(R.string.undefined_content);
            if (model.getRuntime() != 0)
            {
                runtime = model.getRuntime() + " minutes";
            }
            tvRuntime.setText(runtime);
            tvStatus.setText(model.getStatus());
            String budget = model.getBuget() + " USD";
            tvBudget.setText(budget);
            String genres = "-";
            if (model.getStrGenres() != null) genres = model.getStrGenres();
            tvGenres.setText(genres);

            String companies = "-";
            if (model.getStrProductionCompany() != null)
                companies = model.getStrProductionCompany();
            tvCompanies.setText(companies);

            String countries = "-";
            if (model.getStrProductionCountry() != null)
                countries = model.getStrProductionCountry();
            tvCountries.setText(countries);

            String languages = "-";
            if (model.getStrSpokenLanguage() != null) languages = model.getStrSpokenLanguage();
            tvLanguages.setText(languages);

            tvOverview.setText(model.getOverview());
            ivShare.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (getContext() != null)
                    {
                        Intent share = new Intent();
                        share.setAction(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_TEXT, getContext().getString(R.string.share_caption_watch) + " " + model.getTitle() + "\n" + getContext().getResources().getString(R.string.label_release_date)
                                + " " + new DateFormatConverter()
                                .withDate(model.getReleaseDate())
                                .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                                .doConvert() + "\n" + getContext().getResources().getString(R.string.label_overview) + " : " + model.getOverview());
                        share.setType("text/plain");
                        getContext().startActivity(share);
                    }
                }
            });
            ivFav.setImageResource(R.drawable.ic_favorite_white_48dp);
            ivFav.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (getContext() != null)
                    {
                        SimpleAlertDialog.getInstance()
                                .BuildAlert(getContext(), getString(R.string.dialog_title_information), getString(R.string.dialog_confirm_fav), false, false)
                                .Confirm(getString(R.string.dialog_confirm_yes)
                                        , getString(R.string.dialog_confirm_no)
                                        , new SimpleAlertDialog.Listener.confirmAlert()
                                        {
                                            @Override
                                            public void YesButtonCliked()
                                            {
                                                FavoriteManager.deleteFavorite(getContext(), String.valueOf(favID), new MainService.FavoriteContent.onDeleteListener()
                                                {
                                                    @Override
                                                    public void onStart()
                                                    {

                                                    }

                                                    @Override
                                                    public void onCompleted(int returnValue)
                                                    {
                                                        ivFav.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                                                        if (getView() != null)
                                                            Snackbar.make(getView(), model.getTitle() + " " + getString(R.string.notice_delete_fav), Snackbar.LENGTH_LONG).show();
                                                        if (getActivity() != null)
                                                        {
                                                            getActivity().onBackPressed();
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void NoButtonCliked()
                                            {
                                            }
                                        });
                    }
                }
            });

        } else
        {
            llDetailContainer.setVisibility(View.GONE);
            tvLoadInfo.setVisibility(View.VISIBLE);
            tvLoadInfo.setText(R.string.notice_detail_fav_not_available);
        }
    }

    private void displayDetailMovie(final DetailMovie model)
    {
        if (srlDetail.isRefreshing())
        {
            srlDetail.setRefreshing(false);
        }
        if (model != null)
        {
            llDetailContainer.setVisibility(View.VISIBLE);
            tvLoadInfo.setVisibility(View.GONE);
            if (getActivity() != null)
            {
                String banner = "";
                if (model.getBackdropPath() != null)
                {
                    banner = model.getBackdropPath();
                }
                Glide.with(getActivity().getApplicationContext())
                        .load(TMDBApiReference.TMDB_POSTER_500px.concat(banner))
                        .apply(new RequestOptions().placeholder(R.drawable.no_images)
                                .error(R.drawable.no_images))
                        .thumbnail(0.9f)
                        .into(ivBanner);
                Glide.with(getActivity().getApplicationContext())
                        .load(TMDBApiReference.TMDB_POSTER_500px + model.getPosterPath())
                        .apply(new RequestOptions().placeholder(R.drawable.no_images)
                                .error(R.drawable.no_images))
                        .thumbnail(0.9f)
                        .into(ivPoster);
            }

            tvTitle.setText(model.getTitle());
            tvReleaseDate.setText(
                    new DateFormatConverter().withDate(model.getReleaseDate())
                            .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                            .doConvert());
            final String score = model.getVoteAverage() + "/10 (" + model.getVoteCount() + " votes)";
            tvScore.setText(score);
            String runtime = getString(R.string.undefined_content);
            if (model.getRuntime() != 0)
            {
                runtime = model.getRuntime() + " minutes";
            }
            tvRuntime.setText(runtime);
            tvStatus.setText(model.getStatus());
            String budget = model.getBuget() + " USD";
            tvBudget.setText(budget);
            String genres = "-";
            if (model.getGenres().size() > 0)
            {
                genres = "";
                for (int i = 0; i < model.getGenres().size(); i++)
                {
                    if (i != 0)
                    {
                        genres = genres.concat(", ");
                    }
                    genres = genres.concat(model.getGenres().get(i).getName());

                }
            }
            tvGenres.setText(genres);

            String companies = "-";
            if (model.getProductionCompanies().size() > 0)
            {
                companies = "";
                for (int i = 0; i < model.getProductionCompanies().size(); i++)
                {
                    if (i != 0)
                    {
                        companies = companies.concat(", ");
                    }
                    companies = companies.concat(model.getProductionCompanies().get(i).getName());

                }
            }
            tvCompanies.setText(companies);

            String countries = "-";
            if (model.getProductionCountries().size() > 0)
            {
                countries = "";
                for (int i = 0; i < model.getProductionCountries().size(); i++)
                {
                    if (i != 0)
                    {
                        countries = companies.concat(", ");
                    }
                    countries = countries.concat(model.getProductionCountries().get(i).getName());

                }
            }
            tvCountries.setText(countries);

            String languages = "-";
            if (model.getSpokenLanguages().size() > 0)
            {
                languages = "";
                for (int i = 0; i < model.getSpokenLanguages().size(); i++)
                {
                    if (i != 0)
                    {
                        languages = companies.concat(", ");
                    }
                    languages = languages.concat(model.getSpokenLanguages().get(i).getName());

                }
            }
            tvLanguages.setText(languages);

            tvOverview.setText(model.getOverview());
            ivShare.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (getContext() != null)
                    {
                        Intent share = new Intent();
                        share.setAction(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_TEXT, getContext().getString(R.string.share_caption_watch) + " " + model.getTitle() + "\n" + getContext().getResources().getString(R.string.label_release_date)
                                + " " + new DateFormatConverter()
                                .withDate(model.getReleaseDate())
                                .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                                .doConvert() + "\n" + getContext().getResources().getString(R.string.label_overview) + " : " + model.getOverview());
                        share.setType("text/plain");
                        getContext().startActivity(share);
                    }
                }
            });


            isFavorite = false;
            if (getContext() != null)
            {
                FavoriteManager.getFavoriteByMovieID(getContext(), model.getMovieID(), new MainService.FavoriteContent.onGetByIDListener()
                {
                    @Override
                    public void onStart()
                    {

                    }

                    @Override
                    public void onCompleted(@Nullable FavoriteMovie favoriteMovie)
                    {
                        isFavorite = (favoriteMovie != null);
                        if (!isFavorite)
                        {
                            ivFav.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                        } else
                        {
                            ivFav.setImageResource(R.drawable.ic_favorite_white_48dp);
                        }

                        ivFav.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                if (isFavorite)
                                {
                                    if (getContext() != null)
                                    {
                                        SimpleAlertDialog.getInstance()
                                                .BuildAlert(getContext(), getString(R.string.dialog_title_information), getString(R.string.dialog_notice_fav_menu), false, false)
                                                .Basic(getString(R.string.dialog_confirm_ok));
                                    }
                                } else
                                {
                                    String belongs = "";
                                    if (model.getBelongToCollection() != null)
                                    {
                                        belongs = model.getBelongToCollection().getName();
                                    }
                                    if (getContext() != null)
                                    {
                                        FavoriteMovie item=new FavoriteMovie(
                                                model.getMovieID(),
                                        String.valueOf(model.isAdult()),
                                        model.getBackdropPath(),
                                        belongs,
                                        model.getBuget(),
                                                tvGenres.getText().toString(),
                                        model.getHomePage(),
                                        model.getImdbID(),
                                        model.getOriLanguage(),
                                        model.getOriTitle(),
                                        model.getOverview(),
                                        model.getPopularity(),
                                        model.getPosterPath(),
                                        tvCompanies.getText().toString(),
                                        tvCountries.getText().toString(),
                                        model.getReleaseDate(),
                                        model.getRevenue(),
                                        model.getRuntime(),
                                        tvLanguages.getText().toString(),
                                        model.getStatus(),
                                        model.getTagline(),
                                        model.getTitle(),
                                        String.valueOf(model.isVideo()),
                                        model.getVoteCount(),
                                        model.getVoteAverage()
                                        );
                                        FavoriteManager.insertFavorite(getContext(), item, new MainService.FavoriteContent.onInsertListener()
                                        {
                                            @Override
                                            public void onStart()
                                            {

                                            }

                                            @Override
                                            public void onCompleted(Uri returnValue)
                                            {

                                                ivFav.setImageResource(R.drawable.ic_favorite_white_48dp);
                                                isFavorite=true;
                                                if (getView() != null)
                                                    Snackbar.make(getView(), model.getTitle() + " " + getString(R.string.notice_add_fav), Snackbar.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }

                            }
                        });

                    }
                });

            }
        } else
        {
            llDetailContainer.setVisibility(View.GONE);
            tvLoadInfo.setVisibility(View.VISIBLE);
            tvLoadInfo.setText(R.string.loading_detail_failed);
        }
    }
}
