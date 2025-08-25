<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('下级代理')" prop="dailiId">
        <el-cascader
          v-model="queryParams.dailiId"
          :options="dailiList"
          :props="{ value: 'userId', label: 'nickName',children: 'children', emitPath: false, checkStrictly: true }"
        ></el-cascader>
      </el-form-item>
      <el-form-item :label="$t('姓名')" prop="memberName">
        <el-input
          v-model="queryParams.memberName"
          :placeholder="$t('请输入姓名')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('手机号')" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          :placeholder="$t('请输入手机号')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('唯一码')" prop="weiyima">
        <el-input
          v-model="queryParams.weiyima"
          :placeholder="$t('请输入唯一码')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('股票名称')" prop="stockName">
        <el-input
          v-model="queryParams.stockName"
          :placeholder="$t('请输入股票名称')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('股票代码')" prop="stockSymbol">
        <el-input
          v-model="queryParams.stockSymbol"
          :placeholder="$t('请输入股票代码')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('买/卖')" prop="tradingType">
        <el-select
          v-model="queryParams.tradingType"
          :placeholder="$t('买/卖')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('买入')" value="1"/>
          <el-option :label="$t('卖出')" value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('搜索') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('重置') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          plain-->
      <!--          icon="el-icon-plus"-->
      <!--          size="mini"-->
      <!--          @click="handleAdd"-->
      <!--          v-hasPermi="['biz:stockTrading:add']"-->
      <!--        >新增</el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="success"-->
      <!--          plain-->
      <!--          icon="el-icon-edit"-->
      <!--          size="mini"-->
      <!--          :disabled="single"-->
      <!--          @click="handleUpdate"-->
      <!--          v-hasPermi="['biz:stockTrading:edit']"-->
      <!--        >修改</el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['biz:stockTrading:remove']"
        >删除</el-button>
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="warning"-->
      <!--          plain-->
      <!--          icon="el-icon-download"-->
      <!--          size="mini"-->
      <!--          @click="handleExport"-->
      <!--          v-hasPermi="['biz:stockTrading:export']"-->
      <!--        >导出</el-button>-->
      <!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          size="mini"
        >
          <!--          <span style="color: rgb(244 28 106)">{{ $t('玩家手续费统计') }}</span>-->
          {{ $t('买入手续费') }}：<span style="color: #1ab394">{{ statistics.totalBuyFee }}</span>
          {{ $t('卖出手续费') }}：<span style="color: #1ab394">{{ statistics.totalSellFee }}</span>
          {{ $t('印花税') }}：<span style="color: #1ab394">{{ statistics.totalStampDuty }}</span>
          {{ $t('合计') }}：<span style="color: #1ab394">{{ Number(statistics.totalBuyFee) + Number(statistics.totalSellFee) + Number(statistics.totalStampDuty) }}</span>
        </el-button>
      </el-col>
    </el-row>
    <!--    <el-row :gutter="10" class="mb8">-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="info"-->
    <!--          plain-->
    <!--          size="mini"-->
    <!--        >-->
    <!--          <span style="color: rgb(244 28 106)">业务员</span>-->
    <!--          买入手续费：<span style="color: #1ab394">{{ statistics[1].buyPoundage ? statistics[1].buyPoundage : 0 }}</span>-->
    <!--          卖出手续费：<span style="color: #1ab394">{{ statistics[1].sellPoundage ? statistics[1].sellPoundage : 0 }}</span>-->
    <!--          印花税：<span style="color: #1ab394">{{ statistics[1].stampDuty ? statistics[1].stampDuty : 0 }}</span>-->
    <!--          合计：<span style="color: #1ab394">{{ Number(statistics[1].buyPoundage ? statistics[1].buyPoundage : 0) + Number(statistics[1].sellPoundage ? statistics[1].sellPoundage : 0) + Number(statistics[1].stampDuty ? statistics[1].stampDuty : 0) }}</span>-->
    <!--        </el-button>-->
    <!--      </el-col>-->
    <!--    </el-row>-->

    <el-table v-loading="loading" :data="stockTradingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-edit"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--            v-hasPermi="['biz:stockTrading:edit']"-->
          <!--          >修改</el-button>-->
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:stockTrading:remove']"
          ><span style="margin: 5px">删除</span></el-button>
        </template>
      </el-table-column>
      <el-table-column :label="$t('姓名')" align="center" prop="name" >
        <template slot-scope="scope">
          {{ scope.row.faMember.name?scope.row.faMember.name:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('手机号')" align="center" prop="mobile" width="100" v-if="show_mobile === '1'">
        <template slot-scope="scope">
          {{ scope.row.faMember.mobile?scope.row.faMember.mobile.replace(/(\d{3})(\d{4})(\d{4})/, "$1****$3"):"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('唯一码')" align="center" prop="weiyima" width="100">
        <template slot-scope="scope">
          {{ scope.row.faMember.weiyima?scope.row.faMember.weiyima:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('上级(机构码/姓名)')" align="center" prop="pid" width="150" >
        <template slot-scope="scope">
          {{ scope.row.faMember.superiorCode?scope.row.faMember.superiorCode:"" }}/{{ scope.row.faMember.superiorName?scope.row.faMember.superiorName:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('股票/代码')" align="center" prop="stock" width="150">
        <template slot-scope="scope">
          {{ scope.row.stockName?scope.row.stockName:"" }}/{{ scope.row.stockSymbol?scope.row.stockSymbol:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('成交数量')" align="center" prop="tradingNumber" />
      <el-table-column :label="$t('成交价格')" align="center" prop="tradingPrice" />
      <el-table-column :label="$t('成交金额')" align="center" prop="tradingAmount" />
      <el-table-column :label="$t('买/卖')" align="center" prop="tradingType" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.tradingType === 1">{{ $t('买入') }}</el-tag>
          <el-tag type="success" v-if="scope.row.tradingType === 2">{{ $t('卖出') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('手续费')" align="center" prop="tradingPoundage" />
      <el-table-column :label="$t('印花税')" align="center" prop="stampDuty" />
      <!--      <el-table-column label="状态" align="center" prop="status" />-->
      <el-table-column :label="$t('交易时间')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改成交记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户id" prop="memberId">
          <el-input v-model="form.memberId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="股票id" prop="stockId">
          <el-input v-model="form.stockId" placeholder="请输入股票id" />
        </el-form-item>
        <el-form-item label="股票名称" prop="stockName">
          <el-input v-model="form.stockName" placeholder="请输入股票名称" />
        </el-form-item>
        <el-form-item label="股票代码" prop="stockSymbol">
          <el-input v-model="form.stockSymbol" placeholder="请输入股票代码" />
        </el-form-item>
        <el-form-item label="日期" prop="date">
          <el-input v-model="form.date" placeholder="请输入日期" />
        </el-form-item>
        <el-form-item label="时间" prop="time">
          <el-input v-model="form.time" placeholder="请输入时间" />
        </el-form-item>
        <el-form-item label="成交数量" prop="tradingNumber">
          <el-input v-model="form.tradingNumber" placeholder="请输入成交数量" />
        </el-form-item>
        <el-form-item label="成交价格" prop="tradingPrice">
          <el-input v-model="form.tradingPrice" placeholder="请输入成交价格" />
        </el-form-item>
        <el-form-item label="成交金额" prop="tradingAmount">
          <el-input v-model="form.tradingAmount" placeholder="请输入成交金额" />
        </el-form-item>
        <el-form-item label="手续费" prop="tradingPoundage">
          <el-input v-model="form.tradingPoundage" placeholder="请输入手续费" />
        </el-form-item>
        <el-form-item label="印花税" prop="stampDuty">
          <el-input v-model="form.stampDuty" placeholder="请输入印花税" />
        </el-form-item>
        <el-form-item label="删除标记(0否1是)" prop="deleteFlag">
          <el-input v-model="form.deleteFlag" placeholder="请输入删除标记(0否1是)" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStockTrading, getStockTrading, delStockTrading, addStockTrading, updateStockTrading, getTradingStatistics } from "@/api/biz/stockTrading/stockTrading";
import { getDailiList } from "@/api/biz/member/member";

export default {
  name: "StockTrading",
  data() {
    return {
      show_mobile: '0',
      statistics: {
        totalBuyFee: 0,
        totalSellFee: 0,
        totalStampDuty: 0,
      },
      // 代理列表
      dailiList: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 成交记录表格数据
      stockTradingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        dailiId: null,
        memberName: null,
        mobile: null,
        pageNum: 1,
        pageSize: 10,
        id: null,
        memberId: null,
        stockId: null,
        stockName: null,
        stockSymbol: null,
        date: null,
        time: null,
        tradingNumber: null,
        tradingPrice: null,
        tradingAmount: null,
        tradingType: null,
        tradingPoundage: null,
        stampDuty: null,
        status: null,
        createTime: null,
        updateTime: null,
        deleteFlag: null,
        remark: null,
        weiyima: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getDailiList();
    this.getInfoByNames();
  },
  methods: {
    getInfoByNames() {
      this.getInfoByName({"name":"show_mobile"}).then(res => {
        this.show_mobile = res.data.value;
      }).catch(err => {
        console.log(err);
      });
    },
    getTradingStatistics() {
      getTradingStatistics(this.queryParams).then(res => {
        this.statistics = res.data;
      });
    },
    // 获取代理列表
    getDailiList() {
      getDailiList().then(response => {
        this.dailiList = response.data;
      });
    },
    /** 查询成交记录列表 */
    getList() {
      this.loading = true;
      listStockTrading(this.queryParams).then(response => {
        this.stockTradingList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      this.getTradingStatistics();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        memberId: null,
        stockId: null,
        stockName: null,
        stockSymbol: null,
        date: null,
        time: null,
        tradingNumber: null,
        tradingPrice: null,
        tradingAmount: null,
        tradingType: null,
        tradingPoundage: null,
        stampDuty: null,
        status: null,
        createTime: null,
        updateTime: null,
        deleteFlag: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加成交记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStockTrading(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改成交记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStockTrading(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStockTrading(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除成交记录编号为"' + ids + '"的数据项？').then(function() {
        return delStockTrading(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/stockTrading/export', {
        ...this.queryParams
      }, `stockTrading_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
