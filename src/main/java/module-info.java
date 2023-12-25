module org.example.contactsapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens org.example.contactsapplication to javafx.fxml;
    exports org.example.contactsapplication;
    exports org.example.contactsapplication.datamodel;
    opens org.example.contactsapplication.datamodel to javafx.fxml;
    exports org.example.contactsapplication.customtable;
    opens org.example.contactsapplication.customtable to javafx.fxml;
}