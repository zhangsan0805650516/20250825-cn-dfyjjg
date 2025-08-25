<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px" label-position="right">
      <el-col :span="6">
        <el-form-item label="交易所" prop="exchangeType">
          <el-select v-model="queryParams.exchangeType" placeholder="请选择交易所" clearable>
            <el-option
              v-for="dict in dict.type.exchange_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
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
          v-hasPermi="['biz:exchangeConfig:add']"
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
          v-hasPermi="['biz:exchangeConfig:edit']"
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
          v-hasPermi="['biz:exchangeConfig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:exchangeConfig:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="exchangeConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:exchangeConfig:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:exchangeConfig:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
      <el-table-column label="交易所" align="center" prop="exchangeType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.exchange_type" :value="scope.row.exchangeType"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="上午交易时间" align="center" prop="tradingTimeMorning" width="120" />-->
<!--      <el-table-column label="下午交易时间" align="center" prop="tradingTimeAfternoon" width="120" />-->
<!--      <el-table-column label="上午申购时间" align="center" prop="subscribeTimeMorning" width="120" />-->
<!--      <el-table-column label="下午申购时间" align="center" prop="subscribeTimeAfternoon" width="120" />-->
<!--      <el-table-column label="上午配售时间" align="center" prop="placementTimeMorning" width="120" />-->
<!--      <el-table-column label="下午配售时间" align="center" prop="placementTimeAfternoon" width="120" />-->
<!--      <el-table-column label="上午大宗交易时间" align="center" prop="blockTimeMorning" width="150"/>-->
<!--      <el-table-column label="下午大宗交易时间" align="center" prop="blockTimeAfternoon" width="150" />-->
      <el-table-column label="涨停百分比" align="center" prop="limitUp" width="120" />
      <el-table-column label="跌停百分比" align="center" prop="limitDown" width="120" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
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

    <!-- 添加或修改交易所配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <el-form-item label="交易所" prop="exchangeType">
          <el-select v-model="form.exchangeType" placeholder="请选择交易所">
            <el-option
              v-for="dict in dict.type.exchange_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="上午交易时间" prop="tradingTimeMorning">-->
<!--          <el-input v-model="form.tradingTimeMorning" placeholder="请输入上午交易时间" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="下午交易时间" prop="tradingTimeAfternoon">-->
<!--          <el-input v-model="form.tradingTimeAfternoon" placeholder="请输入下午交易时间" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="上午申购时间" prop="subscribeTimeMorning">-->
<!--          <el-input v-model="form.subscribeTimeMorning" placeholder="请输入上午申购时间" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="下午申购时间" prop="subscribeTimeAfternoon">-->
<!--          <el-input v-model="form.subscribeTimeAfternoon" placeholder="请输入下午申购时间" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="上午配售时间" prop="placementTimeMorning">-->
<!--          <el-input v-model="form.placementTimeMorning" placeholder="请输入上午配售时间" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="下午配售时间" prop="placementTimeAfternoon">-->
<!--          <el-input v-model="form.placementTimeAfternoon" placeholder="请输入下午配售时间" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="上午大宗交易时间" prop="blockTimeMorning">-->
<!--          <el-input v-model="form.blockTimeMorning" placeholder="请输入上午大宗交易时间" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="下午大宗交易时间" prop="blockTimeAfternoon">-->
<!--          <el-input v-model="form.blockTimeAfternoon" placeholder="请输入下午大宗交易时间" />-->
<!--        </el-form-item>-->
        <el-form-item label="涨停百分比" prop="limitUp">
          <el-input v-model="form.limitUp" placeholder="请输入涨停百分比" />
        </el-form-item>
        <el-form-item label="跌停百分比" prop="limitDown">
          <el-input v-model="form.limitDown" placeholder="请输入跌停百分比" />
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
import { listExchangeConfig, getExchangeConfig, delExchangeConfig, addExchangeConfig, updateExchangeConfig } from "@/api/biz/exchangeConfig/exchangeConfig";

export default {
  name: "ExchangeConfig",
  dicts: ['exchange_type'],
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
      // 交易所配置表格数据
      exchangeConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        exchangeName: null,
        tradingTimeMorning: null,
        tradingTimeAfternoon: null,
        subscribeTimeMorning: null,
        subscribeTimeAfternoon: null,
        placementTimeMorning: null,
        placementTimeAfternoon: null,
        blockTimeMorning: null,
        blockTimeAfternoon: null,
        limitUp: null,
        limitDown: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        deleteFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        exchangeType: [
          { required: true, message: "请选择交易所", trigger: "change" }
        ],
        tradingTimeMorning: [
          { required: true, message: "请输入上午交易时间", trigger: "blur" }
        ],
        tradingTimeAfternoon: [
          { required: true, message: "请输入下午交易时间", trigger: "blur" }
        ],
        subscribeTimeMorning: [
          { required: true, message: "请输入上午申购时间", trigger: "blur" }
        ],
        subscribeTimeAfternoon: [
          { required: true, message: "请输入下午申购时间", trigger: "blur" }
        ],
        placementTimeMorning: [
          { required: true, message: "请输入上午配售时间", trigger: "blur" }
        ],
        placementTimeAfternoon: [
          { required: true, message: "请输入下午配售时间", trigger: "blur" }
        ],
        blockTimeMorning: [
          { required: true, message: "请输入上午大宗交易时间", trigger: "blur" }
        ],
        blockTimeAfternoon: [
          { required: true, message: "请输入下午大宗交易时间", trigger: "blur" }
        ],
        limitUp: [
          { required: true, message: "请输入涨停百分比", trigger: "blur" }
        ],
        limitDown: [
          { required: true, message: "请输入跌停百分比", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询交易所配置列表 */
    getList() {
      this.loading = true;
      listExchangeConfig(this.queryParams).then(response => {
        this.exchangeConfigList = response.rows;
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
        exchangeName: null,
        tradingTimeMorning: null,
        tradingTimeAfternoon: null,
        subscribeTimeMorning: null,
        subscribeTimeAfternoon: null,
        placementTimeMorning: null,
        placementTimeAfternoon: null,
        blockTimeMorning: null,
        blockTimeAfternoon: null,
        limitUp: null,
        limitDown: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        deleteFlag: null
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
      this.title = "添加交易所配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getExchangeConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改交易所配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateExchangeConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addExchangeConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除交易所配置编号为"' + ids + '"的数据项？').then(function() {
        return delExchangeConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/exchangeConfig/export', {
        ...this.queryParams
      }, `exchangeConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
