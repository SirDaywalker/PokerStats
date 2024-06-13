package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.controller.dtos.InvoiceCreatingRequest;
import de.stammtisch.pokerstats.exceptions.UnautherizedUserException;
import de.stammtisch.pokerstats.models.Invoice;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.repository.InvoiceRepository;
import de.stammtisch.pokerstats.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvoiceService {
	
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    public void createInvoice(User creditor, InvoiceCreatingRequest request) {
        for (String debtorEmail : request.users()) {
            Invoice invoice = new Invoice();
            User debtor = this.userRepository.findByEmail(debtorEmail).orElse(null);
            
            invoice.setDue(request.due()+ZonedDateTime.now().getOffset().getTotalSeconds()*1000);
            invoice.setAmount(request.amount());
            invoice.setInterest(request.interest());
            invoice.setInterestIntervalWeeks(request.interestIntervalWeeks());
            invoice.setTitle(request.title());
            invoice.setDebtor(debtor);
            invoice.setCreditor(creditor);
            
            String[] date = new Date(request.due()).toString().split(" ");
            
            this.emailService.sendInvoiceMail(
            		debtor.getUsername(), 
            		debtorEmail,
            		request.title(),
            		creditor.getUsername(), 
            		request.amount().toString(), 
            		date[0] + ", " + date[2] + ". " + date[1] + " " + date[5] + ", " + date[3], 
            		request.interest().toString(), 
            		request.interestIntervalWeeks().toString()
            	);
            
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

	public void deleteInvoice(User user, long id) throws UnautherizedUserException {
		Invoice invoice = this.invoiceRepository.findById(id).orElseThrow();
		if(!user.equals(invoice.getCreditor())) {
			throw new UnautherizedUserException();
		}
		this.invoiceRepository.deleteById(id);
	}
}
