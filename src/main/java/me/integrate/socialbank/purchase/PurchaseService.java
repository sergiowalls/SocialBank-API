package me.integrate.socialbank.purchase;

import com.braintreegateway.*;
import me.integrate.socialbank.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PurchaseService {

    private PurchaseRepository purchaseRepository;
    private UserRepository userRepository;
    private BraintreeGateway gateway;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, UserRepository userRepository,
                           BraintreeGateway gateway) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.gateway = gateway;
    }

    public PurchaseResult purchaseHours(Purchase purchase, String email) {
        HoursPackage hoursPackage = purchaseRepository.getPackageByName(purchase.getPackageName());
        TransactionRequest request = new TransactionRequest()
                .amount(BigDecimal.valueOf(purchase.getAmount()))
                .paymentMethodNonce(purchase.getNonce())
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        Transaction transaction = null;
        if (result.isSuccess()) {
            transaction = result.getTarget();
        } else {
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                System.err.println(error.getMessage());
            }
            return new PurchaseResult(PurchaseResult.TransactionResults.REJECTED, 0);
        }

        if (transaction == null || transaction.getAmount().doubleValue() != hoursPackage.getPrice())
            return new PurchaseResult(PurchaseResult.TransactionResults.REJECTED, 0);

        purchaseRepository.storePurchase(hoursPackage, email, transaction.getId());
        userRepository.incrementHours(email, hoursPackage.getHours());
        return new PurchaseResult(PurchaseResult.TransactionResults.ACCEPTED, hoursPackage.getHours());
    }
}
