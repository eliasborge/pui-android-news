package com.example.android_news_pui;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import com.bumptech.glide.Glide;
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
                String imageUrl = article.getImage().getImage();  // Assuming getImage() returns the URL or path
                // Remove this line: Instant Glide = null;
                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .into(holder.imageThumbnail);
            }
        } catch (ServerCommunicationError e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setArticles(@NotNull List<? extends Article> articles) {
        this.articles = (List<Article>) articles;
        notifyDataSetChanged();
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
