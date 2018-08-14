package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import java.util.List;

class PhoneCallSearchRangeView extends VerticalPanel {
    PhoneBillList list = new PhoneBillList();
    public final String dateFormat = "mm/dd/yy HH:ii P"; // Nothing is standard, nothing is sacred
    PhoneBill toSearch;

    PhoneCallSearchRangeView() {
        add(new MagicForm());
        add(new PhoneBillChooser(new AsyncCallback<PhoneBill>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(PhoneBill phoneBill) {
                if (list.updateIfChanged(phoneBill)) {
                    toSearch = phoneBill;
                }
            }
        }));
        add(list);
    }

    private class MagicDateBox extends FormGroup {

        DateTimePicker picker = new DateTimePicker();

        MagicDateBox(String inputName, String label){
            FormLabel l = new FormLabel();
            l.setFor(inputName);
            l.setText(label);

            picker.setFormat(dateFormat);
            picker.setAutoClose(true);
            picker.setId(inputName);

            add(l);
            add(picker);
        }
    }

    private class MagicForm extends Form {
        private PhoneBillServiceAsync phoneBillServiceAsync;

        MagicDateBox startRange = new MagicDateBox("Start Date", "start");
        MagicDateBox endRange = new MagicDateBox("End Date", "end");

        MagicForm() {
            phoneBillServiceAsync = GWT.create(PhoneBillService.class);

            setSubmitOnEnter(true);
            addSubmitCompleteHandler(submitCompleteEvent -> searchPhoneCalls());

            FieldSet f = new FieldSet();
            f.add(new Legend("Search Range"));
            FormGroup group = new FormGroup();
            group.add(startRange);
            group.add(endRange);

            f.add(group);
            Button submitButton = new Button("Submit");
            submitButton.addClickHandler(click -> submit());

            f.add(submitButton);

            add(f);
        }

        private void searchPhoneCalls() {
            phoneBillServiceAsync.searchForCalls(toSearch.getCustomer(), startRange.picker.getValue(),
                    endRange.picker.getValue(),
                    new AsyncCallback<List<PhoneCall>>() {
                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(List<PhoneCall> phoneCalls) {
                            Window.alert(phoneCalls.toString());
                        }
                    });
        }
    }
}
