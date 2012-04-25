DROP PROCEDURE IF EXISTS CheckUserExists;

DELIMITER //
CREATE PROCEDURE CheckUserExists
( IN iUsername VARCHAR(255),
  IN iPassword VARCHAR(255),
  OUT oExists BOOL
)
BEGIN
    SELECT EXISTS(SELECT 1
           FROM Login
           WHERE Username = iUsername AND
           Password = iPassword
          )INTO oExists;
END //
DELIMITER ;