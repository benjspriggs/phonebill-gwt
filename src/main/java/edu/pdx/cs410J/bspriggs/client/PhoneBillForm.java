package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

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
        this.phoneBillService = GWT.create(PhoneBillService.class);

        setAction(GWT.getModuleBaseURL() + "bill");
        setEncoding(FormPanel.ENCODING_MULTIPART);
        setMethod(FormPanel.METHOD_POST);

        VerticalPanel p = new VerticalPanel();
        addWidgets(p);
        add(p);

        addSubmitHandler(event -> {
            // TODO: validation here
            phoneBillService.createPhoneBill(values.get("customer").getValue(), new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Window.alert(throwable.getLocalizedMessage());
                }

                @Override
                public void onSuccess(Void aVoid) {
                    Window.alert("I DID IT");
                }
            });
        });
    }

    private void addWidgets(Panel p) {
        Label l = new Label("Create a new Phone bill");
        p.add(l);

        for (Map.Entry<String, String> pair: FIELDS.entrySet()) {
            String name = pair.getKey();
            String displayName = pair.getValue();

            final HorizontalPanel hp = new HorizontalPanel();
            final TextBox tb = new TextBox();
            final Label tbl = new Label(displayName + ":");
            tbl.addClickListener(event -> tb.setFocus(true));
            tb.setName(name);

            hp.add(tbl);
            hp.add(tb);

            p.add(hp);

            values.put(name, tb);
        }

        p.add(new Button("Submit", (ClickHandler) event -> this.submit()));
    }
}
