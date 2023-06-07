-- MySQL dump 10.13  Distrib 8.0.33, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: PoliPases
-- ------------------------------------------------------
-- Server version	8.0.33-0ubuntu0.22.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Equipo`
--

DROP TABLE IF EXISTS `Equipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Equipo` (
  `id` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `posicion_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Equipo`
--

LOCK TABLES `Equipo` WRITE;
/*!40000 ALTER TABLE `Equipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `Equipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Equipo_has_Posicion`
--

DROP TABLE IF EXISTS `Equipo_has_Posicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Equipo_has_Posicion` (
  `Equipo_id` int NOT NULL,
  `Posicion_idPosicion` int NOT NULL,
  `cantidadPermitida` int DEFAULT NULL,
  PRIMARY KEY (`Equipo_id`,`Posicion_idPosicion`),
  KEY `fk_Equipo_has_Posicion_Posicion1_idx` (`Posicion_idPosicion`),
  KEY `fk_Equipo_has_Posicion_Equipo1_idx` (`Equipo_id`),
  CONSTRAINT `fk_Equipo_has_Posicion_Equipo1` FOREIGN KEY (`Equipo_id`) REFERENCES `Equipo` (`id`),
  CONSTRAINT `fk_Equipo_has_Posicion_Posicion1` FOREIGN KEY (`Posicion_idPosicion`) REFERENCES `Posicion` (`idPosicion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Equipo_has_Posicion`
--

LOCK TABLES `Equipo_has_Posicion` WRITE;
/*!40000 ALTER TABLE `Equipo_has_Posicion` DISABLE KEYS */;
/*!40000 ALTER TABLE `Equipo_has_Posicion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Fichaje`
--

DROP TABLE IF EXISTS `Fichaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Fichaje` (
  `idFichaje` int NOT NULL AUTO_INCREMENT,
  `numCamiseta` varchar(45) DEFAULT NULL,
  `fechaHoraFichaje` datetime DEFAULT NULL,
  `Equipo_id` int NOT NULL,
  PRIMARY KEY (`idFichaje`),
  KEY `fk_Fichaje_Equipo1_idx` (`Equipo_id`),
  CONSTRAINT `fk_Fichaje_Equipo1` FOREIGN KEY (`Equipo_id`) REFERENCES `Equipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Fichaje`
--

LOCK TABLES `Fichaje` WRITE;
/*!40000 ALTER TABLE `Fichaje` DISABLE KEYS */;
/*!40000 ALTER TABLE `Fichaje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Fichajes`
--

DROP TABLE IF EXISTS `Fichajes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Fichajes` (
  `idFichajes` int NOT NULL,
  `liga` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idFichajes`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Fichajes`
--

LOCK TABLES `Fichajes` WRITE;
/*!40000 ALTER TABLE `Fichajes` DISABLE KEYS */;
/*!40000 ALTER TABLE `Fichajes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Jugador`
--

DROP TABLE IF EXISTS `Jugador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Jugador` (
  `DNI` int NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `apellido` varchar(45) DEFAULT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  `tieneRepresentante` tinyint(1) DEFAULT NULL,
  `posicion` varchar(45) DEFAULT NULL,
  `salario` decimal(10,2) DEFAULT NULL,
  `Representante_DNI` int DEFAULT NULL,
  `Posicion_idPosicion` int NOT NULL,
  `Fichaje_idFichaje` int NOT NULL,
  PRIMARY KEY (`DNI`),
  KEY `Representante_DNI` (`Representante_DNI`),
  KEY `fk_Jugador_Posicion1_idx` (`Posicion_idPosicion`),
  KEY `fk_Jugador_Fichaje1_idx` (`Fichaje_idFichaje`),
  CONSTRAINT `fk_Jugador_Fichaje1` FOREIGN KEY (`Fichaje_idFichaje`) REFERENCES `Fichaje` (`idFichaje`),
  CONSTRAINT `fk_Jugador_Posicion1` FOREIGN KEY (`Posicion_idPosicion`) REFERENCES `Posicion` (`idPosicion`),
  CONSTRAINT `Jugador_ibfk_1` FOREIGN KEY (`Representante_DNI`) REFERENCES `Representante` (`DNI`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Jugador`
--

LOCK TABLES `Jugador` WRITE;
/*!40000 ALTER TABLE `Jugador` DISABLE KEYS */;
/*!40000 ALTER TABLE `Jugador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Posicion`
--

DROP TABLE IF EXISTS `Posicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Posicion` (
  `idPosicion` int NOT NULL AUTO_INCREMENT,
  `rol` varchar(45) NOT NULL,
  PRIMARY KEY (`idPosicion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Posicion`
--

LOCK TABLES `Posicion` WRITE;
/*!40000 ALTER TABLE `Posicion` DISABLE KEYS */;
/*!40000 ALTER TABLE `Posicion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Representante`
--

DROP TABLE IF EXISTS `Representante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Representante` (
  `DNI` int NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `apellido` varchar(45) DEFAULT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  PRIMARY KEY (`DNI`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Representante`
--

LOCK TABLES `Representante` WRITE;
/*!40000 ALTER TABLE `Representante` DISABLE KEYS */;
/*!40000 ALTER TABLE `Representante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Representante_has_Equipo`
--

DROP TABLE IF EXISTS `Representante_has_Equipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Representante_has_Equipo` (
  `Representante_DNI` int NOT NULL,
  `Equipo_id` int NOT NULL,
  `Representante_habilitado` tinyint DEFAULT NULL,
  PRIMARY KEY (`Representante_DNI`,`Equipo_id`),
  KEY `fk_Representante_has_Equipo_Equipo1_idx` (`Equipo_id`),
  KEY `fk_Representante_has_Equipo_Representante1_idx` (`Representante_DNI`),
  CONSTRAINT `fk_Representante_has_Equipo_Equipo1` FOREIGN KEY (`Equipo_id`) REFERENCES `Equipo` (`id`),
  CONSTRAINT `fk_Representante_has_Equipo_Representante1` FOREIGN KEY (`Representante_DNI`) REFERENCES `Representante` (`DNI`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Representante_has_Equipo`
--

LOCK TABLES `Representante_has_Equipo` WRITE;
/*!40000 ALTER TABLE `Representante_has_Equipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `Representante_has_Equipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-07  9:48:55
