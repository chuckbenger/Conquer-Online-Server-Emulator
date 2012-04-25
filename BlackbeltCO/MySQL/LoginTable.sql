CREATE  TABLE IF NOT EXISTS `BlackBeltCo`.`Login` (
  `idLogin` INT NOT NULL AUTO_INCREMENT ,
  `Username` VARCHAR(15) NOT NULL ,
  `Password` VARCHAR(15) NOT NULL ,
  `Email` VARCHAR(45) NOT NULL ,
  `Email_Verification` VARCHAR(45) NULL ,
  `Email_Status` VARCHAR(45) NOT NULL ,
  `Question` VARCHAR(100) NOT NULL ,
  `Answer` VARCHAR(100) NULL ,
  PRIMARY KEY (`idLogin`) ,
  UNIQUE INDEX `Username_UNIQUE` (`Username` ASC) )
ENGINE = InnoDB