package bank.observer;

public class BalanceNotifier implements Observer {
    @Override
    public void update(Event event) {
        System.out.println("BalanceNotifier: Уведомление клиента " + event.accountId() + " об изменении на " + event.amount());
    }
}