package com.ecorp.abhamburger;

public class AuthenticatedUserHolder {
    private Person appUser = null;
    private String Role = null; //customer/ employee /manager

    private AuthenticatedUserHolder() { }

    public static final AuthenticatedUserHolder instance = new AuthenticatedUserHolder();

    public Person getAppUser() { return this.appUser; }

    public void setAppUser(Person user) { this.appUser = user; }

    public String getRole(){return Role;}

    public void setRole(String Role){this.Role = Role;}

}
