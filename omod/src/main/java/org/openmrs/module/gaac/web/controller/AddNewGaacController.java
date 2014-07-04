package org.openmrs.module.gaac.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.gaac.AffinityType;
import org.openmrs.module.gaac.Gaac;
import org.openmrs.module.gaac.GaacMember;
import org.openmrs.module.gaac.GaacUtils;
import org.openmrs.module.gaac.api.GaacService;
import org.openmrs.module.gaac.propertyeditor.AffinityTypeEditor;
import org.openmrs.module.gaac.validator.GaacValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AddNewGaacController {

	private Log log = LogFactory.getLog(getClass());
	private GaacValidator gaacValidator;

	@Autowired
	public AddNewGaacController(GaacValidator validator) {
		this.gaacValidator = validator;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(AffinityType.class,
				new AffinityTypeEditor());
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				SimpleDateFormat.getDateInstance(3, Context.getLocale()), true));
	}

	@RequestMapping(value = { "/module/gaac/addNewGaac" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public void showAffinityTypeForm(Model model,
			@ModelAttribute("gaac") Gaac gaac) {
		model.addAttribute("affinityTypes", GaacUtils.getService()
				.getAllAffinityType());
	}

	@ModelAttribute("gaac")
	Gaac formBackingObject(
			@RequestParam(value = "gaacId", required = false) Integer gaacId) {
		if (gaacId != null) {
			Gaac gaac = GaacUtils.getService().getGaac(gaacId);

			return gaac;
		}
		Gaac gaac = new Gaac();

		return gaac;
	}

	@RequestMapping(value = { "/module/gaac/addNewGaac" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public ModelAndView processForm(@ModelAttribute("gaac") Gaac gaac,
			BindingResult result, SessionStatus status,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView();

		this.gaacValidator.validate(gaac, result);
		if (result.hasErrors()) {
			return model;
		}
		GaacService service = GaacUtils.getService();

		if ((gaac.getGaacId() == null) && (gaac.getFocalPatient() != null)) {
			GaacMember member = new GaacMember();
			member.setGaac(gaac);
			member.setStartDate(gaac.getStartDate());
			member.setMember(gaac.getFocalPatient());
			member.setDescription(gaac.getDescription());
			if (gaac.getMembers() == null)
				gaac.setMembers(new HashSet<GaacMember>());
			gaac.getMembers().add(member);
		}

		// Desintegrar

		if ((gaac.getCrumbled() != null) && (gaac.getCrumbled().booleanValue())) {
			for (GaacMember member : gaac.getMembers()) {
				member.setLeaving(Boolean.valueOf(true));
				member.setReasonLeaving(service
						.getReasonLeavingGaacType(Integer.valueOf(5)));
				member.setEndDate(gaac.getDateCrumbled());
			}
			gaac.setEndDate(gaac.getDateCrumbled());

		}

		// Reintegar

		if (gaac.getCrumbled() != null && (!gaac.getCrumbled().booleanValue())) {
			gaac.setEndDate(null);
			gaac.setReasonCrumbled(null);
			gaac.setDateCrumbled(null);
			for (GaacMember member : gaac.getMembers()) {
				if (member.getReasonLeaving().getId() == 5) {
					member.setLeaving(false);
					member.setReasonLeaving(null);
					member.setEndDate(null);
				}
			}
		}

		// Anular
		if ((gaac.getVoided() != null) && (gaac.getVoided().booleanValue())) {
			for (GaacMember member : gaac.getMembers()) {
				member.setVoided(true);
				member.setVoidedBy(gaac.getVoidedBy());
				member.setVoidReason(gaac.getVoidReason());
				member.setDateVoided(gaac.getDateVoided());
			}
		}

		// Desanular
		if ((gaac.getVoided() != null) && (!gaac.getVoided().booleanValue())) {
			for (GaacMember member : gaac.getMembers()) {
				member.setVoided(false);
				member.setVoidedBy(null);
				member.setVoidReason(null);
				member.setDateVoided(null);
			}
		}

		service.saveGaac(gaac);

		return new ModelAndView(new RedirectView(request.getContextPath()
				+ "/module/gaac/gaacList.form")).addObject("openmrs_msg",
				"gaac.saved");
	}

}
