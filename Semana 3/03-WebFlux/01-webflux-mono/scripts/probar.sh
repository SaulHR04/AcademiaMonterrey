#!/usr/bin/env bash
# Recorrido por todos los endpoints del proyecto 01.
# Arranca antes la app:  ./mvnw spring-boot:run
BASE="http://localhost:8074"

titulo() { printf '\n\033[1;36m== %s ==\033[0m\n' "$1"; }

titulo "1. Un empleado que SI existe  (200)"
curl -s -w '\n   -> HTTP %{http_code} en %{time_total}s\n' "$BASE/api/employees/1"

titulo "2. Un empleado que NO existe  (404 con mensaje)"
curl -s -w '\n   -> HTTP %{http_code}\n' "$BASE/api/employees/999"

titulo "3. TRAMPA: un Mono vacio NO da 404, da 200 vacio"
curl -s -w '   -> HTTP %{http_code} <- 200, no 404. El vacio no es un error.\n' "$BASE/api/employees-suave/999"
echo "      Si quieres 404, tienes que pedirlo con switchIfEmpty() (endpoint 2)."

titulo "4. El canal de error: truena, pero onErrorResume lo rescata"
curl -s -w '\n   -> HTTP %{http_code} (fijate en el plan B)\n' "$BASE/api/employees/1/boom"

titulo "5. Mono<Void>: no devuelve nada, solo avisa que termino"
curl -s -w '   -> HTTP %{http_code} sin cuerpo\n' -X DELETE "$BASE/api/employees/1"

titulo "6. Quien te atiende (llamalo varias veces)"
for i in 1 2 3 4 5; do curl -s "$BASE/api/hilo" | tr ',' '\n' | grep '"hilo"'; done
echo '   -> Se repiten los mismos nombres: eso es el event loop.'
