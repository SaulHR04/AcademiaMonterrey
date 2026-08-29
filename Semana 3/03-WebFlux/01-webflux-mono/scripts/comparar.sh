#!/usr/bin/env bash
# EL EXPERIMENTO DEL CURSO.
#
# Dos endpoints que devuelven EXACTAMENTE el mismo dato, con EXACTAMENTE la misma
# latencia simulada (5 s). Lo unico que cambia es si el hilo se queda esperando.
#
# LAT se LEE del codigo, no se escribe aqui. Antes era un numero a mano con un
# aviso de "si cambias la constante, cambia esto" -- y se desincronizo dos veces
# el mismo dia. Un recordatorio no es una garantia: la fuente de la verdad es
# EmployeeRepository.LATENCIA y punto. Solo se usa para la prediccion impresa.
#
# Arranca antes la app:  ./mvnw spring-boot:run
BASE="http://localhost:8074"
N=${1:-50}

FUENTE="$(dirname "$0")/../src/main/java/com/academymty/webflux/mono/repo/EmployeeRepository.java"
LAT_MS=$(grep -oE 'ofMillis\([0-9]+\)' "$FUENTE" | grep -oE '[0-9]+' | head -1)
if [ -z "$LAT_MS" ]; then
  echo "No pude leer LATENCIA de $FUENTE. Revisa que siga siendo Duration.ofMillis(N)." >&2
  exit 1
fi
LAT=$(echo "scale=3; $LAT_MS/1000" | bc)

NUCLEOS=$(curl -s "$BASE/api/hilo" | tr ',' '\n' | grep hilosDisponibles | tr -dc '0-9')
[ -z "$NUCLEOS" ] && { echo "No responde $BASE — ¿arrancaste la app con ./mvnw spring-boot:run?"; exit 1; }

medir() {
  local ruta="$1" nombre="$2" ini fin
  ini=$(date +%s.%N)
  for _ in $(seq 1 "$N"); do curl -s -o /dev/null "$BASE$ruta" & done
  wait
  fin=$(date +%s.%N)
  printf '  %-12s %3d peticiones en \033[1m%6.2f s\033[0m\n' "$nombre" "$N" "$(echo "$fin - $ini" | bc)"
}

esperado_bloq=$(echo "scale=2; ($N / $NUCLEOS) * $LAT" | bc)

cat <<TXT

  Tu maquina tiene $NUCLEOS nucleos, asi que el event loop de Netty tiene
  ~$NUCLEOS hilos. Lanzamos $N peticiones CONCURRENTES a cada ruta.

  Prediccion antes de correrlo:
    reactivo    -> ~${LAT}s   (ningun hilo espera: las $N se solapan)
    bloqueante  -> ~${esperado_bloq}s   ($N peticiones / $NUCLEOS hilos = $(echo "scale=1; $N/$NUCLEOS" | bc) tandas de ${LAT}s)

TXT

medir "/api/employees/1"     "reactivo"
medir "/api/mvc/employees/1" "bloqueante"

cat <<TXT

  ¿Cuadro la prediccion? Si tu maquina tiene mas nucleos, la diferencia es menor;
  si tiene menos, es brutal. Prueba con:  ./scripts/comparar.sh 200

  La leccion NO es "reactivo es rapido". Las dos rutas tardan 5 s en el dato.
  La leccion es que el bloqueante DESPERDICIA los $NUCLEOS hilos que tiene, durmiendolos,
  y por eso las peticiones hacen cola. El reactivo los suelta y no encola nada.

  Y ojo: el bloqueante no es un endpoint raro que inventamos. Es EXACTAMENTE el
  codigo del proyecto 15 pegado dentro de una app WebFlux. Esa es la trampa del
  §07 de la guia: si tu repositorio bloquea (JPA/JDBC), WebFlux no te da nada.
TXT
