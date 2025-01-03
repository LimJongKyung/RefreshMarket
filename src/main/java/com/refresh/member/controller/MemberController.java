package com.refresh.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // @Controller로 변경
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;

import com.refresh.email.service.EmailServiceImpl;
import com.refresh.member.service.MemberService;
import com.refresh.member.vo.MemberVO;

import jakarta.servlet.http.HttpSession;

@Controller // RestController 대신 Controller 사용
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private EmailServiceImpl emailService; // EmailServiceImpl 주입
    
    // 회원가입 양식 호출
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("member", new MemberVO());
        return "refresh/signup/signup"; // signup.html 페이지를 반환
    }
    
    // 회원가입 구현
    @PostMapping("/register")
    @ResponseBody // JSON 응답을 위해 @ResponseBody 추가
    public String registerMember(@ModelAttribute MemberVO member) {
        try {
            memberService.registerMember(member);
            return "success"; // 성공 시 응답
        } catch (Exception e) {
            return "error"; // 오류 발생 시 응답
        }
    }
    
    // 이메일 중복 확인 API
    @GetMapping("/check-id")
    @ResponseBody
    public boolean checkId(@RequestParam("id") String id) {
        return memberService.isIdDuplicate(id);
    }
    
    // 로그인 페이지 반환
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("member", new MemberVO());
        return "refresh/login/login"; // signup.html 페이지를 반환
    }
    
    @PostMapping("/check")
    @ResponseBody
    public Map<String, String> checkLogin(@RequestParam String id, @RequestParam String passwd, HttpSession session) {
        // MemberVO 객체 생성 및 ID와 비밀번호 설정
        MemberVO member = new MemberVO();
        member.setId(id);
        member.setPasswd(passwd);

        // 로그인 시도
        boolean result = memberService.login(member);
        Map<String, String> response = new HashMap<>();

        if (result) {
            // 로그인 성공 시 세션에 사용자 ID 저장
            session.setAttribute("userId", id);
            // 성공 응답 구성
            response.put("status", "success");
            response.put("message", "로그인 성공!");
        } else {
            // 실패 응답 구성
            response.put("status", "fail");
            response.put("message", "아이디 또는 비밀번호가 잘못되었습니다.");
        }

        return response; // JSON 형식으로 반환
    }
    
    // 사용자 이름 불러오기
    @GetMapping("/home")
    public String forCheckingId(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
       
        String username = null;

        if (userId != null) {
            // 사용자 ID를 통해 사용자 이름 가져오기
            username = memberService.getUserByName(userId);
        }

        // 모델에 사용자 이름 추가
        model.addAttribute("username", username);
        return "refresh/mainlogin"; // 해당하는 HTML 파일 이름 반환 (예: home.html)
    }
    
    @GetMapping("/login/searchId")
    public String searchId() {
        return "refresh/login/searchId";
    }

    @PostMapping("/login/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(
            @RequestParam String name,
            @RequestParam String email) {
        boolean success = memberService.sendVerificationCodeIfUserExists(name, email);
        if (success) {
            return ResponseEntity.ok("인증번호가 " + email + "로 전송되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    
    // 아이디 찾기
    @PostMapping("/login/findId")
    public ResponseEntity<String> findId(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String code) {
        // 이메일로 발송된 코드를 검증
        if (emailService.validateVerificationCode(code)) {
            // ID 찾기
            String userId = memberService.findUserIdByNameAndEmail(name, email); // 이름과 이메일로 ID 찾기
            if (userId != null) {
                // ID를 이메일로 전송
                emailService.sendUserIdToEmail(email, userId);
                return ResponseEntity.ok("아이디가 " + email + "로 전송되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 코드가 일치하지 않습니다.");
        }
    }
    
    @GetMapping("/login/searchPasswd")
    public String searchPasswd() {
        return "refresh/login/searchPasswd";
    }
    
    // 비밀번호 찾기
    @PostMapping("/login/findPasswd")
    public ResponseEntity<String> findPasswd(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String code) {
        // 이메일로 발송된 코드를 검증
        if (emailService.validateVerificationCode(code)) {
            // ID 찾기
            String userId = memberService.findUserPasswdByNameAndEmail(name, email); // 이름과 이메일로 ID 찾기
            if (userId != null) {
                // ID를 이메일로 전송
                emailService.sendUserPasswdToEmail(email, userId);
                return ResponseEntity.ok("비밀번호가 " + email + "로 전송되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 코드가 일치하지 않습니다.");
        }
    }
 
}
