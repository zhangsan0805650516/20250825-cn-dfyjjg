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
    public static String PROJECT_NAME = "dfyjjg";

    /**
     * RSA公钥
     */
    public static String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXmbEKK/anLykSELZqcQcFLTupFd4r0rUP5TC0xn/OLFaOPC5l2tVeonS9DGhNLmCSfmR8u3dHWRIHzc0yPngC8DVkYj4/cjgevrG5s4q3zQ0YM70KqqviVJAxpcownc58MMD94P6w18uTuz2raImFVbttlTtH7tf3JfPCZoqG8wIDAQAB";

    /**
     * RSA私钥
     */
    public static String RSA_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJeZsQor9qcvKRIQtmpxBwUtO6kV3ivStQ/lMLTGf84sVo48LmXa1V6idL0MaE0uYJJ+ZHy7d0dZEgfNzTI+eALwNWRiPj9yOB6+sbmzirfNDRgzvQqqq+JUkDGlyjCdznwwwP3g/rDXy5O7PatoiYVVu22VO0fu1/cl88JmiobzAgMBAAECgYAP5g1cDLpEVDzRaTFIPmxHDbRZCjMPk3xrm1SdUMfsflfheMxA4E9KKm1luhxiI7/6/Qha0Go08FFkwh+H6mc5dkOEG8lmVZcySU+i05qpezIAZ/TPfaW555cC2BCrWk2rGxxkJeTuPpHy6bdM2APBZuKwjYLO/rwjDIsdMXPFEQJBANF9iYu5Hy7ioE0JFwHqpNrLPASnmYT8SQpH/1fIZRMG/oMGavU2VS3GvqC/kH+fxrwlXn9su9tNSE5F7EI7o/sCQQC5QfQeFxfh/L0JoVR3PaBhFkIrFxn4wfp00kr/LD3HwIw2l/+1n051wPy/OqEsmyy3Tld8e+B1T4rfpNUddL9pAkEApCKL5iGBQSbnGfSJRkMmLxcVS0BVlFBmmduXQJwONT5Boz8gBkgvrNIPAbuV56iz+S3+QDBdb5G5mTpHxg0ciQJAbb2aBHEdnqr+uEOvK2uAI8Lxs2Dd67PMz4L7Fil492kiJfzoZwhVi/+kOQtcCnZpiLSJsp4XGqbh1xVngSl1cQJBALu2A/ufv4775ArPNvJ9yWYIVPFJDlhCMts4Hsj+O2cMHAmlEQoJ/g9E5hJATBjFnwo5qMacR50ATWMBrvYvvto=";

    /**
     * 手机号加密串
     */
    public static String AES_KEY = "PiHs1hF798FXzCuZDjDx1g==";

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
