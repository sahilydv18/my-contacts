package org.example.contactsapplication.customtable;

import javafx.scene.control.TableView;

public class CustomTableView<S> extends TableView<S> {
    //Used this custom table view from https://stackoverflow.com/questions/70370273/javafx-table-column-wont-resize-to-prefwidth so that column resizing will work
    //Added a listener that helps us to automatically resize the columns based on the values from CustomTableColumn class, whenever we resize the window
    public CustomTableView() {
        widthProperty().addListener((obs, old, tableWidth) -> {
            // Deduct 2px from the total table width for borders. Otherwise, you will see a horizontal scroll bar.
            double width = tableWidth.doubleValue() - 2;
            getColumns().stream().filter(col -> col instanceof CustomTableColumn)
                    .map(col -> (CustomTableColumn) col)
                    .forEach(col -> col.setPrefWidth(width * (col.getPercentWidth() / 100)));
        });
    }
}