package com.example.ziqing2_subbook;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jasongao on 2018-02-03.
 */

public class Subscription {
    private String name;
    private Date date;
    private Double monthly_charge;
    private String comment;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd");


    public Subscription(String name, Date date, Double monthly_charge, String comment) {
        this.name = name;
        this.date = date;
        this.monthly_charge = monthly_charge;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getMonthly_charge() {
        return monthly_charge;
    }

    public void setMonthly_charge(Double monthly_charge) {
        this.monthly_charge = monthly_charge;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getDateString(){
        return format.format(date);
    }
    @Override
    public String toString() {
        return "Name: " + name + "\n"+
                "Date: " + format.format(date) + "\n"+
                "Monthly Charge: " + monthly_charge.toString()+ "\n" +
                "Comment: " + comment;
    }
}
