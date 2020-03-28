package com.gmh.plugs.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GMH
 * @title: WebSocketUtil
 * @projectName SelfProject
 * @description: websocket
 * @date 2020/3/28 22:51
 */
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketUtil {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String userId = "";

    //线程安全的map,把userId当作key
    private static ConcurrentHashMap<String,WebSocketUtil> map = new ConcurrentHashMap<String,WebSocketUtil>();

    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        if(map.containsKey(userId)){
            map.remove(userId);
            //加入map中
            map.put(userId,this);
        }else{
            //加入map中
            map.put(userId,this);
            //在线数加1
            addOnlineCount();
        }
        System.out.println("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(map.containsKey(userId)){
            map.remove(userId);
            //从map中删除
            subOnlineCount();
        }
        System.out.println("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("用户ID:"+userId+",消息类容是:"+message);
        if(StringUtils.isNotBlank(message)){
            try {
                //解析前端JSon
                JSONObject getObj = JSON.parseObject(message);
                //找到接收者
                String toUserId=getObj.getString("toUserId");
                String msg=getObj.getString("contentText");
                JSONObject newObj=new JSONObject();
                //发送人
                newObj.put("fromUserId",this.userId);
                newObj.put("msg",msg);
                //传送给对应toUserId用户的websocket
                if(StringUtils.isNotBlank(toUserId) && map.containsKey(toUserId)){
                    map.get(toUserId).sendMessage(newObj.toJSONString());
                }else{
                    System.out.println("请求的userId:"+toUserId+"不存在啊");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息到指定用户
     * */
    public static void sendMsgToUser(String message,String userId) throws IOException {
        System.out.println("发送消息到:"+userId+"，消息类容是:"+message);
        if(StringUtils.isNotBlank(userId) && map.containsKey(userId)){
            map.get(userId).sendMessage(message);
        }else{
            //用户离线,存储到数据库中，等登录状态下再次进行发送
            //todo
            System.out.println("用户"+userId+"不是在线状态");
        }
    }
    /**
     * 发送自定义消息到全体用户
     * */
    public static void sendMsgToAllUser(String message) throws IOException {
        System.out.println("消息类容是:"+message);
        for(WebSocketUtil webSocket : map.values()){
            webSocket.sendMessage(message);
        }
    }

    /**
     * 获取在线人数
     * @return
     */
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }

    /**
     * 增加在线人数
     */
    public static synchronized void addOnlineCount(){
        onlineCount++;
    }

    /**
     * 减少在线人数
     */
    public static synchronized void subOnlineCount(){
        onlineCount--;
    }
}
