
package acme.entities.campaign;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.realms.Spokesperson;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Campaign extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Transient
	@Autowired
	private CampaignRepository	repository;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Spokesperson		spokesperson;

	@Mandatory
	// @ValidTicker
	@Column(unique = true)
	private String				ticker;

	@Mandatory
	// @ValidHeader
	@Column
	private String				name;

	@Mandatory
	// @ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoment(constraint = ValidMoment.Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Moment				startMoment;

	@Mandatory
	@ValidMoment(constraint = ValidMoment.Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Moment				endMoment;

	@Optional
	@ValidUrl
	@Column
	private String				moreInfo;


	@Valid
	@Transient
	private Long monthsActive() {
		Duration duracion = MomentHelper.computeDuration(this.startMoment, this.endMoment);
		return duracion.get(ChronoUnit.MONTHS);
	}

	@Transient
	private Double effort() {
		return this.repository.sumEffortByCampaignId(this.getId());
	};


	@Mandatory
	@Valid
	@Column
	private Boolean draftMode;
}
