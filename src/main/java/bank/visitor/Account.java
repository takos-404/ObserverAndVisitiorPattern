package bank.visitor;

public class Account implements Element {
    public final double balance;

    public Account(double balance) {
        this.balance = balance;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitAccount(this);
    }
}