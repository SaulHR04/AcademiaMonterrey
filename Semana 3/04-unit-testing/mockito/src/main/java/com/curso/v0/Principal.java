package com.curso.v0;

public class Principal {

	public static void main(String[] args) {

		// RESUELTO: no se resuelve AQUI, y esa es la leccion.
		//
		// Mockito es <scope>test</scope> en el pom: no existe en src/main/java,
		// porque un doble de prueba jamas debe viajar a produccion. Asi que este
		// null se queda, y este main revienta con NullPointerException.
		//
		// Quien rellena este hueco depende de donde estes:
		//   - en PRODUCCION, el framework (el //@Autowired de abajo): Spring busca
		//     quien implementa ICalculoComplejo -- la clase del tercero -- y la
		//     inyecta por el constructor;
		//   - en el TEST, Mockito: ver src/test/java, InyeccionTest.
		//
		// Ejecuta este main y veras el problema. Ejecuta `./mvnw test` y veras
		// que el servicio esta probado entero pese a que el problema sigue ahi.
		ICalculoComplejo icc = null;

		ServiceCalculoImpuesto service = new ServiceCalculoImpuesto(icc);

		System.out.println(service.calcularImpuesto());

	}

}
