package com.example.mosaab.news.ViewHolder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mosaab.news.Common.Common;
import com.example.mosaab.news.Model.user;
import com.example.mosaab.news.R;
import com.example.mosaab.news.Remote.API_Service;
import com.example.mosaab.news.Remote.Retrofit_client;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_Activity extends AppCompatActivity {

    private static final String TAG ="Register_Activity" ;
    private EditText name_ET,email_ET,password_ET;
    private Button create_account_Bu,have_account_BU;
    private ProgressBar progressBar;

    private API_Service api_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        Init_UI();
    }

    private void Init_UI() {


        progressBar = findViewById(R.id.progress_circular);
        name_ET = findViewById(R.id.name_TV);
        email_ET = findViewById(R.id.email_reg_TV);
        password_ET = findViewById(R.id.password_reg_TV);
        create_account_Bu = findViewById(R.id.create_account_BU);
        have_account_BU = findViewById(R.id.have_account_BU);

        Init_RetroFit();

        create_account_Bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                  if (Common.isConnectedToInternet())
                  {
                      Register_Process();

                  }
                  else
                  {
                   Common.Show_alert_dialog(Register_Activity.this);
                  }

            }
        });

        have_account_BU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Register_Activity.this,Login_Activity.class));
                finish();
            }
        });
    }

    private void Init_RetroFit()
    {
        api_service = Retrofit_client.getClient(Common.BASE_URL).create(API_Service.class);

    }

    private void Register_Process()
    {
        final String name = name_ET.getText().toString();
        final String email = email_ET.getText().toString();
        String passwprd = password_ET.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(passwprd))
        {
            progressBar.setVisibility(View.VISIBLE);
            user user = new user(name,email,passwprd);

            api_service.Reg_newUser(user).enqueue(new Callback<com.example.mosaab.news.Model.user>() {
                @Override
                public void onResponse(Call<com.example.mosaab.news.Model.user> call, Response<com.example.mosaab.news.Model.user> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful())
                    {
                        SendTo_Login();

                    }

                    else
                    {
                        Toast.makeText(Register_Activity.this, "Error occurred" +response.message(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<com.example.mosaab.news.Model.user> call, Throwable t) {

                    Log.d(TAG, "onFailure: "+t.getMessage());
                }
            });
        }
        else
        {
            Toast.makeText(this, "please fill full info", Toast.LENGTH_SHORT).show();
        }

    }

    private void SendTo_Login() {

        Intent main_intent = new Intent(Register_Activity.this,Login_Activity.class);
        startActivity(main_intent);
        finish();

    }
}
