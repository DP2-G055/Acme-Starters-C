
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.datatypes.Money;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class CostValidator extends AbstractValidator<ValidCost, Money> {

	@Override
	protected void initialise(final ValidCost annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Money money, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (money == null)
			result = true;
		else {
			if (money.getCurrency() == null)
				result = false;
			else
				result = money.getCurrency().equals("EUR");
		}
		return result;

	}
}
