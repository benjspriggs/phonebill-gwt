package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PhoneCallForm extends FormPanel {
    private static final Map<String, String> FIELDS = init();
    private HashMap<String, HasValue<String>> values = new HashMap<>();
    private final PhoneBillServiceAsync phoneBillService;

    private static Map<String, String> init(){
        HashMap<String, String> map = new HashMap<>();
        map.put("customer", "Customer");
        map.put("caller", "Caller");
        map.put("callee", "Callee");
        map.put("startTime", "Start Time");
        map.put("endTime", "End Time");
        return Collections.unmodifiableMap(map);
    }

    PhoneCallForm() {
        this.phoneBillService = GWT.create(PhoneBillService.class);

        setAction(GWT.getModuleBaseURL() + "call");
        setEncoding(FormPanel.ENCODING_MULTIPART);
        setMethod(FormPanel.METHOD_POST);

        VerticalPanel p = new VerticalPanel();
        addWidgets(p);
        add(p);

        addSubmitHandler(event -> {
            // TODO: validation here
            String caller = values.get("caller").getValue();
            String callee = values.get("callee").getValue();
            Date startTime = PhoneCall.parseDate(values.get("startTime").getValue());
            Date endTime = PhoneCall.parseDate(values.get("endTime").getValue());
            PhoneCall call = new PhoneCall(caller, callee, startTime, endTime);
            phoneBillService.addPhoneCallToBill(values.get("customer").getValue(), call, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Window.alert("dong");
                }

                @Override
                public void onSuccess(Void aVoid) {
                    Window.alert("ding");
                }
            });
        });
    }

    private void addWidgets(Panel p) {
        Label l = new Label("Create a new Phone call");
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
