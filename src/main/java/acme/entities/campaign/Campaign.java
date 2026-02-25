
package acme.entities.campaign;

import java.time.Duration;
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
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidCampaign;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Spokesperson;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidCampaign
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
	@ValidTicker
	@Column(unique = true)
	private String				ticker;

	@Mandatory
	@ValidHeader
	@Column
	private String				name;

	@Mandatory
	@ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoment(constraint = ValidMoment.Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startMoment;

	@Mandatory
	@ValidMoment(constraint = ValidMoment.Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endMoment;

	@Optional
	@ValidUrl
	@Column
	private String				moreInfo;


	@Valid
	@Transient
	private Double monthsActive() {
		Duration duracion = MomentHelper.computeDuration(this.startMoment, this.endMoment);
		return (double) 0.0;
	}

	//corregir esfuerzo a 0 si no hay milestone en vez de null
	@Transient
	private Double effort() {
		if (this.repository.getMilestonesByCampaignId(this.getId()).isEmpty())
			return 0.0;
		else
			return this.repository.sumEffortByCampaignId(this.getId());
	}


	@Mandatory
	@Valid
	@Column
	private Boolean draftMode;
}
