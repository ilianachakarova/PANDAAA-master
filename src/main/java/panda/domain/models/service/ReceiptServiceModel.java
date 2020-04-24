package panda.domain.models.service;

import panda.domain.entities.Package;
import panda.domain.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReceiptServiceModel {
    private String id;
    private BigDecimal fee;
    private LocalDateTime issuedOn;
    private User recipient;
    private Package aPackage;

    public ReceiptServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public LocalDateTime getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(LocalDateTime issuedOn) {
        this.issuedOn = issuedOn;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }
}
