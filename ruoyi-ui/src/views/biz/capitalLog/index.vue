<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('姓名')" prop="name">
        <el-input
          v-model="queryParams.name"
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
      <el-form-item :label="$t('类型')" prop="content">
        <el-input
          v-model="queryParams.content"
          :placeholder="$t('请输入类型')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('操作时间')" prop="createTime">
        <el-date-picker
          clearable
          v-model="dateRange"
          type="daterange"
          :picker-options="pickerOptions"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['00:00:00', '23:59:59']"
          align="right"
          style="width: 207px">
        </el-date-picker>
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
      <!--          v-hasPermi="['biz:capitalLog:add']"-->
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
      <!--          v-hasPermi="['biz:capitalLog:edit']"-->
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
      <!--          v-hasPermi="['biz:capitalLog:remove']"-->
      <!--        >删除</el-button>-->
      <!--      </el-col>-->
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleExport"
                v-hasPermi="['biz:capitalLog:export']"
              >导出</el-button>
            </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="capitalLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('姓名/唯一码')" align="center" prop="member" width="250" v-if="show_mobile === '0'">
        <template slot-scope="scope">
          {{ scope.row.faMember.name?scope.row.faMember.name:"" }}/{{ scope.row.faMember.weiyima?scope.row.faMember.weiyima:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('姓名/手机/唯一码')" align="center" prop="member" width="250" v-if="show_mobile === '1'">
        <template slot-scope="scope">
          {{ scope.row.faMember.name?scope.row.faMember.name:"" }}/{{ scope.row.mobile?scope.row.mobile.replace(/(\d{3})(\d{4})(\d{4})/, "$1****$3"):"" }}/{{ scope.row.faMember.weiyima?scope.row.faMember.weiyima:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('上级(机构码/姓名)')" align="center" prop="pid" width="150" >
        <template slot-scope="scope">
          {{ scope.row.superiorCode?scope.row.superiorCode:"" }}/{{ scope.row.superiorName?scope.row.superiorName:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('股票/代码')" align="center" prop="stock" width="150">
        <template slot-scope="scope">
          {{ scope.row.stockName?scope.row.stockName:"" }}/{{ scope.row.stockSymbol?scope.row.stockSymbol:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('类型')" align="center" prop="content" width="150" />
      <el-table-column :label="$t('原金额')" align="center" prop="beforeMoney" />
      <el-table-column :label="$t('金额')" align="center" prop="money" >
        <template slot-scope="scope">
          <span v-if="scope.row.direct === 0">{{ scope.row.money }}</span>
          <span v-if="scope.row.direct === 1">{{ scope.row.money * -1 }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('变动后金额')" align="center" prop="laterMoney" />
      <el-table-column :label="$t('操作时间')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:capitalLog:edit']"
          ><span style="margin: 5px">修改</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:capitalLog:remove']"
          ><span style="margin: 5px">删除</span></el-button>
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

    <!-- 添加或修改资金记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <!--        <el-form-item label="手机号" prop="mobile" >-->
        <!--          <el-input v-model="form.mobile" placeholder="请输入手机号" disabled/>-->
        <!--        </el-form-item>-->
        <el-form-item label="唯一码" prop="weiyima" >
          <el-input v-model="form.weiyima" placeholder="请输入手机号" disabled/>
        </el-form-item>
        <el-form-item label="姓名" prop="name" >
          <el-input v-model="form.name" placeholder="请输入姓名" disabled/>
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="form.content" placeholder="请输入类型" />
        </el-form-item>
        <el-form-item label="原金额" prop="beforeMoney">
          <el-input v-model="form.beforeMoney" placeholder="请输入原金额" />
        </el-form-item>
        <el-form-item label="操作金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入操作金额" />
        </el-form-item>
        <el-form-item label="变动后金额" prop="laterMoney">
          <el-input v-model="form.laterMoney" placeholder="请输入变动后金额" />
        </el-form-item>
        <el-form-item label="操作时间" prop="createTime">
          <el-input v-model="form.createTime" placeholder="请输入操作时间" />
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
import { listCapitalLog, getCapitalLog, delCapitalLog, addCapitalLog, updateCapitalLog } from "@/api/biz/capitalLog/capitalLog";

export default {
  name: "CapitalLog",
  data() {
    return {
      show_mobile: '0',
      // 日期范围
      dateRange: [],
      pickerOptions: {
        shortcuts: [
          {
            text: '今日',
            onClick(picker) {
              const start = new Date();
              start.setHours(0, 0, 0, 0); // 设置为00:00:00
              const end = new Date();
              end.setHours(23, 59, 59, 999); // 设置为23:59:59
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '昨日',
            onClick(picker) {
              const start = new Date();
              start.setDate(start.getDate() - 1); // 设置为昨天
              start.setHours(0, 0, 0, 0); // 设置为00:00:00
              const end = new Date();
              end.setDate(end.getDate() - 1);
              end.setHours(23, 59, 59, 999); // 设置为23:59:59
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '本月',
            onClick(picker) {
              const start = new Date();
              start.setDate(1); // 设置为本月的第一天
              start.setHours(0, 0, 0, 0); // 设置为00:00:00
              const end = new Date(); // 本月的今天
              end.setHours(23, 59, 59, 999); // 设置为23:59:59
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '上个月',
            onClick(picker) {
              const start = new Date();
              start.setMonth(start.getMonth() - 1); // 设置为上个月的第一天
              start.setDate(1);
              start.setHours(0, 0, 0, 0); // 设置为00:00:00
              const end = new Date();
              end.setDate(0); // 设置为上个月的最后一天
              end.setHours(23, 59, 59, 999); // 设置为23:59:59
              picker.$emit('pick', [start, end]);
            }
          },{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              start.setHours(0, 0, 0, 0); // 设置为00:00:00
              end.setHours(23, 59, 59, 999); // 设置为23:59:59
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              start.setHours(0, 0, 0, 0); // 设置为00:00:00
              end.setHours(23, 59, 59, 999); // 设置为23:59:59
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              start.setHours(0, 0, 0, 0); // 设置为00:00:00
              end.setHours(23, 59, 59, 999); // 设置为23:59:59
              picker.$emit('pick', [start, end]);
            }
          }]
      },
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
      // 资金记录表格数据
      capitalLogList: [],
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
        content: null,
        beforeMoney: null,
        laterMoney: null,
        money: null,
        type: null,
        createTime: null,
        updateTime: null,
        orderId: null,
        strategyTitle: null,
        strategyAllCode: null,
        deleteFlag: null,
        weiyima: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
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
    /** 查询资金记录列表 */
    getList() {
      this.loading = true;
      listCapitalLog(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.capitalLogList = response.rows;
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
        content: null,
        beforeMoney: null,
        laterMoney: null,
        money: null,
        type: null,
        createTime: null,
        updateTime: null,
        orderId: null,
        strategyTitle: null,
        strategyAllCode: null,
        deleteFlag: null,
        weiyima: null,
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
      this.dateRange = [];
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
      this.title = "添加资金记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCapitalLog(id).then(response => {
        this.form = response.data;
        this.form.weiyima = response.data.faMember.weiyima;
        this.open = true;
        this.title = "修改资金记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCapitalLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCapitalLog(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除资金记录编号为"' + ids + '"的数据项？').then(function() {
        return delCapitalLog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/capitalLog/export', {
        ...this.queryParams
      }, `capitalLog_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
