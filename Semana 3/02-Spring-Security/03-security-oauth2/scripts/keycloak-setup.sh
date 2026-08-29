#!/bin/bash
# Configura desde cero el realm "academy" en Keycloak.
# Es re-ejecutable: si algo ya existe, Keycloak lo dira y el script sigue.
#
# Requisito: el contenedor keycloak-academy corriendo (ver instalacion.txt).

set -u
kc() { docker exec keycloak-academy /opt/keycloak/bin/kcadm.sh "$@"; }

echo "→ esperando a que Keycloak responda..."
for i in $(seq 1 90); do
  [ "$(curl -s -o /dev/null -w '%{http_code}' -m 2 http://localhost:8090/realms/master)" = "200" ] && break
  sleep 1
done

echo "→ autenticando como admin"
kc config credentials --server http://localhost:8080 --realm master --user admin --password admin

echo "→ realm academy"
kc create realms -s realm=academy -s enabled=true

echo "→ client employee-api"
# publicClient           = sin secreto (es un cliente que no puede guardar secretos)
# directAccessGrantsEnabled = permite pedir el token con usuario y password desde curl.
#                          Se habilita SOLO para poder practicar en la terminal.
#                          El flujo de verdad es Authorization Code + PKCE, con navegador.
kc create clients -r academy -s clientId=employee-api -s enabled=true \
  -s publicClient=true -s directAccessGrantsEnabled=true -s standardFlowEnabled=true \
  -s 'redirectUris=["http://localhost:8073/*"]'

echo "→ roles"
for r in EMPLOYEE MANAGER ADMIN; do kc create roles -r academy -s name=$r; done

echo "→ usuarios"
for u in john mary susan; do
  kc create users -r academy -s username=$u -s enabled=true \
     -s "email=${u}@academy.mx" -s "firstName=${u}" -s lastName=Demo -s emailVerified=true
  # --temporary=false es OBLIGATORIO. Sin eso Keycloak marca la password como
  # "hay que cambiarla al entrar" y el login por curl falla con un mensaje
  # que no ayuda nada: "Account is not fully set up".
  kc set-password -r academy --username $u --new-password test123 --temporary=false
done

echo "→ asignando roles"
kc add-roles -r academy --uusername john  --rolename EMPLOYEE
kc add-roles -r academy --uusername mary  --rolename EMPLOYEE --rolename MANAGER
kc add-roles -r academy --uusername susan --rolename EMPLOYEE --rolename MANAGER --rolename ADMIN

echo
echo "Listo. Comprueba:"
echo "  curl -s http://localhost:8090/realms/academy/.well-known/openid-configuration | head -c 200"
echo "  Consola web: http://localhost:8090  (admin / admin)"
