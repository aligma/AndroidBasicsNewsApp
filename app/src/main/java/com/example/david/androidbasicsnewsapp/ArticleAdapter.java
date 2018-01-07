package com.example.david.androidbasicsnewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        String title = currentArticle.getTitle();
        String sectionName = currentArticle.getSectionName();

        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.title);
        primaryLocationView.setText(title);

        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.section);
        locationOffsetView.setText(sectionName);

        Date dateObject = currentArticle.getWebPublicationDate();

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        String authorName = currentArticle.getAuthorName();
        authorView.setText(authorName);

        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getContext().getString(R.string.date_format));
        return dateFormat.format(dateObject);
    }
}
