package app.android.rynz.imoviefavorite.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import app.android.rynz.imoviefavorite.R;
import app.android.rynz.imoviefavorite.data.model.FavoriteMovie;
import app.android.rynz.imoviefavorite.util.SimpleAlertDialog;
import app.android.rynz.imoviefavorite.view.adapter.MovieListAdapter;
import app.android.rynz.imoviefavorite.view.service.ItemMovieService;
import app.android.rynz.imoviefavorite.viewmodel.FavoriteVM;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    public final static int REQUEST_CODE_REFRESH_LIST = 100;
    @BindView(R.id.rv_fav)
    RecyclerView rvFav;
    @BindView(R.id.srl_main_list)
    SwipeRefreshLayout refreshLayout;

    private FavoriteVM favoriteVM;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        refreshLayout.setRefreshing(true);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (favoriteVM == null)
                {
                    observeFavorite();
                } else
                {
                    favoriteVM.getAllFavoriteData();
                }
            }
        });
        if (favoriteVM == null)
        {
            observeFavorite();
        } else
        {
            favoriteVM.getAllFavoriteData();
        }
    }

    private void observeFavorite()
    {
        favoriteVM = ViewModelProviders.of(this).get(FavoriteVM.class);
        Observer<ArrayList<FavoriteMovie>> observer = new Observer<ArrayList<FavoriteMovie>>()
        {
            @Override
            public void onChanged(@Nullable final ArrayList<FavoriteMovie> favoriteMovies)
            {
                if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                if (favoriteMovies != null)
                {
                    MovieListAdapter adapter = new MovieListAdapter(MainActivity.this, favoriteMovies);
                    rvFav.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setOnClickItemListener(new ItemMovieService.OnClickItemListener()
                    {
                        @Override
                        public void onClickItemListener(@NonNull FavoriteMovie movie, @NonNull View v, int position)
                        {
                            Intent detail = new Intent(MainActivity.this, DetailActivity.class);
                            detail.putExtra(DetailActivity.EXTRA_FAVORITE, movie);
                            startActivityForResult(detail, REQUEST_CODE_REFRESH_LIST);
                        }
                    });
                }
            }
        };
        favoriteVM.getAllFavoriteData().observe(this, observer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == REQUEST_CODE_REFRESH_LIST)
            {
                if (favoriteVM == null)
                {
                    observeFavorite();
                } else
                {
                    favoriteVM.getAllFavoriteData();
                }
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        SimpleAlertDialog.getInstance()
                .BuildAlert(this, getString(R.string.dialog_title_confirmation), getString(R.string.dialog_msg_exit_confirm), false, false)
                .Confirm(getString(R.string.dialog_confirm_yes), getString(R.string.dialog_confirm_no), new SimpleAlertDialog.Listener.confirmAlert()
                {
                    @Override
                    public void YesButtonCliked()
                    {
                        System.exit(0);
                    }

                    @Override
                    public void NoButtonCliked()
                    {
                        //Do nothing
                    }
                });
    }
}
