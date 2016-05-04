package com.panfishingllc.ifish;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
//import java.util.Date;
import java.text.SimpleDateFormat;


public class Detail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int id = Integer.parseInt(getIntent().getStringExtra("ID"));
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getData(id);

        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(rightNow.getTime());

        if (!cursor.isAfterLast()) {
            // set image
            int thumbnail = cursor.getInt(cursor.getColumnIndex("thumbnail"));
            ImageView image = (ImageView) findViewById(R.id.imageView);
            image.setImageResource(thumbnail);

            // set name
            TextView nameView = (TextView) findViewById(R.id.nameView);
            nameView.setText(cursor.getString(cursor.getColumnIndex("name")));

            // season icon
            String openDate = cursor.getString(cursor.getColumnIndex("open_date"));
            String closeDate = cursor.getString(cursor.getColumnIndex("close_date"));
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

            // set size limit
            TextView size = (TextView) findViewById(R.id.min_size);
            double sizeLimit = cursor.getDouble(cursor.getColumnIndex("min_size"));
            size.setText(String.valueOf(sizeLimit) + "\"");

            // set bag limit
            TextView bag = (TextView) findViewById(R.id.bag_limit);
            int bagLimit = cursor.getInt(cursor.getColumnIndex("bag_limit"));
            bag.setText(String.valueOf(bagLimit));
        }

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
