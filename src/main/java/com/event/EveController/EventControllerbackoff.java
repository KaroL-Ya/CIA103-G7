package com.event.EveController;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.event.EveModel.EventService;
import com.event.EveModel.EventVO;
import com.event.EveModel.EventRepository;

//待新增:hibernate util 查詢、會員相關
@Controller
@RequestMapping("/event")
public class EventControllerbackoff {
	// application properties prefix fixed to event
//
//	@Autowired
//	EventService esvc;
	
	@Autowired
    private EventRepository eventRepository;
//	@Autowired
//	會員功能 

	@Autowired
	private SessionFactory sessionFactory;

//	@GetMapping("/greeting")
//	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
//		model.addAttribute("name", name);
//		return "test";
//	}
	/*
	 * create event
	 */
	@GetMapping("/list")
	public String listEvents(ModelMap model) {
        model.addAttribute("events", eventRepository.findAll());
		
		return "MyEvent";
	}
	/*
	@PostMapping("create")
	
	
	@PostMapping("updateActByMem")
	public String getOne_For_Update(@RequestParam("eve_ID") String eve_ID, ModelMap model) {
		//*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************
		//*************************** 2.開始查詢資料 *****************************************
		// EmpService empSvc = new EmpService();
		EventVO eveVO = esvc.getOneEvent(Integer.valueOf(eve_ID));

		//*************************** 3.查詢完成,準備轉交(Send the Success view) **************
		model.addAttribute("eveVO", eveVO);
		return "front-end/act/updateActByMem"; // 查詢完成後轉交update_emp_input.html
	}
	public BindingResult removeFieldError(EventVO eveVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(eveVO, "eveVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	*/
}