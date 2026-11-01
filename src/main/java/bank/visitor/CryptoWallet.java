package bank.visitor;

public class CryptoWallet implements Element {
    public final double btcBalance;
    public CryptoWallet(double btcBalance) { this.btcBalance = btcBalance; }

    @Override
    public <R> R accept(Visitor<R> visitor) { return visitor.visitCryptoWallet(this); }
}