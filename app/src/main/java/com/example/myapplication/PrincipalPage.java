package com.example.myapplication;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PrincipalPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);

        LinearLayout col1Container = findViewById(R.id.col1_container);
        col1Container.addView(createTextView("Élément 1"));
        col1Container.addView(createTextView("Élément 2"));
        col1Container.addView(createTextView("Élément 3"));
        col1Container.addView(createTextView("Autre élément"));
        col1Container.addView(createTextView("Encore un autre élément"));

        // Définir les listeners de drag and drop pour chaque conteneur de colonne
        LinearLayout col1 = findViewById(R.id.col1);
        col1.setOnDragListener(new ColumnDragListener(col1Container));

        LinearLayout col2Container = findViewById(R.id.col2);
        col2Container.setOnDragListener(new ColumnDragListener(col2Container));

        LinearLayout col3Container = findViewById(R.id.col3);
        col3Container.setOnDragListener(new ColumnDragListener(col3Container));


    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(18);
        textView.setPadding(16, 16, 16, 16);
        textView.setOnLongClickListener(new ElementLongClickListener());
        return textView;
    }

    private class ElementLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, shadowBuilder, v, 0);
            return true;
        }
    }

    private class ColumnDragListener implements View.OnDragListener {
        private final LinearLayout container;

        public ColumnDragListener(LinearLayout container) {
            this.container = container;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Vérifier si l'élément qui est dragué est un TextView
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.purple_700));

                        v.invalidate();
                        return true;
                    } else {
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENTERED:
                    //la couleur l'orsque je prend les éléments
                    v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                case DragEvent.ACTION_DRAG_ENDED:

                    v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));

                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Récupérer l'élément dragué et le placer dans la nouvelle colonne
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    // la couleurs lorsque je dépose les elements
                    v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                    v.invalidate();
                    return true;
                default:
                    return false;
            }
        }
    }
}
