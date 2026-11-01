package bank.observer;

public class AuditLog implements Observer {
    @Override
    public void update(Event event) {
        System.out.println("AuditLog: Запись в лог - " + event.type() + " | ID: " + event.accountId() + " | Сумма: " + event.amount());
    }
}