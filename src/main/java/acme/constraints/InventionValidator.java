
package acme.constraints;

import java.util.Date;

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
				boolean uniqueInvention;
				Invention existingInvention;

				existingInvention = this.repository.findInventionByTicker(invention.getTicker());
				uniqueInvention = existingInvention == null || existingInvention.equals(invention);

				super.state(context, uniqueInvention, "ticker", "acme.validation.invention.duplicated-ticker.message");
			}
			{
				boolean hasParts;
				int numParts = this.repository.countPartsByInventionId(invention.getId());

				hasParts = invention.getDraftMode() || numParts > 0;

				super.state(context, hasParts, "*", "acme.validation.invention.parts.message");
			}
			{
				boolean isValidStartMoment;
				boolean isValidEndMoment;

				Date now = MomentHelper.getBaseMoment();

				isValidStartMoment = MomentHelper.isAfterOrEqual(invention.getStartMoment(), now) && invention.getDraftMode() || !invention.getDraftMode();
				isValidEndMoment = MomentHelper.isAfterOrEqual(invention.getEndMoment(), now) && invention.getDraftMode() || !invention.getDraftMode();

				super.state(context, isValidStartMoment, "startMoment", "acme.validation.invention.valid-start-moment.message");
				super.state(context, isValidEndMoment, "endMoment", "acme.validation.invention.valid-end-moment.message");
			}
			{
				boolean isValidInterval;

				isValidInterval = MomentHelper.isAfter(invention.getEndMoment(), invention.getStartMoment());

				super.state(context, isValidInterval, "endMoment", "acme.validation.invention.valid-interval.message");
			}
			result = !super.hasErrors(context);
		}
		return result;
	}
}
