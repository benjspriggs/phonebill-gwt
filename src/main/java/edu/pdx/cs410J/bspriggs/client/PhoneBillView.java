package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.PageHeader;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;

public class PhoneBillView extends VerticalPanel {
    /**
     * The view of the currently selected {@link PhoneBill}.
     */
    private PhoneBillList list = new PhoneBillList();

    /**
     * Dialog displayed when user clicks to add a new phone call.
     */
    private NewCallDialog newCallDialog = new NewCallDialog();
    private VerticalPanel dialogPanel = new VerticalPanel();

    PhoneBillView() {
        PageHeader header = new PageHeader();
        header.setText("Phone Bills");
        header.setSubText("View existing phone bills and add new calls");
        add(header);

        newCallDialog.setWidget(dialogPanel);

        Button addCallButton = new Button("Add new Phone Call");

        addCallButton.addClickHandler(event -> {
            newCallDialog.show();
        });

        add(new PhoneBillForm());
        add(new Heading(HeadingSize.H3, "Available calls"));
        add(new PhoneBillChooser(new AsyncCallback<PhoneBill>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(PhoneBill phoneBill) {
                if (list.updateIfChanged(phoneBill)){
                    addFormAndButtons(phoneBill);
                }
            }
        }));
        add(list);
        add(addCallButton);
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

    private class NewCallDialog extends DialogBox {
        NewCallDialog() {
            setText("Add a new phone call");
            setAnimationEnabled(true);
            setGlassEnabled(true);
        }
    }
}
