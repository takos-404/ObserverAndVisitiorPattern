package bank.visitor;

import java.util.List;

public class Client implements Element {
    public final String name;
    public final List<Element> products;

    public Client(String name, List<Element> products) {
        this.name = name;
        this.products = products;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitClient(this);
    }
}