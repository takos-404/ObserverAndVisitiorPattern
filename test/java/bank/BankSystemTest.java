package bank;

import bank.observer.*;
import bank.visitor.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class BankSystemTest {

    @Test
    void test1_observerAttachAndNotify() {
        TransactionStream stream = new TransactionStream();
        AtomicInteger count = new AtomicInteger(0);
        stream.attach("TEST", e -> count.incrementAndGet());
        stream.notifyObservers(new Event("TEST", "1", 100));
        assertEquals(1, count.get());
    }

    @Test
    void test2_observerDetach() {
        TransactionStream stream = new TransactionStream();
        AtomicInteger count = new AtomicInteger(0);
        Observer obs = e -> count.incrementAndGet();
        stream.attach("TEST", obs);
        stream.detach("TEST", obs);
        stream.notifyObservers(new Event("TEST", "1", 100));
        assertEquals(0, count.get());
    }

    @Test
    void test3_multipleEventTypes() {
        TransactionStream stream = new TransactionStream();
        AtomicInteger count = new AtomicInteger(0);
        stream.attach("A", e -> count.incrementAndGet());
        stream.notifyObservers(new Event("B", "1", 100));
        assertEquals(0, count.get());
    }

    @Test
    void test4_limitMonitorTriggersCorrectly() {
        TransactionStream stream = new TransactionStream();
        AtomicInteger alerts = new AtomicInteger(0);
        stream.attach("TRANSFER", e -> {
            if (e.amount() > 5000) alerts.incrementAndGet();
        });
        stream.notifyObservers(new Event("TRANSFER", "1", 4000));
        stream.notifyObservers(new Event("TRANSFER", "2", 6000));
        assertEquals(1, alerts.get());
    }

    @Test
    void test5_alertAggregatorChain() {
        AlertAggregator aggregator = new AlertAggregator();
        AtomicInteger highValueCount = new AtomicInteger(0);
        aggregator.attach("HIGH_VALUE_ALERT", e -> highValueCount.incrementAndGet());
        aggregator.update(new Event("TRANSFER", "1", 15000)); // > 10000
        aggregator.update(new Event("TRANSFER", "2", 5000));  // < 10000
        assertEquals(1, highValueCount.get());
    }

    @Test
    void test6_fraudDetectorIntegration() {
        Account acc = new Account(100);
        Branch branch = new Branch(List.of(new Client("A", List.of(acc))));
        FraudDetector detector = new FraudDetector(branch);
        assertDoesNotThrow(() -> detector.update(new Event("FRAUD_SUSPICION", "A", 0)));
    }


    @Test
    void test7_balanceAccumulatorAccount() {
        assertEquals(150.0, new Account(150).accept(new BalanceAccumulator()));
    }

    @Test
    void test8_balanceAccumulatorLoan() {
        assertEquals(-500.0, new Loan(500).accept(new BalanceAccumulator())); // Долг вычитается
    }

    @Test
    void test9_balanceAccumulatorTree() {
        Client c = new Client("A", List.of(new Account(1000), new Loan(200)));
        Branch b = new Branch(List.of(c));
        assertEquals(800.0, b.accept(new BalanceAccumulator())); // 1000 - 200 = 800
    }

    @Test
    void test10_interestVisitorLoan() {
        assertEquals(150.0, new Loan(1000).accept(new InterestVisitor())); // 15% от 1000 = 150
    }

    @Test
    void test11_interestVisitorDeposit() {
        assertEquals(50.0, new Deposit(1000, 0.05).accept(new InterestVisitor())); // 5% от 1000 = 50
    }

    @Test
    void test12_activeFilterEmpty() {
        Account acc = new Account(0); // Баланс 0
        assertTrue(acc.accept(new ActiveFilter()).isEmpty());
    }

    @Test
    void test13_activeFilterHasElements() {
        Branch b = new Branch(List.of(new Client("A", List.of(new Account(100), new Account(0)))));
        assertEquals(1, b.accept(new ActiveFilter()).size());
    }

    @Test
    void test14_xmlExportFormat() {
        Client c = new Client("John", List.of(new Account(500)));
        String xml = c.accept(new XmlExportVisitor());
        assertTrue(xml.contains("<client name=\"John\">"));
        assertTrue(xml.contains("<account balance=\"500.0\"/>"));
    }


    @Test
    void test15_stressTestConcurrentEvents() throws InterruptedException {
        TransactionStream stream = new TransactionStream();
        AtomicInteger eventCount = new AtomicInteger(0);
        stream.attach("STRESS", e -> eventCount.incrementAndGet());

        ExecutorService executor = Executors.newFixedThreadPool(4); // 4 потока
        for (int i = 0; i < 4; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) { // 1000 событий на поток
                    stream.notifyObservers(new Event("STRESS", "ID", 10));
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(4000, eventCount.get());
    }
}