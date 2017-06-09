package dev.mars.net.message;

/**
 * 心跳包消息
 * body:{ "type":"0"}
 * @author ma.xuanwei
 *
 */
public class KeepAliveMessage extends SocketMessage {
    public KeepAliveMessage(){
        setBody("");
    }

    @Override
    public boolean equals(Object o) {
        try{
            SocketMessage other = (SocketMessage)o;
            if(other.getBody()==null||other.getBody().length==0) {
                return true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
