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
    public static final String RECORD_TABLE_NAME = "record";
    public static final String TYPE_TABLE_NAME = "type";
    public static final String STATE_TABLE_NAME = "state";

    // species table
    public static final String NAME = "name";
    public static final String THUMBNAIL = "thumbnail";
    public static final String CREATE_SPECIES_TABLE = "CREATE TABLE " + SPECIES_TABLE_NAME
            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT "
            + NAME + " TEXT, "
            + THUMBNAIL + " INTEGER)";

    // type table
    public static final String TYPE = "type";
    public static final String CREATE_TYPE_TABLE = "CREATE TABLE " + TYPE_TABLE_NAME
            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT)";

    // regulation table
    public static final String SPECIES_ID = "species_id";
    public static final String TYPE_ID = "type_id";
    public static final String OPEN_DATE = "open_date";
    public static final String CLOSE_DATE = "close_date";
    public static final String MIN_SIZE = "min_size";
    public static final String BAG_LIMIT = "bag_limit";
    public static final String CREATE_REGULATION_TABLE = "CREATE TABLE " + REGULATION_TABLE_NAME
            + "(" + SPECIES_ID + " INTEGER, "
            + TYPE_ID + " INTEGER, "
            + OPEN_DATE + " TEXT, "
            + CLOSE_DATE + " TEXT, "
            + MIN_SIZE + " REAL, "
            + BAG_LIMIT + " INTEGER, "
            + "PRIMARY KEY(" + SPECIES_ID + ", " + TYPE_ID + "), "
            + "FOREIGN KEY(" + SPECIES_ID + ") REFERENCES " + SPECIES_TABLE_NAME + "(_id), "
            + "FOREIGN KEY(" + TYPE_ID + ") REFERENCES " + TYPE_TABLE_NAME +"(_id) "
            + ")";

    // record table
    public static final String WEIGHT = "weight";
    public static final String RECORD_DATE = "record_date";
    public static final String LOCATION = "location";
    public static final String ANGLER = "angler";

    public static final String CREATE_RECORD_TABLE = "CREATE TABLE " + RECORD_TABLE_NAME +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SPECIES_ID +" INTEGER, "
            + WEIGHT + " INTEGER, "
            + RECORD_DATE + " TEXT, "
            + LOCATION + " TEXT, "
            + ANGLER + " TEXT, "
            + "FOREIGN KEY(" + SPECIES_ID + ") REFERENCES " + SPECIES_TABLE_NAME + "(_id)"
            + ")";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TYPE_TABLE);
        db.execSQL(CREATE_SPECIES_TABLE);
        db.execSQL(CREATE_REGULATION_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);

        // populate data
//        addSomeSpecies();
        return;
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + REGULATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TYPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SPECIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RECORD_TABLE_NAME);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        clear();
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
        return cursor;
    }

    public void insertSpecies(String name, Integer thumbnail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(THUMBNAIL, thumbnail);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(SPECIES_TABLE_NAME, null, contentValues);
        db.close();
        return;
    }

    public void insertRecord(String species, int weight, String date, String location, String angler) {
        ContentValues contentValues = new ContentValues();
        int species_id = getSpeciesId(species);

        contentValues.put(SPECIES_ID, species_id);
        contentValues.put(WEIGHT, weight);
        contentValues.put(RECORD_DATE, date);
        contentValues.put(LOCATION, location);
        contentValues.put(ANGLER, angler);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(RECORD_TABLE_NAME, null, contentValues);
        db.close();
        return;
    }

    public void insertState(String name) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TYPE, name);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TYPE_TABLE_NAME, null, contentValues);
        db.close();
        return;
    }

    public void insertRegulation(String name,
                                 String type,
                                 String open_date,
                                 String close_date,
                                 double min_size,
                                 int bag_limit) {

        int species_id = getSpeciesId(name);
        if (species_id == 0)
            return;

        int type_id = getTypeId(type);
        if (type_id == 0)
            return;

        ContentValues contentValues = new ContentValues();
        contentValues.put(SPECIES_ID, species_id);
        contentValues.put(TYPE_ID, type_id);
        contentValues.put(OPEN_DATE, open_date);
        contentValues.put(CLOSE_DATE, close_date);
        contentValues.put(MIN_SIZE, min_size);
        contentValues.put(BAG_LIMIT, bag_limit);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(REGULATION_TABLE_NAME, null, contentValues);
        db.close();
        return;
    }

    public void updateSpecies(Integer id, String name, Integer size, Integer record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put("size", size);
        contentValues.put("record", record);

        SQLiteDatabase db = getWritableDatabase();
        db.update(SPECIES_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        db.close();
        return;
    }

    public int getTypeId(String type) {
        SQLiteDatabase db = getReadableDatabase();
        String q = "SELECT _id FROM " + TYPE_TABLE_NAME + " WHERE " + TYPE + "= \"" + type + "\"";
        Cursor cursor = db.rawQuery(q, null);

        if (cursor == null)
            return 0;

        cursor.moveToNext();

        if(cursor.isAfterLast())
            return 0;

        return cursor.getInt(cursor.getColumnIndex("_ID"));
    }

    public int getSpeciesId(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String q = "SELECT _id FROM " + SPECIES_TABLE_NAME + " WHERE " + NAME + "=\"" + name +"\"";
        Cursor cursor = db.rawQuery(q, null);

        if (cursor == null)
            return 0;

        cursor.moveToNext();

        if(cursor.isAfterLast())
            return 0;

        return cursor.getInt(cursor.getColumnIndex("_ID"));
    }

    public Cursor getAllSpecies() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(SPECIES_TABLE_NAME, new String[]{"_ID", "name", "thumbnail"},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor getAllSpecies(String state) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(SPECIES_TABLE_NAME, new String[]{"_ID", "name", "thumbnail"},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public void addSomeSpecies() {
        insertState("NY");
        insertState("NJ");
        insertState("MA");
        insertState("CT");

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


