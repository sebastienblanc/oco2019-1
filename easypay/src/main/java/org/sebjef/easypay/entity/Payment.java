/*
 * Copyright 2019 JF James.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sebjef.easypay.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;




/**
 *
 * @author JF James
 */
@Entity
public class Payment extends PanacheEntity {


    private LocalDateTime dateTime;
    
    private long reponseTime;

    private String posId;
    private String cardNumber;
    private String expiryDate;
    private int amount;

    @Enumerated(EnumType.STRING)
    private CardType cardType;
    
    @Enumerated(EnumType.STRING)
    private PaymentResponseCode responseCode;

    @Enumerated(EnumType.STRING)
    private ProcessingMode processingMode;
    
    private boolean bankCalled;
    private boolean authorized;
    private Long authorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public long getReponseTime() {
        return reponseTime;
    }

    public void setReponseTime(long reponseTime) {
        this.reponseTime = reponseTime;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public PaymentResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(PaymentResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public ProcessingMode getProcessingMode() {
        return processingMode;
    }

    public void setProcessingMode(ProcessingMode processingMode) {
        this.processingMode = processingMode;
    }

    public boolean isBankCalled() {
        return bankCalled;
    }

    public void setBankCalled(boolean bankCalled) {
        this.bankCalled = bankCalled;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
