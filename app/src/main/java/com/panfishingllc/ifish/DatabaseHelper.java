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

    // area table: id area
    public static final String CREATE_AREA_T = "CREATE TABLE " + AREA_T
            + " (AreaId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Area TEXT)";

    // species table: id, name, picture id
    public static final String CREATE_SPECIES_T = "CREATE TABLE " + SPECIES_T
            + " (SpeciesId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "SpeciesName TEXT, "
            + "Thumbnail INTEGER)";

    // species area table: id, area, species id,
    public static final String CREATE_AREA_SPECIES_T = "CREATE TABLE " + AREA_SPECIES_T
            + " (AreaSpeciesId INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " AreaId INTEGER,"
            + " SpeciesId INTEGER,"
            + " FOREIGN KEY(SpeciesId) REFERENCES " + SPECIES_T + "(SpeciesId), "
            + " FOREIGN KEY(AreaId) REFERENCES " + AREA_T + "(AreaId) "
            + ")";

    // season table: seasonid, open date, close date, area species id,
    public static final String CREATE_SEASON_T = "CREATE TABLE " + SEASON_T
            + " (SeasonId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " OpenDate TEXT,"
            + " CloseDate TEXT,"
            + " AreaSpeciesId INTEGER, "
            + " FOREIGN KEY(AreaSpeciesId) REFERENCES " + AREA_SPECIES_T + "(AreaSpeciesId)"
            + ")";

    // rule table: id, rule, season id
    public static final String CREATE_RULE_T = "CREATE TABLE " + RULE_T
            + " (RuleId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Rule TEXT,"
            + "SeasonId INTEGER, "
            + "FOREIGN KEY(SeasonId) REFERENCES " + SEASON_T + "(SeasonId) "
            + ")";

    // record table:
    public static final String CREATE_RECORD_T = "CREATE TABLE " + RECORD_T
            + " (RecordId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "SpeciesId INTEGER, "
            + "Weight INTEGER, "
            + "RecordDate TEXT, "
            + "Location TEXT, "
            + "Angler TEXT, "
            + "FOREIGN KEY(SpeciesId) REFERENCES " + SPECIES_T + "(SpeciesId)"
            + ")";

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

        if (cursor != null) {
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
                "SELECT recordid, weight, recorddate, location, angler" +
                        " FROM Record_T " +
                        " WHERE speciesid = " + String.valueOf(species_id), null
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
                                 String [] rules) {
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
        for (int i = 0; i < rules.length; ++i) {
            contentValues.clear();
            contentValues.put("Rule", rules[i]);
            contentValues.put("SeasonId", seasonId);
            db.insert(RULE_T, null, contentValues);
        }

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
        String bagRule = "Bag Limit: " + String.valueOf(bag_limit);
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

        if (cursor.isAfterLast())
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

        if (cursor.isAfterLast())
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

        if (cursor.isAfterLast())
            return 0;

        return cursor.getInt(cursor.getColumnIndex("AreaId"));
    }

    public int getSpeciesId(String speciesName) {
        SQLiteDatabase db = getReadableDatabase();

        String q = "SELECT SpeciesId FROM " + SPECIES_T + " WHERE SpeciesName =\"" + speciesName + "\"";
        Cursor cursor = db.rawQuery(q, null);

        if (cursor == null)
            return 0;

        cursor.moveToNext();

        if (cursor.isAfterLast())
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

        return;
    }

    private void populateSpeciesList(String [] species, String state) {
        for (int i = 0; i < species.length; i++) {
            insertSpeciesArea(species[i], state);
        }
    }

    //    public static final String AMERICAN_EEL = "American Eel";
    public static final String BASS_BLACK_SEA = "Bass Black Sea";
    public static final String BASS_STRIPED = "Bass Striped";
    public static final String BLUEFISH = "Bluefish";
    public static final String COBIA = "Cobia";
    public static final String DRUM_BLACK = "Drum Black";
    public static final String DRUM_RED = "Drum Red";
    public static final String COD_ATLANTIC = "Cod Atlantic";
    public static final String FLOUNDER_SUMMER = "Flounder Summer";
    public static final String FLOUNDER_YELLOWTAIL = "Flounder Yellowtail";
    public static final String FLOUNDER_WINTER = "Flounder Winter";
    public static final String HADDOCK = "Haddock";
    public static final String MACKEREL_KING = "Mackerel King";
    public static final String MACKEREL_SPANISH = "Mackerel Spanish";
    public static final String MENHADEN_ATLANTIC = "Menhaden Atlantic";
    public static final String POLLOCK = "Pollock";
    public static final String SHAD_AMERICAN = "Shad American";
    public static final String SHAD_HICKORY = "Shad Hickory";
    public static final String SCUP = "Scup";
    public static final String STURGEON_ATLANTIC = "Sturgeon Atlantic";
    public static final String TAUTOG = "Tautog";
    public static final String TOADFISH_OYSTER = "Toadfish Oyster";
    public static final String WEAKFISH = "Weakfish";

    public void populateSpecies() {
//        insertSpecies(AMERICAN_EEL, );
        insertSpecies(BASS_BLACK_SEA, R.drawable.bass_black_sea);
        insertSpecies("Bass Largemouth", R.drawable.bass_large_mouth);
        insertSpecies("Bass Smallmouth", R.drawable.bass_small_mouth);
        insertSpecies(BASS_STRIPED, R.drawable.bass_striped);
        insertSpecies("Bass White", R.drawable.bass_white);
        insertSpecies(BLUEFISH, R.drawable.bluefish);
        insertSpecies("Carp Common", R.drawable.carp_common);
        insertSpecies("Catfish Channel", R.drawable.catfish_channel);
        insertSpecies(COBIA, R.drawable.cobia);
        insertSpecies(COD_ATLANTIC, R.drawable.cod_atlantic);
        insertSpecies("Cod Pacific", R.drawable.cod_pacific);
        insertSpecies(DRUM_RED, R.drawable.drum_red);
        insertSpecies(DRUM_BLACK, R.drawable.drum_black);
        insertSpecies(FLOUNDER_SUMMER, R.drawable.flounder_summer);
        insertSpecies(FLOUNDER_YELLOWTAIL, R.drawable.flounder_yellowtail);
        insertSpecies(FLOUNDER_WINTER, R.drawable.flounder_winter);
        insertSpecies("Gar Alligator", R.drawable.gar_alligator);
        insertSpecies("Gar Longnose", R.drawable.gar_longnose);
        insertSpecies("Gar Shortnose", R.drawable.gar_shortnose);
        insertSpecies("Grouper Black",R.drawable.grouper_black);
        insertSpecies("Grouper Red", R.drawable.grouper_red);
        insertSpecies(HADDOCK, R.drawable.haddock);
        insertSpecies(MACKEREL_KING, R.drawable.mackerel_king);
        insertSpecies(MACKEREL_SPANISH, R.drawable.mackerel_span);
        insertSpecies(MENHADEN_ATLANTIC, R.drawable.menhaden_atlantic);
        insertSpecies(POLLOCK, R.drawable.pollock);
        insertSpecies("Salmon Alantic ",R.drawable.salmon_atlantic);
        insertSpecies("Salmon Chinook",R.drawable.salmon_chinook);
        insertSpecies("Salmon Coho",R.drawable.salmon_coho);
        insertSpecies("Salmon Pink",R.drawable.salmon_pink);
        insertSpecies("Salmon Sockeye", R.drawable.salmon_sockeye);
        insertSpecies("Sauger",R.drawable.sauger);
        insertSpecies(SCUP, R.drawable.scup);
        insertSpecies("Seabass Blackfin",R.drawable.seabass_blackfin);
        insertSpecies("Seatrout Spotted",R.drawable.seatrout_spotted);
        insertSpecies(SHAD_AMERICAN, R.drawable.shad_american);
        insertSpecies(SHAD_HICKORY, R.drawable.shad_hickory);
        insertSpecies("Shark Blue",R.drawable.shark_blue);
        insertSpecies("Shark Mako",R.drawable.shark_mako);
        insertSpecies("Shark Tiger", R.drawable.shark_tiger);
        insertSpecies("Shark White", R.drawable.shark_white);
        insertSpecies("Snapper Mutton",R.drawable.snapper_mutton);
        insertSpecies("Snapper Red",R.drawable.snapper_red);
        insertSpecies("Sunfish Redear",R.drawable.sunfish_readear);
        insertSpecies("Sunfish Redbreast", R.drawable.sunfish_redbreast);
        insertSpecies(STURGEON_ATLANTIC, R.drawable.sturgeon_atlantic);
        insertSpecies("Swordfish",R.drawable.swordfish);
        insertSpecies("Taimen",R.drawable.taimen);
        insertSpecies("Tarpon",R.drawable.tarpon);
        insertSpecies(TAUTOG, R.drawable.tautog);
        insertSpecies(TOADFISH_OYSTER, R.drawable.toadfish_oyster);
        insertSpecies("Trout Rainbow",R.drawable.trout_rainbow);
        insertSpecies(WEAKFISH, R.drawable.weakfish);
    }

    public void populateAreas() {
        String[] areas = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE",
                "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA",
                "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV",
                "NH", "NEW JERSEY", "NM", "NEW YORK", "NC", "ND", "OH", "OK", "OR", "PA",
                "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV",
                "WI", "WY"};

        for (int i = 0; i < areas.length; i++) {
            insertArea(areas[i]);
        }
    }

    public void populateSpeciesOfStates() {
        String[] speciesOfNY = {
                BASS_BLACK_SEA,
                BASS_STRIPED,
                BLUEFISH,
                COBIA,
                COD_ATLANTIC,
                DRUM_RED,
                FLOUNDER_SUMMER,
                FLOUNDER_YELLOWTAIL,
                FLOUNDER_WINTER,
                HADDOCK,
                MACKEREL_SPANISH,
                MACKEREL_KING,
                MENHADEN_ATLANTIC,
                POLLOCK,
                SHAD_AMERICAN,
                SHAD_HICKORY,
                SCUP,
                STURGEON_ATLANTIC,
                TAUTOG,
                TOADFISH_OYSTER,
                WEAKFISH,
        };
        populateSpeciesList(speciesOfNY, "NEW YORK");

        String[] speciesOfNJ = {
//                AMERICAN_EEL,
                BASS_BLACK_SEA,
                BASS_STRIPED,
                BLUEFISH,
                COBIA,
                COD_ATLANTIC,
                DRUM_BLACK,
                DRUM_RED,
                FLOUNDER_SUMMER,
                FLOUNDER_WINTER,
                HADDOCK,
                MACKEREL_SPANISH,
                MACKEREL_KING,
                SCUP,
                POLLOCK,
                TAUTOG,
                WEAKFISH,
        };
        populateSpeciesList(speciesOfNJ, "NEW JERSEY");
    }

    public static final String MINI_LEN = "Minimum Length: ";
    public static final String MAX_LEN =  "Maximum Length: ";
    public static final String BAG_LIMIT = "Bag Limit: ";
    public static final String NO_MINI_LEN = "No minimum size";
    public static final String NO_BAG_LIMIT = "No bag limit";

    public void populateRegulations() {
        // regulations
        // Species State Open, Close, Minimum Size, Bag Limit

        // NY regulation ------------------------------------------------------------------------
        String NY = "NEW YORK";
        insertRegulation(BASS_BLACK_SEA,  NY, "2016-07-15", "2016-10-31", new String[] {MINI_LEN + "14\"", BAG_LIMIT + 8});
        insertRegulation(BASS_BLACK_SEA,  NY, "2016-11-01", "2016-12-31", new String[] {MINI_LEN + "14\"", BAG_LIMIT + 10});
        insertRegulation(BASS_STRIPED,    NY, "2016-04-15", "2016-12-31", new String[] {"Marine waters (South of George Washington Bridge)", MINI_LEN + "28\"", BAG_LIMIT + 1});
        insertRegulation(BASS_STRIPED,    NY, "2016-04-01", "2016-11-30", new String[] {"Hudson River (North of George Washington Bridge)", "18\" ~ 28\" or >40\"", BAG_LIMIT + 1});
        insertRegulation(BLUEFISH,        NY, "2016-01-01", "2016-12-31", new String[] {"No minimum size for first 10 fish", "12\" TL for the next 5", BAG_LIMIT + 15, "No more than 10 of which shall be less than 12\" TL"});
        insertRegulation(COBIA,           NY, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "37\"", BAG_LIMIT + 2});
        insertRegulation(COD_ATLANTIC,    NY, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "22\"", BAG_LIMIT + 10});
        insertRegulation(DRUM_RED,        NY, "2016-01-01", "2016-12-31", new String[] {MAX_LEN  + "27\"", NO_BAG_LIMIT});
        insertRegulation(FLOUNDER_SUMMER, NY, "2016-05-17", "2016-09-21", new String[] {MINI_LEN + "18\"", BAG_LIMIT + 5});
        insertRegulation(FLOUNDER_YELLOWTAIL, NY, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "13\"", NO_BAG_LIMIT});
        insertRegulation(FLOUNDER_WINTER, NY, "2016-04-01", "2016-05-30", new String[] {MINI_LEN + "12\"", BAG_LIMIT + 2});
        insertRegulation(HADDOCK,         NY, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "18\"", NO_BAG_LIMIT});
        insertRegulation(MACKEREL_SPANISH,NY, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "14\"", BAG_LIMIT + 15});
        insertRegulation(MACKEREL_KING,   NY, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "23\"", BAG_LIMIT + 3});
        insertRegulation(MENHADEN_ATLANTIC, NY, "2016-01-01", "2016-12-31", new String[] {NO_MINI_LEN, BAG_LIMIT + 100});
        insertRegulation(POLLOCK,         NY, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "19\"", NO_BAG_LIMIT});
        insertRegulation(SCUP,            NY, "2016-05-01", "2016-12-31", new String[] {MINI_LEN + "10\"", BAG_LIMIT + 30});
        insertRegulation(SHAD_AMERICAN,   NY, "2016-12-32", "2016-12-31", new String[] {"No possession allowed"});
        insertRegulation(SHAD_HICKORY,    NY, "2016-01-01", "2016-12-31", new String[] {NO_MINI_LEN, BAG_LIMIT + 5});
        insertRegulation(STURGEON_ATLANTIC,NY,"2016-12-32", "2016-12-30", new String[] {"Moratorium", "No possession allowed"});
        insertRegulation(TAUTOG,          NY, "2016-10-05", "2016-12-14", new String[] {MINI_LEN + "16\"", BAG_LIMIT + 4});
        insertRegulation(TOADFISH_OYSTER, NY, "2016-07-16", "2017-05-15", new String[] {MINI_LEN + "10\"", BAG_LIMIT + 3});
        insertRegulation(WEAKFISH,        NY, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "16\"", "10\" filleted", "12\" dressed", BAG_LIMIT + 1});


