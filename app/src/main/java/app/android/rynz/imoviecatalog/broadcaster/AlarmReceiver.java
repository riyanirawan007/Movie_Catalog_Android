package app.android.rynz.imoviecatalog.broadcaster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

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
import app.android.rynz.imoviecatalog.util.ExtraKeys;
import app.android.rynz.imoviecatalog.util.lib.DateFormatConverter;
import app.android.rynz.imoviecatalog.util.lib.EasyNotification;
import app.android.rynz.imoviecatalog.util.lib.PendingFragment;
import app.android.rynz.imoviecatalog.view.service.TMDBService;
import app.android.rynz.imoviecatalog.view.ui.DetailMovieFragment;
import app.android.rynz.imoviecatalog.view.ui.HomeActivity;

public class AlarmReceiver extends BroadcastReceiver
{
    public static final String TYPE_DAILY_REMINDER = "DailyReminderAlarm";
    public static final String TYPE_DAILY_RELEASE = "DailyReleaseAlarm";
    public static final int DAILY_REMINDER_NOTIFICATION_ID = 77;
    public static final int DAILY_RELEASE_NOTIFICATION_ID = 66;

    public AlarmReceiver()
    {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        String msg = intent.getStringExtra(ExtraKeys.EXTRA_ALARM_MSG);
        String type = intent.getStringExtra(ExtraKeys.EXTRA_ALARM_TYPE);
        String title = context.getResources().getString((type.equals(TYPE_DAILY_REMINDER) ? R.string.notification_daily_reminder_title : R.string.notification_daily_release_title));
        if (type.equals(TYPE_DAILY_REMINDER))
        {
            Intent intentHome = new Intent(context, HomeActivity.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, DAILY_REMINDER_NOTIFICATION_ID, intentHome, PendingIntent.FLAG_UPDATE_CURRENT);
            new EasyNotification()
                    .with(context, title, msg, DAILY_REMINDER_NOTIFICATION_ID)
                    .setContentIntent(pendingIntent)
                    .show();
        } else
        {
            TMDBRepository repository = new TMDBRepository();
            UpComingParams params=new UpComingParams();
            params.requiredParams(BuildConfig.TMDBApiKey);
            repository.upComingMovies(params, true, new TMDBService.UpComingMovies.UpComingListener()
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
                                    if (currentDate.compareTo(compareDate) == 0)
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

                                String title = context.getResources().getString(R.string.notification_release_movie_title)
                                        + "! " + movieList.get(i).getTitle();
                                String desc = context.getResources().getString(R.string.word_movie_date_release_on)
                                        + " " + releaseDate;
                                Intent home = new Intent(context, HomeActivity.class);
                                home.putExtra(ExtraKeys.EXTRA_MOVIE_ID, movieList.get(i).getIdMovie());
                                home.putExtra(ExtraKeys.EXTRA_MOVIE_TITLE, movieList.get(i).getTitle());
                                home.putExtra(PendingFragment.EXTRA_TAG_PENDING_FRAGMENT, DetailMovieFragment.class.getSimpleName());
                                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);

                                PendingIntent pendingIntent = PendingIntent.getActivity(context, movieList.get(i).getIdMovie(), home, PendingIntent.FLAG_UPDATE_CURRENT);

                                new EasyNotification().with(context, title, desc, i + 1)
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
                }
            });

        }
    }

    public void setReminder(View v, Context context, String type, String time, String message)
    {
        setReminder(context, type, time, message);
        Snackbar.make(v
                , context.getResources().getString((type.equals(TYPE_DAILY_REMINDER) ? R.string.info_daily_reminder_setup : R.string.info_daily_release_setup))
                , 3500).show();
    }

    public void setReminder(Context context, String type, String time, String message)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ExtraKeys.EXTRA_ALARM_MSG, message);
        intent.putExtra(ExtraKeys.EXTRA_ALARM_TYPE, type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (type.equals(TYPE_DAILY_REMINDER) ? DAILY_REMINDER_NOTIFICATION_ID : DAILY_RELEASE_NOTIFICATION_ID), intent, 0);
        if (alarmManager != null)
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelReminder(View v, Context context, String type)
    {
        cancelReminder(context, type);
        Snackbar.make(v
                , context.getResources().getString((type.equals(TYPE_DAILY_REMINDER) ? R.string.info_daily_reminder_cancel : R.string.info_daily_release_cancel))
                , 3500).show();
    }

    public void cancelReminder(Context context, String type)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, type.equals(TYPE_DAILY_REMINDER) ? DAILY_REMINDER_NOTIFICATION_ID : DAILY_RELEASE_NOTIFICATION_ID, intent, 0);
        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }

}
