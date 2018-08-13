package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.PageHeader;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Dialog displayed when user clicks to add a new phone call.
     */
    private DialogBox newCallDialog = new DialogBox();
    private VerticalPanel dialogPanel = new VerticalPanel();

    PhoneBillView() {
        phoneBillService = GWT.create(PhoneBillService.class);

        PageHeader header = new PageHeader();
        header.setText("Available Phone Bills");
        add(header);

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

        newCallDialog.add(dialogPanel);
        newCallDialog.setGlassEnabled(true);
        newCallDialog.setAnimationEnabled(true);
        Button addCallButton = new Button("Add new Phone Call");

        addCallButton.addClickHandler(event -> {
            newCallDialog.show();
        });

        add(l);
        add(newCallDialog);
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
                if (list.updateIfChanged(phoneBill)){
                    addFormAndButtons(phoneBill);
                }
            }
        });
    }

    private void addFormAndButtons(PhoneBill phoneBill) {
        if (phoneBill == null)
            return;

        String customer = phoneBill.getCustomer();

        dialogPanel.clear();

        PhoneCallForm form = new PhoneCallForm(customer);

        form.addSubmitHandler(submit -> {
            newCallDialog.hide(true);
        });
        dialogPanel.add(form);

        Button closeButton = new Button("Close");
        closeButton.addClickHandler(event -> {
            newCallDialog.hide(true);
        });
        dialogPanel.add(closeButton);
    }


}
