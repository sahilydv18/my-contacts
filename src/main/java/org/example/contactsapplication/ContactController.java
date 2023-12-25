package org.example.contactsapplication;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.contactsapplication.datamodel.Contact;
import org.example.contactsapplication.datamodel.ContactData;

public class ContactController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextArea notesTextArea;

    public Contact addContact() {                                       //Used for creating a new contact
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String notes = notesTextArea.getText().trim();
        if(!(firstName.isEmpty()) && !(lastName.isEmpty()) && !(phoneNumber.isEmpty()) && !(notes.isEmpty())) {
            Contact newContact = new Contact(firstName,lastName,phoneNumber,notes);
            ContactData.getInstance().addContact(newContact);
            return newContact;
        }
        return null;
    }

    public void editContact(Contact contact) {                          //Used when editing an existing contact,
        firstNameField.setText(contact.getFirstName());                 //We write the already save data from the selected contact into edit dialog pane
        lastNameField.setText(contact.getLastName());
        phoneNumberField.setText(contact.getPhoneNumber());
        notesTextArea.setText(contact.getNotes());
    }
}
