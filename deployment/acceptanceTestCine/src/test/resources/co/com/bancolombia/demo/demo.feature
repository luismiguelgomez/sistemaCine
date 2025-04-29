Feature: Prueba de aceptación para comprar boletas

Background:
  * url 'http://localhost:8080'
  * header Content-Type = 'application/json'

Scenario: Probar la API de compra de boletos con parámetros inválidos
  Given path '/api/comprarBoletas'
  And param codigoFuncion = 1
  And param boletosAcomprar = 0
  When method post
  Then status 201


Scenario: Probar la API de compra de boletos exitosamente
  Given path '/api/comprarBoletas'
  And param codigoFuncion = 102
  And param boletosAcomprar = 1
  When method post
  Then status 201