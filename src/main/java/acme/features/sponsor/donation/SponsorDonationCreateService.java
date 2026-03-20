
package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;
import acme.entities.sponsorship.DonationKind;
import acme.entities.sponsorship.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorDonationCreateService extends AbstractService<Sponsor, Donation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorDonationRepository	repository;

	private Donation					donation;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		Sponsorship sponsorship;
		int id;

		id = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findSponsorshipById(id);
		this.donation = new Donation();
		this.donation.setSponsorship(sponsorship);
	}

	@Override
	public void authorise() {
		boolean status;
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		status = sponsorship != null && //
			this.donation.getSponsorship().isDraftMode() && this.donation.getSponsorship().getSponsor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.donation, "name", "notes", "money", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.donation);
		{
			if (this.donation.getMoney() != null) {
				boolean currencyInEUR;
				currencyInEUR = this.donation.getMoney().getCurrency().equals("EUR");
				super.state(currencyInEUR, "money", "acme.validation.donation.currency.message");
			}
		}

	}

	@Override
	public void execute() {
		this.repository.save(this.donation);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices choices = SelectChoices.from(DonationKind.class, this.donation.getKind());

		tuple = super.unbindObject(this.donation, "name", "notes", "money", "kind");
		tuple.put("draftMode", this.donation.getSponsorship().isDraftMode());
		tuple.put("sponsorshipId", this.donation.getSponsorship().getId());
		tuple.put("kindChoices", choices);
	}

}
