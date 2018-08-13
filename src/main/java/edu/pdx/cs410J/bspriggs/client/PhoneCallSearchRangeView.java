package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import static edu.pdx.cs410J.bspriggs.client.PhoneCall.dateFormat;

class PhoneCallSearchRangeView extends HorizontalPanel {
    PhoneCallSearchRangeView() {
        DateTimePicker dateTimeBox = new DateTimePicker();
        dateTimeBox.setFormat(dateFormat);
        dateTimeBox.setAutoClose(true);
        dateTimeBox.reload();

        add(new PhoneBillList());
        add(new PhoneCallSearchRangeView());
    }

}
