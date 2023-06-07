-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema PoliPases
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema PoliPases
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `PoliPases` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `PoliPases` ;

-- -----------------------------------------------------
-- Table `PoliPases`.`Equipo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Equipo` (
  `id` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `posicion_id` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Posicion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Posicion` (
  `idPosicion` INT NOT NULL AUTO_INCREMENT,
  `rol` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idPosicion`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Equipo_has_Posicion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Equipo_has_Posicion` (
  `Equipo_id` INT NOT NULL,
  `Posicion_idPosicion` INT NOT NULL,
  `cantidadPermitida` INT NULL DEFAULT NULL,
  PRIMARY KEY (`Equipo_id`, `Posicion_idPosicion`),
  INDEX `fk_Equipo_has_Posicion_Posicion1_idx` (`Posicion_idPosicion` ASC) VISIBLE,
  INDEX `fk_Equipo_has_Posicion_Equipo1_idx` (`Equipo_id` ASC) VISIBLE,
  CONSTRAINT `fk_Equipo_has_Posicion_Equipo1`
    FOREIGN KEY (`Equipo_id`)
    REFERENCES `PoliPases`.`Equipo` (`id`),
  CONSTRAINT `fk_Equipo_has_Posicion_Posicion1`
    FOREIGN KEY (`Posicion_idPosicion`)
    REFERENCES `PoliPases`.`Posicion` (`idPosicion`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Fichaje`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Fichaje` (
  `idFichaje` INT NOT NULL AUTO_INCREMENT,
  `numCamiseta` VARCHAR(45) NULL DEFAULT NULL,
  `fechaHoraFichaje` DATETIME NULL DEFAULT NULL,
  `Equipo_id` INT NOT NULL,
  PRIMARY KEY (`idFichaje`),
  INDEX `fk_Fichaje_Equipo1_idx` (`Equipo_id` ASC) VISIBLE,
  CONSTRAINT `fk_Fichaje_Equipo1`
    FOREIGN KEY (`Equipo_id`)
    REFERENCES `PoliPases`.`Equipo` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Representante`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Representante` (
  `DNI` INT NOT NULL,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  `apellido` VARCHAR(45) NULL DEFAULT NULL,
  `fechaNacimiento` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`DNI`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Jugador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Jugador` (
  `DNI` INT NOT NULL,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  `apellido` VARCHAR(45) NULL DEFAULT NULL,
  `fechaNacimiento` DATE NULL DEFAULT NULL,
  `salario` DECIMAL(10,2) NULL DEFAULT NULL,
  `Representante_DNI` INT NULL DEFAULT NULL,
  `Posicion_idPosicion` INT NOT NULL,
  `Fichaje_idFichaje` INT NOT NULL,
  PRIMARY KEY (`DNI`),
  INDEX `Representante_DNI` (`Representante_DNI` ASC) VISIBLE,
  INDEX `fk_Jugador_Posicion1_idx` (`Posicion_idPosicion` ASC) VISIBLE,
  INDEX `fk_Jugador_Fichaje1_idx` (`Fichaje_idFichaje` ASC) VISIBLE,
  CONSTRAINT `fk_Jugador_Fichaje1`
    FOREIGN KEY (`Fichaje_idFichaje`)
    REFERENCES `PoliPases`.`Fichaje` (`idFichaje`),
  CONSTRAINT `fk_Jugador_Posicion1`
    FOREIGN KEY (`Posicion_idPosicion`)
    REFERENCES `PoliPases`.`Posicion` (`idPosicion`),
  CONSTRAINT `Jugador_ibfk_1`
    FOREIGN KEY (`Representante_DNI`)
    REFERENCES `PoliPases`.`Representante` (`DNI`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Representante_has_Equipo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Representante_has_Equipo` (
  `Representante_DNI` INT NOT NULL,
  `Equipo_id` INT NOT NULL,
  `Representante_habilitado` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`Representante_DNI`, `Equipo_id`),
  INDEX `fk_Representante_has_Equipo_Equipo1_idx` (`Equipo_id` ASC) VISIBLE,
  INDEX `fk_Representante_has_Equipo_Representante1_idx` (`Representante_DNI` ASC) VISIBLE,
  CONSTRAINT `fk_Representante_has_Equipo_Equipo1`
    FOREIGN KEY (`Equipo_id`)
    REFERENCES `PoliPases`.`Equipo` (`id`),
  CONSTRAINT `fk_Representante_has_Equipo_Representante1`
    FOREIGN KEY (`Representante_DNI`)
    REFERENCES `PoliPases`.`Representante` (`DNI`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
