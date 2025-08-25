package com.ruoyi.biz.common.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ruoyi.biz.common.ApiCommonService;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sysbank.domain.FaSysbank;
import com.ruoyi.biz.sysbank.service.IFaSysbankService;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.payment.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 策略Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class ApiCommonServiceImpl implements ApiCommonService
{

    private static final Logger log = LoggerFactory.getLogger(ApiCommonServiceImpl.class);

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaSysbankService iFaSysbankService;

    @Override
    public String upload(MultipartFile file, String ip) throws Exception {
        // 1:阿里云 默认2:亚马逊 3:ftp 4:本机服务器
        String ossType = iFaRiskConfigService.getConfigValue("oss.type", "2");
        if ("1".equals(ossType)) {
            return uploadAliyun(file);
        } else if ("2".equals(ossType)) {
            return uploadAmazon(file);
        } else if ("3".equals(ossType)) {
            return uploadFtp(file, ip);
        } else if ("4".equals(ossType)) {
            return uploadToServer(file);
        } else {
            throw new ServiceException("OSS type error", HttpStatus.ERROR);
        }
    }

    /**
     * 上传到本机服务器
     * @param file
     * @return
     * @throws Exception
     */
    private String uploadToServer(MultipartFile file) throws Exception {
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        String url = "http://" + IpUtils.getPublicIp() + fileName;
        return url;
    }

    /**
     * 上传到ftp
     * @param file
     * @return
     * @throws Exception
     */
    private String uploadFtp(MultipartFile file, String ip) throws Exception {
        // 格式校验
        String fileNameCheck = file.getOriginalFilename();
        if (fileNameCheck == null || !fileNameCheck.contains(".")) {
            throw new ServiceException("file name error", HttpStatus.ERROR);
        }
        String extension = fileNameCheck.substring(fileNameCheck.lastIndexOf(".") + 1).toLowerCase();
        if (!"jpg".equals(extension) && !"jpeg".equals(extension) && !"png".equals(extension)) {
            throw new ServiceException("file format error", HttpStatus.ERROR);
        }

        String host = iFaRiskConfigService.getConfigValue("ftp.host", null);
        String port = iFaRiskConfigService.getConfigValue("ftp.port", null);
        String username = iFaRiskConfigService.getConfigValue("ftp.username", null);
        String password = iFaRiskConfigService.getConfigValue("ftp.password", null);
        String remotePath = iFaRiskConfigService.getConfigValue("ftp.remotePath", null);

        // 是否使用域名
        String useDomain = iFaRiskConfigService.getConfigValue("ftp.use.domain", "0");
        // 域名地址
        String domainUrl = iFaRiskConfigService.getConfigValue("ftp.domain.url", null);

        String fileFullPath = remotePath + FileUploadUtils.extractFilename(file);

        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(port) || StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password) || StringUtils.isEmpty(remotePath)) {
            throw new ServiceException("ftp config error", HttpStatus.ERROR);
        }

        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        String fileName = null;
        try {
            ftpClient.connect(host, Integer.parseInt(port));
            ftpClient.login(username, password);
            // 被动模式
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            int reply = ftpClient.getReplyCode();
            log.error("ftp连接成功：" + reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return null;
            }

            fileName = fileFullPath.substring(fileFullPath.lastIndexOf("/") + 1);
            String filePath = fileFullPath.substring(0, fileFullPath.lastIndexOf("/"));

            // 创建目录
            String basePath = "/";
            for (String p : filePath.split("/")) {
                basePath += (p + "/");
                boolean hasPath = ftpClient.changeWorkingDirectory(basePath);
                if (!hasPath) {
                    // 创建目录，一次只能创建一个目录
                    ftpClient.makeDirectory(basePath);
                }
            }

            ftpClient.changeWorkingDirectory(filePath);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean flag = ftpClient.storeFile(fileName, file.getInputStream());
            log.error("ftp上传结果：" + flag);
        } catch (SocketException e) {
            log.error("ftp 连接失败", e);
        } catch (IOException e) {
            log.error("ftp 上传失败", e);
        } finally {
            try {
                if (file.getInputStream() != null) {
                    file.getInputStream().close();
                }
                ftpClient.logout();
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 使用域名
        if ("1".equals(useDomain)) {
            return domainUrl + "/ftp" + fileFullPath;
        }
        // 用用本机ip
        else {
            return "http://" + ip + "/ftp" + fileFullPath;
        }
    }

    /**
     * 上传到阿里云OSS
     * @param file
     * @return
     * @throws Exception
     */
    private String uploadAliyun(MultipartFile file) throws Exception {
        String endpoint = iFaRiskConfigService.getConfigValue("aliyunoss.endpoint", null);
        String accessKeyId = iFaRiskConfigService.getConfigValue("aliyunoss.accessKeyId", null);
        String accessKeySecret = iFaRiskConfigService.getConfigValue("aliyunoss.accessKeySecret", null);
        String bucketName = iFaRiskConfigService.getConfigValue("aliyunoss.bucketName", null);
        String filehost = iFaRiskConfigService.getConfigValue("aliyunoss.filehost", null);
        String url = iFaRiskConfigService.getConfigValue("aliyunoss.url", null);

        if (StringUtils.isEmpty(endpoint) || StringUtils.isEmpty(accessKeyId) || StringUtils.isEmpty(accessKeySecret) ||
                StringUtils.isEmpty(bucketName) || StringUtils.isEmpty(filehost) || StringUtils.isEmpty(url)) {
            throw new ServiceException("Aliyun OSS config error", HttpStatus.ERROR);
        }

        // 生成 OSSClient
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 原始文件名称
        // String originalFilename = file.getOriginalFilename();

        // 编码文件名
        String filePathName = FileUploadUtils.extractFilename(file);
        // 文件路径名称
        filePathName = filehost + "/" + filePathName;
        try {
            ossClient.putObject(bucketName, filePathName, file.getInputStream());
        } catch (IOException e) {
            log.error("uploadOSS", e);
            throw new ServiceException("OSS error", HttpStatus.ERROR);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        String result =  url + "/" + filePathName;
        return result;
    }

    /**
     * 上传到亚马逊s3
     * @param file
     * @return
     * @throws Exception
     */
    private String uploadAmazon(MultipartFile file) throws Exception {
        String domain = iFaRiskConfigService.getConfigValue("amazon.domain", null);
        String accessKey = iFaRiskConfigService.getConfigValue("amazon.accessKey", null);
        String secretKey = iFaRiskConfigService.getConfigValue("amazon.secretKey", null);
        String bucketName = iFaRiskConfigService.getConfigValue("amazon.bucketName", null);
        String regionName = iFaRiskConfigService.getConfigValue("amazon.regionName", null);

        if (StringUtils.isEmpty(domain) || StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(secretKey) ||
                StringUtils.isEmpty(bucketName) || StringUtils.isEmpty(regionName)) {
            throw new ServiceException("Amazon OSS config error", HttpStatus.ERROR);
        }

        String filePath = FileUploadUtils.extractFilename(file);

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(regionName))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        try {
            File uploadFile = convert(file);
            s3client.putObject(new PutObjectRequest(bucketName, filePath, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));

            if (uploadFile.exists()) {
                uploadFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domain.concat("/" + filePath);
    }

    /**
     * 文件格式转换
     * @param file
     * @return
     * @throws IOException
     */
    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        try (InputStream in = file.getInputStream();
             OutputStream out = new FileOutputStream(convFile)) {
            byte[] bytes = new byte[1024];
            int read;
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            return convFile;
        }
    }

    @Override
    public String getPayment(FaMember faMember, FaRecharge faRecharge) throws Exception {
        FaSysbank faSysbank = iFaSysbankService.getById(faRecharge.getSysbankid());
        if (ObjectUtils.isEmpty(faSysbank)) {
            throw new ServiceException("payment config error", HttpStatus.ERROR);
        }

        try {
            switch (faSysbank.getPaymentType()) {
                case 0: // 重构
                    return getPaymentFromConfig(faMember, faRecharge, faSysbank);
                case 1: // 九哥
                    return getPaymentFromNineBrother(faSysbank, faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
                case 2: // 仁德
                    return getPaymentFromRenDe(faSysbank, faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
                case 3: // 火箭
                    return getPaymentFromHuoJian(faSysbank, faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
                case 4: // 四方
                    return getPaymentFromSiFang(faSysbank, faMember.getId(), faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
                case 5: // 代付
                    return getPaymentFromDaiFu(faSysbank, faRecharge.getOrderId(), faRecharge.getMoney());
                case 6: // FML
                    return getPaymentFromFML(faSysbank, faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
                case 7: // 亨通
                    return getPaymentFromHengTong(faSysbank, faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
                case 8: // 衫德宝
                    return getPaymentFromShanDeBao(faSysbank, faRecharge.getOrderId(), faRecharge.getMoney());
                case 9: // XL
                    return getPaymentFromXL(faSysbank, faMember.getId(), faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
                case 10: // 鲨鱼
                    return getPaymentFromShaYu(faSysbank, faRecharge.getOrderId(), faRecharge.getMoney());
                case 11: // AX
                    return getPaymentFromAx(faSysbank, faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
                case 12: // 海贼
                    return getPaymentFromHaiZei(faSysbank, faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
                case 13: // 鸿运
                    return getPaymentFromHongYun(faSysbank, faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney(), faMember.getId());
                case 14: // 中信
                    return getPaymentFromZhongXin(faSysbank, faRecharge.getOrderId(), faRecharge.getMoney(), IpUtils.getIpAddr());
                case 15: // 伟翰
                    return getPaymentFromWeiHan(faSysbank, faRecharge.getOrderId(), faRecharge.getMoney());
                case 16: // 飞云
                    return getPaymentFromFeiYun(faSysbank, faRecharge.getOrderId(), faRecharge.getMoney());
                default:
                    throw new ServiceException("payment type error", HttpStatus.ERROR);
            }
        } catch (Exception e) {
            if (StringUtils.isNotEmpty(faSysbank.getPaymentAppId())) {
                throw new ServiceException(faSysbank.getPaymentAppId(), HttpStatus.ERROR);
            } else {
                throw new ServiceException(e.getMessage(), HttpStatus.ERROR);
            }
        }
    }

    /**
     * 四方支付
     * @param memberId
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromSiFang(FaSysbank faSysbank, Integer memberId, String orderId, Date createTime, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String pay_memberid = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_memberid");
        String Md5key = getValueFromConfig(faSysbank.getPaymentGateway(), "Md5key");
        String pay_bankcode = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_bankcode");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(pay_memberid) || StringUtils.isEmpty(Md5key) ||
                StringUtils.isEmpty(pay_bankcode) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("sifang payment config error", HttpStatus.ERROR);
        }

        String result = SiFangPaymentUtil.getPaymentUrl(gateway, memberId, pay_memberid, Md5key, pay_bankcode, orderId, createTime, money, notifyUrl);
        return result;
    }

    /**
     * 九哥大区
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromNineBrother(FaSysbank faSysbank, String orderId, Date createTime, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String merchNo = getValueFromConfig(faSysbank.getPaymentGateway(), "merchNo");
        String appId = getValueFromConfig(faSysbank.getPaymentGateway(), "appId");
        String apiKey = getValueFromConfig(faSysbank.getPaymentGateway(), "apiKey");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(merchNo) || StringUtils.isEmpty(appId) ||
                StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("9brother payment config error", HttpStatus.ERROR);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = NineBrotherPaymentUtil.getPaymentUrl(gateway, merchNo, appId, apiKey, orderId, sdf.format(createTime), money, notifyUrl);
        return result;
    }

    private String getValueFromConfig(String config, String key) throws Exception {
        if (StringUtils.isNotEmpty(config)) {
            JSONObject object = JSONObject.parseObject(config);
            if (ObjectUtils.isNotEmpty(object) && object.containsKey(key)) {
                return object.getString(key);
            }
        }
        return null;
    }

    /**
     * 仁德支付
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromRenDe(FaSysbank faSysbank, String orderId, Date createTime, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String apiKey = getValueFromConfig(faSysbank.getPaymentGateway(), "apiKey");
        String apiSecret = getValueFromConfig(faSysbank.getPaymentGateway(), "apiSecret");
        String gateId = getValueFromConfig(faSysbank.getPaymentGateway(), "gateId");
        String action = getValueFromConfig(faSysbank.getPaymentGateway(), "action");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(apiSecret) ||
                StringUtils.isEmpty(gateId) || StringUtils.isEmpty(action) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("rende payment config error", HttpStatus.ERROR);
        }

        String result = RendePaymentUtil.getPaymentUrl(gateway, apiKey, apiSecret, gateId, action, orderId, createTime, money, notifyUrl);
        return result;
    }

    /**
     * 火箭支付
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromHuoJian(FaSysbank faSysbank, String orderId, Date createTime, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String appId = getValueFromConfig(faSysbank.getPaymentGateway(), "appId");
        String apiKey = getValueFromConfig(faSysbank.getPaymentGateway(), "apiKey");
        String typId = getValueFromConfig(faSysbank.getPaymentGateway(), "typId");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(apiKey) ||
                StringUtils.isEmpty(typId) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("huojian payment config error", HttpStatus.ERROR);
        }

        String result = HuoJianPaymentUtil.getPaymentUrl(gateway, appId, apiKey, Integer.parseInt(typId), orderId, createTime, money, notifyUrl);
        return result;
    }

    /**
     * 代付支付
     * @param orderId
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromDaiFu(FaSysbank faSysbank, String orderId, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String paymentMemberId = getValueFromConfig(faSysbank.getPaymentGateway(), "paymentMemberId");
        String paymentMd5Key = getValueFromConfig(faSysbank.getPaymentGateway(), "paymentMd5Key");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");
        String frontReturnUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "frontReturnUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(paymentMemberId) || StringUtils.isEmpty(paymentMd5Key) ||
                StringUtils.isEmpty(notifyUrl) || StringUtils.isEmpty(frontReturnUrl)) {
            throw new ServiceException("daifu payment config error", HttpStatus.ERROR);
        }

        String result = DaiFuPaymentUtil.getPaymentUrl(gateway, paymentMemberId, paymentMd5Key, orderId, money, notifyUrl, frontReturnUrl);
        return result;
    }

    /**
     * FML支付
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromFML(FaSysbank faSysbank, String orderId, Date createTime, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String pay_memberid = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_memberid");
        String Md5key = getValueFromConfig(faSysbank.getPaymentGateway(), "Md5key");
        String pay_bankcode = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_bankcode");
        String payCallbackurl = getValueFromConfig(faSysbank.getPaymentGateway(), "payCallbackurl");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(pay_memberid) || StringUtils.isEmpty(Md5key) ||
                StringUtils.isEmpty(pay_bankcode) || StringUtils.isEmpty(payCallbackurl) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("sifang payment config error", HttpStatus.ERROR);
        }

        String result = FMLPaymentUtil.getPaymentUrl(gateway, pay_memberid, Md5key, pay_bankcode, payCallbackurl, orderId, createTime, money, notifyUrl);
        return result;
    }

    /**
     * 亨通支付
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromHengTong(FaSysbank faSysbank, String orderId, Date createTime, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String pay_memberid = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_memberid");
        String paymentAppId = getValueFromConfig(faSysbank.getPaymentGateway(), "paymentAppId");
        String Md5key = getValueFromConfig(faSysbank.getPaymentGateway(), "Md5key");
        String pay_bankcode = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_bankcode");
        String payCallbackurl = getValueFromConfig(faSysbank.getPaymentGateway(), "payCallbackurl");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(pay_memberid) || StringUtils.isEmpty(paymentAppId) ||
                StringUtils.isEmpty(Md5key) || StringUtils.isEmpty(pay_bankcode) || StringUtils.isEmpty(payCallbackurl) ||
                StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("hengtong payment config error", HttpStatus.ERROR);
        }

        String result = HengTongPaymentUtil.getPaymentUrl(gateway, pay_memberid, paymentAppId, Md5key, pay_bankcode, payCallbackurl, orderId, createTime, money, notifyUrl);
        return result;
    }

    /**
     * 衫德宝支付
     * @param orderId
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromShanDeBao(FaSysbank faSysbank, String orderId, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String pay_memberid = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_memberid");
        String Md5key = getValueFromConfig(faSysbank.getPaymentGateway(), "Md5key");
        String pay_bankcode = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_bankcode");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(pay_memberid) || StringUtils.isEmpty(Md5key) ||
                StringUtils.isEmpty(pay_bankcode) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("shandebao payment config error", HttpStatus.ERROR);
        }

        String result = ShanDeBaoPaymentUtil.getPaymentUrl(gateway, pay_memberid, Md5key, pay_bankcode, orderId, money, notifyUrl);
        return result;
    }

    /**
     * XL支付
     * @param memberId
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromXL(FaSysbank faSysbank, Integer memberId, String orderId, Date createTime, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String pay_memberid = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_memberid");
        String paymentAppId = getValueFromConfig(faSysbank.getPaymentGateway(), "paymentAppId");
        String Md5key = getValueFromConfig(faSysbank.getPaymentGateway(), "Md5key");
        String pay_bankcode = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_bankcode");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(pay_memberid) || StringUtils.isEmpty(paymentAppId) ||
                StringUtils.isEmpty(Md5key) || StringUtils.isEmpty(pay_bankcode) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("XL payment config error", HttpStatus.ERROR);
        }

        String result = XlPaymentUtil.getPaymentUrl(gateway, memberId, pay_memberid, paymentAppId, Md5key, pay_bankcode, orderId, createTime, money, notifyUrl);
        return result;
    }

    /**
     * 鲨鱼支付
     * @param orderId
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromShaYu(FaSysbank faSysbank, String orderId, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String pay_memberid = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_memberid");
        String Md5key = getValueFromConfig(faSysbank.getPaymentGateway(), "Md5key");
        String pay_bankcode = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_bankcode");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(pay_memberid) || StringUtils.isEmpty(Md5key) ||
                StringUtils.isEmpty(pay_bankcode) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("shayu payment config error", HttpStatus.ERROR);
        }

        String result = ShaYuPaymentUtil.getPaymentUrl(gateway, pay_memberid, Md5key, pay_bankcode, orderId, money, notifyUrl);
        return result;
    }

    /**
     * AX支付
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromAx(FaSysbank faSysbank, String orderId, Date createTime, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String app_id = getValueFromConfig(faSysbank.getPaymentGateway(), "app_id");
        String product_id = getValueFromConfig(faSysbank.getPaymentGateway(), "product_id");
        String secret = getValueFromConfig(faSysbank.getPaymentGateway(), "secret");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(app_id) || StringUtils.isEmpty(product_id) ||
                StringUtils.isEmpty(secret) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("AX payment config error", HttpStatus.ERROR);
        }

        String result = AXPaymentUtil.getPaymentUrl(gateway, app_id, Integer.parseInt(product_id), secret, orderId, createTime.getTime() / 1000, money, notifyUrl);
        return result;
    }

    /**
     * AX支付
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromHaiZei(FaSysbank faSysbank, String orderId, Date createTime, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String pay_memberid = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_memberid");
        String Md5key = getValueFromConfig(faSysbank.getPaymentGateway(), "Md5key");
        String pay_bankcode = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_bankcode");
        String pay_callbackurl = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_callbackurl");
        String pay_notifyurl = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_notifyurl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(pay_memberid) || StringUtils.isEmpty(Md5key) ||
                StringUtils.isEmpty(pay_bankcode) || StringUtils.isEmpty(pay_callbackurl) || StringUtils.isEmpty(pay_notifyurl)) {
            throw new ServiceException("haizei payment config error", HttpStatus.ERROR);
        }

        String result = HaiZeiPaymentUtil.getPaymentUrl(gateway, pay_memberid, Md5key, pay_bankcode, pay_callbackurl, orderId, createTime, money, pay_notifyurl);
        return result;
    }

    /**
     * 鸿运支付
     * @param orderId
     * @param createTime
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromHongYun(FaSysbank faSysbank, String orderId, Date createTime, BigDecimal money, Integer id) throws Exception {
        String GATEWAY = getValueFromConfig(faSysbank.getPaymentGateway(), "GATEWAY");
        String APIKEY = getValueFromConfig(faSysbank.getPaymentGateway(), "APIKEY");
        String ACCOUNT_ID = getValueFromConfig(faSysbank.getPaymentGateway(), "ACCOUNT_ID");
        String CONTENT_TYPE = getValueFromConfig(faSysbank.getPaymentGateway(), "CONTENT_TYPE");
        String THOROUGHFARE = getValueFromConfig(faSysbank.getPaymentGateway(), "THOROUGHFARE");
        String CALLBACK_URL = getValueFromConfig(faSysbank.getPaymentGateway(), "CALLBACK_URL");

        if (StringUtils.isEmpty(GATEWAY) || StringUtils.isEmpty(APIKEY) || StringUtils.isEmpty(ACCOUNT_ID) ||
                StringUtils.isEmpty(CONTENT_TYPE) || StringUtils.isEmpty(THOROUGHFARE) || StringUtils.isEmpty(CALLBACK_URL)) {
            throw new ServiceException("hongyun payment config error", HttpStatus.ERROR);
        }

        String result = HongYunPaymentUtil.getPaymentUrl(GATEWAY, APIKEY, ACCOUNT_ID, CONTENT_TYPE, THOROUGHFARE, orderId, money, CALLBACK_URL, createTime.getTime(), "1.1.1.1", "other", id);
        return result;
    }

    /**
     * 中信支付
     * @param orderId
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromZhongXin(FaSysbank faSysbank, String orderId, BigDecimal money, String memberIp) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String merchantId = getValueFromConfig(faSysbank.getPaymentGateway(), "merchantId");
        String productId = getValueFromConfig(faSysbank.getPaymentGateway(), "productId");
        String key = getValueFromConfig(faSysbank.getPaymentGateway(), "key");
        String callbackUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "callbackUrl");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(merchantId) || StringUtils.isEmpty(productId) ||
                StringUtils.isEmpty(key) || StringUtils.isEmpty(callbackUrl)) {
            throw new ServiceException("zhongxin payment config error", HttpStatus.ERROR);
        }

        String result = ZhongXinPaymentUtil.getPaymentUrl(gateway, Long.valueOf(merchantId), Integer.parseInt(productId), key, orderId, money, memberIp, callbackUrl);
        return result;
    }

    /**
     * 伟翰支付
     * @param orderId
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromWeiHan(FaSysbank faSysbank, String orderId, BigDecimal money) throws Exception {
        String GATEWAY = getValueFromConfig(faSysbank.getPaymentGateway(), "GATEWAY");
        String pay_memberid = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_memberid");
        String Md5key = getValueFromConfig(faSysbank.getPaymentGateway(), "Md5key");
        String pay_bankcode = getValueFromConfig(faSysbank.getPaymentGateway(), "pay_bankcode");
        String notifyUrl = getValueFromConfig(faSysbank.getPaymentGateway(), "notifyUrl");

        if (StringUtils.isEmpty(GATEWAY) || StringUtils.isEmpty(pay_memberid) || StringUtils.isEmpty(Md5key) ||
                StringUtils.isEmpty(pay_bankcode) || StringUtils.isEmpty(notifyUrl)) {
            throw new ServiceException("weihan payment config error", HttpStatus.ERROR);
        }

        String result = WeiHanPaymentUtil.getPaymentUrl(GATEWAY, pay_memberid, Md5key, pay_bankcode, orderId, money, notifyUrl);
        return result;
    }

    /**
     * 飞云支付
     * @param orderId
     * @param money
     * @return
     * @throws Exception
     */
    private String getPaymentFromFeiYun(FaSysbank faSysbank, String orderId, BigDecimal money) throws Exception {
        String gateway = getValueFromConfig(faSysbank.getPaymentGateway(), "gateway");
        String key = getValueFromConfig(faSysbank.getPaymentGateway(), "key");
        String subtype = getValueFromConfig(faSysbank.getPaymentGateway(), "subtype");
        String format = getValueFromConfig(faSysbank.getPaymentGateway(), "format");
        String url = getValueFromConfig(faSysbank.getPaymentGateway(), "url");

        if (StringUtils.isEmpty(gateway) || StringUtils.isEmpty(key) || StringUtils.isEmpty(subtype) ||
                StringUtils.isEmpty(format) || StringUtils.isEmpty(url)) {
            throw new ServiceException("feiyun payment config error", HttpStatus.ERROR);
        }

        String result = FeiYunPaymentUtil.getPaymentUrl(gateway, key, subtype, format, url, orderId, money);
        return result;
    }

    /**
     * 从配置中拼出请求
     * @param faMember
     * @param faRecharge
     * @param faSysbank
     * @return
     * @throws Exception
     */
    private String getPaymentFromConfig(FaMember faMember, FaRecharge faRecharge, FaSysbank faSysbank) throws Exception {
        String resultUrl = "";
        // 取出配置
        String config = faSysbank.getPaymentGateway();
        if (StringUtils.isNotEmpty(config)) {
            JSONObject jsonObject = JSONObject.parseObject(config);
            if (ObjectUtils.isNotEmpty(jsonObject)) {
                // 参数集合
                SortedMap<String, Object> params = new TreeMap<>();
                params = getConfigParams(jsonObject, faMember, faRecharge, faSysbank);

                String stringSignTemp = "";
                // 排序参数字符串 字典序或者固定序
                if (jsonObject.containsKey("paramSort")) {
                    String paramSort = getConfigKey(jsonObject, "paramSort");
                    // 字典序
                    if ("ASCII".equalsIgnoreCase(paramSort)) {
                        stringSignTemp = getSignTemp(params);
                    }
                    // 固定序
                    else if ("fixed".equalsIgnoreCase(paramSort)) {
                        String sort = getConfigValue(jsonObject, "paramSort");
                        stringSignTemp = getFixedSignTemp(params, sort);
                    }
                }
                // 字典序
                else {
                    stringSignTemp = getSignTemp(params);
                }

                // 签名
                String sign = getConfigSign(jsonObject, stringSignTemp);
                String signKey = getConfigKey(jsonObject, "sign");
                params.put(signKey, sign);

                log.error("getPaymentParams=" + JSONUtil.toJsonStr(params));

                // 请求结果
                String result = getConfigResult(jsonObject, params);

                log.error("getPaymentResult=" + result);

                // 支付地址
                resultUrl = getConfigResultUrl(jsonObject, result);
            }
        }
        return resultUrl;
    }

    private SortedMap<String, Object> getConfigParams(JSONObject jsonObject, FaMember faMember, FaRecharge faRecharge, FaSysbank faSysbank) {
        SortedMap<String, Object> params = new TreeMap<>();
        try {
            // 金额
            String amountKey = getConfigKey(jsonObject, "amount");
            // 单位
            String amountValue = getConfigValue(jsonObject, "amount");
            // 元
            if (StringUtils.equalsIgnoreCase("yuan", amountValue)) {
                params.put(amountKey, faRecharge.getMoney().setScale(2, RoundingMode.HALF_UP).toString());
            }
            // 分
            else if (StringUtils.equalsIgnoreCase("fen", amountValue)) {
                params.put(amountKey, faRecharge.getMoney().setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).intValue());
            }

            // 订单号
            String orderNoKey = getConfigKey(jsonObject, "orderNo");
            params.put(orderNoKey, faRecharge.getOrderId());
            // 时间，可能没有
            if (jsonObject.containsKey("time")) {
                String timeKey = getConfigKey(jsonObject, "time");
                // 1:格式化 2:时间戳 3:时间戳/1000
                String timeValue = getConfigValue(jsonObject, "time");
                switch (timeValue) {
                    case "1":
                        JSONObject timeObject = jsonObject.getJSONObject("time");
                        String format = timeObject.getString("format");
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        params.put(timeKey, sdf.format(faRecharge.getCreateTime()));
                        break;
                    case "2":
                        params.put(timeKey, faRecharge.getCreateTime().getTime());
                        break;
                    case "3":
                        params.put(timeKey, faRecharge.getCreateTime().getTime() / 1000);
                        break;
                    default:
                        throw new ServiceException("payment config error", HttpStatus.ERROR);
                }
            }
            // 用户id，可能没有
            if (jsonObject.containsKey("member")) {
                String memberIdKey = getConfigKey(jsonObject, "member");
                params.put(memberIdKey, faMember.getId());
            }
            // 用户ip，可能没有
            if (jsonObject.containsKey("ip")) {
                String ipKey = getConfigKey(jsonObject, "ip");
                params.put(ipKey, IpUtils.getIpAddr());
            }
            // 其他参数
            if (jsonObject.containsKey("queryInfo")) {
                JSONObject queryObject = jsonObject.getJSONObject("queryInfo");
                for (String key : queryObject.keySet()) {
                    Object value = queryObject.get(key);
                    params.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("payment config error", HttpStatus.ERROR);
        }
        return params;
    }

    /**
     * 参数排序
     * @param params
     * @return
     * @throws Exception
     */
    private static String getSignTemp(Map<String, Object> params) throws Exception {
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        List<String> values = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String k = String.valueOf(entry.getKey());
            String v = String.valueOf(entry.getValue());
            if (StringUtils.isNotBlank(k) && StringUtils.isNotBlank(v)) {
                values.add(k + "=" + v);
            }
        }
        String result = StringUtils.join(values, "&");
        return result;
    }

    /**
     * 参数排序 固定序
     * @param params
     * @return
     * @throws Exception
     */
    private static String getFixedSignTemp(Map<String, Object> params, String sort) throws Exception {
        String[] paramsList = sort.split("=");
        List<String> values = new ArrayList<>();
        for (int i = 0; i < paramsList.length; i++) {
            String k = paramsList[i];
            String v = String.valueOf(params.get(k));
            if (StringUtils.isNotBlank(k) && StringUtils.isNotBlank(v)) {
                values.add(k + "=" + v);
            }
        }
        String result = StringUtils.join(values, "&");
        return result;
    }

    private String getConfigSign(JSONObject jsonObject, String stringSignTemp) {
        // 加密串字段
        String md5Key = getConfigKey(jsonObject, "md5");

        // 加密串
        String md5Value = getConfigValue(jsonObject, "md5");
        if (jsonObject.containsKey("md5Suffix")) {
            String suffix = getConfigValue(jsonObject, "md5Suffix");
            if (StringUtils.isNotEmpty(suffix)) {
                md5Value += suffix;
            }
        }

        if (StringUtils.isEmpty(md5Key)) {
            stringSignTemp = stringSignTemp + md5Value;
        } else {
            stringSignTemp = stringSignTemp + "&" + md5Key + "=" + md5Value;
        }

        log.error("stringSignTemp=" + stringSignTemp);

        String signValue = getConfigValue(jsonObject, "sign");
        String sign = DigestUtils.md5DigestAsHex(stringSignTemp.getBytes());

        if (StringUtils.equalsIgnoreCase(signValue, "UpperCase")) {
            sign = sign.toUpperCase();
        } else if (StringUtils.equalsIgnoreCase(signValue, "LowerCase")) {
            sign = sign.toLowerCase();
        }

        return sign;
    }

    private String getConfigKey(JSONObject jsonObject, String key) {
        try {
            jsonObject = jsonObject.getJSONObject(key);
            return jsonObject.getString("key");
        } catch (Exception e) {
            throw new ServiceException("payment config error", HttpStatus.ERROR);
        }
    }

    private String getConfigValue(JSONObject jsonObject, String key) {
        try {
            jsonObject = jsonObject.getJSONObject(key);
            return jsonObject.getString("value");
        } catch (Exception e) {
            throw new ServiceException("payment config error", HttpStatus.ERROR);
        }
    }

    private String getConfigResult(JSONObject jsonObject, Map<String, Object> params) throws Exception {
        // 网关
        String gatewayValue = getConfigValue(jsonObject, "gateway");

        JSONObject httpObject = jsonObject.getJSONObject("httpMethod");
        String contentType = httpObject.getString("contentType");

        // 请求结果
        String result = "";
        if (StringUtils.equalsIgnoreCase(contentType, "json")) {
            log.error("getConfigResult.params=" + JSONUtil.toJsonStr(params));
            result = HttpUtils.sendPostForPayment(gatewayValue, JSONUtil.toJsonStr(params));
        } else if (StringUtils.equalsIgnoreCase(contentType, "form")) {
            log.error("getConfigResult.params=" + getSignTemp(params));
            result = HttpUtils.sendPostFormPayment(gatewayValue, getSignTemp(params));
        }
        log.error("getConfigResult.result=" + result);
        return result;
    }

    private String getConfigResultUrl(JSONObject configObject, String result) throws Exception {
        String resultUrl = "";
        if (StringUtils.isNotEmpty(result)) {
            // 结果配置
            JSONObject resultInfo = configObject.getJSONObject("resultInfo");
            String statusKey = resultInfo.getString("statusKey");
            String statusValue = resultInfo.getString("statusValue");
            String urlKey = resultInfo.getString("urlKey");
            String msgKey = resultInfo.getString("msgKey");

            // 结果对象
            JSONObject resultObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(resultObject) && resultObject.containsKey(statusKey) && StringUtils.equalsIgnoreCase(resultObject.getString(statusKey), statusValue)) {
                String[] urlPaths = urlKey.split("#");
                for (int i = 0; i < urlPaths.length; i++) {
                    if (i == urlPaths.length - 1) {
                        return resultObject.getString(urlPaths[i]);
                    } else {
                        resultObject = resultObject.getJSONObject(urlPaths[i]);
                    }
                }
            } else if (ObjectUtils.isNotEmpty(resultObject) && resultObject.containsKey(msgKey)) {
                throw new ServiceException(resultObject.getString(msgKey), HttpStatus.ERROR);
            }
        }
        return resultUrl;
    }

}