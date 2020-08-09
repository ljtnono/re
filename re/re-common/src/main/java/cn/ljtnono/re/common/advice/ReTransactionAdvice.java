package cn.ljtnono.re.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 事务配置
 * @author ljt
 * Date: 2020/7/9 16:52 下午
 * Description:
 *
 */
@Aspect
@Slf4j
@Component
public class ReTransactionAdvice {
//
//    /** 事务超时时间 秒为单位 这里为2分钟 -1 为永不过期*/
//    private static final int AOP_TIME_OUT = 60 * 2;
//
//    /** aspect表达式 */
//    private static final String AOP_POINTCUT_EXPRESSION = "execution(public * cn.ljtnono.re.service.*.*(..)))";
//
//    private final PlatformTransactionManager transactionManager;
//
//    public ReTransactionAdvice(PlatformTransactionManager transactionManager) {
//        this.transactionManager = transactionManager;
//    }
//
//    @Bean
//    public TransactionInterceptor txAdvice(){
//        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
//
//        //只读事务，不做更新操作
//        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
//        readOnlyTx.setReadOnly(true);
//        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//
//        //当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务
//        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
//
//        //什么异常需要回滚
//        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
//        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        requiredTx.setTimeout(AOP_TIME_OUT);
//
//        Map<String, TransactionAttribute> methodMap = new HashMap<>(10);
//
//        //可以提及事务或回滚事务的方法
//        methodMap.put("add*", requiredTx);
//        methodMap.put("save*", requiredTx);
//        methodMap.put("update*", requiredTx);
//        methodMap.put("modify*", requiredTx);
//        methodMap.put("edit*", requiredTx);
//        methodMap.put("insert*", requiredTx);
//        methodMap.put("delete*", requiredTx);
//        methodMap.put("remove*", requiredTx);
//        methodMap.put("repair*", requiredTx);
//        methodMap.put("binding*", requiredTx);
//        methodMap.put("change*", requiredTx);
//        methodMap.put("initialize*", requiredTx);
//        methodMap.put("login*", requiredTx);
//        methodMap.put("register*", requiredTx);
//        methodMap.put("audit*", requiredTx);
//        //其他方法无事务，只读
//        methodMap.put("*", readOnlyTx);
//        source.setNameMap(methodMap);
//
//        return new TransactionInterceptor(transactionManager, source);
//    }
//
//    @Bean(name = "txAdviceAdvisor")
//    public Advisor txAdviceAdvisor() {
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
//        return new DefaultPointcutAdvisor(pointcut, txAdvice());
//    }

}
