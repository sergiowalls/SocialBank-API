package me.integrate.socialbank.purchase;

public class Purchase {
    private String packageName;
    private String nonce;
    private double amount;


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getNonce()
    {
        return nonce;
    }

    public void setNonce(String nonce)
    {
        this.nonce = nonce;
    }
}
