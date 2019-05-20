# Contact Filter

| Technology     | Badge |
|:--------------:|:-----:|
| License        | [![LICENSE](https://img.shields.io/badge/LICENSE-Apache%202.0-blue.svg)](LICENSE) |
| Travis CI      | [![Build Status](https://travis-ci.com/ArtemMurk/ContactFilter.svg?branch=master)](https://travis-ci.com/ArtemMurk/ContactFilter) |
| Codacy         | [![Codacy Badge](https://api.codacy.com/project/badge/Grade/087dde42767b4f30a572d705e1306860)](https://www.codacy.com/app/ArtemMurk/ContactFilter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ArtemMurk/ContactFilter&amp;utm_campaign=Badge_Grade) |


## Project Information
This is filter for contacts. 
It takes regexp from REST and process it in java layer. 
Architecture of application is based on REST principle. 
Construct with Spring and Postgres as main DB. 

## Build

Use Maven for build this project. 
This project package in **war** file, you need some Servlet Container to deploy it.
 
Build command.
> mvn clean package

## Configuration

#### Forbidden characters

Before you start the program you must to encode your regex pattern **OR** allow  forbidden characters in your  servlet container.

Tomcat servlet example configuration. Put this line in your 
** catalina.properties**

> tomcat.util.http.parser.HttpParser.requestTargetAllow=|{}

#### DB

Configure your db connection in [jdbc.properites](https://github.com/ArtemMurk/ContactFilter/blob/master/src/main/resources/db/jdbc.properties)

Init database table by script [init_db.sql](https://github.com/ArtemMurk/ContactFilter/blob/master/src/main/resources/db/init_db.sql)

#### MapReduce

Configure main mapReduce properties in [main.properties](https://github.com/ArtemMurk/ContactFilter/blob/master/src/main/resources/mapred/main.properties)

Example:

Total num threads is active in module
> slots=15

Max Mapper count for one proceess
> maxMappers=5

Min Mapper count for one proceess
> minMappers=3

Batch rows in table for one mapper
> batch=50000

## API

Get contacts by regexp

> http://localhost:8080/hello/contacts?nameFilter={Your regexp Here}

## My Contact's 
If you have any questions I will be happy to answer it.
 
* **Author**: Артём Муравлёв
* **Linkeidn**: [My Linkeidn](https://linkedin.com/in/artem-muravlov)
* **Telegram**: @artem_murk
* **Mail**: muravlevars@gmail.com