package entities;

import androidx.annotation.NonNull;

public class TripExpenseEntity {
  public int getTripId() {
    return tripId;
  }

  public void setTripId(int tripId) {
    this.tripId = tripId;
  }

  public String getTripName() {
    return tripName;
  }

  public void setTripName(String tripName) {
    this.tripName = tripName;
  }

  public String getTripDestination() {
    return tripDestination;
  }

  public void setTripDestination(String tripDestination) {
    this.tripDestination = tripDestination;
  }

  public String getTripDate() {
    return tripDate;
  }

  public void setTripDate(String tripDate) {
    this.tripDate = tripDate;
  }

  public String getTripDuration() {
    return tripDuration;
  }

  public void setTripDuration(String tripDuration) {
    this.tripDuration = tripDuration;
  }

  public String getTripDescription() {
    return TripDescription;
  }

  public void setTripDescription(String tripDescription) {
    TripDescription = tripDescription;
  }

  public String getTripRisk() {
    return TripRisk;
  }

  public void setTripRisk(String tripRisk) {
    TripRisk = tripRisk;
  }

  public int getExpenseId() {
    return expenseId;
  }

  public void setExpenseId(int expenseId) {
    this.expenseId = expenseId;
  }

  public String getExpenseType() {
    return expenseType;
  }

  public void setExpenseType(String expenseType) {
    this.expenseType = expenseType;
  }

  public String getExpenseAmount() {
    return expenseAmount;
  }

  public void setExpenseAmount(String expenseAmount) {
    this.expenseAmount = expenseAmount;
  }

  public String getExpenseTime() {
    return expenseTime;
  }

  public void setExpenseTime(String expenseTime) {
    this.expenseTime = expenseTime;
  }

  public String getExpenseComment() {
    return expenseComment;
  }

  public void setExpenseComment(String expenseComment) {
    this.expenseComment = expenseComment;
  }

  @NonNull
  @Override
  public String toString() {
    return expenseType + "-" + expenseAmount + "-" + expenseTime;
  }

  private int tripId;
  protected String tripName;
  protected String tripDestination;
  protected String tripDate;
  protected String tripDuration;
  protected String TripDescription;
  protected String TripRisk;
  private int expenseId;
  private String expenseType;
  private String expenseAmount;
  private String expenseTime;
  private String expenseComment;

}
