package com.huabo.financialdata.config.redis.tracer;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huabo.financialdata.config.id.IdFactory;
import com.huabo.financialdata.config.redis.serialize.JSONSerializer;
import com.huabo.financialdata.config.redis.serialize.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lee
 * @version 1.0.0
 */
public class ThreadLocalProcessTracer {
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalProcessTracer.class);

    private static final String DATA_FERRY_HEADER = "X-DATA-FERRY";

    private static final Logger parameterLogger = LoggerFactory.getLogger(ThreadLocalProcessTracer.class.getName() + ".parameter");

    private static JSONSerializer serializer = new JSONSerializer();

    /**
     * 请求跟踪ID格式:
     * ${服务名}-${实例ID}-${GUID}-|${stack}|:${order}
     * Since 2.2.3, order is deprecated
     * Since 2.2.3, GUID格式不再是GUID策略前会加上日期戳
     */
    private static final Pattern TRACE_ID_PATTERN = Pattern.compile("(.*\\-\\d{4,5}\\-\\d{16})$|(.*\\-\\d{4,5}\\-\\d{16}):(\\d+)$|(.*\\-\\d{4,5}\\-\\d{16,26})\\-\\|(.*)\\|:(\\d+)$|(.*\\-\\d{4,5}\\-\\d{26})$");

    //TODO memory leak testing
    private static final ThreadLocal<ThreadLocalProcessTracer> tracerThreadLocal = new ThreadLocal<ThreadLocalProcessTracer>();
    //最大processes数量
    private static final int maxProcessContextCount = 50;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @JsonIgnore
    private String rawTraceId;

    private String originalTraceId;

    private String traceId;

    @JsonIgnore
    private boolean disableLog = false;

    @JsonIgnore
    private long startTime;

    private long startTimeMillis;

    private String cost = "NaN";

    private String startTimeStr;

    private Map<String, Object> attributes;

    private List<ProcessContext> processes;

    @JsonIgnore
    private String errorStack;

    @JsonIgnore
    private Object parameters;

    private static final String CHAINED_CONTEXT_LINK = "->";

    private static String appName;
    private static String appId;
    private static String appIdentifier;

    static {
        appName = System.getProperty("spring.application.name");
        appId = System.getProperty("spring.application.index");
        if (StringUtils.hasText(appName)) {
            appIdentifier = appName;
            if (StringUtils.hasText(appId)) {
                appIdentifier = appIdentifier + "-" + appId;
            }
        }
    }

    public ThreadLocalProcessTracer() {
        this(createTraceId());
    }

    public ThreadLocalProcessTracer(String traceId) {
        this.traceId = traceId;
        this.startTime = System.nanoTime();
        this.startTimeMillis = System.currentTimeMillis();
        this.processes = new ArrayList();
        this.attributes = new HashMap();
        this.originalTraceId = traceId;
    }

    public static ThreadLocalProcessTracer get() {
        ThreadLocalProcessTracer tracer = tracerThreadLocal.get();
        if (tracer == null) {
            tracer = new ThreadLocalProcessTracer();
            put(tracer);
        }
        tracer.ensureProcessContextCount();
        return tracer;
    }

    public static String getThreadLocalTraceId() {
        ThreadLocalProcessTracer tracer = tracerThreadLocal.get();
        if (tracer != null) {
            return tracer.getRawTraceId();
        }
        return null;
    }

    public static ThreadLocalProcessTracer get(String traceId) {
        traceId = normalizeTraceID(traceId);
        ThreadLocalProcessTracer tracer = tracerThreadLocal.get();
        if (tracer == null) {
            tracer = new ThreadLocalProcessTracer(traceId);
            put(tracer);
        } else {
            tracer.setTraceId(traceId);
            tracer.originalTraceId = traceId;
        }
        tracer.ensureProcessContextCount();
        return tracer;
    }

    /**
     * 在没有被clean的情况下，保障内存不会泄露
     */
    private void ensureProcessContextCount() {
        if (processes != null && processes.size() >= maxProcessContextCount) {
            processes.clear();
            logger.warn("清理ProcessContext,数量:{}", processes.size());
        }
    }

    public static ThreadLocalProcessTracer getNextStack(String traceId) {
        ThreadLocalProcessTracer tracer = get(traceId);
        tracer.originalTraceId = tracer.pushTraceIdStack();
        return tracer;
    }

    private static String createTraceId() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmm");
        String traceId = appIdentifier + '-' + format.format(new Date()) + IdFactory.generateId();
        return normalizeTraceID(traceId);
    }

    private static void put(ThreadLocalProcessTracer httpRequestTracer) {
        tracerThreadLocal.set(httpRequestTracer);
    }

    public static void clean() {
        tracerThreadLocal.remove();
    }

    public ProcessContext join(ProcessContext.Type type, String name) {
        String chainedName = name;
        //找到上一个未关闭的context, 作为它的子context(通过$parentProcessName.$processName识别)
        ProcessContext lastOpened = this.findLastOpenedProcessContext();
        if (lastOpened != null) {
            chainedName = lastOpened.getName() + CHAINED_CONTEXT_LINK + name;
        }

        ProcessContext ctx = new ProcessContext(type, chainedName);
        this.processes.add(ctx);
        return ctx;
    }

    private ProcessContext findLastOpenedProcessContext() {
        if (CollectionUtils.isEmpty(this.processes)) {
            return null;
        }

        for (int i = this.processes.size() - 1; i >= 0; i--) {
            ProcessContext ctx = this.processes.get(i);
            if (ctx.isOpen()) {
                return ctx;
            }
        }
        return null;
    }

    public String getStartTimeStr() {
        return simpleDateFormat.format(this.startTimeMillis);
    }

    public void beginTrace() {
    }

    public void stopTrace() {
        for (ProcessContext ctx : this.processes) {
            //如果没停止,则停止它
            ctx.stop();
        }
        long stopTime = System.nanoTime();
        this.cost = new DecimalFormat("0.00").format((stopTime - this.startTime) / 1000000.0d) + "ms";
    }

    @Override
    public String toString() {
        try {
            byte[] b = serializer.serialize(this);
            return new String(b);
        } catch (SerializeException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    public void setAttribute(String name, Object value) {
        ProcessContext ctx = this.findLastOpenedProcessContext();
        if (ctx == null) {
            this.doSetAttribute(name, value);
        } else {
            ctx.setAttribute(name, value);
        }
    }

    public boolean hasAttribute(String name) {
        return this.attributes.containsKey(name);
    }

    public void setRootAttribute(String name, Object value) {
        this.doSetAttribute(name, value);
    }

    public Object getRootAttribute(String name) {
        return this.attributes.get(name);
    }

    private void doSetAttribute(String name, Object value) {
        if (value == null) {
            this.attributes.remove(name);
        } else {
            if (this.attributes.containsKey(name)) {
                logger.warn("duplicated tracer attribute [" + name + "]");
            }
            this.attributes.put(name, value);
        }
    }

    public ThreadLocalProcessTracer attr(String name, Object value) {
        this.setAttribute(name, value);
        return this;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public List<ProcessContext> getProcesses() {
        return processes;
    }

    public String getTraceId() {
        return traceId;
    }

    @JsonIgnore
    public String getTraceIdAndIncreaseOrder() {
        String traceId = this.traceId;
        nextOrder();
        return traceId;
    }

    public String getOriginalTraceId() {
        return this.originalTraceId;
    }

    public String getRawTraceId() {
        if (this.rawTraceId == null) {
            TraceIDInfo traceIDInfo = parseTraceIdInfo(this.traceId);
            this.rawTraceId = traceIDInfo.raw;
        }
        return this.rawTraceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getCost() {
        return this.cost;
    }

    public void log(String flag) {
        if (!this.disableLog) {
            if (logger.isInfoEnabled()) {
                logger.info("[{}] -> {}", flag, this.toString());
                if (parameterLogger.isInfoEnabled()) {
                    parameterLogger.info("[{}][{}] -> {}", flag + " PARAM", this.originalTraceId, parameter2JSON());
                }
            }
        }
    }

    public String getErrorStack() {
        return errorStack;
    }

    public void setErrorStack(String errorStack) {
        this.errorStack = errorStack;
    }

    public void setError(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        this.errorStack = sw.toString();
        try {
            sw.close();
        } catch (IOException e) {
            ;//handled
        }
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    @JsonIgnore
    public String parameter2JSON() {
        if (this.parameters != null) {
//            return serializer.serialize2JSON(this.parameters);
            return JSON.toJSONString(this.parameters);
        }
        return "{}";
    }

    public void disableThisTraceLog() {
        this.disableLog = true;
    }

    @JsonIgnore
    public boolean isLogDisabled() {
        return disableLog;
    }

    //add by zlbbq, 2017-09-21, 从HEADER中解析出Ferry数据, 设置到attributes
    public void parse(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            if (isFerriedAttribute(header)) {
                this.attr(header, request.getHeader(header));
            }
        }
    }

    public static String getAppId() {
        return appIdentifier;
    }

    //标准化TraceID
    private static String normalizeTraceID(String traceId) {
        TraceIDInfo traceIDInfo = parseTraceIdInfo(traceId);
        return traceIDInfo.normalize();
    }

    public static String parseRawTraceId(String traceId) {
        TraceIDInfo traceIDInfo = parseTraceIdInfo(traceId);
        return traceIDInfo.raw;
    }

    public static TraceIDInfo parseTraceIdInfo(String traceId) {
        TraceIDInfo traceIDInfo = new TraceIDInfo();
        Matcher matcher = TRACE_ID_PATTERN.matcher(traceId);
        if (matcher.matches()) {
            String raw = matcher.group(1);
            if (raw == null) {
                raw = matcher.group(2);
                traceIDInfo.order = matcher.group(3);
            }
            if (raw == null) {
                raw = matcher.group(4);
                traceIDInfo.stack = matcher.group(5);
                traceIDInfo.order = matcher.group(6);
            }
            if (raw == null) {
                raw = matcher.group(7);
            }
            traceIDInfo.raw = raw;
            return traceIDInfo;
        }
        throw new IllegalArgumentException("错误的TraceID格式: " + traceId);
    }

    /**
     * TraceId parts: ${application.name}-${application.index}-${uid}|${stack-info}|:${order}
     */
    private void nextOrder() {
        TraceIDInfo traceIDInfo = parseTraceIdInfo(this.traceId);
        this.setTraceId(traceIDInfo.nextOrder());
    }

    private String pushTraceIdStack() {
        TraceIDInfo traceIDInfo = parseTraceIdInfo(this.traceId);
        this.setTraceId(traceIDInfo.pushStack());
        return this.getTraceId();
    }

    @JsonIgnore
    public Map<String, Object> getAttributesWillBeFerried() {
        if (this.attributes == null) {
            return null;
        }
        Map<String, Object> attrs = this.attributes;
        for (String key : attrs.keySet()) {
            if (isFerriedAttribute(key)) {
                attrs.put(key, this.attributes.get(key));
            }
        }

        return attrs;
    }


    private static boolean isFerriedAttribute(String key) {
        if (key.toUpperCase().startsWith(DATA_FERRY_HEADER)) {
            return true;
        }
        return false;
    }

    public static String MAKE_ATTRIBUTE_FERRIABLE(String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("key has no content");
        }
        return DATA_FERRY_HEADER + "-" + key;
    }

    public static class TraceIDInfo {
        String raw;

        String stack;

        /**
         * 此值将永远保持为1, 为兼容, 暂不去除
         */
        String order;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getStack() {
            return stack;
        }

        public void setStack(String stack) {
            this.stack = stack;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getCompatibleOrderOrStack() {
            if (this.stack != null) {
                return this.stack;
            }

            if (this.order != null) {
                return this.order;
            }

            return "1";
        }

        String getStackNotNull() {
            return this.stack == null ? "1" : this.stack;
        }

        String getOrderNotNull() {
            return this.order == null ? "1" : this.order;
        }

        /**
         * Feign调用时, 序号会因为线程摆渡的问题+1, 这里需要强制-1
         */
        private void decreasePreviousStackOrder() {
            String s = getStackNotNull();
            String[] stackParts = s.split("\\-");
            int lastIdx = stackParts.length - 1;
            stackParts[lastIdx] = String.valueOf(Integer.parseInt(stackParts[lastIdx]) - 1);
            this.stack = StringUtils.arrayToDelimitedString(stackParts, "-");
        }

        public String nextOrder() {
            String s = getStackNotNull();
            String[] stackParts = s.split("\\-");
            int lastIdx = stackParts.length - 1;
            stackParts[lastIdx] = String.valueOf(Integer.parseInt(stackParts[lastIdx]) + 1);
            this.stack = StringUtils.arrayToDelimitedString(stackParts, "-");
            return this.normalize();
        }

        public String pushStack() {
            decreasePreviousStackOrder();
            StringBuffer sb = new StringBuffer();
            sb.append(this.raw).append("-|");
            if (this.stack != null) {
                sb.append(this.getStackNotNull()).append("-1");
            } else {
                sb.append("1");
            }
            sb.append("|:").append(this.getOrderNotNull());
            return sb.toString();
        }

        public String normalize() {
            StringBuffer sb = new StringBuffer();
            sb.append(this.raw).append("-|").append(this.getStackNotNull()).append("|:").append(this.getOrderNotNull());
            return sb.toString();
        }
    }
}
