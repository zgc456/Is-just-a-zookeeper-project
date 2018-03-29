package com.example.demo.serice;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by lenovo on 2018/3/24.
 */
@Component
public class ZookeeperConumer implements CommandLineRunner{
    @Autowired
    SetNodes setNodes;
    @Override
    public void run(String... args) throws Exception {
        ZkClient zkClient=new ZkClient("localhost:2181",6000);
        zkClient.createPersistent("/zhuohua/com.zheng.Test2",true);
//        zkClient.createPersistent("/zhuohua/com.zheng.Test1",true);
        zkClient.close();
        setNodes.setNodes();
    }
}
