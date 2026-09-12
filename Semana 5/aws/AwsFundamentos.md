# AWS Fundamentos 

**Proyecto:** TaskFlow API  
**Servicios utilizados:** AWS Budgets, IAM, EC2, RDS y S3  


## Objetivo

En esta práctica, se llevó la API TaskFlow a AWS. Primero se crearon controles básicos de seguridad y costos. Después se preparó una máquina EC2, se instaló Java, se subió el archivo JAR y se levantó la aplicación. Finalmente se creó una base de datos PostgreSQL en RDS y un bucket privado en S3.

## 1. Presupuesto y acceso a AWS

Se configuró un presupuesto mensual para ayudar a recibir alertas antes de gastar más de lo esperado.

![Presupuesto configurado en AWS](Capturas/Budget.PNG)

**Captura 1.** Se creó el presupuesto mensual para controlar el costo de los recursos utilizados durante la práctica.

![Perfil del usuario IAM](Capturas/Perfil_IAM.PNG)

**Captura 2.** Se verificó el perfil del usuario IAM con el que se ingresó a la consola de AWS.

![Rol de IAM](Capturas/Rol.PNG)

**Captura 3.** Se revisó la configuración de permisos o rol de IAM, que permite asignar accesos de forma controlada en AWS.

## 2. Creación de la instancia EC2

Se creó una instancia EC2 con Amazon Linux. Esta instancia funciona como un servidor remoto donde se ejecuta la API. Durante el proceso se eligió un nombre para la instancia, se generó una llave privada y se configuraron las reglas de red necesarias.

![Formulario de creación de la instancia](Capturas/Instance.PNG)

**Captura 4.** Se configuraron los datos principales de la instancia EC2 antes de lanzarla.

![Creación del key pair](Capturas/Keypair.PNG)

**Captura 5.** Se creó el key pair. El archivo **.pem** descargado sirve para comprobar la identidad al conectarse por SSH.

![Configuración de red de EC2](Capturas/Network_Setting.PNG)

**Captura 6.** Se configuró el security group de EC2. Se habilitó SSH para administrar la máquina y el puerto 8080 para consultar la API.

![Instancia lanzada](Capturas/instancia_Lanzada.PNG)

**Captura 7.** AWS confirmó que la solicitud para lanzar la instancia fue enviada correctamente.

![Instancia EC2 en ejecución](Capturas/Instancia_Running.PNG)

**Captura 8.** Se comprobó que la instancia ya estaba en estado **Running**, por lo que el servidor estaba listo para usarse.

## 3. Conexión remota e instalación de Java

Para entrar al servidor, se utilizó la llave privada y el usuario **ec2-user**. Una vez dentro de EC2, se instaló Amazon Corretto 21, que es la versión de Java necesaria para ejecutar el JAR de TaskFlow.

![Carpeta preparada para la conexión](Capturas/CarpetaConexion.PNG)

**Captura 9.** Se preparó la carpeta que contiene la llave necesaria para conectarse de forma segura a la instancia.

![Conexión SSH a EC2](Capturas/Conecction_SSH.PNG)

**Captura 10.** Se realizó la conexión por SSH a la instancia EC2. 

![Instalación de Java Corretto 21](Capturas/InstalacionJava_Corretto21.PNG)

**Captura 11.** Se instaló Java 21 Amazon Corretto y se verificó la versión desde la terminal del servidor.

## 4. Transferencia y primer arranque de la API

Se compiló TaskFlow en el equipo local y se transfirió el archivo JAR a EC2. 

![Archivo JAR listo para subir](Capturas/SubirJar.PNG)

**Captura 12.** Se seleccionó el archivo JAR generado por el proyecto para transferirlo al servidor.

![JAR almacenado en la máquina EC2](Capturas/JarAMaquina.PNG)

**Captura 13.** Se comprobó que el archivo **taskflow-api.jar** ya se encontraba en la instancia EC2.

![Primer arranque de la aplicación](Capturas/PrimerArranque.PNG)

**Captura 14.** Se inició la API con Java y se revisó el registro de **Auth/login** para confirmar que el arranque fue correcto.

--------------------------------------------------------------------------
## 5. Creación de la base de datos RDS PostgreSQL

Se creó una base de datos PostgreSQL en Amazon RDS. RDS permite guardar los datos fuera de la instancia EC2. Así, los datos no dependen del disco de una sola máquina virtual..

![Servicio RDS en AWS](Capturas/RDS.PNG)

**Captura 15.** Se ingresó al servicio RDS para comenzar la creación de la base de datos.

![Configuración general de RDS](Capturas/Configuracion_RDS.PNG)

**Captura 16.** Se eligió PostgreSQL y se configuraron los datos generales de la instancia de base de datos.

