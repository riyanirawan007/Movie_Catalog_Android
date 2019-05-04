package app.android.rynz.imoviefavorite.util;
/*
 * Small library created by
 * Riyan S.I
 * riyansaputrai007@outlook.com
 * github.com/RynEL7
 * */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SimpleAlertDialog
{
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private boolean isCancelable,isCancelOnTouch;
    private Context context;

    public SimpleAlertDialog BuildAlert(@Nullable Context context, @Nullable String title, @Nullable String msg, boolean isCancelable, boolean isCancelOnTouch)
    {
        builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(isCancelable);
        this.context=context;
        this.isCancelable=isCancelable;
        this.isCancelOnTouch=isCancelOnTouch;
        return this;
    }

    public void Basic(@NonNull String OkButtonCaption, final Listener.basicAlert listener)
    {
        builder.setPositiveButton(OkButtonCaption, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(dialog.isShowing()) dialog.dismiss();
                listener.OkButtonClicked();
            }
        });
        dialog=builder.create();
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelOnTouch);
        if(context!=null) dialog.show();
    }

    public void Basic(@NonNull String OkButtonCaption)
    {
        builder.setPositiveButton(OkButtonCaption, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(dialog.isShowing()) dialog.dismiss();
            }
        });
        dialog=builder.create();
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelOnTouch);
        if(context!=null) dialog.show();
    }

    public void Confirm(@NonNull String YesButtonCaption,@NonNull String NoButtonCaption, final Listener.confirmAlert listener)
    {
        builder.setPositiveButton(YesButtonCaption, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(dialog.isShowing()) dialog.dismiss();
                listener.YesButtonCliked();
            }
        });
        builder.setNegativeButton(NoButtonCaption, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(dialog.isShowing()) dialog.dismiss();
                listener.NoButtonCliked();
            }
        });
        dialog=builder.create();
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelOnTouch);
        if(context!=null) dialog.show();
    }

    public static SimpleAlertDialog getInstance()
    {
        return new SimpleAlertDialog();
    }


    public interface Listener
    {
        interface basicAlert
        {
            void OkButtonClicked();
        }

        interface confirmAlert
        {
            void YesButtonCliked();
            void NoButtonCliked();
        }
    }

}

