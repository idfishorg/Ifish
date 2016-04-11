package com.panfishingllc.ifish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jing on 4/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "fish.db";
    public static final String SPECIES_TABLE_NAME = "species";
    public static final String REGULATION_TABLE_NAME = "regulation";
    public static final String TYPE_TABLE_NAME = "type";
    public static final String SPECIES_COLUMN_ID = "id";
    public static final String SPECIES_COLUMN_NAME = "name";
    public static final String SPECIES_COLUMN_SIZE = "size";
    public static final String SPECIES_COLUMN_RECORD = "record";
    public static final String SPECIES_COLUMN_IMAGE = "image";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE species " +
                        "(_id integer primary key autoincrement, " +
                        "name text, " +
                        "thumbnail integer)"
        );

        db.execSQL(
                "CREATE TABLE  type" +
                        "(_id integer primary key autoincrement,  " +
                        "type text)"
        );

        db.execSQL(
                "CREATE TABLE regulation " +
                        "(species_id integer, " +
                        "type_id integer, " +
                        "open_date text, " +
                        "close_date text, " +
                        "min_size real, " +
                        "bag_limit integer, " +
                        "PRIMARY KEY (species_id, type_id), " +
                        "FOREIGN KEY(species_id) REFERENCES species(_id), " +
                        "FOREIGN KEY(type_id) REFERENCES type(_id) " +
                        ")"

        );

        return;
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("Delete from species WHERE true");
//        db.delete("species", null, null);
        db.execSQL("DROP TABLE IF EXISTS regulation");
        db.execSQL("DROP TABLE IF EXISTS type");
        db.execSQL("DROP TABLE IF EXISTS species");
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS regulation");
        db.execSQL("DROP TABLE IF EXISTS type");
        db.execSQL("DROP TABLE IF EXISTS species");
        onCreate(db);
        return;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
