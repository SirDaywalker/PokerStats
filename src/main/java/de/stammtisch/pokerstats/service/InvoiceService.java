package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.controller.dtos.InvoiceCreatingRequest;
import de.stammtisch.pokerstats.models.Invoice;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.repository.InvoiceRepository;
import de.stammtisch.pokerstats.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    public void createInvoice(User user, InvoiceCreatingRequest request) {
        for (String email : request.users()) {
            Invoice invoice = new Invoice();

            invoice.setAmount(request.amount());
            invoice.setDue(request.due());
            invoice.setInterest(request.interest());
            invoice.setInterestIntervalDays(request.interestIntervalDays());
            invoice.setTitle(request.title());
            invoice.setCreditor(user);
            invoice.setDebtor(this.userRepository.findByEmail(email));
            this.invoiceRepository.save(invoice);
        }
    }
}
