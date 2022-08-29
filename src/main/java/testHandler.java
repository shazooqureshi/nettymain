import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;

/**
 * @program: nettyConnectDemo
 * @description:
 * @author: lengqi
 * @create: 2020-03-28 14:15
 **/
@ChannelHandler.Sharable
public class testHandler extends NettySocketHandler
{
    @Override
    public void nettyReciveMessage(String address, String message)
    {
        System.out.println("this is overridde");

        System.out.println("mess : "+ message);

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(message);

            System.out.println(" get ---> method : "+  root.get("method"));
            System.out.println(" path -->method : "+   root.path("method"));

            System.out.println(" path -->body : "+   root.path("body"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void send()
    {
        sendInfo("192.168.2.3","{\"method\":\" person/delete \",\"timestamp\":1584518232928,\"body\":{\" deleteId \":“1,2”}}");
    }
}
