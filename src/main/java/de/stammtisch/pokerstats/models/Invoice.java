package de.stammtisch.pokerstats.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
	
	@ManyToOne
	private User debtor;
	
	@ManyToOne
	private User creditor;
	
	@Column(length = 4096, nullable = false)
	private String notes;
	
	@PrePersist
    public void prePersist() {
        this.id = System.currentTimeMillis();
    }
}
