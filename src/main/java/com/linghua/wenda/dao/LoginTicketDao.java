package com.linghua.wenda.dao;

import com.linghua.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDao {

    String TABLE_NAME = " login_ticket ";
    String INSERT_FILEDS = " user_id,expired,status,ticket ";
    String SELECT_FILDS = " id,"+INSERT_FILEDS;

    @Insert({"insert into", TABLE_NAME, "(",INSERT_FILEDS,") values(#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket loginTicket);

    @Select({"select",SELECT_FILDS,"from",TABLE_NAME,"where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket")String ticket,@Param("status")int status);

}
