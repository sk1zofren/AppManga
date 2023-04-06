package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        // Récupérer l'objet Manga à la position spécifiée
        Manga manga = getItem(position);

        // Mettre à jour la vue avec les données de l'objet Manga


        // Récupérer l'URL de l'image pour le manga actuel
        String imageUrl = getItem(position).getImageUrl();

        // Charger l'image à partir de l'URL et l'afficher dans l'ImageView
        ImageView imageView = convertView.findViewById(R.id.image_view);
        Picasso.get().load(imageUrl).into(imageView);


        return convertView;
    }
}

