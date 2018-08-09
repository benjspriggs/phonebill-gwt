package edu.pdx.cs410J.bspriggs.client;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.ArrayList;
import java.util.Collection;

public class PhoneBill extends AbstractPhoneBill<PhoneCall> {
  private String customer;
  private Collection<PhoneCall> calls = new ArrayList<>();

  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public PhoneBill() {
    this("");
  }

  public PhoneBill(String customer) {
    this.customer = customer;
  }

  @Override
  public String getCustomer() {
    return customer;
  }

  @Override
  public void addPhoneCall(PhoneCall call) {
    this.calls.add(call);
  }

  @Override
  public Collection<PhoneCall> getPhoneCalls() {
    return this.calls;
  }
}
