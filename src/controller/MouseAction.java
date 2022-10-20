package controller;

import model.HeaderTable;
import model.LineTable;
import view.SigInvoiceFrame;

import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MouseAction extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int selectedInvoice = SigInvoiceFrame.invoicesTable.getSelectedRow() + 1;
        ArrayList<LineTable> invoiceItems;
        for (HeaderTable invoice : SigInvoiceFrame.invoices) {
            if (invoice.getInvoiceNum() == selectedInvoice) {

                SigInvoiceFrame.invoiceNumLbl.setText(String.valueOf(invoice.getInvoiceNum()));
                SigInvoiceFrame.invoiceDateTxtField.setText(sdf.format(invoice.getInvoiceDate()));
                SigInvoiceFrame.customerNameTxtField.setText(invoice.getCustomerName());
                double total = 0.0;
                if (invoice.getInvoiceLines() != null) {
                    for (LineTable item : invoice.getInvoiceLines()) {
                        total += item.getItemPrice() * item.getCount();
                    }

                    SigInvoiceFrame.invoiceTotalLbl.setText(String.valueOf(total));
                    invoiceItems = invoice.getInvoiceLines();
                    Object[][] table2Data = getInvoiceItemsTableData(invoiceItems);
                    SigInvoiceFrame.itemsTable.setModel(new DefaultTableModel(table2Data,
                            new String[]{"No.", "Item Name", "Item Price", "Count", "Item Total"}));
                } else {
                    SigInvoiceFrame.invoiceTotalLbl.setText(String.valueOf(total));

                    SigInvoiceFrame.itemsTable.setModel(new javax.swing.table.DefaultTableModel(
                            new Object[][]{

                            },
                            new String[]{
                                    "No.", "Item Name", "Item Price", "Count", "Item Total"
                            }
                    ));

                    SigInvoiceFrame.itemsTable.setModel(new DefaultTableModel(new Object[][]{},
                            new String[]{"No.", "Item Name", "Item Price", "Count", "Item Total"}));

                }

            }
        }
    }

    // Helper Method
    private Object[][] getInvoiceItemsTableData(ArrayList<LineTable> items) {

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

