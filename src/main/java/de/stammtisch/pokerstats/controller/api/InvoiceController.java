package de.stammtisch.pokerstats.controller.api;

import de.stammtisch.pokerstats.controller.dtos.InvoiceCreatingRequest;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.service.AuthenticationService;
import de.stammtisch.pokerstats.service.InvoiceService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/invoice")
@AllArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceServive;
    private final AuthenticationService authenticationService;

    @PostMapping("/create-invoice")
    public ResponseEntity<String> createInvoice(
            @RequestHeader("Cookie") String cookies,
            @RequestBody InvoiceCreatingRequest request
    ) {
        final User user;
        try {
            user = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            return new ResponseEntity<>("Problem mit User.", HttpStatus.BAD_REQUEST);
        }
        this.invoiceServive.createInvoice(user, request);
        return new ResponseEntity<>("Erfolgreich Rechnung angelegt.", HttpStatus.CREATED);
    }

}
