package org.example.contactsapplication.customtable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TableColumn;

public class CustomTableColumn<S, T> extends TableColumn<S, T> {
    //Used this custom table column from https://stackoverflow.com/questions/70370273/javafx-table-column-wont-resize-to-prefwidth so that column resizing will work
    //Taking the percent width values from mainwindow.fxml file then using them to resize our columns
    private DoubleProperty percentWidth = new SimpleDoubleProperty();

    //Added an empty constructor to handle the error - java.lang.NoSuchMethodException: userAuth.User.<init>()
    public CustomTableColumn() {
    }

    public CustomTableColumn(String columnName) {
        super(columnName);
    }

    public DoubleProperty percentWidth() {
        return percentWidth;
    }

    public double getPercentWidth() {
        return percentWidth.get();
    }

    public void setPercentWidth(double percentWidth) {
        this.percentWidth.set(percentWidth);
    }
}