![Configuración de la base de datos](Capturas/Configuracion_RDS_DB.PNG)

**Captura 17.** Se completó la configuración de credenciales, almacenamiento y conectividad de la base de datos RDS.

![Base de datos PostgreSQL creada](Capturas/PostgreeBD_Created.PNG)

**Captura 18.** Se verificó que la base de datos PostgreSQL fue creada y quedó disponible en RDS.

## 6. Bucket privado en S3

Se creó un bucket de S3 para guardar el JAR como artefacto. El bucket se dejó privado: abrir la URL directa de un objeto debe mostrar un error de acceso. Esto es correcto porque el archivo no fue publicado para todo Internet.

![Primer paso de creación del bucket](Capturas/CreacionBucket2.0.PNG)

**Captura 19.** Se inició la creación del bucket de S3 y se definieron sus datos básicos.

![Configuración final del bucket](<Capturas/CreacionBucker(S2).PNG>)

**Captura 20.** Se dejó activado el bloqueo de acceso público antes de crear el bucket.

![Error al abrir la URL directa del objeto](Capturas/ObjectUrlError.PNG)

**Captura 21.** Se comprobó que la URL directa del objeto responde con acceso denegado. Esto demuestra que el bucket continúa privado.

## 7. Conexión segura entre EC2 y RDS

Para que la API pudiera usar PostgreSQL, se abrió el puerto 5432 en el security group de RDS. La regla añadida es al security group de EC2, no a una IP pública. Con esto solo la instancia autorizada puede intentar conectarse a la base de datos.

![Regla de entrada para PostgreSQL](Capturas/AgregarInboundSecurity.PNG)

**Captura 22.** Se agrego la regla de entrada para PostgreSQL en el puerto 5432 y se autorizó como origen al security group de EC2.

![Generación del secreto JWT](Capturas/JWTTokenGen.PNG)

**Captura 23.** Se genero un token JWT nuevo para la ejecución de la API. 

![Arranque con configuración JWT](Capturas/Arranque_jwttoken.PNG)

**Captura 24.** Se inició la aplicación usando las variables necesarias, incluyendo la configuración del token JWT.

![Arranque de la API con PostgreSQL](Capturas/Arranque-PostGRE.PNG)

**Captura 25.** Se levantó TaskFlow conectada a PostgreSQL en RDS. Con esto, la API dejó de depender de una base de datos local dentro de EC2.

##  Repositorio y conexión

 Se creo un repositorio en GitHub para guardar el codigo de TaskFlow. Después se hizo el primer commit y se creo una conexión entre AWS y GitHub mediante AWS CodeConnections. Esta conexión permitirá que AWS consulte el repositorio sin exponer contraseñas ni tokens dentro del proyecto.

### 1. Creación del repositorio

![Repositorio nuevo en GitHub](Capturas/2.Repo.PNG)

**Captura 26.** Se creo el repositorio **taskflow-aws-saulhr04** en GitHub. En este punto todavía no contenía archivos.

![Primer commit en el repositorio](Capturas/2.RepoPrimerCommit.PNG)

**Captura 27.** Se realizó el primer commit y se subieron los archivos iniciales de TaskFlow al repositorio.

### 2. Conexión de AWS con GitHub

![Pantalla para crear una conexión](Capturas/2.CrearConnection.PNG)

**Captura 28.** Se ingresó a AWS CodeConnections y se selecciono la opción para crear una conexión nueva con un proveedor de código.

![Autorización de GitHub](Capturas/2.ConeccionConRepoGithub.PNG)

**Captura 29.** Se autorizó el acceso de AWS a la cuenta de GitHub. 

![Conexión disponible con GitHub](Capturas/2.ConeccionConRepoGithubExitoso.PNG)

**Captura 30.** Se confirmó que la conexión **github-taskflow** quedó disponible. Esta será utilizada más adelante para enlazar el repositorio con los servicios de despliegue de AWS.

##  Prework para DynamoDB y practica con DynamoDB

Antes de crear la tabla se creó el rol que utilizará la nueva instancia EC2 durante el siguiente despliegue y se relanzó la instancia con dicho rol.

### 1. Configuración de credenciales para AWS CLI

La AWS CLI necesita credenciales del usuario IAM para realizar acciones en la cuenta..

![Selección del uso de la access key](Capturas/3.CreacionDeAccesKey.PNG)

**Captura 31.** Se inició la creación de una access key para el usuario **taskflow-admin** y se seleccionó el uso mediante Command Line Interface (CLI).

![Recuperación de credenciales del usuario IAM](Capturas/3.DescargarCredencialesDeIAMUser.PNG)

