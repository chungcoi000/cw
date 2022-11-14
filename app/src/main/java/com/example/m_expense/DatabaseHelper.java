package com.example.m_expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import entities.ExpenseEntity;
import entities.TripEntity;
import entities.TripExpenseEntity;

public class DatabaseHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "trips";
  private static final String TABLE_EXPENSE = "expenses";
  private static final String TABLE_TRIP = "trips";

  public static final String TRIP_ID = "trip_id";
  public static final String TRIP_NAME = "name";
  public static final String TRIP_DESTINATION = "destination";
  public static final String TRIP_DATE = "date";
  public static final String TRIP_DURATION = "duration";
  public static final String TRIP_CONTACT = "contact";
  public static final String TRIP_RISK = "risk";
  public static final String TRIP_DESCRIPTION = "description";

  private static final String EXPENSE_ID = "expense_id";
  private static final String EXPENSE_TYPE = "type";
  private static final String EXPENSE_AMOUNT = "amount";
  private static final String EXPENSE_TIME = "time";
  private static final String EXPENSE_COMMENT = "comment";

  private SQLiteDatabase database;

  private static final String DATABASE_CREATE = String.format(
    "CREATE TABLE %s (" +
      "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
      "   %s TEXT, " +
      "   %s TEXT, " +
      "   %s TEXT, " +
      "   %s TEXT, " +
      "   %s TEXT, " +
      "   %s TEXT, " +
      "   %s TEXT)",
    TABLE_TRIP, TRIP_ID, TRIP_NAME, TRIP_DATE, TRIP_DURATION, TRIP_DESTINATION, TRIP_CONTACT, TRIP_RISK, TRIP_DESCRIPTION);

  private static final String EXPENSE_TABLE_CREATE = String.format(
    "CREATE TABLE %s (" +
      "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
      "   %s INTEGER, " +
      "   %s TEXT, " +
      "   %s TEXT, " +
      "   %s TEXT, " +
      "   %s TEXT)",
    TABLE_EXPENSE, EXPENSE_ID, TRIP_ID, EXPENSE_TYPE, EXPENSE_AMOUNT, EXPENSE_TIME, EXPENSE_COMMENT);

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, 4);
    database = getWritableDatabase();
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(DATABASE_CREATE);
    db.execSQL(EXPENSE_TABLE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);

    Log.v(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " +
      newVersion + " - old data lost");
    onCreate(db);
  }

  public long insertTrips(String name, String destination, String date, String duration, String contact, String risk, String description) {
    ContentValues rowValues = new ContentValues();

    rowValues.put(TRIP_NAME, name);
    rowValues.put(TRIP_DESTINATION, destination);
    rowValues.put(TRIP_DATE, date);
    rowValues.put(TRIP_DURATION, duration);
    rowValues.put(TRIP_CONTACT, contact);
    rowValues.put(TRIP_RISK, risk);
    rowValues.put(TRIP_DESCRIPTION, description);

    return database.insertOrThrow(TABLE_TRIP, null, rowValues);
  }

  public long insertExpenses(int tripId, String type, String amount, String time, String comment) {
    ContentValues rowValues = new ContentValues();

    rowValues.put(TRIP_ID, tripId);
    rowValues.put(EXPENSE_TYPE, type);
    rowValues.put(EXPENSE_AMOUNT, amount);
    rowValues.put(EXPENSE_TIME, time);
    rowValues.put(EXPENSE_COMMENT, comment);

    return database.insertOrThrow(TABLE_EXPENSE, null, rowValues);
  }

  public long updateTrip(int tripId, String name, String destination, String date, String duration, String contact, String risk, String description) {
    ContentValues rowValues = new ContentValues();

    rowValues.put(TRIP_NAME, name);
    rowValues.put(TRIP_DESTINATION, destination);
    rowValues.put(TRIP_DATE, date);
    rowValues.put(TRIP_DURATION, duration);
    rowValues.put(TRIP_CONTACT, contact);
    rowValues.put(TRIP_RISK, risk);
    rowValues.put(TRIP_DESCRIPTION, description);

    return database.update(TABLE_TRIP, rowValues, TRIP_ID + " = " + tripId, null);
  }

  public void deleteTrip(TripEntity tripEntity) {
    SQLiteDatabase db = this.getWritableDatabase();
    String queryString = "DELETE FROM " + TABLE_TRIP + " WHERE " + TRIP_ID + " = " + tripEntity.getTrip_id();
    String queryString2 = "DELETE FROM " + TABLE_EXPENSE + " WHERE " + TRIP_ID + " = " + tripEntity.getTrip_id();
    db.execSQL(queryString);
    db.execSQL(queryString2);
  }

  public void deleteExpense(TripExpenseEntity tripExpenseEntity) {
    SQLiteDatabase db = this.getWritableDatabase();
    String queryString = "DELETE FROM " + TABLE_EXPENSE + " WHERE " + EXPENSE_ID + " = " + tripExpenseEntity.getExpenseId();
    db.execSQL(queryString);
  }

  public List<TripExpenseEntity> getExpenses(int tripId) {
    String MY_QUERY = "SELECT b.expense_id, b.trip_id, a.name, b.amount, b.time, b.type, b.comment FROM " + TABLE_TRIP + " a INNER JOIN " +
      TABLE_EXPENSE + " b ON a.trip_id=b.trip_id WHERE a.trip_id=?";
    Cursor cursor = database.rawQuery(MY_QUERY, new String[]{String.valueOf(tripId)});

    List<TripExpenseEntity> results = new ArrayList<TripExpenseEntity>();

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      int expense_id = cursor.getInt(0);
      int trip_id = cursor.getInt(1);
      String trip_name = cursor.getString(2);
      String expense_amount = cursor.getString(3);
      String expense_time = cursor.getString(4);
      String expense_type = cursor.getString(5);
      String expense_comment = cursor.getString(6);

      TripExpenseEntity tripExpenseEntity = new TripExpenseEntity();
      tripExpenseEntity.setExpenseId(expense_id);
      tripExpenseEntity.setTripId(trip_id);
      tripExpenseEntity.setTripName(trip_name);
      tripExpenseEntity.setExpenseAmount(expense_amount);
      tripExpenseEntity.setExpenseTime(expense_time);
      tripExpenseEntity.setExpenseType(expense_type);
      tripExpenseEntity.setExpenseComment(expense_comment);
      results.add(tripExpenseEntity);

      cursor.moveToNext();
    }
    return results;
  }

  public List<TripEntity> getTrips() {
    Cursor cursor = database.query(TABLE_TRIP, new String[]{TRIP_ID, TRIP_NAME, TRIP_DATE, TRIP_DURATION, TRIP_DESTINATION, TRIP_CONTACT ,TRIP_RISK, TRIP_DESCRIPTION},
      null, null, null, null, TRIP_NAME);

    List<TripEntity> results = new ArrayList<TripEntity>();

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      int id = cursor.getInt(0);
      String name = cursor.getString(1);
      String date = cursor.getString(2);
      String duration = cursor.getString(3);
      String destination = cursor.getString(4);
      String contact = cursor.getString(5);
      String risk = cursor.getString(6);
      String description = cursor.getString(7);

      TripEntity tripEntity = new TripEntity(id, name, destination, date, duration, contact, risk, description);
      results.add(tripEntity);

      cursor.moveToNext();
    }
    return results;
  }

}
