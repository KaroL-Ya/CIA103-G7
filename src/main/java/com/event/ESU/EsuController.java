package com.event.ESU;


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
@RequestMapping("/Esu")
public class EsuController {

    @Autowired
    private EsuService esuService;  
    @Autowired
    private MemberService memberService; 
    @Autowired
    private EventService eventService;  

    @PostMapping("/enroll")
    public String registerForEvent(@RequestParam("eventId") Integer eventId,
                                   @RequestParam("memberId") Integer memberId,
                                   HttpSession session, Model model) {
        
        //獲取資料
        MemberVO member = memberService.getOneMember(memberId);
        EventVO event = eventService.findById(eventId);

        EsuVO esuVO = new EsuVO();
        esuVO.setEveID(event);   
        esuVO.setMemID(member);  
        
        //報名
        EsuVO savedEsuVO = esuService.saveEsuVO(esuVO); 
        //人數
        eventService.attendEvent(eventService.findById(eventId));
        
        model.addAttribute("savedEsuVO", savedEsuVO);
        
        model.addAttribute("event", event);
        model.addAttribute("member", member);

        return "event/registrationSuccess";  // Redirect to a success page
    }

    // Add more controller methods if needed for handling other requests
}

