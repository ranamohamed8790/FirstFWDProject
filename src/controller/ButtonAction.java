package controller;

import model.HeaderTable;
import model.LineTable;
import view.AddNewItemDialog;
import view.SigInvoiceFrame;
import view.AddNewInvoiceDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ButtonAction implements ActionListener {

    // == Fields ==
    AddNewInvoiceDialog invoiceDialog;
    AddNewItemDialog itemDialog;

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {

            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Add item":
                saveItem();
                break;
            case "Delete item":
                deleteItem();
                break;
        }

    }
    public static ArrayList<HeaderTable> readFile() {
        ArrayList<HeaderTable> invoices = new ArrayList<>();
        String HeaderTableFilePath = "resources\\HeaderTable.csv";
        String invoiceLineFilePath = "resources\\InvoiceLine.csv";
        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(HeaderTableFilePath));
            while ((line = reader.readLine()) != null) {
                HeaderTable invoice;
                String[] row = line.split(",");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(row[1]);
                invoice = new HeaderTable(Integer.parseInt(row[0]), date, row[2]);
                ArrayList<LineTable> invoiceItems = getItemsForInvoice(invoice, invoiceLineFilePath);
                invoice.setLineTable(invoiceItems);
                invoices.add(invoice);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File is not found with this path");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return invoices;
    }

    public static ArrayList<HeaderTable> readFile(String invoicesFilePath, String itemsFilePath) {
        ArrayList<HeaderTable> invoices = new ArrayList<>();

        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(invoicesFilePath));
            while ((line = reader.readLine()) != null) {
                HeaderTable invoice;
                String[] row = line.split(",");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(row[1]);
                invoice = new HeaderTable(Integer.parseInt(row[0]), date, row[2]);
                ArrayList<LineTable> invoiceItems = getItemsForInvoice(invoice, itemsFilePath);
                invoice.setLineTable(invoiceItems);
                invoices.add(invoice);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File is not found with this path");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return invoices;
    }

    public static void writeFile(ArrayList<HeaderTable> invoices) {

        String HeaderTableFilePath = "resources\\Invoices.csv";
        String invoiceLineFilePath = "resources\\InvoiceItems.csv";
        PrintWriter headerWriter = null;
        PrintWriter lineWriter = null;

        try {

            headerWriter = getFileWriter(HeaderTableFilePath);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (HeaderTable invoice : invoices) {
                String formattedDate = sdf.format(invoice.getInvoiceDate());
                headerWriter.println(invoice.getInvoiceNum() + "," + formattedDate + "," + invoice.getCustomerName());
            }

            headerWriter.flush();

            lineWriter = getFileWriter(invoiceLineFilePath);
            ArrayList<LineTable> invoiceItems;

            for (HeaderTable invoice : invoices) {
                invoiceItems = invoice.getLineTable();
                for (LineTable item : invoiceItems) {
                    lineWriter.println(invoice.getInvoiceNum() + "," + item.getItemName() + ","
                            + String.valueOf(item.getItemPrice()) + "," + String.valueOf(item.getCount()));
                }

            }

            lineWriter.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            headerWriter.close();
            lineWriter.close();
        }

    }

    public static void writeFile(ArrayList<HeaderTable> invoices, String invoicesFilePath, String itemsFilePath) {

        PrintWriter headerWriter = null;
        PrintWriter lineWriter = null;

        try {

            headerWriter = getFileWriter(invoicesFilePath);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (HeaderTable invoice : invoices) {
                String formattedDate = sdf.format(invoice.getInvoiceDate());
                headerWriter.println(invoice.getInvoiceNum() + "," + formattedDate + "," + invoice.getCustomerName());
            }

            headerWriter.flush();

            lineWriter = getFileWriter(itemsFilePath);
            ArrayList<LineTable> invoiceItems;

            for (HeaderTable invoice : invoices) {
                invoiceItems = invoice.getLineTable();
                for (LineTable item : invoiceItems) {
                    lineWriter.println(invoice.getInvoiceNum() + "," + item.getItemName() + ","
                            + String.valueOf(item.getItemPrice()) + "," + String.valueOf(item.getCount()));
                }

            }

            lineWriter.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            headerWriter.close();
            lineWriter.close();
        }

    }

    // Helper Method to organize the code in a good way ( Retrieve items for each invoice)
    private static ArrayList<LineTable> getItemsForInvoice(HeaderTable invoice, String filePath) {
        ArrayList<LineTable> invoiceItems = new ArrayList<>();
        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                int num = Integer.parseInt(row[0]);
                if (num == invoice.getInvoiceNum()) {
                    invoiceItems.add(new LineTable(invoice, row[1],
                            Double.parseDouble(row[2]), Integer.parseInt(row[3])));
                }

            }
        } catch (FileNotFoundException ex) {
            ex = new FileNotFoundException("File is not found with this path");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return invoiceItems;
    }

    // Helper Method to organize the code in a good way ( Retrieve writer for a specifc file path)
    private static PrintWriter getFileWriter(String filePath) throws IOException {

        return new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
    }

    private void loadFile() {

        String invoicesFilePath = null;
        String itemsFilePath = null;

        JFileChooser fc1 = new JFileChooser();
        if (fc1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            invoicesFilePath = fc1.getSelectedFile().getPath();
            boolean suffix = invoicesFilePath.endsWith("csv");
            if (!suffix) {
                System.out.println("Please, Enter invoice Files with CSV Extension");
                return;
            }
            System.out.println("Invoices CSV File is selected successfully");
            System.out.println("--------------------------------------");
        }

        JFileChooser fc2 = new JFileChooser();
        if (fc2.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            itemsFilePath = fc2.getSelectedFile().getPath();
            boolean suffix = itemsFilePath.endsWith("csv");
            if (!suffix) {
                System.out.println("Please, Enter invoice File with CSV Extension");
                return;
            }
            System.out.println("Invoice's items CSV file is selected successfully");
            System.out.println("--------------------------------------");
        }

        if (invoicesFilePath != null && itemsFilePath != null) {
            SigInvoiceFrame.invoices = readFile(invoicesFilePath, itemsFilePath);
            Object[][] table1Data = getInvoiceTableData(SigInvoiceFrame.invoices);
            SigInvoiceFrame.invoicesTable.setModel(new DefaultTableModel(table1Data,
                    new String[]{"No.", "Date", "Customer", "Total"}));

            for (HeaderTable invoice : SigInvoiceFrame.invoices) {
                System.out.println(invoice);
                System.out.println("--------------------------------------");
            }
        } else {
            System.out.println("You should select two CSV files");
            System.out.println("--------------------------------------");
        }

    }

    private void saveFile() {

        String invoicesFilePath = null;
        String itemsFilePath = null;

        JFileChooser fc1 = new JFileChooser();
        if (fc1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            invoicesFilePath = fc1.getSelectedFile().getPath();
            boolean suffix = invoicesFilePath.endsWith("csv");
            if (!suffix) {
                System.out.println("Files must be CSV file only");
                return;
            }
            System.out.println("Invoices File is selected successfully");
            System.out.println("--------------------------------------");
        }

        JFileChooser fc2 = new JFileChooser();
        if (fc2.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            itemsFilePath = fc2.getSelectedFile().getPath();
            boolean suffix = itemsFilePath.endsWith("csv");
            if (!suffix) {
                System.out.println("Files must be CSV file only");
                return;
            }
            System.out.println("Invoice's items file is selected");
            System.out.println("******************************************");
        }

        if (invoicesFilePath != null && itemsFilePath != null) {
            writeFile(SigInvoiceFrame.invoices, invoicesFilePath, itemsFilePath);

        } else {
            System.out.println("You must select two files");
            System.out.println("*************************************************");
        }
    }

    private void createNewInvoice() {

        invoiceDialog = new AddNewInvoiceDialog(null, true);
        invoiceDialog.setVisible(true);

        int invoiceNum = SigInvoiceFrame.invoices.size() + 1;
        String dateString = invoiceDialog.getInvoiceDate();
        String customerName = invoiceDialog.getCustomerName();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            if (dateString != null && !(customerName.isEmpty())) {
                Date invoiceDate = df.parse(dateString);
                HeaderTable invoice = new HeaderTable(invoiceNum, invoiceDate, customerName);
                SigInvoiceFrame.invoices.add(invoice);
                Object[][] invoiceTableData = getInvoiceTableData(SigInvoiceFrame.invoices);
                SigInvoiceFrame.invoicesTable.setModel(new DefaultTableModel(invoiceTableData,
                        new String[]{"No.", "Date", "Customer", "Total"}));
            } else {
                System.out.println("You must insert date and customer name");
            }

        } catch (ParseException ex) {
            System.out.println("Incorrecet Date Format ====> It need to be (dd-MM-yyyy) ");
            System.out.println("***********************************************************");
        }

    }

    private void deleteInvoice() {
        if (SigInvoiceFrame.invoicesTable.getSelectedRow() >= 0)
        {
            SigInvoiceFrame.invoices.remove(SigInvoiceFrame.invoicesTable.getSelectedRow());
            //coder : for loop invoices (arralist) .size ->  new num
            for(int i = 0 ; i < SigInvoiceFrame.invoices.size();i++)
            {
                SigInvoiceFrame.invoices.get(i).setInvoiceNum(i+1);
            }
            Object[][] invoiceTableData = getInvoiceTableData(SigInvoiceFrame.invoices);
            SigInvoiceFrame.invoicesTable.setModel(new DefaultTableModel(invoiceTableData,
                    new String[]{"No.", "Date", "Customer", "Total"}));
            SigInvoiceFrame.invoiceNumLbl.setText("0");
            SigInvoiceFrame.invoiceDateTxtField.setText("");
            SigInvoiceFrame.customerNameTxtField.setText("");
            SigInvoiceFrame.invoiceTotalLbl.setText("0.0");
            SigInvoiceFrame.itemsTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{
                            {null, null, null, null, null},
                            {null, null, null, null, null},
                            {null, null, null, null, null},
                            {null, null, null, null, null},
                            {null, null, null, null, null}
                    },
                    new String[]{
                            "No.", "Item Name", "Item Price", "Count", "Item Total"
                    }
            ));
        } else {
            System.out.println("Select Invoice First");
            System.out.println("*****************************");
        }
    }

    private void saveItem() {

        itemDialog = new AddNewItemDialog(null, true);
        itemDialog.setVisible(true);

        int selectedRow = SigInvoiceFrame.invoicesTable.getSelectedRow();
        if (selectedRow >= 0) {
            HeaderTable invoice = SigInvoiceFrame.invoices.get(selectedRow);
            ArrayList<LineTable> invoiceItems = invoice.getLineTable();
            if (invoiceItems == null) {
                invoiceItems = new ArrayList<>();
                invoice.setLineTable(invoiceItems);
            }
            LineTable item = new LineTable(invoice, itemDialog.getItemName(),
                    itemDialog.getItemPrice(), itemDialog.getCount());
            if (!((itemDialog.getName()).isEmpty()) && itemDialog.getItemPrice() > 0.0 && itemDialog.getCount() > 0) {
                invoiceItems.add(item);
            }

            double total = 0.0;
            for (LineTable invoiceItem : invoiceItems) {
                total += invoiceItem.getItemPrice() * invoiceItem.getCount();
            }

            SigInvoiceFrame.invoiceTotalLbl.setText(String.valueOf(total));

            Object[][] table2Data = getInvoiceItemsTableData(invoiceItems);
            SigInvoiceFrame.itemsTable.setModel(new DefaultTableModel(table2Data,
                    new String[]{"No.", "Item Name", "Item Price", "Count", "Item Total"}));

            Object[][] table1Data = getInvoiceTableData(SigInvoiceFrame.invoices);
            SigInvoiceFrame.invoicesTable.setModel(new DefaultTableModel(table1Data,
                    new String[]{"No.", "Date", "Customer", "Total"}));
        } else {
            System.out.println("Please, Select Invoice First");
            System.out.println("------------------------------------------");
        }
    }

    private void deleteItem() {
        int selectedRowInInvoiceTable = SigInvoiceFrame.invoicesTable.getSelectedRow();
        if (selectedRowInInvoiceTable >= 0) {

            HeaderTable invoice = SigInvoiceFrame.invoices.get(selectedRowInInvoiceTable);
            ArrayList<LineTable> items = invoice.getLineTable();
            if (items == null) {
                items = new ArrayList<>();
                invoice.setLineTable(items);
            }
            int selectedRowInItemsTable = SigInvoiceFrame.itemsTable.getSelectedRow();
            if (selectedRowInItemsTable >= 0) {
                items.remove(selectedRowInItemsTable);
                double total = 0.0;
                for (LineTable invoiceItem : items) {
                    total += invoiceItem.getItemPrice() * invoiceItem.getCount();
                }

                SigInvoiceFrame.invoiceTotalLbl.setText(String.valueOf(total));

                Object[][] table2Data = getInvoiceItemsTableData(items);
                SigInvoiceFrame.itemsTable.setModel(new DefaultTableModel(table2Data,
                        new String[]{"No.", "Item Name", "Item Price", "Count", "Item Total"}));

                Object[][] table1Data = getInvoiceTableData(SigInvoiceFrame.invoices);
                SigInvoiceFrame.invoicesTable.setModel(new DefaultTableModel(table1Data,
                        new String[]{"No.", "Date", "Customer", "Total"}));

                System.out.println("The Item is Deleted Successfully");
                System.out.println("---------------------------------------");

            } else {
                System.out.println("Please, Select Invoice and Item in the same time");
                System.out.println("------------------------------------------------");
            }
        } else {
            System.out.println("PLease, Select Invoice and Item in the same time");
            System.out.println("--------------------------------------------------");
        }
    }


    // Helper Methods
    private Object[][] getInvoiceTableData(ArrayList<HeaderTable> invoices) {

        Object[][] tableData = new Object[invoices.size()][4];
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < invoices.size(); i++) {
            tableData[i][0] = invoices.get(i).getInvoiceNum();
            tableData[i][1] = sdf.format(invoices.get(i).getInvoiceDate());
            tableData[i][2] = invoices.get(i).getCustomerName();
            double total = 0.0;
            if (invoices.get(i).getLineTable() != null) {
                for (LineTable item : invoices.get(i).getLineTable()) {
                    total += item.getItemPrice() * item.getCount();
                }
            }

            tableData[i][3] = total;

        }

        return tableData;
    }

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
