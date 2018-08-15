package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.junit.client.GWTTestCase;
import org.junit.Test;

/**
 * An integration test for the PhoneBill GWT UI.  Remember that GWTTestCase is JUnit 3 style.
 * So, test methods names must begin with "test".
 * And since this test code is compiled into JavaScript, you can't use hamcrest matchers.  :(
 */
public class PhoneBillGwtIT extends GWTTestCase {
  @Override
  public String getModuleName() {
    return "edu.pdx.cs410J.bspriggs.PhoneBillIntegrationTests";
  }

  @Test
  public void testNothing() {
    //TODO: remove
  }
}
