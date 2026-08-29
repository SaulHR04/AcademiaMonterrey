# 02-Spring-Security

### 2.1 HTTP BASIC 
Http basic es el modo de autenticacion más básico como su nombre lo dice en el cual se define en el protocolo HTTP.

En donde el usuario es unido solamente con su usuario y contraseña para asi generar su credencial en una cadena codificada con Base64.


### Prueba PowerShell Sin credenciales.
```shell
curl.exe -i http://localhost:8071/api/employees
```
Error 401 ya que no permite iniciar sin credenciales.
![alt text](img/image.png)

### Qué pasa si no lo usas:
Si no se utiliza aunque sea seguridad básica cualquier persona con el URL podria modificar tu Base de Datos y eliminar registros, al tener nuestro Http basic nos aseguramos que solo las credenciales correspondientes puedan acceder a la BD.
```java
@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PATCH, "/api/employees/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.httpBasic(Customizer.withDefaults());
```
Con el código de filterChain nos aseguramos que solo el rol que esta en **.hasRole** tenga acceso a las rutas con las cual es tiene permiso.
**HttpsBasic** exige que cada petición vaya con las credenciales para hacer posible la accion.

### Prueba PowerShell con credenciales de lectura.
```shell
curl.exe -i -u john:test123 http://localhost:8071/api/employees
```
![alt text](img/image-1.png)

### **Bcrypt en BD**

Gracias a bcrypt obtenemos nuestra credenciales Hasheadas como se puede ver en la imagen. Esto en vez de que aparezca directamente las credenciales usadas como contraseña en caso de que se intente colocar credenciales sin el hasheo bcrypt el sistema debera bloquearlo automaticamente.

![alt text](img/image-2.png)
--------------------------------