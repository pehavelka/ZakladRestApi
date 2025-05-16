# ZakladRestApi
Projekt **ZakladRestApi** slouží k demonstraci znalostí technologie Spring Boot formou jednoduchého REST API.

## Použité technologie
- Java 17  
- Maven  
- Spring Boot  
- Spring Security (JWT)  
- JUnit
- JPA / Hibernate  
- QueryDSL  
- Lombok  
- Liquibase  
- Docker  
- PostgreSQL  
- Open API, Swagger

## Popis aplikace

Aplikace implementuje autentizaci pomocí JWT (JSON Web Token).  
Součástí aplikace je také test prováděný pomocí JUnit.  

Poskytuje REST API metody pro:

### Autentizaci:
- registrace nového uživatele
- přihlášení do aplikace
- získání informací o přihlášeném uživateli  


### Správu uživatelů:
- seznam uživatelů
- detail uživatele
- vytvoření uživatele
- úprava existujícího uživatele

## Použité nástroje
[Bruno](https://www.usebruno.com/downloads) - testování REST API  
[Spring Tools for Eclipse](https://spring.io/tools) - vývojové prostředí   

## Logování aplikace
Aplikace využívá knihovnu Logback pro logování. Výstup logování je směrován jak do konzole, tak do souboru /ZakladRestApi/logs/app.log.

Logování do souboru je nastaveno s denní rotací – každý den se vytváří nový logovací soubor, původní se zazipuje.

Konfigurace logování je definována v souboru logback.xml, který je uložen v cestě:  
[/ZakladRestApi/src/main/resources/logback.xml](https://github.com/pehavelka/ZakladRestApi/blob/main/src/main/resources/logback.xml)


## Spuštění aplikace

1.**Klonování repozitáře a sestavení aplikace:**

```
git clone https://github.com/pehavelka/ZakladRestApi.git
cd ZakladRestApi
mvn clean compile
mvn clean eclipse:eclipse
```
2.**Vytvoření PostgreSQL databáze v Dockeru:**  

```
docker run --name pg -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=pass -p 5432:5432 -d postgres
```

3.**Spuštění testů:**  

```
mvn test  
```


4.**Spuštění aplikace:**

```
mvn spring-boot:run
```

Po suštění aplikace se automaticky vytvoří databázové tabulky a založí se výchozí uživatelé s heslem password123:  

- user@example.com
- admin@example.com
- super.admin@email.com 

## Datový model

Tabulky:

- roles - seznam rolí
- users - seznam uživatelů
- roleuser - vazba mezi uživatelem a rolí


```
+---------+           +------------+           +--------+  
|  roles  |           | roleuser   |           | users  |  
|---------|           |------------|           |--------|  
| id      |<--------+ | id         | +-------> | id     |  
| rolename|           | role_id    |           | email  |  
| ...     |           | user_id    |           | ...    |  
+---------+           | ...        |           +--------+  
```

Legenda:  
- roleuser.role_id  → roles.id  
- roleuser.user_id  → users.id  



# Ověření REST API pomocí aplikace Bruno  
1. Otevřít aplikaci **Bruno**  
2. Načíst kolekci ze složky:  
 [/ZakladRestApi/docs/bruno/ZakladRestApi](https://github.com/pehavelka/ZakladRestApi/tree/main/docs/bruno/ZakladRestApi)
3. Před voláním metod nastavit environment **ZakladRestApi**(obsahuje proměnnou {{jwt}})  
4. Zavolat metodu **login**  
  Proměnná {{jwt}} se automaticky nastaví po přihlášení a slouží pro ověření oprávnění v dalších metodách.  

Následně je možné ověřit volání dalších metod.


Validace vstupních dat metod(obsahují kontroly povinných údajů):  
- login  
- signup  
- novy (vytvoření uživatele)  
- zmena (změna uživatele)  



Pokud některý z povinných údajů chybí, vrací se odpověď:
- HTTP 400 – Bad Request
- tělo response obsahuje odpovídající chybové hlášení ve formátu JSON  

V opačném případě je daná akce provedena a vrací se odpověď HTTP 200.  
Pokud to dává smysl, je v těle odpovědi obsažen JSON s relevantními daty pro danou akci.

# Ověření REST API přes Swagger  
Na níže uvedené URL adrese je možné ověřit volání služeb přes 
[Swagger rozhraní](http://localhost:8005/swagger-ui/index.html).  
  
Postup ověření:  
1.  
V sekci AuthenticationController zavolat metodu [/auth/login](http://localhost:8005/swagger-ui/index.html#/AuthenticationController/authenticate).  
Metoda obsahuje uživatele a heslo uživatele vytvořeného při automatické inicializaci databáze.  
Metoda vrátí JWT token.  
2.  
Přes tlačítko *Authorize* zadat JWT token z bodu 1.  
3.  
Nyní je možné volat všechny zabezpečené metody sekce *UserController*.  


