package com.curso.gymv0;

public class CancelarPlan {
private boolean fidelidad;

public CancelarPlan(boolean fidelidad) {
	this.fidelidad = fidelidad;
}
public String CancelarSiNo() {
	if (fidelidad) {
		return " No es posible --Tienes un plan de 12 meses de fidelidad--\n";
		
	} else {			//comprobar si tiene fidelidad o no, cancelar o no cancelar el plan
		return " Si! es posible cancelar tu plan. \n ";
	}
}
}
