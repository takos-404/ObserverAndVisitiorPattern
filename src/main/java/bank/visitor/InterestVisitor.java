package bank.visitor;

public class InterestVisitor implements Visitor<Double> {
    @Override
    public Double visitBranch(Branch branch) {
        double sum = 0;
        for (Client c : branch.clients) sum += c.accept(this);
        return sum;
    }

    @Override
    public Double visitClient(Client client) {
        double sum = 0;
        for (Element e : client.products) sum += e.accept(this);
        return sum;
    }

    @Override
    public Double visitAccount(Account account) { return 0.0; }

    @Override
    public Double visitCard(Card card) { return 0.0; }

    @Override
    public Double visitLoan(Loan loan) { return loan.debt * 0.15; } // 15% годовых долга

    @Override
    public Double visitDeposit(Deposit deposit) { return deposit.amount * deposit.rate; }
}