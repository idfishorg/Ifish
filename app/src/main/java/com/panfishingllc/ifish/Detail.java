package com.panfishingllc.ifish;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class Detail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int id = Integer.parseInt(getIntent().getStringExtra("ID"));
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getData(id);

        if (!cursor.isAfterLast()) {
            int thumbnail = cursor.getInt(cursor.getColumnIndex("species.thumbnail"));
            ImageView image = (ImageView) findViewById(R.id.imageView);
            image.setImageResource(thumbnail);

            TextView nameView = (TextView) findViewById(R.id.nameView);
            nameView.setText(cursor.getString(cursor.getColumnIndex("species.name")));

            TextView seasonView = (TextView) findViewById(R.id.season);
            String openDate = cursor.getString(cursor.getColumnIndex("regulation.open_date"));
            String closeDate = cursor.getString(cursor.getColumnIndex("regulation.close_date"));
            seasonView.setText(openDate + " " + closeDate);

            TextView size = (TextView) findViewById(R.id.min_size);
            size.setText("Mininum Size: " + cursor.getString(cursor.getColumnIndex("regulation.min_size")));

            TextView bag = (TextView) findViewById(R.id.bag_limit);
            bag.setText("Bag Limit: " + cursor.getString(cursor.getColumnIndex("regulation.bag_limit")));
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
