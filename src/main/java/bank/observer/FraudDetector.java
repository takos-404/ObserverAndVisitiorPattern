package bank.observer;
import bank.visitor.*;
public class FraudDetector implements Observer {
    private final Branch districtSubtree;
    public FraudDetector(Branch districtSubtree) { this.districtSubtree = districtSubtree; }

    @Override
    public void update(Event event) {
        if (event.type().equals("FRAUD_SUSPICION")) {
            ActiveFilter filter = new ActiveFilter();
            districtSubtree.accept(filter);
        }
    }
}