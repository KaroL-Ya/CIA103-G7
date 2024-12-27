package com.event.EveController;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.event.EveModel.EventService;
import com.event.EveModel.EventVO;
import com.mall.lookitem.LookItemVO;
import com.member.model.MemberVO;
import com.event.Participate.ParticipateService;

@Controller
	@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService esvc;
    
    @Autowired
    private ParticipateService psvc;

    // 1. 網頁-活動列表
    @GetMapping("/list")
    public String getAllEvents(Model model,HttpSession session) {
    	Integer memberId = (Integer) session.getAttribute("mem_Id");
    	model.addAttribute("events", esvc.getAllEvents());
        return "event/events"; 
    }

    // 2. 網頁-新增活動
    @GetMapping("/new")
    public String addEventForm(Model model, HttpSession session) {
    	//model.addAttribute(memID)
    	 Integer memberId = (Integer) session.getAttribute("mem_Id"); 
         if (memberId != null) {
        	 model.addAttribute("Debug1", new EventVO());
        	 return "event/newEvent"; // newEvent.html 
         	}else{
           return "redirect:/member/loginMem";
         	}
         
    }
    
    // 2. 功能-新增活動
 /*   @GetMapping("addParticipant")
    public String addParticipant(@RequestParam("event") Integer eveID, ModelMap model, HttpSession session) {
        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
        CafeVO loggedInCafe = (CafeVO) session.getAttribute("loggedInCafe");

        if (loggedInMember == null) {
            return "redirect:/member/loginMem";
        }

        if (loggedInCafe == null) { 
            return "redirect:/member/loginMem"; 
        }
*/
    //免圖片版本
    @PostMapping("/insert")
    public String addEvent(@ModelAttribute("Debug1") EventVO Debug1, Model model) {
    
    	esvc.addEvent(Debug1);
    	
    	
		List<EventVO> list = esvc.getAllEvents();
		model.addAttribute("Debug1", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/events/list"; // Redirect to event list
    }
    //圖片版本
//    @PostMapping("/insert")
//    public String addEvent(@ModelAttribute("Debug1") EventVO Debug1, BindingResult result, @RequestParam("eveImg") MultipartFile eveImg, Model model) {
//    	if (eveImg.isEmpty()) {
//    		model.addAttribute("ImgError", "請上傳圖片"); 
//    		return "item/addItem";
//    	} else {
//    		try {
//    			// 將圖片轉換為 byte[]
//    			Debug1.setEveImg(eveImg.getBytes());
//    		} catch (IOException e) {
//    			model.addAttribute("UpdateError", "圖片上傳失敗，請重新嘗試");
//    			return "event/new";
//    		}
//    	}
//    	esvc.addEvent(Debug1);
//    	
//    	
//    	List<EventVO> list = esvc.getAllEvents();
//    	model.addAttribute("Debug1", list);
//    	model.addAttribute("success", "- (新增成功)");
//    	return "events/list"; // Redirect to event list
//    }
    

    
    // 3. 網頁-活動編輯-從我的活動過去，通常不需要新建
    @GetMapping("/edit")
    public String editEventForm(ModelMap model) {
    	model.addAttribute("event", new EventVO());
    	return "event/newEvent";
    }
    
    
    // 更新活動

//    // View event details
//    @GetMapping("/{id}")
//    public String viewEvent(@PathVariable Integer id, Model model) {
//        esvc.getEventById(id).ifPresent(event -> model.addAttribute("event", event));
//        return "viewEvent"; // Render viewEvent.html template
//    }

    
    // 功能-刪除活動，網頁測試
    
	@PostMapping("delete")
	public String delete(@RequestParam("eveID") Integer eveID, ModelMap model) {
		esvc.deleteEvent(eveID);
		List<EventVO> list = esvc.getAllEvents();
		model.addAttribute("EventList", list);
		model.addAttribute("success", "- (刪除成功)");
		return "redirect:/events/list"; // 刪除完成後轉交其他
	}
	
    	
	
	// 詳細資訊
	@GetMapping("/ShowDetails/{eventID}")
	public String getOneItem(@PathVariable("eventID") Integer eventID, Model model, HttpSession session) {
      //報名判斷
		Integer memberId = (Integer) session.getAttribute("mem_Id"); 
         boolean repeat = psvc.RepeatRegister(memberId, eventID);
		EventVO eveThis = esvc.findById(eventID);
		model.addAttribute("eveThis", eveThis);
		model.addAttribute("repeat",repeat);
		return "event/EventDetails";
	}
	//在這邊
	//我創的活動
	@GetMapping("MyHost")
	public String MyEvent(HttpSession session, Model model) {
		
		 Integer memberId = (Integer) session.getAttribute("mem_Id"); 
         if (memberId == null) {
        	 model.addAttribute("Debug1", new EventVO());
        	 return "event/MyHost"; // newEvent.html 
         	}else{
           return "redirect:/member/loginMem";
         	}
	}
//	  @GetMapping("/myEvents/{memID}")
//	    public String getEventsForMember(HttpSession session, Integer memberId, Model model) {
//		  MemberVO loggedInMember = (MemberVO) session.getAttribute("mem_Id");
//	        if (loggedInMember == null) {
//	            return "redirect:/member/loginMem";
//	        }else {return null;};
//	        System.out.println(1);
//			return null;
//	    }
	//活動報名
	
	//不需要這個
//    @GetMapping("delete")
//    public String deleteEvent(@PathVariable Integer eveID) {
//        esvc.deleteEvent(eveID);
//        return "redirect:/events"; // Redirect to event list
//    }
    
    // 查詢活動
	// 查看自己參加的所有活動
//		@GetMapping("/MyPostedEvent")
//		public String myPostedEvent(HttpSession session, Model model) {
//			BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
//			if(businessMember==null||businessMember.getId()==null) {
//				return "redirect:/business_login";
//			}
//			String bMemberId = Integer.toString(businessMember.getId());
//			PageRequest pr=PageRequest.of(0, 10, Sort.by("id").descending());
//			Page<Event> eventPage = eventService.searchEvents(Map.of("businessId", bMemberId), pr);
//			model.addAttribute("page",eventPage);
//			return "event/event_myPostedEvent";
//		}
    
}