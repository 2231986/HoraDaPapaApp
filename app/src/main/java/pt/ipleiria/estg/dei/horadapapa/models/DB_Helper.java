package pt.ipleiria.estg.dei.horadapapa.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB_Helper extends SQLiteOpenHelper {

    private static final String DB_NAME = "horadapapa";
    private static final int DB_VERSION = 8;
    private static final String TABLE_PLATE = "plate";
    private static final String[] TABLE_PLATE_FIELDS = {"id", "title", "description", "price", "image"};
    private static final String TABLE_INVOICE = "invoice";
    private static final String[] TABLE_INVOICE_FIELDS = {"id", "meal_id", "price"};
    private static final String TABLE_INVOICE_REQUESTS = "invoice_requests";
    private static final String[] TABLE_INVOICE_REQUESTS_FIELDS = {"id", "meal_id", "plate_id"};
    private static final String TABLE_REVIEW = "review";
    private static final String[] TABLE_REVIEW_FIELDS = {"id", "plate_id", "description", "value"};
    private static final String TABLE_FAVORITES = "favorites";
    private static final String[] TABLE_FAVORITES_FIELDS = {"id", "plate_id"};


    private static final String TABLE_HELP_TICKET = "help_tickets";

    private static final String[] TABLE_HELP_TICKET_FIELDS = {"id", "description"};


    private final SQLiteDatabase db;


    public DB_Helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTablePlates(db);
        createTableInvoices(db);
        createTableInvoiceRequests(db);
        createTableReviews(db);
        createTableFavorites(db);
        createTableHelp_Ticket(db);
    }

    private void createTablePlates(SQLiteDatabase db) {
        String command = "CREATE TABLE " + TABLE_PLATE + " (" +
                "id INTEGER PRIMARY KEY, " +
                "title TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "price TEXT NOT NULL, " +
                "image TEXT NOT NULL);";
        db.execSQL(command);
    }

    private void createTableInvoices(SQLiteDatabase db) {
        String command = "CREATE TABLE " + TABLE_INVOICE + " (" +
                "id INTEGER PRIMARY KEY, " +
                "meal_id INTEGER NOT NULL, " +
                "price TEXT NOT NULL);";
        db.execSQL(command);
    }

    private void createTableInvoiceRequests(SQLiteDatabase db) {
        String command = "CREATE TABLE " + TABLE_INVOICE_REQUESTS + " (" +
                "id INTEGER PRIMARY KEY, " +
                "meal_id INTEGER NOT NULL, " +
                "plate_id INTEGER NOT NULL);";
        db.execSQL(command);
    }

    private void createTableReviews(SQLiteDatabase db) {
        String command = "CREATE TABLE " + TABLE_REVIEW + " (" +
                "id INTEGER PRIMARY KEY, " +
                "plate_id INTEGER NOT NULL, " +
                "description TEXT NOT NULL," +
                "value INTEGER NOT NULL);";
        db.execSQL(command);
    }

    private void createTableHelp_Ticket(SQLiteDatabase db) {
        String command = "CREATE TABLE " + TABLE_HELP_TICKET + " (" +
                "id INTEGER PRIMARY KEY, " +
                "description TEXT NOT NULL);";
        db.execSQL(command);
    }

    private void createTableFavorites(SQLiteDatabase db) {
        String command = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                "id INTEGER PRIMARY KEY, " +
                "plate_id INTEGER NOT NULL);";
        db.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELP_TICKET);
        onCreate(db);
    }

    public ArrayList<Plate> getPlates() {
        ArrayList<Plate> plates = new ArrayList<>();

        Cursor cursor = db.query(TABLE_PLATE, TABLE_PLATE_FIELDS,
                null, null, null, null, "id");

        if (cursor.moveToFirst()) {
            do {
                Plate plate = new Plate(cursor);

                plates.add(plate);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return plates;
    }

    public void setPlates(ArrayList<Plate> plates) {
        db.delete(TABLE_PLATE, null, null);

        for (Plate plate : plates) {
            ContentValues values = new ContentValues();

            values.put("id", plate.getId());
            values.put("title", plate.getTitle());
            values.put("description", plate.getDescription());
            values.put("price", plate.getPrice());
            values.put("image", plate.getImage());

            db.insert(TABLE_PLATE, null, values);
        }
    }

    public ArrayList<Invoice> getInvoices() {
        ArrayList<Invoice> invoices = new ArrayList<>();

        Cursor cursor = db.query(TABLE_INVOICE, TABLE_INVOICE_FIELDS,
                null, null, null, null, "id");

        if (cursor.moveToFirst()) {
            do {
                Invoice invoice = new Invoice(cursor);
                invoice.setInvoiceRequests(getInvoiceRequests(invoice));

                invoices.add(invoice);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return invoices;
    }

    public void setInvoices(ArrayList<Invoice> invoices) {
        db.delete(TABLE_INVOICE, null, null);

        for (Invoice invoice : invoices) {
            ContentValues values = new ContentValues();

            values.put("id", invoice.getId());
            values.put("meal_id", invoice.getMeal_id());
            values.put("price", invoice.getPrice());

            db.insert(TABLE_INVOICE, null, values);

            setInvoicesRequests(invoice);
        }
    }

    private ArrayList<InvoiceRequest> getInvoiceRequests(Invoice invoice) {
        ArrayList<InvoiceRequest> requests = new ArrayList<>();

        String selection = "meal_id = ?";
        String[] selectionArgs = {String.valueOf(invoice.getMeal_id())};

        Cursor cursor = db.query(TABLE_INVOICE_REQUESTS, TABLE_INVOICE_REQUESTS_FIELDS,
                selection, selectionArgs, null, null, "id");

        if (cursor.moveToFirst()) {
            do {
                InvoiceRequest request = new InvoiceRequest(cursor);
                requests.add(request);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return requests;
    }

    private void setInvoicesRequests(Invoice invoice) {
        // Delete only the rows where invoice_id matches the specified invoice's id
        String whereClause = "meal_id = ?";
        String[] whereArgs = {String.valueOf(invoice.getMeal_id())};
        db.delete(TABLE_INVOICE_REQUESTS, whereClause, whereArgs);

        ArrayList<InvoiceRequest> requests = invoice.getInvoiceRequests();

        if (requests != null) {
            for (InvoiceRequest request : requests) {
                ContentValues values = new ContentValues();

                values.put("id", request.getId());
                values.put("meal_id", invoice.getMeal_id());
                values.put("plate_id", request.getPlate_id());

                db.insert(TABLE_INVOICE_REQUESTS, null, values);
            }
        }
    }

    public ArrayList<Review> getReviews() {
        ArrayList<Review> reviews = new ArrayList<>();

        Cursor cursor = db.query(TABLE_REVIEW, TABLE_REVIEW_FIELDS,
                null, null, null, null, "id");

        if (cursor.moveToFirst()) {
            do {
                Review review = new Review(cursor);

                reviews.add(review);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        db.delete(TABLE_REVIEW, null, null);

        for (Review review : reviews) {
            ContentValues values = new ContentValues();
            values.put("id", review.getId());
            values.put("plate_id", review.getPlate_id());
            values.put("description", review.getDescription());
            values.put("value", review.getValue());
            db.insert(TABLE_REVIEW, null, values);
        }
    }


    public ArrayList<Plate> getFavorites() {
        ArrayList<Favorite> favorites = new ArrayList<>();

        Cursor cursor = db.query(TABLE_FAVORITES, TABLE_FAVORITES_FIELDS,
                null, null, null, null, "id");

        if (cursor.moveToFirst()) {
            do {
                Favorite favorite = new Favorite(cursor);

                favorites.add(favorite);

            } while (cursor.moveToNext());

            cursor.close();
        }

        ArrayList<Plate> plates = new ArrayList<>();

        for (Favorite favorite : favorites) {
            for (Plate plate : getPlates()) {
                if (plate.getId() == favorite.getPlate_id()) {
                    plates.add(plate);
                }
            }
        }

        return plates;
    }

    public void setFavorites(ArrayList<Favorite> favorites) {
        db.delete(TABLE_FAVORITES, null, null);

        for (Favorite favorite : favorites) {
            ContentValues values = new ContentValues();
            values.put("id", favorite.getId());
            values.put("plate_id", favorite.getPlate_id());
            db.insert(TABLE_FAVORITES, null, values);
        }
    }

    public boolean removeReviewDB(int reviewId) {
        return (this.db.delete(TABLE_REVIEW, reviewId + "= ?", new String[]{"" + reviewId}) == 1);

    }

    public boolean updateReviewDB(Review updatedReview) {
        ContentValues values = new ContentValues();

        values.put("plate_id", updatedReview.getPlate_id());
        values.put("description", updatedReview.getDescription());
        values.put("value", updatedReview.getValue());

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(updatedReview.getId())};

        int rowsAffected = this.db.update(TABLE_REVIEW, values, whereClause, whereArgs);

        return rowsAffected > 0;
    }


    public void addReviewDB(Review r) {
        ContentValues values = new ContentValues();

        values.put("id", r.getId());
        values.put("plate_id", r.getPlate_id());
        values.put("description", r.getDescription());
        values.put("value", r.getValue());

        this.db.insert(TABLE_REVIEW, null, values);
    }

    public ArrayList<HelpTicket> getTickets() {
        ArrayList<HelpTicket> tickets = new ArrayList<>();

        Cursor cursor = db.query(TABLE_HELP_TICKET, TABLE_HELP_TICKET_FIELDS,
                null, null, null, null, "id");

        if (cursor.moveToFirst()) {
            do {
                HelpTicket ticket = new HelpTicket(cursor);

                tickets.add(ticket);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return tickets;

    }


    public void SetTickets(ArrayList<HelpTicket> tickets) {
        db.delete(TABLE_HELP_TICKET, null, null);

        for (HelpTicket ticket : tickets) {
            ContentValues values = new ContentValues();
            values.put("id", ticket.getId());
            values.put("description", ticket.getDescription());
            db.insert(TABLE_HELP_TICKET, null, values);
        }
    }

    public void addTicketDB(HelpTicket ticket) {
        ContentValues values = new ContentValues();

        values.put("description", ticket.getDescription());

        this.db.insert(TABLE_HELP_TICKET, null, values);
    }

    public boolean removeTicketDB(int ticketId) {
        return (this.db.delete(TABLE_HELP_TICKET, ticketId + "= ?", new String[]{"" + ticketId}) == 1);

    }
}
