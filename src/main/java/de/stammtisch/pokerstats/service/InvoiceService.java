package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.controller.dtos.InvoiceCreatingRequest;
import de.stammtisch.pokerstats.models.Invoice;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.repository.InvoiceRepository;
import de.stammtisch.pokerstats.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
            invoice.setDebtor(user);
            invoice.setCreditor(this.userRepository.findByEmail(email));
            this.invoiceRepository.save(invoice);
        }
    }
    
    public Set<Invoice> findByDebtor(User debtor) {
    	return this.invoiceRepository.findByDebtor(debtor).orElseThrow();
    }
    
    public Set<Invoice> findByCreditor(User creditor) {
    	return this.invoiceRepository.findByCreditor(creditor).orElseThrow();
    }
    
    public List<Invoice> getAllInvoices() {
    	return this.invoiceRepository.findAll();
    }
}
