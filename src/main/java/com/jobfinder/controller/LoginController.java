package com.jobfinder.controller;

import com.jobfinder.domain.LoginVO;
import com.jobfinder.domain.Login_ComVO;
import com.jobfinder.domain.Recruit;
import com.jobfinder.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/all_delete_data")
    public int all_delete_data(HttpSession session, Model model){
        System.out.println("회원탈퇴진행!");
        int res = 0;
        String type = (String) session.getAttribute("type");
        if (type == "P"){
            LoginVO vo = (LoginVO)session.getAttribute("VO");
            String mem_id = vo.getMem_id();
            res = loginService.all_delete_data_per(mem_id);
        } else if(type == "C"){
            Login_ComVO cvo = (Login_ComVO)session.getAttribute("VO");
            String company_id = cvo.getCompany_id();
            res = loginService.all_delete_data_com(company_id);
        }
        return res;
    }









    @RequestMapping(value = "/Com_gonggo_list")
    public String Com_gonggo_list(HttpSession session, Model model){

        Login_ComVO cvo = (Login_ComVO) session.getAttribute("VO");
        List<Recruit> rec_list = loginService.rec_list(cvo.getCompany_id());
        model.addAttribute("rec_list", rec_list);
        return "Com_gonggo_list";
    }

    @RequestMapping(value = "/Com_emp_list")
    public String Com_emp_list(HttpSession session, Model model){

        /*Login_ComVO cvo = (Login_ComVO) session.getAttribute("VO");
        List<resume> resume_list = loginService.resume_list(cvo.getCompany_id());
        model.addAttribute("resume_list", resume_list);*/
        return "Com_emp_list";
    }








    @RequestMapping(value = "/ump")
    public String ump(Model model, LoginVO loginVO){
        model.addAttribute("loginVO", loginVO);
        return "update_mypage";
    }
    @RequestMapping(value = "/umpc")
    public String umpc(Model model, Login_ComVO login_comVO){
        model.addAttribute("login_comVO", login_comVO);
        return "update_mypage_com";
    }

    @RequestMapping(value = "/update_my_page")
    public String update_my_page(@ModelAttribute("umpform_per") LoginVO vo, HttpSession session){
        System.out.println("session : " + session.getAttribute("VO"));
        System.out.println("update : " + vo);

        LoginVO sessionVO = (LoginVO) session.getAttribute("VO");

        if (vo.getMem_pw() == "") {
            vo.setMem_pw(sessionVO.getMem_pw());
        }
        if (vo.getMem_phone() == "") {
            vo.setMem_phone(sessionVO.getMem_phone());
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(vo.getMem_phone());
            if (sb.substring(0,1) == "02"){
                sb.insert(2,"-");
            } else {
                sb.insert(3,"-");
            }
            sb.insert(vo.getMem_phone().length()-3,"-");
            vo.setMem_phone(sb.toString());
        }
        if (vo.getMem_email() == "") {
            vo.setMem_email(sessionVO.getMem_email());
        }
        if (vo.getMem_addr() == "") {
            vo.setMem_addr(sessionVO.getMem_addr());
        }

        loginService.update_vo(vo);

        return "redirect:/loginform";
    }
    @RequestMapping(value = "/update_my_page_com")
    public String update_my_page_com(@ModelAttribute("umpform_com") Login_ComVO cvo, HttpSession session){
        System.out.println("session : " + session.getAttribute("VO"));
        System.out.println("update : " + cvo);

        Login_ComVO sessionVO = (Login_ComVO) session.getAttribute("VO");

        if (cvo.getCompany_pw() == "") {
            cvo.setCompany_pw(sessionVO.getCompany_pw());
        }
        if (cvo.getCompany_phone() == "") {
            cvo.setCompany_phone(sessionVO.getCompany_pw());
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(cvo.getCompany_phone());
            if (sb.substring(0,1) == "02"){
                sb.insert(2,"-");
            } else {
                sb.insert(3,"-");
            }
            sb.insert(cvo.getCompany_phone().length()-3,"-");
            cvo.setCompany_phone(sb.toString());
        }
        if (cvo.getCompany_email() == "") {
            cvo.setCompany_email(sessionVO.getCompany_email());
        }
        if (cvo.getCompany_addrcompany() == "") {
            cvo.setCompany_addrcompany(sessionVO.getCompany_addrcompany());
        }

        loginService.update_cvo(cvo);

        return "redirect:/loginform";
    }







    @RequestMapping(value = "/loginform")
    public String loginform() {
        return "loginform";
    }

    @RequestMapping(value = "/login_per")
    public String login_per(@RequestParam(value = "mem_id", required=false) String id, @RequestParam(value = "mem_pw", required=false) String pw, HttpSession session){
        System.out.println("개인로그인시도");
        System.out.println(id);
        System.out.println(pw);
        LoginVO vo = new LoginVO();
        vo.setMem_id(id);
        vo.setMem_pw(pw);
        LoginVO rvo = loginService.login_per(vo);
        session.setAttribute("type","P");
        session.setAttribute("VO", rvo);
        System.out.println("개인로그인완료");
        return "redirect:/";
    };

    @RequestMapping(value = "/login_com")
    public String login_com(@ModelAttribute("loginform_com") Login_ComVO cvo, HttpSession session){
        System.out.println("기업로그인시도");
        System.out.println(cvo);
        Login_ComVO rcvo = loginService.login_com(cvo);
        session.setAttribute("type","C");
        session.setAttribute("VO", rcvo);
        System.out.println("기업로그인완료");
        return "redirect:/";
    };

    @RequestMapping(value = "/signup_local")
    public String signup_local(Model model, LoginVO loginVO) {
        model.addAttribute("LoginVO", loginVO);
        return "signup_local";
    }

    @RequestMapping(value = "/signup_company")
    public String signup_company(Model model, Login_ComVO login_comVO) {
        model.addAttribute("Login_ComVO",login_comVO);
        return "signup_company";
    }

    @RequestMapping(value = "/signup_com")
    public String signup_com(@ModelAttribute Login_ComVO cvo) {
        System.out.println("signup_com");
        System.out.println(cvo);

        StringBuffer sb = new StringBuffer();
        sb.append(cvo.getCompany_phone());
        if (sb.substring(0,1) == "02"){
            sb.insert(2,"-");
        } else {
            sb.insert(3,"-");
        }
        sb.insert(cvo.getCompany_phone().length()-3,"-");
        cvo.setCompany_phone(sb.toString());

        int res = loginService.set_signup_com(cvo);
        System.out.println(res);
        return "redirect:/";
    };

    @RequestMapping(value = "/signup_per")
    public String signup_per(@ModelAttribute LoginVO vo) {
        System.out.println("signup_per");
        System.out.println(vo);

        StringBuffer sb = new StringBuffer();
        sb.append(vo.getMem_phone());
        if (sb.substring(0,1) == "02"){
            sb.insert(2,"-");
        } else {
            sb.insert(3,"-");
        }
        sb.insert(vo.getMem_phone().length()-3,"-");
        vo.setMem_phone(sb.toString());

        int res = loginService.set_signup_per(vo);
        System.out.println(res);
        return "redirect:/";
    };

    @RequestMapping(value = "/get_loginVO")
    @ResponseBody
    public String get_loginVO(@ModelAttribute("loginform_per") LoginVO vo) {

        System.out.println("per 넘어오는 vo값 : " + vo);
        LoginVO rvo = loginService.login_per(vo);
        System.out.println("per 받는 vo값 : " + rvo);
        String res = "";
        if (rvo == null){ res = "";}
        else { res = "exist";}
        return res;
    }

    @RequestMapping(value = "/get_login_ComVO")
    @ResponseBody
    public String get_login_ComVO(@ModelAttribute("loginform_com") Login_ComVO cvo) {

        System.out.println("com 넘어오는 cvo값 : " + cvo);
        Login_ComVO rcvo = loginService.login_com(cvo);
        System.out.println("com 받는 cvo값 : " + rcvo);
        String res = "";
        if (rcvo == null){ res = "";}
        else { res = "exist";}
        return res;
    }


    @RequestMapping(value = "/login_id_check_per")
    @ResponseBody
    public String login_id_check_per(@RequestParam("insert_id") String insert_id) {

        System.out.println("per 넘어오는 id값 : " + insert_id);
        String mem_id = loginService.id_check_per(insert_id);
        System.out.println("per 받는 id값 : " + mem_id);
        return mem_id;
    }

    @RequestMapping(value = "/login_id_check_com")
    @ResponseBody
    public String login_id_check_com(@RequestParam("insert_id") String insert_id) {

        System.out.println("com 넘어오는 id값 : " + insert_id);
        String com_id = loginService.id_check_com(insert_id);
        System.out.println("com 받는 id값 : " + com_id);
        return com_id;
    }

    @RequestMapping(value = "/kakaologin")
    public String login(@RequestParam(value = "code", required = false) String code, HttpSession session, HttpServletResponse response) throws Exception{

        System.out.println("loginController!!");

        System.out.println("####code#####" + code);

        String access_Token = loginService.getAccessToken(code);
        System.out.println("###access_Token#### : " + access_Token);

        Cookie token = new Cookie("authorize-access-token", access_Token);
        token.setPath("/");
        response.addCookie(token);

        //*
        HashMap<String, String> userInfo = loginService.getUserInfo(access_Token);
        System.out.println("###user_info### : " + userInfo);

        String mem_id = userInfo.get("id");
        String mem_pw = userInfo.get("id");
        String mem_name = userInfo.get("nickname");
        String mem_email = userInfo.get("email");

        String check = loginService.id_check_per(mem_id);

        if ( check == null ){
            System.out.println(mem_id);
            System.out.println(mem_name);
            System.out.println(mem_email);

            LoginVO vo = new LoginVO();
            vo.setMem_id(mem_id);
            vo.setMem_pw(mem_pw);
            vo.setMem_name(mem_name);
            vo.setMem_email(mem_email);
            vo.setMem_phone("");
            vo.setM_agreement1("Y");
            vo.setM_agreement2("Y");
            vo.setM_agreement3("");
            vo.setM_agreement4("");
            vo.setM_agreement5("");
            vo.setM_agreement6("3");

            int res = loginService.set_signup_kko(vo);

            LoginVO rvo = loginService.login_per(vo);

            session.setAttribute("type","P");
            session.setAttribute("VO", rvo);

            return "redirect:/loginform";
        } else {
            LoginVO vo = new LoginVO();
            vo.setMem_id(mem_id);
            vo.setMem_pw(mem_pw);

            LoginVO rvo = loginService.login_per(vo);

            session.setAttribute("type","P");
            session.setAttribute("VO", rvo);
            return "redirect:/loginform";
        }
    }

    @RequestMapping(value = "/v1/user/unlink")
    public String reset(HttpServletResponse response, HttpSession session) throws Exception {

        System.out.println("Reset code");

        Cookie token = new Cookie("authorize-access-token", null);
        token.setPath("/");
        token.setMaxAge(0);
        response.addCookie(token);

        session.removeAttribute("type");
        session.removeAttribute("VO");

        return "loginform";
    }
}
