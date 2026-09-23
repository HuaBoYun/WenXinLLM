package com.huabo.financialdata.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 常用工具类
 *
 * @author lee
 * @version 1.0.0
 */
public class WebToolUtils {
    /**
     * 操作系统名称
     */
    private static String OS_NAME = "os.name";

    /**
     * 操作系统类型：windows
     */
    private static String OS_TYPE_WINDOWS = "windows";

    /**
     * 获取本地IP地址
     */
    public static String getLocalIp() throws UnknownHostException {
        if (isWindowsOs()) {
            return InetAddress.getLocalHost().getHostAddress();
        } else {
            return getLinuxLocalIp();
        }
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return 返回结果
     */
    public static boolean isWindowsOs() {
        boolean isWindowsOs = false;
        String osName = System.getProperty(OS_NAME);
        if (osName.toLowerCase().contains(OS_TYPE_WINDOWS)) {
            isWindowsOs = true;
        }
        return isWindowsOs;
    }

    /**
     * 获取本地Host名称
     *
     * @return hostName host名称
     * @throws UnknownHostException 未知HOST异常
     */
    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     */
    private static String getLinuxLocalIp() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println("localHostName = [" + getLocalHostName() + "]");
        System.out.println("localIp = [" + getLocalIp() + "]");
    }
}