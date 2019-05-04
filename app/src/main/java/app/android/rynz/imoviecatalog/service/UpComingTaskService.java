package app.android.rynz.imoviecatalog.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import app.android.rynz.imoviecatalog.BuildConfig;
import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.data.model.UpComing;
import app.android.rynz.imoviecatalog.data.model.params.UpComingParams;
import app.android.rynz.imoviecatalog.data.model.results.ResultMovie;
import app.android.rynz.imoviecatalog.data.repository.TMDBRepository;
import app.android.rynz.imoviecatalog.task.UpComingTask;
import app.android.rynz.imoviecatalog.util.ExtraKeys;
import app.android.rynz.imoviecatalog.util.lib.DateFormatConverter;
import app.android.rynz.imoviecatalog.util.lib.EasyNotification;
import app.android.rynz.imoviecatalog.util.lib.PendingFragment;
import app.android.rynz.imoviecatalog.view.service.TMDBService;
import app.android.rynz.imoviecatalog.view.ui.DetailMovieFragment;
import app.android.rynz.imoviecatalog.view.ui.HomeActivity;

public class UpComingTaskService extends GcmTaskService
{
    private TMDBRepository tmdbRepository = new TMDBRepository();
    public static final String TAG_UPCOMING_MOVIE_LOG = "TASK_UPCOMING_MOVIES";
    public static final int DEFAULT_PERIOD = 60, DEFAULT_FLEX = 10;

    @Override
    public int onRunTask(TaskParams taskParams)
    {
        int result = 0;
        if (taskParams.getTag().equals(TAG_UPCOMING_MOVIE_LOG))
        {
            UpComingParams params = new UpComingParams();
            params.requiredParams(BuildConfig.TMDBApiKey);

            tmdbRepository.upComingMovies(params, false, new TMDBService.UpComingMovies.UpComingListener()
            {
                @Override
                public void onCompleted(@Nullable UpComing upComingMovies)
                {
                    if (upComingMovies != null)
                    {
                        if (upComingMovies.getMovieList().size() > 0)
                        {

                            //Compare to get real movie upcoming release date from current date
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date currentDate = new Date();
                            Date compareDate = null;

                            ArrayList<ResultMovie> movieList = new ArrayList<>();
                            for (int i = 0; i < upComingMovies.getMovieList().size(); i++)
                            {
                                try
                                {
                                    compareDate = simpleDateFormat.parse(upComingMovies.getMovieList().get(i).getRealeseDate());
                                } catch (ParseException e)
                                {
                                    e.printStackTrace();
                                }

                                if (compareDate != null)
                                {
                                    //Compare only date
                                    currentDate=DateFormatConverter.setZeroTime(currentDate);
                                    compareDate=DateFormatConverter.setZeroTime(compareDate);
                                    if (currentDate.compareTo(compareDate) < 0)
                                    {
                                        movieList.add(upComingMovies.getMovieList().get(i));
                                    }
                                }
                                compareDate = null;
                            }

                            Collections.sort(movieList, Collections.reverseOrder(ResultMovie.Comparators.RELEASE_DATE));
                            for (int i = 0; i < movieList.size(); i++)
                            {
                                String releaseDate = new DateFormatConverter()
                                        .withDate(movieList.get(i).getRealeseDate())
                                        .withPatternConvert(DateFormatConverter.PATTERN_DATE_SQL, DateFormatConverter.PATTERN_DATE_SPELL_COMMON, Locale.getDefault())
                                        .doConvert();

                                String title = getResources().getString(R.string.notification_upcoming_movie_title) + "! " + movieList.get(i).getTitle();
                                String desc = getResources().getString(R.string.word_movie_date_upcoming_on)
                                        + " " + releaseDate;
                                Intent home=new Intent(getApplicationContext(),HomeActivity.class);
                                home.putExtra(ExtraKeys.EXTRA_MOVIE_ID,movieList.get(i).getIdMovie());
                                home.putExtra(ExtraKeys.EXTRA_MOVIE_TITLE,movieList.get(i).getTitle());
                                home.putExtra(PendingFragment.EXTRA_TAG_PENDING_FRAGMENT, DetailMovieFragment.class.getSimpleName());
                                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        |Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        |Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        |Intent.FLAG_ACTIVITY_NEW_TASK);

                                PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),movieList.get(i).getIdMovie(),home,PendingIntent.FLAG_UPDATE_CURRENT);

                                new EasyNotification().with(getApplicationContext(), title, desc, i+1)
                                        .setContentIntent(pendingIntent)
                                        .show();
                            }
                            movieList.clear();
                        }
                    }
                }

                @Override
                public void onFailed(@Nullable String strError, @Nullable String apiRespons)
                {
                    //Do nothing
                }
            });

            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    @Override
    public void onInitializeTasks()
    {
        super.onInitializeTasks();
        UpComingTask upComingTask = new UpComingTask(this);
        upComingTask.createPeriodicTask();
    }

}
