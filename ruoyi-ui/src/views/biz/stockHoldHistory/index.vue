<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px" label-position="right">
<!--      <el-col :span="6">-->
<!--        <el-form-item label="下级代理" prop="dailiId">-->
<!--          <el-select-->
<!--            v-model="queryParams.dailiId"-->
<!--            placeholder="下级代理"-->
<!--            clearable-->
<!--            @keyup.enter.native="handleQuery"-->
<!--          >-->
<!--            <el-option-->
<!--              v-for="daili in dailiList"-->
<!--              :key="daili.userId"-->
<!--              :label="daili.nickName"-->
<!--              :value="daili.userId"-->
<!--            />-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--      </el-col>-->
      <el-col :span="6">
        <el-form-item label="用户姓名" prop="memberName">
          <el-input
            v-model="queryParams.memberName"
            placeholder="请输入用户姓名"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="手机号" prop="mobile">
          <el-input
            v-model="queryParams.mobile"
            placeholder="请输入手机号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="类型" prop="stockType">
          <el-select
            v-model="queryParams.stockType"
            placeholder="请选择"
            clearable
            @keyup.enter.native="handleQuery"
          >
            <el-option
              v-for="dict in dict.type.exchange_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="股票名称" prop="name">
          <el-input
            v-model="queryParams.stockName"
            placeholder="请输入股票名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="股票代码" prop="code">
          <el-input
            v-model="queryParams.stockSymbol"
            placeholder="请输入股票代码"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
<!--          v-hasPermi="['biz:stockHold:add']"-->
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
<!--          v-hasPermi="['biz:stockHold:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['biz:stockHold:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['biz:stockHold:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          size="mini"
        >
          盈亏总额：<span style="color: #1ab394">{{ statistics.profitLose }}</span>
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockHoldList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="closingPosition(scope.row)"-->
<!--            v-hasPermi="['biz:stockHold:edit']"-->
<!--          >平仓</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['biz:stockHold:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['biz:stockHold:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="姓名/手机号" align="center" prop="member" width="150">
        <template slot-scope="scope">
          {{ scope.row.faMember.name?scope.row.faMember.name:"" }}/{{ scope.row.faMember.mobile?scope.row.faMember.mobile:"" }}
        </template>
      </el-table-column>
      <el-table-column label="上级(机构码/姓名)" align="center" prop="pid" width="150" >
        <template slot-scope="scope">
          {{ scope.row.pidCode?scope.row.pidCode:"" }}/{{ scope.row.pidName?scope.row.pidName:"" }}
        </template>
      </el-table-column>
      <el-table-column label="股票/代码" align="center" prop="stock" width="150">
        <template slot-scope="scope">
          {{ scope.row.stockName?scope.row.stockName:"" }}/{{ scope.row.stockSymbol?scope.row.stockSymbol:"" }}
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="stockType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.exchange_type" :value="scope.row.stockType"/>
        </template>
      </el-table-column>
      <el-table-column label="持有数量/持仓均价/本金" align="center" prop="holdNumber" width="180">
        <template slot-scope="scope">
          {{ scope.row.holdNumber }}/{{ scope.row.average }}/{{ scope.row.holdNumber * scope.row.average }}
        </template>
      </el-table-column>
      <el-table-column label="盈亏" align="center" prop="profitLose" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.profitLose }}</span>
        </template>
      </el-table-column>
      <el-table-column label="建仓时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="T+N" align="center" prop="freezeT" >-->
<!--        <template slot-scope="scope">-->
<!--          T+{{scope.row.freezeT}}-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改持仓汇总对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户id" prop="memberId">
          <el-input v-model="form.memberId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="上级id" prop="pid">
          <el-input v-model="form.pid" placeholder="请输入上级id" />
        </el-form-item>
        <el-form-item label="上级code" prop="pidCode">
          <el-input v-model="form.pidCode" placeholder="请输入上级code" />
        </el-form-item>
        <el-form-item label="上级姓名" prop="pidName">
          <el-input v-model="form.pidName" placeholder="请输入上级姓名" />
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
        <el-form-item label="持有数量" prop="holdNumber">
          <el-input v-model="form.holdNumber" placeholder="请输入持有数量" />
        </el-form-item>
        <el-form-item label="均价" prop="average">
          <el-input v-model="form.average" placeholder="请输入均价" />
        </el-form-item>
        <el-form-item label="盈亏" prop="profitLose">
          <el-input v-model="form.profitLose" placeholder="请输入盈亏" />
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
import {
  listStockHoldDetail,
  getStockHoldDetail,
  delStockHoldDetail,
  addStockHoldDetail,
  updateStockHoldDetail,
} from "@/api/biz/stockHoldDetail/stockHoldDetail";
import { closingPosition } from "@/api/biz/stockTrading/stockTrading";
import { getDailiList } from "@/api/biz/member/member";

export default {
  name: "StockHold",
  dicts: ['exchange_type'],
  data() {
    return {
      statistics: {
        profitLose: 0
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
      // 持仓汇总表格数据
      stockHoldList: [],
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
        pid: null,
        pidCode: null,
        pidName: null,
        stockId: null,
        stockName: null,
        stockSymbol: null,
        stockType: null,
        holdNumber: null,
        average: null,
        profitLose: null,
        createTime: null,
        updateTime: null,
        status: 1,
        deleteFlag: null,
        remark: null,
        holdType: null,
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
  },
  methods: {
    getHoldHistoryStatistics() {
      getHoldHistoryStatistics(this.queryParams).then(res => {
        this.statistics = res.data;
      });
    },
    closingPosition(row) {
      this.$modal.confirm('确认平仓吗？').then(function() {
        return closingPosition({ "id" : rowl.id });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("平仓成功");
      }).catch(() => {});
    },
    // 获取代理列表
    getDailiList() {
      getDailiList().then(response => {
        this.dailiList = response.data;
      });
    },
    /** 查询持仓汇总列表 */
    getList() {
      this.loading = true;
      listStockHoldDetail(this.queryParams).then(response => {
        this.stockHoldList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      this.getHoldHistoryStatistics();
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
        pid: null,
        pidCode: null,
        pidName: null,
        stockId: null,
        stockName: null,
        stockSymbol: null,
        stockType: null,
        holdNumber: null,
        average: null,
        profitLose: null,
        createTime: null,
        updateTime: null,
        status: null,
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
      this.title = "添加持仓汇总";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStockHoldDetail(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改持仓汇总";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStockHoldDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStockHoldDetail(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除持仓汇总编号为"' + ids + '"的数据项？').then(function() {
        return delStockHoldDetail(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/stockHold/export', {
        ...this.queryParams
      }, `stockHold_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
