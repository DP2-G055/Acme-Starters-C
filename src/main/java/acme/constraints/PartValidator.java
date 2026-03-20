
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.part.Part;

@Validator
public class PartValidator extends AbstractValidator<ValidPart, Part> {

	@Override
	protected void initialise(final ValidPart annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Part part, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (part == null)
			result = true;
		else {
			boolean isCostInEuros;
			if (part.getCost() == null || part.getCost().getCurrency() == null)
				isCostInEuros = false;
			else
				isCostInEuros = part.getCost().getCurrency().equals("EUR");
			super.state(context, isCostInEuros, "cost", "acme.validation.cost.message");
			result = !super.hasErrors(context);
		}
		return result;
	}
}
