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
listaUsuarios.add(us);

System.out.println("Cantidad de usuarios en sistema: |" + listaUsuarios.size() + "|");

System.out.println("---Permisos por usuario---");
for(Usuario u : listaUsuarios)
{
	u.permisosUsuario();
	}
	}

}
