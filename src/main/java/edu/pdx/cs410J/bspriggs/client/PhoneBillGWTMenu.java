package edu.pdx.cs410J.bspriggs.client;

import com.google.gwt.event.dom.client.ClickHandler;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarNav;

public class PhoneBillGWTMenu extends Navbar {
    NavbarNav n = new NavbarNav();
    NavbarCollapse navbarCollapse = new NavbarCollapse();

    PhoneBillGWTMenu() {
        navbarCollapse.add(n);

        add(navbarCollapse);
    }

    void addItem(String name, ClickHandler onClick) {
        AnchorListItem item = new AnchorListItem();
        item.setText(name);

        item.addClickHandler(onClick);
        n.add(item);
    }
}
