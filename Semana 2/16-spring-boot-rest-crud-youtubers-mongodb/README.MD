# Proyectos 2 Mongo

Proyecto realizado con la version Mongo
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
1. Conectar contenedor Docker de Mongo
```powershell
   docker run -d --name bd-mongo -p 27017:27017 mongo:latest
### Scripts
2. (Dentro del archivo)

### URL
3. http://localhost:8080/api/Youtubers

### EndPoints
4.

* `GET /api/Youtubers

* `GET /api/Youtubers/{YoutubersId} 

* `POST /api/Youtubers

* `PUT /api/Youtubers 

* `DELETE /api/Youtubers/{YoutubersId}