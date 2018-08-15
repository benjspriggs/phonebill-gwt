package edu.pdx.cs410J.bspriggs.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic GWT class that makes sure that we can send an Phone Bill back from the server
 */
public class PhoneBillGwt implements EntryPoint {
    private final Alerter alerter;
    private final PhoneBillServiceAsync phoneBillService;
    private final Logger logger;

    private DeckPanel deckPanel = new DeckPanel();

    public PhoneBillGwt() {
        this(Window::alert);
    }

    @VisibleForTesting
    PhoneBillGwt(Alerter alerter) {
        this.alerter = alerter;
        this.phoneBillService = GWT.create(PhoneBillService.class);
        this.logger = Logger.getLogger("phoneBill");
        Logger.getLogger("").setLevel(Level.INFO);  // Quiet down the default logging
    }

    private void alertOnException(Throwable throwable) {
        Throwable unwrapped = unwrapUmbrellaException(throwable);
        StringBuilder sb = new StringBuilder();
        sb.append(unwrapped.toString());
        sb.append('\n');

        for (StackTraceElement element : unwrapped.getStackTrace()) {
            sb.append("  at ");
            sb.append(element.toString());
            sb.append('\n');
        }

        this.alerter.alert(sb.toString());
    }

    private Throwable unwrapUmbrellaException(Throwable throwable) {
        if (throwable instanceof UmbrellaException) {
            UmbrellaException umbrella = (UmbrellaException) throwable;
            if (umbrella.getCauses().size() == 1) {
                return unwrapUmbrellaException(umbrella.getCauses().iterator().next());
            }

        }

        return throwable;
    }

    private void addMainPanelWidgets(VerticalPanel panel) {
        panel.add(new PhoneBillView());
    }

    @Override
    public void onModuleLoad() {
        setUpUncaughtExceptionHandler();

        // The UncaughtExceptionHandler won't catch exceptions during module load
        // So, you have to set up the UI after module load...
        Scheduler.get().scheduleDeferred(this::setupUI);
    }

    private void setupUI() {
        RootPanel rootPanel = RootPanel.get();

        PhoneBillGWTMenu menu = new PhoneBillGWTMenu();
        VerticalPanel mainPanel = new VerticalPanel();
        VerticalPanel secondaryPanel = new VerticalPanel();
        Container rootContainer = new Container();

        deckPanel.add(mainPanel);
        deckPanel.add(secondaryPanel);
        deckPanel.showWidget(0);

        rootContainer.add(menu);
        rootContainer.add(deckPanel);

        rootPanel.add(rootContainer);

        addMainPanelWidgets(mainPanel);
        addSecondaryPanelWidgets(secondaryPanel);
        addMenuOptions(menu);
    }

    private void addSecondaryPanelWidgets(VerticalPanel secondaryPanel) {
        PageHeader searchHeader = new PageHeader();
        searchHeader.setText("Search");
        searchHeader.setSubText("Choose an existing phone bill and search for calls within a date range");

        secondaryPanel.add(searchHeader);

        secondaryPanel.add(new PhoneCallSearchRangeView());
    }

    private void addMenuOptions(PhoneBillGWTMenu menu) {
        menu.addItem("Home", click -> {
            deckPanel.showWidget(0);
        });

        menu.addItem("Search", click -> {
            deckPanel.showWidget(1);
        });

        ReadmePopup r = new ReadmePopup();

        menu.addItem("Help", click -> {
            r.show();
        });
    }

    private void setUpUncaughtExceptionHandler() {
        GWT.setUncaughtExceptionHandler(this::alertOnException);
    }

    @VisibleForTesting
    interface Alerter {
        void alert(String message);
    }

    private static class ReadmePopup extends Modal {
        ReadmePopup() {
            ModalBody b = new ModalBody();
            add(b);

            ModalFooter f = new ModalFooter();
            add(f);

            setTitle("README");

            Container p = new Container();
            p.setFluid(true);

            Button closeMe = new Button("Ok!");
            closeMe.setDataDismiss(ButtonDismiss.MODAL);
            closeMe.setType(ButtonType.SUCCESS);

            for (String s : getReadme()) {
                Paragraph l = new Paragraph(s);

                p.add(l);
            }

            f.add(closeMe);
            b.add(p);
        }

        private String[] getReadme() {
            return new String[]{"This is an application to keep track of phone bills and phone calls on each bill.",
                    "To create a new phone bill, add a customer name and click on 'Create Phone Bill'. ",
                    "To add a call to an existing phone bill, click on the 'Add Phone Call' button and fill out the form.",
                "To search for calls in a bill, click on 'Search', select the customer and date range, and click 'Search'."};
        }
    }
}
