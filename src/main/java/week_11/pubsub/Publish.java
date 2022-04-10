package week_11.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Random;

/**
 * @author shirui
 * @date 2022/3/27
 */
public class Publish {

    public Publish(JedisPool jedisPool, String channelName) {
        try (Jedis jedis = jedisPool.getResource()) {
            int sleepTime = 0;
            for (int i = 0; i < 10; i++) {
                sleepTime = new Random().nextInt(10) + 1;

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                jedis.publish(channelName, "order sleep " + sleepTime);
            }
            jedis.publish(channelName, "");
        }
    }
}
