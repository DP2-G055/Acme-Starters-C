
package acme.features.spokesperson.milestone;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneListService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repository;

	private List<Milestone>					milestones;
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

			status = this.campaign != null && this.campaign.getSpokesperson().getUserAccount().getId() == userAccountId;
		}

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int campaignId;

		campaignId = super.getRequest().getData("campaignId", int.class);
		this.milestones = this.repository.findMilestonesByCampaignId(campaignId);

		if (this.campaign == null)
			this.campaign = this.repository.findCampaignById(campaignId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.milestones, "title", "kind", "effort");
		super.unbindGlobal("campaignId", this.campaign.getId());
		super.unbindGlobal("draftMode", this.campaign.getDraftMode());
	}
}
