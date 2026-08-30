package com.curso.v0;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Probar sin que exista la implementacion del tercero")
class SinElTerceroTest {

	@Test
	@DisplayName("El servicio se prueba entero aunque nadie haya escrito el calculo")
	void sePruebaSinImplementacion() {
		
		ICalculoComplejo icc = mock(ICalculoComplejo.class);


		when(icc.ejecutaCalculoComplejo((byte) 30, (char) 100, (short) 1000, 77777, 44444L, 90.90F))
				.thenReturn(1234.5);

		ServiceCalculoImpuesto service = new ServiceCalculoImpuesto(icc);

	
		assertEquals(1234.5, service.calcularImpuesto(),
				"el servicio debe devolver lo que le dio el colaborador, sin tocarlo");
	}

	@Test
	@DisplayName("Un mock sin entrenar no falla: miente con ceros")
	void elMockSinEntrenarDevuelveCero() {
		
		ICalculoComplejo icc = mock(ICalculoComplejo.class);

		ServiceCalculoImpuesto service = new ServiceCalculoImpuesto(icc);

		assertEquals(0.0, service.calcularImpuesto(),
				"un doble al que no le enseñaste nada responde con el valor por defecto");

	
	}
}
