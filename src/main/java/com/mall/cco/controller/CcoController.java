package com.mall.cco.controller;

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

import com.mall.cco.model.CcoVO;
import com.mall.cco.model.CcoService;

import java.util.List;

@Controller
@RequestMapping("/cco")
public class CcoController {

    @Autowired
    private CcoService ccoSvc;

    // 顯示新增 CCO 優惠券頁面
    @GetMapping("addCco")
    public String addCco(ModelMap model) {
        CcoVO ccoVO = new CcoVO();
        model.addAttribute("ccoVO", ccoVO);
        return "front-end/cco/addCco";  // 返回新增頁面
    }

    // 新增 CCO 優惠券
    @PostMapping("insert")
    public String insert(@Valid CcoVO ccoVO, BindingResult result, ModelMap model) {
        // 校验 minSpend > discount
        if (ccoVO.getMinSpend() < ccoVO.getDiscount()) {
            result.rejectValue("minSpend", "minSpend.error", "最低消費金額需大於折扣金額");
        }

        if (result.hasErrors()) {
            return "front-end/cco/addCco";  // 如果有錯誤，返回新增頁面
        }

        ccoSvc.addCco(ccoVO);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/cco/listAllCco";  // 重定向到所有 CCO 優惠券列表頁面
    }

    // 更新資料的 POST 請求
    @PostMapping("update")
    public String update(@Valid CcoVO ccoVO, BindingResult result, ModelMap model) {
        // 校验 minSpend > discount
        if (ccoVO.getMinSpend() < ccoVO.getDiscount()) {
            result.rejectValue("minSpend", "minSpend.error", "最低消費金額需大於折扣金額");
        }

        if (result.hasErrors()) {
            return "front-end/cco/update_cco_input";  // 如果有錯誤，返回修改頁面
        }

        ccoSvc.updateCco(ccoVO);  // 更新資料
        model.addAttribute("success", "- (修改成功)");  // 顯示成功訊息
        model.addAttribute("ccoVO", ccoSvc.getOneCco(ccoVO.getCcoId()));  // 再次查詢並返回更新後的資料
        return "front-end/cco/listOneCco";  // 顯示更新後的 CCO 優惠券頁面
    }
    
    // 顯示更新 CCO 優惠券頁面
    @PostMapping("getOne_For_Update")
    public String getOneForUpdate(@RequestParam("ccoId") String ccoId, ModelMap model) {
        // 根據 ccoId 查詢 CCO 優惠券資料
        CcoVO ccoVO = ccoSvc.getOneCco(Integer.valueOf(ccoId));
        
        // 把查詢到的 CCO 資料傳遞給更新頁面
        model.addAttribute("ccoVO", ccoVO);
        
        // 返回更新頁面
        return "front-end/cco/update_cco_input";
    }


    // 刪除 CCO 優惠券
    @PostMapping("delete")
    public String delete(@RequestParam("ccoId") String ccoId, ModelMap model) {
        try {
            ccoSvc.deleteCco(Integer.valueOf(ccoId));
            model.addAttribute("success", "- (刪除成功)");
        } catch (Exception e) {
            model.addAttribute("error", "刪除失敗: " + e.getMessage());
        }
        return "redirect:/cco/listAllCco";  // 重定向回 CCO 優惠券列表頁面
    }

    // 提供 CCO 優惠券列表數據給其他頁面使用
    @ModelAttribute("ccoListData")
    public List<CcoVO> referenceListData() {
        return ccoSvc.getAll();  // 提供所有 CCO 優惠券資料
    }
}