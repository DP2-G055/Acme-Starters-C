
package acme.features.inventor.invention;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.part.Part;
import acme.realms.Inventor;

@Service
public class InventorInventionDeleteService extends AbstractService<Inventor, Invention> {

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

	}

	@Override
	public void validate() {

	}

	@Override
	public void execute() {
		List<Part> parts = this.repository.findPartsByInventionId(this.invention.getId());
		if (!parts.isEmpty())
			for (int i = 0; i < parts.size(); i++)
				this.repository.delete(parts.get(i));
		this.repository.delete(this.invention);
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
