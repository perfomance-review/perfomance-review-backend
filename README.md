# performance-review-backend

##### Настройка и запуск
###### БД
* запустить postgresql server локально (через докер доработаем позже)
* накатить 'performance-review-backend/init.sql'

###### idea
* скачать плагин Lombok
* в настройках поставить галочку: 
File | Settings | Build, Execution, Deployment | Compiler | Annotation Processors  ->  [x] Enable annotation processing (performance-review-backend) 

запуск: `mvn clean install exec:java`

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
