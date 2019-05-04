package app.android.rynz.imoviecatalog.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.util.ExtraKeys;
import app.android.rynz.imoviecatalog.util.PreferenceHelper;
import app.android.rynz.imoviecatalog.util.lib.FragmentSwitcher;
import app.android.rynz.imoviecatalog.util.lib.PendingFragment;
import app.android.rynz.imoviecatalog.util.lib.SimpleAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private FragmentManager fragmentManager;
    private Fragment restoreFragment;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        proceedPendingFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.app_title);
        fragmentManager = getSupportFragmentManager();
        FragmentSwitcher.getInstance()
                .withContainer(R.id.home_fragment_container)
                .withContext(this)
                .withFragmentManager(fragmentManager)
                .withFragment(new HomeFragment())
                .setToBackStack(false)
                .commitReplace();
        proceedPendingFragment();
    }

    private void proceedPendingFragment()
    {
        Bundle bundle=new Bundle();
        String pendingFragmentTag=getIntent().getStringExtra(PendingFragment.EXTRA_TAG_PENDING_FRAGMENT);
        if(pendingFragmentTag!=null)
        {
            if(pendingFragmentTag.equals(DetailMovieFragment.class.getSimpleName()))
            {
                bundle.putString(PendingFragment.EXTRA_TAG_PENDING_FRAGMENT,pendingFragmentTag);
                if(getIntent().getExtras()!=null)
                {
                    if(getIntent().getExtras().containsKey(ExtraKeys.EXTRA_MOVIE_ID))
                    {
                        bundle.putInt(ExtraKeys.EXTRA_MOVIE_ID,getIntent().getIntExtra(ExtraKeys.EXTRA_MOVIE_ID,0));
                    }
                    if (getIntent().getExtras().containsKey(ExtraKeys.EXTRA_FAV_ID))
                    {
                        bundle.putInt(ExtraKeys.EXTRA_FAV_ID,getIntent().getIntExtra(ExtraKeys.EXTRA_FAV_ID,0));
                    }
                    bundle.putString(ExtraKeys.EXTRA_MOVIE_TITLE,getIntent().getStringExtra(ExtraKeys.EXTRA_MOVIE_TITLE));
                }
            }
        }

        PendingFragment.getInstance()
                .with(this,getSupportFragmentManager())
                .setFragmentsForPending(new Fragment[]{new DetailMovieFragment()
                        ,new MovieListFragment()})
                .setExtraBundle(bundle)
                .commit();
    }

    public ActionBar getSupportActBar()
    {
        return getSupportActionBar();
    }

    public void enableDrawerNavigationMenu(boolean isEnable)
    {
        drawerToggle.setDrawerIndicatorEnabled(isEnable);
        if(isEnable)
        {
//            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawerToggle.setToolbarNavigationClickListener(null);
        }
        else
        {
//            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        //Persistence fragment state even fragment in backstack
        //This method for resolve fragment retainInstance not working on backstack fragment
        String lastFragmentTag = null;
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++)
            {
                lastFragmentTag = getSupportFragmentManager().getFragments().get(i).getTag();
            }
        } else
        {
            lastFragmentTag = HomeFragment.class.getSimpleName();
        }
        outState.putString(ExtraKeys.LASTEST_FRAGMENT, lastFragmentTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            //Persistence fragment state
            //This method for resolve fragment retainInstance not working on backstack fragment
            restoreFragment = getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString(ExtraKeys.LASTEST_FRAGMENT));
            if (restoreFragment != null)
            {
                if (restoreFragment.getTag() != null)
                {
                    FragmentSwitcher.getInstance()
                            .withContext(this)
                            .withContainer(R.id.home_fragment_container)
                            .withFragmentManager(getSupportFragmentManager())
                            .withFragment(restoreFragment)
                            .setToBackStack(false)
                            .commitReplace();
                }
            }
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            {
                super.onBackPressed();
            } else
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Bundle bundle=new Bundle();
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_search:
            {
                if(restoreFragment!=null)
                {
                    restoreFragment.setArguments(null);
                }
                bundle.putString(ExtraKeys.EXTRA_MOVIE_LIST_STATE,ExtraKeys.MOVIE_LIST_STATE_SEARCH);
                FragmentSwitcher.getInstance()
                        .withContainer(R.id.home_fragment_container)
                        .withContext(HomeActivity.this)
                        .withFragmentManager(fragmentManager)
                        .withFragment(new MovieListFragment())
                        .withExtraBundle(bundle,null)
                        .setToBackStack(true)
                        .commitReplace();
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.nav_top_rated:
            {
                for(int i=0;i<getSupportFragmentManager().getBackStackEntryCount();i++)
                {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                Bundle bundle=new Bundle();
                bundle.putString(ExtraKeys.EXTRA_MOVIE_LIST_STATE,ExtraKeys.MOVIE_LIST_STATE_TOP_RATED);
                FragmentSwitcher.getInstance()
                        .withContext(this)
                        .withContainer(R.id.home_fragment_container)
                        .withFragmentManager(getSupportFragmentManager())
                        .withFragment(new MovieListFragment())
                        .withExtraBundle(bundle,null)
                        .setToBackStack(true)
                        .commitReplace();
                break;
            }
            case R.id.nav_now_playing:
            {
                for(int i=0;i<getSupportFragmentManager().getBackStackEntryCount();i++)
                {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                Bundle bundle=new Bundle();
                bundle.putString(ExtraKeys.EXTRA_MOVIE_LIST_STATE,ExtraKeys.MOVIE_LIST_STATE_NOW_PLAYING);
                FragmentSwitcher.getInstance()
                        .withContext(this)
                        .withContainer(R.id.home_fragment_container)
                        .withFragmentManager(getSupportFragmentManager())
                        .withFragment(new MovieListFragment())
                        .withExtraBundle(bundle,null)
                        .setToBackStack(true)
                        .commitReplace();
                break;
            }
            case R.id.nav_up_coming:
            {
                for(int i=0;i<getSupportFragmentManager().getBackStackEntryCount();i++)
                {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                Bundle bundle=new Bundle();
                bundle.putString(ExtraKeys.EXTRA_MOVIE_LIST_STATE,ExtraKeys.MOVIE_LIST_STATE_UP_COMING);
                FragmentSwitcher.getInstance()
                        .withContext(this)
                        .withContainer(R.id.home_fragment_container)
                        .withFragmentManager(getSupportFragmentManager())
                        .withFragment(new MovieListFragment())
                        .withExtraBundle(bundle,null)
                        .setToBackStack(true)
                        .commitReplace();
                break;
            }
            case R.id.nav_fav:
            {
                for(int i=0;i<getSupportFragmentManager().getBackStackEntryCount();i++)
                {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                Bundle bundle=new Bundle();
                bundle.putString(ExtraKeys.EXTRA_MOVIE_LIST_STATE,ExtraKeys.MOVIE_LIST_STATE_FAVORITE);
                FragmentSwitcher.getInstance()
                        .withContext(this)
                        .withContainer(R.id.home_fragment_container)
                        .withFragmentManager(getSupportFragmentManager())
                        .withFragment(new MovieListFragment())
                        .withExtraBundle(bundle,null)
                        .setToBackStack(true)
                        .commitReplace();
                break;
            }
            case R.id.nav_settings:
            {
                for(int i=0;i<getSupportFragmentManager().getBackStackEntryCount();i++)
                {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                FragmentSwitcher.getInstance()
                        .withContext(this)
                        .withContainer(R.id.home_fragment_container)
                        .withFragmentManager(getSupportFragmentManager())
                        .withFragment(new PreferenceFragment())
                        .setToBackStack(true)
                        .commitReplace();
                break;
            }
            case R.id.nav_home:
            {
                for(int i=0;i<getSupportFragmentManager().getBackStackEntryCount();i++)
                {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
