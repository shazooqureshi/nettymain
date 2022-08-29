import java.util.Scanner;

/**
 * @program: nettyConnect
 * @description:
 * @author: lengqi
 * @create: 2020-03-28 15:14
 **/
public class NettyMain
{
    public static void main(String[] args)
    {

        new NettyServer(8989,new testHandler()).start();

    }

}
