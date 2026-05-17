# kur-java

Миниобучения: **БД (SQLite) + интерфейс (Thymeleaf/Bootstrap) + CRUD** на Java с акцентом на **DDD + Clean Architecture + Unit of Work**.

## Запуск

Требования: Java 17+, Maven 3.9+

```bash
./mvnw spring-boot:run
```

Открыть:
- `http://localhost:8080/` — UI

База данных: SQLite файл `./data/kur.db` (создаётся автоматически).

## Админ и доступ студентов

Перед первым входом нужно создать **админа** через CLI:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="create-admin --email=admin@example.com --password=Admin12345"
```

Если `--password` не указать — пароль будет сгенерирован и выведен в консоль.

Далее:
- Войти: `http://localhost:8080/login`
- Админ видит полный интерфейс и может выдавать/забирать доступ студентам на странице `/students` (кнопки **Grant/Revoke**).
- Студенты без доступа не могут зайти и, соответственно, не могут получить доступ к курсам.

## Функции (CRUD)
- Курсы: `/courses`
- Студенты: `/students`
- Записи на курс (enrollments): `/enrollments`

## Архитектура
- `com.example.kur.domain` — доменная модель и порты репозиториев
- `com.example.kur.application` — use-case сервисы и порт `UnitOfWork`
- `com.example.kur.infrastructure` — JPA-адаптеры + реализация `UnitOfWork`
- `com.example.kur.interfaces.web` — MVC контроллеры + HTML (Thymeleaf)
