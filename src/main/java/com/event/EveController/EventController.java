package com.event.EveController;

import java.io.IOException;
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

@Controller
	@RequestMapping("/events")
public class EventController {
/*需要功能:各網頁、活動插入
已完成:全部活動，確認CRUD可用
活動列表放首頁，創建活動放在不同會員or商家的個人頁面，直接從session取會員or商家ID
不用特別做商家or會員分割，不同名稱function和參數即可
活動編輯和刪除用網頁分，直接登入-中心頁-我的活動去編輯，用登入後的session抓
活動參加只准一般會員，商家不用做
活動創建也要求登入，登入後導向不同頁面?不可。登入後還是用session存商家or會員
還是需要做活動創建的不同implement
session放在mem_id
我的活動要從會員ID找活動
*/
    @Autowired
    private EventService esvc;

    // 1. 網頁-活動列表
    @GetMapping("/list")
    public String getAllEvents(Model model) {
        model.addAttribute("events", esvc.getAllEvents());
        return "event/events"; 
    }

    // 2. 網頁-新增活動
    @GetMapping("/new")
    public String addEventForm(Model model) {
    	//model.addAttribute(memID)
        model.addAttribute("Debug1", new EventVO());
        return "event/newEvent"; // newEvent.html 
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
    @PostMapping("/insert")
    public String addEvent(@ModelAttribute("Debug1") EventVO Debug1, BindingResult result, @RequestParam("eveImg") MultipartFile eveImg, Model model) {
    	if (eveImg.isEmpty()) {
			model.addAttribute("coverImgError", "請上傳封面圖片"); // 自定義錯誤訊息
			return "item/addItem";
		} else {
			try {
				// 將圖片轉換為 byte[]
				Debug1.setEveImg(eveImg.getBytes());
			} catch (IOException e) {
				model.addAttribute("ImgError", "圖片上傳失敗，請重新嘗試");
				return "events/new";
			}
		}
    	esvc.addEvent(Debug1);
		//*************************** 3.新增完成,準備轉交(Send the Success view) **************
		List<EventVO> list = esvc.getAllEvents();
		model.addAttribute("Debug1", list);
		model.addAttribute("success", "- (新增成功)");
        return "redirect:/events/list"; // Redirect to event list
    }
    

    
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
	
/*	// 活動細節
	@PostMapping("details")
    public String getDetails(@RequestParam("eveID") String actNo, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
        
        ActVO actVO = actSvc.getOneAct(Integer.valueOf(actNo));
         
        //如果活動存在，則通過getDetailsActPic方法根據活動編號 actNo 載入該活動的所有圖片
        if (actVO != null) {
            List<ActPictureVO> pictures = actPictureSvc.getDetailsActPic(Integer.valueOf(actNo));
            actVO.setActPictures(pictures);
        }
        //添加包含圖片的活動跟留言到model
        model.addAttribute("actVO", actVO);
        model.addAttribute("messageVO", new MessageVO());
        return "front-end/act/listOneAct";
    }
    */
	//不需要這個
//    @GetMapping("delete")
//    public String deleteEvent(@PathVariable Integer eveID) {
//        esvc.deleteEvent(eveID);
//        return "redirect:/events"; // Redirect to event list
//    }
    
    // 查詢活動
	// 查看自己發布的所有活動
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