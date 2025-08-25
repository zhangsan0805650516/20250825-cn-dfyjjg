package com.ruoyi.biz.mail;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.biz.member.mapper.FaMemberMapper;
import com.ruoyi.biz.riskConfig.domain.FaRiskConfig;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.core.domain.entity.ExportMember;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FaMemberMapper faMemberMapper;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String to, String subject, String text, FileSystemResource file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailUserList() throws Exception {
        LambdaQueryWrapper<FaRiskConfig> riskConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        riskConfigLambdaQueryWrapper.eq(FaRiskConfig::getName, "admin_email");
        riskConfigLambdaQueryWrapper.last(" limit 1 ");
        FaRiskConfig faRiskConfig = iFaRiskConfigService.getOne(riskConfigLambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRiskConfig) && StringUtils.isNotEmpty(faRiskConfig.getValue())) {
            LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
            List<FaMember> list = faMemberMapper.selectList(lambdaQueryWrapper);

            // 手机号解密
            for (FaMember faMember : list) {
                String mobile = AESUtil.decrypt(faMember.getSalt());
                faMember.setUsername(mobile);
                faMember.setMobile(mobile);
            }

            JSONArray memberArray = JSONArray.parseArray(JSON.toJSONString(list));
            List<ExportMember> memberList = JSON.parseArray(memberArray.toJSONString(), ExportMember.class);
            ExcelUtil<ExportMember> util = new ExcelUtil<>(ExportMember.class);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            FileSystemResource file = util.exportExcelFile(memberList, "会员列表", "会员列表");

            sendEmailWithAttachment(faRiskConfig.getValue(), sdf.format(new Date()) + "会员列表", "会员列表", file);
        }
    }
}
