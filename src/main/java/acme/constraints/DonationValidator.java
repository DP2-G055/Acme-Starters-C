
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.sponsorship.Donation;
import acme.entities.sponsorship.SponsorshipRepository;

@Validator
public class DonationValidator extends AbstractValidator<ValidDonation, Donation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorshipRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidDonation annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Donation donation, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (donation == null)
			result = true;
		else {
			{
				boolean currencyInEUR;

				currencyInEUR = donation.getMoney().getCurrency() == "EUR";

				super.state(context, currencyInEUR, "money", "acme.validation.donation.currency.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}

}
