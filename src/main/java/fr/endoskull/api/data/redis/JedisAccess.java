package fr.endoskull.api.data.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisAccess {

    private String ip, password;
    private int port;
    private static JedisPool userpool;
    private static JedisPool serverpool;
    private static JedisPool partyPool;

    public JedisAccess(String ip, int port, String password){
        this.ip = ip;
        this.port = port;
        this.password = password;
    }

    public void initConnection(){
        ClassLoader previous = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(Jedis.class.getClassLoader());
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxWaitMillis(5000);
        userpool = new JedisPool(jedisPoolConfig, ip, port, 5000, password, 0);
        serverpool = new JedisPool(jedisPoolConfig, ip, port, 5000, password, 1);
        partyPool = new JedisPool(jedisPoolConfig, ip, port, 5000, password, 2);
        Thread.currentThread().setContextClassLoader(previous);
    }

    public static JedisPool getServerpool() {
        return serverpool;
    }

    public static JedisPool getUserpool() {
        return userpool;
    }

    public static JedisPool getPartyPool() {
        return partyPool;
    }
}
