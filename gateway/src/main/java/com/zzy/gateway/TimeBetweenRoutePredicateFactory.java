package com.zzy.gateway;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author zzy
 * @Date 2020/12/7 18:49
 */
@Component
public class TimeBetweenRoutePredicateFactory
        extends AbstractRoutePredicateFactory<TimeBetweenConfig>
{

    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetweenConfig.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
        LocalTime start = config.getStart();
        LocalTime end = config.getEnd();
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange exchange) {
                LocalTime now = LocalTime.now();
                return now.isAfter(start) && now.isBefore(end);
            }
        };
    }

    /**
     * 对应配置类和配置文件的映射关系
     * 参数分别对应配置文件里的start,end
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("start","end");
    }




}
