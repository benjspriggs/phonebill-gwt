package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.FormType;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

class PhoneCallSearchRangeView extends HorizontalPanel {
    PhoneBillList list = new PhoneBillList();

    PhoneCallSearchRangeView() {
        add(list);
        add(new MagicForm());
    }

    private class MagicDateBox extends FormGroup {
        DateTimePicker picker = new DateTimePicker();

        MagicDateBox(String inputName, String label){
            FormLabel l = new FormLabel();
            l.setFor(inputName);
            l.setText(label);

            picker.setFormat("mm/dd/yy HH:ii P");
            picker.setAutoClose(true);
            picker.setId(inputName);

            add(l);
            add(picker);
        }
    }

    private class MagicForm extends Form {
        MagicDateBox startRange = new MagicDateBox("Start Date", "start");
        MagicDateBox endRange = new MagicDateBox("End Date", "end");

        MagicForm() {
            add(new Legend("Search Range"));
            setType(FormType.HORIZONTAL);

            FieldSet f = new FieldSet();
            FormGroup group = new FormGroup();
            group.add(startRange);
            group.add(endRange);

            f.add(group);

            add(f);
        }
    }
}
