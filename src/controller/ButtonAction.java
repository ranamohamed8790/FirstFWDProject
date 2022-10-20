package controler;

import model.InvoiceHeader;
import model.InvoiceLines;
import view.InvoiceFrame;
import view.NewInvoiceDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActionHandler implements ActionListener {

    // == Fields ==
    NewInvoiceDialog invoiceDialog;
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
        }

    }

    public static ArrayList<InvoiceHeader> readFile() {
        ArrayList<InvoiceHeader> invoices = new ArrayList<>();
        String invoiceHeaderFilePath = "resources\\InvoiceHeader.csv";
        String invoiceLineFilePath = "resources\\InvoiceLine.csv";
        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(invoiceHeaderFilePath));
            while ((line = reader.readLine()) != null) {
                InvoiceHeader invoice;
                String[] row = line.split(",");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(row[1]);
                invoice = new InvoiceHeader(Integer.parseInt(row[0]), date, row[2]);
                ArrayList<InvoiceLines> invoiceItems = getItemsForInvoice(invoice, invoiceLineFilePath);
                invoice.setInvoiceLines(invoiceItems);
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

    public static ArrayList<InvoiceHeader> readFile(String invoicesFilePath, String itemsFilePath) {
        ArrayList<InvoiceHeader> invoices = new ArrayList<>();

        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(invoicesFilePath));
            while ((line = reader.readLine()) != null) {
                InvoiceHeader invoice;
                String[] row = line.split(",");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(row[1]);
                invoice = new InvoiceHeader(Integer.parseInt(row[0]), date, row[2]);
                ArrayList<InvoiceLines> invoiceItems = getItemsForInvoice(invoice, itemsFilePath);
                invoice.setInvoiceLines(invoiceItems);
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

    public static void writeFile(ArrayList<InvoiceHeader> invoices) {

        String invoiceHeaderFilePath = "resources\\Invoices.csv";
        String invoiceLineFilePath = "resources\\InvoiceItems.csv";
        PrintWriter headerWriter = null;
        PrintWriter lineWriter = null;

        try {

            headerWriter = getFileWriter(invoiceHeaderFilePath);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (InvoiceHeader invoice : invoices) {
                String formattedDate = sdf.format(invoice.getInvoiceDate());
                headerWriter.println(invoice.getInvoiceNum() + "," + formattedDate + "," + invoice.getCustomerName());
            }

            headerWriter.flush();

            lineWriter = getFileWriter(invoiceLineFilePath);
            ArrayList<InvoiceLines> invoiceItems;

            for (InvoiceHeader invoice : invoices) {
                invoiceItems = invoice.getInvoiceLines();
                for (InvoiceLines item : invoiceItems) {
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

    public static void writeFile(ArrayList<InvoiceHeader> invoices, String invoicesFilePath, String itemsFilePath) {

        PrintWriter headerWriter = null;
        PrintWriter lineWriter = null;

        try {

            headerWriter = getFileWriter(invoicesFilePath);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (InvoiceHeader invoice : invoices) {
                String formattedDate = sdf.format(invoice.getInvoiceDate());
                headerWriter.println(invoice.getInvoiceNum() + "," + formattedDate + "," + invoice.getCustomerName());
            }

            headerWriter.flush();

            lineWriter = getFileWriter(itemsFilePath);
            ArrayList<InvoiceLines> invoiceItems;

            for (InvoiceHeader invoice : invoices) {
                invoiceItems = invoice.getInvoiceLines();
                for (InvoiceLines item : invoiceItems) {
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
    private static ArrayList<InvoiceLines> getItemsForInvoice(InvoiceHeader invoice, String filePath) {
        ArrayList<InvoiceLines> invoiceItems = new ArrayList<>();
        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                int num = Integer.parseInt(row[0]);
                if (num == invoice.getInvoiceNum()) {
                    invoiceItems.add(new InvoiceLines(invoice, row[1],
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
                System.out.println("Files must be CSV file only");
                return;
            }
            System.out.println("Invoices CSV File is selected");
            System.out.println("******************************************");
        }

        JFileChooser fc2 = new JFileChooser();
        if (fc2.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            itemsFilePath = fc2.getSelectedFile().getPath();
            boolean suffix = itemsFilePath.endsWith("csv");
            if (!suffix) {
                System.out.println("Files must be CSV file only");
                return;
            }
            System.out.println("Invoice's items CSV file is selected");
            System.out.println("******************************************");
        }

        if (invoicesFilePath != null && itemsFilePath != null) {
            InvoiceFrame.invoices = readFile(invoicesFilePath, itemsFilePath);
            Object[][] table1Data = getInvoiceTableData(InvoiceFrame.invoices);
            InvoiceFrame.invoicesTable.setModel(new DefaultTableModel(table1Data,
                    new String[]{"No.", "Date", "Customer", "Total"}));

            for (InvoiceHeader invoice : InvoiceFrame.invoices) {
                System.out.println(invoice);
                System.out.println("*********************************************");
            }
        } else {
            System.out.println("You must select two CSV files");
            System.out.println("*************************************************");
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
            System.out.println("Invoices File is selected");
            System.out.println("******************************************");
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
            writeFile(InvoiceFrame.invoices, invoicesFilePath, itemsFilePath);

        } else {
            System.out.println("You must select two files");
            System.out.println("*************************************************");
        }
    }

    private void createNewInvoice() {

        invoiceDialog = new NewInvoiceDialog(null, true);
        invoiceDialog.setVisible(true);

        int invoiceNum = InvoiceFrame.invoices.size() + 1;
        String dateString = invoiceDialog.getInvoiceDate();
        String customerName = invoiceDialog.getCustomerName();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            if (dateString != null && !(customerName.isEmpty())) {
                Date invoiceDate = df.parse(dateString);
                InvoiceHeader invoice = new InvoiceHeader(invoiceNum, invoiceDate, customerName);
                InvoiceFrame.invoices.add(invoice);
                Object[][] invoiceTableData = getInvoiceTableData(InvoiceFrame.invoices);
                InvoiceFrame.invoicesTable.setModel(new DefaultTableModel(invoiceTableData,
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
        if (InvoiceFrame.invoicesTable.getSelectedRow() >= 0)
        {
            InvoiceFrame.invoices.remove(InvoiceFrame.invoicesTable.getSelectedRow());
            //coder : for loop invoices (arralist) .size ->  new num
            for(int i = 0 ; i < InvoiceFrame.invoices.size();i++)
            {
                InvoiceFrame.invoices.get(i).setInvoiceNum(i+1);
            }
            Object[][] invoiceTableData = getInvoiceTableData(InvoiceFrame.invoices);
            InvoiceFrame.invoicesTable.setModel(new DefaultTableModel(invoiceTableData,
                    new String[]{"No.", "Date", "Customer", "Total"}));
            InvoiceFrame.invoiceNumLbl.setText("0");
            InvoiceFrame.invoiceDateTxtField.setText("");
            InvoiceFrame.customerNameTxtField.setText("");
            InvoiceFrame.invoiceTotalLbl.setText("0.0");
            InvoiceFrame.itemsTable.setModel(new javax.swing.table.DefaultTableModel(
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


    // Helper Methods
    private Object[][] getInvoiceTableData(ArrayList<InvoiceHeader> invoices) {

        Object[][] tableData = new Object[invoices.size()][4];
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < invoices.size(); i++) {
            tableData[i][0] = invoices.get(i).getInvoiceNum();
            tableData[i][1] = sdf.format(invoices.get(i).getInvoiceDate());
            tableData[i][2] = invoices.get(i).getCustomerName();
            double total = 0.0;
            if (invoices.get(i).getInvoiceLines() != null) {
                for (InvoiceLines item : invoices.get(i).getInvoiceLines()) {
                    total += item.getItemPrice() * item.getCount();
                }
            }

            tableData[i][3] = total;

        }

        return tableData;
    }

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
