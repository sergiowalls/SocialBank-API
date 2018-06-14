package me.integrate.socialbank.purchase;

import com.braintreegateway.BraintreeGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class PurchaseController {
    private PurchaseService purchaseService;
    private BraintreeGateway gateway;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, BraintreeGateway gateway) {
        this.purchaseService = purchaseService;
        this.gateway = gateway;
    }

    @GetMapping("/purchase")
    @ResponseStatus(HttpStatus.OK)
    public String obtainToken() {
        return gateway.clientToken().generate();
    }

    @PostMapping("/purchase")
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseResult purchaseHours(Principal principal, @RequestBody Purchase purchase) {
        return purchaseService.purchaseHours(purchase, principal.getName());
    }

}
