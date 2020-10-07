package com.esi.Components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;

public class MessageComponent extends VBox {

    public MessageComponent() {
        // A helper method, will be explained
        // in detail later.
        loadFXML(this);
    }

    @FXML
    private TextArea editableText;
    @FXML
    private TextArea upperText;

    @FXML
    private void initialize() {
        upperText.textProperty().bind(editableText.textProperty());
    }

    public static <T extends Parent> void loadFXML(T component) {
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(component);
        loader.setControllerFactory(theClass -> component);

        String fileName = component.getClass().getSimpleName() + ".fxml";
        System.out.println(fileName);
        try {
            InputStream input = component.getClass().getResourceAsStream(fileName);
            loader.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
