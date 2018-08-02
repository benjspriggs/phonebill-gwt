package edu.pdx.cs410J.bspriggs.server;

import edu.pdx.cs410J.bspriggs.client.PhoneBill;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PhoneBillServiceImplTest {

  @Test
  public void serviceReturnsExpectedPhoneBill() {
    PhoneBillServiceImpl service = new PhoneBillServiceImpl();
    PhoneBill bill = service.getPhoneBill();
    assertThat(bill.getPhoneCalls().size(), equalTo(1));
  }
}
