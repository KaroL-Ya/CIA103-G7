package com.mall.memOrder;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/memOrder")

public class MemOrderController {
	
	@Autowired
    private MemOrderService memOrderService;

	//訂單(全部)
	
	@GetMapping("/memOrder")
	public String getOneMemOrder(Model model) {
	  List<MemOrderVO> orders = memOrderService.getALl();
	  model.addAttribute("orders",orders); 
	  return "front-end/orders/memOrder"; 
	}
	
	//會員本人訂單
	@GetMapping("/memOrder/{memId}")
	public String getOneMemOrder(@PathVariable("memId") Integer memId, Model model) {
		List<MemOrderVO> orders = memOrderService.getOrdersByMemId(memId);
		model.addAttribute("orders",orders); 
		return "front-end/orders/memOrder"; 
	}
  
   
 // 訂單明細
 	@GetMapping("/memOrderDetails/{orderId}")
 	public String getOneOrder(@PathVariable("orderId") Integer orderId, Model model) {
 		 MemOrderVO memOrderVO = memOrderService.getOneOrder(orderId);
 		 model.addAttribute("memOrderVO", memOrderVO);
 		 //System.out.println(memOrderVO);
 		 
 		 model.addAttribute("details", memOrderService.findByOrderId(memOrderVO.getOrderId()));
 		 //  html--116行-- ${details}
 		 
 		 //System.out.println(memOrderService.findByOrderId(memOrderVO.getOrderId()));
 	
 		 return "front-end/orders/memOrderDetails";
 	}
}








