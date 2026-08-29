package com.curso.v0;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;

/**
 * EL ERROR QUE COMETEN TODOS, y que con seis parametros es casi inevitable.
 *
 * En una misma llamada, o TODOS los argumentos son matchers (anyByte(),
 * anyInt()...) o TODOS son valores. Mezclar no compila mal: compila bien y
 * revienta en ejecucion con un mensaje que hay que saber leer.
 *
 * Este test deja el error por escrito, en verde, para que se reconozca cuando
 * aparezca de verdad.
 */
@DisplayName("La trampa: mezclar matchers con valores crudos")
class TrampaDeMatchersTest {

	@Test
	@DisplayName("Cinco matchers y un valor crudo: InvalidUseOfMatchersException")
	void mezclarMatchersConValoresEsUnError() {
		ICalculoComplejo icc = mock(ICalculoComplejo.class);

		// El (char) 100 es un valor crudo entre cinco matchers. Mockito solo
		// registra 5 matchers para una llamada de 6 argumentos, y se planta:
		//   "Invalid use of argument matchers! 6 matchers expected, 5 recorded"
		assertThrows(InvalidUseOfMatchersException.class,
				() -> when(icc.ejecutaCalculoComplejo(anyByte(), (char) 100, anyShort(),
						anyInt(), anyLong(), anyFloat())).thenReturn(1.0));

		// La forma correcta seria eq((char) 100) en lugar del valor pelado.
	}

	@Test
	@DisplayName("Todos matchers: asi si")
	void todosMatchersFunciona() {
		ICalculoComplejo icc = mock(ICalculoComplejo.class);

		when(icc.ejecutaCalculoComplejo(anyByte(), org.mockito.ArgumentMatchers.anyChar(),
				anyShort(), anyInt(), anyLong(), anyFloat())).thenReturn(42.0);

		org.junit.jupiter.api.Assertions.assertEquals(42.0,
				new ServiceCalculoImpuesto(icc).calcularImpuesto());
	}
}
