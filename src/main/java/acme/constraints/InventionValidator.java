
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.helpers.MomentHelper;
import acme.entities.invention.Invention;
import acme.entities.invention.InventionRepository;

public class InventionValidator implements ConstraintValidator<ValidInvention, Invention> {

	@Autowired
	private InventionRepository repository;


	@Override
	public boolean isValid(final Invention invention, final ConstraintValidatorContext context) {
		if (invention == null)
			return true;
		boolean isValid = true;

		if (invention.getEndMoment() != null && invention.getStartMoment() != null && MomentHelper.isAfter(invention.getStartMoment(), invention.getEndMoment()))
			isValid = false;

		if (invention.getDraftMode() != null && !invention.getDraftMode())
			if (invention.getId() != 0) {
				int partsCount = this.repository.countPartsByInventionId(invention.getId());
				if (partsCount < 1)
					isValid = false;
			} else
				isValid = false;
		return isValid;

	}
}
