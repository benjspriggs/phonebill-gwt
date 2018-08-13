package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import edu.pdx.cs410J.AbstractPhoneCall;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class PhoneCall extends AbstractPhoneCall {
    @NotNull
    @Pattern(regexp=phoneNumberPattern)
    private String caller;

    @NotNull
    @Pattern(regexp=phoneNumberPattern)
    private String callee;

    private Date startDate;
    private Date endDate;

    public static final String phoneNumberPattern = "\\d{3}-\\d{3}-\\d{4}";
    public static final String dateFormat = "mm/dd/yyyy hh:mm a";

    public PhoneCall() {
        this("","",null, null);
    }

    public PhoneCall(String caller, String callee, Date startDate, Date endDate) {
        this.caller = caller;
        this.callee = callee;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Date parseDate(String startDateAndTime) {
        return new DateTimeFormat(dateFormat, new DefaultDateTimeFormatInfo()){}.parseStrict(startDateAndTime);
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
        return startDate;
    }

    @Override
    public String getStartTimeString() {
        return formatDate(startDate);
    }

    @Override
    public Date getEndTime() {
        return endDate;
    }

    @Override
    public String getEndTimeString() {
        return formatDate(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof PhoneCall)) {
            return false;
        }

        PhoneCall comparing = (PhoneCall) o;

        return comparing.getCaller().contentEquals(getCaller())
                && comparing.getCallee().contentEquals(getCallee())
                && comparing.getStartTimeString().contentEquals(getStartTimeString())
                && comparing.getEndTimeString().contentEquals(getEndTimeString());
    }
}
