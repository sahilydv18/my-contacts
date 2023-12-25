package org.example.contactsapplication;

import javafx.application.Platform;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.example.contactsapplication.datamodel.Contact;
import org.example.contactsapplication.datamodel.ContactData;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class HelloController {
    @FXML
    private TableView<Contact> tableView;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TableColumn<Contact,String> firstName;
    @FXML
    private TableColumn<Contact,String> lastName;
    @FXML
    private TableColumn<Contact,String> phoneNumber;
    @FXML
    private TableColumn<Contact,String> notes;

    private ContextMenu contextMenu;

    @FXML
    public void initialize() {
        //Sorting the table according to first name of the contact
        SortedList<Contact> sortedList = new SortedList<>(ContactData.getInstance().getContacts(), new Comparator<>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getFirstName().compareTo(o2.getFirstName());
            }
        });
        //taking contact list from sorted contact list data
        //tableView.setItems(ContactData.getInstance().getContacts());
        tableView.setItems(sortedList);
        //Creating context menu
        contextMenu = new ContextMenu();
        //Setting up delete in context menu
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Contact contact = tableView.getSelectionModel().getSelectedItem();
                deleteContactConformation(contact);
            }
        });

        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showEditDialog();
            }
        });
        contextMenu.getItems().addAll(edit,delete);

        //Setting up table view to show data
        firstName.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Contact,String>("lastName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Contact,String>("phoneNumber"));
        notes.setCellValueFactory(new PropertyValueFactory<Contact,String>("notes"));

        //Setting the selection mode to single
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //Initializing to select the first item
        tableView.getSelectionModel().selectFirst();

        //Using method to add context menu to each column
        addContextMenu();
    }

    public void addContextMenu() {
        //Adding context menu to each column
        for (TableColumn<Contact, String> contactStringTableColumn : Arrays.asList(firstName, lastName, phoneNumber, notes)) {
            contactStringTableColumn.setCellFactory(new Callback<TableColumn<Contact, String>, TableCell<Contact, String>>() {
                @Override
                public TableCell<Contact, String> call(TableColumn<Contact, String> contactStringTableColumn) {
                    TableCell<Contact, String> cell = new TableCell<>() {
                        @Override
                        protected void updateItem(String s, boolean empty) {       //Updating first name to its cells
                            super.updateItem(s, empty);
                            if (empty) {                                            //Checking if the first name is provided or not
                                setText(null);
                            } else {
//                                Text text = new Text(s);
//                                text.setWrappingWidth(380);
//                                setGraphic(text);
                                setText(s);
                            }
                        }
                    };
                    cell.emptyProperty().addListener(             //Checking if the selected cell is empty or not
                            (obs, wasEmpty, isNowEmpty) -> {
                                if (isNowEmpty) {
                                    cell.setContextMenu(null);
                                } else {
                                    cell.setContextMenu(contextMenu);
                                }
                            }
                    );
                    return cell;
                }
            });
        }
    }

    public void handleDeleteKey(KeyEvent keyEvent) {
        Contact contact = tableView.getSelectionModel().getSelectedItem();  //Getting the selected contact
        if(contact != null) {
            if(keyEvent.getCode().equals(KeyCode.DELETE)) {                 //Checking if the pressed key is DELETE
                deleteContactConformation(contact);
            }
        }
    }

    @FXML
    public void showNewContactDialog() {
        //Creating save new contact dialog pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add a new Contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("new-contact.fxml"));
        try {
            //Using the new-contact.fxml to show the save new contact dialog pane
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        //Adding OK and CANCEL buttons
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //Changing type to show and wait, so that user can input and according to that contact can be added or not
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {   //Checking if the selected button is OK
            ContactController contactController = fxmlLoader.getController();
            Contact newContact = contactController.addContact();    //Adding the new Contact
            if(newContact != null) {
                tableView.getSelectionModel().select(newContact);   //Selecting the added contact if it's not null
            }
        }
    }

    public void showEditDialog() {
        //Creating edit contact dialog pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("edit-contact.fxml"));
        try {
            //Using edit-contact.fxml to load the edit dialog pane
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        Contact contact = tableView.getSelectionModel().getSelectedItem(); //Getting the selected contact
        if(contact != null) {                                                   //Checking if selected contact is not null
            ContactController contactController = fxmlLoader.getController();
            contactController.editContact(contact);                        //Writing the already provided data into edit dialog pane
        }

        //Adding buttons
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();         //Changing dialog type to show and wait
        if(result.isPresent() && result.get() == ButtonType.OK) {
            ContactController contactController = fxmlLoader.getController();
            Contact newContact = contactController.addContact();
            if(newContact != null) {                                 //Checking if we didn't pass a null contact
                ContactData.getInstance().deleteContact(contact);    //Deleting the old contact, to then update it with the new contact
                tableView.getSelectionModel().select(newContact);
            }
        }
    }

    //Adding conformation for deleting a contact
    public void deleteContactConformation(Contact contact) {
        //Using inbuilt alert functionality to show alert dialog pane of type conformation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText("Delete Contact: " + contact.getFirstName() + " " + contact.getLastName());
        alert.setContentText("Are you sure? \n" +
                "Press OK to confirm and CANCEL to back out");
        //Changing the type of dialog pane to show and wait, to wait for user input
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {     //Checking if the user action is OK
            ContactData.getInstance().deleteContact(contact);
        }
    }

    public void exitApp() {
        Platform.exit();
    }
}