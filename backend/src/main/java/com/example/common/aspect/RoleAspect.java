package com.example.common.aspect;

import com.example.common.annotation.RequireRole;
import com.example.common.context.UserContext;
import com.example.common.enums.Role;
import com.example.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class RoleAspect {

    @Around("@annotation(requireRole)")
    public Object RoleCheck(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {
        Role currentRole = UserContext.get().getRole();
        boolean allowed = currentRole != null && Arrays.asList(requireRole.value()).contains(currentRole);
        if (allowed) {
            return joinPoint.proceed();
        } else {
            throw new BusinessException("权限不足，无法访问");
        }
    }
}
