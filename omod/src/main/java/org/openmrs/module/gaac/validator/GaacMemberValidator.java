package org.openmrs.module.gaac.validator;

import java.util.Date;

import org.openmrs.module.gaac.GaacMember;
import org.openmrs.module.gaac.GaacUtils;
import org.openmrs.module.gaac.api.GaacService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class GaacMemberValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return GaacMember.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		GaacMember member = (GaacMember) target;
		if (member == null) {
			errors.rejectValue("gaacMember", "gaac.member.error.general");
		} else {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate",
					"gaac.member.error.startDate");
			if (member.getGaacMemberId() == null) {
				GaacService gs = GaacUtils.getService();
				GaacMember temp = gs.getGaacMemberByMember(member.getMember());
				if (temp != null) {
					errors.rejectValue("member", "gaac.member.error.ingaac");
				}
			}
			if ((member.getGaac() != null)
					&& (member.getStartDate().before(member.getGaac()
							.getStartDate()))) {
				errors.rejectValue("startDate",
						"gaac.member.error.startDateAfterGaac");
			}
			if(member.getLeaving()!=null && member.getLeaving() && member.getEndDate().after(new Date())){
				errors.rejectValue("endDate",
						"gaac.member.error.startDateAfterGaac");
				
			}

			
		}
	}

}
