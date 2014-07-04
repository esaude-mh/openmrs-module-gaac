package org.openmrs.module.gaac.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.gaac.Gaac;
import org.openmrs.module.gaac.GaacMember;
import org.openmrs.module.gaac.GaacUtils;
import org.openmrs.module.gaac.ReasonLeavingGaacType;
import org.openmrs.module.gaac.api.GaacService;
import org.openmrs.module.gaac.propertyeditor.GaacEditor;
import org.openmrs.module.gaac.propertyeditor.ReasonLeavingTypeEditor;
import org.openmrs.module.gaac.validator.GaacMemberValidator;
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
public class GaacMemberController {

	private Log log = LogFactory.getLog(getClass());
	private GaacMemberValidator gaacMemberValidator;

	@Autowired
	public GaacMemberController(GaacMemberValidator validator) {
		this.gaacMemberValidator = validator;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(ReasonLeavingGaacType.class,
				new ReasonLeavingTypeEditor());

		binder.registerCustomEditor(Gaac.class, new GaacEditor());

		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				SimpleDateFormat.getDateInstance(3, Context.getLocale()), true));
	}

	@RequestMapping(value = { "/module/gaac/gaacMemberForm" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public void showGaacForm(Model model,
			@ModelAttribute("gaacMember") GaacMember gaacMember,
			@RequestParam(required = false, value = "gaacId") Gaac gaac) {
		GaacService gs = GaacUtils.getService();
		model.addAttribute("reasonLeavingTypes",
				gs.getAllReasonLeavingGaacType());
	}

	@RequestMapping(value = { "/module/gaac/gaacMemberForm" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public ModelAndView processForm(
			@ModelAttribute("gaacMember") GaacMember gaacMember,
			BindingResult result, SessionStatus status,
			HttpServletRequest request) {
		GaacService service = GaacUtils.getService();

		if (gaacMember.getGaac() == null) {
			gaacMember.setGaac(service.getGaac(Integer.valueOf(request
					.getParameter("gaacId"))));
		}
		this.gaacMemberValidator.validate(gaacMember, result);
		if (result.hasErrors()) {
			return new ModelAndView();
		}
		
		/*if (gaacMember.getLeaving() != null) {
			if (gaacMember.getLeaving().booleanValue()) {
				gaacMember.setRestart(Boolean.valueOf(false));
				gaacMember.setRestartDate(null);
			} else {
				gaacMember.setReasonLeaving(null);
				gaacMember.setEndDate(null);
			}
		}*/
		
		if ((gaacMember.getRestart() != null)
				&& (gaacMember.getRestart().booleanValue())) {
			gaacMember.setLeaving(Boolean.valueOf(false));
			gaacMember.setReasonLeaving(null);
			gaacMember.setEndDate(null);
			log.debug("RESTART IS TRUE AND RESTART DATE IS"+gaacMember.getRestartDate());
		}

		service.saveGaacMember(gaacMember);

		return new ModelAndView(new RedirectView(request.getContextPath()
				+ "/module/gaac/addNewGaac.form?gaacId="
				+ gaacMember.getGaac().getGaacId()));
	}

	@ModelAttribute("gaacMember")
	GaacMember formBackingObject(
			@RequestParam(value = "gaacMemberId", required = false) Integer gaacMemberId) {
		if (gaacMemberId != null) {
			GaacMember member = GaacUtils.getService().getGaacMember(
					gaacMemberId);

			return member;
		}
		GaacMember member = new GaacMember();

		return member;
	}

}
