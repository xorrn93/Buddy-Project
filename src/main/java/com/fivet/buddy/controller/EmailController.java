package com.fivet.buddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fivet.buddy.services.InviteService;
import com.fivet.buddy.services.MemberService;
import com.fivet.buddy.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/email/")
public class EmailController {

    @Autowired
    private InviteService inviteService;

    @Autowired
    private MemberService ms;

    private final EmailUtil emailUtil;

    // ExceptionHandler
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e) {
        e.printStackTrace();
        return "error";
    }

    // 이메일 발송
    @PostMapping("sendEmail")
    public Map<String, Object> sendEmail(@RequestBody Map<String, Object> params){
        return emailUtil.sendEmail( (String) params.get("userId")
                , (String) params.get("subject").toString()
                , (String) params.get("body")
        );
    }

    // 이메일 인증번호 리턴
    @ResponseBody
    @PostMapping("emailProve")
    public String emailProve() throws  Exception{
        return createEmailKey();
    }

    // 이메일 인증코드 (숫자 6)
    public String createEmailKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 이메일 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        System.out.println(key);
        return key.toString();
    }

    // INVITE
    // 인증 이메일 발송
    @PostMapping("sendInviteEmail")
    public Map<String, Object> sendInviteEmail(@RequestBody Map<String, Object> params){
        return emailUtil.sendInviteEmail( (String) params.get("userId")
                , (String) params.get("subject")
                , (String) params.get("body")
                , (String) params.get("teamName")
        );
    }

    // 초대 인증코드 리턴(초대에선 db에 저장해야함)
    @ResponseBody
    @PostMapping("inviteProve")
    public String inviteProve() throws Exception{
        return inviteCode();
    }

    // 숫자+영문 10자리 생성
    public String inviteCode() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 10; i++) {
            if (rnd.nextBoolean()) {
                key.append((char) ((int) (rnd.nextInt(26)) + 97));
            } else {
                key.append((rnd.nextInt(10)));
            }
        }
        System.out.println(key);
        return key.toString();
    }

    // 임시비밀번호 생성
    public String getTempPW(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
    // 임시 비밀번호 리턴
    @ResponseBody
    @PostMapping("returnTempPW")
    public String returnTempPW() throws  Exception{
        return getTempPW();
    }
    // 임시비밀번호 발송 및 업데이트
    @PostMapping("sendPW")
    public Map<String, Object> sendPW(@RequestBody Map<String, Object> params) throws Exception {
        // 임시비밀번호로 업데이트
        ms.updateTempPW((String) params.get("body"),(String) params.get("userId"));
        // 이메일 전송
        return emailUtil.sendEmail( (String)params.get("userId")
                , (String)params.get("subject")
                , (String)params.get("body")
        );
    }

}
