package com.barclays.indiacp.reference.data.entities;

import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Legal_Entity_ref_data")
public class LegalEntity
{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "legal_entity_id")
    Integer legal_entity_id;

    @Column(name = "legal_entity_name")
    String legal_entity_name;

    @Column(name = "CIN")
    String CIN;

    @Column(name = "registered_address")
    String registered_address;

    @Column(name = "official_contact_number")
    String official_contact_number;

    @Column(name = "official_fax_number")
    String official_fax_number;

    @Column(name = "official_email")
    String official_email;

    @Column(name = "official_website")
    String official_website;

    @Column(name = "entity_type")
    String entity_type;

    @Column(name = "contact_person")
    String contact_person;

    @Transient
    private Collection<UserDetails> userDetails;


    @Transient
    private Collection<SettlementDetails> settlementDetails;

    //@ManyToOne(mappedBy="person_id", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    //private Collection<UserDetails> userDetails;

    public LegalEntity() {
    }

    public LegalEntity(

            String legal_entity_name,
            String CIN,
            String registered_address,
            String official_contact_number,
            String official_fax_number,
            String official_email,
            String official_website,
            String entity_type,
            String contact_person
            )
    {

        this.legal_entity_name=legal_entity_name;
        this.CIN=CIN;
        this.registered_address=registered_address;
        this.official_contact_number=official_contact_number;
        this.official_fax_number=official_fax_number;
        this.official_email=official_email;
        this.official_website=official_website;
        this.entity_type=entity_type;
        this.contact_person=contact_person;
    }


    public Integer getLegal_entity_id() {
        return legal_entity_id;
    }



    public String getLegal_entity_name() {
        return legal_entity_name;
    }

    public void setLegal_entity_name(String legal_entity_name) {
        this.legal_entity_name = legal_entity_name;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

    public String getRegistered_address() {
        return registered_address;
    }

    public void setRegistered_address(String registered_address) {
        this.registered_address = registered_address;
    }

    public String getOfficial_contact_number() {
        return official_contact_number;
    }

    public void setOfficial_contact_number(String official_contact_number) {
        this.official_contact_number = official_contact_number;
    }

    public String getOfficial_fax_number() {
        return official_fax_number;
    }

    public void setOfficial_fax_number(String official_fax_number) {
        this.official_fax_number = official_fax_number;
    }

    public String getOfficial_email() {
        return official_email;
    }

    public void setOfficial_email(String official_email) {
        this.official_email = official_email;
    }

    public String getOfficial_website() {
        return official_website;
    }

    public void setOfficial_website(String official_website) {
        this.official_website = official_website;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
    }


    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public Collection<UserDetails> getUserDetails() {

        return userDetails;
    }

    public void setUserDetails(Collection<UserDetails> userDetails) {
        this.userDetails = userDetails;
    }

    public Collection<SettlementDetails> getSettlementDetails() {
        return settlementDetails;
    }

    public void setSettlementDetails(Collection<SettlementDetails> settlementDetails) {
        this.settlementDetails = settlementDetails;
    }

    @Override
    public String toString() {

        String ud = "";

        if(userDetails!=null) {
            for (UserDetails u : userDetails) {
                ud += u.toString();
            }
        }

        String sd = "";

        if(settlementDetails!=null) {
            for (SettlementDetails s : settlementDetails) {
                sd += s.toString();
            }
        }

        String temp =
        "LegalEntity{" +
                "legal_entity_id=" + legal_entity_id +
                ", legal_entity_name='" + legal_entity_name + '\'' +
                ", CIN='" + CIN + '\'' +
                ", registered_address='" + registered_address + '\'' +
                ", official_contact_number='" + official_contact_number + '\'' +
                ", official_fax_number='" + official_fax_number + '\'' +
                ", official_email='" + official_email + '\'' +
                ", official_website='" + official_website + '\'' +
                ", entity_type='" + entity_type + '\'' +
                ", contact_person='" + contact_person + '\'' +
                ", userDetails=[" +
                ud+
                "], settlementDetails=[" + sd + "]";
         /*String users;
        for (int i=0, n=userDetails.size(); i < n; i++)
        {
            userDetails
        }*/


         String legalEntityString = temp;

        return legalEntityString;
    }
}