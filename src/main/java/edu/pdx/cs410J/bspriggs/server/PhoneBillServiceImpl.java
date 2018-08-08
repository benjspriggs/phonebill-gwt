package edu.pdx.cs410J.bspriggs.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.bspriggs.client.PhoneBill;
import edu.pdx.cs410J.bspriggs.client.PhoneBillService;
import edu.pdx.cs410J.bspriggs.client.PhoneCall;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The server-side implementation of the Phone Bill service
 */
public class PhoneBillServiceImpl extends RemoteServiceServlet implements PhoneBillService
{
  private Map<String, PhoneBill> calls = new HashMap<>();

  @Override
  public PhoneBill getPhoneBill(String customer) {
    return calls.get(customer);
  }

  @Override
  public void createPhoneBill(String customer) {
    calls.put(customer, new PhoneBill(customer));
  }

  @Override
  public void addPhoneCallToBill(String customer, PhoneCall call) {
    PhoneBill b = calls.get(customer);
    b.addPhoneCall(call);
    calls.put(b.getCustomer(), b);
  }

  @Override
  public List<PhoneCall> searchForCalls(String customer, Date start, Date end) {
    PhoneBill bill = calls.get(customer);
    Collection<PhoneCall> calls = bill.getPhoneCalls();
    return calls
            .stream()
            .map(PhoneCall.class::cast)
            .filter(call -> call.getStartTime().compareTo(start) >= 0)
            .filter(call -> call.getEndTime().compareTo(end) <= 0)
            .collect(Collectors.toList());
  }


  @Override
  public void throwUndeclaredException() {
    throw new IllegalStateException("Expected undeclared exception");
  }

  @Override
  public void throwDeclaredException() throws IllegalStateException {
    throw new IllegalStateException("Expected declared exception");
  }

  /**
   * Log unhandled exceptions to standard error
   *
   * @param unhandled
   *        The exception that wasn't handled
   */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
