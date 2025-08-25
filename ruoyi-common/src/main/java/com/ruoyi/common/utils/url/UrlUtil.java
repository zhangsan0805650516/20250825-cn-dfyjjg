package com.ruoyi.common.utils.url;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * sql操作工具类
 * 
 * @author ruoyi
 */
public class UrlUtil
{
    /**
     * 项目名称
     */
    public static String PROJECT_NAME = "yhhk";

    /**
     * RSA公钥
     */
    public static String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcpJ7s8l4YNnNpP/8IpSQi4gZ3P1JQxoha14JrhV7FkA+N37Sf5xcrnqDNEtIlxU3DL4oUMj7C9TO9EEtVJf9UaA9h0xS5dlwisw1SorwnaRxPy+BjmkFUDxUYOl902o4XsWnk/FucwNz03d/OFiIq+6jyr2/HwstUeFAXv4NqqQIDAQAB";

    /**
     * RSA私钥
     */
    public static String RSA_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJyknuzyXhg2c2k//wilJCLiBnc/UlDGiFrXgmuFXsWQD43ftJ/nFyueoM0S0iXFTcMvihQyPsL1M70QS1Ul/1RoD2HTFLl2XCKzDVKivCdpHE/L4GOaQVQPFRg6X3TajhexaeT8W5zA3PTd384WIir7qPKvb8fCy1R4UBe/g2qpAgMBAAECgYAiR4xYAjpBRjk/gTfIposf7yLHGd5juu7AIoVyDj/NMFeXDquyipnxe8do2YOPrHv2CQNIpC5D7/EgoN47hhM4B6J0LUpVc3kuiz0Kkdj7Rn/NmcdgYpxD9+IPonUmgqqa8/zN3Ys6GmL59xOMbdMp6yX0uwK2bz20g/52w5fH4QJBANesQ7clhbn5qEivzSoIeUYwM84EXYzkkbziyvn94+e+NP/WwCJ2qOO1/JQcWM3wBuoCqkLULDBt5jVaJyHKH5UCQQC57r25t2QKYF8iOW7JWs30LDAZC4A3MLs//ULajXWI30da79JzavBD4Cv610e0g9NY3zm8DH+6IayiOYp6sWnFAkEAzcA/0adwTh3I158vwVvYJoexxGcRwGoGRwat0ZTPKbSh5zi+j6JYOPTTA5GPftfwIKtuNtcnWQRfTKK3Fls3iQJAVCFYq631kFRm939xR57UzgUohkyT6WUddSjNlUKoyhPtn5IgcaUTrkLC46+BfIahnPb0ksg9CyWtjYHnzPh12QJAPCeS7/RmO4tfYSs3lFKnJ3v71iaofLN+wDMSDhY6kG8f1NsDEEDfJ7Zi57wdjBBZaL3mZYwny3mi9khtrW5lwA==";

    /**
     * 手机号加密串
     */
    public static String AES_KEY = "LRrn4KwYwDpZVfJAk5XOIA==";

    /**
     * 还原请求地址
     * @param path
     * @return
     */
    public static String modifyPath(String path) {
        String apiConcat = getApiSeparator();

        // 包含api，不包含api拼接的请求，忽略配置地址，其他全部拒绝
//        if (path.contains("api") && !path.contains(apiConcat)) {
//
//            String param = "/api/rechargeNotify/";
//            String[] excludedUris = param.split(",");
//
//            boolean forbid = true;
//            for (String uri : excludedUris) {
//                if (path.contains(uri)) {
//                    forbid = false;
//                    break;
//                }
//            }
//
//            if (forbid) {
//                throw new ServiceException("404", HttpStatus.NOT_FOUND);
//            }
//        }

        if(path.contains("api") && path.contains(apiConcat)){
            String result = "";
            String[] uris = path.split("/");
            for (String str : uris) {
                if (StringUtils.isNotEmpty(str)) {
                    if (str.indexOf(apiConcat) > 0) {
                        result += "/" + str.substring(0, str.indexOf(apiConcat));
                    } else {
                        result += "/" + str;
                    }
                }
            }
            path = result;
        }
        return path;
    }

    /**
     * api拼接
     * @param
     * @return
     */
    public static String getApiSeparator() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String apiConcat = AESUtil.encrypt(sdf.format(new Date()));
            apiConcat = apiConcat.replaceAll("/", "").replaceAll("\\+", "");
            apiConcat = apiConcat.substring(0, 6);
            return apiConcat;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 请求参数
     * @param
     * @return
     */
    public static String getQueryInfo() {
        try {
            String apiConcat = getApiSeparator();
            String info = AESUtil.encrypt(apiConcat);
            info = info.replaceAll("/", "").replaceAll("\\+", "");
            info = info.substring(0, 6);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2025);
        calendar.set(Calendar.MONTH, Calendar.JUNE);
        calendar.set(Calendar.DAY_OF_MONTH, 30);
        Date endDate = calendar.getTime();

        calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();

        while (startDate.compareTo(endDate) <= 0) {

            String apiConcat = AESUtil.encrypt(sdf.format(startDate));
            apiConcat = apiConcat.replaceAll("/", "").replaceAll("\\+", "");
            apiConcat = apiConcat.substring(0, 6);

            System.out.println(sdf.format(startDate) + " === " + apiConcat);
            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }
    }

}
