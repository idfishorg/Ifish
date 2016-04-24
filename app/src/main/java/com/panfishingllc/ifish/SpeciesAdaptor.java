package com.panfishingllc.ifish;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jing on 4/10/2016.
 */
public class SpeciesAdaptor extends BaseAdapter {
    private ArrayList<Species> speciesList = new ArrayList<Species>();

    public SpeciesAdaptor(ArrayList<Species> speciesList) {
        this.speciesList = speciesList;
    }
    public SpeciesAdaptor() {
    }

    @Override
    public int getCount() {
        return speciesList.size();
    }

    @Override
    public Object getItem(int position) {
        return speciesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.species_item, parent, false);
        }

        Species species = speciesList.get(position);

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumnailView);
        thumbnail.setImageResource(species.getThumbnail());

        TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
        nameView.setText(species.getName());

        return convertView;
    }

    public void setSpeciesList(ArrayList<Species> list) {
        this.speciesList = list;
    }
}
