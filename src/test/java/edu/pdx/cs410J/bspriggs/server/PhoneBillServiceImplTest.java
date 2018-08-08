package edu.pdx.cs410J.bspriggs.server;

import com.google.gwt.junit.client.GWTTestCase;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.bspriggs.client.PhoneBill;
import edu.pdx.cs410J.bspriggs.client.PhoneCall;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ibm.icu.impl.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PhoneBillServiceImplTest {

  @Test
  public void testReturnsExpectedPhoneBill() {
    PhoneBillServiceImpl service = new PhoneBillServiceImpl();
    PhoneBill bill = service.getPhoneBill("anything");
    assertThat(bill, equalTo(null));
  }

  private PhoneBill bill;
  private Date startDate;
  private Date endDate;
  private List<AbstractPhoneCall> callsInSearchRange;

  @Before
  public void setUpStuff() {
    this.bill = getPopulatedPhoneBill();

    var pair = this.bill.getPhoneCalls().stream()
            .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
              Collections.shuffle(collected);
              return collected.stream();
            }))
            .limit(2)
            .collect(Collectors.toList());

    this.startDate = pair.get(0).getStartTime();
    this.endDate = pair.get(1).getEndTime();

    this.callsInSearchRange = this.bill.getPhoneCalls()
            .stream()
            .filter(call -> call.getStartTime().compareTo(startDate) >= 0
                    && call.getEndTime().compareTo(endDate) <= 0)
            .collect(Collectors.toList());
  }

  private static Random r = new Random();

  static Date generateDate() {
    return new Date(r.nextInt());
  }

  static String generatePhoneNumber() {
    return String.format("%03d-%03d-%04d",
            1 + r.nextInt(998),
            1 + r.nextInt(998),
            1 + r.nextInt(9998)
    );
  }

  static PhoneCall generatePhoneCall() throws ParserException {
    var start = generateDate();
    var end = generateDateAfter(start);
    return new PhoneCall(generatePhoneNumber(), generatePhoneNumber(),
            PhoneCall.formatDate(start),
            PhoneCall.formatDate(end));
  }

  static Date generateDateAfter(Date date) {
    return new Date(date.getTime() + TimeUnit.DAYS.toMillis(1) + TimeUnit.SECONDS.toMillis(r.nextInt(100)));
  }


  public static PhoneBill getPopulatedPhoneBill() {
    var bill = new PhoneBill(String.valueOf(r.nextInt()));

    try {
      for (var i = 0; i < 10; i++) {
        bill.addPhoneCall(generatePhoneCall());
      }
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

    return bill;
  }

}
