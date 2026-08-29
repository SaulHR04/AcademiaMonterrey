package com.curso.v0;

public class ServiceCalculoImpuesto {
	
	private ICalculoComplejo icc; //HAS A

	//@Autoriwed
	public ServiceCalculoImpuesto(ICalculoComplejo icc) {
		this.icc = icc;
	}
	
	double calcularImpuesto() {
		double resultado = icc.ejecutaCalculoComplejo( //DELEGACION
								(byte)30, (char)100, (short)1000, 77777, 44444, 90.90F);
		return resultado;
	}

}
