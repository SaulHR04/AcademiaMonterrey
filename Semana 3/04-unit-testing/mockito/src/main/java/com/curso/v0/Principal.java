package com.curso.v0;

public class Principal {

	public static void main(String[] args) {

		ICalculoComplejo icc = null;

		ServiceCalculoImpuesto service = new ServiceCalculoImpuesto(icc);

		System.out.println(service.calcularImpuesto());

	}

}
