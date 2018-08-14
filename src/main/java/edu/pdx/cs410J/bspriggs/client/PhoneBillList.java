package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static org.gwtbootstrap3.client.ui.constants.ColumnSize.MD_3;

class PhoneBillList extends VerticalPanel {
    private Heading customerLabel = new Heading(HeadingSize.H2, "No customer selected.");
    private Container callGrid = new Container();
    private PhoneBill bill;

    PhoneBillList() {
        add(customerLabel);
        add(callGrid);
    }

    boolean updateIfChanged(PhoneBill bill) {
        if (bill == null)
            return false;
        if (this.bill != null && this.bill.equals(bill)) {
            return false;
        }

        update(bill);
        return true;
    }

    void update(PhoneBill bill) {
        this.bill = bill;

        customerLabel.setText("Customer: " + bill.getCustomer());
        callGrid.clear();
        addHeader();

        addCallsToContainer(bill.getPhoneCalls());
    }

    boolean updateSomeIfChanged(PhoneBill bill, List<PhoneCall> some) {
        if (bill == null)
            return false;

        updateSome(bill, some);
        return true;
    }

    void updateSome(PhoneBill bill, List<PhoneCall> some) {
        this.bill = bill;

        customerLabel.setText("(partial) Customer: " + bill.getCustomer());
        callGrid.clear();
        addHeader();

        addCallsToContainer(some);
    }

    private void addCallsToContainer(Collection<PhoneCall> calls){
        Iterator<PhoneCall> cn = calls.stream()
                .sorted(Comparator.comparing(PhoneCall::getStartTime))
                .iterator();

        IntStream.range(1, calls.size() + 1).forEach(i -> {
            PhoneCall call = cn.next();
            Row row = new Row();
            row.add(columnFor(call.getCaller()));
            row.add(columnFor(call.getCallee()));
            row.add(columnFor(call.getStartTimeString()));
            row.add(columnFor(call.getEndTimeString()));

            callGrid.add(row);
        });
    }

    private static Widget columnFor(String s) {
        Column col = new Column(MD_3);
        Paragraph p = new Paragraph();
        p.setText(s);
        col.add(p);
        return col;
    }

    private void addHeader() {
        Row headerRow = new Row();
        headerRow.add(columnFor("Caller"));
        headerRow.add(columnFor("Callee"));
        headerRow.add(columnFor("Start Time"));
        headerRow.add(columnFor("End Time"));

        callGrid.add(headerRow);
    }
}
