package com.luv2code.springboot.cruddemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="Youtubers")
public class Youtubers {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="nombre")
    private String nombre;

    @Column(name="canal_youtube")
    private String canal_youtube;

    @Column(name="tipo_contenido")
    private String tipo_contenido; 
    
    @Column(name="seguidores")
    private String seguidores;
    
    @Column(name="monetizacion")
    private boolean monetizacion;
    
    @Column(name="videos_total")
    private int videos_total;
    
    @Column(name="funas")
    private int funas;

    @Column(name="tiempo_youtube")
    private String tiempo_youtube;
    
    @Column(name="equipo_de_trabajo")
    private String equipo_de_trabajo;
    // constructor
    public Youtubers() {

    }

    public Youtubers(String nombre, String canal_youtube, String tipo_contenido,String seguidores,boolean monetizacion,int videos_total
    	, int funas,String tiempo_youtube, String equipo_de_trabajo) {
        this.nombre = nombre;
        this.canal_youtube = canal_youtube;
        this.tipo_contenido = tipo_contenido;
        this.seguidores = seguidores;
        this.monetizacion = monetizacion;
        this.videos_total = videos_total;
        this.funas = funas;
        this.tiempo_youtube = tiempo_youtube;
        this.equipo_de_trabajo = equipo_de_trabajo;
    }

    // define getter/setter

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCanal_youtube() {
		return canal_youtube;
	}

	public void setCanal_youtube(String canal_youtube) {
		this.canal_youtube = canal_youtube;
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

	public boolean isMonetizacion() {
		return monetizacion;
	}

	public void setMonetizacion(boolean monetizacion) {
		this.monetizacion = monetizacion;
	}

	public int getVideos_total() {
		return videos_total;
	}

	public void setVideos_total(int videos_total) {
		this.videos_total = videos_total;
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

	public String getEquipo_de_trabajo() {
		return equipo_de_trabajo;
	}

	public void setEquipo_de_trabajo(String equipo_de_trabajo) {
		this.equipo_de_trabajo = equipo_de_trabajo;
	}

	@Override
	public String toString() {
		return "Youtubers [id=" + id + ", nombre=" + nombre + ", canal_youtube=" + canal_youtube + ", tipo_contenido="
				+ tipo_contenido + ", seguidores=" + seguidores + ", monetizacion=" + monetizacion + ", videos_total="
				+ videos_total + ", funas=" + funas + ", tiempo_youtube=" + tiempo_youtube + ", equipo_de_trabajo="
				+ equipo_de_trabajo + "]";
	}
	
}








