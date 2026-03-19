
package acme.features.any.milestone;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AbstractService;

import acme.entities.milestone.Milestone;

@Service
public class AnyMilestoneListService extends AbstractService<Any, Milestone> {

	@Autowired
	private AnyMilestoneRepository	repository;

	private Collection<Milestone>	milestones;

	private int						campaignId;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().hasData("campaignId");
		super.setAuthorised(status);
	}

	@Override
	public void load() {
		this.campaignId = super.getRequest().getData("campaignId", int.class);
		this.milestones = this.repository.findMilestonesByCampaignId(this.campaignId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.milestones, "title", "achievements", "effort", "kind");
		super.unbindGlobal("campaignId", this.campaignId);
	}
}
