
package acme.features.inventor.invention;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionListService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository	repository;

	private List<Invention>				inventions;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.inventions, "name", "description", "ticker");
	}

	@Override
	public void load() {
		int inventorId = super.getRequest().getPrincipal().getAccountId();
		this.inventions = this.repository.findInventionByUserAccountId(inventorId);
	}

}
