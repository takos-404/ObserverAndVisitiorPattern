package bank.observer;
public class AlertAggregator extends TransactionStream implements Observer {
    @Override
    public void update(Event event) {
        if (event.amount() > 10000) {
            notifyObservers(new Event("HIGH_VALUE_ALERT", event.accountId(), event.amount()));
        }
    }
}