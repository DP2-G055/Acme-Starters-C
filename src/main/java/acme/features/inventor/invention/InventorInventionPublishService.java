
package acme.features.inventor.invention;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionPublishService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository	repository;

	private Invention					invention;


	@Override
	public void load() {
		try {
			if (super.getRequest().hasData("id", int.class)) {
				int id = super.getRequest().getData("id", int.class);
				this.invention = this.repository.findInventionById(id);
			}
		} catch (Exception e) {
			this.invention = null;
		}

	}

	@Override
	public void authorise() {
		boolean status;
		int inventorId;

		if (!super.getRequest().hasData("id", int.class))
			status = false;
		else {
			inventorId = super.getRequest().getPrincipal().getAccountId();

			status = this.invention != null && this.invention.getInventor().getUserAccount().getId() == inventorId && this.invention.getDraftMode();
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);

		if (this.invention != null) {
			int partsCount = this.repository.countPartsByInventionId(this.invention.getId());
			boolean hasParts = partsCount > 0;
			super.getResponse().getErrors().state(super.getRequest(), hasParts, "ticker", "inventor.invention.form.error.no-parts");
		}
		if (this.invention.getStartMoment() != null && this.invention.getEndMoment() != null) {
			Date startMoment = this.invention.getStartMoment();
			Date endMoment = this.invention.getEndMoment();
			Date now = MomentHelper.getCurrentMoment();
			boolean validInterval = MomentHelper.isBefore(now, endMoment) && MomentHelper.isBefore(now, endMoment);
			super.getResponse().getErrors().state(super.getRequest(), validInterval, "startMoment", "inventor.invention.form.error.valid-interval.after-current-moment");
			if (validInterval) {
				validInterval = MomentHelper.isBefore(startMoment, endMoment);
				super.getResponse().getErrors().state(super.getRequest(), validInterval, "startMoment", "inventor.invention.form.error.valid-interval.start-after-end");
			}
		}
	}

	@Override
	public void execute() {
		this.invention.setDraftMode(false);
		this.repository.save(this.invention);
	}

	@Override
	public void unbind() {
		if (this.invention != null)
			super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
