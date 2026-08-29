#!/usr/bin/env bash
# Recorrido por el proyecto 02.
# Arranca antes la app:  ./mvnw spring-boot:run
BASE="http://localhost:8075"
titulo() { printf '\n\033[1;36m== %s ==\033[0m\n' "$1"; }

titulo "A. application/json  -- vas a esperar 5 segundos EN SILENCIO"
echo "   (mira el reloj: no aparece nada hasta el final)"
time curl -s "$BASE/api/lecturas" | tr '}' '}\n'

titulo "B. text/event-stream -- el MISMO Flux, una lectura por segundo"
echo "   (la -N de curl desactiva el buffer; sin ella verias lo mismo que en A)"
curl -N -s "$BASE/api/lecturas/stream" | head -8

titulo "C. filter() sobre un flujo vivo: solo por encima de 30 C"
curl -N -s --max-time 12 "$BASE/api/lecturas/alertas?umbral=30" | head -5

titulo "D. takeUntil(): se cierra SOLO cuando baja de 20 C"
echo "   (fijate en que el curl termina sin que aprietes Ctrl-C)"
curl -N -s --max-time 30 "$BASE/api/lecturas/hasta/20" | tail -3

titulo "E. collectList(): el Flux se colapsa en UN solo valor"
curl -s "$BASE/api/lecturas/resumen"
echo

printf '\n\033[1;33mY ahora lo importante:\033[0m abre http://localhost:8075 en el navegador\n'
printf 'y dale a los dos botones a la vez. Eso es lo que no se ve en una terminal.\n\n'
