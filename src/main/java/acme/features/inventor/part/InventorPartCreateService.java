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

package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.part.Part;
import acme.entities.part.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartCreateService extends AbstractService<Inventor, Part> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventorPartRepository	repository;

	private Part					part;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void load() {
		Invention invention;
		int id;

		id = super.getRequest().getData("inventionId", int.class);
		invention = this.repository.findInventionById(id);
		this.part = new Part();
		this.part.setInvention(invention);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		if (status)
			status = this.part.getInvention().getInventor().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.part, "name", "description", "cost", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.part);
	}

	@Override
	public void execute() {
		this.repository.save(this.part);
	}

	@Override
	public void unbind() {
		SelectChoices kindChoices = SelectChoices.from(PartKind.class, this.part.getKind());
		super.unbindObject(this.part, "name", "description", "cost", "kind");
		super.unbindGlobal("draftMode", this.part.getInvention().getDraftMode());
		super.unbindGlobal("inventionId", this.part.getInvention().getId());
		super.unbindGlobal("kindOptions", kindChoices);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
