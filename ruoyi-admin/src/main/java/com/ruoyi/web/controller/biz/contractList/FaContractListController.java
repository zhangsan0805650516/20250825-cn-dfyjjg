package com.ruoyi.web.controller.biz.contractList;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.biz.contractList.domain.FaContractList;
import com.ruoyi.biz.contractList.service.IFaContractListService;
import com.ruoyi.biz.contractTemplate.service.IFaContractTemplateService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
 * 用户合同Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Api(tags = "用户合同")
@RestController
@RequestMapping("/biz/contractList")
public class FaContractListController extends BaseController
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
    @PreAuthorize("@ss.hasPermi('biz:contractList:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaContractList faContractList)
    {
        try {
            // 合同地址
            String contractUrl = iFaRiskConfigService.getConfigValue("contract.url", null);

            if (StringUtils.isNotEmpty(faContractList.getMobile())) {
                faContractList.setSalt(AESUtil.encrypt(faContractList.getMobile()));
                faContractList.setMobile(null);
            }

            startPage();
            List<FaContractList> list = faContractListService.selectFaContractListList(faContractList);
            if (!list.isEmpty()) {
                for (FaContractList contract : list) {
                    contract.setFaContractTemplate(iFaContractTemplateService.getById(contract.getTemplateId()));
                    contract.setContractUrl(contractUrl + "/contract/info?id=" + contract.getId());
                }
            }
            return getDataTable(list);
        } catch (Exception e) {
            return getDataTable(null);
        }
    }

    /**
     * 导出用户合同列表
     */
    @ApiOperation("导出用户合同列表")
    @PreAuthorize("@ss.hasPermi('biz:contractList:export')")
    @Log(title = "用户合同", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaContractList faContractList)
    {
        List<FaContractList> list = faContractListService.selectFaContractListList(faContractList);
        ExcelUtil<FaContractList> util = new ExcelUtil<FaContractList>(FaContractList.class);
        util.exportExcel(response, list, "用户合同数据");
    }

    /**
     * 获取用户合同详细信息
     */
    @ApiOperation("获取用户合同详细信息")
    @PreAuthorize("@ss.hasPermi('biz:contractList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faContractListService.selectFaContractListById(id));
    }

    /**
     * 新增用户合同
     */
    @ApiOperation("新增用户合同")
    @PreAuthorize("@ss.hasPermi('biz:contractList:add')")
    @Log(title = "用户合同", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaContractList faContractList)
    {
        return toAjax(faContractListService.insertFaContractList(faContractList));
    }

    /**
     * 修改用户合同
     */
    @ApiOperation("修改用户合同")
    @PreAuthorize("@ss.hasPermi('biz:contractList:edit')")
    @Log(title = "用户合同", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaContractList faContractList)
    {
        return toAjax(faContractListService.updateFaContractList(faContractList));
    }

    /**
     * 删除用户合同
     */
    @ApiOperation("删除用户合同")
    @PreAuthorize("@ss.hasPermi('biz:contractList:remove')")
    @Log(title = "用户合同", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faContractListService.deleteFaContractListByIds(ids));
    }

    /**
     * 合同签名
     */
    @ApiOperation("合同签名")
    @PostMapping("/signature")
    public AjaxResult signature(FaContractList faContractList)
    {
        try {
            if (null == faContractList.getId()) {
                throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
            }

            // 合同地址
            String contractUrl = iFaRiskConfigService.getConfigValue("contract.url", null);

            LambdaUpdateWrapper<FaContractList> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaContractList::getId, faContractList.getId());
            lambdaUpdateWrapper.set(FaContractList::getImage, faContractList.getImage());
            lambdaUpdateWrapper.set(FaContractList::getSignStatus, 1);
            lambdaUpdateWrapper.set(FaContractList::getUpdateTime, new Date());
            faContractListService.update(lambdaUpdateWrapper);

            return AjaxResult.success("签名成功", contractUrl + "/contract/info?id=" + faContractList.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("签名失败");
        }
    }

}
