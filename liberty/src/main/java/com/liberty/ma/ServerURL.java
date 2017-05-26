package com.liberty.ma;

public class ServerURL {
    private static final String PROTOCOL_HTTP = "http://";
    //正式环境
    private static String HOST = "api.gumpcome.com";
    //备用环境
    private static String HOST_SPARE = "120.76.46.229:8010/svmservice";
    private static String PAYHOST_SPARE = "120.76.46.229:8010/svmpay";
    private static String SCANLOGINHOST_SPARE = "120.76.46.229:8012/svmmswechat";
    private static String LOGINPARAMHOST_SPARE = "120.76.46.229:8011/wechatwap";
    private static String SDKPAYHOST_SPARE = "120.76.46.232:8023/svmpay-sdkhttp-java";


    static {
        HOST = "api.gumpcome.net";//开发环境{
        HOST = "api.gumphealth.net";//测试环境

    }

    //Config配置
    private static final String CONFIG_URL = PROTOCOL_HTTP + HOST + "/svm_iot/v3/config/get_latest";
    //获取IOT密钥
    private static final String IOT_KEY = PROTOCOL_HTTP + HOST + "/svm_iot/v3/iot_key";
    //心跳
    private static final String HEARTBEAT_URL = PROTOCOL_HTTP + HOST + "/svm_iot/v3/heartbeat/push";
    //创建订单
    private static final String ORDER_CREATE_URL = PROTOCOL_HTTP + HOST + "/sale/createOrder";
    //获取二维码
    private static final String ALIQRCODE_URL = PROTOCOL_HTTP + HOST + "/alipay/getQrcode";//支付宝二维码
    private static final String WECHATQRCODE_URL = PROTOCOL_HTTP + HOST + "/wechatpay/getQrcode";//微信二维码
    private static final String NICELIFEQRCODE_URL = PROTOCOL_HTTP + HOST + "/cshpay/getQrcode";//采之云二维码
    private static final String JDPAY_URL = PROTOCOL_HTTP + HOST + "/jdpay/getQrcode";  //京东支付二维码
    //获取支付状态
    private static final String ALISTATUS_URL = PROTOCOL_HTTP + HOST + "/alipay/getPayStatus";//支付宝支付状态
    private static final String WECHATSTATUS_URL = PROTOCOL_HTTP + HOST + "/wechatpay/getPayStatus";//微信支付状态
    private static final String JDSTATUS_URL = PROTOCOL_HTTP + HOST + "/jdpay/getPayStatus";  //京东支付的状态
    private static final String NICELIFESTATUS_URL = PROTOCOL_HTTP + HOST + "/cshpay/getPayStatus";//采之云支付状态
    //登陆
    private static final String LOGIN_URL = PROTOCOL_HTTP + HOST + "/svm_iot/v3/login/passwd";
    private static final String SCANLOGIN_URL = PROTOCOL_HTTP + HOST + "/svm_iot/v3/config/get_latest";
    //取货码
    private static final String CHECKGOODS = PROTOCOL_HTTP + HOST + "/code";
    //现金回收
    private static final String CASHRECYCLE_URL = PROTOCOL_HTTP + HOST + "/svm_iot/v3/cash/recycle";
    //流量上报
    private static final String UPLOAD_TRAFFIC_URL = PROTOCOL_HTTP + HOST + "/svm_iot/v3/report/traffic";
    //广告时长统计
    private static final String UPLOAD_ADS_DURATION = PROTOCOL_HTTP + HOST + "/svm_iot/v3/report/ad_duration";

    //白名单
    private static final String SVMWHITEBOOK_URL = PROTOCOL_HTTP + HOST + "/svm_iot/v3/white_list";
    //补货记录
    private static final String UPLOAD_RESTOCK_URL = PROTOCOL_HTTP + HOST + "/svm_iot/v3/report/replenish/";
    //售货机状态
    private static final String SETSVMSTATUS_URL = PROTOCOL_HTTP + HOST + "/svmstatus";   //售货机状态


    private static final String SVMSTATEQUERY_URL = PROTOCOL_HTTP + HOST + "/svmstatequery";  //售货机状态查询
    private static final String NEWEST_CLIENT_INFO = PROTOCOL_HTTP + HOST + "/svmcheck/checkClient";//获取服务器最新客户端信息

    //第三方API地址
    private static final String WEATHER_INFO_URL = "http://apis.baidu.com/heweather/weather/free";//天气预报查询地址
    private static final String UNIONPAY_SCAN_URL = "https://222.240.185.180:18081/uphnspf/client/json";//银联扫码

    //支付SDK地址
    private static final String SDK_QRCODEALI_URL = PROTOCOL_HTTP + HOST + "/v1/sdk/ali/order/createqrcode";//支付SDK支付宝二维码获取地址
    private static final String SDK_QRCODEWECHAT_URL = PROTOCOL_HTTP + HOST + "/v1/sdk/wx/order/createqrcode";//支付SDK微信二维码获取地址
    private static final String SDK_QRCODEJD_URL = PROTOCOL_HTTP + HOST + "/v1/sdk/jd/order/createqrcode";//支付SDK京东支付二维码获取地址
    private static final String SDK_WECHAT_PAYSTATUS_URL = PROTOCOL_HTTP + HOST + "/v1/sdk/wx/order/paystatus";//支付SDK微信支付结果获取地址
    private static final String SDK_ALI_PAYSTATUS_URL = PROTOCOL_HTTP + HOST + "/v1/sdk/ali/order/paystatus";//支付SDK支付宝支付结果获取地址
    private static final String SDK_JD_PAYSTATUS_URL = PROTOCOL_HTTP + HOST + "/v1/sdk/jd/order/paystatus";//支付SDK京东支付结果获取地址
    //<-- 附加功能地址 -->
    //表情大咖秀卡券地址
    private static final String FACE_CARD_URL = "http://face.card.gumpcome.com";

