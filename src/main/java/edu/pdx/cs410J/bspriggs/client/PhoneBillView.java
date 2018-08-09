package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.*;
import java.util.stream.IntStream;

public class PhoneBillView extends VerticalPanel {
    private final PhoneBillServiceAsync phoneBillService;

    /**
     * All of the possible customer names;
     */
    private List<String> bills = new ArrayList<>();
    /**
     * The view of the currently selected {@link PhoneBill}.
     */
    private PhoneBillList list = new PhoneBillList();

    PhoneBillView() {
        phoneBillService = GWT.create(PhoneBillService.class);

        add(new Label("Available Phone Bills"));

        ListBox l = new ListBox();
        l.addChangeHandler(change -> {
            refreshAvailablePhoneBills(l);
        });

        refreshAvailablePhoneBills(l);

        Timer t = new Timer() {
            @Override
            public void run() {
                refreshAvailablePhoneBills(l);
            }
        };
        t.scheduleRepeating(100);

        add(l);
        add(list);
    }

    /**
     * Refreshes the available phone bills.
     * @param l
     */
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
                bills.forEach(l::addItem);
            }
        });

        updateSelectedPhoneBill(l);
    }

    /**
     * Updates the selected phone bill from {@link PhoneBillService} based on what's selected in the ListBox.
     * @param l
     */
    private void updateSelectedPhoneBill(ListBox l) {
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
        private DialogBox newCallDialog = new DialogBox();
        private VerticalPanel dialogPanel = new VerticalPanel();
        private PhoneBill bill;

        PhoneBillList() {
            add(customerLabel);
            add(callGrid);

            Button addCallButton = new Button("Add new Phone Call");

            newCallDialog.add(dialogPanel);
            newCallDialog.setGlassEnabled(true);
            newCallDialog.setAnimationEnabled(true);
            addFormAndButtons(null);

            addCallButton.addClickHandler(event -> {
                newCallDialog.show();
            });

            add(addCallButton);
        }

        private void addFormAndButtons(String customer) {
            dialogPanel.clear();
            dialogPanel.add(new PhoneCallForm(customer));

            Button closeButton = new Button("Close");
            closeButton.addClickHandler(event -> {
                newCallDialog.hide(true);
            });
            dialogPanel.add(closeButton);
        }

        void update(PhoneBill bill) {
            if (bill.equals(this.bill)) {
                return;
            }

            this.bill = bill;

            addFormAndButtons(bill.getCustomer());

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

}
