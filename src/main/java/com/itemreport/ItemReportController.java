package com.itemreport;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/itemReport")
public class ItemReportController {

    @Autowired
    private ItemReportService itemReportService;

    // 顯示檢舉表單
    @GetMapping("/itemReport/{itemId}")
    public String showReportForm(@PathVariable("itemId") Integer itemId, Model model) {
        ItemReportVO itemReportVO = new ItemReportVO();
        itemReportVO.setItemId(itemId);
        model.addAttribute("itemReportVO", itemReportVO);
        return "front-end/lookitem/formReport";  // 返回檢舉表單頁面
    }
    
    // 顯示全部檢舉列表
    @GetMapping("/itemReport")
	public String getOneItemReport(Model model) {
	  List<ItemReportVO> reports = itemReportService.getAll();
	  model.addAttribute("reports",reports); 
	  return "front-end/lookitem/itemReport"; 
	}
    
//    // 提交檢舉表單.
    @PostMapping("/itemReport")
    public String postReportForm(@ModelAttribute ItemReportVO itemReportVO) {
        itemReportService.reportItem(itemReportVO);  
        return "front-end/lookitem/oneItem";  
    }
    

    

}
