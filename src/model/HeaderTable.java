package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HeaderTable {
    private int invoiceNum;
    private Date invoiceDate;
    private String customerName;
    private ArrayList<LineTable> invoiceLines;
    public HeaderTable(int invoiceNum, Date invoiceDate, String customerName) {
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
    }

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public ArrayList<LineTable> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(ArrayList<LineTable> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(invoiceDate);
        return "HeaderTable{" + "invoiceNum=" + invoiceNum + ", invoiceDate="
                + date + ", customerName=" + customerName
                + ",\ninvoiceLines=" + invoiceLines + '}';
    }

}

