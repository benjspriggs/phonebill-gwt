package edu.pdx.cs410J.bspriggs.client;

import com.em.validation.client.constraints.NotEmpty;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.ParserException;

import javax.validation.constraints.Pattern;
import java.util.Date;

public class PhoneCall extends AbstractPhoneCall {
    @NotEmpty
    @Pattern(regexp=phoneNumberPattern)
    private final String caller;

    @NotEmpty
    @Pattern(regexp=phoneNumberPattern)
    private final String callee;

    @NotEmpty
    private final String startTime;

    @NotEmpty
    private final String endTime;

    private static final String phoneNumberPattern = "\\d{3}-\\d{3}-\\d{4}";
    private static final String dateFormat = "mm/dd/yyyy hh:mm";

    public PhoneCall() throws ParserException {
       this("","","","");
    }

    public PhoneCall(String caller, String callee, String startDateAndTime, String endDateAndTime) {
        this.caller = caller;
        this.callee = callee;
        this.startTime = startDateAndTime;
        this.endTime = endDateAndTime;

    }

    private Date parseDate(String startDateAndTime) {
        return new DateTimeFormat(dateFormat, new DefaultDateTimeFormatInfo()){}.parse(startDateAndTime);
    }

    public static String formatDate(Date start) {
        return new DateTimeFormat(dateFormat, new DefaultDateTimeFormatInfo()){}.format(start);
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
        return parseDate(startTime);
    }

    @Override
    public String getStartTimeString() {
        return startTime;
    }

    @Override
    public Date getEndTime() {
        return parseDate(endTime);
    }

    @Override
    public String getEndTimeString() {
        return endTime;
    }
}
