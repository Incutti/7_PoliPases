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
  `idEquipo` int NOT NULL,
  `nombreEquipo` varchar(45) NOT NULL,
  PRIMARY KEY (`idEquipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Equipo`
--

LOCK TABLES `Equipo` WRITE;
/*!40000 ALTER TABLE `Equipo` DISABLE KEYS */;
INSERT INTO `Equipo` VALUES (1,'Equipo A'),(2,'Equipo B'),(3,'Equipo C');
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
  `idPosicion` int NOT NULL,
  `cantidadPermitida` int DEFAULT NULL,
  PRIMARY KEY (`Equipo_id`,`idPosicion`),
  KEY `fk_Equipo_has_Posicion_Posicion1_idx` (`idPosicion`),
  KEY `fk_Equipo_has_Posicion_Equipo1_idx` (`Equipo_id`),
  CONSTRAINT `fk_Equipo_has_Posicion_Equipo1` FOREIGN KEY (`Equipo_id`) REFERENCES `Equipo` (`idEquipo`),
  CONSTRAINT `fk_Equipo_has_Posicion_Posicion1` FOREIGN KEY (`idPosicion`) REFERENCES `Posicion` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Equipo_has_Posicion`
--

LOCK TABLES `Equipo_has_Posicion` WRITE;
/*!40000 ALTER TABLE `Equipo_has_Posicion` DISABLE KEYS */;
INSERT INTO `Equipo_has_Posicion` VALUES (1,1,3),(1,2,4),(2,1,2),(2,3,3),(3,2,5);
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
  `Jugador_id` int NOT NULL,
  PRIMARY KEY (`idFichaje`),
  KEY `fk_Fichaje_Equipo1_idx` (`Equipo_id`),
  KEY `fk_Fichaje_Jugador1_idx` (`Jugador_id`),
  CONSTRAINT `fk_Fichaje_Equipo1` FOREIGN KEY (`Equipo_id`) REFERENCES `Equipo` (`idEquipo`),
  CONSTRAINT `fk_Fichaje_Jugador1` FOREIGN KEY (`Jugador_id`) REFERENCES `Jugador` (`DNI`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Fichaje`
--

LOCK TABLES `Fichaje` WRITE;
/*!40000 ALTER TABLE `Fichaje` DISABLE KEYS */;
INSERT INTO `Fichaje` VALUES (1,'10','2023-06-01 10:00:00',1,11111111),(2,'7','2023-06-02 15:30:00',2,22222222),(3,'5','2023-06-03 12:45:00',3,33333333);
/*!40000 ALTER TABLE `Fichaje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Jugador`
--

DROP TABLE IF EXISTS `Jugador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Jugador` (
  `DNI` int NOT NULL,
  `nombreJugador` varchar(45) DEFAULT NULL,
  `apellidoJugador` varchar(45) DEFAULT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  `salario` decimal(10,2) DEFAULT NULL,
  `Representante_DNI` int DEFAULT NULL,
  `Posicion_idPosicion` int NOT NULL,
  PRIMARY KEY (`DNI`),
  KEY `Representante_DNI` (`Representante_DNI`),
  KEY `fk_Jugador_Posicion1_idx` (`Posicion_idPosicion`),
  CONSTRAINT `fk_Jugador_Posicion1` FOREIGN KEY (`Posicion_idPosicion`) REFERENCES `Posicion` (`id`),
  CONSTRAINT `Jugador_ibfk_1` FOREIGN KEY (`Representante_DNI`) REFERENCES `Representante` (`dniRepresentante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Jugador`
--

LOCK TABLES `Jugador` WRITE;
/*!40000 ALTER TABLE `Jugador` DISABLE KEYS */;
INSERT INTO `Jugador` VALUES (11111111,'Jugador A','Apellido A','1995-08-20',5000.00,12345678,1),(11111112,'Jugador D','Apelldo D','1998-02-10',4000.00,87654321,1),(22222222,'Jugador B','Apellido B','1998-02-10',4000.00,87654321,2),(33333333,'Jugador C','Apellido C','1993-11-05',6000.00,12345678,3);
/*!40000 ALTER TABLE `Jugador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Posicion`
--

DROP TABLE IF EXISTS `Posicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Posicion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rol` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Posicion`
--

LOCK TABLES `Posicion` WRITE;
/*!40000 ALTER TABLE `Posicion` DISABLE KEYS */;
INSERT INTO `Posicion` VALUES (1,'Delantero'),(2,'Centrocampista'),(3,'Defensa');
/*!40000 ALTER TABLE `Posicion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Representante`
--

DROP TABLE IF EXISTS `Representante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Representante` (
  `dniRepresentante` int NOT NULL,
  `nombreRepresentante` varchar(45) DEFAULT NULL,
  `apellidoRepresentante` varchar(45) DEFAULT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  PRIMARY KEY (`dniRepresentante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Representante`
--

LOCK TABLES `Representante` WRITE;
/*!40000 ALTER TABLE `Representante` DISABLE KEYS */;
INSERT INTO `Representante` VALUES (12345678,'Juan','Pérez','1990-05-15'),(87654321,'María','Gómez','1985-12-10');
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
  `Equipo_idEquipo` int NOT NULL,
  `Representante_habilitado` tinyint DEFAULT NULL,
  PRIMARY KEY (`Representante_DNI`,`Equipo_idEquipo`),
  KEY `fk_Representante_has_Equipo_Equipo1_idx` (`Equipo_idEquipo`),
  KEY `fk_Representante_has_Equipo_Representante1_idx` (`Representante_DNI`),
  CONSTRAINT `fk_Representante_has_Equipo_Equipo1` FOREIGN KEY (`Equipo_idEquipo`) REFERENCES `Equipo` (`idEquipo`),
  CONSTRAINT `fk_Representante_has_Equipo_Representante1` FOREIGN KEY (`Representante_DNI`) REFERENCES `Representante` (`dniRepresentante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Representante_has_Equipo`
--

LOCK TABLES `Representante_has_Equipo` WRITE;
/*!40000 ALTER TABLE `Representante_has_Equipo` DISABLE KEYS */;
INSERT INTO `Representante_has_Equipo` VALUES (12345678,1,1),(12345678,3,0),(87654321,2,1);
/*!40000 ALTER TABLE `Representante_has_Equipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `jugadoresPorClub`
--

DROP TABLE IF EXISTS `jugadoresPorClub`;
/*!50001 DROP VIEW IF EXISTS `jugadoresPorClub`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `jugadoresPorClub` AS SELECT 
 1 AS `nombreEquipo`,
 1 AS `nombreJugador`,
 1 AS `apellidoJugador`,
 1 AS `rol`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `jugadoresPorClub`
--

/*!50001 DROP VIEW IF EXISTS `jugadoresPorClub`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`alumno`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `jugadoresPorClub` AS select `Equipo`.`nombreEquipo` AS `nombreEquipo`,`Jugador`.`nombreJugador` AS `nombreJugador`,`Jugador`.`apellidoJugador` AS `apellidoJugador`,`Posicion`.`rol` AS `rol` from (((`Jugador` join `Fichaje` on((`Jugador`.`DNI` = `Fichaje`.`Jugador_id`))) join `Equipo` on((`Fichaje`.`Equipo_id` = `Equipo`.`idEquipo`))) join `Posicion` on((`Jugador`.`Posicion_idPosicion` = `Posicion`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-16 17:50:32
