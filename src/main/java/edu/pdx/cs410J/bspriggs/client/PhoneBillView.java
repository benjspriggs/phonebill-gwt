package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
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
    private Container dialogPanel = new Container();

    PhoneBillView() {
        PageHeader header = new PageHeader();
        header.setText("Phone Bills");
        header.setSubText("View existing phone bills and add new calls");
        add(header);

        newCallDialog.addWidget(dialogPanel);

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

        dialogPanel.add(form);
    }

    private class NewCallDialog extends Modal {
        ModalBody b = new ModalBody();

        NewCallDialog() {
            setId("NewCallDialog");
            setClosable(true);
            setTitle("Add a new phone call");
            setClosable(true);
            setFade(true);
            ModalFooter footer = new ModalFooter();
            Button closeButton = new Button("Close");
            closeButton.setDataDismiss(ButtonDismiss.MODAL);
            closeButton.setType(ButtonType.DANGER);
            footer.add(closeButton);

            add(b);
            add(footer);
        }

        public void addWidget(Container w) {
            b.add(w);
        }
    }
}
