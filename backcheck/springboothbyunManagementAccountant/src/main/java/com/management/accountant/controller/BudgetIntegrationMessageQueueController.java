package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetIntegrationMessageQueueService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算消息队列集成Controller
 * 
 * @description 预算消息队列集成接口，支持RabbitMQ、Kafka、RocketMQ等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-消息队列集成"})
@RequestMapping(value = "/accountant/budget/integration/mq")
@Slf4j
public class BudgetIntegrationMessageQueueController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIntegrationMessageQueueService integrationMessageQueueService;

    /**
     * 配置消息队列
     */
    @Operation(summary = "配置消息队列")
    @ApiOperation("配置消息队列")
    @PostMapping("/config")
    public MyJsonBean<Map<String, Object>> configMessageQueue(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> config = integrationMessageQueueService.configMessageQueue(params);
            result.setCode(1);
            result.setMsg("配置成功");
            result.setData(config);
        } catch (ServiceException ex) {
            log.error("配置消息队列失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("配置消息队列异常", e);
            result.setCode(0);
            result.setMsg("配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 发送消息
     */
    @Operation(summary = "发送消息")
    @ApiOperation("发送消息")
    @PostMapping("/send")
    public MyJsonBean<Map<String, Object>> sendMessage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> sendResult = integrationMessageQueueService.sendMessage(params);
            result.setCode(1);
            result.setMsg("发送成功");
            result.setData(sendResult);
        } catch (ServiceException ex) {
            log.error("发送消息失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("发送消息异常", e);
            result.setCode(0);
            result.setMsg("发送失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 接收消息
     */
    @Operation(summary = "接收消息")
    @ApiOperation("接收消息")
    @PostMapping("/receive")
    public MyJsonBean<Map<String, Object>> receiveMessage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> receiveResult = integrationMessageQueueService.receiveMessage(params);
            result.setCode(1);
            result.setMsg("接收成功");
            result.setData(receiveResult);
        } catch (ServiceException ex) {
            log.error("接收消息失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("接收消息异常", e);
            result.setCode(0);
            result.setMsg("接收失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 订阅主题
     */
    @Operation(summary = "订阅主题")
    @ApiOperation("订阅主题")
    @PostMapping("/subscribe")
    public MyJsonBean<Map<String, Object>> subscribeTopic(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> subscription = integrationMessageQueueService.subscribeTopic(params);
            result.setCode(1);
            result.setMsg("订阅成功");
            result.setData(subscription);
        } catch (ServiceException ex) {
            log.error("订阅主题失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("订阅主题异常", e);
            result.setCode(0);
            result.setMsg("订阅失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 消息确认
     */
    @Operation(summary = "消息确认")
    @ApiOperation("消息确认")
    @PostMapping("/acknowledge")
    public MyJsonBean<Map<String, Object>> acknowledgeMessage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> ackResult = integrationMessageQueueService.acknowledgeMessage(params);
            result.setCode(1);
            result.setMsg("确认成功");
            result.setData(ackResult);
        } catch (ServiceException ex) {
            log.error("消息确认失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("消息确认异常", e);
            result.setCode(0);
            result.setMsg("确认失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 死信队列处理
     */
    @Operation(summary = "死信队列处理")
    @ApiOperation("死信队列处理")
    @PostMapping("/dead-letter/handle")
    public MyJsonBean<Map<String, Object>> handleDeadLetter(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> handleResult = integrationMessageQueueService.handleDeadLetter(params);
            result.setCode(1);
            result.setMsg("处理成功");
            result.setData(handleResult);
        } catch (ServiceException ex) {
            log.error("死信队列处理失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("死信队列处理异常", e);
            result.setCode(0);
            result.setMsg("处理失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 消息监控
     */
    @Operation(summary = "消息监控")
    @ApiOperation("消息监控")
    @PostMapping("/monitor")
    public MyJsonBean<Map<String, Object>> monitorMessages(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> monitoring = integrationMessageQueueService.monitorMessages(params);
            result.setCode(1);
            result.setMsg("监控成功");
            result.setData(monitoring);
        } catch (ServiceException ex) {
            log.error("消息监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("消息监控异常", e);
            result.setCode(0);
            result.setMsg("监控失败：" + e.getMessage());
        }
        return result;
    }
}

