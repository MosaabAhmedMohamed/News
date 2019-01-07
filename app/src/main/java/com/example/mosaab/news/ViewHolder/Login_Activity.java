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
import com.example.mosaab.news.Model.response;
import com.example.mosaab.news.R;
import com.example.mosaab.news.Remote.API_Service;
import com.example.mosaab.news.Remote.Retrofit_client;
import com.google.gson.Gson;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {


    private static final String TAG = "Login_Activity";
    private com.rey.material.widget.CheckBox checkBox_remember_me ;
    private Button login_Bu,need_account_Bu;
    private EditText email_ET,password_ET;
    private ProgressBar progressBar;


    private API_Service api_service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        Init_UI();
    }

    private void Init_UI() {


        progressBar = findViewById(R.id.progress_circular);
        checkBox_remember_me = findViewById(R.id.checkbox_remember);
        email_ET = findViewById(R.id.email_loging_ET);
        password_ET = findViewById(R.id.password_login_ET);
        login_Bu = findViewById(R.id.login_BU);
        need_account_Bu = findViewById(R.id.need_account_BU);

        Paper.init(this);
        Init_RetroFit();

        login_Bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet())
                {
                    Login_Process();
                }
                else
                {
                    Common.Show_alert_dialog(Login_Activity.this);
                }
            }
        });

        need_account_Bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Login_Activity.this,Register_Activity.class));
                finish();
            }
        });

    }

    private void Init_RetroFit()
    {
        api_service = Retrofit_client.getClient(Common.BASE_URL).create(API_Service.class);

    }


    private void Login_Process() {

        String email = email_ET.getText().toString();
        String password = password_ET.getText().toString();

        if (checkBox_remember_me.isChecked())
        {
            Paper.book().write(Common.USER_KEY,email);
            Paper.book().write(Common.PWD_KEY,password);
        }

        if(!TextUtils.isEmpty(email) &&!TextUtils.isEmpty(password))
        {

            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "Login_Process: ");

            final user user = new user("s",email,password);

            api_service.Login_User(user).enqueue(new Callback<response>() {
                @Override
                public void onResponse(Call<response> call, Response<response> response) {

                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful())
                    {
                        response response1 = response.body();

                        if (response1.getResponse().equals("success"))
                        {
                            Common.Login_status = "success";
                            getToken(response1.getResult().toString());
                            Log.d(TAG, "onResponse: "+Common.Token);
                            SendTO_main();

                        }
                    }
                    else
                    {
                        Toast.makeText(Login_Activity.this, "Error"+response.message(), Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<response> call, Throwable t) {

                    Log.d(TAG, "onFailure: "+t.getMessage());
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please fill full Info", Toast.LENGTH_SHORT).show();
        }
    }

    private String getToken(String s) {

        String token = s;
        token = token.replace("token=","");
        token = token.replace("{","");
        token = token.replace("}","");
        Common.Token = token;
        return token;
    }

    private void SendTO_main() {

        Intent Main_intent = new Intent(Login_Activity.this,MainActivity.class);
        startActivity(Main_intent);
        finish();
    }
}
