Examen 

Introducción:
El siguiente ejercicio consiste en un API REST que nos permitirá consultar adns y verificar si se trata de un adn mutante o humano.
También podremos consultar el ratio de mutantes / humanos registrados luego de ser analizados en cada una de las requests al api.

## Funcionalidades:

### Verificación de ADN:

Se debe realizar un post a la siguiente Url http://localhosto:8080/camel/api/mutant

POST → /mutant/
{
“dna”:["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}

En caso de verificarse el ADN mutante, se obtendrá un 200-OK. Caso contrario, si es humano obtendremos un 403-FORBIDDEN.

### Stats:

Se debe realizar un get a la siguiente http://localhost:8080/camel/api/stats

En la misma se devolvera un json.

## Tecnologias
Java 1.8
Camel
Spring boot
mongoDb

## Especificaciones
Se deben levantar ambos microservicios con una base mongo en el mismo ambiente puertos y credenciales se usan por defecto.
MongoRest levantara en el puerto 4444, mientras que spring-boot-camel en el puerto 8080

## Comandos para levantar microservicios
spring-boot-camel/mvn spring-boot:run
MongoRest/mvn spring-boot:run