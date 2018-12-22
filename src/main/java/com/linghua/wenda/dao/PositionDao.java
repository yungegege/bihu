package com.linghua.wenda.dao;

import com.linghua.wenda.model.User;
import com.linghua.wenda.model.baidu.Position;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PositionDao {

    String TABLE_NAME = "position";
    String INSERT_FILEDS = " userId,executeTime,url,allAddress,simpleAddress,city,cityCode,district,province,street,streetNumber,x,y ";
    String SELECT_FILEDS = " id,"+INSERT_FILEDS;

    @Insert({"insert into", TABLE_NAME, "(",INSERT_FILEDS,") values(#{userId},#{executeTime},#{url},#{allAddress}," +
            "#{simpleAddress},#{city},#{cityCode},#{district},#{province},#{street},#{streetNumber},#{x},#{y})"})
    int addPostion(Position position);

//    @Select({"select",SELECT_FILEDS,"from",TABLE_NAME,"where id=#{id}"})
//    Position selectById(Position position);

//    @Update({"update",TABLE_NAME,"set method=#{method} where id=#{id}"})
//    void updatePassword(Position position);
}
