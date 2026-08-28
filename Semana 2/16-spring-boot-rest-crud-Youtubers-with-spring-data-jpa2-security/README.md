# Proyectos 1 SQL

Proyecto realizado con la version SQL

---

## Proyecto 1: Youtubers

### Entidad y Campos
* **Entidad:** Youtubers`
* **Campos:**
  * `id` Identificador
  * `nombre`Nombre del youtuber.
  * `seguidores` Cantidad de seguidores del youtuber.
  * `tipo_contenido` Tipo de contenido del youtuber.
  * `videos_total` Cantidad total de videos subidos.
  * `equipo_de_trabajo` Tamaño del equipo del youtuber.
  * `funas` Cantidad de funas del youtuber.
  * `tiempo_youtube` Tiempo que lleva desde el primer video.
  * `monetizacion` Cuenta con monetización activa (BOOlean).
  * `canal_youtube` Nombre del canal de youtube.

### Configuración y Despliegue
1. Conectar al contenedor Docker de MySQL e iniciar sesión:
   ```powershell
   docker exec -it bd-mysql mysql -u root -p

### Scripts 
2. Importado de phpmyadmin (en carpeta)

### URL
3.
 * `http://localhost:8080/api/Youtubers
### EndPoints

4.
Get: GET /api/youtubers , GET /api/youtubers/{youtuberId}
POST: POST /api/youtubers
PUT: PUT /api/youtubers
DELETE: DELETE /api/youtubers/{youtuberId}

