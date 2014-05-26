package org.openmrs.module.gaac.validator;

import org.openmrs.module.gaac.Gaac;
import org.openmrs.module.gaac.GaacUtils;
import org.openmrs.module.gaac.api.GaacService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class GaacValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return Gaac.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		Gaac gaac = (Gaac) target;
		if (gaac == null) {
			errors.rejectValue("gaac", "gaac.error.general");
		} else {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gaacIdentifier",
					"gaac.error.identifier");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
					"error.name");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate",
					"gaac.member.error.startDate");

			if (gaac.getGaacId() == null) {
				GaacService gs = GaacUtils.getService();
				Gaac temp = gs.getGaacByName(gaac.getName());
				if (temp != null) {
					errors.rejectValue("name", "gaac.error.name.inuse");
				}
				temp = gs.getGaacByIdentifier(gaac.getGaacIdentifier());
				if (temp != null)
					errors.rejectValue("gaacIdentifier",
							"gaac.error.identifier.inuse");
			}
		}
	}

}
