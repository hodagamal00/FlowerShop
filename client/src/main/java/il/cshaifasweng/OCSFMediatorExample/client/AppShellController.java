package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Controller for the {@code AppShell.fxml}.  This class manages the
 * application shell which contains a header, a central content area
 * and a status bar.  It provides methods for swapping the centre
 * content and updating header elements such as the cart badge and
 * login/profile buttons.
 */
public class AppShellController {

    @FXML private TextField searchField;
    @FXML private Button loginButton;
    @FXML private VBox profileContainer;
    @FXML private Button profileButton;
    @FXML private Label profileNameLabel;
    @FXML private Button cartButton;
    @FXML private Label statusLabel;
    @FXML private StackPane contentPane;
    /**
     * Called by the FXML loader after the fields have been injected.
     * Registers this controller with the {@link NavigationService} and
     * attaches basic navigation handlers to the header buttons.
     */
    public void initialize() {
        NavigationService.getInstance().setAppShellController(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // Attach simple handlers that delegate navigation to the
        // NavigationService.  These may be overridden or extended
        // by individual controllers as needed.
        if (loginButton != null) {
            loginButton.setOnAction(e -> NavigationService.getInstance().navigate("Login"));
        }
        if (profileButton != null) {
            profileButton.setOnAction(e -> NavigationService.getInstance().navigate("Profile"));
        }
        if (cartButton != null) {
            cartButton.setOnAction(e -> NavigationService.getInstance().navigate("cart"));
        }
        updateLoginState(SimpleClient.getUser());

    }

    /**
     * Replaces the content of the centre pane with the given node.
     *
     * @param node the node to display in the centre
     */
    public void setContent(Node node) {
        contentPane.getChildren().setAll(node);
    }

    /**
     * Updates the cart button text to show the current item count.
     *
     * @param count the number of items in the cart
     */
    public void updateCartCount(int count) {
        if (cartButton != null) {
            cartButton.setText("Cart (" + count + ")");
        }
    }

    /**
     * Shows or hides the login and profile buttons based on login state.
     * When the user is logged in, the login button is hidden and the
     * profile button is visible.
     *
     * @param loggedIn whether the user is currently logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        Account account = loggedIn ? SimpleClient.getUser() : null;
        updateLoginState(account);
    }

    /**
     * Updates the status message at the bottom of the application.
     *
     * @param message the message to display
     */
    public void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
    @Subscribe
    public void onAccountReceived(PassAccountEvent event) {
        updateLoginState(event.getRecievedAccount());
    }

    private void updateLoginState(Account account) {
        if (loginButton == null || profileButton == null || profileContainer == null || profileNameLabel == null) {
            return;
        }

        Account effectiveAccount = account;
        if (effectiveAccount == null || (isNullOrBlank(effectiveAccount.getFullName()) && isNullOrBlank(effectiveAccount.getEmail()))) {
            effectiveAccount = SimpleClient.getUser();
        }

        final Account finalAccount = effectiveAccount;
        Platform.runLater(() -> {
            boolean loggedIn = finalAccount != null;
            String displayName = "";
            if (loggedIn) {
                displayName = isNullOrBlank(finalAccount.getFullName()) ? finalAccount.getEmail() : finalAccount.getFullName();
                if (isNullOrBlank(displayName)) {
                    loggedIn = false;
                }
            }

            profileNameLabel.setText(loggedIn ? displayName : "");

            loginButton.setVisible(!loggedIn);
            loginButton.setManaged(!loggedIn);

            profileContainer.setVisible(loggedIn);
            profileContainer.setManaged(loggedIn);
        });
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}