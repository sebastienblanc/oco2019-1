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
package org.sebjef.merchantbo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
        name = "Payment.count",
        query = "SELECT COUNT(p) FROM Payment p"
)
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    
    boolean bankCalled;
    boolean authorized;
    
    // Optional ???
    private Long authorId;

}
