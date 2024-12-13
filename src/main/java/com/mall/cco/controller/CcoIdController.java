package com.mall.cco.controller;

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
import com.mall.cco.model.CcoVO;
import com.mall.cco.model.CcoService;

@Controller
@Validated
@RequestMapping("/cco")
public class CcoIdController {

    @Autowired
    private CcoService ccoSvc;

    // 查詢單一優惠券資料
    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        @NotEmpty(message = "優惠券編號: 請勿空白")
        @RequestParam("ccoId") String CCOID,
        ModelMap model) {

        // 根據自增主鍵（CCOID）查詢優惠券資料
        CcoVO ccoVO = ccoSvc.getOneCco(Integer.valueOf(CCOID));
        
        List<CcoVO> list = ccoSvc.getAll();
        model.addAttribute("ccoListData", list);

        if (ccoVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "front-end/cco/select_page";
        }

        model.addAttribute("ccoVO", ccoVO);
        model.addAttribute("getOne_For_Display", "true");
        return "front-end/cco/select_page";
    }

    // 全局處理輸入驗證錯誤
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, ModelMap model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "<br>");
        }

        List<CcoVO> list = ccoSvc.getAll();
        model.addAttribute("ccoListData", list);
        String message = strBuilder.toString();

        return new ModelAndView("front-end/cco/select_page", "errorMessage", "請修正以下錯誤:<br>" + message);
    }
}