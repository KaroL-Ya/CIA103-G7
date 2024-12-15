
package com.cafe.filter;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cafe.model.CafeService;
import com.cafe.model.CafeVO;

@Controller
public class CafeLoginController {

    @Autowired
    CafeService cafeSvc;
    
    @Autowired
    HttpSession session;

    @PostMapping("/cafe/cafe_Login")
    public String login(@RequestParam String ac, @RequestParam String pw,
                        HttpSession session, ModelMap model) {
        CafeVO cafeVO = cafeSvc.login(ac, pw);
        if (cafeVO != null) {
            // 登入成功
            session.setAttribute("cafeId", cafeVO.getCafeId());
            session.setAttribute("ac", ac);
            session.setAttribute("name", cafeVO.getName());
            session.setAttribute("email", cafeVO.getEmail());
            session.setAttribute("phone", cafeVO.getPhone());
            session.setAttribute("city", cafeVO.getCity());
            session.setAttribute("disc", cafeVO.getDisc());
            session.setAttribute("address", cafeVO.getAddress());
            session.setAttribute("state", cafeVO.getState());
            return "redirect:/cafe";
        } else {
            // 登入失敗
            model.addAttribute("error", "帳號或密碼錯誤");
            return "front-end/cafeLogin";  // 返回登入頁面
        }
    }
    // 柏志幫你家/cafe/cafe_
    @GetMapping("/cafe/cafe_logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 清除 Session
        return "redirect:/cafe/cafeLogin";
    }
}
