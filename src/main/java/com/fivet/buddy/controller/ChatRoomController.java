package com.fivet.buddy.controller;

import com.fivet.buddy.dao.ChatMemberDAO;
import com.fivet.buddy.dao.ChatRoomDAO;
import com.fivet.buddy.dto.ChatMemberDTO;
import com.fivet.buddy.dto.ChatRoomDTO;
import com.fivet.buddy.dto.TeamDTO;
import com.fivet.buddy.dto.TeamMemberDTO;
import com.fivet.buddy.services.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chatRoom/")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMsgService chatMsgService;

    @Autowired
    private HttpSession session;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ChatMemberService chatMemberService;

    //채팅방 입장
    @RequestMapping("join")
    public String insertChatMsg(int chatRoomSeq, Model model) {
        model.addAttribute("chatRoomSeq",chatRoomSeq);
        model.addAttribute("chatMsgList", chatMsgService.selectChatMsg(chatRoomSeq));
        model.addAttribute("chatMemberList",teamMemberService.selectChatMember(chatRoomSeq));
        return ("/team/teamChating");
    }

    // 채팅방 목록 출력
    @ResponseBody
    @PostMapping("chatRoomList")
    public String chatRoomList() {
        Map<String, Integer> param = new HashMap<>();
        param.put("memberSeq", (int)session.getAttribute("memberSeq"));
        param.put("teamSeq", (int)session.getAttribute("teamSeq"));

        Gson g = new Gson();
        return g.toJson(chatRoomService.chatRoomList(param));
    }

    // 토픽 생성
    @PostMapping("insertTopic")
    public String insertTopic(ChatRoomDTO chatRoomDto) {
        TeamDTO teamDto = teamService.selectTeamOne(session.getAttribute("teamSeq").toString());

        //ChatRoomDTO 영역 (chatRoom테이블에 topic 생성)

        chatRoomDto = chatRoomService.insertTopic(teamDto, chatRoomDto);

        //ChatMember 영역(각 회원을 토픽에 가입)
        chatRoomService.insertTopic(teamDto, chatRoomDto);
        chatMemberService.insertTopicMember(chatRoomDto);

        return "redirect:/team/goTeamAgain";
    }

}
