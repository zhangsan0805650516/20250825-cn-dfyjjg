//package com.ruoyi.web.controller.api.newsCatalog;
//
//import com.ruoyi.biz.newsCatalog.domain.FaNewsCatalog;
//import com.ruoyi.biz.newsCatalog.service.IFaNewsCatalogService;
//import com.ruoyi.common.annotation.AppLog;
//import com.ruoyi.common.core.controller.BaseController;
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.common.enums.BusinessType;
//import com.ruoyi.common.utils.MessageUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * 新闻栏目Controller
// *
// * @author ruoyi
// * @date 2024-01-09
// */
//@Api(tags = "新闻栏目")
//@RestController
//@RequestMapping("/api/newsCatalog")
//public class NewsCatalogController extends BaseController
//{
//    @Autowired
//    private IFaNewsCatalogService faNewsCatalogService;
//
//    /**
//     * 查询新闻栏目
//     */
//    @ApiOperation("查询新闻栏目")
//    @AppLog(title = "查询新闻栏目", businessType = BusinessType.OTHER)
//    @PostMapping("/getNewsCatalog")
//    public AjaxResult getNewsCatalog()
//    {
//        try {
//            List<FaNewsCatalog> list = faNewsCatalogService.getNewsCatalog();
//            return AjaxResult.success(list);
//        } catch (Exception e) {
//            logger.error("getNewsCatalog", e);
//            return AjaxResult.error();
//        }
//    }
//
//}
