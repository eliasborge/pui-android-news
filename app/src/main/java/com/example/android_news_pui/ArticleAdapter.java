package com.example.android_news_pui;


import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.temporal.TemporalAdjuster;
import java.util.List;

import es.upm.hcid.pui.assignment.exceptions.ServerCommunicationError;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.textTitle.setText(article.getTitleText());
        holder.textAbstract.setText(article.getAbstractText());
        holder.textCategory.setText(article.getCategory());

        // Load thumbnail using Glide or any other image loading library
        try {
            if (article.getImage() != null) {
                Instant Glide = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        Glide.with((TemporalAdjuster) holder.itemView)
                                .get(article.getImage().getImage()) // Use the correct method to get the image URL from the Image class
                                .into(holder.imageThumbnail);
                    } catch (ServerCommunicationError e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (ServerCommunicationError e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textAbstract;
        ImageView imageThumbnail;
        TextView textCategory;

        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAbstract = itemView.findViewById(R.id.textAbstract);
            imageThumbnail = itemView.findViewById(R.id.imageThumbnail);
            textCategory = itemView.findViewById(R.id.textCategory);
        }
    }
}
