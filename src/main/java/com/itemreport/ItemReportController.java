package com.itemreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.Valid;

@Controller
@RequestMapping("/itemReport")
public class ItemReportController {
	


    @Autowired
    private ItemReportService itemReportService;

    // 顯示檢舉表單
    @GetMapping("/formReport/{itemId}")
    public String showReportForm(@PathVariable("itemId") Integer itemId, Model model) {
        ItemReportVO itemReportVO = new ItemReportVO();
        itemReportVO.setItemId(itemId);
        model.addAttribute("itemReportVO", itemReportVO);
        return "front-end/lookitem/formReport";  // 檢舉表單頁面
    }
    
    // 顯示全部檢舉列表
    @GetMapping("/itemReport")
	public String getOneItemReport(Model model) {
	  List<ItemReportVO> reports = itemReportService.getAll();
	  model.addAttribute("reports",reports);
	  return "front-end/lookitem/itemReport"; 
	}
    ///////////////
    @PostMapping("/itemReport/{itemId}")
    public String submitReport(@PathVariable("itemId") Integer itemId,
                               @ModelAttribute ItemReportVO itemReportVO,
                               @RequestParam(value = "file", required = false) MultipartFile file,
                               Model model) {
    	System.out.println(itemId);
        itemReportVO.setItemId(itemId);

       
        itemReportService.reportItem(itemReportVO);
        return "redirect:/lookitem/lookItem";  // 根据需求重定向或返回到检举列表页面
    }


//    // 提交檢舉表單.
//    @PostMapping("/itemReport/{itemId}")
//    public String submitReport(@PathVariable("itemId") Integer itemId, Model model) {
//    	ItemReportVO itemReportVO = new ItemReportVO();
//        itemReportVO.setItemId(itemId);
//        model.addAttribute("itemReportVO", itemReportVO);
//        return "front-end/lookitem/lookItem";  
//    }
    
    
//    @PostMapping("/itemReport/{itemId}")
//    public String submitReport(@PathVariable("itemId") Integer itemId, 
//                               @ModelAttribute ItemReportVO itemReportVO, Model model) {
//    	
//        itemReportService.reportItem(itemReportVO); 
//        
//        return "front-end/lookitem/lookItem";  
//    }



    

}
