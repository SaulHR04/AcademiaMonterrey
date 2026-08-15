package com.curso.gymv0;

import java.util.*;

public class Principal {

	public static void main(String[] args) {
System.out.println("----GYM SMARTFIT----");
System.out.println("     Bienvenid@     ");
System.out.println("--------------------");
   
List<Usuario> listaUsuarios = new ArrayList<>(); 

PlanUsuario planSmart = new PlanUsuario("Smart",570.0,false,false);
PlanUsuario planBlack = new PlanUsuario("Black",600.0,true,true); //crear las membresias del smartfit
PlanUsuario planFit  = new PlanUsuario("Fit",470.0,false,true);


Cliente c1 = new Cliente("Saul Hernandez",planSmart, new CancelarPlan(true));
Cliente c2 = new Cliente("Miguel Rugerio",planBlack, new CancelarPlan(false));
Cliente c3 = new Cliente("Sergio Prado",planFit, new CancelarPlan(false));
Entrenador co = new Entrenador("Ismael");
Staff st = new Staff("Diego");
Limpiador lp = new Limpiador("Luisa");
Usuario us = new Usuario("lopez");

listaUsuarios.add(c1);
listaUsuarios.add(c2);
listaUsuarios.add(c3);
listaUsuarios.add(co);
listaUsuarios.add(st);
listaUsuarios.add(lp);


System.out.println("Cantidad de usuarios en sistema: |" + listaUsuarios.size() + "|");

System.out.println("---Permisos por usuario--- ");
for(Usuario u : listaUsuarios)
{
	u.permisosUsuario();
	}
System.out.println("\n---Cancelar plan---");

System.out.println("Usuario: " + c1.getNombre() + "Posibilidad de cancelacion: |" + c1.CancelacionSolicitar());
System.out.println("Usuario: " + c2.getNombre() + "Posibilidad de cancelacion: |" + c2.CancelacionSolicitar());
System.out.println("Usuario: " + c3.getNombre() + "Posibilidad de cancelacion: |" + c3.CancelacionSolicitar());

{
	System.out.println("---Buscar por plan Black---");

 for (Usuario u : listaUsuarios) {
 if (u instanceof Cliente c && c.getPlan().getNombre().equals("Black")) {
 System.out.println("  Usuario cuenta con plan black: " + c.getNombre());}}
 
 System.out.println("---Buscar por plan Smart---");

 for (Usuario u : listaUsuarios) {
 if (u instanceof Cliente c && c.getPlan().getNombre().equals("Smart")) {
 System.out.println("  Usuario cuenta con plan black: " + c.getNombre());}}
 
 System.out.println("---Buscar por plan fit---");

 for (Usuario u : listaUsuarios) {
 if (u instanceof Cliente c && c.getPlan().getNombre().equals("Fit")) {
 System.out.println("  Usuario cuenta con plan black: " + c.getNombre());}}
}
	}
}




	


