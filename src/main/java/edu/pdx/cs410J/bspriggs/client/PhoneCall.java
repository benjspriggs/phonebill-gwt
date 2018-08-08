package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.ParserException;

import java.util.Date;

public class PhoneCall extends AbstractPhoneCall {
    private final String caller;
    private final String callee;
    private final Date startTime;
    private final Date endTime;
    private static final String phoneNumberPattern = "\\d{3}-\\d{3}-\\d{4}";
    private static final String dateFormat = "mm/dd/yyyy hh:mm";

    public PhoneCall() throws ParserException {
       this("","","","");
    }

    public PhoneCall(String caller, String callee, String startDateAndTime, String endDateAndTime) throws ParserException {
        this.caller = validatePhoneNumber(caller);
        this.callee = validatePhoneNumber(callee);

        this.startTime = DateTimeFormat.getFormat(dateFormat).parse(startDateAndTime);
        this.endTime = DateTimeFormat.getFormat(dateFormat).parse(endDateAndTime);

        if (startTime.compareTo(endTime) > 0) {
            throw new ParserException("Start time for the call was after end time.");
        }
    }

    public static String formatDate(Date start) {
        return DateTimeFormat.getFormat(dateFormat).format(start);
    }

  private String validatePhoneNumber(String in) throws ParserException {
        if (!in.matches(phoneNumberPattern)) {
            throw new ParserException("Invalid phone number " + in);
        }

        return in;
    }

    @Override
    public String getCaller() {
        return this.caller;
    }

    @Override
    public String getCallee() {
        return this.callee;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public String getStartTimeString() {
        return formatDate(startTime);
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public String getEndTimeString() {
        return formatDate(endTime);
    }
}
