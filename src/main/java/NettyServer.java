import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * @program: nettyConnectDemo
 * @description:
 * @author: lengqi
 * @create: 2020-03-27 13:54
 **/
public class NettyServer implements NettyProxy
{
    private Integer port;

    private NettySocketHandler nettySocketHandler;



    private EventLoopGroup bossGroup = null;

    private EventLoopGroup workerGroup = null;

    public NettyServer(Integer port,NettySocketHandler nettySocketHandler)
    {
        this.port = port;
        this.nettySocketHandler = nettySocketHandler;
    }

    public void start()
    {
         bossGroup = new NioEventLoopGroup();
         workerGroup = new NioEventLoopGroup();

        try
        {
            System.out.println("start netty ... ");

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(this.port)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception
                        {
//                            channel.pipeline().addLast(new NettySocketHandler());
                            channel.pipeline().addLast(nettySocketHandler);

                        }
                    });

            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            //服务器异步创建 绑定
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            //关闭服务器通道
            channelFuture.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //释放资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void close()
    {
        if(null != workerGroup)
        {
            workerGroup.shutdownGracefully();
        }

        if(null != bossGroup)
        {
            bossGroup.shutdownGracefully();
        }
    }
}
