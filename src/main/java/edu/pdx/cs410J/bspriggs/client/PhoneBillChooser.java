package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.gwtbootstrap3.client.ui.ListBox;

import java.util.ArrayList;
import java.util.List;

public class PhoneBillChooser extends ListBox {
    private final PhoneBillServiceAsync phoneBillService;

    private AsyncCallback<PhoneBill> onUpdate;

    /**
     * All of the possible customer names;
     */
    private List<String> bills = new ArrayList<>();

    PhoneBillChooser(AsyncCallback<PhoneBill> onUpdate) {
        this.onUpdate = onUpdate;
        phoneBillService = GWT.create(PhoneBillService.class);

        addChangeHandler(change -> {
            refreshAvailablePhoneBills();
        });

        refreshAvailablePhoneBills();

        Timer t = new Timer() {
            @Override
            public void run() {
                refreshAvailablePhoneBills();
            }
        };
        t.scheduleRepeating(100);
    }

    private void refreshAvailablePhoneBills() {
        phoneBillService.getAvailablePhonebills(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                // TODO: probably should do something
                Window.alert(throwable.toString());
            }

            @Override
            public void onSuccess(List<String> strings) {
                if (bills.size() == strings.size()) {
                    return;
                }

                bills = strings;
                clear();
                bills.forEach(PhoneBillChooser.super::addItem);

            }
        });

        phoneBillService.getPhoneBill(getSelectedItemText(), onUpdate);
    }
}
