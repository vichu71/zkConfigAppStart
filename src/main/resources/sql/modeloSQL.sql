CREATE TABLE dns (
	id int NOT NULL AUTO_INCREMENT,
	name varchar(255) NOT NULL,
	dntypecode char(1) NOT NULL,
	mediacode char(1) NOT NULL,
	remotepeer varchar(255) NULL,
	domid int NOT NULL,
	nodeid varchar(100) NULL,
	CONSTRAINT s_dns_pkey PRIMARY KEY (id)
);

CREATE TABLE dn_device (
	id int NOT NULL AUTO_INCREMENT,
	dnid int NOT NULL,
	devid int NOT NULL,
	CONSTRAINT s_dn_device_pkey PRIMARY KEY (id)
);

CREATE TABLE addins_dev (
	id int NOT NULL AUTO_INCREMENT,
	name varchar(255) NOT NULL,
	devgroup varchar(255) NOT NULL,
	media char(1) NOT NULL,
	CONSTRAINT s_addins_dev_pkey PRIMARY KEY (id)
);