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
//        speciesList.add(new Species(0, "Bass Striped", R.drawable.bass_striped));
//        speciesList.add(new Species(1, "Bass Largemouth", R.drawable.bass_large_mouth));
//        speciesList.add(new Species(2, "Bass Smallmouth", R.drawable.bass_small_mouth));
//        speciesList.add(new Species(3, "Bass White", R.drawable.bass_white));
//        speciesList.add(new Species(4, "Bluefishh", R.drawable.bluefish));
//        speciesList.add(new Species(5, "Carp Common", R.drawable.carp_common));
//        speciesList.add(new Species(6, "Cobia", R.drawable.cobia));
//        speciesList.add(new Species(7, "Cod Atlantic", R.drawable.cod_atlantic));
//        speciesList.add(new Species(8, "Cod Pacific", R.drawable.cod_pacific));
//        speciesList.add(new Species(9, "Catfish Channel", R.drawable.catfish_channel));
//        speciesList.add(new Species(10, "Flounder Summer", R.drawable.flounder_summer));
//        speciesList.add(new Species(11, "Flounder Winter", R.drawable.flounder_winter));
//        speciesList.add(new Species(12, "Gar Alligator", R.drawable.gar_alligator));
//        speciesList.add(new Species(13, "Gar Longnose", R.drawable.gar_longnose));
//        speciesList.add(new Species(14, "Gar Shortnose", R.drawable.gar_shortnose));
//        speciesList.add(new Species(15, "Grouper Black", R.drawable.grouper_black));
//        speciesList.add(new Species(16, "Grouper Red", R.drawable.cobia));
//        speciesList.add(new Species(17, "Salmon Alantic", R.drawable.salmon_atlantic));
//        speciesList.add(new Species(18, "Salmon Chinook", R.drawable.salmon_chinook));
//        speciesList.add(new Species(19, "Salmon Coho", R.drawable.salmon_coho));
//        speciesList.add(new Species(20, "Salmon Pink", R.drawable.salmon_pink));
//        speciesList.add(new Species(21, "Salmon Sockeye", R.drawable.salmon_sockeye));
//        speciesList.add(new Species(22, "Sauger", R.drawable.sauger));
//        speciesList.add(new Species(23, "Seabass Black", R.drawable.bass_black_sea));
//        speciesList.add(new Species(24, "Seabass Blackfin", R.drawable.seabass_blackfin));
//        speciesList.add(new Species(25, "Seatrout Spotted", R.drawable.seatrout_spotted));
//        speciesList.add(new Species(26, "Shad American", R.drawable.shad_american));
//        speciesList.add(new Species(27, "Shark Blue", R.drawable.shark_blue));
//        speciesList.add(new Species(28, "Shark Mako", R.drawable.shark_mako));
//        speciesList.add(new Species(29, "Shark Tiger", R.drawable.shark_tiger));
//        speciesList.add(new Species(30, "Shark Wite", R.drawable.shark_white));
//        speciesList.add(new Species(31, "Snapper Mutton", R.drawable.snapper_mutton));
//        speciesList.add(new Species(32, "Snapper Red", R.drawable.snapper_red));
//        speciesList.add(new Species(33, "Sunfish Redear", R.drawable.sunfish_readear));
//        speciesList.add(new Species(34, "Sunfish Redbreast", R.drawable.sunfish_redbreast));
//        speciesList.add(new Species(35, "Swordfish", R.drawable.swordfish));
//        speciesList.add(new Species(36, "Taimen", R.drawable.taimen));
//        speciesList.add(new Species(37, "Tarpon", R.drawable.tarpon));
//        speciesList.add(new Species(38, "Tautog", R.drawable.tautog));
//        speciesList.add(new Species(39, "Weakfish", R.drawable.weakfish));
//        speciesList.add(new Species(40, "Trout Rainboew", R.drawable.trout_rainbow));
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
}
