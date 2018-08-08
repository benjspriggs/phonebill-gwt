package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
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

        this.startTime = parseDate(startDateAndTime);
        this.endTime = parseDate(endDateAndTime);

        if (startTime.compareTo(endTime) > 0) {
            throw new ParserException("Start time for the call was after end time.");
        }
    }

    private Date parseDate(String startDateAndTime) {
        return new DateTimeFormat(dateFormat, new DefaultDateTimeFormatInfo()){}.parse(startDateAndTime);
    }

    public static String formatDate(Date start) {
        return new DateTimeFormat(dateFormat, new DefaultDateTimeFormatInfo()){}.format(start);
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
