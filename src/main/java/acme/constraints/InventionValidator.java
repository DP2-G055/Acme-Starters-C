
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.invention.Invention;
import acme.entities.invention.InventionRepository;

@Validator
public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	@Autowired
	private InventionRepository repository;


	@Override
	protected void initialise(final ValidInvention annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Invention invention, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (invention == null)
			result = true;
		else {
			{
				boolean uniqueInvention = true;
				Invention existingInvention;
				if (invention.getTicker() != null) {
					existingInvention = this.repository.findInventionByTicker(invention.getTicker());
					uniqueInvention = existingInvention == null || existingInvention.equals(invention);
				}

				super.state(context, uniqueInvention, "ticker", "acme.validation.invention.duplicated-ticker.message");
			}
			{
				boolean hasParts = false;
				int numParts = this.repository.countPartsByInventionId(invention.getId());
				hasParts = invention.getDraftMode() || numParts > 0;

				super.state(context, hasParts, "*", "acme.validation.invention.parts.message");
			}
			{
				boolean isValidInterval = true;
				if (!invention.getDraftMode())
					if (invention.getStartMoment() != null && invention.getEndMoment() != null)
						isValidInterval = MomentHelper.isAfter(invention.getEndMoment(), invention.getStartMoment());
					else
						isValidInterval = false;

				super.state(context, isValidInterval, "endMoment", "acme.validation.invention.valid-interval.message");
			}
			result = !super.hasErrors(context);
		}
		return result;
	}
}
