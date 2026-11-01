package bank.observer;
public interface Subject {
    void attach(String eventType, Observer observer);
    void detach(String eventType, Observer observer);
    void notifyObservers(Event event);
}