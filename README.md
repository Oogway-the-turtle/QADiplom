# Процедура запуска автотестов
> Открыть Git bash here
>  
> Склонировать репозиторий:
> git clone https://github.com/Oogway-the-turtle/QADiplom
> 
> Запустить Docker Desktop
> 
> Открыть проект в IntelliJ IDEA
> 
> Запустить контейнеры, выполнив команду в терминале
> `docker-compose up`
> 
> В новой вкладке терминала для запуска базы СУБД выполнить команду:
> 
> 1. Для СУБД MySQL: `java -jar artifacts/aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app`
> 
> 2. Для СУБД PostgreSQL: PostgreSQL: `java -jar artifacts/aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app`
> 
> Запустить тесты командой:
> 
> 1. Для СУБД MySQL: 
> `./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`
> 
> 2. Для СУБД PostgreSQL:
> `./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`
> 
> Запустить сервис формирования отчетов Allure командой: 
> `./gradlew allureServe`
> 
> Для закрытия отчёта ввести комбинацию клавиш:
> **CTRL + C**
> 
> Остановить контейнеры, выполнив команду в терминале:
>  `docker-compose down`

 
