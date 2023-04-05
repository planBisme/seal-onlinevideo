package com.seal.onlinevideo.api.aspect;

import com.seal.onlinevideo.api.support.UserSupport;
import com.seal.onlinevideo.domain.UserMoment;
import com.seal.onlinevideo.domain.auth.UserRole;
import com.seal.onlinevideo.domain.constant.AuthRoleConstant;
import com.seal.onlinevideo.domain.exception.ConditionException;
import com.seal.onlinevideo.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class DataLimitedAspect {

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.seal.onlinevideo.domain.annotation.DataLimited)")
    public void check(){
    }

    @Before("check()")
    public void doBefore(JoinPoint joinPoint){
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        Object[] args = joinPoint.getArgs();
        for(Object arg : args){
          if(arg instanceof UserMoment){
              UserMoment userMoment = (UserMoment)arg;
              String type = userMoment.getType();
              if(roleCodeSet.contains(AuthRoleConstant.ROLE_LV1) && !"0".equals(type)){
                  throw new ConditionException("参数异常");
              }
          }
        }
    }
}
