package me.integrate.socialbank.purchase;


public interface PurchaseRepository {
    HoursPackage getPackageByName(String packageName);

    void storePurchase(HoursPackage hoursPackage, String email, String transactionID);
}
