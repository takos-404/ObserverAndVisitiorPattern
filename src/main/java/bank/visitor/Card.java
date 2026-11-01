package bank.visitor;

public class Card implements Element {
    public final double balance;
    public final double limit;

    public Card(double balance, double limit) {
        this.balance = balance;
        this.limit = limit;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitCard(this);
    }
}