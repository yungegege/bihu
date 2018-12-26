package com.linghua.wenda.ftp;

import lombok.extern.log4j.Log4j;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class ftpServer implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();
        //设置监听端口
        factory.setPort(2121);
        //替换默认监听
        serverFactory.addListener("default", factory.createListener());
        //用户名
        BaseUser user = new BaseUser();
        user.setName("admin");
        //密码 如果不设置密码就是匿名用户
        user.setPassword("lyf.199781rnj");
        user.setHomeDirectory("/usr/local/tomcat/apache-tomcat-8.5.37/logs/");
        List<Authority> authorities = new ArrayList<Authority>();
        //增加写权限
        authorities.add(new WritePermission());
        user.setAuthorities(authorities);
        //增加该用户
        serverFactory.getUserManager().save(user);
        /**
         * 也可以使用配置文件来管理用户
         */
//      PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
//      userManagerFactory.setFile(new File("users.properties"));
//      serverFactory.setUserManager(userManagerFactory.createUserManager());
        FtpServer server = serverFactory.createServer();
        server.start();
    }
}
