package controller;

import model.InvoiceHeader;
import model.InvoiceLines;
import view.InvoiceFrame;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MouseHandler extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int selectedInvoice = InvoiceFrame.invoicesTable.getSelectedRow() + 1;
        ArrayList<InvoiceLines> invoiceItems;
        for (InvoiceHeader invoice : InvoiceFrame.invoices) {
            if (invoice.getInvoiceNum() == selectedInvoice) {

                InvoiceFrame.invoiceNumLbl.setText(String.valueOf(invoice.getInvoiceNum()));
                InvoiceFrame.invoiceDateTxtField.setText(sdf.format(invoice.getInvoiceDate()));
                InvoiceFrame.customerNameTxtField.setText(invoice.getCustomerName());
                double total = 0.0;
                if (invoice.getInvoiceLines() != null) {
                    for (InvoiceLines item : invoice.getInvoiceLines()) {
                        total += item.getItemPrice() * item.getCount();
                    }

                    InvoiceFrame.invoiceTotalLbl.setText(String.valueOf(total));
                    invoiceItems = invoice.getInvoiceLines();
                    Object[][] table2Data = getInvoiceItemsTableData(invoiceItems);
                    InvoiceFrame.itemsTable.setModel(new DefaultTableModel(table2Data,
                            new String[]{"No.", "Item Name", "Item Price", "Count", "Item Total"}));
                } else {
                    InvoiceFrame.invoiceTotalLbl.setText(String.valueOf(total));

                    InvoiceFrame.itemsTable.setModel(new javax.swing.table.DefaultTableModel(
                            new Object[][]{

                            },
                            new String[]{
                                    "No.", "Item Name", "Item Price", "Count", "Item Total"
                            }
                    ));

                    InvoiceFrame.itemsTable.setModel(new DefaultTableModel(new Object[][]{},
                            new String[]{"No.", "Item Name", "Item Price", "Count", "Item Total"}));

                }

            }
        }
    }

    // Helper Method
    private Object[][] getInvoiceItemsTableData(ArrayList<InvoiceLines> items) {

        Object[][] tableData = new Object[items.size()][5];
        for (int i = 0; i < items.size(); i++) {
            tableData[i][0] = items.get(i).getInvoice().getInvoiceNum();
            tableData[i][1] = items.get(i).getItemName();
            tableData[i][2] = items.get(i).getItemPrice();
            tableData[i][3] = items.get(i).getCount();
            double itemTotal = items.get(i).getItemPrice() * items.get(i).getCount();
            tableData[i][4] = itemTotal;

        }

        return tableData;

    }

}

