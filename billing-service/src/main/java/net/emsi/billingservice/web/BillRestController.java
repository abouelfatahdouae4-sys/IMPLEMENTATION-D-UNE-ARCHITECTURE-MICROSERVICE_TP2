package net.emsi.billingservice.web;

import net.emsi.billingservice.entities.Bill;
import net.emsi.billingservice.feign.CustomerRestClient;
import net.emsi.billingservice.feign.ProductRestClient;
import net.emsi.billingservice.repository.BillRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {
    private final BillRepository billRepository;
    private final CustomerRestClient customerRestClient;
    private final ProductRestClient productRestClient;

    public BillRestController(BillRepository billRepository,
                              CustomerRestClient customerRestClient,
                              ProductRestClient productRestClient) {
        this.billRepository = billRepository;
        this.customerRestClient = customerRestClient;
        this.productRestClient = productRestClient;
    }

    /** Facture complete : facture + client + produits (agregation via OpenFeign). */
    @GetMapping("/fullBill/{id}")
    public Bill getBill(@PathVariable("id") Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill " + id + " not found"));
        bill.setCustomer(customerRestClient.getCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(pi ->
                pi.setProduct(productRestClient.getProductById(pi.getProductId())));
        return bill;
    }
}
