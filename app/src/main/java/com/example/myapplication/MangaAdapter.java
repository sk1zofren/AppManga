package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MangaAdapter extends ArrayAdapter<Manga> {
    private LayoutInflater inflater;

    public MangaAdapter(Context context, int resource, List<Manga> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_manga, parent, false);
        }

        // Get the Manga object at the specified position
        Manga manga = getItem(position);

        // Update the view with the data from the Manga object

        // Get the URL of the image for the current manga
        String imageUrl = manga.getImageUrl();

        // Load the image from the URL and display it in the ImageView
        ImageView imageView = convertView.findViewById(R.id.image_view);
        Picasso.get().load(imageUrl).into(imageView);

        return convertView;
    }
}
