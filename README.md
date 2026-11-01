# Bank Transactions System (Observer & Visitor Patterns)

## Design Decisions (Clean Code ch. 11-13)
* **Observer:** Использована модель **Push**. Событие `Event` (Java Record) неизменяемо и легковесно. Слушателям нужны только данные о конкретной транзакции (accountId, amount), поэтому нет необходимости заставлять их тянуть полное состояние (Pull) у источника.
* **Visitor:** Выбран **внутренний обход деревом (visitor does traversal)**. Элементы (`Account`, `Client`) отвечают только за хранение данных, а логика итерации и агрегации инкапсулирована в самих визиторах (например, `visitBranch` вызывает `accept` у списка своих клиентов).
* **Concurrency:** `TransactionStream` использует потокобезопасные коллекции `ConcurrentHashMap` и `CopyOnWriteArrayList`. Это позволяет слушателям безопасно отписываться прямо во время итерации по списку (без `ConcurrentModificationException`) и не блокирует весь субъект при отправке уведомлений.
* **Integration:** При обнаружении подозрительной транзакции (`FRAUD_SUSPICION`), слушатель `FraudDetector` запускает визитор `ActiveFilter` на поддереве `Branch`, собирая все активные счета для блокировки.

## Trade-off Proof
В репозитории сделано два отдельных коммита для демонстрации плюсов и минусов паттерна Visitor:

1. **Коммит 1: Добавление новой операции (`XmlExportVisitor`)**
    * **Измененные файлы:** Добавлен только `XmlExportVisitor.java`.
    * **Стоимость:** Нулевая для существующих классов элементов. Паттерн идеально подходит для добавления новых алгоритмов на стабильном дереве.

2. **Коммит 2: Добавление нового типа узла (`CryptoWallet`)**
    * **Измененные файлы:** Добавлен `CryptoWallet.java`. Изменен интерфейс `Visitor.java` (добавлен `visitCryptoWallet`), а также переписаны **все** 5 существующих классов визиторов (`BalanceAccumulator`, `InterestVisitor`, `ActiveFilter`, `JsonExportVisitor`, `XmlExportVisitor`).
    * **Стоимость:** Очень высокая. В этом заключается главный недостаток паттерна Visitor (Visitor's trade-off). Добавление нового элемента нарушает принцип OCP (Open-Closed Principle) для интерфейса Visitor, заставляя модифицировать каждую операцию в системе.