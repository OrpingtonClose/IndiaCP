package com.barclays.indiacp.reference.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "User_Details_Ref_Data")
public class UserDetails
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    String person_id;

    @Column(name = "person_name")
    String person_name;

    @Column(name = "dept")
    String dept;

    @Column(name = "officer_address")
    String officer_address;

    @Column(name = "officer_contact_number")
    String officer_contact_number;

    @Column(name = "officer_email")
    String officer_email;


    @Column(name = "legal_entity_id")
    Integer legal_entity_id;


    public UserDetails()
    {}

    public UserDetails(
            Integer legal_entity_id, String person_name, String dept, String officer_address, String officer_contact_number, String officer_email
    ) {
        this.legal_entity_id = legal_entity_id;
        this.person_name = person_name;
        this.dept = dept;
        this.officer_address = officer_address;
        this.officer_contact_number = officer_contact_number;
        this.officer_email = officer_email;
    }


    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getOfficer_address() {
        return officer_address;
    }

    public void setOfficer_address(String officer_address) {
        this.officer_address = officer_address;
    }

    public String getOfficer_contact_number() {
        return officer_contact_number;
    }

    public void setOfficer_contact_number(String officer_contact_number) {
        this.officer_contact_number = officer_contact_number;
    }

    public String getOfficer_email() {
        return officer_email;
    }

    public void setOfficer_email(String officer_email) {
        this.officer_email = officer_email;
    }

    public Integer getLegal_entity_id() {
        return legal_entity_id;
    }

    public void setLegal_entity_id(Integer legal_entity_id) {
        this.legal_entity_id = legal_entity_id;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "person_id='" + person_id + '\'' +
                ", person_name='" + person_name + '\'' +
                ", dept='" + dept + '\'' +
                ", officer_address='" + officer_address + '\'' +
                ", officer_contact_number='" + officer_contact_number + '\'' +
                ", officer_email='" + officer_email + '\'' +
                '}';
    }
}
