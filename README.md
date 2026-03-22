# yttelegram

![Version](https://img.shields.io/badge/version-1.1.16-blue?style=for-the-badge)
![Java](https://img.shields.io/badge/java-17-orange?style=for-the-badge&logo=openjdk)
![Maven Central](https://img.shields.io/maven-central/v/xyz.youtradecs/yttelegram-buttons?label=Maven%20Central&style=for-the-badge)
![License](https://img.shields.io/badge/license-Apache%202.0-green?style=for-the-badge)
![Telegram](https://img.shields.io/badge/Telegram-Bot%20API-26A5E4?style=for-the-badge&logo=telegram&logoColor=white)

**yttelegram** is a **Java** framework for **Telegram bots** focused on **editable inline menus**, **predictable state transitions**, and **type safety** via generic patterns that tie together state, outgoing messages, and edits.

The **`yttelegram-buttons`** library is published on [**Maven Central**](https://central.sonatype.com/) — add a single dependency; no need to build from source.

---

## Why use it

Telegram Bot API code often turns into callback-string spaghetti, manual parsing, and duplicated send/edit logic. **yttelegram** provides a single structure:

- **Menus as enums** — buttons are described with `IMenuEnum` (label, `callbackData`, row index, optional pay flag).
- **States as enums** — each bot screen maps to an FSM state; transitions go through `execute` / `executeCallback`.
- **Edit instead of noise** — when changing screens, if the message type is compatible with the previous one, an **edit** is used (`EditMessageText`, `EditMessageMedia`, …); otherwise a new message is sent and the old one may be removed to **avoid duplicate menus** in the chat.
- **Typed senders** — separate abstractions for text, photo, document, and invoice flows behind `IMessageSender`.
- **Queued sending with pacing** — `BaseSendMessageService` queues outbound traffic and spaces sends per chat (helps with rate limits and API load).

Built on **TelegramBots** `telegrambots-client` (**TelegramClient**, 9.x line).

### Module capabilities (`yttelegram-buttons`)

| Area | What you get |
|------|----------------|
| **Menus** | `AbstractMenuState` builds `InlineKeyboardMarkup` from menu options, groups rows via `getRowNum`, supports dynamic labels, URL buttons, visibility via `Predicate`, and captions via `Function`. |
| **States** | `DefStateInt` / `AbstractDefState` — per state: `supportedState()`, `execute()`, `executeOnState()`, `buildMessage()`, optional `buildEdit()` / `supportedEdit()`. |
| **Registries** | `BaseMenuRegistry` maps `STATE` → handler; `AbstractStateRegistry` stores per-user builders (get-or-create). |
| **Screen types** | `AbstractTextMenuState`, `AbstractImageMenuState`, `AbstractDocMenuState`, `AbstractInvoiceState`. |
| **User session** | `AbstractUserData` — `chatId`, last message id, supported edit type, update flag for correct edit vs send. |
| **Services** | Message queue, delete, callback answer (`replyCallback`), file download (`TelegramFileDownloader`). |

Callback failures surface a user-friendly message and a path back via a command such as `/start` (default copy in `AbstractMenuState` is Russian).

### Requirements

- **Java 17+** (the `yttelegram-buttons` module is compiled with `--enable-preview`).
- Runtime deps include **Log4j2**, **Lombok**, **TelegramBots Client** (see the artifact POM for exact versions).

### Installation (Maven Central)

Artifact: **`xyz.youtradecs:yttelegram-buttons`**

```xml
<dependency>
    <groupId>xyz.youtradecs</groupId>
    <artifactId>yttelegram-buttons</artifactId>
    <version>1.1.16</version>
</dependency>
```

Check the latest version on [Maven Central](https://central.sonatype.com/search?q=g%3Axyz.youtradecs+yttelegram-buttons) or in [`yttelegram-buttons/pom.xml`](yttelegram-buttons/pom.xml).

**Repository:** [github.com/a-matisse/yttelegram-main](https://github.com/a-matisse/yttelegram-main)

### Getting started (outline)

1. Define your **state enum(s)** and **menu enum(s)** implementing `IMenuEnum`.
2. Extend **`AbstractUserData`** with your session fields.
3. For each screen, subclass e.g. **`AbstractTextMenuState`** (or image/doc/invoice) and implement `getHeaderText`, `getOptions`, `executeCallback`.
4. Register instances in **`BaseMenuRegistry`** and wire your bot loop to `get(state)` then `execute` / `executeOnState` from each `Update`.

See published **sources** and **javadoc** jars on Central for exact APIs.

---

## Русский

**yttelegram** — фреймворк на **Java** для **Telegram-ботов** с упором на **наглядные редактируемые меню** (inline-клавиатуры), **предсказуемую смену состояний** и **типобезопасность** за счёт обобщённых паттернов «состояние + сообщение + правка».

Библиотека **`yttelegram-buttons`** доступна в [**Maven Central**](https://central.sonatype.com/) — одна зависимость в `pom.xml`, без сборки из исходников.

### Зачем это нужно

В типичном боте легко получить «спагетти» из callback-строк и дублирование отправки/редактирования. **yttelegram** даёт единый каркас:

- **Меню как перечисление** — `IMenuEnum` (текст, `callbackData`, строка сетки, при необходимости платёжная кнопка).
- **Состояния как enum** — экраны как состояния КА; переходы через `execute` / `executeCallback`.
- **Редактирование** — при совместимости типа сообщения вызывается **edit**, иначе новое сообщение и при необходимости удаление старого, чтобы **не засорять чат**.
- **Типобезопасные sender’ы** — текст, фото, документ, invoice через `IMessageSender`.
- **Очередь с ограничением частоты** — `BaseSendMessageService` снижает риск лимитов API.

Клиент: **TelegramBots** `telegrambots-client` 9.x.

### Возможности модуля `yttelegram-buttons`

| Область | Описание |
|--------|-----------|
| **Меню** | `AbstractMenuState` — `InlineKeyboardMarkup`, строки по `getRowNum`, URL, видимость, динамические подписи. |
| **Состояния** | `DefStateInt` / `AbstractDefState` — полный жизненный цикл состояния и сообщения. |
| **Реестр** | `BaseMenuRegistry`, `AbstractStateRegistry`. |
| **Экраны** | Текст, фото, документ, invoice — см. абстрактные классы в пакетах `menu.*`. |
| **Пользователь** | `AbstractUserData` — данные для edit vs send. |

### Требования и подключение

- **Java 17+**, зависимости см. POM артефакта.

```xml
<dependency>
    <groupId>xyz.youtradecs</groupId>
    <artifactId>yttelegram-buttons</artifactId>
    <version>1.1.16</version>
</dependency>
```

Актуальная версия: [Maven Central](https://central.sonatype.com/search?q=g%3Axyz.youtradecs+yttelegram-buttons) или [`yttelegram-buttons/pom.xml`](yttelegram-buttons/pom.xml).

### Быстрый старт (шаги)

1. Enum состояний и меню (`IMenuEnum`).
2. Наследник `AbstractUserData`.
3. Классы экранов на базе `AbstractTextMenuState` / image / doc / invoice.
4. `BaseMenuRegistry` и цикл обработки `Update`.

---

## Maintainer

Contacts from [`pom.xml`](pom.xml) (`developers`):

| | |
|--|--|
| **id** | `a_matisse` |
| **name** | Aleksandr |
| **email** | [provotorov-02@mail.ru](mailto:provotorov-02@mail.ru) |

---

## License

See [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Keywords

`java` `telegram` `telegram-bot` `telegram-bots` `telegram-bot-api` `telegram-java` `telegrambots` `telegrambots-client` `inline-keyboard` `inline-menu` `callback-query` `finite-state-machine` `fsm` `state-machine` `type-safe` `generics` `editable-message` `edit-message-text` `edit-message-media` `send-invoice` `maven` `maven-central` `xyz-youtradecs` `yttelegram` `yttelegram-buttons` `java-17` `lombok` `log4j` `apache-license` `rest-api` `bot-framework`
