package com.refresh.member.service;

import java.util.List;

import com.refresh.member.vo.MemberBenefit;
import com.refresh.member.vo.MemberVO;

public interface MemberService {
    void registerMember(MemberVO member) throws Exception; // 회원가입
    boolean isIdDuplicate(String id); // 아이디 중복확인
    boolean login(MemberVO member); // 로그인
    String getUserByName(String id); // 로그인 회원 이름 확인
    boolean sendVerificationCodeIfUserExists(String name, String email);
    String findUserIdByNameAndEmail(String name, String email);
    String findUserPasswdByNameAndEmail(String name, String email);
    
    MemberVO getUserById(String id);
    
    // 멤버 혜택
    List<MemberBenefit> getBenefits(String memberId);
    void deleteBenefits(String memberId, List<String> benefitDescriptions);
    
    void usePoint(String customerId, int usedPoint);
}