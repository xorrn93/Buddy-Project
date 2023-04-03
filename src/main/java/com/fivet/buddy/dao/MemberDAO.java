package com.fivet.buddy.dao;

import com.fivet.buddy.dto.MemberDTO;
import com.fivet.buddy.dto.MemberImgDTO;
import com.fivet.buddy.mapper.MemberMapper;
import com.fivet.buddy.mapper.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberDAO {

    @Autowired
    private MemberMapper memberMapper;

    // 회원가입 (signUp)
    public void signUp(MemberDTO memberDto) throws Exception {
        memberMapper.signUp(memberDto);
    }

    // 회원가입 시 아이디(이메일) 중복체크
    public boolean idCheck(String id) throws Exception{
        return memberMapper.idCheck(id);
    }

    // 로그인 시 아이디 있는지 체크
    public boolean isAccountExist(MemberDTO memberDto) throws  Exception {
        return memberMapper.isAccountExist(memberDto);
    }

    // 로그인 시 정보 불러오기
    public MemberDTO selectAccountInfo(MemberDTO memberDto) throws Exception{
        return memberMapper.selectAccountInfo(memberDto);
    }

    // 로그인 시 정보 불러오기(네이버/카카오)
    public MemberDTO selectAccountInfoForNK(MemberDTO memberDto) throws Exception{
        return memberMapper.selectAccountInfoForNK(memberDto);
    }

    // 카카오 로그인 시 아이디 있는지 체크
    public boolean isKakaoExist(MemberDTO memberDto) throws Exception{
        return memberMapper.isKakaoExist(memberDto);
    }

    // 네이버 로그인 시 아이디 있는지 체크
    public boolean isNaverExist(MemberDTO memberDto) throws Exception{
        return memberMapper.isNaverExist(memberDto);
    }

    // 로그인 상태에서 로그인페이지 접근 차단
    public MemberDTO selectMyInfo(String memberSeq) throws Exception{
        return memberMapper.selectMyInfo(memberSeq);
    }

    //계정설정으로 이동
    public MemberDTO selectMyProfile(String memberSeq) throws Exception{
        return memberMapper.selectMyProfile(memberSeq);
    }

    //휴대전화 수정
    public void updatePhone(MemberDTO memberDto) throws Exception{
        memberMapper.updatePhone(memberDto);
    }

    //현재비밀번호 일치여부
    public int selectMyProfilePw(MemberDTO memberDto) throws Exception{
        return memberMapper.selectMyProfilePw(memberDto);
    }
    //비밀번호 수정
    public void updatePw(MemberDTO memberDto) throws Exception{
        memberMapper.updatePw(memberDto);
    }

    //프로필 이미지 출력
    public String selectProfileImg(String memberImgMemberSeq) throws Exception{
        return memberMapper.selectProfileImg(memberImgMemberSeq);
    }

    //회원가입 시 프로필 이미지 기본값으로 추가
    public void insertProfileImg(int memberSeq) throws Exception{
        memberMapper.insertProfileImg(memberSeq);
    }

    //프로필 이미지 업로드
    public void updateProfileImg(MemberImgDTO memberImgDto) throws Exception{
        memberMapper.updateProfileImg(memberImgDto);
    }

    //프로필 이미지 삭제
    public void updateDefaultProfileImg(String memberSeq) throws Exception{
        memberMapper.updateDefaultProfileImg(memberSeq);
    }

    //회원 탈퇴
    public void deleteMember(String memberSeq) throws Exception{
        memberMapper.deleteMember(memberSeq);
    }

    // 회원 목록 출력
    public List<MemberDTO> selectMembers() throws Exception{
        return memberMapper.selectMembers();
    }

    // 검색한 회원 출력
    public List<MemberDTO> memberSearch(String searchPick, String memberSearchText) throws Exception{
        return memberMapper.memberSearch(searchPick, memberSearchText);
    }


    // 회원 강퇴(관리자)
    public void memberKickOut(int memberSeq) throws Exception{
        memberMapper.memberKickOut(memberSeq);
    }
    // 폴더 소유자 이름
    public String getOwnerName(int checkOwner) {
        return memberMapper.getOwnerName(checkOwner);
    }

    // 회원 번호를 통해 이름과 이메일 추출
    public String getMemberId(int memberSeq) {
        return memberMapper.getMemberId(memberSeq);
    }
    // 임시비밀번호로  업데이트
    public void updateTempPW(String tempPW, String userEmail) throws Exception{
        memberMapper.updateTempPW(tempPW,userEmail);
    }
}