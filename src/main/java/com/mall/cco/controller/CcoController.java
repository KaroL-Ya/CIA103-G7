//package com.mall.cco.controller;
//
//import javax.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.mall.cco.model.CcoVO;
//import com.mall.cco.model.CcoService;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/cco")
//public class CcoController {
//
//    @Autowired
//    private CcoService ccoSvc;
//
//    // 顯示新增 CCO 優惠券頁面
//    @GetMapping("addCco")
//    public String addCco(ModelMap model) {
//        CcoVO ccoVO = new CcoVO();
//        model.addAttribute("ccoVO", ccoVO);
//        return "front-end/cco/addCco";  // 返回新增頁面
//    }
//
//    // 新增 CCO 優惠券
//    @PostMapping("insert")
//    public String insert(@Valid CcoVO ccoVO, BindingResult result, ModelMap model) {
//        // 校验 minSpend > discount
//        if (ccoVO.getMinSpend() < ccoVO.getDiscount()) {
//            result.rejectValue("minSpend", "minSpend.error", "最低消費金額需大於折扣金額");
//        }
//
//        if (result.hasErrors()) {
//            return "front-end/cco/addCco";  // 如果有錯誤，返回新增頁面
//        }
//
//        ccoSvc.addCco(ccoVO);
//        model.addAttribute("success", "- (新增成功)");
//        return "redirect:/cco/listAllCco";  // 重定向到所有 CCO 優惠券列表頁面
//    }
//
//    // 更新資料的 POST 請求
//    @PostMapping("update")
//    public String update(@Valid CcoVO ccoVO, BindingResult result, ModelMap model) {
//        // 校验 minSpend > discount
//        if (ccoVO.getMinSpend() < ccoVO.getDiscount()) {
//            result.rejectValue("minSpend", "minSpend.error", "最低消費金額需大於折扣金額");
//        }
//
//        if (result.hasErrors()) {
//            return "front-end/cco/update_cco_input";  // 如果有錯誤，返回修改頁面
//        }
//
//        ccoSvc.updateCco(ccoVO);  // 更新資料
//        model.addAttribute("success", "- (修改成功)");  // 顯示成功訊息
//        model.addAttribute("ccoVO", ccoSvc.getOneCco(ccoVO.getCcoId()));  // 再次查詢並返回更新後的資料
//        return "front-end/cco/listOneCco";  // 顯示更新後的 CCO 優惠券頁面
//    }
//    
//    // 顯示更新 CCO 優惠券頁面
//    @PostMapping("getOne_For_Update")
//    public String getOneForUpdate(@RequestParam("ccoId") String ccoId, ModelMap model) {
//        // 根據 ccoId 查詢 CCO 優惠券資料
//        CcoVO ccoVO = ccoSvc.getOneCco(Integer.valueOf(ccoId));
//        
//        // 把查詢到的 CCO 資料傳遞給更新頁面
//        model.addAttribute("ccoVO", ccoVO);
//        
//        // 返回更新頁面
//        return "front-end/cco/update_cco_input";
//    }
//
//
//    // 刪除 CCO 優惠券
//    @PostMapping("delete")
//    public String delete(@RequestParam("ccoId") String ccoId, ModelMap model) {
//        try {
//            ccoSvc.deleteCco(Integer.valueOf(ccoId));
//            model.addAttribute("success", "- (刪除成功)");
//        } catch (Exception e) {
//            model.addAttribute("error", "刪除失敗: " + e.getMessage());
//        }
//        return "redirect:/cco/listAllCco";  // 重定向回 CCO 優惠券列表頁面
//    }
//
//    // 提供 CCO 優惠券列表數據給其他頁面使用
//    @ModelAttribute("ccoListData")
//    public List<CcoVO> referenceListData() {
//        return ccoSvc.getAll();  // 提供所有 CCO 優惠券資料
//    }
//}
package com.mall.cco.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private HttpSession session;

    // 顯示優惠券管理主頁 (select_page)
    @GetMapping("select_page")
    public String selectPage(ModelMap model) {
        // 從 Session 中獲取當前登入的商家 ID
        Integer cafeId = (Integer) session.getAttribute("cafeId");
        if (cafeId == null) {
            return "redirect:/cafe/cafeLogin"; // 未登入，重定向到登入頁面
        }

        // 根據 cafeId 獲取該商家的所有優惠券
        List<CcoVO> ccoList = ccoSvc.findAllByCafeId(cafeId);
        model.addAttribute("ccoListData", ccoList);
        return "front-end/cco/select_page"; // 返回商家優惠券選擇頁面
    }

    // 顯示新增 CCO 優惠券頁面
    @GetMapping("addCco")
    public String addCco(ModelMap model) {
        Integer cafeId = (Integer) session.getAttribute("cafeId");
        if (cafeId == null) {
            return "redirect:/cafe/cafeLogin"; // 未登入，重定向到登入頁面
        }

        CcoVO ccoVO = new CcoVO();
        ccoVO.setCafeId(cafeId); // 預先設置商家 ID
        model.addAttribute("ccoVO", ccoVO);
        model.addAttribute("cafeId", cafeId); // 傳遞商家 ID 給頁面
        return "front-end/cco/addCco"; // 返回新增頁面
    }

    // 新增 CCO 優惠券
    @PostMapping("insert")
    public String insert(@Valid CcoVO ccoVO, BindingResult result, ModelMap model) {
        Integer cafeId = (Integer) session.getAttribute("cafeId");
        if (cafeId == null) {
            return "redirect:/cafe/cafeLogin"; // 未登入，重定向到登入頁面
        }

        ccoVO.setCafeId(cafeId); // 綁定當前商家
        if (ccoVO.getMinSpend() < ccoVO.getDiscount()) {
            result.rejectValue("minSpend", "minSpend.error", "最低消費金額需大於折扣金額");
        }

        if (result.hasErrors()) {
            return "front-end/cco/addCco"; // 如果有錯誤，返回新增頁面
        }

        ccoSvc.addCco(ccoVO);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/cco/listAllCco"; // 重定向到所有 CCO 優惠券列表頁面
    }

    // 更新資料的 POST 請求
    @PostMapping("update")
    public String update(@Valid CcoVO ccoVO, BindingResult result, ModelMap model) {
        Integer cafeId = (Integer) session.getAttribute("cafeId");
        if (cafeId == null || !ccoVO.getCafeId().equals(cafeId)) {
            return "redirect:/cafe/cafeLogin"; // 未登入或無權限
        }

        if (ccoVO.getMinSpend() < ccoVO.getDiscount()) {
            result.rejectValue("minSpend", "minSpend.error", "最低消費金額需大於折扣金額");
        }

        if (result.hasErrors()) {
            return "front-end/cco/update_cco_input"; // 如果有錯誤，返回修改頁面
        }

        ccoSvc.updateCco(ccoVO);
        model.addAttribute("success", "- (修改成功)");
        model.addAttribute("ccoVO", ccoSvc.getOneCco(ccoVO.getCcoId()));
        return "front-end/cco/listOneCco"; // 顯示更新後的 CCO 優惠券頁面
    }

    // 顯示更新 CCO 優惠券頁面
    @PostMapping("getOne_For_Update")
    public String getOneForUpdate(@RequestParam("ccoId") Integer ccoId, ModelMap model) {
        Integer cafeId = (Integer) session.getAttribute("cafeId");
        if (cafeId == null) {
            return "redirect:/cafe/cafeLogin"; // 未登入，重定向到登入頁面
        }

        CcoVO ccoVO = ccoSvc.getOneCco(ccoId);
        if (ccoVO == null || !ccoVO.getCafeId().equals(cafeId)) {
            model.addAttribute("errorMessage", "查無資料或無權限");
            return "front-end/cco/select_page";
        }

        model.addAttribute("ccoVO", ccoVO);
        return "front-end/cco/update_cco_input";
    }

    // 刪除 CCO 優惠券
    @PostMapping("delete")
    public String delete(@RequestParam("ccoId") Integer ccoId, ModelMap model) {
        Integer cafeId = (Integer) session.getAttribute("cafeId");
        if (cafeId == null) {
            return "redirect:/cafe/cafeLogin"; // 未登入，重定向到登入頁面
        }

        CcoVO ccoVO = ccoSvc.getOneCco(ccoId);
        if (ccoVO == null || !ccoVO.getCafeId().equals(cafeId)) {
            model.addAttribute("errorMessage", "刪除失敗，無權限");
            return "redirect:/cco/listAllCco";
        }

        ccoSvc.deleteCco(ccoId);
        model.addAttribute("success", "- (刪除成功)");
        return "redirect:/cco/listAllCco";
    }

    // 查詢當前商家的所有優惠券
    @GetMapping("listAllCco")
    public String listAllCco(ModelMap model) {
        Integer cafeId = (Integer) session.getAttribute("cafeId");
        if (cafeId == null) {
            return "redirect:/cafe/cafeLogin"; // 未登入，重定向到登入頁面
        }

        List<CcoVO> list = ccoSvc.findAllByCafeId(cafeId);
        model.addAttribute("ccoListData", list);
        return "front-end/cco/listAllCco"; // 顯示優惠券列表頁面
    }
}
