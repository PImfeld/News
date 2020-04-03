package com.example.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information news at the given position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        // Finds the news at the given position in the list
        News currentNews = getItem(position);

        // Find the TextView with view ID title
        TextView titleView = convertView.findViewById(R.id.title);
        // Displays the title of the current news in the TextView
        titleView.setText(currentNews.getTitle());

        // Find the TextView with view ID section
        TextView sectionView = convertView.findViewById(R.id.section);
        // Displays the articles section in that TextView
        sectionView.setText(currentNews.getSection());

        // Find the TextView with view ID author
        TextView authorView = convertView.findViewById(R.id.author);
        // Checks if there is an author for the news object
        if (currentNews.hasAuthor()) {
            // Displays the date of the current article in that TextView
            authorView.setText(currentNews.getAuthor());
            // Makes sure the view is visible
            authorView.setVisibility(View.VISIBLE);
        } else {
            //hides the image view
            authorView.setVisibility(View.GONE);
        }

        // Returns the list view that shows the correct data
        return convertView;
        }
    }
