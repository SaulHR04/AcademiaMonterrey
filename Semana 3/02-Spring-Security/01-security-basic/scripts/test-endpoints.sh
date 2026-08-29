#!/bin/bash
# Matriz de seguridad de 01-security-basic (HTTP Basic + roles)
# Puerto 8071. Los 3 usuarios tienen la misma password: test123
#
#   john   ROLE_EMPLOYEE                          -> solo puede LEER
#   mary   ROLE_EMPLOYEE + ROLE_MANAGER           -> lee, crea y modifica
#   susan  ROLE_EMPLOYEE + ROLE_MANAGER + ADMIN   -> ademas puede BORRAR
#
# El script crea un empleado temporal y lo borra al final: tus datos quedan igual.

BASE="http://localhost:8071/api/employees"

paso() { echo; echo "════════ $1 ════════"; }

# probar USUARIO:PASS METODO URL [JSON] [esperado]
probar() {
  local cred=$1 m=$2 u=$3 d=$4 esp=$5
  local salida code
  if [ -n "$d" ]; then
    salida=$(curl -s -w "\n%{http_code}" -u "$cred" -X "$m" "$u" -H "Content-Type: application/json" -d "$d")
  else
    salida=$(curl -s -w "\n%{http_code}" -u "$cred" -X "$m" "$u")
  fi
  code=$(echo "$salida" | tail -1)
  local marca="  "
  [ -n "$esp" ] && { [ "$code" = "$esp" ] && marca="OK" || marca="!!"; }
  printf "%s  %-22s %-6s -> HTTP %s   (esperado %s)\n" "$marca" "${cred%%:*}" "$m" "$code" "${esp:-?}"
  echo "$salida" | sed '$d' | head -c 160; echo
}

paso "1. Sin credenciales: la API ya no esta abierta"
echo "→ curl -i $BASE   (fijate en la cabecera WWW-Authenticate)"
curl -s -D - -o /dev/null "$BASE" | head -2

paso "2. Credenciales invalidas -> 401 (no se quien eres)"
probar "john:PASSWORD_MALA" GET "$BASE" "" 401

paso "3. john = ROLE_EMPLOYEE: puede LEER"
probar "john:test123" GET "$BASE" "" 200

paso "4. john intenta CREAR -> 403 (se quien eres, pero no te toca)"
probar "john:test123" POST "$BASE" '{"firstName":"X","lastName":"Y","email":"x@y.com"}' 403

paso "5. john intenta BORRAR -> 403"
probar "john:test123" DELETE "$BASE/1" "" 403

# Red de seguridad: si el script se interrumpe (Ctrl-C, o la salida se corta con
# head/less), borramos igual el empleado temporal para no dejar basura en la tabla.
ID=""
limpiar() { [ -n "$ID" ] && curl -s -o /dev/null -u susan:test123 -X DELETE "$BASE/$ID"; }
trap limpiar EXIT

paso "6. mary = ROLE_MANAGER: si puede CREAR"
NUEVO=$(curl -s -u mary:test123 -X POST "$BASE" -H "Content-Type: application/json" \
  -d '{"firstName":"Temp","lastName":"Borrame","email":"temp@test.com"}')
echo "$NUEVO"
ID=$(echo "$NUEVO" | sed -n 's/.*"id":\([0-9]*\).*/\1/p')
echo "  ➜ empleado temporal creado con id: $ID"

paso "7. mary intenta BORRAR -> 403 (crear no implica borrar)"
probar "mary:test123" DELETE "$BASE/$ID" "" 403

paso "8. susan = ROLE_ADMIN: si puede BORRAR"
probar "susan:test123" DELETE "$BASE/$ID" "" 200
ID=""   # ya lo borramos: la limpieza automatica no tiene nada que hacer

paso "9. Lo que curl -u manda en realidad"
echo "→ curl -v -u john:test123 $BASE  | grep Authorization"
curl -s -o /dev/null -v -u john:test123 "$BASE" 2>&1 | grep -i "^> authorization:"
echo
echo "Eso NO esta cifrado, solo empaquetado en base64. Se revierte en un comando:"
echo "→ echo -n 'am9objp0ZXN0MTIz' | base64 -d"
echo -n 'am9objp0ZXN0MTIz' | base64 -d; echo
echo
echo "Moraleja: HTTP Basic SIN HTTPS = mandar la password en texto plano."

paso "10. Estado final (debe ser igual al inicial)"
curl -s -u susan:test123 "$BASE"; echo

echo
echo "════════ RESUMEN ════════"
echo "  401 = no se quien eres      (fallo de AUTENTICACION)"
echo "  403 = se quien eres, pero no puedes  (fallo de AUTORIZACION)"
