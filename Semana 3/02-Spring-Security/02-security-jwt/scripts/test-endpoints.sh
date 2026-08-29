#!/bin/bash
# Matriz de seguridad de 02-security-jwt (JWT + roles)
# Puerto 8072. Los 3 usuarios tienen la misma password: test123
#
# La diferencia con el proyecto 01: la contrasena solo viaja UNA vez, al /login.
# Todo lo demas va con "Authorization: Bearer <token>".

BASE="http://localhost:8072/api/employees"
LOGIN="http://localhost:8072/api/auth/login"

paso() { echo; echo "════════ $1 ════════"; }

# OJO con el quoting: en zsh, "$1:test123" activa un modificador de historia
# y te deja sin credenciales. Hay que escribir "${1}:test123".
token() {
  curl -s -u "${1}:test123" -X POST "$LOGIN" \
    | python3 -c "import json,sys;print(json.load(sys.stdin)['accessToken'])" 2>/dev/null
}

payload() {  # imprime el contenido del token, sin ninguna llave: solo base64
  echo "$1" | cut -d. -f2 | python3 -c "
import sys,base64,json
s=sys.stdin.read().strip(); s+='='*(-len(s)%4)
print(json.dumps(json.loads(base64.urlsafe_b64decode(s)), indent=2))"
}

probar() {  # probar DESCRIPCION ESPERADO curl-args...
  local desc=$1 esp=$2; shift 2
  local code=$(curl -s -o /dev/null -w '%{http_code}' "$@")
  local marca="!!"; [ "$code" = "$esp" ] && marca="OK"
  printf "%s  %-48s -> HTTP %s  (esperado %s)\n" "$marca" "$desc" "$code" "$esp"
}

ID=""
limpiar() { [ -n "$ID" ] && curl -s -o /dev/null -H "Authorization: Bearer $TS" -X DELETE "$BASE/$ID"; }
trap limpiar EXIT

paso "1. Login: cambiar credenciales por un token"
probar "login con password mala"   401 -u john:MALA -X POST "$LOGIN"
probar "login sin credenciales"    401 -X POST "$LOGIN"
TJ=$(token john); TM=$(token mary); TS=$(token susan)
echo "→ curl -u john:test123 -X POST $LOGIN"
echo "$TJ" | head -c 80; echo "..."

paso "2. Lo que ese token lleva dentro"
echo "Nadie necesita una llave para leer esto. Va FIRMADO, no cifrado:"
payload "$TJ"
echo "Por eso NUNCA se ponen datos secretos en un JWT."

paso "3. Usar el token"
probar "GET sin token"                      401 "$BASE"
probar "GET con token de john"              200 -H "Authorization: Bearer $TJ" "$BASE"
probar "POST con token de john (EMPLOYEE)"  403 -H "Authorization: Bearer $TJ" -X POST -H 'Content-Type: application/json' -d '{"firstName":"X","lastName":"Y","email":"x@y.com"}' "$BASE"
probar "DELETE con token de john"           403 -H "Authorization: Bearer $TJ" -X DELETE "$BASE/1"

paso "4. Lo que YA NO funciona (esta es la leccion del proyecto)"
probar "HTTP Basic contra /api/employees"   401 -u susan:test123 "$BASE"
echo "La cadena 2 solo entiende Bearer. La contrasena ya no abre esta puerta."

paso "5. Manipular el token"
# IMPORTANTE: se cambia un caracter del MEDIO de la firma, no el ultimo.
# Al ultimo caracter de la firma le sobran 4 bits, asi que 15 de 63 sustituciones
# decodifican a la MISMA firma y el token seguiria siendo valido (~24% de las veces).
SIG=$(echo "$TJ" | cut -d. -f3)
MID=$(python3 -c "
s='$SIG'; c='A' if s[10]!='A' else 'B'; print(s[:10]+c+s[11:])")
TBAD="$(echo "$TJ" | cut -d. -f1).$(echo "$TJ" | cut -d. -f2).$MID"
probar "firma alterada (1 caracter del medio)" 401 -H "Authorization: Bearer $TBAD" "$BASE"

FALSO="$(echo "$TJ" | cut -d. -f1).$(echo -n '{"iss":"security-jwt","sub":"john","exp":9999999999,"roles":["ROLE_ADMIN"]}' | base64 | tr -d '=' | tr '/+' '_-').$(echo "$TJ" | cut -d. -f3)"
probar "payload reescrito a ROLE_ADMIN"        401 -X DELETE -H "Authorization: Bearer $FALSO" "$BASE/1"
echo "Puedes reescribir el payload todo lo que quieras: sin la llave privada"
echo "no puedes recalcular la firma, y el servidor lo detecta."

paso "6. Los roles siguen mandando (van dentro del token)"
NUEVO=$(curl -s -H "Authorization: Bearer $TM" -X POST "$BASE" -H "Content-Type: application/json" \
  -d '{"firstName":"Temp","lastName":"Jwt","email":"temp@jwt.com"}')
echo "$NUEVO"
ID=$(echo "$NUEVO" | sed -n 's/.*"id":\([0-9]*\).*/\1/p')
echo "  ➜ empleado temporal creado con id: $ID"
probar "DELETE con token de mary (MANAGER)"  403 -H "Authorization: Bearer $TM" -X DELETE "$BASE/$ID"
probar "DELETE con token de susan (ADMIN)"   200 -H "Authorization: Bearer $TS" -X DELETE "$BASE/$ID"
ID=""

paso "7. Estado final (debe ser igual al inicial)"
curl -s -H "Authorization: Bearer $TS" "$BASE"; echo

echo
echo "════════ SOBRE LA CADUCIDAD ════════"
echo "  Para verla en vivo, arranca con un TTL corto:"
echo "    java -jar target/security-jwt-0.0.1-SNAPSHOT.jar --jwt.ttl-seconds=3"
echo "  ...y NO esperes 3 segundos: espera 65."
echo "  Spring tolera 60 segundos de desfase de reloj por defecto"
echo "  (JwtTimestampValidator), pensando en servidores con relojes distintos."
