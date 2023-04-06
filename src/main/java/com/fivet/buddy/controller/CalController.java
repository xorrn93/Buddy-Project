package com.fivet.buddy.controller;

import com.fivet.buddy.dto.CalDTO;
import com.fivet.buddy.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/calendar/")
public class CalController {

    @Autowired
    private CalService calService;

    @Autowired
    private HttpSession session;

    // 오류 발생시 Exception 으로 처리
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e) {
        e.printStackTrace();
        return "calendar/calendarError";
    }
    // 이벤트 생성 섹션
    @RequestMapping("insertEvent")
    public String insertEvent(CalDTO calDto) throws Exception{
        calDto.setMemberSeq((Integer)session.getAttribute("memberSeq"));
        calDto.setTeamSeq((Integer)session.getAttribute("teamSeq"));
        // 작성자는 자동으로 팀 닉네임으로 설정
        calDto.setEventWriter(session.getAttribute("teamMemberNickname").toString());
        calService.insertEvent(calDto);
        return "redirect:/calendar/moveCalendar";
    }

    // 캘린더 이동 처리 섹션
    @RequestMapping(value = "moveCalendar" ,method = RequestMethod.GET)
    public String moveCalendar()throws Exception {
        // teamSeq 세션 값이 없을 시 오류 페이지 이동
        if (session.getAttribute("teamSeq") != null ){
        return "calendar/calendar";
        }
        return "calendar/calendarError";
    }
    // 개인 캘린더 이동
    @RequestMapping(value = "goPrivate")
    public String goPrivate()throws Exception {
        if ( session.getAttribute("teamSeq") != null ){
            return "calendar/calendarPrivate";
        }
        return "calendar/calendarError";
    }
    // 팀 캘린더 이동
    @RequestMapping(value = "goTeam")
    public String goTeam()throws Exception {
        if ( session.getAttribute("teamSeq") != null ){
            return "calendar/calendarTeam";
        }
        return "calendar/calendarError";
    }
    // 캘린더 DB 가져오기 (전체데이터)
    @ResponseBody
    @RequestMapping (value = "selectAll" , produces = "text/html;charset=utf8" ,method=RequestMethod.GET)
    public String selectAll() throws Exception{
            List<CalDTO> calList = calService.selectAll((Integer) session.getAttribute("teamSeq"));
            // DB 값을 gson 으로 처리
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String list = gson.toJson(calList);
            return list;
    }
    // 캘린더 DB 가져오기 (팀데이터)
    @ResponseBody
    @RequestMapping (value = "selectTeam" , produces = "text/html;charset=utf8")
    public String selectTeam() throws Exception{
        List<CalDTO> calList = calService.selectTeam((Integer) session.getAttribute("teamSeq"));
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String list = gson.toJson(calList);
        return list;
    }
    // 캘린더 DB 가져오기 (개인 데이터)
    @ResponseBody
    @RequestMapping (value = "selectPrivate" , produces = "text/html;charset=utf8")
    public String selectPrivate() throws Exception{
        int memberSeq = ((Integer)session.getAttribute("memberSeq"));
        int teamSeq = ((Integer)session.getAttribute("teamSeq"));
        List<CalDTO> calList = calService.selectPrivate(memberSeq,teamSeq);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String list = gson.toJson(calList);
        System.out.println(list);
        return list;
    }
    // 팀 캘린더 일정 삭제 시
    @Transactional
    @GetMapping("teamEventDel")
    public String teamEventDel(int eventSeq) throws Exception{
        // 닉네임 섹션값으로 등급 판별
        String grade = calService.selectGrade(session.getAttribute("teamMemberNickname").toString());
        // 등급이 매니저 일 시 행동
        String manager = "매니저";
        boolean result =  Objects.equals(grade, manager);
        if (result){
            // 결과 값이 참일 시에 memberSeq 와 eventSeq 섹션으로 삭제
            int memberSeq = (Integer)session.getAttribute("memberSeq");
            calService.deleteEvent(eventSeq, memberSeq);
            return "redirect:/calendar/moveCalendar";
        }else {
            return "calendar/gradeError";
        }
    }
    // 팀 캘린더 일정 업데이트 시
    // 팀 캘린더 편집은 등급이 매니저일 시 만 가능
    @Transactional
    @GetMapping("teamEventUpd")
    public String privateEventDel(CalDTO calDto) throws Exception{
        String grade = calService.selectGrade(session.getAttribute("teamMemberNickname").toString());
        String manager = "매니저";
        boolean result =  Objects.equals(grade, manager);
        if (result){
            calDto.setEventWriter(session.getAttribute("teamMemberNickname").toString());
            calService.updateEvent(calDto);
            return "redirect:/calendar/moveCalendar";
        }else {
            return "calendar/gradeError";

        }
    }
    // 일반 캘린더 일정 삭제 시
    @Transactional
    @GetMapping("deleteEvent")
    public String deleteEvent(int eventSeq , String eventWriter ) throws Exception{
        // 닉네임 세션 값과 작성자 닉네임이 같을 시
        String Session = session.getAttribute("teamMemberNickname").toString();
        boolean result = Objects.equals(eventWriter, Session);
        if (result){
            int memberSeq = (Integer)session.getAttribute("memberSeq");
        calService.deleteEvent(eventSeq, memberSeq);
        return "redirect:/calendar/moveCalendar";

        }else {
            return "calendar/gradeError";

        }
    }

    // 일반 캘린더 업데이트
    @GetMapping (value="updateEvent")
    public String updateEvent(CalDTO calDto, String eventWriter) throws Exception{
        String Session = session.getAttribute("teamMemberNickname").toString();
        boolean result = Objects.equals(eventWriter, Session);
        if(result) {
            calDto.setEventWriter(session.getAttribute("teamMemberNickname").toString());
            calService.updateEvent(calDto);
            return "redirect:/calendar/moveCalendar";
        }else {
            return "calendar/gradeError";

        }

    }

    @RequestMapping(value = "goMobile")
    public String goMobile(){
        return "calendar/mCalendar";
    }


}