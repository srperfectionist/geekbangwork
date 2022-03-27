package week11.pubsub;

import redis.clients.jedis.JedisPool;

/**
 * @author shirui
 * @date 2022/3/27
 */
public class PubSubTest {

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool();
        String channelName = "ORDER";

        Subscribe subscribeOrder = new Subscribe(jedisPool, channelName);
        Publish publishOrder = new Publish(jedisPool, channelName);
    }
}
