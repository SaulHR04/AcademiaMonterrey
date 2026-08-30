package com.curso.v0;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Simular al tercero portandose mal")
class FallosDelTerceroTest {

	@Test
	@DisplayName("Si el colaborador revienta, la excepcion sube sin envolver")
	void siElTerceroRevientaLaExcepcionSube() {
		ICalculoComplejo icc = mock(ICalculoComplejo.class);
		when(icc.ejecutaCalculoComplejo((byte) 30, (char) 100, (short) 1000, 77777, 44444L, 90.90F))
				.thenThrow(new ArithmeticException("servicio de calculo no disponible"));

		ServiceCalculoImpuesto service = new ServiceCalculoImpuesto(icc);

		ArithmeticException e = assertThrows(ArithmeticException.class, service::calcularImpuesto);
		assertEquals("servicio de calculo no disponible", e.getMessage());
	}

	@Test
	@DisplayName("Un impuesto negativo se devuelve tal cual: el servicio no valida")
	void elServicioNoValidaElSigno() {
		ICalculoComplejo icc = mock(ICalculoComplejo.class);
		when(icc.ejecutaCalculoComplejo((byte) 30, (char) 100, (short) 1000, 77777, 44444L, 90.90F))
				.thenReturn(-1.0);

	
		assertEquals(-1.0, new ServiceCalculoImpuesto(icc).calcularImpuesto());
	}

	@Test
	@DisplayName("Un NaN atraviesa el servicio y contamina la cuenta")
	void elNaNAtraviesaElServicio() {
		ICalculoComplejo icc = mock(ICalculoComplejo.class);
		when(icc.ejecutaCalculoComplejo((byte) 30, (char) 100, (short) 1000, 77777, 44444L, 90.90F))
				.thenReturn(Double.NaN);

		double resultado = new ServiceCalculoImpuesto(icc).calcularImpuesto();

		assertTrue(Double.isNaN(resultado),
				"el NaN pasa entero: nadie lo filtra por el camino");
	}
}
