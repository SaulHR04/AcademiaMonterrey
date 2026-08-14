package com.curso.gymv0;

public class Cliente extends Usuario {
private PlanUsuario plan;
private CancelarPlan fidelidad;

public Cliente(String nombre, PlanUsuario plan, CancelarPlan fidelidad) {
	super(nombre);
	this.plan = plan;
	this.fidelidad = fidelidad;
}
public PlanUsuario getPlan() {return plan;}

public String CancelacionSolicitar() {
	return fidelidad.CancelarSiNo();
}

@Override
public void permisosUsuario() {
	System.out.println("| Cliente: "+ getNombre()+ "| Plan activo: "+ plan.getNombre()+" Precio: "+ plan.getPrecio() + " | SPA: " + plan.getPermisoSPA() + " | Fidelidad: " + plan.getfidelidad() );
}
	
	
	}
	
	


