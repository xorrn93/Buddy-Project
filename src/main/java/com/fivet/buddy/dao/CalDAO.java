package com.fivet.buddy.dao;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fivet.buddy.dto.CalDTO;
import com.fivet.buddy.mapper.CalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CalDAO {
    @Autowired
    private CalMapper calMapper;

    public void insertEvent(CalDTO calDto) throws Exception{
         calMapper.insertEvent(calDto);
    }

    public List<CalDTO> selectAll(int teamSeq) throws Exception{
        return calMapper.selectAll(teamSeq);
    }

    public List<CalDTO> selectTeam(int teamSeq) throws Exception{
        return calMapper.selectTeam(teamSeq);
    }
    public List<CalDTO> selectPrivate(int memberSeq, int teamSeq) throws Exception{
        return calMapper.selectPrivate(memberSeq, teamSeq);
    }

    public String selectGrade(String teamMemberNickname) throws Exception {
        return calMapper.selectGrade(teamMemberNickname);
    }

    public void deleteEvent(int eventSeq, int memberSeq) throws Exception{
        calMapper.deleteEvent(eventSeq, memberSeq);
    }

    public void deleteUserEvent(int memberSeq) throws Exception{
        calMapper.deleteUserEvent(memberSeq);
    }

    public void deleteTeamMemberEvent(String teamMemberNickname) throws Exception{
        calMapper.deleteTeamMemberEvent(teamMemberNickname);
    }

    public void deleteTeamAllEvent(int teamSeq) throws Exception {
        calMapper.deleteTeamAllEvent(teamSeq);
    }

    public  void updateEvent(CalDTO calDto) throws  Exception {
        calMapper.updateEvent(calDto);
    }

    public void updateNickname(int teamSeq, int memberSeq, String eventWriter) throws  Exception{
        calMapper.updateNickname(teamSeq, memberSeq, eventWriter);
    }



}
