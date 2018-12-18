package com.ecorp.abhamburger;

public class Person  {
     String firstName;
     String lastName;
     String email;
     String password;


     public Person(String firstName, String lastName, String email, String password) {
          this.firstName = firstName;
          this.lastName = lastName;
          this.email = email;
          this.password = password;
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

     public String getPassword() {
          return password;
     }

     public void setPassword(String password) {
          this.password = password;
     }


     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }


}
