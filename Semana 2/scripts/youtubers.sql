-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: host.docker.internal
-- Tiempo de generación: 22-08-2026 a las 03:12:11
-- Versión del servidor: 26.7.0
-- Versión de PHP: 8.3.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `db_Youtubers`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `youtubers`
--

CREATE TABLE `youtubers` (
  `id` int NOT NULL,
  `canal_youtube` varchar(255) DEFAULT NULL,
  `equipo_de_trabajo` varchar(255) DEFAULT NULL,
  `funas` int DEFAULT NULL,
  `monetizacion` bit(1) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `seguidores` varchar(255) DEFAULT NULL,
  `tiempo_youtube` varchar(255) DEFAULT NULL,
  `tipo_contenido` varchar(255) DEFAULT NULL,
  `videos_total` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `youtubers`
--

INSERT INTO `youtubers` (`id`, `canal_youtube`, `equipo_de_trabajo`, `funas`, `monetizacion`, `nombre`, `seguidores`, `tiempo_youtube`, `tipo_contenido`, `videos_total`) VALUES
(1, 'LuisitoComunica', 'Equipo de producción completo', 0, b'1', 'Luisito el pillo', '44M', '12 años', 'Vlogs de viajes y documentales', 1250),
(3, 'Ibai', 'Equipo de producción completo', 0, b'1', 'Ibai Llanos', '15M', '6 años', 'Entretenimiento / Eventos', 650);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `youtubers`
--
ALTER TABLE `youtubers`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `youtubers`
--
ALTER TABLE `youtubers`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
