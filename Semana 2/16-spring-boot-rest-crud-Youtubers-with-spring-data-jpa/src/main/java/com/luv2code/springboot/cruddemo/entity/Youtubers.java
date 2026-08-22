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

    @Column(name="seguidores")
    private String seguidores;

    @Column(name="tipo_contenido")
    private String tipo_contenido;
    
    @Column(name="videos_total")
    private int videos_total;
    
    @Column(name="equipo_de_trabajo")
    private String equipo_de_trabajo;
    @Column(name="funas")
    int funas; 
    @Column(name="tiempo_youtube")
    private String tiempo_youtube;
    
    @Column(name="monetizacion")
    private boolean monetizacion;
    @Column(name="canal_youtube")
    private String canal_youtube;

    // define constructors
    public Youtubers() {

    }

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

	public String getSeguidores() {
		return seguidores;
	}

	public void setSeguidores(String seguidores) {
		this.seguidores = seguidores;
	}

	public String getTipo_contenido() {
		return tipo_contenido;
	}

	public void setTipo_contenido(String tipo_contenido) {
		this.tipo_contenido = tipo_contenido;
	}

	public int getVideos_total() {
		return videos_total;
	}

	public void setVideos_total(int videos_total) {
		this.videos_total = videos_total;
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

	public String getCanal_youtube() {
		return canal_youtube;
	}

	public void setCanal_youtube(String canal_youtube) {
		this.canal_youtube = canal_youtube;
	}

	public Youtubers(int id, String nombre, String seguidores, String tipo_contenido, int videos_total,
			String equipo_de_trabajo, int funas, String tiempo_youtube, boolean monetizacion, String canal_youtube) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.seguidores = seguidores;
		this.tipo_contenido = tipo_contenido;
		this.videos_total = videos_total;
		this.equipo_de_trabajo = equipo_de_trabajo;
		this.funas = funas;
		this.tiempo_youtube = tiempo_youtube;
		this.monetizacion = monetizacion;
		this.canal_youtube = canal_youtube;
	}



    
}








