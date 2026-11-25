package projectcsi.reservoir;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

//Note: Set compiler to a 64-bit version of JavaFX to avoid compatibility issues with JavaFX 17+.
//ctrl+shift+P to open command palette, then select "Java: Configure Java Runtime" and set the JDK to a 64-bit version of JavaFX.
public class UniversalTest {

    // =====================================================
    // JAVAFX INITIALIZATION
    // =====================================================

    @BeforeAll
    public static void initializeJavaFX() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);
        try {Platform.startup(latch::countDown);}
        catch (IllegalStateException exception) {Platform.runLater(latch::countDown);}

        //Latch to ensure JavaFX is initialized before running tests.
        assertTrue(latch.await(10, TimeUnit.SECONDS), "JavaFX failed to initialize.");
        
        //Prevent JavaFX from exiting after tests complete.
        Platform.setImplicitExit(false);
    }

    // =====================================================
    // TEST 1: EVENT CREATION
    // =====================================================

    @Test
    public void testEventCreation() {
        //Create a test event with valid information.
        Event testEvent = new Event(
                "Test Concert",
                "A fake concert for testing.",
                "Fake Location",
                "2026-12-15",
                "18:00",
                100,
                25.50
        );

        //Verify event creation.
        assertNotNull(testEvent);

        //Verify stored event information.
        assertEquals("Test Concert", testEvent.getEventName());
        assertEquals("A fake concert for testing.", testEvent.getDescription());
        assertEquals("Fake Location", testEvent.getLocation());
        assertEquals("2026-12-15", testEvent.getDate());
        assertEquals("18:00", testEvent.getTime());
        assertEquals(100, testEvent.getCapacity());
        assertEquals(25.50, testEvent.getPrice(), 0.001);

        System.out.println("\nEvent creation successful.\n");
    }

    // =====================================================
    // TEST 2: SUCCESSFUL PAYMENT
    // =====================================================

    @Test
    public void testSuccessfulPayment() throws Exception {

        runOnJavaFXThread(() -> {
            //Use an AtomicBoolean to track if the payment callback was triggered.
            AtomicBoolean paymentSuccess = new AtomicBoolean(false);

            Stage stage = new Stage();

            try {
                //Create a payment screen.
                PaymentScreen paymentScreen = new PaymentScreen(stage,"Test Concert",25.50,() -> paymentSuccess.set(true),() -> {});

                paymentScreen.show();

                Parent root = stage.getScene().getRoot();

                //Enter valid fake payment information.
                setInput(root, "Enter name on card", "Test User");

                setInput(root,"1234 5678 9012 3456","1234 5678 9012 3456");

                setInput(root, "MM/YY", "12/28");
                setInput(root, "3 digits", "123");
                setInput(root, "Enter ZIP code", "12345");

                //Trigger the actual payment handler.
                Button payButton = findButton(root, "Pay $25.50");

                assertNotNull(payButton);

                payButton.fire();

                //Verify successful payment.
                assertTrue(paymentSuccess.get(),"Valid payment should trigger the success callback.");

                System.out.println("\nPayment Received: Test User, 1234 5678 9012 3456, 12/28, 123, 12345\n");

            }
            finally {stage.close();}
            return null;
        });
    }

    // =====================================================
    // JAVAFX THREAD HELPER
    // =====================================================
        //This method ensures that the provided action is executed on the JavaFX Application Thread.
            private static <T> T runOnJavaFXThread(Callable<T> action)
            throws Exception {if (Platform.isFxApplicationThread()) {return action.call();}
        //If not on the JavaFX Application Thread, use a FutureTask to execute the action and wait for the result.
            FutureTask<T> task = new FutureTask<>(action);
        //Schedule the task to run on the JavaFX Application Thread.
            Platform.runLater(task);
        //Wait for the task to complete and return the result, with a timeout of 10 seconds.
            return task.get(10, TimeUnit.SECONDS);
        }

    // =====================================================
    // INPUT HELPER
    // =====================================================

    private static void setInput(Parent root,String prompt,String value) {
        //Iterate through all nodes in the scene graph to find the TextInputControl with the specified prompt text.
        for (Node node : root.lookupAll("*")) {
            if (node instanceof TextInputControl) {
                TextInputControl input = (TextInputControl) node;
                if (prompt.equals(input.getPromptText())) {
                    input.setText(value);
                    return;
        }}}
        throw new AssertionError("Could not find payment input: " + prompt);
    }

    // =====================================================
    // BUTTON HELPER
    // =====================================================

    private static Button findButton(Parent root,String expected) {
        //Iterate through all nodes in the scene graph to find the Button with the specified text.
        for (Node node : root.lookupAll("*")) {
            if (node instanceof Button) {
                Button button = (Button) node;
                if (expected.equals(button.getText())) {
                    return button;
        }}}
        throw new AssertionError("Could not find button: " + expected);}}
