package com.refresh.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.refresh.member.vo.MemberBenefit;
import com.refresh.member.vo.MemberVO;
import com.refresh.email.service.EmailService;
import com.refresh.member.dao.MemberDAO;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDAO memberDAO;
    private final EmailService emailService;
    
    // 회원가입
    @Override
    public void registerMember(MemberVO member) throws Exception {
        memberDAO.insertMember(member);
    }
    
    // 아이디 중복 확인
    @Override
    public boolean isIdDuplicate(String id) {
        int count = memberDAO.checkIdDuplicate(id);
        return count > 0;  // 중복된 이메일이 있으면 true, 없으면 false 반환
    }
    
    // 로그인
    @Override
    public boolean login(MemberVO member) {
        int count = memberDAO.checkLogin(member);
        return count == 1;
    }
    
    // 회원 이름 정보 표시를 위해 불러오기
    @Override
    public String getUserByName(String id) {
        return memberDAO.getUserByName(id);
    }
    
    // 이름과 메일 정보 확인 후 확인 이메일 발송
    @Autowired
    public MemberServiceImpl(MemberDAO memberDAO, EmailService emailService) {
        this.memberDAO = memberDAO;
        this.emailService = emailService;
    }

    @Override
    public boolean sendVerificationCodeIfUserExists(String name, String email) {
    	String memberId = memberDAO.findIdByEmailAndName(name, email);
        if (!memberId.isEmpty()) { // 사용자 정보가 일치하는 경우
            String verificationCode = emailService.generateVerificationCode();
            emailService.sendVerificationEmail(email, verificationCode);
            return true;
        }
        return false; // 사용자 정보가 일치하지 않음
    }
    
    @Override // 아이디 찾기
    public String findUserIdByNameAndEmail(String name, String email) {
        return memberDAO.findIdByEmailAndName(name, email); // DAO
    }
    
    @Override // 비밀번호 찾기
    public String findUserPasswdByNameAndEmail(String name, String email) {
        return memberDAO.findPasswdByEmailAndName(name, email); // DAO
    }
    
    @Override
    public MemberVO getUserById(String id) {
        return memberDAO.findById(id);
    }
    
    @Override
    public List<MemberBenefit> getBenefits(String memberId) {
        return memberDAO.getBenefitsByMemberId(memberId);
    }

    @Override
    public void deleteBenefits(String memberId, List<String> benefitDescriptions) {
        if (benefitDescriptions == null || benefitDescriptions.isEmpty()) return;

        memberDAO.deleteBenefits(memberId, benefitDescriptions);
    }
}