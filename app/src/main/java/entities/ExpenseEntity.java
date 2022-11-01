package entities;

public class ExpenseEntity {
  private int tripId;
  private int expenseId;
  private String expenseType;
  private String expenseAmount;
  private String expenseTime;
  private String expenseComment;

  public int getTripId() {
    return tripId;
  }

  public void setTripId(int tripId) {
    this.tripId = tripId;
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

  public ExpenseEntity(int tripId, int expenseId, String expenseType, String expenseAmount, String expenseTime, String expenseComment) {
    this.tripId = tripId;
    this.expenseId = expenseId;
    this.expenseType = expenseType;
    this.expenseAmount = expenseAmount;
    this.expenseTime = expenseTime;
    this.expenseComment = expenseComment;
  }
}
