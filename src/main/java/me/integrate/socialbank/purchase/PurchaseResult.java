package me.integrate.socialbank.purchase;

public class PurchaseResult {

    enum TransactionResults {
        ACCEPTED, REJECTED
    }

    private TransactionResults transactionResults;
    private int hours;

    public PurchaseResult(TransactionResults transactionResults, int hours)
    {
        this.transactionResults = transactionResults;
        this.hours = hours;
    }

    public TransactionResults getTransactionResults()
    {
        return transactionResults;
    }

    public void setTransactionResults(TransactionResults transactionResults)
    {
        this.transactionResults = transactionResults;
    }

    public int getHours()
    {
        return hours;
    }

    public void setHours(int hours)
    {
        this.hours = hours;
    }
}
