package com.news.medianews.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.news.medianews.Adapter.MainArticleAdapter;
import com.news.medianews.Model.Article;
import com.news.medianews.Model.ResponseModel;
import com.news.medianews.R;
import com.news.medianews.Rests.ApiClient;
import com.news.medianews.Rests.ApiInterface;
import com.news.medianews.Utils.OnRecyclerViewItemClickListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    private static final String API_KEY = "cb937d9b6ff74a79a4bb12c47bf2e112";
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    Button btn_date;

    private int mYear, mMonth, mDay;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().hide();
       // setSupportActionBar(toolbar);
        nav=(NavigationView)findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
               switch (menuitem.getItemId())
                {
                    case  R.id.menu_home :
                        Toast.makeText(getApplicationContext(),"Home Panel open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case  R.id.call :
                        Toast.makeText(getApplicationContext(),"Call Panel open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case  R.id.setting :
                        Toast.makeText(getApplicationContext(),"Setting  Panel open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
*/


        btn_date = (Button) findViewById(R.id.btn_date);
        final RecyclerView mainRecycler = findViewById(R.id.activity_main_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainRecycler.setLayoutManager(linearLayoutManager);

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.getLatestNewsy("in",  API_KEY);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }

        });
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse
                    (Call<ResponseModel> call, Response<ResponseModel> response) {

                 Log.i("Response: ", response.body().toString());

                 try
                 {
                if (response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if (articleList.size() > 0) {
                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(articleList);
                        mainArticleAdapter.setOnRecyclerViewItemClickListener(MainActivity.this);
                        mainRecycler.setAdapter(mainArticleAdapter);

                    }

                    }
                } catch (Exception e) {
                     e.printStackTrace();
                 }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("out", t.toString());
            }
        });
        //Sharing




    }


    private void selectDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (year > mYear) {
                            Toast.makeText(MainActivity.this, "Please choose past dates", Toast.LENGTH_SHORT).show();
                        } else if (monthOfYear > mMonth && year >= mYear) {
                            Toast.makeText(MainActivity.this, "please choose past dates", Toast.LENGTH_SHORT).show();
                        } else if (dayOfMonth > mDay && monthOfYear >= mMonth && year >= mYear) {
                            Toast.makeText(MainActivity.this, "please choose past or present dates", Toast.LENGTH_SHORT).show();
                        } else {

                            String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            Log.i("date", date);
                            Intent intent = new Intent(MainActivity.this, NewsByDate.class);
                            intent.putExtra("Date", date);
                            startActivity(intent);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    @Override
    public void onItemClick(int position, View view) {
        switch (view.getId()) {
            case R.id.article_adapter_ll_parent:
                Article article = (Article) view.getTag();
                if (!TextUtils.isEmpty(article.getUrl())) {
                    Log.e("clicked url", article.getUrl());
                    Intent webActivity = new Intent(this, WebActivity.class);
                    webActivity.putExtra("url", article.getUrl());
                    startActivity(webActivity);

                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater. inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.item2:
                Toast.makeText(this,"Help is selected",Toast.LENGTH_SHORT).show();
                return  true;
            case R.id.item3:
                Toast.makeText(this,"Contact us soon",Toast.LENGTH_SHORT).show();
                return  true;
            case R.id.item6:
                Toast.makeText(this,"You can Download",Toast.LENGTH_SHORT).show();
                return  true;
            case R.id.sub1:
                Toast.makeText(this,"You clicked",Toast.LENGTH_SHORT).show();
                return  true;
            case R.id.sub2:
                Toast.makeText(this,"You pressed",Toast.LENGTH_SHORT).show();
                return  true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}