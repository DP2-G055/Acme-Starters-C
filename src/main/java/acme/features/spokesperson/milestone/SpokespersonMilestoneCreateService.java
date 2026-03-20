
package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneCreateService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repository;

	private Milestone						milestone;
	private Campaign						campaign;


	@Override
	public void authorise() {
		boolean status;
		int userAccountId;

		if (!super.getRequest().hasData("campaignId"))
			status = false;
		else {
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			int campaignId = super.getRequest().getData("campaignId", int.class);
			this.campaign = this.repository.findCampaignById(campaignId);

			status = this.campaign != null && this.campaign.getSpokesperson().getUserAccount().getId() == userAccountId && this.campaign.getDraftMode();
		}

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		if (this.campaign == null) {
			int campaignId = super.getRequest().getData("campaignId", int.class);
			this.campaign = this.repository.findCampaignById(campaignId);
		}

		this.milestone = new Milestone();
		this.milestone.setCampaign(this.campaign);
	}

	@Override
	public void bind() {
		super.bindObject(this.milestone, "title", "achievements", "effort", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.milestone);
	}

	@Override
	public void execute() {
		this.repository.save(this.milestone);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");
		super.unbindGlobal("campaignId", this.campaign.getId());
		super.unbindGlobal("draftMode", this.campaign.getDraftMode());
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
