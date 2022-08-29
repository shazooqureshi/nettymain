import com.sun.xml.internal.ws.api.message.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: nettyConnectDemo
 * @description:
 * @author: lengqi
 * @create: 2020-03-27 14:54
 **/
@ChannelHandler.Sharable
public class NettySocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
{

    // ip : Context
    private ConcurrentHashMap<String, ChannelHandlerContext> addressContext = new ConcurrentHashMap<String, ChannelHandlerContext>();

    //服务器接收客户端发送数据
    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame msg) throws Exception
    {
        System.out.println("message : "+ msg.text());

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("与客户端: "+getAddress(ctx)+" 建立连接，通道开启。。。");
        getContextMap(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        ByteBuf in = (ByteBuf) msg;

        try
        {
            byte[] meesByte = new byte[in.readableBytes()];
            in.readBytes(meesByte);
            String str = new String(meesByte,"UTF-8");


            String address = (getAddress(ctx));
            nettyReciveMessage(address,str);

//            sendInfo(getAddress(ctx), address+"  --->aaaaaaaaaaaaaaaaaaaaaaaaaaaaaafffffffffffffffffff");
        }
        finally
        {
            ReferenceCountUtil.release(msg);
        }
    }

    private String getAddress(ChannelHandlerContext ctx)
    {
        String remoteIp = ctx.channel().remoteAddress().toString();
        return remoteIp.substring(1,remoteIp.indexOf(":"));
    }

    public void nettyReciveMessage(String address, String message)
    {
        System.out.println("========================================");
        System.out.println("message : " + message + " address : "+address);
    }

    private void getContextMap(ChannelHandlerContext ctx)
    {
        String address = getAddress(ctx);

        if(null == addressContext.get(address))
        {
            addressContext.put(address,ctx);
        }
    }


    public void sendInfo(String address, String message)
    {
        ChannelHandlerContext ctx = addressContext.get(address);

        if(null == ctx)
        {
            System.out.println("the address --->ctx is null");
            return;
        }

        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
    }

}
