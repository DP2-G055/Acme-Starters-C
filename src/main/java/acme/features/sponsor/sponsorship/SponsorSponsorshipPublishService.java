
package acme.features.sponsor.sponsorship;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository	repository;

	private Sponsorship						sponsorship;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findSponsorshipById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.sponsorship != null && this.sponsorship.isDraftMode() && this.sponsorship.getSponsor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.sponsorship);

		{
			boolean hasDonations;

			hasDonations = this.repository.numDonationsBySponsorshipId(this.sponsorship.getId()) > 0;
			super.state(hasDonations, "*", "acme.validation.sponsorship.donations.message");
		}
		{
			boolean futureInterval;
			boolean validInterval;

			Date startMoment = this.sponsorship.getStartMoment();
			Date endMoment = this.sponsorship.getEndMoment();

			futureInterval = MomentHelper.isAfter(this.sponsorship.getStartMoment(), MomentHelper.getCurrentMoment()) && //
				MomentHelper.isAfter(this.sponsorship.getEndMoment(), MomentHelper.getCurrentMoment());
			super.state(futureInterval, "startMoment", "acme.validation.sponsorship.future-interval.message");

			validInterval = MomentHelper.isAfter(endMoment, startMoment);
			super.state(validInterval, "startMoment", "acme.validation.sponsorship.valid-interval.message");

		}
	}

	@Override
	public void execute() {
		this.sponsorship.setDraftMode(false);
		this.repository.save(this.sponsorship);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

}
