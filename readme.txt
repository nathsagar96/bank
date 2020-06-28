REST End Points Details.

1. Add new account, enter customer details too
POST :/bank/account

2. Update Customer Details
PUT :/bank/customer/{customerId}

3. Get Single Customer Detail along with Account Details
GET :/bank/customer/{customerId}

4. Get All Customers Details along with account Details
GET :/bank/customers

5. Transfer funds between two accounts
POST :/bank/transfer

6: Get Balance Info (Account Info including customer details)
GET :/bank/balance/{accountId}

*** Attached POSTMAN Collection with working Requests.