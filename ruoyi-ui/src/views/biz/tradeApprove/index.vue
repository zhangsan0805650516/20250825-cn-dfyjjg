<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="id" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上级id" prop="superiorId">
        <el-input
          v-model="queryParams.superiorId"
          placeholder="请输入上级id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上级机构码" prop="superiorCode">
        <el-input
          v-model="queryParams.superiorCode"
          placeholder="请输入上级机构码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上级姓名" prop="superiorName">
        <el-input
          v-model="queryParams.superiorName"
          placeholder="请输入上级姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联id" prop="tradeId">
        <el-input
          v-model="queryParams.tradeId"
          placeholder="请输入关联id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联流水id" prop="capitalId">
        <el-input
          v-model="queryParams.capitalId"
          placeholder="请输入关联流水id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="股票id" prop="stockId">
        <el-input
          v-model="queryParams.stockId"
          placeholder="请输入股票id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="股票名称" prop="stockName">
        <el-input
          v-model="queryParams.stockName"
          placeholder="请输入股票名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="股票代码" prop="stockSymbol">
        <el-input
          v-model="queryParams.stockSymbol"
          placeholder="请输入股票代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成交手数(1手=100股)" prop="tradingHand">
        <el-input
          v-model="queryParams.tradingHand"
          placeholder="请输入成交手数(1手=100股)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成交数量" prop="tradingNumber">
        <el-input
          v-model="queryParams.tradingNumber"
          placeholder="请输入成交数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成交价格" prop="tradingPrice">
        <el-input
          v-model="queryParams.tradingPrice"
          placeholder="请输入成交价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成交金额" prop="tradingAmount">
        <el-input
          v-model="queryParams.tradingAmount"
          placeholder="请输入成交金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="补缴说明" prop="repayDescription">
        <el-input
          v-model="queryParams.repayDescription"
          placeholder="请输入补缴说明"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="扣款时刻" prop="costTime">
        <el-input
          v-model="queryParams.costTime"
          placeholder="请输入扣款时刻"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="驳回原因" prop="rejectReason">
        <el-input
          v-model="queryParams.rejectReason"
          placeholder="请输入驳回原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建者" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入创建者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="更新者" prop="updateBy">
        <el-input
          v-model="queryParams.updateBy"
          placeholder="请输入更新者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="更新时间" prop="updateTime">
        <el-date-picker clearable
          v-model="queryParams.updateTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择更新时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="删除标记" prop="deleteFlag">
        <el-input
          v-model="queryParams.deleteFlag"
          placeholder="请输入删除标记"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['biz:tradeApprove:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['biz:tradeApprove:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['biz:tradeApprove:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:tradeApprove:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tradeApproveList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="手机号" align="center" prop="mobile" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="上级id" align="center" prop="superiorId" />
      <el-table-column label="上级机构码" align="center" prop="superiorCode" />
      <el-table-column label="上级姓名" align="center" prop="superiorName" />
      <el-table-column label="交易类型(1大宗 2VIP调研 3申购 4配售 )" align="center" prop="tradeType" />
      <el-table-column label="关联id" align="center" prop="tradeId" />
      <el-table-column label="关联流水id" align="center" prop="capitalId" />
      <el-table-column label="股票id" align="center" prop="stockId" />
      <el-table-column label="股票名称" align="center" prop="stockName" />
      <el-table-column label="股票代码" align="center" prop="stockSymbol" />
      <el-table-column label="成交手数(1手=100股)" align="center" prop="tradingHand" />
      <el-table-column label="成交数量" align="center" prop="tradingNumber" />
      <el-table-column label="成交价格" align="center" prop="tradingPrice" />
      <el-table-column label="成交金额" align="center" prop="tradingAmount" />
      <el-table-column label="配售状态" align="center" prop="placingStatus" />
      <el-table-column label="补缴状态" align="center" prop="repayStatus" />
      <el-table-column label="补缴说明" align="center" prop="repayDescription" />
      <el-table-column label="扣款方式" align="center" prop="costType" />
      <el-table-column label="扣款时刻" align="center" prop="costTime" />
      <el-table-column label="状态(0待审核 1审核通过 2审核驳回)" align="center" prop="status" />
      <el-table-column label="驳回原因" align="center" prop="rejectReason" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新者" align="center" prop="updateBy" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除标记" align="center" prop="deleteFlag" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:tradeApprove:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:tradeApprove:remove']"
          >删除</el-button>
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

    <!-- 添加或修改交易审核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="上级id" prop="superiorId">
          <el-input v-model="form.superiorId" placeholder="请输入上级id" />
        </el-form-item>
        <el-form-item label="上级机构码" prop="superiorCode">
          <el-input v-model="form.superiorCode" placeholder="请输入上级机构码" />
        </el-form-item>
        <el-form-item label="上级姓名" prop="superiorName">
          <el-input v-model="form.superiorName" placeholder="请输入上级姓名" />
        </el-form-item>
        <el-form-item label="关联id" prop="tradeId">
          <el-input v-model="form.tradeId" placeholder="请输入关联id" />
        </el-form-item>
        <el-form-item label="关联流水id" prop="capitalId">
          <el-input v-model="form.capitalId" placeholder="请输入关联流水id" />
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
        <el-form-item label="成交手数(1手=100股)" prop="tradingHand">
          <el-input v-model="form.tradingHand" placeholder="请输入成交手数(1手=100股)" />
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
        <el-form-item label="补缴说明" prop="repayDescription">
          <el-input v-model="form.repayDescription" placeholder="请输入补缴说明" />
        </el-form-item>
        <el-form-item label="扣款时刻" prop="costTime">
          <el-input v-model="form.costTime" placeholder="请输入扣款时刻" />
        </el-form-item>
        <el-form-item label="驳回原因" prop="rejectReason">
          <el-input v-model="form.rejectReason" placeholder="请输入驳回原因" />
        </el-form-item>
        <el-form-item label="删除标记" prop="deleteFlag">
          <el-input v-model="form.deleteFlag" placeholder="请输入删除标记" />
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
import { listTradeApprove, getTradeApprove, delTradeApprove, addTradeApprove, updateTradeApprove } from "@/api/biz/tradeApprove/tradeApprove";

export default {
  name: "TradeApprove",
  data() {
    return {
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
      // 交易审核表格数据
      tradeApproveList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        userId: null,
        mobile: null,
        name: null,
        superiorId: null,
        superiorCode: null,
        superiorName: null,
        tradeType: null,
        tradeId: null,
        capitalId: null,
        stockId: null,
        stockName: null,
        stockSymbol: null,
        tradingHand: null,
        tradingNumber: null,
        tradingPrice: null,
        tradingAmount: null,
        placingStatus: null,
        repayStatus: null,
        repayDescription: null,
        costType: null,
        costTime: null,
        status: null,
        rejectReason: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        deleteFlag: null,
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
  },
  methods: {
    /** 查询交易审核列表 */
    getList() {
      this.loading = true;
      listTradeApprove(this.queryParams).then(response => {
        this.tradeApproveList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        userId: null,
        mobile: null,
        name: null,
        superiorId: null,
        superiorCode: null,
        superiorName: null,
        tradeType: null,
        tradeId: null,
        capitalId: null,
        stockId: null,
        stockName: null,
        stockSymbol: null,
        tradingHand: null,
        tradingNumber: null,
        tradingPrice: null,
        tradingAmount: null,
        placingStatus: null,
        repayStatus: null,
        repayDescription: null,
        costType: null,
        costTime: null,
        status: null,
        rejectReason: null,
        createBy: null,
        createTime: null,
        updateBy: null,
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
      this.title = "添加交易审核";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTradeApprove(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改交易审核";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTradeApprove(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTradeApprove(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除交易审核编号为"' + ids + '"的数据项？').then(function() {
        return delTradeApprove(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/tradeApprove/export', {
        ...this.queryParams
      }, `tradeApprove_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
