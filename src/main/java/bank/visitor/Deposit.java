package bank.visitor;

public class Deposit implements Element {
    public final double amount;
    public final double rate;

    public Deposit(double amount, double rate) {
        this.amount = amount;
        this.rate = rate;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitDeposit(this);
    }
}