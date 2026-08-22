package com.luv2code.springboot.cruddemo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Youtubers")
public class Youtubers {

   
    @Id
    private String id;
    private String nombre;
    private String tipo_contenido;
    private String seguidores;
    private int videos_totales;
    private String equipo_de_trabajo;
    private int funas; 
    private String tiempo_youtube;
    private boolean monetizacion;
	
    public Youtubers() {
    }
    
    public Youtubers(String id, String nombre, String tipo_contenido, String seguidores, int videos_totales,
			String equipo_de_trabajo, int funas, String tiempo_youtube, boolean monetizacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo_contenido = tipo_contenido;
		this.seguidores = seguidores;
		this.videos_totales = videos_totales;
		this.equipo_de_trabajo = equipo_de_trabajo;
		this.funas = funas;
		this.tiempo_youtube = tiempo_youtube;
		this.monetizacion = monetizacion;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo_contenido() {
		return tipo_contenido;
	}
	public void setTipo_contenido(String tipo_contenido) {
		this.tipo_contenido = tipo_contenido;
	}
	public String getSeguidores() {
		return seguidores;
	}
	public void setSeguidores(String seguidores) {
		this.seguidores = seguidores;
	}
	public int getVideos_totales() {
		return videos_totales;
	}
	public void setVideos_totales(int videos_totales) {
		this.videos_totales = videos_totales;
	}
	public String getEquipo_de_trabajo() {
		return equipo_de_trabajo;
	}
	public void setEquipo_de_trabajo(String equipo_de_trabajo) {
		this.equipo_de_trabajo = equipo_de_trabajo;
	}
	public int getFunas() {
		return funas;
	}
	public void setFunas(int funas) {
		this.funas = funas;
	}
	public String getTiempo_youtube() {
		return tiempo_youtube;
	}
	public void setTiempo_youtube(String tiempo_youtube) {
		this.tiempo_youtube = tiempo_youtube;
	}
	public boolean isMonetizacion() {
		return monetizacion;
	}
	public void setMonetizacion(boolean monetizacion) {
		this.monetizacion = monetizacion;
	}
    
    }

