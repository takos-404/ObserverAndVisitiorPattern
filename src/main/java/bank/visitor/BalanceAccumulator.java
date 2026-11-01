package bank.visitor;

public class BalanceAccumulator implements Visitor<Double> {
    @Override
    public Double visitBranch(Branch branch) {
        double sum = 0;
        for (Client c : branch.clients) {
            sum += c.accept(this);
        }
        return sum;
    }

    @Override
    public Double visitClient(Client client) {
        double sum = 0;
        for (Element e : client.products) {
            sum += e.accept(this);
        }
        return sum;
    }

    @Override
    public Double visitAccount(Account account) { return account.balance; }

    @Override
    public Double visitCard(Card card) { return card.balance; }

    @Override
    public Double visitLoan(Loan loan) { return -loan.debt; }

    @Override
    public Double visitDeposit(Deposit deposit) { return deposit.amount; }
    @Override public Double visitCryptoWallet(CryptoWallet cw) { return cw.btcBalance * 90000.0; }
}