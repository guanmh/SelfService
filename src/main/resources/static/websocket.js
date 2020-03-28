var socket;
function closeSocket() {
    //关闭事件
    socket.onclose = function() {
        console.log("websocket已关闭");
    };
}
function sendMessage() {
    if(typeof(WebSocket) == "undefined") {
        alert("浏览器版本过低");
    }else {
        var toUserId = $("#toUserId").val();
        if(toUserId == '' || toUserId == null){
            alert("请先选择联系人")
        }else{
            var sendMsg = $("#contentText").val();
            socket.send('{"toUserId":"'+toUserId+'","contentText":"'+sendMsg+'"}');
            //追加span
            $("#showMsg").append("<span style='display:inline-block;text-align: right;width: 100%'>"+sendMsg+"</span>");
            //清空
            $("#contentText").val("");
        }
    }
}
function toUserInfo(info) {
    var userId = info.id;
    var name = $("#" + userId).text();
    $("#toUser").html("正在和" + name + "聊天")
    $("#toUserId").val(info.id)
}
