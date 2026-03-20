/*
 * InventorPartUpdateService.java
 *
 * Copyright (C) 2012-2026 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionUpdateService extends AbstractService<Inventor, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventorInventionRepository	repository;

	private Invention					invention;

	// AbstractService interface ----------------------------------------------ç


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
	}

	@Override
	public void execute() {
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
