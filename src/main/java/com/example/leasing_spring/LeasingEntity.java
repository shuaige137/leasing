package com.example.leasing_spring;

import jakarta.persistence.*;

@Entity
@Table(name = "leasing")
public class LeasingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "client_type")
    private String clientType;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "agreement_n", unique = true)
    private Integer agreementN;
    @Column(name = "inn")
    private String inn;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;

    protected LeasingEntity() {
    }

    public Long getClientId() {
        return this.clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientType() {
        return this.clientType;
    }
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getFullName() {
        return this.fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAgreementN() {
        return this.agreementN;
    }
    public void setAgreementN(Integer agreementN) {
        this.agreementN = agreementN;
    }

    public String getInn() {
        return this.inn;
    }
    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}