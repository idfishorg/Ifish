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
    public static final String RECORD_TABLE_NAME = "record";

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

        db.execSQL(
                "CREATE TABLE record " +
                        "(_id integer primary key autoincrement, " +
                        " species_id integer, " +
                        "weight integer, " +
                        "record_date text, " +
                        "location text, " +
                        "angler text, " +
                        "FOREIGN KEY(species_id) REFERENCES species(_id)" +
                        ")"

        );

        addSomeSpecies();
        return;
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("Delete from species WHERE true");
//        db.delete("species", null, null);
        db.execSQL("DROP TABLE IF EXISTS regulation");
        db.execSQL("DROP TABLE IF EXISTS type");
        db.execSQL("DROP TABLE IF EXISTS species");
        db.execSQL("DROP TABLE IF EXISTS record");
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS regulation");
        db.execSQL("DROP TABLE IF EXISTS type");
        db.execSQL("DROP TABLE IF EXISTS species");
        db.execSQL("DROP TABLE IF EXISTS record");
        onCreate(db);
        return;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
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

    public Cursor getRecord(int species_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT _id, weight, record_date, location, angler" +
                        " FROM record " +
                        " WHERE species_id = " + String.valueOf(species_id), null
        );

        if (cursor != null) {
            cursor.moveToNext();
        }
//        db.close();
        return cursor;
    }

    public void insertSpecies(String name, Integer thumbnail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("thumbnail", thumbnail);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(SPECIES_TABLE_NAME, null, contentValues);
        db.close();
        return;
    }

    public void insertRecord(String species, int weight, String date, String location, String angler) {
        ContentValues contentValues = new ContentValues();
        int species_id = getSpeciesId(species);
        contentValues.put("species_id", species_id);
        contentValues.put("weight", weight);
        contentValues.put("record_date", date);
        contentValues.put("location", location);
        contentValues.put("angler", angler);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(RECORD_TABLE_NAME, null, contentValues);

        return;
    }

    public void insertState(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", name);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TYPE_TABLE_NAME, null, contentValues);

        return;
    }

    public void insertRegulation(String name, String type, String open_date, String close_date,
                                 double min_size, int bag_limit) {

        int species_id = getSpeciesId(name);
        if (species_id == 0)
            return;

        int type_id = getTypeId(type);
        if (type_id == 0)
            return;

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

    public int getTypeId(String type) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT _id FROM " + TYPE_TABLE_NAME + " WHERE type = \"" + type + "\"",
                null
        );

        if (cursor == null)
            return 0;

        cursor.moveToNext();

        if(cursor.isAfterLast())
            return 0;

        return cursor.getInt(cursor.getColumnIndex("_id"));
    }

    public int getSpeciesId(String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT _id FROM " + SPECIES_TABLE_NAME + " WHERE name = \"" + name +"\"",
                null
        );

        if (cursor == null)
            return 0;

        cursor.moveToNext();

        if(cursor.isAfterLast())
            return 0;

        return cursor.getInt(cursor.getColumnIndex("_id"));
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
        insertState("NY");
        insertState("NJ");


        insertSpecies("Flounder Summer", R.drawable.flounder_summer);
        insertSpecies("Flounder Winter", R.drawable.flounder_winter);
        insertSpecies("Tautog", R.drawable.tautog);
        insertSpecies("Bluefish", R.drawable.bluefish);
        insertSpecies("Weakfish", R.drawable.weakfish);
        insertSpecies("Cod Atlantic", R.drawable.cod_atlantic);
        insertSpecies("Pollock", R.drawable.pollock);
        insertSpecies("Haddock", R.drawable.haddock);
        insertSpecies("Bass Striped", R.drawable.bass_striped);
        insertSpecies("Drum Red", R.drawable.drum_red);
        insertSpecies("Mackerel Spanish", R.drawable.mackerel_span);
        insertSpecies("Mackerel King", R.drawable.mackerel_king);
        insertSpecies("Cobia", R.drawable.cobia);

        insertSpecies("Bass Largemouth", R.drawable.bass_large_mouth);
        insertSpecies("Bass Smallmouth", R.drawable.bass_small_mouth);

        // NY regulation
        insertRegulation("Bass Striped",    "NY", "2016-04-15", "2016-12-31", 28, 1);
        insertRegulation("Bluefish",        "NY", "2016-01-01", "2016-12-31", 12, 15);
        insertRegulation("Bass Largemouth", "NY", "2016-06-17", "2016-11-30", 12, 5);
        insertRegulation("Bass Smallmouth", "NY", "2016-06-17", "2016-11-30", 99999, 0);
        insertRegulation("Flounder Summer", "NY", "2016-05-17", "2016-09-21", 18, 5);
        insertRegulation("Flounder Winter", "NY", "2016-04-01", "2016-05-30", 12, 2);
        insertRegulation("Tautog",          "NY", "2016-10-05", "2016-12-14", 16, 4);
        insertRegulation("Weakfish",        "NY", "2016-01-01", "2016-12-31", 16, 1);
        insertRegulation("Cod Atlantic",    "NY", "2016-01-01", "2016-12-31", 22, 10);
        insertRegulation("Cobia",           "NY", "2016-01-01", "2016-12-31", 37, 2);

        // world record
        insertRecord("Bass Striped",     1310, "2011-08-04", "Long Island Sound, Westbrook, Connecticut, USA", "Gregory Myerson");
        insertRecord("Bluefish",         508,  "1972-01-30", "Hatteras, North Carolina, USA", "James Hussey");
        insertRecord("Bass Largemouth",  356,  "1932-06-02", "Montgomery Lake, Georgia, USA", "George W. Perry");
        insertRecord("Mackerel Spanish", 206,  "1987-11-04", "Ocracoke Inlet, North Carolina, USA", "Robert Cranton");
        insertRecord("Mackerel King",    1488, "1999-04-18", "San Juan, Puerto Rico", "Steve Graulau");
        insertRecord("Cobia",            2169, "1985-07-09", "Shark Bay, W.A., Australia", "Peter Goulding");

//        insertSpecies("Bass White", 10, 40, R.drawable.bass_white);
//        insertSpecies("Bass Largemouth", 11, 12, R.drawable.bass_large_mouth);
//        insertSpecies("Bass Smallmouth", 11, 23, R.drawable.bass_small_mouth);
//        insertSpecies("Seabass Black", 12, 12, R.drawable.bass_black_sea);
//        insertSpecies("Carp Common", 1, 1, R.drawable.carp_common);
//        insertSpecies("Catfish Channel", 1, 1, R.drawable.catfish_channel);
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
//        insertSpecies("Trout Rainbow",1,1,R.drawable.trout_rainbow);

        return;
    }

}


