package com.luv2code.springboot.cruddemo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Youtubers")
public class Youtubers {


    @Id
 private String id;
 private String firstName;
 private String lastName;
 private String email;
 private String nombre;
 private String canal_youtube;
 private String tipo_contenido; 
 private String seguidores;
 private boolean monetizacion;
 private int videos_total;
 private int funas;
 private String tiempo_youtube;
 private String equipo_de_trabajo;
 public Youtubers(String id, String firstName, String lastName, String email, String nombre, String canal_youtube,
		String tipo_contenido, String seguidores, boolean monetizacion, int videos_total, int funas,
		String tiempo_youtube, String equipo_de_trabajo) {
	super();
	this.id = id;
	this.firstName = firstName;
	this.lastName = lastName;
	this.email = email;
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
 public String getId() {
	return id;
 }
 public void setId(String id) {
	this.id = id;
 }
 public String getFirstName() {
	return firstName;
 }
 public void setFirstName(String firstName) {
	this.firstName = firstName;
 }
 public String getLastName() {
	return lastName;
 }
 public void setLastName(String lastName) {
	this.lastName = lastName;
 }
 public String getEmail() {
	return email;
 }
 public void setEmail(String email) {
	this.email = email;
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

}