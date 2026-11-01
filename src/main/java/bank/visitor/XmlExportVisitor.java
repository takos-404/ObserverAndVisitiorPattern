package bank.visitor;

public class XmlExportVisitor implements Visitor<String> {
    @Override
    public String visitBranch(Branch branch) {
        StringBuilder sb = new StringBuilder("<branch>");
        for (Client c : branch.clients) sb.append(c.accept(this));
        return sb.append("</branch>").toString();
    }

    @Override
    public String visitClient(Client client) {
        StringBuilder sb = new StringBuilder("<client name=\"").append(client.name).append("\">");
        for (Element e : client.products) sb.append(e.accept(this));
        return sb.append("</client>").toString();
    }

    @Override
    public String visitAccount(Account account) { return "<account balance=\"" + account.balance + "\"/>"; }

    @Override
    public String visitCard(Card card) { return "<card balance=\"" + card.balance + "\" limit=\"" + card.limit + "\"/>"; }

    @Override
    public String visitLoan(Loan loan) { return "<loan debt=\"" + loan.debt + "\"/>"; }

    @Override
    public String visitDeposit(Deposit deposit) { return "<deposit amount=\"" + deposit.amount + "\" rate=\"" + deposit.rate + "\"/>"; }
}