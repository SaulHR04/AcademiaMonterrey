package com.curso.v0;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
@DisplayName("Mockito inyecta en el test lo que Spring inyectara en produccion")
class InyeccionTest {

	@Mock
	private ICalculoComplejo icc;

	@InjectMocks
	private ServiceCalculoImpuesto service;

	@Test
	@DisplayName("El servicio llega construido y con su colaborador dentro")
	void seInyectaPorConstructor() {
		// Nadie escribio new ServiceCalculoImpuesto(...): lo armo la extension.
		assertNotNull(service, "@InjectMocks debio construirlo usando el constructor");
	}

	@Test
	@DisplayName("Y funciona igual que con la inyeccion de verdad")
	void elServicioDelegaEnElColaboradorInyectado() {
		when(icc.ejecutaCalculoComplejo((byte) 30, (char) 100, (short) 1000, 77777, 44444L, 90.90F))
				.thenReturn(999.99);

		assertEquals(999.99, service.calcularImpuesto());

		verify(icc).ejecutaCalculoComplejo((byte) 30, (char) 100, (short) 1000, 77777, 44444L, 90.90F);
	}
}
