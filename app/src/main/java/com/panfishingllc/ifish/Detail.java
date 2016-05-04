package com.panfishingllc.ifish;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

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
        int thumbnail = cursor.getInt(cursor.getColumnIndex("Thumbnail"));
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(thumbnail);

        // set name
        TextView nameView = (TextView) findViewById(R.id.nameView);
        String speciesName = cursor.getString(cursor.getColumnIndex("SpeciesName"));
        nameView.setText(speciesName);

        // get state
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        String state = setting.getString("state", "NY");
        Log.e("AREA", state);

        // get season
        cursor = db.getSeason(speciesName, state);
        if (cursor.isAfterLast()) {
            return;
        }

        int seasonId = cursor.getInt(cursor.getColumnIndex("SeasonId"));
        String openDate = cursor.getString(cursor.getColumnIndex("OpenDate"));
        String closeDate = cursor.getString(cursor.getColumnIndex("CloseDate"));

        Log.e("CC", String.valueOf(seasonId));
        Log.e("CC", openDate +  " " + closeDate);

        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(rightNow.getTime());

        ImageView reg_icon = (ImageView) findViewById(R.id.icon_reg);
        if (openDate.compareTo(today) <= 0 && today.compareTo(closeDate) <= 0) {
            reg_icon.setImageResource(R.drawable.open);
        } else {
            reg_icon.setImageResource(R.drawable.close);
        }

        // convert season open date and close date to MMM d format
        Calendar openCal = Calendar.getInstance();
        Calendar closeCal = Calendar.getInstance();
        try {
            SimpleDateFormat seasonFormatter = new SimpleDateFormat("MMM d");

            openCal.setTime(formatter.parse(openDate));
            closeCal.setTime(formatter.parse(closeDate));

            formatter.applyPattern("MMM d");
            openDate = formatter.format(openCal.getTime());
            closeDate = formatter.format(closeCal.getTime());
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        // set season open and close date
        TextView seasonView = (TextView) findViewById(R.id.season);
        seasonView.setText(openDate + " -- " + closeDate);

        cursor = db.getRule(seasonId);
        ArrayList<String> ruleList = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            ruleList.add(cursor.getString(cursor.getColumnIndex("Rule")));
            Log.e("RU", cursor.getString(cursor.getColumnIndex("Rule")));
            cursor.moveToNext();
        }

        RuleAdaptor adaptor = new RuleAdaptor(ruleList);
        ListView ruleView = (ListView) findViewById(R.id.rules);
        ruleView.setAdapter(adaptor);

//        if (!cursor.isAfterLast()) {
//            TextView bag = (TextView) findViewById(R.id.bag_limit);
//            bag.setText(cursor.getString(cursor.getColumnIndex("Rule")));
//        }

        if (true)
            return;
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
