package com.event.Participate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.event.EveModel.*;
import com.event.EveController.*;
import com.event.Participate.*;
import com.member.model.*;
import com.member.filter.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/participate")
public class ParticipateControllerBackUp {

    @Autowired
    ParticipateService psvc;
    
    @Autowired
    EventService esvc;
    
    //報名頁面--也不是這個
    @GetMapping("Renroll/{id}")
    public String RegistraterForm(@PathVariable Integer eveID, Model model, HttpSession session) {
     
    	EventVO event = esvc.findById(eveID);
    	MemberVO memID =  (MemberVO) session.getAttribute("mem_Id");
//    	if (memID == null) {
//    		//redirect to login
//    	}
    	
        model.addAttribute("event", event);
        model.addAttribute("memVO", memID);
        return "register_form"; // HTML form view name
    }
    
    
    //報名頁面--不是這個
    @GetMapping("/join")
    // actNo EveId
    public String addParticipant(@RequestParam("eveID") Integer eveID, ModelMap model, HttpSession session) {
        MemberVO loggedInMember = (MemberVO) session.getAttribute("mem_Id");
        if (loggedInMember == null) {
            return "redirect:/member/loginMem";
        }

        ParticipateVO PartVO = new ParticipateVO();
        EventVO eventVO = esvc.findById(eveID);
        PartVO.setEveID(eventVO);
//        PartVO.setMemID(loggedInMember);

        model.addAttribute("participateVO", PartVO);
        model.addAttribute("eveName", eventVO.getEveName());
//        model.addAttribute("memName", loggedInMember.getMem_Id());
        return "participate/join_test1";
    }
    
  //活動詳情報名method
    @PostMapping("/enroll/{eventID}")
    public String registerForEvent(@PathVariable("eventID") Integer eventID,HttpSession session) {
    	return psvc.addParticipant(eventID,session);
    	
        }
    //報名後我的活動
    @GetMapping("/myEvents/{memID}")
    public String getEventsForMember(@PathVariable("memID") Integer memberId, Model model) {
        List<EventVO> events = esvc.getEventsForMember(memberId);
        model.addAttribute("events", events);
        return "event/MyEve";
    }
   //活動詳情取消報名method 
    @PostMapping("/cancel/{eventID}")
    public String retreatParticipant(@PathVariable("eventID") Integer eventID,HttpSession session) {
    	return psvc.retreatParticipant(eventID,session);
        }
    
    //取消報名
//    @PostMapping("cancel")
//    public String cancelRegistration( @RequestParam("eventId") Integer eventId,HttpSession session, Model model) {
//    	Integer memId = (Integer)session.getAttribute("mem_Id");
//        psvc.cancel(memId, eventId);
//        
////        List<EventVO> eventList = esvc.getAllEvents();
////        
////        model.addAttribute("EventList", eventList);
////        model.addAttribute("success", "- (報名取消成功)");
//
//        return "redirect:/event/Myeve"; 
//    }
//    @DeleteMapping("/cancel")
//    public ResponseEntity<String> cancelRegistration(@RequestParam Integer memId, @RequestParam Integer eventId) {
//        try {
//            participateService.cancelRegistration(memId, eventId);
//            return ResponseEntity.ok("Registration canceled successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel registration.");
//        }
//    }

    
    
    //傳值 eventID
//    @PostMapping("/enroll")
//    public String enrollEvent(@RequestParam("eveID") Integer eveID, HttpSession session, RedirectAttributes redirectAttributes) {
//         Add EveID to PartVO
//        String redirectUrl = psvc.addParticipant(eveID, session);
//		ParticipateVO participateVO = new ParticipateVO();
//    	Integer EveID=eveVO.getEveID();
//    	List<ParticipateVO> list = psvc.addParticipant(EveID);
//    	psvc.saveParticipate(participateVO);
//		redirectAttributes.addFlashAttribute("message", "報名成功");
//        return redirectUrl ;
//    }
    
    //直接用attribute EventVO 物件
//    @PostMapping("/enroll1")
//    public String enrollEvent1(@ModelAttribute("eveThis") EventVO eveVO,Model model) {
//    	
//    	
//    	ParticipateVO participateVO = new ParticipateVO();
//    	Integer EveID=eveVO.getEveID();
//    	List<ParticipateVO> list = psvc.addParticipant(EveID);
//    	psvc.saveParticipate(participateVO);
//    	psvc.addParticipant2(eveVO);
//    	
//    	List<EventVO> list = esvc.getAllEvents();
//		model.addAttribute("Debug1", list);
//		model.addAttribute("success", "- (新增成功)");
//        return "redirect:/event/list";
//    }
    
    
//    @PostMapping("enroll")
//    public String insert(@Valid ParticipateVO participateVO, BindingResult result, ModelMap model,
//                         MultipartFile[] parts, HttpSession session) throws IOException {
//        if (result.hasErrors()) {
//            return "participate/addparticipate";
//        }
//
//        psvc.addParticipant1(participateVO);
//
//        model.addAttribute("participantListData", psvc.getall());
//        model.addAttribute("success", "- (新增成功)");
//        return "redirect:/participate/listMyAllParticipant";
//    }
    
  
}
