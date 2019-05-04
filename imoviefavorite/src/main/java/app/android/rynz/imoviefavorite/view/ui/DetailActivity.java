package app.android.rynz.imoviefavorite.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Locale;

import app.android.rynz.imoviefavorite.R;
import app.android.rynz.imoviefavorite.data.contentmanager.FavoriteManager;
import app.android.rynz.imoviefavorite.data.model.FavoriteMovie;
import app.android.rynz.imoviefavorite.data.repository.TMDBApiReference;
import app.android.rynz.imoviefavorite.util.DateFormatConverter;
import app.android.rynz.imoviefavorite.util.SimpleAlertDialog;
import app.android.rynz.imoviefavorite.view.service.MainService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity
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

    public static String EXTRA_FAVORITE="favorite_data";
    private FavoriteMovie movie=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if(getIntent().getExtras()!=null)
        {
            if(getIntent().getParcelableExtra(EXTRA_FAVORITE)!=null)
            {
                movie=getIntent().getParcelableExtra(EXTRA_FAVORITE);
                if(getSupportActionBar()!=null) getSupportActionBar().setTitle(movie.getTitle());
            }
        }
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
                displayDetailMovieFav(movie);
            }
        });
        displayDetailMovieFav(movie);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_FAVORITE,movie);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        movie=savedInstanceState.getParcelable(EXTRA_FAVORITE);
        if(getSupportActionBar()!=null) getSupportActionBar().setTitle(movie.getTitle());
        displayDetailMovieFav(movie);
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

            String banner = "";
            if (model.getBackdropPath() != null)
            {
                banner = model.getBackdropPath();
            }
            Glide.with(this)
                    .load(TMDBApiReference.TMDB_POSTER_500px.concat(banner))
                    .apply(new RequestOptions().placeholder(R.drawable.no_images)
                            .error(R.drawable.no_images))
                    .thumbnail(0.9f)
                    .into(ivBanner);
            Glide.with(this)
                    .load(TMDBApiReference.TMDB_POSTER_500px + model.getPosterPath())
                    .apply(new RequestOptions().placeholder(R.drawable.no_images)
                            .error(R.drawable.no_images))
                    .thumbnail(0.9f)
                    .into(ivPoster);

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
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_caption_watch) + " " + model.getTitle() + "\n" + getString(R.string.label_release_date)
                            + " " + new DateFormatConverter()
                            .withDate(model.getReleaseDate())
                            .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                            .doConvert() + "\n" + getString(R.string.label_overview) + " : " + model.getOverview());
                    share.setType("text/plain");
                    startActivity(share);
                }
            });
            ivFav.setImageResource(R.drawable.ic_favorite_white_48dp);
            ivFav.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View view)
                {
                    SimpleAlertDialog.getInstance()
                            .BuildAlert(DetailActivity.this, getString(R.string.dialog_title_information), getString(R.string.dialog_confirm_fav), false, false)
                            .Confirm(getString(R.string.dialog_confirm_yes)
                                    , getString(R.string.dialog_confirm_no)
                                    , new SimpleAlertDialog.Listener.confirmAlert()
                                    {
                                        @Override
                                        public void YesButtonCliked()
                                        {
                                            FavoriteManager.deleteFavorite(DetailActivity.this, String.valueOf(model.getID()), new MainService.FavoriteContent.onDeleteListener()
                                            {
                                                @Override
                                                public void onStart()
                                                {

                                                }

                                                @Override
                                                public void onCompleted(int returnValue)
                                                {
                                                    ivFav.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                                                    Toast.makeText(DetailActivity.this, getString(R.string.notice_delete_fav), Toast.LENGTH_SHORT).show();
                                                    setResult(RESULT_OK);
                                                    finish();
                                                }
                                            });
                                        }

                                        @Override
                                        public void NoButtonCliked()
                                        {
                                        }
                                    });
                }
            });

        } else
        {
            llDetailContainer.setVisibility(View.GONE);
            tvLoadInfo.setVisibility(View.VISIBLE);
            tvLoadInfo.setText(R.string.notice_detail_fav_not_available);
        }
    }
}
