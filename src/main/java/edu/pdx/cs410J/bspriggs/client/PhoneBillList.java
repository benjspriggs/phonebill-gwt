package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.IntStream;

class PhoneBillList extends VerticalPanel {
    private Label customerLabel = new Label("NO CUSTOMER SELECTED");
    private Grid callGrid = new Grid();
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

        callGrid.resize(bill.getPhoneCalls().size() + 1, 4);
        addHeader();

        Collection<PhoneCall> calls = bill.getPhoneCalls();
        Iterator<PhoneCall> cn = calls.stream()
                .sorted(Comparator.comparing(PhoneCall::getStartTime))
                .iterator();

        IntStream.range(1, calls.size() + 1).forEach(i -> {
            PhoneCall call = cn.next();
            callGrid.setText(i, 0, call.getCaller());
            callGrid.setText(i, 1, call.getCallee());
            callGrid.setText(i, 2, call.getStartTimeString());
            callGrid.setText(i, 3, call.getEndTimeString());
        });
    }

    private void addHeader() {
        callGrid.setText(0, 0, "Caller");
        callGrid.setText(0, 1, "Callee");
        callGrid.setText(0, 2, "Start Time");
        callGrid.setText(0, 3, "End Time");
    }
}
