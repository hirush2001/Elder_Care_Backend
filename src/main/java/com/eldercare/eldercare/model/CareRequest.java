package com.eldercare.eldercare.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "careRequest")

public class CareRequest {

    @Id
    @Column(name = "Req_Id", nullable = false, unique = true)
    private String requestId;
    @Column(name = "Req_Date")
    private String requestDate;
    @Column(name = "Status")
    private String status;
    @Column(name = "Care_Id")
    private String careId;

    // 🔹 Many care requests belong to one Elder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Elder_Id", referencedColumnName = "Elder_Id")
    @JsonIgnore
    private Elder elder;

    // 🔹 Many care requests can be assigned to one CareGiver
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Care_Taker_Id", referencedColumnName = "Elder_Id")
    @JsonIgnore
    private Elder careGiver;

    public CareRequest(String requestId, String requestDate, String status, String careId) {
        this.requestId = requestId;
        this.requestDate = requestDate;
        this.status = status;
        this.careId = careId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCId() {
        return careId;
    }

    public void setCId(String careId) {
        this.careId = careId;
    }

}
