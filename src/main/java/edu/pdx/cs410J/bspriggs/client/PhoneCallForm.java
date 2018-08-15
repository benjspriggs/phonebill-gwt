package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ContextualBackground;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PhoneCallForm extends Form {
    private static final Map<String, String> FIELDS = init();
    private final Paragraph errorDisplay = new Paragraph();
    private HashMap<String, HasValue<String>> values = new HashMap<>();
    private final PhoneBillServiceAsync phoneBillService;

    private static Map<String, String> init(){
        HashMap<String, String> map = new HashMap<>();
        map.put("caller", "Caller");
        map.put("callee", "Callee");
        map.put("startTime", "Start Time");
        map.put("endTime", "End Time");
        return Collections.unmodifiableMap(map);
    }

    PhoneCallForm(String customer) {
        this.phoneBillService = GWT.create(PhoneBillService.class);

        errorDisplay.setContextualBackground(ContextualBackground.DANGER);

        FieldSet p = new FieldSet();
        addWidgets(p);
        add(p);

        addSubmitHandler(event -> {
            if (!validate()) {
                event.cancel();
                return;
            }

            String caller = values.get("caller").getValue();
            String callee = values.get("callee").getValue();
            Date startTime = PhoneCall.parseDate(values.get("startTime").getValue());
            Date endTime = PhoneCall.parseDate(values.get("endTime").getValue());

            PhoneCall call = new PhoneCall(caller, callee, startTime, endTime);

            if (!call.validate()) {
                InvalidPhoneReason r = call.invalidBecause();
                errorDisplay.setText("Unable to submit: " + r.toString());
                event.cancel();
                return;
            }

            phoneBillService.addPhoneCallToBill(customer, call, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(Void aVoid) {
                }
            });
        });
    }

    private void addWidgets(FieldSet set) {
        for (Map.Entry<String, String> pair: FIELDS.entrySet()) {
            String name = pair.getKey();
            String displayName = pair.getValue();

            if (name.contains("Time")) {
                FormattedDateBox db = new FormattedDateBox(name, displayName);
                set.add(db);
                values.put(name, db.picker.getTextBox());
                continue;
            }

            FormGroup formGroup = new FormGroup();

            FormLabel label = new FormLabel();
            label.setFor(name);
            label.setText(displayName);

            Input input = new Input();
            input.setId(name);
            input.setAllowBlank(false);
            input.setValidateOnBlur(true);

            formGroup.add(label);
            formGroup.add(input);

            set.add(formGroup);

            values.put(name, input);
        }

        set.add(errorDisplay);

        set.add(new Button("Submit", event -> this.submit()));
        set.add(new Button("Reset", event -> this.reset()));
    }
}
