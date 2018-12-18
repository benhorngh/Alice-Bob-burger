package com.ecorp.abhamburger;

import java.util.Date;

public class Person  {
     String firstName;
     String lastName;
     String email;
     String password;
     Date birthday;

     public Person(String firstName, String lastName, String email, String password, Date birthday) {
          this.firstName = firstName;
          this.lastName = lastName;
          this.email = email;
          this.password = password;
          this.birthday = birthday;
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

     public Date getBirthday() {
          return birthday;
     }

     public void setBirthday(Date birthday) {
          this.birthday = birthday;
     }


     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }


}
