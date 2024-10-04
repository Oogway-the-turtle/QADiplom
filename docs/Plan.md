# План автоматизации

**Цель плана автоматизации** - описание процесса автоматизации тестирования веб сервиса при переходе к форме покупки тура по
определённой цене двумя способами:

1. Обычная оплата по дебетовой карте.
2. Уникальная технология: выдача кредита по данным банковской карты.


### Общие предусловия

- Открыт сайт [веб-сервиса](http://localhost:8080/).
- Нажата кнопка "Купить"  или "Купить в кредит" 
- Номер одобренной карты 4444 4444 4444 4441
- Номер отклонённой карты 4444 4444 4444 4442


## Перечень автоматизируемых сценариев

1. Успешная оплата тура по одобренной карте
- В поле "Номер карты" ввести ввалидный номер одобренной карты
- В поле "Месяц" ввести ввалидное значение
- В поле "Год" ввести ввалидное значение
- В поле "Владелец" ввести ввалидное значение
- В поле "CVC/CVV" ввести валидное значение
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* Сообщение "Успешно! Операция одобрена банком"

2. Отказ оплаты тура по отклонённой карте
- В поле "Номер карты" ввести ввалидный номер отклонённой карты
- В поле "Месяц" ввести ввалидное значение
- В поле "Год" ввести ввалидное значение
- В поле "Владелец" ввести ввалидное значение
- В поле "CVC/CVV" ввести валидное значение
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* Сообщение "Ошибка! Банк отказал в проведении операции."

3. Оставить поле "CVC/CVV" с пустым значением
- В поле "Номер карты" ввести валидные значение
- В поле "Месяц" ввести ввалидное значение
- В поле "Год" ввести ввалидное значение
- В поле "Владелец" ввести ввалидное значение
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "CVC/CVV" сообщение "Поле обязательно для заполнения"

4. Оставить поле "Владелец" с пустым значением
- В поле "Номер карты" ввести валидные значение
- В поле "Месяц" ввести ввалидное значение
- В поле "Год" ввести ввалидное значение
- В поле "CVC/CVV" ввести валидное значение
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "Владелец" сообщение "Поле обязательно для заполнения"

5. Оставить поле "Год" с пустым значением
- В поле "Номер карты" ввести валидные значение
- В поле "Месяц" ввести ввалидное значение
- В поле "Владелец" ввести ввалидное значение
- В поле "CVC/CVV" ввести валидное значение
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "Год" сообщение "Поле обязательно для заполнения"

6. Оставить поле "Месяц" с пустым значением
- В поле "Номер карты" ввести валидные значение
- В поле "Год" ввести ввалидное значение
- В поле "Владелец" ввести ввалидное значение
- В поле "CVC/CVV" ввести валидное значение
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "Месяц" сообщение "Поле обязательно для заполнения"

7. Оставить поле "Номер карты" с пустым значением
- В поле "Месяц" ввести ввалидное значение
- В поле "Год" ввести ввалидное значение
- В поле "Владелец" ввести ввалидное значение
- В поле "CVC/CVV" ввести валидное значение
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "Месяц" сообщение "Поле обязательно для заполнения"

8. Короткий номер "CVC/CVV"
- В поле "Номер карты" ввести валидные значение
- В поле "Месяц" ввести ввалидное значение
- В поле "Год" ввести ввалидное значение
- В поле "Владелец" ввести ввалидное значение
- В поле "CVC/CVV" ввести значение в формате ХХ
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "CVC/CVV" сообщение "Неверный формат"

9. Поле "CVC/CVV" заполнено специальными символами
- В поле "CVC/CVV" ввести специальные символы (%:*)

  *Ожидаемый результат:* значение не вводится

10. Поле "CVC/CVV" заполнено латиницей
- В поле "CVC/CVV" ввести значение латиницей

  *Ожидаемый результат:* значение не вводится

11. Поле "CVC/CVV" заполнено кирилицей
- В поле "CVC/CVV" ввести значение кирилицей

  *Ожидаемый результат:* значение не вводится

12. Поле "Владелец" заполнено цифрами
- В поле "Владелец" ввести значение цифрами

  *Ожидаемый результат:* под полем "Владелец" сообщение "Неверный формат"

13. Поле "Владелец" заполнено специальными символами
- В поле "Владелец" ввести значение специальными символами

  *Ожидаемый результат:* под полем "Владелец" сообщение "Неверный формат"

14. Поле "Владелец" заполнено кирилицей
- В поле "Владелец" ввести значение кирилицей

  *Ожидаемый результат:* под полем "Владелец" сообщение "Неверный формат"

15. Поле "Год" заполнено специальными символами
- В поле "Год" ввести значение специальными символами

  *Ожидаемый результат:* значение не вводится

16. Поле "Год" заполнено кирилицей
- В поле "Год" ввести значение кирилицей

  *Ожидаемый результат:* значение не вводится

17. Поле "Год" заполнено латиницей
- В поле "Год" ввести значение латиницей

  *Ожидаемый результат:* значение не вводится

18. Поле "Год" заполнено неполностью
- В поле "Год" ввести значение в формате Х
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "Год" сообщение "Неверный формат"

19. Поле "Год" с истёкшим сроком действия карты
- В поле "Год" ввести значение 23
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "Год" сообщение "Истёк срок действия карты"

20. Поле "Месяц" заполнено специальными символами
- В поле "Месяц" ввести значение специальными символами

  *Ожидаемый результат:* значение не вводится

21. Поле "Месяц" заполнено кирилицей
- В поле "Месяц" ввести значение кирилицей

  *Ожидаемый результат:* значение не вводится

22. Поле "Месяц" заполнено латиницей
- В поле "Месяц" ввести значение латиницей

  *Ожидаемый результат:* значение не вводится

23. Поле "Месяц" заполнено неполностью
- В поле "Месяц" ввести значение в формате Х
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "Месяц" сообщение "Неверный формат"

24. Поле "Месяц" заполнено нулевым месяцем
- В поле "Месяц" ввести значения 00
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "Месяц" сообщение "Неверный формат"

25. Поле "Месяц" с истёкшим сроком действия карты
- В поле "Месяц" ввести значения предыдущего месяца
- В поле "Год" ввести значение 24
- Нажать кнопку "Продолжить"

  *Ожидаемый результат:* под полем "Месяц" сообщение "Неверно указан срок действия карты"

26. Поле "Номер карты" заполнено специальными символами
- В поле "Номер карты" ввести значение специальными символами

  *Ожидаемый результат:* значение не вводится

27. Поле "Номер карты" заполнено латиницей
- В поле "Номер карты" ввести значение латиницей

  *Ожидаемый результат:* значение не вводится

28. Поле "Номер карты" заполнено кирилицей
- В поле "Номер карты" ввести значение кирилицей

  *Ожидаемый результат:* значение не вводится

29. Поле "Номер карты" заполнено неполностью
- В поле "Номер карты" ввести значение в формате ХХХХ ХХХХ ХХХХ ХХХ

  *Ожидаемый результат:* под полем "Номер карты" сообщение "Неверный формат"


## Перечень используемых инструментов с обоснованием выбора

- **Google chrome** - быстрый, стабильный браузер с синхронизацией Google аккаунта.
- **GitHub** - платформа для хостинга IT‑проектов и совместной разработки.
- **Git** - платформа для контроля версий.
- **IntelliJ IDEA** - интегрированная среда разработки программного обеспечения.
- **JDK 11** - набор инструментов для разработки на языке Java.
- **JUnit** - фреймворк предназначенный для тестирования программ.
- **Selenide** - фреймворк для автоматизированного тестирования веб-приложений.
- **JavaFaker** - библиотека для генерации случайных данных.
- **Allure** - фреймворк для создания простых и понятных отчётов по тестированию.
- **Grandle** - система для автоматизации сборки приложений и сбора статистики.
- **Docker** - программная платформа для разработки, доставки и запуска контейнерных приложений.
- **Docker Desktop** - локальное приложение которое упрощает управление процессом разработки, развертывания и выполнения приложений в контейнерах Docker


## Перечень и описание возможных рисков при автоматизации

- Риск некорректной настройки СУБД и её запуска.
- Риск при написании кода логики и тестов приложения.
- Риск некорректно работающего заявленного функционала приложения.
- Риск больших затрат ресурсов на автомтизирование тестирования.

## Интервальная оценка с учётом рисков в часах

- Подготовка окружения, запуск БД и симулятора - 14 часов.
- Автоматизация тестирования - 80 часов.
- Составление баг репортов по итогам тестирования -10 часов.
- Составление отчётных документов по итогам тестирования -20 часов.

## План сдачи работ

- Составление плана тестирования до 05.10.24
- Написание автотестов и составление баг репортов до 16.10.24
- Результаты и отчёты тестирования до 19.10.24