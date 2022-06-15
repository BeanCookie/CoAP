package cn.lz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CoAPServer {
    public static class P {
        Map<Long, String> map;

        public P setMap(Map<Long, String> map) {
            this.map = map;
            return this;
        }

        public Map<Long, String> getMap() {
            return map;
        }
    }
    public static void main(String[] args) {
        CoapServer server = new CoapServer();//主机为localhost 端口为默认端口5683
        server.add(new CoapResource("hello"){//创建一个资源为hello 请求格式为 主机：端口\hello
            @Override
            public void handleGET(CoapExchange exchange) { //重写处理GET请求的方法
                exchange.getRequestPayload();
                ObjectMapper mapper = new CBORMapper();
                try {
                    byte[] bytes = exchange.getRequestPayload();
                    System.out.println(bytes.length);
                    System.out.println(Arrays.toString(exchange.getRequestPayload()));
                    Map<?, ?> data = mapper.readValue(exchange.getRequestPayload(), Map.class);
//                    List<Object> data = mapper.readValue(exchange.getRequestPayload(), List.class);
                    System.out.println(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exchange.respond(CoAP.ResponseCode.CONTENT, "Hello CoAP!");
            }
        });
        server.add(new CoapResource("time"){ //创建一个资源为time 请求格式为 主机：端口\time
            @Override
            public void handleGET(CoapExchange exchange) {
                Date date = new Date();
                exchange.respond(CoAP.ResponseCode.CONTENT,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
            }
        });
        server.start();
    }
}
