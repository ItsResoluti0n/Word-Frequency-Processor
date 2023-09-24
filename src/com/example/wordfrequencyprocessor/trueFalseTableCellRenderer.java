package com.example.wordfrequencyprocessor;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class trueFalseTableCellRenderer extends JList<String> implements TableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value.toString().equals("true")) {
            setBackground(Color.green);
        } else {
            setBackground(Color.red);
        }
        return this;
    }
}
