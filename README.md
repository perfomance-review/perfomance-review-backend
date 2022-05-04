# performance-review-backend

##### Настройка и запуск
###### БД
* запустить postgresql server локально (через докер доработаем позже)
* накатить 'performance-review-backend/init.sql'

###### idea
* скачать плагин Lombok
* в настройках поставить галочку: 
File | Settings | Build, Execution, Deployment | Compiler | Annotation Processors  ->  [x] Enable annotation processing (performance-review-backend) 

#####запуск:
для запуска profile dev
 `mvn clean install exec:java -P DEV`
для запуска profile prod
 `mvn clean install exec:java -P PROD`
 
 если необходимо указать свои настройки к БД то необходимо добавить параметр:
 * для логина: `-DdbUser=login_name`, где `login_name` - логин от вашей БД
 * для пароля: `-DdbPass=passsword`, где `passsword` - пароль от вашей БД
 * для хоста: `-DdbHost=host_name`, где `host_name` - хост от вашей БД
 > если параметр не установлен, используется из проперти-файла `performance-review-backend/config/dev/service.properties`
 > example `mvn clean install exec:java -P DEV -DdbUser=postgres -DdbPass=postgres -DdbHost=localhost` 
 > example `mvn clean install exec:java -P PROD -DdbUser=postgres -DdbPass=postgres -DdbHost=localhost` 
 > example `mvn clean install exec:java -P DEV -DdbUser=postgres -DdbHost=localhost` 

##### Добавление новых миграций в liquibase
1). сделать копию существующей миграции  `20220425000000_create_schema.xml` , путь `performance-review-backend/src/main/liquibase/release_1.0.0/`
 >1. переименовать по образцу, где префикс '2022042500000' (год месяц день время ), а дальше короткое название миграции
 >2. в атрибуте `logicalFilePath` тега `<databaseChangeLog>` должно быть указано имя миграции
 >3. в теге `<changeSet>`:
   >>* в `id` - префикс из названия миграции
   >>* в `author` - указать создателя миграции
   >>* без изменений `context="schema" runOnChange="true"`
   >>* в теге `<sql>` скрипт
   >>* в теге `<rollback>/<sql>` скрипт отката миграции (опционально)
   >>* в теге `<comment>` комментарий

2). Созданную миграцию помещаем в 'changelog.xml':
   >* важно соблюдать порядок добавления миграций, все новые миграции добавляем после существующих
   >* добавляем `<include file="имя_созданной_в_п.1_миграции.xml" relativeToChangelogFile="true"/>`
