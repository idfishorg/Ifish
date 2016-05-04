package com.panfishingllc.ifish;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private DatabaseHelper db = new DatabaseHelper(this);
    private String state;
    public static String STATE = "state";
    public SpeciesAdaptor adapter;

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.e("AA", "onResume()");
        if (true)
            return;

        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("BB", setting.getString("state", "CA"));

        String updatedState = setting.getString("state", "NY");
        if (!updatedState.equals(state)) {
            Cursor cursor = db.getAllSpecies(updatedState);

            ArrayList<Species> updatedList = new ArrayList<Species>();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("SpeciesId"));
                String name = cursor.getString(cursor.getColumnIndex("SpeciesName"));
                int thumbnail = cursor.getInt(cursor.getColumnIndex("Thumbnail"));
                updatedList.add(new Species(id, name, thumbnail));
                cursor.moveToNext();
            }

            adapter.setSpeciesList(updatedList);
            adapter.notifyDataSetChanged();
            state = updatedState;
        }

        SharedPreferences.OnSharedPreferenceChangeListener prefListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        Log.e("AA", prefs.getString("state", "CA"));
                        if (key.equals("state")) {
                            Log.e("AA", prefs.getString("state", "CA"));
                            String updatedState = prefs.getString("state", "NY");
                            Cursor cursor = db.getAllSpecies(updatedState);

                            ArrayList<Species> updatedList = new ArrayList<Species>();
                            while (!cursor.isAfterLast()) {
                                int id = cursor.getInt(cursor.getColumnIndex("SpeciesId"));
                                String name = cursor.getString(cursor.getColumnIndex("SpeciesName"));
                                int thumbnail = cursor.getInt(cursor.getColumnIndex("thumbnail"));
                                updatedList.add(new Species(id, name, thumbnail));
                                cursor.moveToNext();
                            }

                            adapter.setSpeciesList(updatedList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                };
        setting.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView speciesListView = (ListView) findViewById(R.id.speciesView);

        if (false)
            return;

        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        state = setting.getString("state", "NY");
        Log.e("AAA", state);
        Cursor cursor = db.getAllSpecies(state);

        ArrayList<Species> speciesList = new ArrayList<Species>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("SpeciesId"));
            String name = cursor.getString(cursor.getColumnIndex("SpeciesName"));
            int thumbnail = cursor.getInt(cursor.getColumnIndex("Thumbnail"));
            speciesList.add(new Species(id, name, thumbnail));
            cursor.moveToNext();
        }

        adapter = new SpeciesAdaptor(speciesList);
        speciesListView.setAdapter(adapter);

        speciesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), Detail.class);
                i.putExtra("ID", String.valueOf(position + 1));
                startActivity(i);
            }
        });


    }

    //called when the preferences are changed in any way
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
//        if ("key" == "state") {
//            Log.e("AA", sharedPreferences.getString("state", "CA"));
//        }
////        txtMessage1.setText(prefs.getString("custom_message_1", ""));
////        txtMessage2.setText(prefs.getString("custom_message_2", ""));
//    }


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
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

//
//      return super.onOptionsItemSelected(item);
    }



}
