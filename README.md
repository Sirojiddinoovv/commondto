# 🧱 Common DTO Library

Универсальная библиотека DTO, перечислений, исключений и утилит на Kotlin для повторного использования в разных микросервисах.

## 📦 Структура

- `model/dto/core` — базовые DTO (например, `ResultResponse`, `DateRangeResult`)
- `model/enums` — общие enum'ы (`ProcessStatus`, `ResultState`)
- `exception` — кастомные исключения (`InvalidDataException`, `NotImplementedException`)
- `utils` — полезные классы (`DateUtils`)
- `test` — модульные тесты

## 🛠️ Использование

### Пример: работа с результатом

```kotlin
val result = ResultResponse(
    state = ResultState.SUCCESS,
    message = "Операция выполнена успешно"
)
```

### Пример: использование диапазона дат

```kotlin
val range = DateRange(
    from = LocalDate.parse("2024-01-01"),
    to = LocalDate.parse("2024-12-31")
)
```

### Пример: enum-статусы процесса

```kotlin
when (val status = ProcessStatus.PROCESSING) {
    ProcessStatus.PROCESSING -> println("Идёт обработка")
    ProcessStatus.COMPLETED -> println("Завершено")
    else -> println("Неизвестный статус")
}
```

## 🔍 Исключения

```kotlin
throw InvalidDataException("Некорректный формат данных")
```

## 🧪 Тестирование

```bash
./gradlew test
```

## 🔗 Подключение

Добавьте модуль как зависимость в `build.gradle.kts`:

```kotlin
implementation(project(":common-dto"))
```

---

> Создано для стандартизации и ускорения разработки микросервисов.
