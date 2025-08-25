package com.ruoyi.web.controller.biz.contractList;

import com.ruoyi.biz.contractList.domain.FaContractList;
import com.ruoyi.biz.contractList.service.IFaContractListService;
import com.ruoyi.biz.contractTemplate.domain.FaContractTemplate;
import com.ruoyi.biz.contractTemplate.service.IFaContractTemplateService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;


/**
 * 用户合同Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Controller
@RequestMapping("/contract")
public class ContractInfoController extends BaseController
{
    @Autowired
    private IFaContractListService faContractListService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaContractTemplateService iFaContractTemplateService;

    /**
     * 合同详情
     */
    @ApiOperation("合同详情")
    @GetMapping("/info")
    public String hello(@RequestParam(name="id") Integer id, Model model) {
        if (null == id) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaContractList faContractList = faContractListService.getById(id);
        if (ObjectUtils.isNotEmpty(faContractList)) {
            FaContractTemplate faContractTemplate = iFaContractTemplateService.getById(faContractList.getTemplateId());
            String mainContent = faContractTemplate.getMainContent();
            mainContent = mainContent.replace("{name}", faContractList.getName());
            mainContent = mainContent.replace("{address}", faContractList.getAddress());
            mainContent = mainContent.replace("{idcard}", faContractList.getIdCard());

            // 未签约
            if (0 == faContractList.getSignStatus()) {
                mainContent = mainContent.replace("{signature}", "");
            } else if (1 == faContractList.getSignStatus()) {
                mainContent = mainContent.replace("{signature}", "<img src=\"" + faContractList.getImage() + "\" style=\"position: absolute; width: 200px;\">");
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(faContractList.getSignDate());
            mainContent = mainContent.replace("{year}", String.valueOf(calendar.get(Calendar.YEAR)));
            mainContent = mainContent.replace("{month}", String.valueOf(calendar.get(Calendar.MONTH) + 1));
            mainContent = mainContent.replace("{day}", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            faContractList.setMainContent(mainContent);
        }

        model.addAttribute("contract", faContractList);
        return "info";
    }

}
