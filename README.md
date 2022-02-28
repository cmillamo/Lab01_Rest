# Lab01_Rest

Componentes que se utilizaron para la solución:
  Glassfish 4.1
  Mysql 5.0 
  Java 1.8 
  JPA
  
Nivel 1: Para el punto la manera de solucionar el problema fue dividir la matriz en 4 partes. 
         Creé cuatro ciclos que me validaban las cadenas de dna en forma verticales, horizontales y dos grupos de cadenas con las posibles diagonales. Teniendo siempre presente          que la validación termina en el momento que cumpla la condición de ser mutante. Cuando va hacer costoso realizar esta validación cuando el usuario no es mutante porque          se validan todas las posibles combinaciones. 
         Seguido a esto aplique una expresión regular que me evaluara el patrón definido dada una cadena.
         Para los casos de prerrequisitos se utilizó la función de patrón con expresión regular para validar que solo permitiera las letras definidas en el requerimiento.
              
 Nivel 2: Teniendo como base el desarrollo en el nivel 1 simplemente se creó el servicio y se configuraron los mensajes. En este punto se realizó la inserción de las cadenas dna que se evalúan.    
 
 Nivel 3:  Se creó un servicio REST tipo GET en el cual se ejecuta una consulta de base que calcula las estadísticas de las verificaciones de DNA. 


Expone dos servicios tipo REST: 
 1. Se crear el servicio “/mutant/” en donde se pueda detectar si un humano es
    mutante enviando la secuencia de ADN mediante un HTTP POST con un Json el cual tenga el
    siguiente formato:
    Para consumir el servicio se debe hacer atraves de postman:
    URL: http://ec2-54-159-148-164.compute-1.amazonaws.com:8080/Lab_Rest/api/XMEN/mutant
    body:
    {
     “dna”:["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
    }
    Mensajes del servicio:
    200 OK - Mutante 
    200 OK - No Mutante.
    Mensaje de Error:
    403- Forbidden - com.proyecto.modelo.exceptions.PrerequisitosException: No cumple con el tamaño requerido - 6 Cadena ATGCGAD Tamaño 7
    403- Forbidden - com.proyecto.modelo.exceptions.PrerequisitosException: No cumple con la base nitrogenda de ADN
    403- Forbidden - No se envio cadena de DNA
  2. Se crea el servicio /stats/, el cual  que devuelva un Json con las estadísticas de las verificaciones de ADN
     URL : http://ec2-54-159-148-164.compute-1.amazonaws.com:8080/Lab_Rest/api/XMEN/stats
     TIPO: GET
     RESPUESTA: {
                   "count_human_dna": 2,
                   "count_mutant_dna": 1,
                   "ratio": "0.5"
            }
