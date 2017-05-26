package com.liberty;

/**
 * Author:LiuPen Created at 2017/5/24 15:21
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public class HttpConstants {
    /**
     * 连接超时时间 Connection time out.
     */
    public static final int CONN_TIME_OUT = 3000;//millisecond毫秒
    /**
     * 读取超时时间 Read time out.
     */
    public static final int READ_TIME_OUT = 3000;//millisecond毫秒
    /**
     * 网盘请求认证字段BDUSS TAG
     */
    public static final String NETDISK_COOKIE_TAG = "Cookie";
    /**
     * 服务器更新或者添加数据的上限
     */
    public static final int MAX_LIMIT = 100;
}
