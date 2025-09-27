package com.refresh.member.dao;

import com.refresh.member.vo.MemberBenefit;
import com.refresh.member.vo.MemberVO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberDAO {
    void insertMember(MemberVO member) throws Exception; // 회원 가입
    int checkIdDuplicate(@Param("id") String id); // 아이디 중복 확인
    int checkLogin(MemberVO member); // 로그인
    String getUserByName(String id); // 로그인회원 이름 확인
    String findIdByEmailAndName(@Param("name") String name, @Param("email") String email); // 이름과 이메일로 회원 아이디 찾기
    String findPasswdByEmailAndName(@Param("name") String name, @Param("email") String email);
    
    MemberVO findById(String id);
    
    // 혜택적용
    List<MemberBenefit> getBenefitsByMemberId(String memberId);
    void deleteBenefits(@Param("memberId") String memberId,
            @Param("benefitDescriptions") List<String> benefitDescriptions);
    
    void subtractPoint(@Param("id") String id, @Param("usedPoint") int usedPoint);
}