//                "SELECT f.id, f.name, f.thumbnail, " +
//                        " s.id, s.type " +
//                        " r.open_date, r.close_date, r.min_size, r.bag_limit " +
//                        " FROM regulation r INNER JOIN species f ON r.species_id = f.id  " +
//                        " INNTER JOIN state s ON r.state_id = s.id WHERE f.id =" + id + " AND s.id = 1", null
                "SELECT species._id, species.name, species.thumbnail, " +
                        " regulation.open_date, regulation.close_date, regulation.min_size, regulation.bag_limit " +
                        " FROM regulation INNER JOIN species ON regulation.species_id = species._id  " +
                        " WHERE species._id = " + String.valueOf(id), null
        );

        if (cursor != null) {
            cursor.moveToNext();
        }
        return cursor;
    }

    public void insertSpecies(String name, Integer thumbnail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("thumbnail", thumbnail);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(SPECIES_TABLE_NAME, null, contentValues);

        return;
    }

    public void insertState(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", name);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TYPE_TABLE_NAME, null, contentValues);

        return;
    }

    public void insertRegulation(int species_id, int type_id, String open_date, String close_date,
                                 double min_size, int bag_limit ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("species_id", species_id);
        contentValues.put("type_id", type_id);
        contentValues.put("open_date", open_date);
        contentValues.put("close_date", close_date);
        contentValues.put("min_size", min_size);
        contentValues.put("bag_limit", bag_limit);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(REGULATION_TABLE_NAME, null, contentValues);

        return;
    }

    public void updateSpecies(Integer id, String name, Integer size, Integer record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("size", size);
        contentValues.put("record", record);

        SQLiteDatabase db = getWritableDatabase();
        db.update(SPECIES_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});

        return;
    }


    public Cursor getAllSpecies() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("species", new String[]{"_id", "name", "thumbnail"},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public void addSomeSpecies() {
        insertSpecies("Bass Striped", R.drawable.bass_striped);
        insertSpecies("Bluefish", R.drawable.bluefish);
        insertSpecies("Bass Largemouth", R.drawable.bass_large_mouth);
        insertSpecies("Bass Smallmouth", R.drawable.bass_small_mouth);
        insertSpecies("Flounder Summer", R.drawable.flounder_summer);
        insertSpecies("Flounder Winter", R.drawable.flounder_winter);
        insertSpecies("Tautog", R.drawable.tautog);
        insertSpecies("Weakfish", R.drawable.weakfish);
        insertState("NY");
        insertState("NJ");
        insertRegulation(1, 1, "2016-04-15", "2016-12-31", 28, 1);
        insertRegulation(2, 1, "2016-01-01", "2016-12-31", 12, 15);
        insertRegulation(3, 1, "2016-06-17", "2016-11-30", 12, 5);
        insertRegulation(4, 1, "2016-06-17", "2016-11-30", 99999, 0);
        insertRegulation(5, 1, "2016-05-17", "2016-09-21", 18, 5);
        insertRegulation(6, 1, "2016-04-01", "2016-05-30", 12, 2);
        insertRegulation(7, 1, "2016-10-05", "2016-12-14", 16, 4);
        insertRegulation(8, 1, "2016-01-01", "2016-12-31", 16, 1);



//        insertSpecies("Bass Striped", 20, 81, R.drawable.bass_striped);
//        insertSpecies("Bluefish", 30, 45, R.drawable.bluefish);
//        insertSpecies("Bass White", 10, 40, R.drawable.bass_white);
//        insertSpecies("Bass Largemouth", 11, 12, R.drawable.bass_large_mouth);
//        insertSpecies("Bass Smallmouth", 11, 23, R.drawable.bass_small_mouth);
//        insertSpecies("Seabass Black", 12, 12, R.drawable.bass_black_sea);
//        insertSpecies("Carp Common", 1, 1, R.drawable.carp_common);
//        insertSpecies("Catfish Channel", 1, 1, R.drawable.catfish_channel);
//        insertSpecies("Cobia", 1, 1, R.drawable.cobia);
//        insertSpecies("Cod Atlantic", 1, 1, R.drawable.cod_atlantic);
//        insertSpecies("Cod Pacific", 2, 2, R.drawable.cod_pacific);
//        insertSpecies("Flounder Summer", 1, 1, R.drawable.flounder_summer);
//        insertSpecies("Flounder Winter", 1, 1, R.drawable.flounder_winter);
//        insertSpecies("Gar Alligator", 1, 1, R.drawable.gar_alligator);
//        insertSpecies("Gar Longnose",1,1, R.drawable.gar_longnose);
//        insertSpecies("Gar Shortnose",1,1, R.drawable.gar_shortnose);
//        insertSpecies("Grouper Black", 1,1,R.drawable.grouper_black);
//        insertSpecies("Grouper Red",1,1, R.drawable.grouper_red);
//        insertSpecies("Salmon Alantic ",1,1,R.drawable.salmon_atlantic);
//        insertSpecies("Salmon Chinook",1,1,R.drawable.salmon_chinook);
//        insertSpecies("Salmon Coho",1,1,R.drawable.salmon_coho);
//        insertSpecies("Salmon Pink",1,1,R.drawable.salmon_pink);
//        insertSpecies("Salmon Sockeye",1,1, R.drawable.salmon_sockeye);
//        insertSpecies("Sauger",1,1,R.drawable.sauger);
//        insertSpecies("Seabass Blackfin",1,1,R.drawable.seabass_blackfin);
//        insertSpecies("Seatrout Spotted",1,1,R.drawable.seatrout_spotted);
//        insertSpecies("Shad American",1,1,R.drawable.shad_american);
//        insertSpecies("Shark Blue", 1,1,R.drawable.shark_blue);
//        insertSpecies("Shark Mako",1,1,R.drawable.shark_mako);
//        insertSpecies("Shark Tiger",1,1, R.drawable.shark_tiger);
//        insertSpecies("Shark White",1,1, R.drawable.shark_white);
//        insertSpecies("Snapper Mutton",1,1,R.drawable.snapper_mutton);
//        insertSpecies("Snapper Red",1,1,R.drawable.snapper_red);
//        insertSpecies("Sunfish Redear",1,1,R.drawable.sunfish_readear);
//        insertSpecies("Sunfish Redbreast",1,1, R.drawable.sunfish_redbreast);
//        insertSpecies("Swordfish",1,1,R.drawable.swordfish);
//        insertSpecies("Taimen",1,1,R.drawable.taimen);
//        insertSpecies("Tarpon",1,1,R.drawable.tarpon);
//        insertSpecies("Tautog",1,1,R.drawable.tautog);
//        insertSpecies("Weakfish",1,1, R.drawable.weakfish);
//        insertSpecies("Trout Rainbow",1,1,R.drawable.trout_rainbow);

        return;
    }

}


