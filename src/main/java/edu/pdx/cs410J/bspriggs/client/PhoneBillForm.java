package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.FormType;
import org.gwtbootstrap3.client.ui.gwt.FormPanel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PhoneBillForm extends FormPanel {
    private static final Map<String, String> FIELDS = init();
    private HashMap<String, HasValue<String>> values = new HashMap<>();
    private final PhoneBillServiceAsync phoneBillService;

    private static Map<String, String> init(){
        HashMap<String, String> map = new HashMap<>();
        map.put("customer", "Customer");
        return Collections.unmodifiableMap(map);
    }

    PhoneBillForm() {
        setType(FormType.INLINE);
        this.phoneBillService = GWT.create(PhoneBillService.class);

        FieldSet set = new FieldSet();
        addWidgets(set);
        add(set);

        addSubmitHandler(event -> {
            phoneBillService.createPhoneBill(values.get("customer").getValue(), new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Window.alert(throwable.getLocalizedMessage());
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

        set.add(new Button("Create Phone Bill", event -> this.submit()));
    }
}
