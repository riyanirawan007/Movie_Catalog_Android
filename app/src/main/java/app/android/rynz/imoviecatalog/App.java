package app.android.rynz.imoviecatalog;

import android.app.Application;

import app.android.rynz.imoviecatalog.broadcaster.AlarmReceiver;
import app.android.rynz.imoviecatalog.task.UpComingTask;
import app.android.rynz.imoviecatalog.util.PreferenceHelper;
import app.android.rynz.imoviecatalog.view.ui.PreferenceFragment;

public class App extends Application
{
    PreferenceHelper preferenceHelper;
    UpComingTask upComingTask;
    AlarmReceiver alarmReceiver;

    @Override
    public void onCreate()
    {
        super.onCreate();
        preferenceHelper =new PreferenceHelper(getApplicationContext());
        upComingTask=new UpComingTask(getApplicationContext());
        alarmReceiver=new AlarmReceiver();
        setUpDefaultSetting();
    }

    private void setUpDefaultSetting()
    {
        if(preferenceHelper.isUpComingNotifierEnabled()) {
            upComingTask.cancelPeriodicTask();
            upComingTask.createPeriodicTask();
        }
        else {
            upComingTask.cancelPeriodicTask();
        }

        if(preferenceHelper.getDailyReminderTime()==null && preferenceHelper.isDefaultSettings())
        {
            String msg=getString(R.string.notification_daily_reminder_msg);
            preferenceHelper.setDailyReminder(PreferenceFragment.DEFAULT_TIMER_DAILY_REMINDER,msg);
            alarmReceiver.setReminder(getApplicationContext(),AlarmReceiver.TYPE_DAILY_REMINDER,PreferenceFragment.DEFAULT_TIMER_DAILY_REMINDER,msg);
        }
        if(preferenceHelper.getDailyReleaseTime()==null && preferenceHelper.isDefaultSettings())
        {
            preferenceHelper.setDailyRelease(PreferenceFragment.DEFAULT_TIMER_DAILY_RELEASE,"-");
            alarmReceiver.setReminder(getApplicationContext(),AlarmReceiver.TYPE_DAILY_RELEASE,PreferenceFragment.DEFAULT_TIMER_DAILY_RELEASE,"-");
        }

    }

}
