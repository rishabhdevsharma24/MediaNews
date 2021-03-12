package com.news.medianews.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.news.medianews.Activity.MainActivity;
import com.news.medianews.GlideApp;
import com.news.medianews.Model.Article;
import com.news.medianews.Model.SourceModel;
import com.news.medianews.R;
import com.news.medianews.Utils.OnRecyclerViewItemClickListener;

import java.util.List;

public class MainArticleAdapter extends RecyclerView.Adapter<MainArticleAdapter.ViewHolder> {
    private List<Article> articleArrayList;
    private Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public MainArticleAdapter(List<Article> articleArrayList) {
        this.articleArrayList = articleArrayList;
    }

    @Override
    public MainArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main_article_adapter, viewGroup, false);
        return new MainArticleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainArticleAdapter.ViewHolder holder, int position) {

        context = holder.itemView.getContext();
        final Article articleModel = articleArrayList.get(position);

        SourceModel sourceModel=new SourceModel();
        GlideApp.with(context).load(articleModel.getUrlToImage()).into(holder.imageView);
        if (!TextUtils.isEmpty(articleModel.getTitle())) {
            holder.titleText.setText(articleModel.getTitle());
        }
        if (!TextUtils.isEmpty(articleModel.getDescription())) {
            holder.descriptionText.setText(articleModel.getDescription());
        }
        if (!TextUtils.isEmpty(articleModel.getPublishedAt())) {
            String s1=articleModel.getPublishedAt();
            String substr = s1.substring(0,10);
            holder.time.setText(substr);
        }
        if (!TextUtils.isEmpty(articleModel.getAuthor())) {
            holder.source.setText(articleModel.getAuthor());
        }
        holder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareCompat.IntentBuilder.from((Activity) context)
                        .setType("text/plain")
                        .setChooserTitle("News Reader")
                        .setText(articleModel.getTitle() + articleModel.getUrl())
                        .startChooser();
            }
            });
        holder.artilceAdapterParentLinear.setTag(articleModel);
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText,time,source;
        private TextView descriptionText;
        ImageView imageView;
        Button btn1;
        private LinearLayout artilceAdapterParentLinear;

        ViewHolder(View view) {
            super(view);
           titleText = view.findViewById(R.id.article_adapter_tv_title);
            source=view.findViewById(R.id.source);
            descriptionText = view.findViewById(R.id.article_adapter_tv_description);
            time=itemView.findViewById(R.id.publishedAt);
            artilceAdapterParentLinear = view.findViewById(R.id.article_adapter_ll_parent);
            imageView=itemView.findViewById(R.id.img);
            btn1=view.findViewById(R.id.EXTRA_TEXT);
            artilceAdapterParentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), view);
                    }
                }
            });
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}