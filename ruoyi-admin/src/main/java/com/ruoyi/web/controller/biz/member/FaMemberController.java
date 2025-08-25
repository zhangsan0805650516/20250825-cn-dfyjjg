package com.ruoyi.web.controller.biz.member;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.biz.member.domain.ExportBankInfo;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.ExportMember;
import com.ruoyi.common.core.domain.entity.ExportMemberWithMobile;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ase.AESUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员管理Controller
 * 
 * @author ruoyi
 * @date 2024-01-03
 */
@Api(tags = "会员管理")
@RestController
@RequestMapping("/biz/member")
public class FaMemberController extends BaseController
{
    @Autowired
    private IFaMemberService faMemberService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    /**
     * 查询会员管理列表
     */
    @ApiOperation("查询会员管理列表")
    @PreAuthorize("@ss.hasPermi('biz:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaMember faMember)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if (null != faMember.getDailiId()) {
            faMember.setParentId(faMember.getDailiId());
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faMember.setParentId(1L);
            } else {
                faMember.setParentId(loginUser.getUserId());
            }
        }
        List<FaMember> list = faMemberService.selectFaMemberList(faMember);
        return getDataTable(list);
    }

    /**
     * 查询会员实名认证列表
     */
    @ApiOperation("查询会员实名认证列表")
    @PreAuthorize("@ss.hasPermi('biz:authMember:list')")
    @GetMapping("/authMemberList")
    public TableDataInfo authMemberList(FaMember faMember)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if (null != faMember.getDailiId()) {
            faMember.setParentId(faMember.getDailiId());
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faMember.setParentId(1L);
            } else {
                faMember.setParentId(loginUser.getUserId());
            }
        }
        List<FaMember> list = faMemberService.authMemberList(faMember);
        return getDataTable(list);
    }

    /**
     * 查询会员绑卡列表
     */
    @ApiOperation("查询会员绑卡列表")
    @PreAuthorize("@ss.hasPermi('biz:memberBank:list')")
    @GetMapping("/memberBankList")
    public TableDataInfo memberBankList(FaMember faMember)
    {
        startPage();
        LoginUser loginUser = getLoginUser();
        if (null != faMember.getDailiId()) {
            faMember.setParentId(faMember.getDailiId());
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faMember.setParentId(1L);
            } else {
                faMember.setParentId(loginUser.getUserId());
            }
        }
        List<FaMember> list = faMemberService.memberBankList(faMember);
        return getDataTable(list);
    }

    /**
     * 导出会员管理列表
     */
    @ApiOperation("导出会员管理列表")
    @PreAuthorize("@ss.hasPermi('biz:member:export')")
    @Log(title = "会员管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaMember faMember)
    {
        LoginUser loginUser = getLoginUser();
        if (null != faMember.getDailiId()) {
            faMember.setParentId(faMember.getDailiId());
        } else {
            if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                faMember.setParentId(1L);
            } else {
                faMember.setParentId(loginUser.getUserId());
            }
        }

        try {
            // 手机号解密开关，默认关
            String mobile_decrypt = iFaRiskConfigService.getConfigValue("mobile_decrypt", "0");
            faMember.setMobileDecrypt(Integer.parseInt(mobile_decrypt));
            List<FaMember> list = faMemberService.selectFaMemberListForExport(faMember);

//            // 手机号解密开关，默认关
//            String mobile_decrypt = iFaRiskConfigService.getConfigValue("mobile_decrypt", "0");
//            if ("1".equalsIgnoreCase(mobile_decrypt)) {
//                for (FaMember member : list) {
//                    String mobile = AESUtil.decrypt(member.getSalt());
//                    member.setUsername(mobile);
//                    member.setMobile(mobile);
//                }
//            }

            JSONArray memberArray = JSONArray.parseArray(JSON.toJSONString(list));

            // 是否显示手机号，默认不显示
            String show_mobile = iFaRiskConfigService.getConfigValue("show_mobile", "0");
            if ("0".equals(show_mobile)) {
                List<ExportMember> memberList = JSON.parseArray(memberArray.toJSONString(), ExportMember.class);
                ExcelUtil<ExportMember> util = new ExcelUtil<>(ExportMember.class);
                util.exportExcel(response, memberList, "会员管理");
            } else if ("1".equals(show_mobile)) {
                List<ExportMemberWithMobile> memberList = JSON.parseArray(memberArray.toJSONString(), ExportMemberWithMobile.class);
                ExcelUtil<ExportMemberWithMobile> util = new ExcelUtil<>(ExportMemberWithMobile.class);
                util.exportExcel(response, memberList, "会员管理");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取会员管理详细信息
     */
    @ApiOperation("获取会员管理详细信息")
    @PreAuthorize("@ss.hasPermi('biz:member:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        try {
            return success(faMemberService.getById(id));
        } catch (ServiceException e) {
            logger.error("memberDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("memberDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 新增会员管理
     */
    @ApiOperation("新增会员管理")
    @PreAuthorize("@ss.hasPermi('biz:member:add')")
    @Log(title = "会员管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaMember faMember)
    {
        try {
            faMemberService.insertFaMember(faMember);
        } catch (ServiceException e) {
            logger.error("add", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("add", e);
            return AjaxResult.error();
        }
        return toAjax(1);
    }

    /**
     * 修改会员管理
     */
    @ApiOperation("修改会员管理")
    @PreAuthorize("@ss.hasPermi('biz:member:edit')")
    @Log(title = "会员管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaMember faMember)
    {
        return toAjax(faMemberService.updateFaMember(faMember));
    }

    /**
     * 删除会员管理
     */
    @ApiOperation("删除会员管理")
    @PreAuthorize("@ss.hasPermi('biz:member:remove')")
    @Log(title = "会员管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faMemberService.deleteFaMemberByIds(ids));
    }

    /**
     * 删除实名认证
     */
    @ApiOperation("删除实名认证")
    @PreAuthorize("@ss.hasPermi('biz:member:remove')")
    @Log(title = "删除实名认证", businessType = BusinessType.DELETE)
    @DeleteMapping("/delAuthMember/{ids}")
    public AjaxResult delAuthMember(@PathVariable Integer[] ids)
    {
        return toAjax(faMemberService.delAuthMemberByIds(ids));
    }

    /**
     * 删除绑卡
     */
    @ApiOperation("删除绑卡")
    @PreAuthorize("@ss.hasPermi('biz:member:remove')")
    @Log(title = "删除绑卡", businessType = BusinessType.DELETE)
    @DeleteMapping("/delMemberBank/{ids}")
    public AjaxResult delMemberBank(@PathVariable Integer[] ids)
    {
        return toAjax(faMemberService.delMemberBankByIds(ids));
    }

    /**
     * 修改会员状态
     */
    @ApiOperation("修改会员状态")
    @PreAuthorize("@ss.hasPermi('biz:member:changeMemberStatus')")
    @Log(title = "修改会员状态", businessType = BusinessType.UPDATE)
    @PostMapping("/changeMemberStatus")
    public AjaxResult changeMemberStatus(@RequestBody FaMember faMember)
    {
        try {
            return faMemberService.changeMemberStatus(faMember);
        } catch (ServiceException e) {
            logger.error("changeMemberStatus", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("changeMemberStatus", e);
            return AjaxResult.error();
        }
    }

    /**
     * 提交实名认证
     */
    @ApiOperation("提交实名认证")
    @PreAuthorize("@ss.hasPermi('biz:member:authMember')")
    @Log(title = "提交实名认证", businessType = BusinessType.UPDATE)
    @PostMapping("/submitAuthMember")
    public AjaxResult submitAuthMember(@RequestBody FaMember faMember)
    {
        try {
            faMemberService.submitAuthMember(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("submitAuthMember", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("submitAuthMember", e);
            return AjaxResult.error();
        }
    }

    /**
     * 提交绑定银行卡
     */
    @ApiOperation("提交绑定银行卡")
    @PreAuthorize("@ss.hasPermi('biz:member:bindingBank')")
    @Log(title = "提交绑定银行卡", businessType = BusinessType.UPDATE)
    @PostMapping("/submitBindingBank")
    public AjaxResult submitBindingBank(@RequestBody FaMember faMember)
    {
        try {
            faMemberService.submitBindingBank(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("submitBindingBank", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("submitBindingBank", e);
            return AjaxResult.error();
        }
    }

    /**
     * 搜索用户
     */
    @ApiOperation("搜索用户")
    @Log(title = "搜索用户", businessType = BusinessType.OTHER)
    @PostMapping("/searchMember")
    public AjaxResult searchMember(@RequestBody FaMember faMember)
    {
        try {
            List<FaMember> faMemberList = faMemberService.searchMember(faMember);
            return AjaxResult.success(faMemberList);
        } catch (ServiceException e) {
            logger.error("searchMember", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("searchMember", e);
            return AjaxResult.error();
        }
    }

    /**
     * 提交充值
     */
    @RepeatSubmit
    @ApiOperation("提交充值")
    @PreAuthorize("@ss.hasPermi('biz:member:recharge')")
    @Log(title = "提交充值", businessType = BusinessType.UPDATE)
    @PostMapping("/submitRecharge")
    public AjaxResult submitRecharge(@RequestBody FaMember faMember)
    {
        try {
            faMemberService.submitRecharge(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("submitRecharge", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("submitRecharge", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改余额
     */
    @ApiOperation("修改余额")
    @PreAuthorize("@ss.hasPermi('biz:member:updateBalance')")
    @Log(title = "修改余额", businessType = BusinessType.UPDATE)
    @PostMapping("/submitUpdateBalance")
    public AjaxResult submitUpdateBalance(@RequestBody FaMember faMember)
    {
        try {
            faMemberService.submitUpdateBalance(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("submitUpdateBalance", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("submitUpdateBalance", e);
            return AjaxResult.error();
        }
    }

    /**
     * 修改T+1锁定转入转出
     */
    @ApiOperation("修改T+1锁定转入转出")
    @PreAuthorize("@ss.hasPermi('biz:member:updateFreezeProfit')")
    @Log(title = "修改T+1锁定转入转出", businessType = BusinessType.UPDATE)
    @PostMapping("/submitUpdateFreezeProfit")
    public AjaxResult submitUpdateFreezeProfit(@RequestBody FaMember faMember)
    {
        try {
            faMemberService.submitUpdateFreezeProfit(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("submitUpdateFreezeProfit", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("submitUpdateFreezeProfit", e);
            return AjaxResult.error();
        }
    }

    /**
     * 用户统计
     */
    @ApiOperation("用户统计")
    @Log(title = "用户统计", businessType = BusinessType.OTHER)
    @PostMapping("/getMemberStatistics")
    public AjaxResult getMemberStatistics(@RequestBody FaMember faMember)
    {
        try {
            LoginUser loginUser = getLoginUser();
            if (null != faMember.getDailiId()) {
                faMember.setParentId(faMember.getDailiId());
            } else {
                if ("admin2".equals(loginUser.getUser().getRoles().get(0).getRoleKey())) {
                    faMember.setParentId(1L);
                } else {
                    faMember.setParentId(loginUser.getUserId());
                }
            }

            faMember.setDeleteFlag(0);

            if (StringUtils.isNotEmpty(faMember.getMobile())) {
                faMember.setSalt(AESUtil.encrypt(faMember.getMobile()));
                faMember.setMobile(null);
            }

            // 总余额
            BigDecimal totalBalance = faMemberService.getTotalBalance(faMember);
            // 总充值
            BigDecimal totalRecharge = faMemberService.getTotalRecharge(faMember);
            // 总提现
            BigDecimal totalWithdraw = faMemberService.getTotalWithdraw(faMember);
            // 新股申购冻结
            BigDecimal totalSg = faMemberService.getTotalSg(faMember);
            // 新股配售冻结
            BigDecimal totalPs = faMemberService.getTotalPs(faMember);
            // 上市持仓市值
            BigDecimal totalListed = faMemberService.getTotalListed(faMember);
            // 未上市持仓市值
            BigDecimal totalUnlisted = faMemberService.getTotalUnlisted(faMember);

            Map<String, BigDecimal> map = new HashMap<>();
            map.put("totalBalance", totalBalance);
            map.put("totalRecharge", totalRecharge);
            map.put("totalWithdraw", totalWithdraw);
            map.put("totalSg", totalSg);
            map.put("totalPs", totalPs);
            map.put("totalListed", totalListed);
            map.put("totalUnlisted", totalUnlisted);

            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("MemberStatistics", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("MemberStatistics", e);
            return AjaxResult.error();
        }
    }

    /**
     * 单个用户统计
     */
    @ApiOperation("单个用户统计")
    @Log(title = "单个用户统计", businessType = BusinessType.OTHER)
    @PostMapping("/getMemberStatisticsSingle")
    public AjaxResult getMemberStatisticsSingle(@RequestBody FaMember faMember)
    {
        try {
            Map<String, BigDecimal> map = faMemberService.getMemberStatisticsSingle(faMember);
            return AjaxResult.success(map);
        } catch (ServiceException e) {
            logger.error("MemberStatistics", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("MemberStatistics", e);
            return AjaxResult.error();
        }
    }

    /**
     * 批量审核身份认证
     */
    @ApiOperation("批量审核身份认证")
    @Log(title = "批量审核身份认证", businessType = BusinessType.UPDATE)
    @PostMapping("/batchAuthMember")
    public AjaxResult batchAuthMember(@RequestBody FaMember faMember)
    {
        try {
            faMemberService.batchAuthMember(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("batchAuthMember", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("batchAuthMember", e);
            return AjaxResult.error();
        }
    }

    /**
     * 导出银行卡
     */
    @ApiOperation("导出银行卡")
    @PreAuthorize("@ss.hasPermi('biz:member:export')")
    @Log(title = "导出银行卡", businessType = BusinessType.EXPORT)
    @PostMapping("/exportBankInfo")
    public void exportBankInfo(HttpServletResponse response, FaMember faMember)
    {
        List<FaMember> list = faMemberService.memberBankList(faMember);
        JSONArray memberArray = JSONArray.parseArray(JSON.toJSONString(list));
        List<ExportBankInfo> bankInfoList = JSON.parseArray(memberArray.toJSONString(), ExportBankInfo.class);
        ExcelUtil<ExportBankInfo> util = new ExcelUtil<>(ExportBankInfo.class);
        util.exportExcel(response, bankInfoList, "会员银行卡数据");
    }

    /**
     * 获取用户手机号
     */
    @ApiOperation("获取用户手机号")
    @Log(title = "获取用户手机号", businessType = BusinessType.OTHER)
    @PostMapping("/getMobile")
    public AjaxResult getMobile(@RequestBody FaMember faMember)
    {
        try {
            String mobile = faMemberService.getMobile(faMember);
            return AjaxResult.success(mobile);
        } catch (ServiceException e) {
            logger.error("getMobile", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getMobile", e);
            return AjaxResult.error();
        }
    }

}
