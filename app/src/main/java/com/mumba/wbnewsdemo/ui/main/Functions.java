package com.mumba.wbnewsdemo.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.mumba.wbnewsdemo.R;

public class Functions {

    public static Dialog dialog;
    public static void Show_loader(Context context, boolean outside_touch, boolean cancleable) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_loading_view);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.whitebackground));


        CamomileSpinner loader=dialog.findViewById(R.id.loader);
        loader.start();


        if(!outside_touch)
            dialog.setCanceledOnTouchOutside(false);

        if(!cancleable)
            dialog.setCancelable(false);

        dialog.show();
    }


    public static void cancel_loader(){
        if(dialog!=null){
            dialog.cancel();
        }
    }
}
