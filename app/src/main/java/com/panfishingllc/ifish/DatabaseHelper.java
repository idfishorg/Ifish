package com.panfishingllc.ifish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jing on 4/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "fish.db";

    // table name
    public static final String AREA_T = "Area_T";
    public static final String SPECIES_T = "Species_T";
    public static final String AREA_SPECIES_T = "AreaSpecies_T";

    // season and rules for regulations
    public static final String SEASON_T = "Season_T";
    public static final String RULE_T = "Rule_T";

    public static final String RECORD_T = "Record_T";

    // area table
    public static final String CREATE_AREA_T = "CREATE TABLE " + AREA_T
            + " (AreaId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Area TEXT)";

    // species table
    public static final String CREATE_SPECIES_T = "CREATE TABLE " + SPECIES_T
            + " (SpeciesId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "SpeciesName TEXT, "
            + "Thumbnail INTEGER)";

    // species area table
    public static final String CREATE_AREA_SPECIES_T = "CREATE TABLE " + AREA_SPECIES_T
            + " (AreaSpeciesId INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " AreaId INTEGER,"
            + " SpeciesId INTEGER,"
            + " FOREIGN KEY(SpeciesId) REFERENCES " + SPECIES_T + "(SpeciesId), "
            + " FOREIGN KEY(AreaId) REFERENCES " + AREA_T +"(AreaId) "
            + ")";

    // season table
    public static final String CREATE_SEASON_T = "CREATE TABLE " + SEASON_T
            + " (SeasonId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " OpenDate TEXT,"
            + " CloseDate TEXT,"
            + " AreaSpeciesId INTEGER, "
            + " FOREIGN KEY(AreaSpeciesId) REFERENCES " + AREA_SPECIES_T + "(AreaSpeciesId)"
            + ")";

    // rule table
    public static final String CREATE_RULE_T = "CREATE TABLE " + RULE_T
            + " (RuleId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Rule TEXT,"
            + "SeasonId INTEGER, "
            + "FOREIGN KEY(SeasonId) REFERENCES " + SEASON_T + "(SeasonId) "
            + ")";

//    regulation table
//    public static final String CREATE_REGULATION_T = "CREATE TABLE " + REGULATION_T
//            + " (RegulationId INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + "SpeciesId INTERGER, "
//            + "AreaId, INTEGER, "
//            + "SeasonId INTEGER, "
//            + "FOREIGN KEY(SpeciesId) REFERENCES " + SPECIES_T + "(SpeciesId), "
//            + "FOREIGN KEY(AreaId) REFERENCES " + AREA_T +"(AreaId), "
//            + "FOREIGN KEY(SeasonId) REFERENCES " + SEASON_T +"(SeasonId)"
//            + ")";

    public static final String CREATE_RECORD_T = "CREATE TABLE " + RECORD_T
            + " (RecordId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "SpeciesId INTEGER, "
            + "Weight INTEGER, "
            + "RecordDate TEXT, "
            + "Location TEXT, "
            + "Angler TEXT, "
            + "FOREIGN KEY(SpeciesId) REFERENCES " + SPECIES_T + "(SpeciesId)"
            + ")";

//    // regulation table
//    public static final String SPECIES_ID = "species_id";
//    public static final String TYPE_ID = "type_id";
//    public static final String OPEN_DATE = "open_date";
//    public static final String CLOSE_DATE = "close_date";
//    public static final String MIN_SIZE = "min_size";
//    public static final String BAG_LIMIT = "bag_limit";

//    public static final String CREATE_REGULATION_TABLE = "CREATE TABLE " + REGULATION_TABLE
//            + "(" + SPECIES_ID + " INTEGER, "
//            + TYPE_ID + " INTEGER, "
//            + OPEN_DATE + " TEXT, "
//            + CLOSE_DATE + " TEXT, "
//            + MIN_SIZE + " REAL, "
//            + BAG_LIMIT + " INTEGER, "
//            + "PRIMARY KEY(" + SPECIES_ID + ", " + TYPE_ID + "), "
//            + "FOREIGN KEY(" + SPECIES_ID + ") REFERENCES " + SPECIES_TABLE + "(_id), "
//            + "FOREIGN KEY(" + TYPE_ID + ") REFERENCES " + TYPE_TABLE_NAME +"(_id) "
//            + ")";

    // record table
