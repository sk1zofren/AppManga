package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        TextView titleTextView = convertView.findViewById(R.id.title_text_view);
        titleTextView.setText(manga.getTitle());

        return convertView;
    }
}

