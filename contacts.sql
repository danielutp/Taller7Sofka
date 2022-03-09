CREATE SCHEMA IF NOT EXISTS contacts DEFAULT CHARACTER SET utf8 ;
USE contacts ;

-- -----------------------------------------------------
-- Table contacts
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS contacts (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  phone VARCHAR(14) NOT NULL,
  email VARCHAR(64) NULL,
  date_of_birth DATE NULL,
  create_at DATE NULL,
  update_at DATE NULL,
  delete_at DATE NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX phone_UNIQUE (phone ASC, name ASC) VISIBLE)
ENGINE = InnoDB;

select * from contacts;

insert into contacts(id,name,phone,email,date_of_birth,create_at,update_at,delete_at)
value(1,'Daniel','3145243523','danistcruz@gmail.com',"1995-08-27",null,null,null);
insert into contacts(id,name,phone,email,date_of_birth,create_at,update_at,delete_at)
value(2,'Carlos','3145243524','carlos@gmail.com',"1967-12-30",null,null,null);
insert into contacts(id,name,phone,email,date_of_birth,create_at,update_at,delete_at)
value(3,'Monica','3145243525','monica@gmail.com',"1966-05-30",null,null,null);
