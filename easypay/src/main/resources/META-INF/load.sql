
insert into posref(posId, location, active) values ('POS-01', 'Vallauris France', true)
insert into posref(posId, location, active) values ('POS-02', 'Blois France', true)
insert into posref(posId, location, active) values ('POS-03', 'Paris France', false)
insert into posref(posId, location, active) values ('POS-04', 'San Francisco US', true)

## VISA
insert into cardref(cardNumber, cardType, blackListed) values ('4485248221242242', 'VISA', true)
## MCARD
insert into cardref(cardNumber, cardType, blackListed) values ('5261749597812879', 'MASTERCARD', true)
## Discover
insert into cardref(cardNumber, cardType, blackListed) values ('6011191990123805', 'DISCOVER', true)
## AMEX
insert into cardref(cardNumber, cardType, blackListed) values ('343506189778618', 'AMERICAN_EXPRESS', true)
## DINERS
insert into cardref(cardNumber, cardType, blackListed) values ('36031319313683', 'DINERS_CLUB', true)
## China Union Pay
insert into cardref(cardNumber, cardType, blackListed) values ('6289193933258511', 'CHINA_UNION_PAY', true)