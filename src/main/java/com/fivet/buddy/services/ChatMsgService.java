package com.fivet.buddy.services;

import com.fivet.buddy.dao.ChatMsgDAO;
import com.fivet.buddy.dto.ChatMsgDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMsgService {

    @Autowired
    private ChatMsgDAO chatMsgDao;

    // 회원가입시 메세지 입력
    public void insertChatMsg(ChatMsgDTO chatMsgDto) {
        chatMsgDao.insertChatMsg(chatMsgDto);
    }

    // 채팅방 입장시 메세지 출력
    public List<ChatMsgDTO> selectChatMsg(int chatRoomSeq) {
        return chatMsgDao.selectChatMsg(chatRoomSeq);
    }
}
