package com.ruoyi.web.controller.api.contractList;

import com.ruoyi.biz.contractList.domain.FaContractList;
import com.ruoyi.biz.contractList.service.IFaContractListService;
import com.ruoyi.biz.contractTemplate.service.IFaContractTemplateService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户合同Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Api(tags = "用户合同")
@RestController
@RequestMapping("/api/contractList")
public class ContractListController extends BaseController
{
    @Autowired
    private IFaContractListService faContractListService;

    @Autowired
    private IFaContractTemplateService iFaContractTemplateService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    /**
     * 查询用户合同列表
     */
    @ApiOperation("查询用户合同列表")
    @AppLog(title = "查询用户合同列表", businessType = BusinessType.OTHER)
    @PostMapping("/getContractList")
    public AjaxResult getContractList(HttpServletRequest request)
    {
        try {
            // 合同地址
            String contractUrl = iFaRiskConfigService.getConfigValue("contract.url", null);

            LoginMember loginMember = getLoginMember();
            List<FaContractList> list = faContractListService.getContractList(loginMember.getFaMember().getId());

            if (!list.isEmpty()) {
                for (FaContractList contract : list) {
                    contract.setFaContractTemplate(iFaContractTemplateService.getById(contract.getTemplateId()));
                    contract.setContractUrl(contractUrl + "/contract/info?id=" + contract.getId());
                }
            }

            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("getContractList", e);
            return AjaxResult.error();
        }
    }

}
