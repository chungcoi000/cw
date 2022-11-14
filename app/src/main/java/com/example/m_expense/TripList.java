package com.example.m_expense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.TripAdapter;
import entities.TripEntity;

public class TripList extends AppCompatActivity {
  ArrayList<TripEntity> tripEntities;
  ListView tripList;
  TripAdapter tripAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trip_detail);

    DatabaseHelper dbHelper = new DatabaseHelper(this);
    tripEntities = (ArrayList<TripEntity>) dbHelper.getTrips();
    tripAdapter = new TripAdapter(this, R.layout.list_item, tripEntities);

    tripList = findViewById(R.id.tripListView);
    tripList.setAdapter(tripAdapter);

    tripList.setOnItemClickListener((adapterView, view1, i, l) -> {
      TripEntity selectedTripEntity = tripEntities.get(i);
      openTripDetail(selectedTripEntity);
    });

    tripList.setOnItemLongClickListener((adapterView, view, position, l) -> {
      TripEntity selectedTripEntity = tripEntities.get(position);

      new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_delete)
        .setTitle("Are you sure?")
        .setMessage("Do you want to delete this trip?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dbHelper.deleteTrip(selectedTripEntity);
            tripEntities.remove(position);
            tripAdapter.notifyDataSetChanged();
            Toast.makeText(TripList.this, "Deleted trip successful!", Toast.LENGTH_SHORT).show();
          }
        })
        .setNegativeButton("No", null)
        .show();
      return true;
    });

  }

  private void openTripDetail(TripEntity selectedTripEntity) {
    Intent intent = new Intent(this, TripDetail.class);
    intent.putExtra("trip_id", selectedTripEntity.getTrip_id());
    intent.putExtra("name", selectedTripEntity.getName());
    intent.putExtra("destination", selectedTripEntity.getDestination());
    intent.putExtra("date", selectedTripEntity.getDate());
    intent.putExtra("duration", selectedTripEntity.getDuration());
    intent.putExtra("contact", selectedTripEntity.getContact());
    intent.putExtra("risk", selectedTripEntity.getRisk());
    intent.putExtra("description", selectedTripEntity.getDescription());
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(@NonNull Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu, menu);

    MenuItem searchItem = menu.findItem(R.id.item_search);
    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        ArrayList<TripEntity> tripsList = new ArrayList<>();
        for (TripEntity trip : tripEntities) {
          if (trip.getName().toLowerCase().contains(newText.toLowerCase())) {
            tripsList.add(trip);
          }
        }

        tripAdapter = new TripAdapter(TripList.this, R.layout.list_item, tripsList);
        tripList.setAdapter(tripAdapter);
        return true;
      }
    });
    return super.onCreateOptionsMenu(menu);
  }
}