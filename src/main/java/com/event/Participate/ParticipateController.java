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
import com.member.model.*;
import com.member.filter.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/participate")
public class ParticipateController {

    @Autowired
    ParticipateService psvc;
    
    @Autowired
    EventService esvc;
    
//    //報名頁面
//    @GetMapping("Register/{id}")
//    public String RegistraterForm(@PathVariable Integer eveID, Model model, HttpSession session) {
//     
//    	EventVO event = esvc.findById(eveID);
//    	MemberVO memID =  (MemberVO) session.getAttribute("mem_Id");
////    	if (memID == null) {
////    		//redirect to login
////    	}
//    	
//        model.addAttribute("event", event);
//        model.addAttribute("memVO", memID);
//        return "register_form"; // HTML form view name
//    }
    //報名頁面
    @GetMapping("/join_test1")
    // actNo EveId
    public String addParticipant(@RequestParam("t13") Integer eveID, ModelMap model, HttpSession session) {
        MemberVO loggedInMember = (MemberVO) session.getAttribute("mem_Id");
        if (loggedInMember == null) {
            return "redirect:/member/loginMem";
        }

        ParticipateVO PartVO = new ParticipateVO();
        EventVO eventVO = esvc.findById(eveID);
        PartVO.setEveID(eventVO);
        PartVO.setMemID(loggedInMember);

        model.addAttribute("participateVO", PartVO);
        model.addAttribute("eveName", eventVO.getEveName());
        model.addAttribute("memName", loggedInMember.getMem_Id());
        return "participate/join_test1";
    }

    
    
    @PostMapping("enroll")
    public String insert(@Valid ParticipateVO participateVO, BindingResult result, ModelMap model,
                         MultipartFile[] parts, HttpSession session) throws IOException {
        if (result.hasErrors()) {
            return "participate/addparticipate";
        }

        psvc.addParticipant(participateVO);

        model.addAttribute("participantListData", psvc.getall());
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/participate/listMyAllParticipant";
    }
    
  
}
