
package acme.features.inventor.invention;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
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

	}

	@Override
	public void authorise() {
		boolean status;

		status = this.invention != null && this.invention.getInventor().isPrincipal() && this.invention.getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);

		int partsCount = this.repository.countPartsByInventionId(this.invention.getId());
		boolean hasParts = partsCount > 0;
		super.state(hasParts, "ticker", "inventor.invention.form.error.no-parts");
		if (!super.getErrors().hasErrors("startMoment") && !super.getErrors().hasErrors("endMoment")) {
			Date startMoment = this.invention.getStartMoment();
			Date endMoment = this.invention.getEndMoment();
			Date now = MomentHelper.getCurrentMoment();
			boolean validInterval = MomentHelper.isBefore(now, startMoment);
			super.state(validInterval, "startMoment", "inventor.invention.form.error.valid-interval.after-current-moment");
			if (validInterval) {
				validInterval = MomentHelper.isBefore(startMoment, endMoment);
				super.state(validInterval, "startMoment", "inventor.invention.form.error.valid-interval.start-after-end");
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
		Tuple tuple = super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		tuple.put("draftMode", this.invention.getDraftMode());
		tuple.put("cost", this.invention.getCost());
		tuple.put("monthsActive", this.invention.getMonthsActive());
	}
}
