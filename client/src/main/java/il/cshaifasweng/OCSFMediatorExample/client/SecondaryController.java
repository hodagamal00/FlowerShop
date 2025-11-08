package il.cshaifasweng.OCSFMediatorExample.client;
import  il.cshaifasweng.OCSFMediatorExample.entities.Product;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.PrimitiveIterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SecondaryController {


    @FXML // fx:id="apply_changes"
    private Button apply_changes; // Value injected by FXMLLoader

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button edit_product;

    @FXML
    private DialogPane flower_details;

    @FXML
    private DialogPane flower_name;

    @FXML
    private DialogPane flower_price;

    @FXML
    private ImageView flower_image;

    @FXML
    private Button back_button;

    @FXML
    private TextArea setDetails;

    @FXML
    private TextField setName;

    @FXML
    private TextField setPrice;


    private Button btn;

    @FXML
    void edit_product(ActionEvent event)
    {
        apply_changes.setVisible(true);
        Product currtProduct  = il.cshaifasweng.OCSFMediatorExample.client.PrimaryController.getCurrent_button();

        flower_details.setVisible(false);
        flower_name.setVisible(false);
        flower_price.setVisible(false);

        setDetails.setVisible(true);
        setName.setVisible(true);
        setPrice.setVisible(true);

        setPrice.setText(String.valueOf(currtProduct.getPrice()));
        setDetails.setText(currtProduct.getDetails());
        setName.setText(currtProduct.getName());


    }

    @FXML
    void returnWindow(ActionEvent event) throws IOException {
        PrimaryController.setReturnedFromSecondaryController(true);
        App.setRoot("primary");
    }

    @FXML
    void updateProduct(ActionEvent event)
    {
        Product currtProduct  = il.cshaifasweng.OCSFMediatorExample.client.PrimaryController.getCurrent_button();


        // setPrice expects a double; parse the text field into a double
        try {
            double newPrice = Double.parseDouble(setPrice.getText());
            currtProduct.setPrice(newPrice);
        } catch (NumberFormatException ex) {
            // If parsing fails, leave the price unchanged or handle accordingly
            // You might want to show an error to the user here
            ex.printStackTrace();
        }
        currtProduct.setDetails(setDetails.getText());
        currtProduct.setName(setName.getText());


        flower_details.setVisible(true);
        flower_name.setVisible(true);
        flower_price.setVisible(true);

        flower_details.setContentText(currtProduct.getDetails());
        flower_name.setContentText(currtProduct.getName());
        // getPrice() returns a double; convert to String for display
        flower_price.setContentText(String.valueOf(currtProduct.getPrice()));

        setDetails.setVisible(false);
        setName.setVisible(false);
        setPrice.setVisible(false);

        apply_changes.setVisible(false);

        try {
            SimpleClient.getClient().sendToServer(currtProduct); // sends the updated product to the server class
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @FXML
    void initialize() throws MalformedURLException {

        assert edit_product != null : "fx:id=\"edit_product\" was not injected: check your FXML file 'secondary.fxml'.";
        assert flower_details != null : "fx:id=\"flower_details1\" was not injected: check your FXML file 'secondary.fxml'.";
        assert flower_name != null : "fx:id=\"flower_name1\" was not injected: check your FXML file 'secondary.fxml'.";
        assert flower_price != null : "fx:id=\"flower_price1\" was not injected: check your FXML file 'secondary.fxml'.";
        assert flower_image != null : "fx:id=\"flower_image1\" was not injected: check your FXML file 'secondary.fxml'.";

        // disable editing fields
        setDetails.setVisible(false);
        setName.setVisible(false);
        setPrice.setVisible(false);

        // disable all fields
        flower_details.setVisible(false);
        flower_name.setVisible(false);
        flower_price.setVisible(false);

        // set all fields details using product object
        Product currentProduct = il.cshaifasweng.OCSFMediatorExample.client.PrimaryController.getCurrent_button();
        flower_details.setContentText(currentProduct.getDetails());
        // Convert price to string before setting content text
        flower_price.setContentText(String.valueOf(currentProduct.getPrice()));
        flower_name.setContentText(currentProduct.getName());

        // Load the product image from the resources folder based on the ID
        String img_path = "src/main/resources/images/flower" + currentProduct.getID() + ".png";
        File file = new File(img_path);
        String localUrl = file.toURI().toURL().toExternalForm();
        flower_image.setImage(new Image(localUrl,true));

        // enable all fields
        flower_details.setVisible(true);
        flower_name.setVisible(true);
        flower_price.setVisible(true);
        flower_image.setVisible(true);

        apply_changes.setVisible(false);
    }

}
