package com.example.mosaab.news.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mosaab.news.R;


import java.io.IOException;

public class Common {

     public static final com.example.mosaab.news.Model.user user = null;
     public static String Token ;
     public static String Login_status;

     public static final String BASE_URL ="http://192.232.198.55/~khaled/api/";

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String USER_KEY = "USER_KEY";
    public static final String PWD_KEY = "PWD_KEY";

    public static boolean  isConnectedToInternet()
     {
         Runtime runtime = Runtime.getRuntime();
         try {
             Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
             int     exitValue = ipProcess.waitFor();
             return (exitValue == 0);
         }
         catch (IOException e)          { e.printStackTrace(); }
         catch (InterruptedException e) { e.printStackTrace(); }

         return false;
     }

     public static void Show_alert_dialog(final Context context)
     {
          Dialog builder = new Dialog(context);

         View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null);


         TextView title =  view.findViewById(R.id.title);
         Button alert_btn = view.findViewById(R.id.alert_btn);

         title.setText("Please make sure you are connected to Internet");

         alert_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ((Activity) context).finish();

             }
         });

         builder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
         builder.setContentView(view);
         builder.show();

     }

}
