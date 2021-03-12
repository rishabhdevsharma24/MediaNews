package com.news.medianews.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import static android.os.Build.VERSION_CODES.N;

public class NewsByDate extends AppCompatActivity {

    private static final String API_KEY = "cb937d9b6ff74a79a4bb12c47bf2e112";
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_by_date);
        context = getApplicationContext();
        String Date = getIntent().getStringExtra("Date");
        Log.i("Dateless:", Date);

        final RecyclerView mainRecycler = findViewById(R.id.activity_news_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainRecycler.setLayoutManager(linearLayoutManager);

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.getLatestNews("in", Date, Date, API_KEY);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse
                    (Call<ResponseModel> call, Response<ResponseModel> response) {

                if (response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if (articleList.size() > 0) {
                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(articleList);
                        mainArticleAdapter.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
                        mainRecycler.setAdapter(mainArticleAdapter);

                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("out", t.toString());
            }
        });
    }

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
}