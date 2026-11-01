package bank;

import bank.observer.*;
import bank.visitor.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Account acc = new Account(15000);
        Card card = new Card(2000, 5000);
        Loan loan = new Loan(30000);
        Deposit dep = new Deposit(50000, 0.1);

        Client client1 = new Client("Alice", List.of(acc, card));
        Client client2 = new Client("Bob", List.of(loan, dep));
        Branch mainBranch = new Branch(List.of(client1, client2));


        BalanceAccumulator balanceVisitor = new BalanceAccumulator();
        System.out.println("Total Balance in Branch: " + mainBranch.accept(balanceVisitor));

        JsonExportVisitor jsonVisitor = new JsonExportVisitor();
        System.out.println("JSON Export: " + mainBranch.accept(jsonVisitor));

        TransactionStream stream = new TransactionStream();

        AlertAggregator aggregator = new AlertAggregator();
        FraudDetector fraudDetector = new FraudDetector(mainBranch); // Интеграция паттернов!

        stream.attach("TRANSFER", aggregator);
        stream.attach("TRANSFER", new BalanceNotifier());
        stream.attach("TRANSFER", new LimitMonitor());
        stream.attach("FRAUD_SUSPICION", fraudDetector);

        aggregator.attach("HIGH_VALUE_ALERT", new AuditLog());


        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("\n--- Запуск транзакций ---");
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                stream.notifyObservers(new Event("TRANSFER", "ACC-123", 500));
                stream.notifyObservers(new Event("TRANSFER", "ACC-999", 15000)); // Вызовет HIGH_VALUE_ALERT
                stream.notifyObservers(new Event("FRAUD_SUSPICION", "ACC-000", 0)); // Вызовет визитор
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}