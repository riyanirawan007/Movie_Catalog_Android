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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import app.android.rynz.imoviecatalog.R;

public class PendingFragment
{
    public static final String EXTRA_TAG_PENDING_FRAGMENT="pending_fragment";
    private Context context;
    private ArrayList<Fragment> fragmentsForPending;
    private FragmentManager fragmentManager;
    private Bundle bundle;

    public static PendingFragment getInstance()
    {
        return new PendingFragment();
    }

    public PendingFragment with(@NonNull Context context,@NonNull FragmentManager fragmentManager)
    {
        this.context=context;
        this.fragmentManager=fragmentManager;
        return this;
    }

    public PendingFragment setFragmentsForPending(@NonNull ArrayList<Fragment> fragmentsForPending)
    {
        this.fragmentsForPending = fragmentsForPending;
        return this;
    }

    public PendingFragment setFragmentsForPending(@NonNull Fragment[] fragmentsForPending)
    {
        this.fragmentsForPending=new ArrayList<>();
        for(Fragment fragment:fragmentsForPending)
        {
            this.fragmentsForPending.add(fragment);
        }
        return this;
    }

    public PendingFragment setExtraBundle(@NonNull Bundle extraBundle)
    {
        bundle = extraBundle;
        return this;
    }

    public void commit()
    {
        if(bundle!=null && context!=null && fragmentManager!=null && fragmentsForPending!=null)
        {
            if(bundle.containsKey(EXTRA_TAG_PENDING_FRAGMENT))
            {
                String pendingFragmentTag=bundle.getString(EXTRA_TAG_PENDING_FRAGMENT);
                Fragment pendingFragment=fragmentManager.findFragmentByTag(pendingFragmentTag);

                if(pendingFragment!=null)
                {
                    for(int i=0;i<fragmentManager.getBackStackEntryCount();i++)
                    {
                        fragmentManager.popBackStackImmediate();
                    }
                    bundle.remove(EXTRA_TAG_PENDING_FRAGMENT);
                    FragmentSwitcher.getInstance()
                            .withContainer(R.id.home_fragment_container)
                            .withContext(context)
                            .withFragmentManager(fragmentManager)
                            .withFragment(pendingFragment)
                            .withExtraBundle(bundle,null)
                            .setToBackStack(true)
                            .commitReplace();
                }
                else
                {
                    for(Fragment fragment:fragmentsForPending)
                    {
                        if(fragment.getClass().getSimpleName().equals(pendingFragmentTag))
                        {
                            bundle.remove(EXTRA_TAG_PENDING_FRAGMENT);
                           FragmentSwitcher.getInstance()
                                    .withContainer(R.id.home_fragment_container)
                                    .withContext(context)
                                    .withFragmentManager(fragmentManager)
                                    .withFragment(fragment)
                                    .withExtraBundle(bundle,null)
                                    .setToBackStack(true)
                                    .commitReplace();
                            break;
                        }
                    }
                }
            }
        }
    }
}
