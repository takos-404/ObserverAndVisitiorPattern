package bank.observer;
public record Event(String type, String accountId, double amount) {}