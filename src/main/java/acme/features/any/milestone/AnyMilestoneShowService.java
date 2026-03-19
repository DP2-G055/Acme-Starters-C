
package acme.features.any.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AbstractService;

import acme.entities.milestone.Milestone;

@Service
public class AnyMilestoneShowService extends AbstractService<Any, Milestone> {

	@Autowired
	private AnyMilestoneRepository	repository;

	private Milestone				milestone;


	@Override
	public void authorise() {
		boolean status;

		status = this.milestone != null && this.milestone.getCampaign() != null && !this.milestone.getCampaign().getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.milestone = this.repository.findMilestoneById(id);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");
		super.unbindGlobal("campaignId", this.milestone.getCampaign().getId());
	}
}
