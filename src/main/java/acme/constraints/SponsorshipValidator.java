
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipRepository;

@Validator
public class SponsorshipValidator extends AbstractValidator<ValidSponsorship, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorshipRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidSponsorship annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Sponsorship sponsorship, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (sponsorship == null)
			result = true;
		else {
			{
				boolean uniqueSponsorship;
				Sponsorship existingSponsorship;

				existingSponsorship = this.repository.findSponsorshipByTicker(sponsorship.getTicker());
				uniqueSponsorship = existingSponsorship == null || existingSponsorship.equals(sponsorship);

				super.state(context, uniqueSponsorship, "ticker", "acme.validation.sponsorship.duplicated-ticker.message");
			}
			{
				boolean hasDonations;
				int numDonations = this.repository.countDonationsBySponsorship(sponsorship.getId());

				hasDonations = sponsorship.isDraftMode() || numDonations == 0;

				super.state(context, hasDonations, "*", "acme.validation.sponsorship.donations.message");
			}
			{
				boolean isValidInterval;

				isValidInterval = MomentHelper.isAfter(sponsorship.getEndMoment(), sponsorship.getStartMoment());

				super.state(context, isValidInterval, "endMoment", "acme.validation.sponsorship.valid-interval.message");
			}
			result = !super.hasErrors(context);
		}

		return result;
	}

}
