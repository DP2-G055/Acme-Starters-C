
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<<<<<<< Updated upstream
import acme.client.helpers.PrincipalHelper;
=======
import acme.client.helpers.MomentHelper;
>>>>>>> Stashed changes
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
		int id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findInventionById(id);
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
	}

	@Override
	public void authorise() {
<<<<<<< Updated upstream
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		if (status)
			status = this.invention.getInventor().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		status = status && this.invention.getDraftMode();
		super.setAuthorised(status);
=======
		boolean status = false;

		if (super.getRequest().hasData("id", int.class) && this.invention != null) {
			boolean isOwner = this.invention.getInventor().isPrincipal();
			boolean isDraft = this.invention.getDraftMode() != null && this.invention.getDraftMode();

			status = isOwner && isDraft;
		}

		super.getResponse().setAuthorised(status);
>>>>>>> Stashed changes
	}

	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);

		if (!super.getResponse().getErrors().hasErrors("ticker")) {
			int partsCount = this.repository.countPartsByInventionId(this.invention.getId());
			boolean hasParts = partsCount > 0;
			super.state(hasParts, "ticker", "inventor.invention.form.error.no-parts");
		}
<<<<<<< Updated upstream
		{
			// añadir que si la fecha es inferior a la actual enseñar mensaje de error
=======
		if (this.invention.getStartMoment() != null && this.invention.getEndMoment() != null) {
			Date startMoment = this.invention.getStartMoment();
			Date endMoment = this.invention.getEndMoment();
			Date now = MomentHelper.getCurrentMoment();
			boolean validInterval = MomentHelper.isBefore(now, startMoment);
			super.state(validInterval, "startMoment", "inventor.invention.form.error.valid-interval.after-current-moment");
			if (validInterval) {
				validInterval = MomentHelper.isBefore(startMoment, endMoment);
				super.state(validInterval, "startMoment", "inventor.invention.form.error.valid-interval.start-after-end");
			}
>>>>>>> Stashed changes
		}

	}

	@Override
	public void execute() {
		this.invention.setDraftMode(false);
		this.repository.save(this.invention);
	}

	@Override
	public void unbind() {
<<<<<<< Updated upstream
		super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
=======
		if (this.invention != null) {
			super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
			super.unbindGlobal("cost", this.invention.cost());
			super.unbindGlobal("monthsActive", this.invention.monthsActive());
		}
>>>>>>> Stashed changes
	}
}
