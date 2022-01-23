package week3.filter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author shirui
 * @date 2022/1/23
 */
public class ProxyBizFilter extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        //判断该  HttpObject msg 参数是否是 Http 请求
        if(o instanceof HttpRequest){
            System.out.println(channelHandlerContext.channel().remoteAddress() + " 客户端请求数据 ... ");

            // HTTP 请求过滤核心代码 -----------------------------------------------------------
            // 判定 HTTP 请求类型, 过滤 HTTP 请求

            // 获取 HTTP 请求
            HttpRequest httpRequest = (HttpRequest) o;
            // 获取网络资源 URI
            URI uri = new URI(httpRequest.uri());
            System.out.println("本次 HTTP 请求资源 " + uri.getPath());

            // 判定 uri 中请求的资源, 如果请求的是网站图标, 那么直接屏蔽本次请求
            if(uri != null && uri.getPath() != null && uri.getPath().contains("ico")){
                System.out.println("请求图标资源 " + uri.getPath() +", 屏蔽本次请求 !");
                return;
            }
            // HTTP 请求过滤核心代码 -----------------------------------------------------------

            // 准备给客户端浏览器发送的数据
            ByteBuf byteBuf = Unpooled.copiedBuffer("Hello Client", CharsetUtil.UTF_8);

            // 设置 HTTP 版本, 和 HTTP 的状态码, 返回内容
            DefaultFullHttpResponse defaultFullHttpResponse =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK, byteBuf);

            // 设置 HTTP 请求头
            // 设置内容类型是文本类型
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            // 设置返回内容的长度
            defaultFullHttpResponse.headers().set(
                    HttpHeaderNames.CONTENT_LENGTH,
                    byteBuf.readableBytes());

            // 写出 HTTP 数据
            channelHandlerContext.writeAndFlush(defaultFullHttpResponse);
        }
    }
}
