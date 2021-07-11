## spring-boot-oracle-docker

Este proyecto documenta una arquitectura [Spring-boot] + [Oracle] + [Docker].    
Se basa en un contexto de microservicio con base de datos heredada, por lo que no puede ejecutar migraciones para sanitizar la base, en cambio, debe respetar los *constrains* existentes.    

El esquema en cuestion no posee *foreign keys*, y utiliza una secuencia oracle para indices autoincrementales.    

### Aplicacion

La aplicacion implementa diferentes controles que permiten 
al cliente de la api detectar inconvenientes.    
Los `DTOs` agregan validaciones que informan de manera inmediata 
el incumplimiento de una regla de negocio.   
Así mismo un `ErrorHandler` se encarga de manejar excepciones generadas por la aplicacion 
y por el motor de base de datos, de esta manera, 
si un servicio propaga una excepcion de tipo `ResourceNotFoundException` 
el cliente HTTP obtendra un response JSON con la información simplificada y 
el correspondiente *status-code* de error. 
Si en un contexto menos controlado el motor de base de datos encuentra un inconveniente, 
el *error hadler* extraera la informacion mas relevante para mostrarle al cliente, 
sin necesidad de exponer el *stack trace* de la aplicacion    

Tambien es posible exportar el esquema de los `DTOs` a interfaces `Typescript` para ser utilizadas 
en aplicaciones web. (ver apartado **api**)

**pendiente**
* integracion swagger

### **¡importante!**
Para utilizar la imagen de **Oracle Database Enterprise Edition** 
debe iniciar sesion en la web de docker

> https://hub.docker.com/_/oracle-database-enterprise-edition    

y aceptar los terminos de servicio de Oracle

![terms-of-service](wiki/terms-of-service.png)    

Debe, ademas, iniciar sesion con docker en el contexto local (terminal) para asociar la cuenta

```
docker login    
```

En las pruebas realizadas se ejecuto la imagen en un container aislado, 
lo que genera por transicion un pull de la imagen, 
si docker-compose presenta problemas al momento de construir los contenedores, 
se recomienda ejecutar el comando correspondiente

```
docker run -d -it --name oracle-database-isolate store/oracle/database-enterprise:12.2.0.1
```

### docker 

Ejecutar docker-compose para desarrollo

```
docker-compose -f docker-compose-dev.yml build    
```

```
docker-compose -f docker-compose-dev.yml up    
```

Debe esperar a que el container de oracle se encuentre en estado `healthy` para poder proseguir.
No confundir con el estado `health:starting`    
Es normal tambien que durante la etapa de configuracion el container muestre estado `unhealthy`

A continuacion se muestra un ejemplo de la terminal al finalizar el proceso de configuracion, 
el cual puede tardar varios minutos

![oracle-logs](wiki/oracle-logs.png)    

### Oracle-db  


Conectarse al container docker    

```
docker exec -it spring-boot-oracle-docker-oracle-db-dev /bin/bash    
```

Para evitar inconvenientes con el numero de columnas de la terminal puede usar el siguiente comando

```
docker exec -it spring-boot-oracle-docker-oracle-db-dev /bin/bash -c "export COLUMNS=`tput cols`; export LINES=`tput lines`; exec bash" 
```

Una vez dentro del contenedor debe configurar el contexto actual con el siguiente comando

```
source /home/oracle/.bashrc;    
```

cambiamos el espacio de trabajo a `/initdb.d` para acceder de manera sencilla a los archivos de 
creacion del esquema

```
cd  /initdb.d
```
   
La contraseña default de la base de datos es `Oradoc_db1` por lo que para conectarse en principio puede
utilizarse el siguiente comando

```
sqlplus sys/Oradoc_db1@ORCLCDB as sysdba  
```
   
una vez iniciada la sesion, es posible ejecutar el script de creacion de credenciales 
mediante el comando `START` de `SqlPlus`

```
SQL> START create_user 
```

Esto permitira iniciar sesion con las credenciales de la api y generar el esquema correspondiente 
y algunos datos de prueba

```
sqlplus api_admin/api_pass@ORCLCDB 
```

```
SQL> START create_schema 
```    

```
SQL> START seed 
```

`SqlPlus` asume que la extension del archivo es `.sql`

### api    

El servidor de datos es una aplicacion Java + Spring

El conector de Oracle no esta publicado en ningun registro maven, por lo que debe instalarse en el repositorio local manualmente

```
mvn install:install-file -Dfile=lib/ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=12.2.0.1 -Dpackaging=jar
```

Conectarse al container docker    
> docker exec -it spring-boot-oracle-docker-api-dev /bin/bash  

Instalar dependencias en modo desarrollo    

```
mvn install    
```

Ejecutar aplicaciones en modo desarrollo    

```
mvn spring-boot:run
```

#### Typescript

La aplicacion integra un plugin que permite exportar las clases `DTO` como interfaces typescript

```
mvn typescript-generator:generate
```

#### Swagger

La aplicacion expone una interfaz `swagger` para consultar los *endpoints* y su informacion en `/swagger-ui.html`

> http://localhost:8080/swagger-ui.html    

**pendiente**

* excluir servicios irrelevantes
* configurar servicio por entorno

### Eclipse

Es posible desplegar la aplicacion Java en modo desarrollo con un IDE convencional.    
En particular Eclipse puede ejecutar el metodo `main` de la clase `Application` como aplicacion java,
es necesario para el correcto funcionamiento configurar las variables de entorno de manera similar 
a como lo hace **Docker** 

![eclipse-environment](wiki/eclipse-environment.png)    

*2019-01-25*
