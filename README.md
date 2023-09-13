# Wallet
This is an example simple REST API application using Spring Boot to manage customer balances and transactions(top-up, transfer, and refund) that will be called from a mobile application.

## Technology
Technology / Library / Framework used:
* Java 17
* Spring Boot
* Maven
* Junit 
* Mockito
* Spring Data JPA
* Spring MVC
* spring Validation
* H2 (In memory database)
* Lombok

## Entity Relationship Diagram
![wallet-erd.png](..%2F..%2F..%2FPictures%2Fwallet-erd.png)

## Use Cases / API Specification

### Register Account
- Endpoint : POST {host}/api/accounts
- Description: This endpoint is used to create a new customer account. 
- Request Body :
```json
{
  "username": "username",
  "password": "password",
  "accountHolderName": "Full Name"
}
```
- Response Body (Success):
```json
{
  "data": "OK",
  "errors": null,
  "paging": null
}
```
- Response Body (Failed) to all use cases:
```json
{
  "data": null,
  "errors": "error message",
  "paging": null
}
```

### Login Account
- Endpoint : POST {host}/api/auth/login
- Description: This endpoint is used for user authentication and get token. The token can then be used for accessing protected resources and making authenticated requests to the application.
- Request Body :
```json
{
  "username": "username",
  "password": "password"
}
```
- Response Body (Success):
```json
{
  "data": "TOKEN",
  "errors": null,
  "paging": null
}
```

### Get Current Account Detail
- Endpoint : Get {host}/api/accounts/current
- Description: This endpoint is used for user to view their current balance information.
- Header :
    * X-API-TOKEN : {TOKEN}
- Response Body (Success):
```json
{
  "data": {
    "accountNumber": 9893876597,
    "accountHolderName": "Test User",
    "balance": 0.00
  },
  "errors": null,
  "paging": null
}
```

### Post Transaction TopUp
- Endpoint : Post /api/transactions/top-up
- Description: This endpoint is used for user to top up the balance.
- Header :
    * X-API-TOKEN : {TOKEN}
- Request Body :
```json
{
  "amount": 100000.00,
  "sourceAccountNumber": 1234567890,
  "destinationAccountNumber": 1234567890,
  "description": "notes"
}
```
- Response Body (Success):
```json
{
  "data": "OK",
  "errors": null,
  "paging": null
}
```

### Post Transaction Transfer
- Endpoint : Post /api/transactions/top-up
- Description: This endpoint is used for user to transfer money to other account.
- Header :
    * X-API-TOKEN : {TOKEN}
- Request Body :
```json
{
  "destinationAccountNumber": 5235196612,
  "amount": 10000,
  "referenceNumber": "REF-002",
  "description": "gift"
}
```
- Response Body (Success):
```json
{
  "data": "OK",
  "errors": null,
  "paging": null
}
```

### Post Transaction Refund
- Endpoint : Post /api/transactions/top-up
- Description: This endpoint is used for user to refund money.
- Header :
    * X-API-TOKEN : {TOKEN}
- Request Body :
```json
{
  "amount": 10000.00,
  "sourceAccountNumber": 5235196612,
  "destinationAccountNumber": 1234567890,
  "description": "cancel"
}
```
- Response Body (Success):
```json
{
  "data": "OK",
  "errors": null,
  "paging": null
}
```

### Get Account Transactions History
- Endpoint : Get /api/customers/current/transactions
- Description: This endpoint is used for user to view all transaction histories in his account.
- Header :
    * X-API-TOKEN : {TOKEN}
- Query Param:
    * startTimeEpoch: long optional  (unix epoch time in millisecond)
    * endTimeEpoch: long  optional (unix epoch time in millisecond)
    * currentPage: int optional (default 0)
    * pageSize: int optional  (default 10)
- Response Body (Success):
```json
{
  "data": [
    {
      "type": "TOP_UP",
      "amount": 100000.00,
      "transactionTimeEpoch": 1694627293823,
      "referenceNumber": "REF-001",
      "description": "top up dari transfer Bank"
    },
    {
      "type": "TRANSFER",
      "amount": 10000.00,
      "transactionTimeEpoch": 1694627459943,
      "referenceNumber": "REF-002",
      "description": "transfer"
    },
    {
      "type": "REFUND",
      "amount": 10000.00,
      "transactionTimeEpoch": 1694627561121,
      "referenceNumber": "REF-003",
      "description": "refund"
    }
  ],
  "errors": null,
  "paging": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPage": 1
  }
}
```

## How to Use
**1. Clone the application**
```bash
git clone https://github.com/charlybutar21/wallet.git
```

**2. Build the application**
```bash
mvn clean install
```

**3. Run the spring boot application**

```bash
java -jar target/wallet-0.0.1-SNAPSHOT.jar
```

**4. Access H2 Database GUI with open http://localhost:8080/h2-console**

**5. Open file manual.http and run test cases**
