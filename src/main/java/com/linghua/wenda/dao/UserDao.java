package com.linghua.wenda.dao;

import com.linghua.wenda.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    String TABLE_NAME = "user";
    String INSERT_FILEDS = " name,password,salt,head_url,email,status";
    String SELECT_FILEDS = " id,"+INSERT_FILEDS;

    @Insert({"insert into", TABLE_NAME, "(",INSERT_FILEDS,") values(#{name},#{password},#{salt},#{headUrl},#{email},#{status})"})
    int addUser(User user);

    @Select({"select",SELECT_FILEDS,"from",TABLE_NAME,"where id=#{id}"})
    User selectById(int id);

    @Select({"select",SELECT_FILEDS,"from",TABLE_NAME,"where name=#{name}"})
    User selectByName(String name);

    @Update({"update",TABLE_NAME,"set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Update({"update",TABLE_NAME,"set status=#{status} where id=#{id}"})
    void updateStatus(User user);
}
