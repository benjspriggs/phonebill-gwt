package edu.pdx.cs410J.bspriggs.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic GWT class that makes sure that we can send an Phone Bill back from the server
 */
public class PhoneBillGwt implements EntryPoint {
    private final Alerter alerter;
    private final PhoneBillServiceAsync phoneBillService;
    private final Logger logger;


    @VisibleForTesting
    Button showPhoneBillButton;

    @VisibleForTesting
    Button showUndeclaredExceptionButton;

    @VisibleForTesting
    Button showDeclaredExceptionButton;

    @VisibleForTesting
    Button showClientSideExceptionButton;

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

    private void addWidgets(VerticalPanel panel) {
        showPhoneBillButton = new Button("Show Phone Bill");
        showPhoneBillButton.addClickHandler(clickEvent -> showPhoneBill());

        showUndeclaredExceptionButton = new Button("Show undeclared exception");
        showUndeclaredExceptionButton.addClickHandler(clickEvent -> showUndeclaredException());

        showDeclaredExceptionButton = new Button("Show declared exception");
        showDeclaredExceptionButton.addClickHandler(clickEvent -> showDeclaredException());

        showClientSideExceptionButton= new Button("Show client-side exception");
        showClientSideExceptionButton.addClickHandler(clickEvent -> throwClientSideException());

        /*
        panel.add(showPhoneBillButton);
        panel.add(showUndeclaredExceptionButton);
        panel.add(showDeclaredExceptionButton);
        panel.add(showClientSideExceptionButton);
        */
        panel.add(new PhoneBillView());
    }

    private void throwClientSideException() {
        logger.info("About to throw a client-side exception");
        throw new IllegalStateException("Expected exception on the client side");
    }

    private void showUndeclaredException() {
        logger.info("Calling throwUndeclaredException");
        phoneBillService.throwUndeclaredException(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable ex) {
                alertOnException(ex);
            }

            @Override
            public void onSuccess(Void aVoid) {
                alerter.alert("This shouldn't happen");
            }
        });
    }

    private void showDeclaredException() {
        logger.info("Calling throwDeclaredException");
        phoneBillService.throwDeclaredException(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable ex) {
                alertOnException(ex);
            }

            @Override
            public void onSuccess(Void aVoid) {
                alerter.alert("This shouldn't happen");
            }
        });
    }

    private void showPhoneBill() {
        logger.info("Calling getPhoneBill");
        phoneBillService.getPhoneBill("CS410J", new AsyncCallback<PhoneBill>() {

            @Override
            public void onFailure(Throwable ex) {
                alertOnException(ex);
            }

            @Override
            public void onSuccess(PhoneBill phoneBill) {
                StringBuilder sb = new StringBuilder(phoneBill.toString());
                Collection<PhoneCall> calls = phoneBill.getPhoneCalls();
                for (PhoneCall call : calls) {
                    sb.append(call);
                    sb.append("\n");
                }
                alerter.alert(sb.toString());
            }
        });
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
        VerticalPanel panel = new VerticalPanel();
        MenuBar menu = new MenuBar();
        rootPanel.add(menu);
        rootPanel.add(panel);

        addWidgets(panel);
        addMenuOptions(menu);
    }

    private void addMenuOptions(MenuBar menu) {
        menu.addItem("New", new CreateMenuItem());
        menu.addItem("Help", new HelpMenuItem());
    }

    private void setUpUncaughtExceptionHandler() {
        GWT.setUncaughtExceptionHandler(this::alertOnException);
    }

    @VisibleForTesting
    interface Alerter {
        void alert(String message);
    }

    private static class ReadmePopup extends DialogBox {
        ReadmePopup() {
            setText("README");
            setAnimationEnabled(true);
            setGlassEnabled(true);

            VerticalPanel p = new VerticalPanel();
            Button closeMe = new Button("Close");
            closeMe.addClickHandler(event -> hide());

            Label l = new Label(getReadme());

            p.add(l);
            p.add(closeMe);
            setWidget(p);
        }

        private String getReadme() {
            return "This is an application to keep track of phone bills and phone calls on each bill." +
                    "To create a new phone bill, click on 'New -> Phone Bill'. To add a call to an existing" +
                    "phone bill, click on the 'Add Phone Call' button and fill out the form. ";
        }
    }

    private static class PhoneBillPopup extends DialogBox {
        PhoneBillPopup() {
            setText("New Phone Bill");
            setAnimationEnabled(true);
            setGlassEnabled(true);

            VerticalPanel p = new VerticalPanel();
            Button closeMe = new Button("Close");
            closeMe.addClickHandler(event -> hide());
            PhoneBillForm form = new PhoneBillForm();
            form.addSubmitHandler(event -> hide());

            p.add(new PhoneBillForm());
            p.add(closeMe);
            setWidget(p);
        }
    }

    private static class HelpMenuItem extends MenuBar {
        HelpMenuItem() {
            ReadmePopup readme = new ReadmePopup();
            // readme.setPopupPosition(Window.getClientWidth() / 2, Window.getClientHeight() / 2);
            addItem(new MenuItem("README", readme::show));
        }
    }

    private class CreateMenuItem extends MenuBar {
        CreateMenuItem() {
            PhoneBillPopup p = new PhoneBillPopup();
            // p.setPopupPosition(Window.getClientWidth() / 2, Window.getClientHeight() / 2);
            addItem(new MenuItem("Phone bill", new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    p.show();
                }
            }));
        }
    }
}
