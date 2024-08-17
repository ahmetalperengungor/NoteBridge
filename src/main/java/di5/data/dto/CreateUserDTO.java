package di5.data.dto;

import di5.data.enums.Gender;

import java.util.Date;

public class CreateUserDTO {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String password;
    private String description;
    private  String country;
    private  String city;

    public CreateUserDTO() {}

    public CreateUserDTO(String firstName, String lastName, Date birthDate, Gender gender, String email, String phoneNumber, String password, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.description = description;
        this.country = country;
        this.city = city;
    }

    // getters and setters

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {}
}
