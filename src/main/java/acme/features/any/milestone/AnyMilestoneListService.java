
package acme.features.any.milestone;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.milestone.Milestone;

@Service
public class AnyMilestoneListService extends AbstractService<Any, Milestone> {

	@Autowired
	private AnyMilestoneRepository	repository;

	private List<Milestone>			milestones;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Campaign campaignPublished;

		if (!super.getRequest().hasData("campaignId"))
			status = false;
		else {
			id = super.getRequest().getData("campaignId", Integer.class);

			campaignPublished = this.repository.findCampaignById(id);

			status = campaignPublished != null && !campaignPublished.getDraftMode();
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.milestones, "title", "achievements", "effort", "kind");
	}

	@Override
	public void load() {
		if (super.getRequest().hasData("campaignId")) {
			int id = super.getRequest().getData("campaignId", int.class);
			this.milestones = this.repository.findMilestonesByCampaignId(id);
		}
	}
}
