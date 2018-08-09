package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Date;
import java.util.List;

/**
 * A GWT remote service that returns a dummy Phone Bill
 */
@RemoteServiceRelativePath("phoneBill")
public interface PhoneBillService extends RemoteService {

  /**
   * Returns a phone bill by name.
   */
  public PhoneBill getPhoneBill(String customer);

  /**
   * Creates a {@link PhoneBill} for a given customer.
   * @param customer
   */
  public void createPhoneBill(String customer);

  /**
   * Adds a phone call to a bill.
   * @param customer
   * @param call
   */
  public void addPhoneCallToBill(String customer, PhoneCall call);

  /**
   * Searches for calls within a date range.
   * @param customer
   * @param start
   * @param end
   * @return
   */
  public List<PhoneCall> searchForCalls(String customer, Date start, Date end);

  /***
   * Gets all the available phone bills on the server.
   * @return
   */
  public List<String> getAvailablePhonebills();

  /**
   * Always throws an undeclared exception so that we can see GWT handles it.
   */
  void throwUndeclaredException();

  /**
   * Always throws a declared exception so that we can see GWT handles it.
   */
  void throwDeclaredException() throws IllegalStateException;

}
