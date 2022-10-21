package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HeaderTable {

    // == Fields ==
    private int invoiceNum;
    private Date invoiceDate;
    private String customerName;
    private ArrayList<LineTable> LineTable;

    // == Constructors ==
    public HeaderTable(int invoiceNum, Date invoiceDate, String customerName) {
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
    }

    // == Getter and Setter Methods ==
    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<LineTable> getLineTable() {
        return LineTable;
    }

    public void setLineTable(ArrayList<LineTable> LineTable) {
        this.LineTable = LineTable;
    }

    // == toString method ==
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(invoiceDate);
        return "HeaderTable{" + "invoiceNum=" + invoiceNum + ", invoiceDate=" + date + ", customerName=" + customerName + ",\nLineTable=" + LineTable + '}';
    }

}

