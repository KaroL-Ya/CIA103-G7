package com.mall.pco.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mall.pco.model.PcoVO;
import com.mall.pco.model.PcoService;

import java.util.List;

@Controller
@RequestMapping("/pco")
public class PcoController {

    @Autowired
    private PcoService pcoSvc;

    @GetMapping("addPco")
    public String addPco(ModelMap model) {
        PcoVO pcoVO = new PcoVO();
        model.addAttribute("pcoVO", pcoVO);
        return "back-end/pco/addPco";
    }

    @PostMapping("insert")
    public String insert(@Valid PcoVO pcoVO, BindingResult result, ModelMap model) {
        // 檢查錯誤
        if (result.hasErrors()) {
            return "back-end/pco/addPco"; // 返回表單並顯示錯誤訊息
        }

        // 如果有特殊驗證邏輯，如 minSpend <= discount，則需要加強處理
        if (pcoVO.getMinSpend() <= pcoVO.getDiscount()) {
            result.rejectValue("minSpend", "error.minSpend", "最低消費金額需大於折扣金額");
            return "back-end/pco/addPco"; // 返回表單並顯示錯誤訊息
        }

        try {
            // 新增優惠券並自動分發
            pcoSvc.addPco(pcoVO);
            model.addAttribute("success", "- (新增成功)");
        } catch (Exception e) {
            // 處理可能的分發失敗
            model.addAttribute("errorMessage", "新增優惠券失敗: " + e.getMessage());
            return "back-end/pco/addPco";
        }
//        // 處理新增資料
//        pcoSvc.addPco(pcoVO);

        // 成功後的操作
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/pco/listAllPco";
    }

    @PostMapping("getOne_For_Update")
    public String getOneForUpdate(@RequestParam("pcoId") String pcoId, ModelMap model) {
        // 查詢資料
        PcoVO pcoVO = pcoSvc.getOnePco(Integer.valueOf(pcoId));

        model.addAttribute("pcoVO", pcoVO);
        return "back-end/pco/update_pco_input";
    }

    @PostMapping("update")
    public String update(@Valid PcoVO pcoVO, BindingResult result, ModelMap model) {
        // 檢查 minSpend 和 discount 關係
        if (pcoVO.getMinSpend() <= pcoVO.getDiscount()) {
            result.rejectValue("minSpend", "error.minSpend", "最低消費金額需大於折扣金額");
        }

        // 驗證錯誤
        if (result.hasErrors()) {
            return "back-end/pco/update_pco_input"; // 返回更新頁面並顯示錯誤訊息
        }

        // 處理更新資料
        pcoSvc.updatePco(pcoVO);

        // 成功後的操作
        model.addAttribute("success", "- (修改成功)");
        model.addAttribute("pcoVO", pcoSvc.getOnePco(pcoVO.getPcoId()));
        return "back-end/pco/listOnePco";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("pcoId") String pcoId, ModelMap model) {
        try {
            // 處理刪除
            pcoSvc.deletePco(Integer.valueOf(pcoId));
            model.addAttribute("success", "- (刪除成功)");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "刪除失敗: " + e.getMessage());
        }
        return "redirect:/pco/listAllPco";
    }

    @ModelAttribute("pcoListData")
    public List<PcoVO> referenceListData() {
        return pcoSvc.getAll();
    }
}
