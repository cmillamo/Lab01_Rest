# Lab01_Rest
Este proyecto esta en hecho en java, esta sobre un servidor de aplicaciones Glassfish 4.1 y una base de datos MYSQL 5.0. 
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
    200 OK - Es mutante 
    200 OK - Es humano.
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
