/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sebjef.smartbank.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author JF James
 */
@Entity
@NoArgsConstructor
@Data
@NamedQuery(
        name = "BankAuthor.findAll",
        query = "SELECT a FROM BankAuthor a"
)
@NamedQuery(
        name = "BankAuthor.count",
        query = "SELECT COUNT(a) FROM BankAuthor a"
)
public class BankAuthor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    private String merchantId;
    private String cardNumber;
    private String expiryDate;
    private int amount;

    private boolean authorized;

}
