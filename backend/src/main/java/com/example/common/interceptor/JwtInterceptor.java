package com.example.common.interceptor;

import com.example.common.context.UserContext;
import com.example.common.enums.Role;
import com.example.common.utils.JwtTools;
import com.example.modules.entity.ContextData;
import com.example.modules.mapper.UtilsMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtTools jwtTools;
    @Autowired
    private UtilsMapper utilsMapper;

    // 执行指定路径函数前执行一次
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        log.info("启动拦截器，请求路径: {}", request.getRequestURI());
        // 获取令牌
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.getWriter().write("请先登录");
            log.info("未检测到令牌");
            return false;
        }
        String token = authHeader.substring(7);
        // 解析并验证
        try {
            response.setContentType("application/json; charset=utf-8");
            Claims claims = jwtTools.parseToken(token);
            // 获取账户状态和身份
            Long userId = Long.parseLong(claims.getSubject());
            Role role = Role.valueOf(claims.get("role", String.class));
            Integer status = utilsMapper.selectStatusById(userId);
            // 账户正常
            if(status != null && status != 0) {
                request.setAttribute("userId", userId);
                String username = utilsMapper.selectUsernameById(userId);
                log.info("账户\"{}\"尝试访问{} ({})，身份：{}，放行", username, request.getRequestURI(), request.getMethod(), role.getDesc());
                // 存入本地变量
                UserContext.set(new ContextData(userId, role));
                return true;
            }
            // 账户不存在
            else if(status == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("账户不存在或被删除");
            }
            // 账户被禁用
            else {
                String username = utilsMapper.selectUsernameById(userId);
                log.info("被禁用账户\"{}\"尝试访问{} ({})，身份：{}，被拦截", username, request.getRequestURI(), request.getMethod(), role.getDesc());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("该账户已被禁用");
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("令牌过期，请重新登录");
        } catch (SignatureException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("令牌验证失败，请重新登录");
        }
        return false;
    }

    // 执行指定路径函数后执行一次
    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) throws Exception {
        UserContext.remove();
    }
}

