//package com.qianbing.gateway.filter;
//
//import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
//import cn.dev33.satoken.stp.StpUtil;
//import cn.dev33.satoken.exception.NotLoginException;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.*;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Component
//public class AuthGlobalFilter implements GlobalFilter, Ordered {
//
//    // 放行的白名单路径（如登录、注册）
//    private static final List<String> whiteList = List.of(
//            "/api/user/doLogin",
//            "/api/get/info",
//            "/auth/captcha"
//    );
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getPath().toString();
//
//        // 白名单放行
//        if (whiteList.stream().anyMatch(path::startsWith)) {
//            return chain.filter(exchange);
//        }
//
//        try {
//            // 先 set 上下文，再调用 Sa-Token 同步 API，并在 finally 里清除上下文
//            SaReactorSyncHolder.setContext(exchange);
//            // Sa-Token 登录校验（这里会自动从请求头读取 token）
//            StpUtil.checkLogin();
//        } catch (NotLoginException e) {
//            // 未登录，构造 JSON 响应
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//            String body = "{\"code\":401,\"msg\":\"未登录，请登录后再访问\"}";
//            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
//            return response.writeWith(Mono.just(buffer));
//        }finally {
//            SaReactorSyncHolder.clearContext();
//        }
//
//        // 登录成功，放行
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return -100; // 优先执行
//    }
//}
