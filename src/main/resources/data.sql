insert into account(account_id,account_type,balance) values(1,'SAVING',20000);
insert into account(account_id,account_type,balance) values(2,'JOINT',28000);
insert into account(account_id,account_type,balance) values(3,'CURRENT',200000);

insert into customer(customer_id, first_name,last_name,email,account_id) values(1,'Sagar','Nath','sagarnath@example.com',1);
insert into customer(customer_id, first_name,last_name,email,account_id) values(2,'Deepak','Nath','depaknath@example.com',2);
insert into customer(customer_id, first_name,last_name,email,account_id) values(3,'Bhavana','Nath','bahavananath@example.com',2);
insert into customer(customer_id, first_name,last_name,email,account_id) values(4,'Pankaj','Nath','pankajnath@example.com',3);