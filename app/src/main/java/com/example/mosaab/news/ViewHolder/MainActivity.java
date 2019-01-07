package com.example.mosaab.news.ViewHolder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mosaab.news.Common.Common;
import com.example.mosaab.news.Interface.ItemClickListner;
import com.example.mosaab.news.Model.AddNews;
import com.example.mosaab.news.Model.Auth;
import com.example.mosaab.news.Model.Data;
import com.example.mosaab.news.Model.News;
import com.example.mosaab.news.Model.user;
import com.example.mosaab.news.Model.response;
import com.example.mosaab.news.R;
import com.example.mosaab.news.Remote.API_Service;
import com.example.mosaab.news.Remote.Retrofit_client;
import java.util.ArrayList;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ItemClickListner {

    private static final String TAG = "MainActivity";
    private TextView update_news_title_TV;
    private EditText news_title,news_desc;
    private Button Confirm_Add_btn, Cancele_btn;
    private CardView add_update_news_card;
    private FloatingActionButton AddBtn;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private News_Adapter news_adapter;


    private ArrayList<News> dataList;
    private API_Service api_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Paper.init(this);
        InitRetorFit();

        if (Common.Login_status != null)
        {
            Log.d(TAG, "onCreate: "+Common.Token);
            Init_UI();
        }
        else {
            check_remember_me();
        }



    }

    private void Init_UI() {


        add_update_news_card = findViewById(R.id.add_update_news_card);
        update_news_title_TV = findViewById(R.id.title_tv);
        Confirm_Add_btn = findViewById(R.id.add_btn);
        Cancele_btn = findViewById(R.id.cancle_btn);
        news_title = findViewById(R.id.title_edt);
        news_desc =  findViewById(R.id.descripition_edt);
        AddBtn = findViewById(R.id.add_fab_btn);
        progressBar = findViewById(R.id.progress_circular);
        refreshLayout = findViewById(R.id.refresh_news_layout);
        recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


            refreshLayout.post(new Runnable() {
                @Override
                public void run() {

                    Check_Internet_Connection();
                    progressBar.setVisibility(View.VISIBLE);
                }
            });


        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Check_Internet_Connection();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (add_update_news_card.getVisibility() == View.GONE)
                {
                    news_title.setText("");
                    news_desc.setText("");
                    Show_add_news();

                }
                else
                {
                    add_update_news_card.setVisibility(View.GONE);
                }
            }
        });


    }

    //Check Remember me if the user saved his sign in
    private void check_remember_me()
    {
        String user =Paper.book().read(Common.USER_KEY);
        String password =Paper.book().read(Common.PWD_KEY);


        if(user != null && password !=null)
        {
            if(!user.isEmpty()&&!password.isEmpty())
            {
                remember_me_login(user,password);
            }
        }
        else
        {
            startActivity(new Intent(MainActivity.this,Login_Activity.class));
            finish();
        }
    }


    private void remember_me_login(final String email, final String password) {

        if (Common.isConnectedToInternet()) {

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please Wait...");
            mDialog.show();

            Log.d(TAG, "remember_me_login: "+email+password);

            api_service.Login_User(new user(" ",email,password)).enqueue(new Callback<response>() {
                @Override
                public void onResponse(Call<response> call, Response<response> response) {

                    if (!response.isSuccessful())
                    {
                        Log.d(TAG, "onResponse: "+response.message() + response.code());
                    }

                    response response1 = response.body();



                    getToken(response1.getResult().toString());

                    Log.d(TAG, "onResponse: "+Common.Token);

                    mDialog.dismiss();
                    Init_UI();



                }

                @Override
                public void onFailure(Call<response> call, Throwable t) {

                    Log.d(TAG, "onFailure: "+t.getMessage());
                }
            });

        }
        else {

            Common.Show_alert_dialog(this);
            return;
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

    private void Check_Internet_Connection() {

        if (Common.isConnectedToInternet())
        {
            getNews_data();
        } else
        {

            Common.Show_alert_dialog(this);
        }
    }


    private void Init_recycler_News() {
        news_adapter = new News_Adapter(dataList, this);
        refreshLayout.setRefreshing(false);
        recyclerView.setAdapter(news_adapter);
        news_adapter.setOnItemClickListner(this);

    }

    private void InitRetorFit()
    {
       api_service  = Retrofit_client.getClient(Common.BASE_URL).create(API_Service.class);
    }

    //Getting the Data from the API
    private void getNews_data() {


        Call<Data> call = api_service.get_all_news("application/json","Bearer "+Common.Token);

       call.enqueue(new Callback<Data>() {
           @Override
           public void onResponse(Call<Data> call, Response<Data> response) {
               if (!response.isSuccessful())
               {
                   Log.d(TAG, "onResponse: "+response.message());
               }


               Data data = response.body();


               dataList = new ArrayList<>();
               dataList.addAll(data.getData());
               Init_recycler_News();
               refreshLayout.setRefreshing(false);
               progressBar.setVisibility(View.GONE);
           }

           @Override
           public void onFailure(Call<Data> call, Throwable t) {

               Log.d(TAG, "onFailure: "+t.getMessage());

           }
       });



    }

    private void getAuth_user()
    {
        api_service.get_Auth_user("application/json","Bearer "+Common.Token).enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (!response.isSuccessful())
                {
                    Log.d(TAG, "onResponse:, "+response.message());
                }

                Auth auth = response.body();

                Log.d(TAG, "onResponse: "+auth.getUser().get(0).getName());
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Log.d(TAG, "onFailure: csv"+t.getMessage());

            }
        });
    }

    private void Add_news_API(final String title, final String desciription)
    {

        final AddNews addNews = new AddNews(title,desciription);

        api_service.add_news("application/x-www-form-urlencoded","Bearer "+Common.Token,addNews)
                .enqueue(new Callback<AddNews>() {
            @Override
            public void onResponse(Call<AddNews> call, Response<AddNews> response) {

                if (!response.isSuccessful())
                {
                    Log.d(TAG, "onResponse:error "+response.message());
                }

                String message = response.body().getMessage();
                Log.d(TAG, "onResponse:"+message);
                if (message.equals("News added successfully !!"))
                {

                    dataList.add(new News(title,desciription));
                    news_adapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onFailure(Call<AddNews> call, Throwable t) {
                Log.d(TAG, "onResponse: "+t.getMessage());

            }
        });
    }

    private void Show_add_news() {

        add_update_news_card.setVisibility(View.VISIBLE);

        update_news_title_TV.setText("Add new news");


        Confirm_Add_btn.setText("ADD");


            Confirm_Add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (       !TextUtils.isEmpty(news_title.getText().toString())
                            && !TextUtils.isEmpty(news_desc.getText().toString()))
                    {

                        Add_news_API(news_title.getText().toString(),news_desc.getText().toString());
                        Snackbar.make(refreshLayout,"New News "+ " "+"was added",Snackbar.LENGTH_SHORT).show();
                        add_update_news_card.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please fill full Info !", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        Cancele_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_update_news_card.setVisibility(View.GONE);
            }
        });
    }

    private void Show_update_news(final int item)
    {

        add_update_news_card.setVisibility(View.VISIBLE);

        update_news_title_TV.setText("Update news");
        news_title.setText(String.valueOf(dataList.get(item).getTitle()));
        news_desc.setText(String.valueOf(dataList.get(item).getDescription()));

        Confirm_Add_btn.setText("UPDATE");

            Confirm_Add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(news_title.getText().toString()) && !TextUtils.isEmpty(news_desc.getText().toString()))
                    {
                        final String updated_title = news_title.getText().toString();
                        final String updated_desciription = news_desc.getText().toString();

                        api_service.update_news("application/x-www-form-urlencoded","Bearer "+Common.Token ,
                                dataList.get(item).getId(),new News(updated_title,updated_desciription))
                                .enqueue(new Callback<News>() {

                                    @Override
                                    public void onResponse(Call<News> call, Response<News> response) {

                                        if (response.isSuccessful())
                                        {
                                            Log.d(TAG, "onResponse: ");
                                            dataList.set(item,new News(updated_title,updated_desciription));
                                            news_adapter.notifyDataSetChanged();
                                            add_update_news_card.setVisibility(View.GONE);
                                            Snackbar.make(refreshLayout, String.valueOf(item)+" "+"was updated",Snackbar.LENGTH_SHORT).show();

                                        }

                                        else {
                                            Log.d(TAG, "onResponse: "+response.message());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<News> call, Throwable t) {
                                        Log.d(TAG, "onFailure: "+t.getMessage());
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "please fill full Info !", Toast.LENGTH_SHORT).show();

                    }


                }
            });


        Cancele_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_update_news_card.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
        {
            Show_update_news(item.getOrder());
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
              Delete_fromAPI(item.getOrder());
        }

        return super.onContextItemSelected(item);
    }

    private void Delete_fromAPI(final int item) {


        api_service.delete_news("application/x-www-form-urlencoded","Bearer "+Common.Token , dataList.get(item).getId())
                .enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {

                        if (response.isSuccessful())
                        {
                            dataList.remove(item);
                            news_adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d(TAG, "onResponse: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Paper.book().destroy();
            startActivity(new Intent(MainActivity.this,Login_Activity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
