package com.fivet.buddy.services;

import com.fivet.buddy.dao.MemberDAO;
import com.fivet.buddy.dto.MemberDTO;
import com.fivet.buddy.dto.MemberImgDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberDAO memberDao;

    // 회원가입 (signUp)
    public void signUp(MemberDTO memberDto) throws Exception{
        memberDto.setMemberPw(getSHA512(memberDto.getMemberPw()));
        memberDao.signUp(memberDto);
    }

    // 회원가입 시 아이디(이메일) 중복체크
    public boolean idCheck(String id) throws Exception{
        return memberDao.idCheck(id);
    }

    // 로그인 시 아이디 있는지 체크
    public boolean isAccountExist(MemberDTO memberDto) throws Exception{
        memberDto.setMemberPw(getSHA512(memberDto.getMemberPw()));
        return memberDao.isAccountExist(memberDto);
    }

    // 로그인 시 정보 불러오기
    public MemberDTO selectAccountInfo(MemberDTO memberDto) throws Exception{
        return memberDao.selectAccountInfo(memberDto);
    }

    // 로그인 시 정보 불러오기(네이버/카카오)
    public MemberDTO selectAccountInfoForNK(MemberDTO memberDto) throws Exception{
        return memberDao.selectAccountInfoForNK(memberDto);
    }

    // 카카오 로그인 시 아이디 있는지 체크
    public boolean isKakaoExist(MemberDTO memberDto) throws Exception{
        return memberDao.isKakaoExist(memberDto);
    }

    // 네이버 로그인 시 아이디 있는지 체크
    public boolean isNaverExist(MemberDTO memberDto) throws Exception{
        return memberDao.isNaverExist(memberDto);
    }

    // 로그인 상태에서 로그인페이지 접근 차단
    public MemberDTO selectMyInfo(String memberSeq) throws Exception{
        return memberDao.selectMyInfo(memberSeq);
    }

    //계정설정으로 이동
    public MemberDTO selectMyProfile(String memberSeq) throws Exception{
        return memberDao.selectMyProfile(memberSeq);
    }

    //휴대전화 수정
    public void updatePhone(MemberDTO memberDto) throws Exception{
        memberDao.updatePhone(memberDto);
    }

    //현재비밀번호 일치여부
    public boolean selectMyProfilePw(MemberDTO memberDto) throws Exception{
        memberDto.setMemberPw(getSHA512(memberDto.getMemberPw()));
        if(memberDao.selectMyProfilePw(memberDto)==1){
            return true;
        }else{
            return false;
        }
    }

    //비밀번호 수정
    public void updatePw(MemberDTO memberDto) throws Exception{
        memberDto.setMemberPw(getSHA512(memberDto.getMemberPw()));
        memberDao.updatePw(memberDto);
    }

    //프로필 이미지 출력
    public String selectProfileImg(String memberImgMemberSeq) throws Exception{
        return memberDao.selectProfileImg(memberImgMemberSeq);
    }

    //회원가입 시 프로필 이미지 기본값으로 추가
    public void insertProfileImg(int memberSeq) throws Exception{
        memberDao.insertProfileImg(memberSeq);
    }

    //프로필 이미지 업로드
    public void updateProfileImg(MemberImgDTO memberImgDto) throws Exception{
        memberDao.updateProfileImg(memberImgDto);
    }

    //프로필 이미지 삭제
    public void updateDefaultProfileImg(String memberSeq) throws Exception{
        memberDao.updateDefaultProfileImg(memberSeq);
    }
    //회원 탈퇴
    public void deleteMember(String memberSeq) throws Exception{
        memberDao.deleteMember(memberSeq);
    }
    // 회원 목록 출력
    public List<MemberDTO> selectMembers() throws Exception{
        return memberDao.selectMembers();
    }

    // 검색한 회원 출력
    public List<MemberDTO> memberSearch(String searchPick, String memberSearchText) throws Exception{
        return memberDao.memberSearch(searchPick, memberSearchText);
    }

    // 회원 강퇴(관리자)
    public void memberKickOut(int memberSeq) throws Exception{
        memberDao.memberKickOut(memberSeq);
    }

    // getSHA512
    public static String getSHA512(String input) throws Exception{
        String toReturn = null;
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(input.getBytes("utf8"));
        toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        return toReturn;
    }
    // 폴더 소유자 이름
    public String getOwnerName(int checkOwner) {
        return memberDao.getOwnerName(checkOwner);
    }

    // 회원 번호를 통해 이름과 이메일 추출
    public String getMemberId(int memberSeq) {
        return memberDao.getMemberId(memberSeq);
    }

    // 임시비밀번호 로 업데이트
    public void updateTempPW(String tempPW, String userEmail) throws Exception{
        memberDao.updateTempPW(getSHA512(tempPW),userEmail);
    }


}
