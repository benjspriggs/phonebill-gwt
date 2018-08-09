package edu.pdx.cs410J.bspriggs;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import edu.pdx.cs410J.bspriggs.client.PhoneBill;
import edu.pdx.cs410J.bspriggs.client.PhoneBillService;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PhoneBillServiceSyncProxyIT extends HttpRequestHelper {

  private final int httpPort = Integer.getInteger("http.port", 8080);
  private String webAppUrl = "http://localhost:" + httpPort + "/phonebill";

  @Test
  public void gwtWebApplicationIsRunning() throws IOException {
    Response response = get(this.webAppUrl);
    assertEquals(200, response.getCode());
  }

  @Test
  public void canInvokePhoneBillServiceWithGwtSyncProxy() {
    String moduleName = "phonebill";
    SyncProxy.setBaseURL(this.webAppUrl + "/" + moduleName + "/");
    final String customer = "CS410J";

    PhoneBillService service = SyncProxy.createSync(PhoneBillService.class);
    PhoneBill bill = service.getPhoneBill(customer);
    assertThat(bill, equalTo(null));
  }

}