//    public static final String WEIGHT = "weight";
//    public static final String RECORD_DATE = "record_date";
//    public static final String LOCATION = "location";
//    public static final String ANGLER = "angler";
//
//    public static final String CREATE_RECORD_TABLE = "CREATE TABLE " + RECORD_TABLE_NAME
//            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + SPECIES_ID +" INTEGER, "
//            + WEIGHT + " INTEGER, "
//            + RECORD_DATE + " TEXT, "
//            + LOCATION + " TEXT, "
//            + ANGLER + " TEXT, "
//            + "FOREIGN KEY(" + SPECIES_ID + ") REFERENCES " + SPECIES_TABLE + "(_id)"
//            + ")";



    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AREA_T);
        db.execSQL(CREATE_SPECIES_T);
        db.execSQL(CREATE_AREA_SPECIES_T);

        db.execSQL(CREATE_SEASON_T);
        db.execSQL(CREATE_RULE_T);

        db.execSQL(CREATE_RECORD_T);
        return;
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + SPECIES_T);
        db.execSQL("DROP TABLE IF EXISTS " + AREA_T);
        db.execSQL("DROP TABLE IF EXISTS " + AREA_SPECIES_T);

        db.execSQL("DROP TABLE IF EXISTS " + SEASON_T);
        db.execSQL("DROP TABLE IF EXISTS " + RULE_T);

        db.execSQL("DROP TABLE IF EXISTS " + RECORD_T);

        onCreate(db);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        clear();
        return;
    }

    public Cursor getSpeiciesData(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SpeciesId, SpeciesName, Thumbnail FROM Species_T WHERE SpeciesId = " + String.valueOf(id), null
        );

        if (cursor != null) {
            cursor.moveToNext();
        }
        return cursor;
    }

    public Cursor getSeason(String speciesName, String area) {
        int speciesId = getSpeciesId(speciesName);
        int areaId = getAreaId(area);

        Log.e("DB", String.valueOf(speciesId));
        Log.e("DB", String.valueOf(areaId));

        if (areaId == 0 || areaId == 0) {
            return null;
        }


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SeasonId, OpenDate, CloseDate FROM "
                    + " Season_T INNER JOIN AreaSpecies_T "
                    + " ON Season_T.AreaSpeciesId = AreaSpecies_T.AreaSpeciesId "
                    + " WHERE SpeciesId = " + String.valueOf(speciesId) + " "
                    + " AND AreaId = " + String.valueOf(areaId),
                null
        );

        if(cursor != null) {
            Log.e("DB", "null cursor");
            cursor.moveToNext();
        }

        return cursor;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT Species_T.SpeciesId, SpeciesName, Thumbnail, " +
                        " regulation.open_date, regulation.close_date, regulation.min_size, regulation.bag_limit " +
                        " FROM regulation INNER JOIN species ON regulation.species_id = species._id  " +
                        " WHERE species._id = " + String.valueOf(id), null
        );

        if (cursor != null) {
            cursor.moveToNext();
        }
        return cursor;
    }

    public Cursor getRule(int seasonId) {
        SQLiteDatabase db = getReadableDatabase();
        Log.e("RU", String.valueOf(seasonId));
        Cursor cursor = db.rawQuery(
                "SELECT Rule FROM Rule_T WHERE SeasonId = " + String.valueOf(seasonId), null);

        if (cursor != null) {
            cursor.moveToFirst();
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

    public void insertArea(String area) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("Area", area);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(AREA_T, null, contentValues);
        db.close();
        return;
    }

    public void insertSpecies(String speciesName, Integer thumbnail) {
        ContentValues contentValues = new ContentValues();
        Log.e("AA", speciesName);
        Log.e("AA", String.valueOf(thumbnail));
        contentValues.put("SpeciesName", speciesName);
        contentValues.put("Thumbnail", thumbnail);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(SPECIES_T, null, contentValues);
        db.close();
        return;
    }

    public void insertSpeciesArea(String speciesName, String area) {
        int speciesId = getSpeciesId(speciesName);
        int areaId = getAreaId(area);

        ContentValues contentValues = new ContentValues();
        contentValues.put("AreaId", areaId);
        contentValues.put("SpeciesId", speciesId);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(AREA_SPECIES_T, null, contentValues);
        db.close();
    }

    public void insertRecord(String species, int weight, String date, String location, String angler) {
        ContentValues contentValues = new ContentValues();
        int species_id = getSpeciesId(species);

        contentValues.put("SpeciesId", species_id);
        contentValues.put("Weight", weight);
        contentValues.put("RecordDate", date);
        contentValues.put("Location", location);
        contentValues.put("Angler", angler);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(RECORD_T, null, contentValues);
        db.close();
        return;
    }




    public void insertRegulation(String speaciesName,
                                 String area,
                                 String openDate,
                                 String closeDate,
                                 double minSize,
                                 int bag_limit) {
        int areaSpeciesId = getAreaSpeciesId(area, speaciesName);
        Log.e("EE", String.valueOf(areaSpeciesId));
        if (areaSpeciesId == 0) {
            return;
        }

        // season
        ContentValues contentValues = new ContentValues();
        contentValues.put("AreaSpeciesId", areaSpeciesId);
        contentValues.put("OpenDate", openDate);
        contentValues.put("CloseDate", closeDate);
        SQLiteDatabase db = getWritableDatabase();
        // todo: insert AreaSpeciesId
        long seasonId = db.insert(SEASON_T, null, contentValues);

        // rules
        contentValues.clear();
        String sizeRule = "Minimum " + String.valueOf(minSize) + "\"";
        contentValues.put("Rule", sizeRule);
        contentValues.put("SeasonId", seasonId);
        db.insert(RULE_T, null, contentValues);

        // bag rule
        contentValues.clear();
        String bagRule = String.valueOf(bag_limit);
        contentValues.put("Rule", bagRule);
        contentValues.put("SeasonId", seasonId);
        db.insert(RULE_T, null, contentValues);

        db.close();
        return;
    }


    public int getSeasonId(String openDate, String closeDate) {
        SQLiteDatabase db = getReadableDatabase();
        String q = "SELECT SeasonId FROM " + SEASON_T
                + " WHERE  OpenDate = \"" + openDate + "\""
                + " AND CloseDate = \"" + closeDate + "\"";

        Log.e("DB", q);

        Cursor cursor = db.rawQuery(q, null);

        if (cursor == null)
            return 0;

        cursor.moveToNext();

        if(cursor.isAfterLast())
            return 0;

        return cursor.getInt(cursor.getColumnIndex("SeasonId"));
    }

    public void updateSpecies(Integer id, String name, Integer size, Integer record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("SpeciesName", name);
        contentValues.put("size", size);
        contentValues.put("record", record);

        SQLiteDatabase db = getWritableDatabase();
        db.update(SPECIES_T, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        db.close();
        return;
    }

    public int getAreaSpeciesId(String area, String species) {
        SQLiteDatabase db = getReadableDatabase();
        String q = "SELECT AreaSpeciesId FROM Area_T INNER JOIN AreaSpecies_T ON "
                + " Area_T.AreaId = AreaSpecies_T.AreaId INNER JOIN Species_T ON "
                + " AreaSpecies_T.SpeciesId = Species_T.SpeciesId"
                + " WHERE Area = \"" + area + "\" AND SpeciesName = \"" + species + "\"";

        Log.e("DB", q);

        Cursor cursor = db.rawQuery(q, null);

        if (cursor == null)
            return 0;

        cursor.moveToNext();

        if(cursor.isAfterLast())
            return 0;

        return cursor.getInt(cursor.getColumnIndex("AreaSpeciesId"));
    }

    public int getAreaId(String area) {
        SQLiteDatabase db = getReadableDatabase();
        String q = "SELECT AreaId FROM " + AREA_T + " WHERE  area = \"" + area + "\"";

        Log.e("DB", q);

        Cursor cursor = db.rawQuery(q, null);

        if (cursor == null)
            return 0;

        cursor.moveToNext();

        if(cursor.isAfterLast())
            return 0;

        return cursor.getInt(cursor.getColumnIndex("AreaId"));
    }

    public int getSpeciesId(String speciesName) {
        SQLiteDatabase db = getReadableDatabase();

        String q = "SELECT SpeciesId FROM " + SPECIES_T + " WHERE SpeciesName =\"" + speciesName +"\"";
        Cursor cursor = db.rawQuery(q, null);

        if (cursor == null)
            return 0;

        cursor.moveToNext();

        if(cursor.isAfterLast())
            return 0;

        return cursor.getInt(cursor.getColumnIndex("SpeciesId"));
    }

    public Cursor getAllSpecies() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(SPECIES_T, new String[]{"SpeciesId", "SpeciesName", "Thumbnail"},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }



    public Cursor getAllSpecies(String area) {
        int areaId = getAreaId(area);
        Log.e("getAllSpecies", area + " " + String.valueOf(areaId));

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT Species_T.SpeciesId as SpeciesId, SpeciesName, Thumbnail " +
                        " FROM Species_T INNER JOIN AreaSpecies_T ON Species_T.SpeciesId = AreaSpecies_T.SpeciesId " +
                        " INNER JOIN Area_T ON AreaSpecies_T.AreaId = Area_T.AreaId " +
                        " WHERE Area = \"" + area + "\"", null
        );

        if (cursor != null) {
            cursor.moveToNext();
        }

        return cursor;
    }

    public void addSomeSpecies() {
        populateSpecies();

        populateAreas();

        populateSpeciesOfStates();

        populateRegulations();

//        populateRecord();

        return;
    }

    public void populateRegulations() {
        // NY regulation
        insertRegulation("Bass Striped",    "NY", "2016-04-15", "2016-12-31", 28, 1);
        insertRegulation("Bluefish",        "NY", "2016-01-01", "2016-12-31", 12, 15);
        insertRegulation("Bass Largemouth", "NY", "2016-06-17", "2016-11-30", 12, 5);
        insertRegulation("Bass Smallmouth", "NY", "2016-06-17", "2016-11-30", 99999, 0);
        insertRegulation("Flounder Summer", "NY", "2016-05-17", "2016-09-21", 18, 5);
        insertRegulation("Flounder Winter", "NY", "2016-04-01", "2016-05-30", 12, 2);
        insertRegulation("Tautog", "NY", "2016-10-05", "2016-12-14", 16, 4);
        insertRegulation("Weakfish", "NY", "2016-01-01", "2016-12-31", 16, 1);
        insertRegulation("Cod Atlantic", "NY", "2016-01-01", "2016-12-31", 22, 10);
        insertRegulation("Cobia", "NY", "2016-01-01", "2016-12-31", 37, 2);
    }

    public void populateSpeciesOfStates() {
        String[] speciesOfNY = {"Flounder Summer", "Flounder Winter", "Tautog", "Bluefish",
            "Weakfish", "Cod Atlantic", "Pollock", "Haddock", "Bass Striped"};
        for (int i = 0; i < speciesOfNY.length; i++) {
            insertSpeciesArea(speciesOfNY[i], "NY");
            insertSpeciesArea(speciesOfNY[i], "NJ");
        }
    }

    public void populateSpecies() {
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
    }

    public void populateAreas() {
        String[] areas = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE",
                "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA",
                "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV",
                "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA",
                "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV",
                "WI", "WY"};
        for (int i = 0; i < areas.length; i++) {
            insertArea(areas[i]);
        }
    }

    public void populateRecord() {
        // world record
        insertRecord("Bass Striped",     1310, "2011-08-04", "Long Island Sound, Westbrook, Connecticut, USA", "Gregory Myerson");
        insertRecord("Bluefish",         508,  "1972-01-30", "Hatteras, North Carolina, USA", "James Hussey");
        insertRecord("Bass Largemouth",  356,  "1932-06-02", "Montgomery Lake, Georgia, USA", "George W. Perry");
        insertRecord("Mackerel Spanish", 206,  "1987-11-04", "Ocracoke Inlet, North Carolina, USA", "Robert Cranton");
        insertRecord("Mackerel King",    1488, "1999-04-18", "San Juan, Puerto Rico", "Steve Graulau");
        insertRecord("Cobia",            2169, "1985-07-09", "Shark Bay, W.A., Australia", "Peter Goulding");
    }

}


