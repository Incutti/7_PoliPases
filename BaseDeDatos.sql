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
drop database if exists Polipases;
CREATE SCHEMA IF NOT EXISTS `PoliPases` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `PoliPases` ;
-- -----------------------------------------------------
-- Table `PoliPases`.`Equipo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Equipo` (
  `idEquipo` INT NOT NULL,
  `nombreEquipo` VARCHAR(45) NOT NULL,
  `posicion_id` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idEquipo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Posicion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Posicion` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rol` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Equipo_has_Posicion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Equipo_has_Posicion` (
  `Equipo_id` INT NOT NULL,
  `idPosicion` INT NOT NULL,
  `cantidadPermitida` INT NULL DEFAULT NULL,
  PRIMARY KEY (`Equipo_id`, `idPosicion`),
  INDEX `fk_Equipo_has_Posicion_Posicion1_idx` (`idPosicion` ASC) VISIBLE,
  INDEX `fk_Equipo_has_Posicion_Equipo1_idx` (`Equipo_id` ASC) VISIBLE,
  CONSTRAINT `fk_Equipo_has_Posicion_Equipo1`
    FOREIGN KEY (`Equipo_id`)
    REFERENCES `PoliPases`.`Equipo` (`idEquipo`),
  CONSTRAINT `fk_Equipo_has_Posicion_Posicion1`
    FOREIGN KEY (`idPosicion`)
    REFERENCES `PoliPases`.`Posicion` (`id`))
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
    REFERENCES `PoliPases`.`Equipo` (`idEquipo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Representante`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Representante` (
  `dniRepresentante` INT NOT NULL,
  `nombreRepresentante` VARCHAR(45) NULL DEFAULT NULL,
  `apellidoRepresentante` VARCHAR(45) NULL DEFAULT NULL,
  `fechaNacimiento` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`dniRepresentante`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Jugador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Jugador` (
  `DNI` INT NOT NULL,
  `nombreJugador` VARCHAR(45) NULL DEFAULT NULL,
  `apellidoJugador` VARCHAR(45) NULL DEFAULT NULL,
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
    REFERENCES `PoliPases`.`Posicion` (`id`),
  CONSTRAINT `Jugador_ibfk_1`
    FOREIGN KEY (`Representante_DNI`)
    REFERENCES `PoliPases`.`Representante` (`dniRepresentante`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `PoliPases`.`Representante_has_Equipo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PoliPases`.`Representante_has_Equipo` (
  `Representante_DNI` INT NOT NULL,
  `Equipo_idEquipo` INT NOT NULL,
  `Representante_habilitado` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`Representante_DNI`, `Equipo_idEquipo`),
  INDEX `fk_Representante_has_Equipo_Equipo1_idx` (`Equipo_idEquipo` ASC) VISIBLE,
  INDEX `fk_Representante_has_Equipo_Representante1_idx` (`Representante_DNI` ASC) VISIBLE,
  CONSTRAINT `fk_Representante_has_Equipo_Equipo1`
    FOREIGN KEY (`Equipo_idEquipo`)
    REFERENCES `PoliPases`.`Equipo` (`idEquipo`),
  CONSTRAINT `fk_Representante_has_Equipo_Representante1`
    FOREIGN KEY (`Representante_DNI`)
    REFERENCES `PoliPases`.`Representante` (`dniRepresentante`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;