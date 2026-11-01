package bank.observer;
import java.util.concurrent.*;
import java.util.*;
public class TransactionStream implements Subject {
    private final Map<String, List<Observer>> listeners = new ConcurrentHashMap<>();

    @Override
    public void attach(String eventType, Observer observer) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(observer);
    }

    @Override
    public void detach(String eventType, Observer observer) {
        listeners.getOrDefault(eventType, new CopyOnWriteArrayList<>()).remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        List<Observer> evListeners = listeners.getOrDefault(event.type(), Collections.emptyList());
        for (Observer obs : evListeners) {
            obs.update(event);
        }
    }
}