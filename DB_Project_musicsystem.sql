-- MariaDB dump 10.17  Distrib 10.5.6-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: musicsystem
-- ------------------------------------------------------
-- Server version	10.5.6-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `musicsystem`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `musicsystem` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `musicsystem`;

--
-- Table structure for table `add_to_playlist`
--

DROP TABLE IF EXISTS `add_to_playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `add_to_playlist` (
  `musicNo` int(11) NOT NULL,
  `userNo` int(11) NOT NULL,
  `title` varchar(20) NOT NULL,
  PRIMARY KEY (`musicNo`,`userNo`,`title`),
  KEY `userNo` (`userNo`,`title`),
  CONSTRAINT `add_to_playlist_ibfk_1` FOREIGN KEY (`musicNo`) REFERENCES `music` (`musicNo`),
  CONSTRAINT `add_to_playlist_ibfk_2` FOREIGN KEY (`userNo`, `title`) REFERENCES `playlist` (`userNo`, `title`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `add_to_playlist`
--

LOCK TABLES `add_to_playlist` WRITE;
/*!40000 ALTER TABLE `add_to_playlist` DISABLE KEYS */;
INSERT INTO `add_to_playlist` VALUES (2,4,'p'),(2,6,'siuns playlist'),(2,6,'yeh~~'),(2,8,'user playlist'),(3,6,'playy'),(3,6,'siuns playlist'),(3,6,'yeh~~'),(3,8,'user playlist'),(4,4,'playlist'),(4,6,'playy'),(4,9,'jj'),(5,6,'playy'),(10,6,'playy'),(10,6,'siuns playlist'),(10,6,'yeh~~'),(12,6,'yeh~~'),(12,9,'jj'),(13,6,'siuns playlist');
/*!40000 ALTER TABLE `add_to_playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `album`
--

