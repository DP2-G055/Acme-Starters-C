
package acme.features.spokesperson.campaign;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignPublishService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repository;

	private Campaign						campaign;


	@Override
	public void authorise() {
		boolean status;
		int userAccountId;

		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			status = this.campaign != null && this.campaign.getSpokesperson().getUserAccount().getId() == userAccountId && this.campaign.getDraftMode();
		}

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void bind() {
		super.bindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		int milestonesCount;

		super.validateObject(this.campaign);

		milestonesCount = this.repository.countMilestonesByCampaignId(this.campaign.getId());
		super.state(milestonesCount > 0, "ticker", "spokesperson.campaign.form.error.no-milestones");

		if (this.campaign.getStartMoment() != null && this.campaign.getEndMoment() != null) {
			Date now;
			boolean validInterval;

			now = MomentHelper.getCurrentMoment();
			validInterval = MomentHelper.isBefore(now, this.campaign.getStartMoment()) && MomentHelper.isBefore(this.campaign.getStartMoment(), this.campaign.getEndMoment());

			super.state(validInterval, "startMoment", "spokesperson.campaign.form.error.invalid-period");
		}
	}

	@Override
	public void execute() {
		this.campaign.setDraftMode(false);
		this.repository.save(this.campaign);
	}

	@Override
	public void unbind() {
		if (this.campaign != null)
			super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
