package com.hbfk.sdk.log.support.parse;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @Description: 解析需要存储数据的SpeEL表达式
* @Author: 61
*/
public class OperationLogValueParser implements BeanFactoryAware {

    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");
    private final OperationLogExpressionEvaluator expressionEvaluator = new OperationLogExpressionEvaluator();
    private BeanFactory beanFactory;
    private LogFunctionParser logFunctionParser;

    public Map<String, String> processTemplate(Collection<String> templates, Object ret,
                                               Class<?> targetClass, Method method, Object[] args, String errorMsg) {
        Map<String, String> expressionValues = new HashMap<>();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);

        for (String etl : templates) {
            if (etl.contains("{")) {
                // 根据 { 匹配 spEL信息
                Matcher matcher = pattern.matcher(etl);
                StringBuffer parsedStr = new StringBuffer();
                AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    String functionName = matcher.group(1);
                    // 处理spEL
                    Object value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                    // 处自定义function
                    expression = logFunctionParser.getFunctionReturnValue(value, functionName);
                    matcher.appendReplacement(parsedStr, Matcher.quoteReplacement(StrUtil.nullToEmpty(expression)));
                }
                matcher.appendTail(parsedStr);
                expressionValues.put(etl, parsedStr.toString());
            } else {
                expressionValues.put(etl, etl);
            }

        }
        return expressionValues;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setLogFunctionParser(LogFunctionParser logFunctionParser) {
        this.logFunctionParser = logFunctionParser;
    }
}