DROP TABLE IF EXISTS `album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album` (
  `albumNo` int(11) NOT NULL AUTO_INCREMENT,
  `releaseDate` date DEFAULT NULL,
  `title` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`albumNo`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `album`
--

LOCK TABLES `album` WRITE;
/*!40000 ALTER TABLE `album` DISABLE KEYS */;
INSERT INTO `album` VALUES (2,'2020-01-01','palette'),(3,'2019-11-18','Love poem'),(6,'2014-11-13','a'),(9,'2014-11-24','HAPPY TOGETHER');
/*!40000 ALTER TABLE `album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artist`
--

DROP TABLE IF EXISTS `artist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist` (
  `artistNo` int(11) NOT NULL AUTO_INCREMENT,
  `debutDate` date DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`artistNo`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artist`
--

LOCK TABLES `artist` WRITE;
/*!40000 ALTER TABLE `artist` DISABLE KEYS */;
INSERT INTO `artist` VALUES (11,'2008-09-23','IU'),(12,'2012-11-25','AKMU'),(14,'2003-12-31','Teayeon'),(15,'1999-11-04','ParkHyosin'),(16,'2016-08-08','BLACKPINK');
/*!40000 ALTER TABLE `artist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artist_member`
--

DROP TABLE IF EXISTS `artist_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_member` (
  `artistNo` int(11) NOT NULL,
  `mName` varchar(20) NOT NULL,
  PRIMARY KEY (`artistNo`,`mName`),
  CONSTRAINT `artist_member_ibfk_1` FOREIGN KEY (`artistNo`) REFERENCES `artist` (`artistNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artist_member`
--

LOCK TABLES `artist_member` WRITE;
/*!40000 ALTER TABLE `artist_member` DISABLE KEYS */;
INSERT INTO `artist_member` VALUES (11,'IU'),(11,'jieun'),(12,'c'),(12,'s'),(14,'teayeon'),(15,'parkhyosin'),(16,'je'),(16,'ji'),(16,'li'),(16,'ro');
/*!40000 ALTER TABLE `artist_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `musicNo` int(11) NOT NULL,
  `userNo` int(11) NOT NULL,
  `writeDate` date DEFAULT NULL,
  `content` varchar(200) NOT NULL,
  PRIMARY KEY (`userNo`,`musicNo`,`content`),
  KEY `musicNo` (`musicNo`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`musicNo`) REFERENCES `music` (`musicNo`),
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`userNo`) REFERENCES `user` (`userNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (2,4,'2020-12-04','hungry...'),(2,6,'2020-12-06','good!'),(10,6,'2020-11-07','good song~'),(2,8,'2020-12-07','hahaha'),(4,9,'2020-12-05','hi!'),(10,9,'2020-12-02','good!!'),(10,9,'2020-12-07','haha');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `likes` (
  `musicNo` int(11) NOT NULL,
  `userNo` int(11) NOT NULL,
  PRIMARY KEY (`musicNo`,`userNo`),
  KEY `userNo` (`userNo`),
  CONSTRAINT `likes_ibfk_1` FOREIGN KEY (`musicNo`) REFERENCES `music` (`musicNo`),
  CONSTRAINT `likes_ibfk_2` FOREIGN KEY (`userNo`) REFERENCES `user` (`userNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES (2,4),(2,6),(2,8),(3,6),(3,8),(4,4),(4,6),(4,9),(5,4),(9,4),(10,6),(10,9),(12,6),(12,9),(13,6);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `make_album`
--

DROP TABLE IF EXISTS `make_album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `make_album` (
  `artistNo` int(11) NOT NULL,
  `albumNo` int(11) NOT NULL,
  PRIMARY KEY (`artistNo`,`albumNo`),
  KEY `albumNo` (`albumNo`),
  CONSTRAINT `make_album_ibfk_1` FOREIGN KEY (`artistNo`) REFERENCES `artist` (`artistNo`),
  CONSTRAINT `make_album_ibfk_2` FOREIGN KEY (`albumNo`) REFERENCES `album` (`albumNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `make_album`
--

LOCK TABLES `make_album` WRITE;
/*!40000 ALTER TABLE `make_album` DISABLE KEYS */;
INSERT INTO `make_album` VALUES (11,2),(11,3),(11,9),(12,6),(15,9);
/*!40000 ALTER TABLE `make_album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `make_music`
--

DROP TABLE IF EXISTS `make_music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `make_music` (
  `artistNo` int(11) NOT NULL,
  `musicNo` int(11) NOT NULL,
  PRIMARY KEY (`artistNo`,`musicNo`),
  KEY `musicNo` (`musicNo`),
  CONSTRAINT `make_music_ibfk_1` FOREIGN KEY (`artistNo`) REFERENCES `artist` (`artistNo`),
  CONSTRAINT `make_music_ibfk_2` FOREIGN KEY (`musicNo`) REFERENCES `music` (`musicNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `make_music`
--

LOCK TABLES `make_music` WRITE;
/*!40000 ALTER TABLE `make_music` DISABLE KEYS */;
INSERT INTO `make_music` VALUES (11,2),(11,3),(11,5),(11,12),(12,4),(12,13),(15,9),(15,10),(16,13);
/*!40000 ALTER TABLE `make_music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manager` (
  `managerNo` int(11) NOT NULL AUTO_INCREMENT,
  `id` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  PRIMARY KEY (`managerNo`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` VALUES (2,'3','3','3','2020-10-10','F'),(3,'m','m','name','2001-01-20','M'),(4,'man','man','manager','1999-01-02','F');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music`
--

DROP TABLE IF EXISTS `music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `music` (
  `musicNo` int(11) NOT NULL AUTO_INCREMENT,
  `genre` varchar(20) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `lyrics` text DEFAULT NULL,
  `playtime` time DEFAULT NULL,
  `releaseDate` date DEFAULT NULL,
  `albumNo` int(11) DEFAULT NULL,
  `managerNo` int(11) NOT NULL,
  PRIMARY KEY (`musicNo`),
  KEY `albumNo` (`albumNo`),
  KEY `managerNo` (`managerNo`),
  CONSTRAINT `music_ibfk_1` FOREIGN KEY (`albumNo`) REFERENCES `album` (`albumNo`),
  CONSTRAINT `music_ibfk_2` FOREIGN KEY (`managerNo`) REFERENCES `manager` (`managerNo`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music`
--

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES (2,'g','unlucky','just life we re still good without luck\ntake your time\ntheres no right\ni know that life is sometimes so mean\n','00:03:50','2019-11-18',3,3),(3,'genre','palette','I like it Im twenty five\nI got this Im truly fine\n','00:03:38','2017-04-21',2,3),(4,'genre','title','ttttttt\naaaaaaa\n','00:03:30','2020-12-12',6,3),(5,'g','Black out','That she said\ncourse course I dont care\n','00:04:00','2017-11-11',2,3),(9,'b','goodbye','gooooodbyeeeee\neeeee\n','00:04:50','2019-05-06',NULL,3),(10,'b','happy together','so happy together\nwe are dancing together\nwe belong together\n','00:04:10','2014-11-24',9,3),(12,'gggg','blueming','I feel bloom I feel bloom\nI feel blue I feel blue\n','00:03:20','2010-12-12',NULL,3),(13,'genre','music','lalala\nfkfkfk\n','00:03:40','2000-02-12',6,3);
/*!40000 ALTER TABLE `music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `play`
--

DROP TABLE IF EXISTS `play`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `play` (
  `musicNo` int(11) NOT NULL,
  `userNo` int(11) NOT NULL,
  `cnt_play` int(11) DEFAULT 0,
  PRIMARY KEY (`musicNo`,`userNo`),
  KEY `userNo` (`userNo`),
  CONSTRAINT `play_ibfk_1` FOREIGN KEY (`musicNo`) REFERENCES `music` (`musicNo`),
  CONSTRAINT `play_ibfk_2` FOREIGN KEY (`userNo`) REFERENCES `user` (`userNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `play`
--

LOCK TABLES `play` WRITE;
/*!40000 ALTER TABLE `play` DISABLE KEYS */;
INSERT INTO `play` VALUES (2,4,1),(2,6,2),(2,8,3),(3,6,4),(3,8,4),(4,4,6),(4,9,2),(5,4,1),(9,4,2),(10,6,9),(10,8,1),(10,9,2),(12,6,3),(12,9,1),(13,6,2);
/*!40000 ALTER TABLE `play` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playlist` (
  `title` varchar(20) NOT NULL,
  `userNo` int(11) NOT NULL,
  `isOpen` int(11) DEFAULT NULL,
  PRIMARY KEY (`userNo`,`title`),
  CONSTRAINT `playlist_ibfk_1` FOREIGN KEY (`userNo`) REFERENCES `user` (`userNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
INSERT INTO `playlist` VALUES ('p',4,1),('playlist',4,1),('playy',6,0),('siuns playlist',6,1),('yeh~~',6,1),('user playlist',8,1),('jj',9,1);
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userNo` int(11) NOT NULL AUTO_INCREMENT,
  `id` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `managerNo` int(11) NOT NULL,
  PRIMARY KEY (`userNo`),
  UNIQUE KEY `id` (`id`),
  KEY `managerNo` (`managerNo`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`managerNo`) REFERENCES `manager` (`managerNo`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (4,'u','u','u','2002-02-02','M',2),(6,'siun','siun','siun','2000-08-16','F',3),(8,'user','user','user','2020-12-07','M',2),(9,'jj','jj','jj','2000-12-07','M',4);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-07 23:25:55
