package com.panfishingllc.ifish;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//import java.util.Date;


public class Detail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int id = Integer.parseInt(getIntent().getStringExtra("ID"));
        DatabaseHelper db = new DatabaseHelper(this);

        // get species info
        Cursor cursor = db.getSpeiciesData(id);
        if (cursor.isAfterLast()) {
            return;
        }

        // set image
//        int thumbnail = cursor.getInt(cursor.getColumnIndex("Thumbnail"));
//        ImageView image = (ImageView) findViewById(R.id.imageView);
//        image.setImageResource(thumbnail);


        // set name
        TextView nameView = (TextView) findViewById(R.id.nameView);
        String speciesName = cursor.getString(cursor.getColumnIndex("SpeciesName"));
        nameView.setText(speciesName);

        Context context = getApplicationContext();
        int thumbnail = context.getResources().getIdentifier(speciesName,
                "drawable", context.getPackageName());
        Log.e("img", String.valueOf(thumbnail));
        Log.e("img", String.valueOf(R.drawable.bass_striped));
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(thumbnail);

        // get state
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        String state = setting.getString("state", "NEW YORK");
        Log.e("AREA", state);

        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(rightNow.getTime());

        // get season
        cursor = db.getSeason(speciesName, state);
        ArrayList<String> ruleList = new ArrayList<String>();

        boolean isSeasonOpen = false;
        while(!cursor.isAfterLast()) {
            int seasonId = cursor.getInt(cursor.getColumnIndex("SeasonId"));
            Log.e("detail", String.valueOf(seasonId));
            String openDate = cursor.getString(cursor.getColumnIndex("OpenDate"));
            String closeDate = cursor.getString(cursor.getColumnIndex("CloseDate"));
            if (closeDate.compareTo(today) >= 0) {
                if (openDate.compareTo(today) <= 0 && today.compareTo(closeDate) <= 0) {
                    isSeasonOpen = true;
                }

                if (openDate.compareTo(closeDate) < 0) {
                    ruleList.add(openDate + " ~ " + closeDate);
                }

                Cursor ruleCursor = db.getRule(seasonId);
                while (!ruleCursor.isAfterLast()) {
                    ruleList.add(ruleCursor.getString(ruleCursor.getColumnIndex("Rule")));
                    Log.e("RULE", cursor.getString(ruleCursor.getColumnIndex("Rule")));
                    ruleCursor.moveToNext();
                }
            }

            cursor.moveToNext();
        }

        RuleAdaptor adaptor = new RuleAdaptor(ruleList);
        ListView ruleView = (ListView) findViewById(R.id.rules);
        ruleView.setAdapter(adaptor);

        TextView season = (TextView) findViewById(R.id.season);
        if (isSeasonOpen) {
            season.setText("Open");
        } else {
            season.setText("Closed");
        }

//
//        // convert season open date and close date to MMM d format
//        Calendar openCal = Calendar.getInstance();
//        Calendar closeCal = Calendar.getInstance();
//        try {
//            SimpleDateFormat seasonFormatter = new SimpleDateFormat("MMM d");
//
//            openCal.setTime(formatter.parse(openDate));
//            closeCal.setTime(formatter.parse(closeDate));
//
//            formatter.applyPattern("MMM d");
//            openDate = formatter.format(openCal.getTime());
//            closeDate = formatter.format(closeCal.getTime());
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//        // set season open and close date
//        TextView seasonView = (TextView) findViewById(R.id.season);
//        seasonView.setText(openDate + " -- " + closeDate);
//
//        cursor = db.getRule(seasonId);
//
//        while (!cursor.isAfterLast()) {
//            ruleList.add(cursor.getString(cursor.getColumnIndex("Rule")));
//            Log.e("RU", cursor.getString(cursor.getColumnIndex("Rule")));
//            cursor.moveToNext();
//        }



//        if (!cursor.isAfterLast()) {
//            TextView bag = (TextView) findViewById(R.id.bag_limit);
//            bag.setText(cursor.getString(cursor.getColumnIndex("Rule")));
//        }


        Cursor recordCursor = db.getRecord(id);
        if (!recordCursor.isAfterLast()) {
            // set weight
            int weight = recordCursor.getInt(recordCursor.getColumnIndex("weight"));
            int lbs = weight / 16;
            int ozs = weight % 16;
            TextView weightView = (TextView) findViewById(R.id.weight);
            weightView.setText("World Record: " + String.valueOf(lbs) + "lb " + String.valueOf(ozs) + "oz");

            // set date
            String recordDate = recordCursor.getString(recordCursor.getColumnIndex("record_date"));
            TextView recordView = (TextView) findViewById(R.id.record_date);
            recordView.setText(recordDate);

            // set location
            String location = recordCursor.getString(recordCursor.getColumnIndex("location"));
            TextView locationView = (TextView) findViewById(R.id.location);
            locationView.setText(location);
        }

        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
