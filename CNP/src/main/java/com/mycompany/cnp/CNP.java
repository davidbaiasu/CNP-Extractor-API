package com.mycompany.cnp;

public class CNP {
    
    private String gender;
    private String birthDate;
    private String countyName;
    private String nationality;
    private String serialCode;
    private boolean isValid;
    
    public CNP() {}

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getCountyName() { return countyName; }
    public void setCountyName(String countyName) { this.countyName = countyName; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public String getSerialCode() { return serialCode; }
    public void setSerialCode(String serialCode) { this.serialCode = serialCode; }

    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { isValid = valid; }
    
}