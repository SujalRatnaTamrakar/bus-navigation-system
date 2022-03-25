package com.project.of.busnavigationsystem.NavBar.Profile;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
    private String password1;
    private String password2;
    private String dob;
    private int usertype;

    public User() {
    }

    public User(String firstName, String lastName, String email, Long phoneNumber, String password1, String password2, int usertype, String dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password1 = password1;
        this.password2 = password2;
        this.dob = dob;
        this.usertype = usertype;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getphoneNumber() {
        return phoneNumber;
    }

    public void setphoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }
}
