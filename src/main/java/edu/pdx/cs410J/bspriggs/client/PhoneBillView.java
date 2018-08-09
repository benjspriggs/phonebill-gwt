package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class PhoneBillView extends VerticalPanel {
    private final PhoneBillServiceAsync phoneBillService;
    private List<String> bills = new ArrayList<>();
    private PhoneBillList list = new PhoneBillList();

    PhoneBillView() {
        phoneBillService = GWT.create(PhoneBillService.class);

        add(new Label("Available Phone Bills"));

        ListBox l = new ListBox();

        Timer t = new Timer() {
            @Override
            public void run() {
                refreshAvailablePhoneBills(l);
            }
        };
        t.scheduleRepeating(100);

        refreshAvailablePhoneBills(l);

        add(l);
        add(list);
    }

    private void refreshAvailablePhoneBills(ListBox l) {
        phoneBillService.getAvailablePhonebills(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                // TODO: probably should do something
            }

            @Override
            public void onSuccess(List<String> strings) {
                if (bills.size() == strings.size()) {
                    return;
                }

                bills = strings;
                l.clear();
                bills.stream().forEach(l::addItem);

                updateSelectedPhoneBill(l);
            }
        });
    }

    private void updateSelectedPhoneBill(ListBox l) {
        l.getSelectedValue();
        phoneBillService.getPhoneBill(l.getSelectedValue(), new AsyncCallback<PhoneBill>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(PhoneBill phoneBill) {
                list.update(phoneBill);
            }
        });
    }

    private class PhoneBillList extends VerticalPanel {
        private Label customerLabel = new Label("NO CUSTOMER SELECTED");
        private Grid callGrid = new Grid();

        PhoneBillList() {
            add(customerLabel);
            add(callGrid);
        }

        void update(PhoneBill bill) {
            customerLabel.setText("Customer: " + bill.getCustomer());

            callGrid.resize(bill.getPhoneCalls().size(), 4);
            Collection<PhoneCall> calls = bill.getPhoneCalls();
            Iterator<PhoneCall> cn = calls.iterator();

            IntStream.range(0, calls.size()).forEach(i -> {
                PhoneCall call = cn.next();
                callGrid.setText(i, 0, call.getCaller());
                callGrid.setText(i, 0, call.getCallee());
                callGrid.setText(i, 0, call.getStartTimeString());
                callGrid.setText(i, 0, call.getEndTimeString());
            });
        }
    }
}
