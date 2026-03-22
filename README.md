# yttelegram

**yttelegram** — фреймворк на **Java** для разработки **Telegram-ботов**, в котором упор сделан на **наглядные редактируемые меню** (inline-клавиатуры), **предсказуемую смену состояний** и **типобезопасность** за счёт обобщённых (generic) паттернов «состояние + сообщение + правка».

Библиотека **`yttelegram-buttons`** опубликована в [**Maven Central**](https://central.sonatype.com/) — подключение одной зависимостью, без ручной сборки из исходников.

---

## Зачем это нужно

В типичном боте на Telegram Bot API легко получить «спагетти» из callback-строк, ручного парсинга и дублирования логики отправки/редактирования сообщений. **yttelegram** предлагает единый каркас:

- **Меню как перечисление** — кнопки описываются через `IMenuEnum` (текст, `callbackData`, строка сетки, при необходимости платёжная кнопка).
- **Состояния как enum** — каждый экран бота соответствует состоянию конечного автомата; переходы задаются через `execute` / `executeCallback`.
- **Редактирование вместо лишних сообщений** — при смене экрана, если тип сообщения совместим с предыдущим, вызывается **edit** (`EditMessageText`, `EditMessageMedia` и т.д.), иначе отправляется новое сообщение и при необходимости удаляется предыдущее, чтобы **не засорять чат дубликатами меню**.
- **Типобезопасные sender’ы** — отдельные абстракции для текста, фото, документов и счетов (invoice), с единым контрактом `IMessageSender`.
- **Очередь отправки с ограничением частоты** — `BaseSendMessageService` выстраивает сообщения в очередь и учитывает интервалы между отправками в чат (снижает риск `Too Many Requests` и перегрузки API).

В основе лежит официальный клиент **Telegram Bots** (`telegrambots-client`), совместимый с `TelegramClient` из **TelegramBots** 9.x.

---

## Возможности модуля `yttelegram-buttons`

| Область | Что даёт фреймворк |
|--------|---------------------|
| **Меню** | `AbstractMenuState` — сборка `InlineKeyboardMarkup` из опций меню, группировка по строкам (`getRowNum`), динамический текст и URL-кнопки, условная видимость кнопок через `Predicate` и `Function` для подписей. |
| **Состояния** | `DefStateInt` / `AbstractDefState` — для каждого состояния: `supportedState()`, `execute()`, `executeOnState()`, `buildMessage()`, опционально `buildEdit()` и `supportedEdit()`. |
| **Реестр** | `BaseMenuRegistry` — сопоставление `STATE` → обработчик; `AbstractStateRegistry` — хранение временных «билдеров» по пользователю (паттерн get-or-create). |
| **Типы экранов** | `AbstractTextMenuState` — текст + HTML + inline-меню; `AbstractImageMenuState` — фото с подписью; `AbstractDocMenuState` — документы; `AbstractInvoiceState` — платежи (invoice). |
| **Пользователь** | `AbstractUserData` — `chatId`, id последнего сообщения, тип поддерживаемого редактирования, флаг обновления — для корректного выбора edit vs send. |
| **Сервисы** | Очередь сообщений, удаление, ответ на callback (`replyCallback`), скачивание файлов (`TelegramFileDownloader`). |

Ошибки callback обрабатываются с дружелюбным сообщением пользователю и возможностью вернуться через команду вроде `/start` (текст по умолчанию в `AbstractMenuState` на русском).

---

## Требования

- **Java 17+** (в проекте включён `--enable-preview` для компилятора модуля `yttelegram-buttons`).
- Зависимости: **Log4j2**, **Lombok**, **TelegramBots Client** (версия задаётся в POM артефакта).

---

## Подключение (Maven Central)

Артефакт: **`xyz.youtradecs:yttelegram-buttons`**.

```xml
<dependency>
    <groupId>xyz.youtradecs</groupId>
    <artifactId>yttelegram-buttons</artifactId>
    <version>1.1.16</version>
</dependency>
```

Актуальную версию всегда можно проверить на [Maven Central](https://central.sonatype.com/search?q=g%3Axyz.youtradecs+yttelegram-buttons) или в файле [`yttelegram-buttons/pom.xml`](yttelegram-buttons/pom.xml).

**Лицензия:** Apache License 2.0.

**Исходный код и issues:** [github.com/a-matisse/yttelegram-main](https://github.com/a-matisse/yttelegram-main).

---

## Как начать (концептуально)

1. Описать **enum состояний** бота и **enum(ы) меню**, реализующие `IMenuEnum`.
2. Расширить **`AbstractUserData`** своими полями (роль пользователя, контекст диалога и т.д.).
3. Для каждого экрана создать класс, наследующий, например, **`AbstractTextMenuState`** (или image/doc/invoice), и реализовать `getHeaderText`, `getOptions`, `executeCallback`.
4. Собрать экземпляры в **`BaseMenuRegistry`** и в основном цикле бота вызывать `get(state)`, затем `execute` / `executeOnState` в зависимости от входящего `Update`.

Точные сигнатуры и контракты смотрите в Javadoc в опубликованном артефакте (к проекту подключаются **sources** и **javadoc** jar).

---

## Ключевые слова и темы (для поиска и каталогов)

Telegram bot Java, Telegram Bot API, inline keyboard, callback query, finite state machine, FSM, editable message, EditMessageText, EditMessageMedia, Maven Central, `xyz.youtradecs`, telegrambots, Telegram Java library, типобезопасное меню, конечный автомат бота, inline-меню Telegram, rate limit очередь сообщений, SendInvoice, платежи Telegram.

---

## Автор

Проект развивается в рамках экосистемы **youtradecs**. Контакты разработчика указаны в [`pom.xml`](pom.xml) (секция `developers`).