//        insertRegulation("Bass Largemouth", NY, "2016-06-17", "2016-11-30", 12, 5);
//        insertRegulation("Bass Smallmouth", NY, "2016-06-17", "2016-11-30", 99999, 0);

        // NJ regulation ------------------------------------------------------------------------
        String NJ = "NEW JERSEY";
        insertRegulation(BASS_BLACK_SEA,   NJ, "2016-05-23", "2016-06-19", new String[] {MINI_LEN + "12.5\"", BAG_LIMIT + 10});
        insertRegulation(BASS_BLACK_SEA,   NJ, "2016-07-01", "2016-08-31", new String[] {MINI_LEN + "12.5\"", BAG_LIMIT + 2});
        insertRegulation(BASS_BLACK_SEA,   NJ, "2016-10-22", "2016-12-31", new String[] {MINI_LEN + "13\"", BAG_LIMIT + 5});
        insertRegulation(BASS_STRIPED,     NJ, "2016-05-01", "2016-12-31", new String[] {"1 fish at 28 inches to less than 43 inches", "1 fish at 43 inches or greater"});
        insertRegulation(BLUEFISH,         NJ, "2016-01-01", "2016-12-31", new String[] {NO_MINI_LEN, BAG_LIMIT + 15});
        insertRegulation(COD_ATLANTIC,     NJ, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "19\"", NO_BAG_LIMIT});
        insertRegulation(DRUM_BLACK,       NJ, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "13\"", BAG_LIMIT + 1});
        insertRegulation(FLOUNDER_SUMMER,  NJ, "2016-05-21", "2016-09-25", new String[] {MINI_LEN + "18\"", BAG_LIMIT + 5});
        insertRegulation(FLOUNDER_WINTER,  NJ, "2016-03-01", "2016-12-31", new String[] {MINI_LEN + "12\"", BAG_LIMIT + 2});
        insertRegulation(HADDOCK,          NJ, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "21\"", NO_BAG_LIMIT});
        insertRegulation(POLLOCK,          NJ, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "19\"", NO_BAG_LIMIT});
        insertRegulation(TAUTOG,           NJ, "2016-01-01", "2016-02-28", new String[] {MINI_LEN + "15\"", BAG_LIMIT + 4});
        insertRegulation(TAUTOG,           NJ, "2016-04-01", "2016-04-30", new String[] {MINI_LEN + "15\"", BAG_LIMIT + 4});
        insertRegulation(TAUTOG,           NJ, "2016-07-17", "2016-11-15", new String[] {MINI_LEN + "15\"", BAG_LIMIT + 1});
        insertRegulation(TAUTOG,           NJ, "2016-11-16", "2016-12-31", new String[] {MINI_LEN + "15\"", BAG_LIMIT + 6});
        insertRegulation(WEAKFISH,         NJ, "2016-01-01", "2016-12-31", new String[] {MINI_LEN + "13\"", BAG_LIMIT + 1});
    }

    public void populateRecord() {
        // world record
        insertRecord(BASS_BLACK_SEA, 164, "2000-01-01", "Virginia Beach, Virginia, USA", "Allan Paschall");
        insertRecord(BASS_STRIPED, 1310, "2011-08-04", "Long Island Sound, Westbrook, Connecticut, USA", "Gregory Myerson");
        insertRecord("Bass Largemouth", 356, "1932-06-02", "Montgomery Lake, Georgia, USA", "George W. Perry");
        insertRecord(BLUEFISH, 508, "1972-01-30", "Hatteras, North Carolina, USA", "James Hussey");
        insertRecord(DRUM_BLACK, 1809, "1975-09-15", "Lewes, Delaware, USA", "Gerald Townsend");
        insertRecord("Mackerel Spanish", 206, "1987-11-04", "Ocracoke Inlet, North Carolina, USA", "Robert Cranton");
        insertRecord("Mackerel King", 1488, "1999-04-18", "San Juan, Puerto Rico", "Steve Graulau");
        insertRecord("Cobia", 2169, "1985-07-09", "Shark Bay, W.A., Australia", "Peter Goulding");
        insertRecord(WEAKFISH, 316, "2008-05-07", "Staten Island, New York, USA", "Dave Alu");
    }

}


