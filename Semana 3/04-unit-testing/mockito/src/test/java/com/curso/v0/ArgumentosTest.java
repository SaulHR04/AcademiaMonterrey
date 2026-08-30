package com.curso.v0;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


@DisplayName("Verificar la llamada, no el resultado")
class ArgumentosTest {

	@Test
	@DisplayName("verify comprueba los seis argumentos exactos")
	void verificaLosSeisArgumentos() {
		ICalculoComplejo icc = mock(ICalculoComplejo.class);

		new ServiceCalculoImpuesto(icc).calcularImpuesto();

		
		verify(icc).ejecutaCalculoComplejo((byte) 30, (char) 100, (short) 1000, 77777, 44444L, 90.90F);
	}

	@Test
	@DisplayName("El captor enseña lo que de verdad se envio, valor por valor")
	void elCaptorEnseñaLoQueSeEnvio() {
		ICalculoComplejo icc = mock(ICalculoComplejo.class);

		new ServiceCalculoImpuesto(icc).calcularImpuesto();

		ArgumentCaptor<Byte> b = ArgumentCaptor.forClass(Byte.class);
		ArgumentCaptor<Character> ch = ArgumentCaptor.forClass(Character.class);
		ArgumentCaptor<Short> sh = ArgumentCaptor.forClass(Short.class);
		ArgumentCaptor<Integer> i = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Long> l = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Float> f = ArgumentCaptor.forClass(Float.class);

	
		verify(icc).ejecutaCalculoComplejo(b.capture(), ch.capture(), sh.capture(),
				i.capture(), l.capture(), f.capture());

		assertEquals((byte) 30, b.getValue());
		assertEquals((char) 100, ch.getValue());
		assertEquals((short) 1000, sh.getValue());
		assertEquals(77777, i.getValue());

		
		assertEquals(44444L, l.getValue());

		assertEquals(90.90F, f.getValue());
	}

	@Test
	@DisplayName("El calculo caro se pide UNA sola vez")
	void seLlamaUnaSolaVez() {
		ICalculoComplejo icc = mock(ICalculoComplejo.class);

		new ServiceCalculoImpuesto(icc).calcularImpuesto();

	
		verify(icc, times(1)).ejecutaCalculoComplejo((byte) 30, (char) 100, (short) 1000,
				77777, 44444L, 90.90F);
		verifyNoMoreInteractions(icc);
	}
}
