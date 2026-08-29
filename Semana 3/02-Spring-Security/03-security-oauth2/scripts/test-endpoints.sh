#!/bin/bash
# Matriz de seguridad de 03-security-oauth2 (OAuth2 + Keycloak)
# API en 8073. Keycloak en 8090. Usuarios john/mary/susan, password test123.
#
# Diferencia clave con los proyectos 01 y 02: esta API NO tiene usuarios.
# El token lo pides a Keycloak; la API solo lo valida.

BASE="http://localhost:8073/api/employees"
KC="http://localhost:8090/realms/academy/protocol/openid-connect"

paso() { echo; echo "════════ $1 ════════"; }

token() {
  curl -s -X POST "$KC/token" \
    -d grant_type=password -d client_id=employee-api \
    -d username="$1" -d password=test123 \
    | python3 -c "import json,sys;print(json.load(sys.stdin)['access_token'])" 2>/dev/null
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

paso "1. Lo que la API descarga sola del emisor"
echo "→ curl $KC/../.well-known/openid-configuration"
curl -s "http://localhost:8090/realms/academy/.well-known/openid-configuration" \
  | python3 -c "
import sys,json; d=json.load(sys.stdin)
for k in ('issuer','token_endpoint','jwks_uri'): print(f'  {k}: {d[k]}')"
echo "  Esa jwks_uri es la llave publica. La API la baja sola: no hay .pem en el proyecto."

paso "2. Pedir el token A KEYCLOAK (no a la API)"
TJ=$(token john); TM=$(token mary); TS=$(token susan)
echo "→ curl -X POST $KC/token -d grant_type=password -d client_id=employee-api ..."
echo "  token de john: ${#TJ} caracteres (el del proyecto 02 medía ~490)"
echo "$TJ" | cut -d. -f2 | python3 -c "
import sys,base64,json
s=sys.stdin.read().strip(); s+='='*(-len(s)%4)
p=json.loads(base64.urlsafe_b64decode(s))
print('  iss:', p['iss'])
print('  sub:', p['sub'], ' <- un UUID, no \"john\"')
print('  preferred_username:', p.get('preferred_username'))
print('  realm_access.roles:', p['realm_access']['roles'])"

paso "3. Usar el token contra la API"
probar "sin token"                          401 "$BASE"
probar "token de john (EMPLOYEE) GET"       200 -H "Authorization: Bearer $TJ" "$BASE"
probar "token de john POST"                 403 -H "Authorization: Bearer $TJ" -X POST -H 'Content-Type: application/json' -d '{"firstName":"X","lastName":"Y","email":"x@y.com"}' "$BASE"
probar "token de john DELETE"               403 -H "Authorization: Bearer $TJ" -X DELETE "$BASE/1"

paso "4. HTTP Basic ya no existe en esta API"
probar "curl -u susan:test123"              401 -u susan:test123 "$BASE"
echo "Y no podria existir: esta API no tiene tabla de usuarios ni contrasenas."

paso "5. Los roles siguen mandando (ahora vienen de Keycloak)"
NUEVO=$(curl -s -H "Authorization: Bearer $TM" -X POST "$BASE" -H "Content-Type: application/json" \
  -d '{"firstName":"Temp","lastName":"Oauth","email":"temp@oauth.com"}')
echo "$NUEVO"
ID=$(echo "$NUEVO" | sed -n 's/.*"id":\([0-9]*\).*/\1/p')
echo "  ➜ empleado temporal creado con id: $ID"
probar "DELETE con token de mary (MANAGER)" 403 -H "Authorization: Bearer $TM" -X DELETE "$BASE/$ID"
probar "DELETE con token de susan (ADMIN)"  200 -H "Authorization: Bearer $TS" -X DELETE "$BASE/$ID"
ID=""

paso "6. Un token VALIDO, pero del emisor equivocado"
echo "Si tienes el proyecto 02 corriendo en el 8072, su token es un JWT perfecto..."
T02=$(curl -s -u "john:test123" -X POST http://localhost:8072/api/auth/login \
      | python3 -c "import json,sys;print(json.load(sys.stdin)['accessToken'])" 2>/dev/null)
if [ -n "$T02" ]; then
  probar "token emitido por el proyecto 02"  401 -H "Authorization: Bearer $T02" "$BASE"
  curl -s -D - -o /dev/null -H "Authorization: Bearer $T02" "$BASE" | grep -i www-authenticate
  echo "...pero lo firmo OTRA llave privada. La confianza no es en 'un JWT': es en QUIEN lo firmo."
else
  echo "  (el proyecto 02 no esta corriendo; arrancalo en el 8072 para ver esta prueba)"
fi

paso "7. Estado final (debe ser igual al inicial)"
curl -s -H "Authorization: Bearer $TS" "$BASE"; echo
