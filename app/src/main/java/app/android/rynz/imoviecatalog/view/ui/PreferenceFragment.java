package app.android.rynz.imoviecatalog.view.ui;


import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.Calendar;

import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.broadcaster.AlarmReceiver;
import app.android.rynz.imoviecatalog.task.UpComingTask;
import app.android.rynz.imoviecatalog.util.PreferenceHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreferenceFragment extends PreferenceFragmentCompat
{
    private UpComingTask upComingTask;
    private AlarmReceiver alarmReceiver=new AlarmReceiver();
    private PreferenceHelper preferenceHelper;
    private SwitchPreference upComingNotifier;
    private SwitchPreference dailyReminder;
    private SwitchPreference dailyRelease;
    private Preference language;
    public static final String TAG_KEY_UPCOMING="preference_up_coming_notifier";
    public static final String TAG_KEY_DAILY_REMINDER="preference_daily_reminder";
    public static final String TAG_KEY_DAILY_RELEASE="preference_daily_release";
    public static final String TAG_KEY_LANGUAGE="preference_language";
    public static final String DEFAULT_TIMER_DAILY_REMINDER="7:0";
    public static final String DEFAULT_TIMER_DAILY_RELEASE="8:0";

    @Override
    public void onCreatePreferences(Bundle bundle, String s)
    {
        setPreferencesFromResource(R.xml.preference,s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(getContext()!=null)
        {
            preferenceHelper=new PreferenceHelper(getContext());
            upComingTask=new UpComingTask(getContext());
        }
        bindView();
        loadPreference();
        onChange();
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        if(getActivity()!=null) {
            ((HomeActivity)getActivity()).enableDrawerNavigationMenu(false);
            ((HomeActivity)getActivity()).getSupportActBar().setTitle(getResources().getString(R.string.label_settings));
            ((HomeActivity)getActivity()).getSupportActBar().setDisplayHomeAsUpEnabled(true);
            ((HomeActivity)getActivity()).getSupportActBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void bindView()
    {
        upComingNotifier=(SwitchPreference) findPreference(TAG_KEY_UPCOMING);
        dailyReminder=(SwitchPreference) findPreference(TAG_KEY_DAILY_REMINDER);
        dailyRelease=(SwitchPreference) findPreference(TAG_KEY_DAILY_RELEASE);
        language= findPreference(TAG_KEY_LANGUAGE);
    }


    private void loadPreference()
    {
        upComingNotifier.setChecked(preferenceHelper.isUpComingNotifierEnabled());
        if(preferenceHelper.getDailyReminderTime()==null && preferenceHelper.isDefaultSettings())
        {
            String msg=getString(R.string.notification_daily_reminder_msg);
            preferenceHelper.setDailyReminder(DEFAULT_TIMER_DAILY_REMINDER,msg);
            alarmReceiver.setReminder(getContext(),AlarmReceiver.TYPE_DAILY_REMINDER,DEFAULT_TIMER_DAILY_REMINDER,msg);
        }
        if(preferenceHelper.getDailyReleaseTime()==null && preferenceHelper.isDefaultSettings())
        {
            preferenceHelper.setDailyRelease(DEFAULT_TIMER_DAILY_RELEASE,"-");
            alarmReceiver.setReminder(getContext(),AlarmReceiver.TYPE_DAILY_RELEASE,DEFAULT_TIMER_DAILY_RELEASE,"-");
        }
        dailyReminder.setChecked(preferenceHelper.getDailyReminderTime()!=null);
        dailyRelease.setChecked(preferenceHelper.getDailyReleaseTime()!=null);
        if(dailyReminder.isChecked())
        {
            String times[] = preferenceHelper.getDailyReminderTime().split(":");
            int hour = Integer.valueOf(times[0]);
            int minute = Integer.valueOf(times[1]);
            String strHour=times[0];
            String strMinute=times[1];
            if(hour<10)
            {
                strHour="0"+strHour;
            }
            if(minute<10)
            {
                strMinute="0"+strMinute;
            }
            dailyReminder.setSummary(getString(R.string.preferance_summary_daily_reminder_on).concat(" ")
                    .concat(strHour+":"+strMinute));
        }
        else
        {
            dailyReminder.setSummary(getString(R.string.preference_default_daily_reminder));
        }

        if(dailyRelease.isChecked())
        {
            String times[] = preferenceHelper.getDailyReleaseTime().split(":");
            int hour = Integer.valueOf(times[0]);
            int minute = Integer.valueOf(times[1]);
            String strHour=times[0];
            String strMinute=times[1];
            if(hour<10)
            {
                strHour="0"+strHour;
            }
            if(minute<10)
            {
                strMinute="0"+strMinute;
            }
            dailyRelease.setSummary(getString(R.string.preferance_summary_daily_release_on).concat(" ")
                    .concat(strHour+":"+strMinute));
        }
        else
        {
            dailyRelease.setSummary(getString(R.string.preference_default_daily_release));
        }

        if(preferenceHelper.isDefaultSettings())
        {
            preferenceHelper.setDefaultSettings(false);
        }
    }

    private void onChange()
    {
        upComingNotifier.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o)
            {
                boolean isChecked=!((SwitchPreference) preference).isChecked();
                if(isChecked)
                {
                    upComingTask.createPeriodicTask();
                }
                else
                {
                    upComingTask.cancelPeriodicTask();
                }
                preferenceHelper.setUpComingNotifierStat(isChecked);
                return true;
            }
        });

        dailyReminder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(final Preference preference, Object o)
            {
                boolean isChecked=!((SwitchPreference) preference).isChecked();
                if(isChecked)
                {
                    final Calendar calendar=Calendar.getInstance();
                    int mHour=calendar.get(Calendar.HOUR_OF_DAY);
                    int mMinute=calendar.get(Calendar.MINUTE);

                    String loadedTimer=preferenceHelper.getDailyReminderTime();
                    if(loadedTimer!=null)
                    {
                        String time[] = preferenceHelper.getDailyReminderTime().split(":");
                        mHour = Integer.valueOf(time[0]);
                        mMinute = Integer.valueOf(time[1]);
                    }

                    TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener()
                    {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute)
                        {
                            String time=String.valueOf(hour).concat(":").concat(String.valueOf(minute));
                            String msg = getResources().getString(R.string.notification_daily_reminder_msg);
                            dailyReminder.setChecked(true);
                            preferenceHelper.setDailyReminder(time,msg);
                            String times[] = preferenceHelper.getDailyReminderTime().split(":");
                            hour = Integer.valueOf(times[0]);
                            minute = Integer.valueOf(times[1]);
                            String strHour=times[0];
                            String strMinute=times[1];
                            if(hour<10)
                            {
                                strHour="0"+strHour;
                            }
                            if(minute<10)
                            {
                                strMinute="0"+strMinute;
                            }
                            dailyReminder.setSummary(getString(R.string.preferance_summary_daily_reminder_on).concat(" ")
                                    .concat(strHour+":"+strMinute));
                            alarmReceiver.setReminder(getView(),getContext(),AlarmReceiver.TYPE_DAILY_REMINDER,time,msg);
                        }
                    },mHour,mMinute,true);

                    timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel(DialogInterface dialogInterface)
                        {
                            dailyReminder.setSummary(getString(R.string.preference_default_daily_reminder));
                            dailyReminder.setChecked(false);
                        }
                    });

                    timePickerDialog.setCanceledOnTouchOutside(false);
                    timePickerDialog.setCancelable(false);
                    timePickerDialog.show();
                }
                else
                {
                    preferenceHelper.resetDailyReminder();
                    alarmReceiver.cancelReminder(getView(),getContext(),AlarmReceiver.TYPE_DAILY_REMINDER);
                    dailyReminder.setSummary(getString(R.string.preference_default_daily_reminder));
                }
                return true;
            }
        });

        dailyRelease.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o)
            {
                boolean isChecked=!((SwitchPreference) preference).isChecked();
                if(isChecked)
                {
                    final Calendar calendar=Calendar.getInstance();
                    int mHour=calendar.get(Calendar.HOUR_OF_DAY);
                    int mMinute=calendar.get(Calendar.MINUTE);

                    String loadedTimer=preferenceHelper.getDailyReleaseTime();
                    if(loadedTimer!=null)
                    {
                        String time[] = loadedTimer.split(":");
                        mHour = Integer.valueOf(time[0]);
                        mMinute = Integer.valueOf(time[1]);
                    }

                    TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener()
                    {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute)
                        {
                            String time=String.valueOf(hour).concat(":").concat(String.valueOf(minute));
                            String msg = "-";
                            dailyRelease.setChecked(true);
                            preferenceHelper.setDailyRelease(time,msg);
                            String times[] = preferenceHelper.getDailyReleaseTime().split(":");
                            hour = Integer.valueOf(times[0]);
                            minute = Integer.valueOf(times[1]);
                            String strHour=times[0];
                            String strMinute=times[1];
                            if(hour<10)
                            {
                                strHour="0"+strHour;
                            }
                            if(minute<10)
                            {
                                strMinute="0"+strMinute;
                            }
                            dailyRelease.setSummary(getString(R.string.preferance_summary_daily_release_on).concat(" ")
                                    .concat(strHour+":"+strMinute));
                            alarmReceiver.setReminder(getView(),getContext(),AlarmReceiver.TYPE_DAILY_RELEASE,time,msg);
                        }
                    },mHour,mMinute,true);

                    timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel(DialogInterface dialogInterface)
                        {
                            dailyRelease.setSummary(getString(R.string.preference_default_daily_release));
                            dailyRelease.setChecked(false);
                        }
                    });

                    timePickerDialog.setCanceledOnTouchOutside(false);
                    timePickerDialog.setCancelable(false);
                    timePickerDialog.show();
                }
                else
                {
                    preferenceHelper.resetDailyRelease();
                    alarmReceiver.cancelReminder(getView(),getContext(),AlarmReceiver.TYPE_DAILY_RELEASE);
                    dailyRelease.setSummary(getString(R.string.preference_default_daily_release));
                }
                return true;
            }
        });

        language.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Intent settingLanguage=new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(settingLanguage);
                return true;
            }
        });

    }
}
