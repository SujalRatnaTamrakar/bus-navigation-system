package com.example.chirag.navigationdrawer;

public class Driver {
    private String FirstName;
    private String LastName;
    private String Email;
    private Long PhoneNumber;
    private String BusNo;
    private  String BusRoute;
    private String dob;
    private String Password1;
    private String Password2;
    private int usertype;

    public Driver(){

    }

    public Driver(String firstName, String lastName, String email, Long phoneNumber, String busNo, String busRoute, String dob, String password1, String password2, int usertype) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        PhoneNumber = phoneNumber;
        BusNo = busNo;
        BusRoute = busRoute;
        this.dob = dob;
        Password1 = password1;
        Password2 = password2;
        this.usertype = usertype;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Long getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getBusNo() {
        return BusNo;
    }

    public void setBusNo(String busNo) {
        BusNo = busNo;
    }

    public String getBusRoute() {
        return BusRoute;
    }

    public void setBusRoute(String busRoute) {
        BusRoute = busRoute;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword1() {
        return Password1;
    }

    public void setPassword1(String password1) {
        Password1 = password1;
    }

    public String getPassword2() {
        return Password2;
    }

    public void setPassword2(String password2) {
        Password2 = password2;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }
}
