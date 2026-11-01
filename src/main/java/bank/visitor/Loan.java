package bank.visitor;

public class Loan implements Element {
    public final double debt;

    public Loan(double debt) {
        this.debt = debt;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitLoan(this);
    }
}