package com.example.demo.serice;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import sun.net.www.http.HttpClient;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

/**
 * Created by lenovo on 2018/3/24.
 */
@Service
public class SetNodes {
    public void setNodes() {
        ZkClient zkClient = new ZkClient("localhost:2181", 6000);
        if (zkClient.exists("/zhuohua/com.zheng.Test2") && zkClient.exists("/zhuohua/com.zheng.Test1")) {
            zkClient.writeData("/zhuohua/com.zheng.Test2", "{\"localname\":\"localhost\",\"port\":\"8080\"}");
            zkClient.writeData("/zhuohua/com.zheng.Test1", "{\"localname\":\"localhost\",\"port\":\"8081\"}");
            int size = zkClient.getChildren("/zhuohua").size();
            System.out.println(size);
            //节点的监听
            zkClient.subscribeDataChanges("/zhuohua/com.zheng.Test1", new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {
                    //属性修改触发
                }

                @Override
                public void handleDataDeleted(String dataPath) throws Exception {

                }
            });
            Random r = new Random();
            int a=r.nextInt(size);
            if (a==0){
               a=1;
            }else{
                a=2;
            }
            String o = zkClient.readData("/zhuohua/com.zheng.Test" +a );
            try {
                JSONObject obj = new JSONObject(o);
                String name = obj.getString("localname");
                Integer port = Integer.parseInt(obj.getString("port"));
                System.out.println(name);
                System.out.println(port);
                //1.新建一个客户端对象
                CloseableHttpClient client = HttpClients.createDefault();
                //2.使用URIBuilder来生成一个get类型的URI
                URI uri = new URIBuilder().setScheme("http").setPort(port).setHost(name).build();   //把set设置的值按照get接口类型进行拼接
                //3.新建一个HttpGet类型的get请求对象,并使用uri进行初始化->
                //将uri请求值赋给get对象
                HttpGet get = new HttpGet(uri);
                //4.新建一个响应对象来接收客户端执行get的结果
                CloseableHttpResponse response = client.execute(get);
                System.out.println(response.getStatusLine().getStatusCode()); //获取访问的状态

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
