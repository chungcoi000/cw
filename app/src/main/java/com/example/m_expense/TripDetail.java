package com.example.m_expense;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import adapter.TripExpenseAdapter;
import entities.TripExpenseEntity;

public class TripDetail extends AppCompatActivity {
  final Context context = this;
  TextView textView;
  EditText inputDestination, inputDate, inputRisk, inputDescription, inputDuration, inputContact;
  ArrayList<TripExpenseEntity> tripExpenseEntities;
  TripExpenseAdapter tripExpenseAdapter;
  ListView expenseList;
  Button viewExpenseBtn, addBtn, updateBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trip_detail2);

    Intent intent = getIntent();
    int trip_id = intent.getIntExtra("trip_id", 0);
    String name = intent.getStringExtra("name");
    String destination = intent.getStringExtra("destination");
    String date = intent.getStringExtra("date");
    String duration = intent.getStringExtra("duration");
    String contact = intent.getStringExtra("contact");
    String risk = intent.getStringExtra("risk");
    String description = intent.getStringExtra("description");

    String title = name + " Trip detail";

    Toast.makeText(this, "ok" + trip_id, Toast.LENGTH_SHORT).show();

    DatabaseHelper dbHelper = new DatabaseHelper(this);

    textView = findViewById(R.id.viewTripName);
    inputDestination = findViewById(R.id.inputDestination);
    inputDate = findViewById(R.id.inputDate);
    inputDuration = findViewById(R.id.inputDuration);
    inputContact = findViewById(R.id.inputContact);
    inputRisk = findViewById(R.id.inputRisk);
    inputDescription = findViewById(R.id.inputDescription);

    inputDate.setOnFocusChangeListener((view, b) -> {
      if (b) {
        TripDetail.MyDatePicker dlg = new TripDetail.MyDatePicker();
        dlg.setExpense_time(inputDate);
        dlg.show(getSupportFragmentManager(), "dateTimePicker");
      }
    });

    textView.setText(title);
    inputDestination.setText(destination);
    inputDate.setText(date);
    inputDuration.setText(duration);
    inputContact.setText(contact);
    inputRisk.setText(risk);
    inputDescription.setText(description);

    tripExpenseEntities = (ArrayList<TripExpenseEntity>) dbHelper.getExpenses(trip_id);
    tripExpenseAdapter = new TripExpenseAdapter(this, R.layout.list_expense_item, tripExpenseEntities);

    expenseList = findViewById(R.id.expenseListView);

    //update trip
    updateBtn = findViewById(R.id.updateBtn);
    updateBtn.setOnClickListener(view -> {
      String trip_destination = inputDestination.getText().toString();
      String trip_date = inputDate.getText().toString();
      String trip_contact = inputContact.getText().toString();
      String trip_duration = inputDuration.getText().toString();
      String trip_description = inputDescription.getText().toString();

      if (TextUtils.isEmpty(trip_destination)) {
        inputDestination.setError("Trip destination is required!");
        return;
      }
      if (TextUtils.isEmpty(trip_date)) {
        inputDate.setError("Trip date is required!");
        return;
      }

      if (TextUtils.isEmpty(trip_contact)) {
        inputContact.setError("Trip contract is required!");
        return;
      }

      if (TextUtils.isEmpty(trip_duration)) {
        inputDuration.setError("Trip duration is required!");
        return;
      }

      dbHelper.updateTrip(trip_id, name, trip_destination, trip_date, trip_duration, trip_contact, risk, trip_description);
      Toast.makeText(this, "Update trip successfully!", Toast.LENGTH_LONG).show();
    });

    // view list of expenses
    viewExpenseBtn = findViewById(R.id.viewExpenseBtn);
    viewExpenseBtn.setOnClickListener(view -> {
      if (tripExpenseEntities.isEmpty()) {
        Toast.makeText(this, "There is no expense in this trip!", Toast.LENGTH_LONG).show();
        return;
      }
      expenseList.setAdapter(tripExpenseAdapter);
    });

    // view expense's detail
    expenseList.setOnItemClickListener((adapterView, view1, i, l) -> {
      TripExpenseEntity selectedTripExpenseEntity = tripExpenseEntities.get(i);
      final Dialog dialog = new Dialog(TripDetail.this);
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.setCancelable(true);
      dialog.setContentView(R.layout.expense_detail_dialog);

      final EditText typeInput = dialog.findViewById(R.id.inputType);
      final EditText amountInput = dialog.findViewById(R.id.inputAmount);
      final EditText timeInput = dialog.findViewById(R.id.inputTime);
      final EditText commentInput = dialog.findViewById(R.id.inputComment);

      typeInput.setEnabled(false);
      amountInput.setEnabled(false);
      timeInput.setEnabled(false);
      commentInput.setEnabled(false);

      typeInput.setText(selectedTripExpenseEntity.getExpenseType());
      amountInput.setText(selectedTripExpenseEntity.getExpenseAmount());
      timeInput.setText(selectedTripExpenseEntity.getExpenseTime());
      commentInput.setText(selectedTripExpenseEntity.getExpenseComment());

      dialog.show();
    });

    // delete expense
    expenseList.setOnItemLongClickListener((adapterView, view, position, l) -> {
      TripExpenseEntity selectedTripExpenseEntity = tripExpenseEntities.get(position);

      new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_delete)
        .setTitle("Are you sure?")
        .setMessage("Do you want to delete this expense?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dbHelper.deleteExpense(selectedTripExpenseEntity);
            tripExpenseEntities.remove(position);
            tripExpenseAdapter.notifyDataSetChanged();
            Toast.makeText(context, "Deleted expense successful! ", Toast.LENGTH_SHORT).show();
          }
        })
        .setNegativeButton("No", null)
        .show();
      return true;
    });

    //add expense
    addBtn = findViewById(R.id.addBtn);
    addBtn.setOnClickListener(view -> {
      LayoutInflater layoutInflater = LayoutInflater.from(context);
      View dialog = layoutInflater.inflate(R.layout.dialog, null);

      final Spinner typeInput = dialog.findViewById(R.id.inputType);

      String[] types = new String[]{
        "Food",
        "Travel",
        "Hotel",
        "Other"
      };
      List<String> typesList = new ArrayList<>
        (Arrays.asList(types));
      ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typesList);

      spinnerArrayAdapter.setDropDownViewResource(
        android.R.layout.simple_spinner_dropdown_item
      );
      typeInput.setAdapter(spinnerArrayAdapter);

      final EditText amountInput = dialog.findViewById(R.id.inputAmount);
      final EditText timeInput = dialog.findViewById(R.id.inputTime);
      final EditText commentInput = dialog.findViewById(R.id.inputComment);

      timeInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
          if (b) {
            TripDetail.MyDatePicker dlg = new TripDetail.MyDatePicker();
            dlg.setExpense_time(timeInput);
            dlg.show(getSupportFragmentManager(), "dateTimePicker");
          }
        }
      });

      final AlertDialog alertDialog = new AlertDialog.Builder(context)
        .setView(dialog)
        .setTitle("Add Expense")
        .setCancelable(false)
        .setPositiveButton("Save", null) //Set to null. We override the onclick
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
          }
        })
        .create();

      alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
          Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
          b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String type = typeInput.getSelectedItem().toString();
              String amount = amountInput.getText().toString();
              String time = timeInput.getText().toString();
              String comment = commentInput.getText().toString();

              // TODO Do something
              if (TextUtils.isEmpty(time)) {
                timeInput.setError("Expense time is required!");
                return;
              }
              if (TextUtils.isEmpty(amount)) {
                amountInput.setError("Expense amount is required!");
                return;
              }
              //Dismiss once everything is OK.
              dbHelper.insertExpenses(trip_id, type, amount, time, comment);
              TripExpenseEntity tripExpenseEntity = new TripExpenseEntity();
              tripExpenseEntity.setExpenseType(type);
              tripExpenseEntity.setExpenseAmount(amount);
              tripExpenseEntity.setExpenseTime(time);
              tripExpenseEntity.setExpenseComment(comment);
              tripExpenseEntities.add(tripExpenseEntity);
              tripExpenseAdapter.notifyDataSetChanged();
              Toast.makeText(getApplicationContext(), "Expense was saved!",
                Toast.LENGTH_SHORT).show();
              alertDialog.dismiss();
            }
          });
        }
      });
      alertDialog.show();
    });
  }

  //Custom DatePicker
  public static class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText expense_time;

    public EditText getExpense_time() {
      return expense_time;
    }

    public void setExpense_time(EditText expense_time) {
      this.expense_time = expense_time;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);
      // Create a new instance of DatePickerDialog and return it
      return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
      int month = i1 + 1;
      String dateValue = i2 + "/" + month + "/" + i;
      expense_time.setText(dateValue);
    }
  }
}