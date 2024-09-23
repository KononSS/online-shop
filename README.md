## Online Shop

Этот проект представляет собой веб-приложение для онлайн-магазина, разработанное с использованием Spring Boot. Приложение позволяет пользователям просматривать товары, добавлять их в корзину и оформлять заказы. Позволяет выбрать администратора, который сможет управлять ассортиментом продуктов.

## Технологии

Проект использует следующие технологии:

- **Spring Boot**: Фреймворк для создания веб-приложений на Java.
- **Spring Data JPA**: Для работы с базой данных.
- **Spring Security**: Для аутентификации и авторизации.
- **Thymeleaf**: Шаблонизатор для генерации HTML.
- **Bootstrap**: Для стилизации веб-интерфейса.
- **PostgreSQL**: База данных для хранения информации о товарах, пользователях и заказах.
- **ModelMapper**: Для маппинга объектов.
- **Logback**: Для логирования.
- **Liquibase**: Для миграций.
- **JUnit**: Для тестов.

## Установка

Для установки и запуска проекта выполните следующие шаги:

1. **Клонируйте репозиторий**:

2. **Настройте базу данных**:

Создайте базу данных PostgreSQL с именем online-shop1_db.

Настройте параметры подключения к базе данных в файле application.properties:

spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/online-shop1_db
spring.datasource.username=postgres
spring.datasource.password=postgre

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.show_sql=true
spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.xml

Запуск миграций с использованием Liquibase
Убедитесь, что у вас установлен Maven.
Выполните следующую команду в корне проекта:
mvn liquibase:update
Убедитесь, что база данных настроена и миграции выполнены успешно.
Также необходимый sql находится в :src/main/java/ru/obozhulkin/Onlineshop/sql/db.sql

3. **Запустите приложение:**

Приложение будет доступно по адресу http://localhost:8080.

## Использование

После запуска приложения вы можете использовать его следующим образом:

**Регистрация и вход:**

Перейдите на страницу регистрации (/auth/registration) и зарегистрируйтесь.

Войдите в систему на странице входа (/auth/login).

**Администрирование:**

Вы можете перейти в базу данных и изменить свою роль(по умолчанию "ROLE_USER") на ROLE_ADMIN. Выйдите из профиля в приложении в браузере и войдите снова, и в навигационно панели вы увидите кнопку "Админ"("/admin/adminPage"). После нажатия на нее вы перейдете в сервисы администрирования.
Создайте товар, заполнив всю форму. После добавления товар появится в каталоге("/user/catalog") и будет доступен всем пользователям для добавления в корзину.

**Просмотр товаров:**

После входа вы можете просматривать доступные товары на главной странице (/).

**Добавление товаров в корзину:**

Нажмите кнопку "Добавить в корзину" на странице товара, чтобы добавить его в корзину.



## API
Приложение предоставляет следующие API-эндпоинты:

GET /api/hello: Приветственное сообщение.

GET /api/allProducts: Получить список всех товаров.

GET /api/search: Поиск товаров по названию.

GET /api/{id}: Получить информацию о конкретном товаре.

POST /api/addProduct: Добавить новый товар. (Для добавления картинки используйте ключ "img" тип файл.)
Пример:![image](https://github.com/user-attachments/assets/b56ccb20-5c37-4361-80e2-7664016b6f3c)


DELETE /api/delete/{id}: Удалить товар. 

## Тестирование
Тесты будут позже._db
spring.datasource.username=**ВАШИ ДАННЫЕ**
spring.datasource.password=**ВАШИ ДАННЫЕ**

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.show_sql=true

Выполните код из файла: src/main/java/ru/obozhulkin/Onlineshop/sql/db.sql

3. **Запустите приложение:**

Приложение будет доступно по адресу http://localhost:8080.

## Использование

После запуска приложения вы можете использовать его следующим образом:

**Регистрация и вход:**

Перейдите на страницу регистрации (/auth/registration) и зарегистрируйтесь.

Войдите в систему на странице входа (/auth/login).

**Администрирование:**

Вы можете перейти в базу данных и изменить свою роль(по умолчанию "ROLE_USER") на ROLE_ADMIN. Обновите страницу приложения в браузере и в навигационно панели вы увидите кнопку "Админ"("/admin/adminPage"). После нажатия на нее вы перейдете в сервисы администрирования. 
Создайте товар, заполнив всю форму. После добавления товар появится в каталоге("/user/catalog") и будет доступен всем пользователям для добавления в корзину.

**Просмотр товаров:**

После входа вы можете просматривать доступные товары на главной странице (/).

**Добавление товаров в корзину:**

Нажмите кнопку "Добавить в корзину" на странице товара, чтобы добавить его в корзину.



## API
Приложение предоставляет следующие API-эндпоинты:

GET /api/hello: Приветственное сообщение.

GET /api/allProducts: Получить список всех товаров.

GET /api/search: Поиск товаров по названию.

GET /api/{id}: Получить информацию о конкретном товаре.

POST /api/addProduct: Добавить новый товар.

DELETE /api/delete/{id}: Удалить товар.

## Тестирование
Тесты будут позже.

## Документация
HTML-документацию вы можете найти по пути: Online-shop/docs/
