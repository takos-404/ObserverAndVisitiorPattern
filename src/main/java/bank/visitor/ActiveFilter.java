package bank.visitor;

import java.util.ArrayList;
import java.util.List;

public class ActiveFilter implements Visitor<List<Element>> {
    @Override
    public List<Element> visitBranch(Branch branch) {
        List<Element> result = new ArrayList<>();
        for (Client c : branch.clients) result.addAll(c.accept(this));
        return result;
    }

    @Override
    public List<Element> visitClient(Client client) {
        List<Element> result = new ArrayList<>();
        for (Element e : client.products) result.addAll(e.accept(this));
        return result;
    }

    @Override
    public List<Element> visitAccount(Account account) {
        return account.balance > 0 ? List.of(account) : List.of();
    }

    @Override
    public List<Element> visitCard(Card card) {
        return card.balance > 0 ? List.of(card) : List.of();
    }

    @Override
    public List<Element> visitLoan(Loan loan) {
        return loan.debt > 0 ? List.of(loan) : List.of();
    }

    @Override
    public List<Element> visitDeposit(Deposit deposit) {
        return deposit.amount > 0 ? List.of(deposit) : List.of();
    }
}