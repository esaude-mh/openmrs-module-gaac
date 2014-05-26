package org.openmrs.module.gaac.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.gaac.AffinityType;
import org.openmrs.module.gaac.GaacUtils;
import org.openmrs.module.gaac.api.GaacService;
import org.openmrs.module.gaac.validator.AffinityTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AffinityTypeController {

	protected final Log log = LogFactory.getLog(getClass());
	AffinityTypeValidator validator;

	@Autowired
	public AffinityTypeController(AffinityTypeValidator validator) {
		this.validator = validator;
	}

	@ModelAttribute("affinityType")
	AffinityType formBackingObject(
			@RequestParam(value = "affinityTypeId", required = false) Integer affinityTypeId) {
		if (affinityTypeId != null) {
			AffinityType af = GaacUtils.getService().getAffinityTypeById(
					affinityTypeId);

			return af;
		}
		AffinityType af = new AffinityType();

		return af;
	}

	@RequestMapping(value = { "/module/gaac/affinityTypeForm" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public void showAffinityTypeForm(Model model,
			@ModelAttribute("affinityType") AffinityType affinityType) {
	}

	@RequestMapping(value = { "/module/gaac/affinityTypeForm" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public ModelAndView processForm(
			@ModelAttribute("affinityType") AffinityType affinityType,
			BindingResult result, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		this.validator.validate(affinityType, result);
		if (result.hasErrors()) {
			return model;
		}

		if (Context.isAuthenticated()) {
			GaacService service = GaacUtils.getService();

			if (request.getParameter("save") != null) {
				service.saveAffinityType(affinityType);

				RedirectView rdV = new RedirectView(request.getContextPath()
						+ "/module/gaac/affinityTypeList.form");

				return new ModelAndView(rdV).addObject("openmrs_msg",
						"gaac.affinityType.saved");
			}
			if (request.getParameter("retire") != null) {
				String retireReason = request.getParameter("retireReason");
				if ((affinityType.getAffinityTypeId() != null)
						&& (!StringUtils.hasText(retireReason))) {
					result.rejectValue("retireReason", "retireReason",
							"gaac.affinityType.error.emptyretirereason");
					return model;
				}

				service.retireAffinityType(affinityType, retireReason);

				return new ModelAndView(new RedirectView(
						request.getContextPath()
								+ "/module/gaac/affinityTypeList.form"))
						.addObject("openmrs_msg", "gaac.affinityType.retired");
			}
			if (request.getParameter("purge") != null) {
				try {
					service.purgeAffinityType(affinityType);

					return new ModelAndView(new RedirectView(
							request.getContextPath()
									+ "/module/gaac/affinityTypeList.form"))
							.addObject("openmrs_msg",
									"gaac.affinityType.purged");
				} catch (DataIntegrityViolationException e) {
					model.addObject("openmrs_error",
							"error.object.inuse.cannot.purge");
				} catch (APIException e) {
					model.addObject("openmrs_error",
							"error.general: " + e.getLocalizedMessage());
				}

			}

		}

		return model;
	}

}