**Captura 32.** Se recuperaron las credenciales para configurar la CLI. 

![Configuración de AWS CLI](Capturas/3.AwsConfiguracionCLI.PNG)

**Captura 33.** Se ejecutó **aws configure** y se definieron las credenciales, la región de trabajo y el formato de salida JSON.

![Validación de AWS CLI](Capturas/3.AwsConfiguracionCLIResultado.PNG)

**Captura 34.** Se validó la configuración con **aws sts get-caller-identity**. El resultado confirmó que la CLI quedo autentificada con el usuario IAM correcto.

### 2. Rol y nueva instancia EC2

Para el siguiente despliegue se creó el rol **taskflow-ec2-role**. Este rol da permiso necesario para leer artefactos desde S3. 

![Selección de política para el rol](Capturas/3.DynamoCrearRol.PNG)

**Captura 35.** Se ingresó a la sección de roles de IAM para iniciar la creación del rol que utilizará la instancia EC2.

![Rol de EC2 creado](Capturas/3.RolCreado.PNG)

**Captura 36.** Se creó el rol **taskflow-ec2-role** para que pueda ser asignado a una instancia EC2.

![Propiedades del rol de EC2](Capturas/3.RolPropiedades.PNG)

**Captura 37.** Se verificó que el rol tiene solo lectura para S3 antes de utilizarlo en EC2.

![Nueva instancia EC2 lanzada](Capturas/3.InstanciaLanzada.PNG)

**Captura 38.** Se lanzó una nueva instancia EC2.

### 3. Creación de la tabla DynamoDB

 Para esta practica se creo la tabla **taskflow-eventos** con la clave de partición es **taskId**.

![Comando para crear la tabla DynamoDB](Capturas/3.CrearTablaDynamo.PNG)

**Captura 39.** Se verifica si se creo la tabla **taskflow-eventos** desde la AWS CLI. 

![Tabla DynamoDB activa](Capturas/3.TablaEnAWSFuncionando.PNG)

**Captura 40.** Se comprobó en la consola que la tabla quedó en estado **Active**..

![Consulta scan sobre eventos](Capturas/3.ScanEventosTabla.PNG)

**Captura 41.** Se ejecutó un **scan**.

##  Preparación del pipeline y codeploy (Hasta lo permitido)

En esta etapa se preparó la automatización del despliegue. El objetivo es que mas adelante, con solo un **git push** inicie el proceso sin subir manualmente el archivo JAR a EC2. GitHub guarda el código, CodeBuild genera el artefacto, S3 lo almacena y CodeDeploy lo instala en la instancia.

![Flujo de CodePipeline y CodeDeploy](Capturas/4.PipeLineGuia.PNG)

**Captura 42.** Flujo de Codepipeline y CodeDeploy proporcionado por el instructor donde se muestra el recorrido que se hacepara llegar al deploy.

### 1. Bucket para artefactos



![Bucket de artefactos creado](Capturas/4.Buckettaskflow-artefactos.PNG)

**Captura 43.** Se creó el bucket **taskflow-artefactos-saulhr04**. Donde se almacenaran temporalmente los archivos que pasaran de CodeBuild a CodeDeploy.

### 2. Agente de CodeDeploy en EC2

 Se instaló el agente dentro de EC2 y se verific que el servicio quedó activo y/o running para comprobar si el agente se encuentra iniciado.

![Servicio del agente CodeDeploy activo](Capturas/4.Agentecodepipeline.PNG)

**Captura 44.** Se comprobo que **codedeploy-agent** esta en estado **active (running)** dentro de EC2. 

### 3. Archivos de automatización en GitHub

Se agregaron al repositorio los archivos que definen cómo se construye e instala la aplicación. **buildspec.yml** indica a CodeBuild cómo generar el JAR; **appspec.yml** indica a CodeDeploy qué archivos copiar; **taskflow.service** registra la API como servicio y la carpeta **scripts** contiene los pasos para detener, preparar, iniciar y validar la aplicación.

![Archivos de pipeline en GitHub](Capturas/4.ArchivosEnGithub.PNG)

**Captura 45.** Se comprobo que los archivos **buildspec.yml**, **appspec.yml**, **taskflow.service** y la carpeta **scripts** fueron subidos al repositorio de GitHub correctamente.

### 4. Rol de servicio para CodeDeploy

Se creó un rol para que AWS CodeDeploy pueda trabajar con las instancias seleccionadas durante el despliegue. 

![Rol de CodeDeploy creado](Capturas/4.RolCodeDeploy.PNG)

**Captura 46.** Se creó el rol **taskflow-codedeploy-role**. (Hasta aqui nos dejo la capa gratis que nos ofrece AWS).
