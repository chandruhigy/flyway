package com.example.product.connections;

import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TenantFinder {

    List<Tenant> tenants = new ArrayList<Tenant>();

    private final String redisTenantValue = "tenant_list";

    private Jedis jedis = null;

    private static TenantFinder tenantFinder = null;

    private TenantFinder(){

    }

    public static TenantFinder getInstance(){
        if(tenantFinder ==null){
            tenantFinder = new TenantFinder();

        }
        return tenantFinder;
    }

    @PostConstruct
    public void loadRedisIntoMap(){
        jedis = RedisClientConnection.getInstance();
        if(jedis.exists(redisTenantValue)){
            Map<String,String> tenantkeys = jedis.hgetAll(redisTenantValue);
            for(Map.Entry<String,String> entry : tenantkeys.entrySet()){
                    Tenant tenant = new Tenant(Integer.parseInt(entry.getKey()), entry.getValue());
                    tenants.add(tenant);
            }
        }
    }

    public  Tenant findByName( String name) {
        loadRedisIntoMap();
        return tenants.stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public  Tenant findById(Integer id) {
        loadRedisIntoMap();
        return tenants.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Tenant> getAllTenants(){
        loadRedisIntoMap();
        return tenants;
    }


    @Converter
    public class TypeConverter implements AttributeConverter<Tenant, Integer> {
        @Override
        public Integer convertToDatabaseColumn(Tenant tenantName) {
            return tenantName != null ? tenantName.getId() : null;
        }

        @Override
        public Tenant convertToEntityAttribute(Integer id) {
            return id != null ? findById(id) : null;
        }
    }





}
