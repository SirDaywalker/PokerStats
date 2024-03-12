package de.stammtisch.pokerstats.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	private long due;
	private double interest;
	private int interestIntervalWeeks;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User debtor;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User creditor;
	
	@Column(length = 32, nullable = false)
	private String title;
	
	@PrePersist
    public void prePersist() {
        this.id = System.currentTimeMillis();
    }
}
