
package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.milestone.Milestone;
import acme.entities.milestone.MilestoneKind;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneUpdateService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repository;

	private Milestone						milestone;


	@Override
	public void authorise() {
		boolean status;
		int userAccountId;

		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			status = this.milestone != null && this.milestone.getCampaign() != null && this.milestone.getCampaign().getSpokesperson().getUserAccount().getId() == userAccountId && this.milestone.getCampaign().getDraftMode();
		}

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.milestone = this.repository.findMilestoneById(id);
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
		SelectChoices kindChoices = SelectChoices.from(MilestoneKind.class, this.milestone.getKind());
		super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");
		super.unbindGlobal("campaignId", this.milestone.getCampaign().getId());
		super.unbindGlobal("draftMode", this.milestone.getCampaign().getDraftMode());
		super.unbindGlobal("kindOptions", kindChoices);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
