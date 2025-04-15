package com.example.application.data.orders;

public class OfficeMasterDto {
    private String officeName;
    private String address;
    private String contacts;
    private String employeeName;
    private String position;

    public OfficeMasterDto(
            String officeName,
            String address,
            String contacts,
            String employeeName,
            String position
    ) {
        this.officeName = officeName;
        this.address = address;
        this.contacts = contacts;
        this.employeeName = employeeName;
        this.position = position;
    }

    // Getters
    public String getOfficeName() { return officeName; }
    public String getAddress() { return address; }
    public String getContacts() { return contacts; }
    public String getEmployeeName() { return employeeName; }
    public String getPosition() { return position; }
}