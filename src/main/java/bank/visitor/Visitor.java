package bank.visitor;

public interface Visitor<R> {
    R visitBranch(Branch branch);
    R visitClient(Client client);
    R visitAccount(Account account);
    R visitCard(Card card);
    R visitLoan(Loan loan);
    R visitDeposit(Deposit deposit);
}