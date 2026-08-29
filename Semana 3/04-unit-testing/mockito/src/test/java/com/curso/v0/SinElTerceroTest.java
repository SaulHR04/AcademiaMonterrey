package com.curso.v0;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * LA RAZON DE SER DE MOCKITO EN ESTE PROYECTO.
 *
 * De ICalculoComplejo solo tenemos la interfaz: la implementacion la escribe un
 * tercero y todavia no la tenemos. Sin Mockito, este proyecto no se puede probar
 * -- ni siquiera se puede ejecutar, como demuestra Principal.main().
 *
 * Mockito fabrica una implementacion de la interfaz en tiempo de ejecucion. Con
 * ella podemos probar HOY la unica parte que es nuestra: que el servicio delega,
 * y que devuelve lo que el colaborador le dio.
 */
@DisplayName("Probar sin que exista la implementacion del tercero")
class SinElTerceroTest {

	@Test
	@DisplayName("El servicio se prueba entero aunque nadie haya escrito el calculo")
	void sePruebaSinImplementacion() {
		// 1. Mockito implementa la interfaz por nosotros.
		ICalculoComplejo icc = mock(ICalculoComplejo.class);

		// 2. Le decimos que contestar. Este 1234.5 es un numero INVENTADO,
		//    y esa es justo la gracia: no necesitamos saber que formula usara
		//    el tercero para comprobar que nuestro servicio la usa bien.
		when(icc.ejecutaCalculoComplejo((byte) 30, (char) 100, (short) 1000, 77777, 44444L, 90.90F))
				.thenReturn(1234.5);

		ServiceCalculoImpuesto service = new ServiceCalculoImpuesto(icc);

		// 3. Lo que de verdad probamos: que calcularImpuesto() DELEGA y devuelve
		//    el resultado tal cual, sin adulterarlo.
		assertEquals(1234.5, service.calcularImpuesto(),
				"el servicio debe devolver lo que le dio el colaborador, sin tocarlo");
	}

	@Test
	@DisplayName("Un mock sin entrenar no falla: miente con ceros")
	void elMockSinEntrenarDevuelveCero() {
		// Ni un solo when(). Mockito NO explota ni devuelve null: para un metodo
		// que devuelve double, contesta 0.0 -- el valor por defecto del tipo.
		ICalculoComplejo icc = mock(ICalculoComplejo.class);

		ServiceCalculoImpuesto service = new ServiceCalculoImpuesto(icc);

		assertEquals(0.0, service.calcularImpuesto(),
				"un doble al que no le enseñaste nada responde con el valor por defecto");

		// LA TRAMPA MAS CARA DE MOCKITO: si se te olvida el when(), el test pasa
		// en verde contra un 0.0 que nadie calculo. Por eso las aserciones sobre
		// el valor devuelto no bastan -- hay que verificar la LLAMADA. Eso es
		// ArgumentosTest.
	}
}
