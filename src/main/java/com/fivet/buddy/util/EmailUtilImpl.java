package com.fivet.buddy.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailUtilImpl implements EmailUtil {

    @Autowired
    private JavaMailSender sender;

    @Override
    public Map<String, Object> sendEmail(String toAddress, String subject, String body) {
        Map<String, Object> result = new HashMap<String, Object>();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String htmlStr = "    <div id=\"container\" style=\"border:1px solid beige; width:500px; height:590px\">\n" +
                "        <div id=\"header\" style=\"overflow:hidden; width:100%; height:140px;\">\n" +
                "            <div style=\"float:left; height:100%; width: 100%;\">\n" +
                "                <a href=\"http://buddychat.site\" target=\"blank\"><img src=\"https://i.esdrop.com/d/f/iRteJoq4AE/UZlnlzP5pa.png\" style=\"width:300px; height:80%;\"></a>\n" +
                "                <hr style=\"height:10px;background-color: #8621F7; border:none;\">\n" +
                "</div>\n" +
                "        </div>\n" +
                "        <div id=\"main\" style=\"width:100%; height:280px;\">\n" +
                "            <div style=\"margin:auto; width:400px; height:70%;\">\n" +
                "                <h2>&#50504;&#45397;&#54616;&#49464;&#50836;.&#128522;&#128522;</h2>\n" +
                "                <p>&#54801;&#50629;&#47700;&#49888;&#51200; Buddy&#51077;&#45768;&#45796;.</p>\n" +
                "                <p>&#50836;&#52397;&#54616;&#49888; &#51060;&#47700;&#51068; &#51064;&#51613;&#53076;&#46300;&#51077;&#45768;&#45796;.</p>\n" +
                "                <p>&#50500;&#47000; &#51064;&#51613; &#53076;&#46300;&#47484; &#54924;&#50896;&#44032;&#51077; &#52285;&#50640; &#51221;&#54869;&#55176; &#51077;&#47141;&#54644;&#51452;&#49464;&#50836;.</p>\n" +
                "                <div style=\"margin:auto; width:400px; height:30%; background-color:beige; text-align:center; line-height:60px; font-size:30px; font-weight:bold;\">\n" +
                "                    "+body+"\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div id=\"footer\" style=\" width:100%; height:140px; color:gray; \">\n" +
                "            <hr>\n" +
                "<p style=\"margin-left:15px\">&#48376; &#47700;&#51068;&#51008; &#48156;&#49888; &#51204;&#50857; &#47700;&#51068;&#51060;&#47728;,</p>\n" +
                "            <p style=\"margin-left:15px\">&#54924;&#49888;&#46104;&#51648; &#50506;&#51004;&#48064;&#47196; &#47928;&#51032;&#49324;&#54637;&#51008; <a href=\"http://buddychat.site\" style=\"color:black;\" target=\"blank\">&#44256;&#44061;&#49468;&#53552;</a>&#47484; &#51060;&#50857;&#54644;&#51452;&#49464;&#50836;.</p>\n" +
                "            <p style=\"margin-left:15px\">COPYRIGHTS (C)BUDDY ALL RIGHTS RESERVED.</p>\n" +
                "        </div>\n" +
                "    </div>\n"
                ;

        try {
            helper.setTo(toAddress);
            helper.setSubject(subject);
            message.setText(htmlStr, "UTF-8", "html");
            //helper.setText(body);
            result.put("resultCode", 200);
        } catch (MessagingException e) {
            e.printStackTrace();
            result.put("resultCode", 500);
        }

        sender.send(message);
        return result;
    }

    @Override
    public Map<String, Object> sendInviteEmail(String toAddress, String subject, String body, String teamName) {
        Map<String, Object> result = new HashMap<String, Object>();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String htmlStr = "<html><body><div id=\"container\" style=\"border:1px solid beige; width:500px; height:680px\">\n" +
                "        <div id=\"header\" style=\"overflow:hidden; width:100%; height:140px;\">\n" +
                "            <div style=\"float:left; height:100%; width: 100%;\">\n" +
                "                <a href=\"http://buddychat.site\" target=\"blank\"><img src=\"https://i.esdrop.com/d/f/iRteJoq4AE/UZlnlzP5pa.png\" style=\"width:300px; height:80%;\"></a>\n" +
                "                <hr style=\"height:10px;background-color: #8621F7; border:none;\">\n" +
                "</div>\n" +
                "        </div>\n" +
                "        <div id=\"main\" style=\"width:100%; height:370px;\">\n" +
                "            <div style=\"margin:auto; width:400px; height:70%;\">\n" +
                "                <h2>&#50504;&#45397;&#54616;&#49464;&#50836;&#128522;&#128522;</h2>\n" +
                "                <p>&#54801;&#50629;&#47700;&#49888;&#51200; Buddy&#51077;&#45768;&#45796;.</p>\n" +
                "                <p>'"+ teamName +"'&#54016;&#50640;&#49436; &#48372;&#45240; &#54016; &#52488;&#45824;&#53076;&#46300;&#51077;&#45768;&#45796;.</p>\n" +
                "                <p>&#50500;&#47000; &#52488;&#45824; &#53076;&#46300;&#47484; &#52488;&#45824;&#53076;&#46300; &#51077;&#47141; &#52285;&#50640; &#51077;&#47141;&#54644;&#51452;&#49464;&#50836;.</p>\n" +
                "                <div style=\"margin:auto; width:400px; height:30%; background-color:beige; text-align:center; line-height:60px; font-size:30px; font-weight:bold;\">\n" +
                "                    "+body+"\n" +
                "                </div>\n" +
                "                <p style=\"font-size:small;\">&#50948; &#53076;&#46300;&#45716; &#51068;&#54924;&#49457; &#53076;&#46300;&#51060;&#47728; &#54620; &#48264; &#49324;&#50857;&#54616;&#47732; &#51116;&#49324;&#50857; &#54624; &#49688; &#50630;&#49845;&#45768;&#45796;.</p>\n" +
                "                <a href=\"http://buddychat.site\"><button style=\"cursor:pointer; border:none; background-color:#8621F7; border-radius:10px; margin-left:50%; transform:translateX(-50%); color:white; width:200px; height:40px;\">Buddy&#47196; &#51060;&#46041;&#54616;&#44592;</button></a>\n" +
                "                <p style=\"font-size:small;\">&#50500;&#51649; &#54924;&#50896;&#51060; &#50500;&#45768;&#46972;&#47732;? <a href=\"http://buddychat.site/member/toSignUp\" target=\"blank\" style=\"color:#8621F7;\">&#54924;&#50896;&#44032;&#51077;</a></p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div id=\"footer\" style=\" width:100%; height:140px; color:gray; \">\n" +
                "            <hr>\n" +
                "<p style=\"margin-left:15px; font-size:small; line-height: 100%;\">&#48376; &#47700;&#51068;&#51008; &#48156;&#49888; &#51204;&#50857; &#47700;&#51068;&#51060;&#47728;,</p>\n" +
                "            <p style=\"margin-left:15px; font-size:small; line-height: 100%;\">&#54924;&#49888;&#46104;&#51648; &#50506;&#51004;&#48064;&#47196; &#47928;&#51032;&#49324;&#54637;&#51008; <a href=\"http://buddychat.site\" style=\"color:black;\" target=\"blank\">&#44256;&#44061;&#49468;&#53552;</a>&#47484; &#51060;&#50857;&#54644;&#51452;&#49464;&#50836;.</p>\n" +
                "            <p style=\"margin-left:15px; font-size:small; line-height: 100%;\">COPYRIGHTS (C)BUDDY ALL RIGHTS RESERVED.</p>\n" +
                "        </div>\n" +
                "    </div></body></html>"
                ;

        try {
            helper.setTo(toAddress);
            helper.setSubject(subject);
            message.setText(htmlStr, "UTF-8", "html");
            //helper.setText(body);
            result.put("resultCode", 200);
        } catch (MessagingException e) {
            e.printStackTrace();
            result.put("resultCode", 500);
        }

        sender.send(message);
        return result;
    }



}
