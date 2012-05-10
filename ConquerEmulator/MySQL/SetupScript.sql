CREATE DATABASE IF NOT EXISTS ConquerBox;

USE ConquerBox;

CREATE  TABLE IF NOT EXISTS `ConquerBox`.`AuthAccounts` (
  `idAuthAccounts` INT NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(16) NOT NULL ,
  `password` VARCHAR(16) NOT NULL ,
  PRIMARY KEY (`idAuthAccounts`) ,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) )
ENGINE = InnoDB