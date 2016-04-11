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

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView speciesListView = (ListView) findViewById(R.id.speciesView);

        Cursor cursor = db.getAllSpecies();

        ArrayList<Species> speciesList = new ArrayList<Species>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int thumbnail = cursor.getInt(cursor.getColumnIndex("thumbnail"));
            speciesList.add(new Species(id, name, thumbnail));
            cursor.moveToNext();
        }

        SpeciesAdaptor adapter = new SpeciesAdaptor(speciesList);
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
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }
}
