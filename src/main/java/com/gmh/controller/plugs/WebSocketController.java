package com.gmh.controller.plugs;

import com.gmh.plugs.websocket.WebSocketUtil;
import com.gmh.utils.ReturnUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author GMH
 * @title: WebSocketController
 * @projectName SelfProject
 * @description:
 * @date 2020/3/28 23:16
 */
@RestController("webSocket")
public class WebSocketController {

    /**
     * 指定用户发送消息
     * @param message
     * @param toUserId
     * @return
     * @throws IOException
     */
    @GetMapping("/sendToUser")
    String sendToUser(String message,String toUserId) throws IOException {
        WebSocketUtil.sendMsgToUser(message,toUserId);
        return ReturnUtil.success();
    }

    /**
     * 发送消息给所有用户
     * @param message
     * @return
     * @throws IOException
     */
    @GetMapping("/sendToAllUser")
    String sendToAllUser(String message) throws IOException {
        WebSocketUtil.sendMsgToAllUser(message);
        return ReturnUtil.success();
    }
}
