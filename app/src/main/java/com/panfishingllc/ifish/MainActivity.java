package com.panfishingllc.ifish;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.SharedPreferences.Editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {
    private DatabaseHelper db = new DatabaseHelper(this);
    private String state;
    public static String STATE = "state";
    public SpeciesAdaptor adapter;
    public SharedPreferences.OnSharedPreferenceChangeListener prefChangedListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Log.e("AA", prefs.getString("state", "CA"));
        if (key.equals("state")) {
            Context context = getApplicationContext();
            Log.e("AA", prefs.getString("state", "CA"));
            String updatedState = prefs.getString("state", "NY");
            Cursor cursor = db.getAllSpecies(updatedState);

            ArrayList<Species> updatedList = new ArrayList<Species>();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("SpeciesId"));
                String name = cursor.getString(cursor.getColumnIndex("SpeciesName"));
                String thumbnailName = cursor.getString(cursor.getColumnIndex("Thumbnail"));
                int thumbnail = context.getResources().getIdentifier(thumbnailName,
                        "drawable", getPackageName());
                updatedList.add(new Species(id, name, thumbnail));
                cursor.moveToNext();
            }

            adapter.setSpeciesList(updatedList);
            adapter.notifyDataSetChanged();
        }
    }
            };

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.e("AA", "onResume()");


        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("BB", setting.getString("state", "CA"));
        setting.registerOnSharedPreferenceChangeListener(prefChangedListener);

//        String updatedState = setting.getString("state", "NY");
//        if (!updatedState.equals(state)) {
//            Cursor cursor = db.getAllSpecies(updatedState);
//
//            ArrayList<Species> updatedList = new ArrayList<Species>();
//            while (!cursor.isAfterLast()) {
//                int id = cursor.getInt(cursor.getColumnIndex("SpeciesId"));
//                String name = cursor.getString(cursor.getColumnIndex("SpeciesName"));
//                int thumbnail = cursor.getInt(cursor.getColumnIndex("Thumbnail"));
//                updatedList.add(new Species(id, name, thumbnail));
//                cursor.moveToNext();
//            }
//
//            adapter.setSpeciesList(updatedList);
//            adapter.notifyDataSetChanged();
//            state = updatedState;
//        }
//        setting.registerOnSharedPreferenceChangeListener(prefChangedListener);

//        SharedPreferences.OnSharedPreferenceChangeListener prefListener =
//                new SharedPreferences.OnSharedPreferenceChangeListener() {
//
//                };
//        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView speciesListView = (ListView) findViewById(R.id.speciesView);

        if (false)
            return;

        // get state
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        state = preferences.getString("state", "NEW YORK");

        // get all species of state
        Cursor cursor = db.getAllSpecies(state);
        Context context = getApplicationContext();
        ArrayList<Species> speciesList = new ArrayList<Species>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("SpeciesId"));
            String name = cursor.getString(cursor.getColumnIndex("SpeciesName"));
            String thumbnailName = cursor.getString(cursor.getColumnIndex("Thumbnail"));
            int thumbnail = context.getResources().getIdentifier(thumbnailName,
                    "drawable", getPackageName());
            speciesList.add(new Species(id, name, thumbnail));
            cursor.moveToNext();
        }

        adapter = new SpeciesAdaptor(speciesList);
        speciesListView.setAdapter(adapter);

        speciesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Species species = (Species) parent.getItemAtPosition(position);
                Intent i = new Intent(view.getContext(), Detail.class);
                i.putExtra("ID", String.valueOf(species.getId()));
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.populate:
                db.addSomeSpecies();
                // populate database
                return true;
//            case R.id.camera:
//                dispatchTakePictureIntent();
//                return true;
            case R.id.clear:
                db.clear();
                break;
            case R.id.set_location:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.use_my_location:
                getCurrentLocation();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void getCurrentLocation() {
        Log.e("LOC", "in getCurrentLocation");
        LocationManager locationManager =
                (LocationManager) this.getSystemService(getApplicationContext().LOCATION_SERVICE);


        // use network provider
        String locationProvider = LocationManager.GPS_PROVIDER;

        Location last = locationManager.getLastKnownLocation(locationProvider);

        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(last.getLatitude(),
                    last.getLongitude(), 1);

            Log.e("ADD", addresses.toString());
            if (addresses.size() > 0) {
                String state = addresses.get(0).getAdminArea();
                SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
                Editor editor = setting.edit();
                editor.putString("state", state.toUpperCase());
                editor.commit();
                Log.e("ADD", state);
                Log.e("ADD", setting.getString("state", "NY"));

//                String cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("LOC", last.toString());
//        LocationListener locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                // Called when a new location is found by the network location provider.
//                Log.e("LOC", location.toString());
////                locationManager.remo
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//            public void onProviderEnabled(String provider) {}
//
//            public void onProviderDisabled(String provider) {}
//        };
//
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        return;
    }
}
