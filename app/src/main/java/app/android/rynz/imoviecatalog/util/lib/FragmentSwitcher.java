package app.android.rynz.imoviecatalog.util.lib;
/*
 * Small library created by
 * Riyan S.I
 * riyansaputrai007@outlook.com
 * github.com/RynEL7
 * */
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentSwitcher
{
    private Context context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private Bundle bundle;
    private String description;
    private boolean isSetToBackStack=false;
    private int resIDFrameContainer;

    public FragmentSwitcher(){
        //require blank constructor
    }
    public FragmentSwitcher(int resIDFrameContainer, @NonNull Context context, @NonNull FragmentManager fragmentManager)
    {
        this.resIDFrameContainer = resIDFrameContainer;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public FragmentSwitcher(int resIDFrameContainer, @NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull Fragment fragment)
    {
        this.resIDFrameContainer = resIDFrameContainer;
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.fragment = fragment;
    }

    public FragmentSwitcher(int resIDFrameContainer, @NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @Nullable Bundle bundle)
    {
        this.resIDFrameContainer = resIDFrameContainer;
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.fragment = fragment;
        this.bundle = bundle;
    }

    public FragmentSwitcher(int resIDFrameContainer, @NonNull Context context, @NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @Nullable Bundle bundle, @Nullable String description)
    {
        this.resIDFrameContainer = resIDFrameContainer;
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.fragment = fragment;
        this.bundle = bundle;
        this.description = description;
    }

    public FragmentSwitcher withContext(@NonNull Context context)
    {
        this.context=context;
        return this;
    }

    public FragmentSwitcher withContainer(int resIDFrameContainer)
    {
        this.resIDFrameContainer=resIDFrameContainer;
        return this;
    }

    public FragmentSwitcher withFragmentManager(@NonNull FragmentManager fragmentManager)
    {
        this.fragmentManager=fragmentManager;
        return this;
    }

    public FragmentSwitcher withFragment(@NonNull Fragment fragment)
    {
        this.fragment = fragment;
        return this;
    }

    public FragmentSwitcher withExtraBundle(@Nullable Bundle bundle, @Nullable String description)
    {
        this.bundle = bundle;
        this.description = description;
        return this;
    }

    public FragmentSwitcher setToBackStack(boolean isSetToBackStack)
    {
        this.isSetToBackStack = isSetToBackStack;
        return this;
    }

    public void commitAdd()
    {
        if(bundle!=null)
        {
            fragment.setArguments(bundle);
        }
        //fragment.setDescription(description); no more implemented
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(resIDFrameContainer, fragment, fragment.getClass().getSimpleName());
        if (isSetToBackStack)
        {
            fragmentTransaction.addToBackStack(fragment.getTag());
        }
        fragmentTransaction.commit();
    }

    public void commitReplace()
    {
        if(bundle!=null)
        {
            fragment.setArguments(bundle);
        }
        //fragment.setDescription(description); no more implemented
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(resIDFrameContainer, fragment, fragment.getClass().getSimpleName());
        if (isSetToBackStack)
        {
            fragmentTransaction.addToBackStack(fragment.getTag());
        }
        fragmentTransaction.commit();
    }

    public static FragmentSwitcher getInstance()
    {
        return new FragmentSwitcher();
    }
}
