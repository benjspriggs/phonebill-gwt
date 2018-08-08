package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;
import java.util.List;

/**
 * The client-side interface to the phone bill service
 */
public interface PhoneBillServiceAsync {

  /**
   * Return a customer's phone bill.
   */
  void getPhoneBill(String customer, AsyncCallback<PhoneBill> async);

  /**
   * Always throws an exception so that we can see how to handle uncaught
   * exceptions in GWT.
   */
  void throwUndeclaredException(AsyncCallback<Void> async);

  /**
   * Always throws a declared exception so that we can see GWT handles it.
   */
  void throwDeclaredException(AsyncCallback<Void> async);

  /**
   * Creates a {@link PhoneBill} for a given customer.
   *
   * @param customer
   */
  void createPhoneBill(String customer, AsyncCallback<Void> async);

  /**
   * Adds a phone call to a bill.
   *
   * @param customer
   * @param call
   */
  void addPhoneCallToBill(String customer, PhoneCall call, AsyncCallback<Void> async);

  /**
   * Searches for calls within a date range.
   *
   * @param customer
   * @param start
   * @param end
   * @return
   */
  void searchForCalls(String customer, Date start, Date end, AsyncCallback<List<PhoneCall>> async);
}
