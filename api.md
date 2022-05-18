## Api приложения

### Получение опросов

`GET /polls`

`Cookie: user-id=USER_ID`

Возвращает для пользователя с указанным id (USER_ID) массив опросов с полями:

* **pollId** - id опроса
* **title** - название опроса
* **deadline** - дедлайн
* **questionsCount** - количество вопросов
* **respondentsCount** - количество респондентов
* **status** - статус
