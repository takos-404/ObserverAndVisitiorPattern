package bank.visitor;

import java.util.List;

public class Branch implements Element {
    public final List<Client> clients;

    public Branch(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitBranch(this);
    }
}