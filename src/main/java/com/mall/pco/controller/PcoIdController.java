package com.mall.pco.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import com.mall.pco.model.PcoVO;
import com.mall.pco.model.PcoService;

@Controller
@Validated
@RequestMapping("/pco")
public class PcoIdController {

    @Autowired
    PcoService pcoSvc;

    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        @NotEmpty(message = "優惠券編號: 請勿空白")  // 必填檢查
        @RequestParam("pcoId") String PCOID,
        ModelMap model) {

        // 根據 pcoId 查詢資料
        PcoVO pcoVO = pcoSvc.getOnePco(Integer.valueOf(PCOID));
        
        List<PcoVO> list = pcoSvc.getAll();
        model.addAttribute("pcoListData", list);

        if (pcoVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "back-end/pco/select_page";
        }

        model.addAttribute("pcoVO", pcoVO);
        model.addAttribute("getOne_For_Display", "true");
        return "back-end/pco/select_page";
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, ModelMap model) {
        // 收集所有錯誤訊息
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage()).append("<br>");
        }

        // 重新加載所有 pco 資料並顯示錯誤訊息
        List<PcoVO> list = pcoSvc.getAll();
        model.addAttribute("pcoListData", list);

        return new ModelAndView("back-end/pco/select_page", "errorMessage", "請修正以下錯誤:<br>" + strBuilder.toString());
    }
}
