package com.ruoyi.common.utils.google;


import com.ruoyi.common.utils.url.UrlUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleUtil {

    private static final Logger log = LoggerFactory.getLogger(GoogleUtil.class);

    // 发行者（项目名），可为空，注：不允许包含冒号
//    public static final String ISSUER = "yhhk";

    // 生成的key长度( Generate secret key length)
    public static final int SECRET_SIZE = 32;

    // Java实现随机数算法
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";

    // 最多可偏移的时间, 假设为2，表示计算前面2次、当前时间、后面2次，共5个时间内的验证码
    static int window_size = 1; // max 17
    static long second_per_size = 30L;// 每次时间长度，默认30秒

    /**
     * 生成一个SecretKey，外部绑定到用户
     *
     * @return SecretKey
     */
    public static String generateSecretKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        // 生成一个密钥
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secretKey = key.getKey();
        return secretKey;
    }

    /**
     * 生成二维码所需的字符串，注：这个format不可修改，否则会导致身份验证器无法识别二维码
     *
     * @param user   绑定到的用户名
     * @param secret 对应的secretKey
     * @return 二维码字符串
     */
    public static String getQRBarcode(String user, String secret) {
        if (UrlUtil.PROJECT_NAME != null) {
            if (UrlUtil.PROJECT_NAME.contains(":")) {
                throw new IllegalArgumentException("Issuer cannot contain the ':' character.");
            }
            user = UrlUtil.PROJECT_NAME + ":" + user;
        }
        String format = "otpauth://totp/%s?secret=%s";
        String ret = String.format(format, user, secret);
        if (UrlUtil.PROJECT_NAME != null) {
            ret += "&issuer=" + UrlUtil.PROJECT_NAME;
        }
        return ret;
    }

    /**
     * 验证用户提交的code是否匹配
     *
     * @param secret 用户绑定的secretKey
     * @param code   用户输入的code
     * @return 匹配成功与否
     */
    public static boolean checkCode(String secret, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        // 验证用户输入的验证码
        boolean isCodeValid = gAuth.authorize(secret, code);

        if (isCodeValid) {
            return true;
        } else {
            return false;
        }
    }

}
