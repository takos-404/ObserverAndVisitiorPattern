package bank.observer;

public class LimitMonitor implements Observer {
    @Override
    public void update(Event event) {
        if (event.amount() > 5000) {
            System.out.println("LimitMonitor: Транзакция " + event.accountId() + " близка к лимиту!");
        }
    }
}