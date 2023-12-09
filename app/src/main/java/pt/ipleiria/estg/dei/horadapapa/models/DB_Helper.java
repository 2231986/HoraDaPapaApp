package pt.ipleiria.estg.dei.horadapapa.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB_Helper extends SQLiteOpenHelper {

    private final SQLiteDatabase db;

    private static final String DB_NAME = "horadapapa";
    private static final int DB_VERSION = 1;

    private static final String TABLE_PLATE = "plate";

    public DB_Helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTablePlates = "CREATE TABLE " + TABLE_PLATE + " (" +
                "id" + " INTEGER PRIMARY KEY, " +
                "title" + " TEXT NOT NULL, " +
                "description" + " TEXT NOT NULL, " +
                "price" + " TEXT NOT NULL, " +
                "image" + " TEXT NOT NULL);";

        db.execSQL(sqlCreateTablePlates);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDropTablePlates = "DROP TABLE IF EXISTS " + TABLE_PLATE;
        db.execSQL(sqlDropTablePlates);

        this.onCreate(db);
    }

    public ArrayList<Plate> getPlates(){
        ArrayList<Plate> plates = new ArrayList<>();

        Cursor cursor = db.query(TABLE_PLATE, new String[]{"id", "title", "description", "price", "image"},
        null, null, null, null, "id");

        if(cursor.moveToFirst()){
            do {
                Plate plate = new Plate(cursor);

                plates.add(plate);

            }while (cursor.moveToNext());

            cursor.close();
        }

        return plates;
    }

    public void setPlates(ArrayList<Plate> plates)
    {
        db.delete(TABLE_PLATE, null, null);

        for(Plate plate : plates)
        {
            ContentValues values = new ContentValues();

            values.put("id", plate.getId());
            values.put("title", plate.getTitle());
            values.put("description", plate.getDescription());
            values.put("price", plate.getPrice());
            values.put("image", plate.getImage());

            db.insert(TABLE_PLATE, null, values);
        }
    }
}
