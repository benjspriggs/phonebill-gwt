package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.gwtbootstrap3.client.ui.*;

import java.util.List;

class PhoneCallSearchRangeView extends Container {
    PhoneBillList list = new PhoneBillList();
    PhoneBill toSearch;
    boolean keepSearch = false;

    PhoneCallSearchRangeView() {
        add(new MagicForm());
        add(new PhoneBillChooser(new AsyncCallback<PhoneBill>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(PhoneBill phoneBill) {
                if (list.updateIfChanged(phoneBill)) {
                    keepSearch = false;
                    toSearch = phoneBill;
                }
            }
        }));
        add(list);
    }

    private class MagicForm extends Form {
        private PhoneBillServiceAsync phoneBillServiceAsync;

        FormattedDateBox startRange = new FormattedDateBox("Start Date", "start");
        FormattedDateBox endRange = new FormattedDateBox("End Date", "end");

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

            Button submitButton = new Button("Search");
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
                            if (list.updateSomeIfChanged(toSearch, phoneCalls))
                                keepSearch = true;
                        }
                    });
        }
    }
}
