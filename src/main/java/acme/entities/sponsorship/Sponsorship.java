
package acme.entities.sponsorship;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidHeader;
import acme.constraints.ValidSponsorship;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Sponsor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidSponsorship
public class Sponsorship extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String					ticker;

	@Mandatory
	@ValidHeader
	@Column
	private String					name;

	@Mandatory
	@ValidText
	@Column
	private String					description;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date					startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date					endMoment;

	@Optional
	@ValidUrl
	@Column
	private String					moreInfo;

	@Mandatory
	@Column
	private boolean					draftMode;

	// Derived attributes -----------------------------------------------------

	@Transient
	@Autowired
	private SponsorshipRepository	repository;


	@Transient
	public Money totalMoney() {
		Money result;
		Double wrapper;
		wrapper = this.repository.computeTotalMoney(this.getId());
		result = new Money();
		result.setAmount(wrapper);
		result.setCurrency("EUR");

		return result;

	}

	@Transient
	public double monthsActive() {
		LocalDate start = this.startMoment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		LocalDate end = this.endMoment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		long fullMonths = ChronoUnit.MONTHS.between(start, end);

		LocalDate adjustedStart = start.plusMonths(fullMonths);

		long remainingDays = ChronoUnit.DAYS.between(adjustedStart, end);

		long daysInMonth = adjustedStart.lengthOfMonth();

		double fractionalMonth = (double) remainingDays / daysInMonth;

		double totalMonths = fullMonths + fractionalMonth;

		return BigDecimal.valueOf(totalMonths).setScale(1, RoundingMode.HALF_UP).doubleValue();
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Sponsor sponsor;
}
