package bank.visitor;

public class JsonExportVisitor implements Visitor<String> {
    @Override
    public String visitBranch(Branch branch) {
        StringBuilder sb = new StringBuilder("{\"branch\": [");
        for (int i = 0; i < branch.clients.size(); i++) {
            sb.append(branch.clients.get(i).accept(this));
            if (i < branch.clients.size() - 1) sb.append(",");
        }
        return sb.append("]}").toString();
    }

    @Override
    public String visitClient(Client client) {
        StringBuilder sb = new StringBuilder("{\"client\": \"").append(client.name).append("\", \"products\": [");
        for (int i = 0; i < client.products.size(); i++) {
            sb.append(client.products.get(i).accept(this));
            if (i < client.products.size() - 1) sb.append(",");
        }
        return sb.append("]}").toString();
    }

    @Override
    public String visitAccount(Account account) { return "{\"type\":\"account\", \"balance\": " + account.balance + "}"; }

    @Override
    public String visitCard(Card card) { return "{\"type\":\"card\", \"balance\": " + card.balance + ", \"limit\": " + card.limit + "}"; }

    @Override
    public String visitLoan(Loan loan) { return "{\"type\":\"loan\", \"debt\": " + loan.debt + "}"; }

    @Override
    public String visitDeposit(Deposit deposit) { return "{\"type\":\"deposit\", \"amount\": " + deposit.amount + ", \"rate\": " + deposit.rate + "}"; }
}