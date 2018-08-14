package edu.pdx.cs410J.bspriggs.client;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

class FormattedDateBox extends FormGroup {
    DateTimePicker picker = new DateTimePicker();
    public final String dateFormat = "mm/dd/yyyy HH:ii P"; // Nothing is standard, nothing is sacred

    FormattedDateBox(String inputName, String label){
        FormLabel l = new FormLabel();
        l.setFor(inputName);
        l.setText(label);

        picker.setFormat(dateFormat);
        picker.setAllowBlank(false);
        picker.setForceParse(true);
        picker.setAutoClose(true);
        picker.setId(inputName);

        add(l);
        add(picker);
    }
}
