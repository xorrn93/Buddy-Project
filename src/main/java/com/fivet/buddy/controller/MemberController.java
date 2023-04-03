package com.fivet.buddy.controller;

import com.fivet.buddy.dto.*;
import com.fivet.buddy.services.*;
import com.fivet.buddy.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;



@Controller
@RequestMapping("/member/")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private CalService calService;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private HttpSession session;

    @Autowired
    private BasicFolderService basicFolderService;

    @Autowired
    private PersonalFolderService personalFolderService;

    @Autowired
    private PersonalFileService personalFileService;

    @Autowired
    private ChatMsgService chatMsgService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMemberService chatMemberService;

    private Logger logger = LoggerFactory.getLogger(MemberController.class);

    // 회원 indexPage 경로

    private String memberIndex = "redirect:/member/loginIndex";

    // ExceptionHandler
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e) {
        e.printStackTrace();
        return "error";
    }

    // 회원가입 페이지 이동
    @Transactional
    @RequestMapping("toSignUp")
    public String toSignUp(MemberDTO memberDto,Model model) throws Exception{
        // 세션확인
        if(session.getAttribute("memberSeq")!=null){
            model.addAttribute("memberSeq",session.getAttribute("memberSeq"));
            return "member/duplLogin";
        }

        // 일반회원인지 카카오 / 네이버 회원인지 검토
        if(memberDto.getMemberLogtype() == null){
            memberDto.setMemberLogtype("normal");
            model.addAttribute("userInfo",memberDto);
        }else{
            model.addAttribute("userInfo",memberDto);
        }
        return "member/signup";
    }

    // 비밀번호 찾기

    @RequestMapping("toFindPW")
    public String toFindPw() throws Exception {
        return "member/findPW";
    }

    // 회원가입 (signUp)
    @Transactional
    @RequestMapping("signUp")
    public String signUp(MemberDTO memberDto) throws Exception{
        memberService.signUp(memberDto);
        // 회원 가입 시 기본 폴더 생성
        basicFolderService.newBasicFolder(memberDto);
        // 회원 가입 시 개인 폴더 생성
        personalFolderService.newPersonalFolder(memberDto);
        // 회원 가입시 member_img 테이블에 컬럼 추가
        memberService.insertProfileImg(memberDto.getMemberSeq());
        return "redirect:/";
    }

    // 회원가입 아이디 체크
    @ResponseBody
    @RequestMapping(value="idCheck",produces = "text/html;charset=utf8")
    public String idCheck(String id ) throws Exception{
        boolean result = memberService.idCheck(id);
        return String.valueOf(result);
    }

    // 로그인
    @Transactional
    @PostMapping("login")
    public String login(MemberDTO memberDto, Model model) throws Exception{
        // 세션확인
        if(session.getAttribute("memberSeq")!=null){
            model.addAttribute("memberSeq",session.getAttribute("memberSeq"));
            return "member/duplLogin";
        }

        boolean result = memberService.isAccountExist(memberDto);
        if(result){
            // 로그인 정보가 있으면 마이페이지
            MemberDTO dto = memberService.selectAccountInfo(memberDto);
            // 세션에 멤버 seq,logtype 뿌림
            session.setAttribute("memberSeq",dto.getMemberSeq());
            session.setAttribute("memberLogtype",dto.getMemberLogtype());
            session.setAttribute("memberName",dto.getMemberName());
            //model.addAttribute("userInfo",dto); model에 넣을 필요 없어보여서 주석처리.
            return memberIndex;
        }else{
            // 로그인 정보가 없으면 새로고침
            return "redirect:/";
        }
    }

    // 로그아웃
    @RequestMapping("logout")
    public String logout(String memberLogtype) throws Exception{
        session.invalidate();
        return "redirect:/";
    }

    // 카카오 로그인
    @Transactional
    @RequestMapping("kakaoLogin")
    public String kakaoLogin(MemberDTO memberDto,Model model) throws Exception{
        // 세션확인
        if(session.getAttribute("memberSeq")!=null){
            model.addAttribute("memberSeq",session.getAttribute("memberSeq"));
            return "member/duplLogin";
        }

        boolean result = memberService.isKakaoExist(memberDto);
        if(!result){
            // 회원이 아닐 때 회원가입 페이지
            model.addAttribute("userInfo",memberDto);
            return "member/signup";
        }else{
            // 회원일 때 마이페이지
            MemberDTO dto = memberService.selectAccountInfoForNK(memberDto);
            // 세션에 멤버 seq,logtype 뿌림
            session.setAttribute("memberSeq",dto.getMemberSeq());
            session.setAttribute("memberLogtype",dto.getMemberLogtype());
            session.setAttribute("memberName",dto.getMemberName());
            model.addAttribute("userInfo",dto);
            return memberIndex;
        }
    }

    // 네이버 로그인
    @Transactional
    @RequestMapping("naverLogin")
    public String naverLogin(MemberDTO memberDto,Model model) throws Exception{
        // 세션확인
        if(session.getAttribute("memberSeq")!=null){
            model.addAttribute("memberSeq",session.getAttribute("memberSeq"));
            return "member/duplLogin";
        }

        boolean result = memberService.isNaverExist(memberDto);
        if(!result){
            // 회원이 아닐 때 회원가입 페이지
            String naverPhone = memberDto.getMemberPhone();
            String replacePhone = naverPhone.replaceAll("-","");
            memberDto.setMemberPhone(replacePhone);

            model.addAttribute("userInfo",memberDto);
            return "member/signup";
        }else{
            // 회원일 때 마이페이지
            MemberDTO dto = memberService.selectAccountInfoForNK(memberDto);
            // 세션에 멤버 seq,logtype 뿌림
            session.setAttribute("memberSeq",dto.getMemberSeq());
            session.setAttribute("memberLogtype",dto.getMemberLogtype());
            session.setAttribute("memberName",dto.getMemberName());
            model.addAttribute("userInfo",dto);
            return memberIndex;
        }
    }

    // 에러페이지 >> 마이페이지 이동
    @Transactional
    @RequestMapping("goMypage")
    public String selectMyInfo(String memberSeq,Model model) throws Exception{
        MemberDTO dto = memberService.selectMyInfo(memberSeq);
        model.addAttribute("userInfo",dto);
        List <TeamDTO> teamDtoList = teamService.selectMemberTeam(dto.getMemberSeq());

        //부매니저인 멤버 출력 (부매니저일때도 팀 관리 들어갈 수 있게)
        int SubManagerMember = teamMemberService.selectSubManagerMember((Integer) session.getAttribute("memberSeq"));
        if(SubManagerMember>0){
            model.addAttribute("SubManagerMember",SubManagerMember);
        }
        //프로필 이미지 출력
        String ifSysName = memberService.selectProfileImg(String.valueOf(session.getAttribute("memberSeq")));
        if(ifSysName!=null) {
            if (ifSysName.equals("/static/img/defaultProfileImg.png")) {
                ifSysName=ifSysName.replaceAll("\\s", "");
                model.addAttribute("memberImg", ifSysName);
            } else {
                String elseSysName = "/member/selectProfileImg/" + ifSysName;
                model.addAttribute("memberImg", elseSysName);
            }
        }
        model.addAttribute("teamDtoList", teamDtoList);
        return memberIndex;
    }

    //계정설정으로 이동
    @Transactional
    @RequestMapping("goMyProfile")
    public String selectMyProfile(Model model) throws Exception {
        MemberDTO memberDto = memberService.selectMyProfile(String.valueOf(session.getAttribute("memberSeq")));
        model.addAttribute("myProfile",memberDto);
        return "member/myProfile";
    }

    //휴대전화 수정
    @Transactional
    @RequestMapping ("updatePhone")
    public String updatePhone(MemberDTO memberDto,Model model) throws Exception{
        memberDto.setMemberSeq((Integer) session.getAttribute("memberSeq"));
        memberService.updatePhone(memberDto);
        return "redirect:/member/goMyProfile";
    }

    //현재 비밀번호 일치여부
    @ResponseBody
    @RequestMapping(value="myProfileCheckPw", produces = "text/html;charset=utf8")
    public String selectMyProfilePw(MemberDTO memberDto) throws Exception{
        memberDto.setMemberSeq((Integer) session.getAttribute("memberSeq"));
        boolean result = memberService.selectMyProfilePw(memberDto);
        return String.valueOf(result);
    }

    //비밀번호 수정
    @RequestMapping ("updatePw")
    public String updatePw(MemberDTO memberDto) throws Exception{
        memberDto.setMemberSeq((Integer) session.getAttribute("memberSeq"));
        memberService.updatePw(memberDto);
        return "redirect:/member/goMyProfile";
    }

    @Value("${profile.save.path}")
    String proFilePath;

    //프로필 이미지 출력
    @Transactional
    @ResponseBody
    @RequestMapping("selectProfileImg")
    public String selectProfileImg() throws Exception{
        String ifSysName = memberService.selectProfileImg(String.valueOf(session.getAttribute("memberSeq")));
        if(ifSysName==null) {
            return null;
        }
        if(ifSysName.equals("/static/img/defaultProfileImg.png")){
            return ifSysName;
        }else{
            String elseSysName = "/member/selectProfileImg/"+ifSysName;
            elseSysName.replaceAll("\\s", "");
            return elseSysName;
        }
    }


    //프로필 이미지 수정
    @Transactional
    @ResponseBody
    @RequestMapping("updateProfileImg")
    public void updateProfileImg(MemberImgDTO memberImgDto, MultipartFile file, FileUtil util) throws Exception{
        if(!memberService.selectProfileImg(String.valueOf(session.getAttribute("memberSeq"))).equals("/static/img/defaultProfileImg.png")){
            memberImgDto.setMemberImgSysName(memberService.selectProfileImg(String.valueOf(session.getAttribute("memberSeq"))));
//            util.delete(proFilePath,memberImgDto.getMemberImgSysName());

            String memberImgOriName = file.getOriginalFilename();
            String memberImgSysName = UUID.randomUUID() + "_" + memberImgOriName;
            //UUID.randomUUID() : 현재 시간과 자체 매커니즘을 통해 겹치지 않는 기다란 문자를 자동으로 생성해줌
            util.save(file,proFilePath,memberImgSysName);

            memberImgDto.setMemberImgMemberSeq((Integer) session.getAttribute("memberSeq"));
            memberImgDto.setMemberImgOriName(memberImgOriName);
            memberImgDto.setMemberImgSysName(memberImgSysName);
            memberService.updateProfileImg(memberImgDto);
        }else{
            String memberImgOriName = file.getOriginalFilename();
            String memberImgSysName = UUID.randomUUID() + "_" + memberImgOriName;
            memberImgSysName=memberImgSysName.replaceAll("\\s", "");
            //UUID.randomUUID() : 현재 시간과 자체 매커니즘을 통해 겹치지 않는 기다란 문자를 자동으로 생성해줌
            util.save(file,proFilePath,memberImgSysName);

            memberImgDto.setMemberImgMemberSeq((Integer) session.getAttribute("memberSeq"));
            memberImgDto.setMemberImgOriName(memberImgOriName);
            memberImgDto.setMemberImgSysName(memberImgSysName);
            memberService.updateProfileImg(memberImgDto);
        }
    }

    //프로필 이미지 삭제
    @ResponseBody
    @RequestMapping("defaultProfileImg")
    public void updateDefaultProfileImg(MemberImgDTO memberImgDto, FileUtil util) throws Exception{
        memberImgDto.setMemberImgSysName(memberService.selectProfileImg(String.valueOf(session.getAttribute("memberSeq"))));
//        util.delete(proFilePath,memberImgDto.getMemberImgSysName());
        memberService.updateDefaultProfileImg(String.valueOf(session.getAttribute("memberSeq")));
    }

    //회원 탈퇴
    @Transactional
    @RequestMapping("deleteMember")
    public String deleteMember() throws Exception{
        int memberSeq = (int) session.getAttribute("memberSeq");
        // 파일 삭제
        personalFileService.memberOut(memberSeq);
        // 기본 폴더 삭제
        basicFolderService.memberOut(memberSeq);
        // 폴더 삭제
        personalFolderService.memberOut(memberSeq);
        // 캘린더 삭제
        calService.deleteUserEvent(memberSeq);


        // 회원 탈퇴(강퇴포함)시 삭제할 팀 목록 출력
        List<TeamMemberDTO> teamMemberList = teamMemberService.selectMembersManager(memberSeq);

        // 팀 탈퇴시 채팅방 삭제 로직을 탈퇴대상 팀으로 반복문 돌려준다.
        for (TeamMemberDTO teamMemberdto : teamMemberList) {
            chatRoomService.teamSelfOut(teamMemberdto);
        }
        memberService.deleteMember(String.valueOf(session.getAttribute("memberSeq")));

        session.invalidate();
        return "redirect:/";
    }

    // 로그인시 회원이 가입한 팀 목록 출력을 위한 통로
    @Transactional
    @RequestMapping("loginIndex")
    public String loginIndex(Model model) throws Exception {
        if (session.getAttribute("memberSeq")!=null) {
            List <TeamDTO> teamDtoList = teamService.selectMemberTeam((int)session.getAttribute("memberSeq"));
            //부매니저인 멤버 출력 (부매니저일때도 팀 관리 들어갈 수 있게)
            int SubManagerMember = teamMemberService.selectSubManagerMember((Integer) session.getAttribute("memberSeq"));
            if(SubManagerMember>0){
                model.addAttribute("SubManagerMember",SubManagerMember);
            }

            //프로필 이미지 출력
            String ifSysName = memberService.selectProfileImg(String.valueOf(session.getAttribute("memberSeq")));
            if(ifSysName!=null) {
                if (ifSysName.equals("/static/img/defaultProfileImg.png")) {
                    ifSysName.replaceAll("\\s", "");
                    model.addAttribute("memberImg", ifSysName);
                } else {
                    String elseSysName = "/member/selectProfileImg/" + ifSysName;
                    model.addAttribute("memberImg", elseSysName);
                }
            }
            model.addAttribute("teamDtoList", teamDtoList);
            model.addAttribute("teamCount", teamMemberService.selectMemberTeam((int)session.getAttribute("memberSeq")));
        }
        return "index";
    }

    // 관리자 페이지(회원관리)로 이동
    @RequestMapping("toAdminMember")
    public String toAdminPage(Model model) throws Exception {
        if (session.getAttribute("memberLogtype").equals("admin")) {
            List<MemberDTO> list = memberService.selectMembers();
            model.addAttribute("memberList", list);
            return "admin/adminMember";
        }
        return "error";
    }

    // 회원 검색(관리자)
    @RequestMapping("memberSearch")
    public String memberSearch(String searchPick, String memberSearchText, Model model) throws Exception {
        List<MemberDTO> list = memberService.memberSearch(searchPick, memberSearchText);
        model.addAttribute("memberList", list);
        return "/admin/adminMember";
    }

    // 회원 강퇴(관리자)
    @Transactional
    @RequestMapping("memberKickOut")
    public String memberKickOut(int memberSeq, Model model) throws Exception {
        memberService.memberKickOut(memberSeq);
        // 파일 삭제
        personalFileService.memberOut(memberSeq);
        // 기본 폴더 삭제
        basicFolderService.memberOut(memberSeq);
        // 폴더 삭제
        personalFolderService.memberOut(memberSeq);
        // 캘린더 삭제
        calService.deleteUserEvent(memberSeq);

        List<MemberDTO> list = memberService.selectMembers();

        // 회원이 매니저면서, 한명뿐인 팀이 있는 경우, 관련 컨텐츠를 모두 없애준다.
        chatMsgService.delOnlyOneMsg(memberSeq);
        chatMemberService.delOnlyOneChatMember(memberSeq);
        chatRoomService.delOnlyOneChatRoom(memberSeq);
        teamMemberService.delOnlyOneTeamMember(memberSeq);
        teamService.delTeamOnlyOne(memberSeq);

        //회원이 매니저인 팀을 제외한 나머지 목록 출력
        List<TeamMemberDTO> teamMemberList = teamMemberService.selectMembersTeam(memberSeq);
        //각 목록별로 팀 삭제 가동
        for (TeamMemberDTO teamMemberDto : teamMemberList) {
            teamMemberService.deleteTeamMember(teamMemberDto);
            teamService.updateMinusTeamCount(teamMemberDto.getTeamSeq());
            chatRoomService.teamSelfOut(teamMemberDto);
        }
        memberService.deleteMember(String.valueOf(memberSeq));

        model.addAttribute("memberList", list);
        return "redirect:member/toAdminMember";
    }
}