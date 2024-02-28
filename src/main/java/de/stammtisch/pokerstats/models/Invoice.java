package de.stammtisch.pokerstats.models;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoices")
@NoArgsConstructor
@Getter
@Setter
public class Invoice {
	@Id
	@Setter(AccessLevel.NONE)
	private Long id;
	
	private double amount;
	private Date due;
	private double interest;
	private int interestIntervalDays;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User debtor;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User creditor;
	
	@Column(length = 4096, nullable = false)
	private String notes;
	
	@PrePersist
    public void prePersist() {
        this.id = System.currentTimeMillis();
    }
}
