package pt.ipleiria.estg.dei.horadapapa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pt.ipleiria.estg.dei.horadapapa.models.Plate;

public class PlateSpinnerAdapter extends ArrayAdapter<Plate> {

    public PlateSpinnerAdapter(@NonNull Context context, @NonNull List<Plate> plates) {
        super(context, 0, plates);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        // Customize the view for the selected item (display only the Plate name)
        TextView textView = convertView.findViewById(android.R.id.text1);
        Plate plate = getItem(position);
        if (plate != null) {
            textView.setText(plate.getTitle());
        }

        return convertView;
    }
}