    ////////////////=备用   防止DNS解析失败=//////////////
    /**
     * 缩略图服务    config /v1/sdk/ali/order/createqrcode
     **/
    //Config配置
    private static final String CONFIG_URL_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/vm_iot/v3/config/latest";
    //获取IOT密钥
    private static final String IOT_KEY_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svm_iot/v3/iot_key";
    //心跳
    private static final String HEARTBBEAT_URL_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svm_iot/v3/heartbeat/push";
    //创建订单
    private static final String ORDER_CREATE_URL_SPARE = PROTOCOL_HTTP + PAYHOST_SPARE + "/sale/createOrder";
    //获取二维码
    private static final String ALIQRCODE_URL_SPARE = PROTOCOL_HTTP + PAYHOST_SPARE + "/alipay/getQrcode";//支付宝二维码
    private static final String WECHATQRCODE_URL_SPARE = PROTOCOL_HTTP + PAYHOST_SPARE + "/wechatpay/getQrcode";//微信二维码
    private static final String JDPAY_URL_SPARE = PROTOCOL_HTTP + PAYHOST_SPARE + "/jdpay/getQrcode";  //京东支付二维码
    private static final String NICELIFEQRCODE_URL_SPARE = PROTOCOL_HTTP + PAYHOST_SPARE + "/cshpay/getQrcode";//采之云二维码
    //获取支付状态
    private static final String ALISTATUS_URL_SPARE = PROTOCOL_HTTP + PAYHOST_SPARE + "/alipay/getPayStatus";//支付宝支付状态
    private static final String WECHATSTATUS_URL_SPARE = PROTOCOL_HTTP + PAYHOST_SPARE + "/wechatpay/getPayStatus";//微信支付状态
    private static final String JDSTATUS_URL_SPARE = PROTOCOL_HTTP + PAYHOST_SPARE + "/jdpay/getPayStatus";  //京东支付的状态
    private static final String NICELIFESTATUS_URL_SPARE = PROTOCOL_HTTP + PAYHOST_SPARE + "/cshpay/getPayStatus";//采之云支付状态
    //登陆
    private static final String LOGIN_URL_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svm_iot/v3/login/passwd";
    private static final String SCANLOGIN_URL_SPARE = PROTOCOL_HTTP + SCANLOGINHOST_SPARE + "/svm_iot/v3/login/scan";
    //取货码
    private static final String CHECKGOODS_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/code";
    //现金回收
    private static final String CASHRECYCLE_URL_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svm_iot/v3/cash/recycle)";
    //流量上报
    private static final String UPLOAD_TRAFFIC_URL_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svm_iot/v3/traffic_report";
    //广告时长统计
    private static final String UPLOAD_ADS_DURATION_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svm_iot/v3/ad_duration";
    //白名单
    private static final String SVMWHITEBOOK_URL_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svm_iot/v3/white_list";
    //补货记录
    private static final String UPLOAD_RESTOCK_URL_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svm_iot/v3/add_goods";
    //售货机状态
    //    private static final String SETSVMSTATUS_URL_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svmstatus";   //售货机状态
    //    private static final String SVMSTATEQUERY_URL_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svmstatequery";  //售货机状态查询
    private static final String NEWEST_CLIENT_INFO_SPARE = PROTOCOL_HTTP + HOST_SPARE + "/svmcheck/checkClient";//获取服务器最新客户端信息
    //    //第三方API地址
//    private static final String WEATHER_INFO_URL = "http://apis.baidu.com/heweather/weather/free";//天气预报查询地址
    //支付SDK地址
    private static final String SDK_QRCODEALI_URL_SPARE = PROTOCOL_HTTP + SDKPAYHOST_SPARE + "/v1/sdk/ali/order/createqrcode";//支付SDK支付宝二维码获取地址
    private static final String SDK_QRCODEWECHAT_URL_SPARE = PROTOCOL_HTTP + SDKPAYHOST_SPARE + "/v1/sdk/wx/order/createqrcode";//支付SDK微信二维码获取地址
    private static final String SDK_QRCODEJD_URL_SPARE = PROTOCOL_HTTP + SDKPAYHOST_SPARE + "/v1/sdk/jd/order/createqrcode";//支付SDK京东支付二维码获取地址
    private static final String SDK_WECHAT_PAYSTATUS_URL_SPARE = PROTOCOL_HTTP + SDKPAYHOST_SPARE + "/v1/sdk/wx/order/paystatus";//支付SDK微信支付结果获取地址
    private static final String SDK_ALI_PAYSTATUS_URL_SPARE = PROTOCOL_HTTP + SDKPAYHOST_SPARE + "/v1/sdk/ali/order/paystatus";//支付SDK支付宝支付结果获取地址
    private static final String SDK_JD_PAYSTATUS_URL_SPARE = PROTOCOL_HTTP + SDKPAYHOST_SPARE + "/v1/sdk/jd/order/paystatus";//支付SDK京东支付结果获取地址

    public static String getConfigUrl() {
        return "http://svmservice.gumpcome.com/whitebook";
    }

    public static String getBaseUrl() {
        return "http://svmservice.gumpcome.com/";
    }
}
