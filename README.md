                                                     ClientConnect

   ClientConnect — это RESTful веб-приложение для управления клиентами и их контактными данными. 
   Позволяет выполнять CRUD-операции с клиентами и их контактами.

           Технологии

   - Java 17

   - Spring Boot

   - PostgreSQL

   - Hibernate

   - Liquibase

   - Swagger

   - JUnit 5, Mockito

   - Docker

         Требования

   - Java 17+

   - Docker 

   - PostgreSQL (если не используете Docker)


          Установка и запуск

1. Клонирование репозитория

2. Конфигурация окружения

3. Запуск через Docker

docker-compose up --build

4. Запуск локально (без Docker)

Убедитесь, что PostgreSQL запущен и сконфигурирован

         API Документация (Swagger)

После запуска приложения документация API доступна по адресу:

http://localhost:8080/swagger-ui.html

        Использование API

1. Работа с клиентами (/clients)

Получение всех клиентов:

GET /clients

Получение клиента по ID:

GET /clients/{id}

Создание клиента:

POST /clients
Content-Type: application/json

{
  "name": "John",
  
  "lastName": "Doe"
}

Обновление клиента:

PUT /clients/{id}
Content-Type: application/json

{
  "name": "John",
  
  "lastName": "Smith"
}

Удаление клиента:

DELETE /clients/{id}

2. Работа с контактами (/contacts)

Получение всех контактов:

GET /contacts

Получение контакта по ID:

GET /contacts/{id}

Создание контакта:

POST /contacts
Content-Type: application/json

{
  "phone": "+123456789",
  
  "email": "test@example.com"
}

Обновление контакта:

PUT /contacts/{id}
Content-Type: application/json

{
  "phone": "+987654321",
  
  "email": "updated@example.com"
}

Удаление контакта:

DELETE /contacts/{id}

      Тестирование
    
Запуск тестов:
mvn test
