
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionShowService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository	repository;

	private Invention					invention;


	@Override
	public void authorise() {
<<<<<<< Updated upstream
		super.getResponse().setAuthorised(true);
=======
		boolean status = false;

		if (super.getRequest().hasData("id", int.class) && this.invention != null) {
			boolean isOwner = this.invention.getInventor().isPrincipal();
			boolean isDraft = this.invention.getDraftMode() != null && this.invention.getDraftMode();

			status = isOwner || !isDraft;
		}

		super.getResponse().setAuthorised(status);
>>>>>>> Stashed changes
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findInventionById(id);
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
	}

	@Override
	public void unbind() {
		super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "id", "draftMode");
	}
}
