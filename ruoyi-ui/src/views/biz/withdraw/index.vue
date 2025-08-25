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
      <el-form-item :label="$t('状态')" prop="isPay">
        <el-select
          v-model="queryParams.isPay"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('未打款')" :value="0"/>
          <el-option :label="$t('已打款')" :value="1"/>
          <el-option :label="$t('驳回')" :value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('创建时间')" prop="createTime">
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
      <!--          v-hasPermi="['biz:withdraw:add']"-->
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
      <!--          v-hasPermi="['biz:withdraw:edit']"-->
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
      <!--          v-hasPermi="['biz:withdraw:remove']"-->
      <!--        >删除</el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:withdraw:export']"
        >{{ $t('导出') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          size="mini"
        >
          {{ $t('已打款') }}：<span style="color: #1ab394">{{ statistics.totalPaid }}</span>
          {{ $t('未打款') }}：<span style="color: #1ab394">{{ statistics.totalUnpaid }}</span>
          {{ $t('驳回') }}：<span style="color: #1ab394">{{ statistics.totalRefuse }}</span>
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="withdrawList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('审核')" align="center" class-name="small-padding fixed-width" width="80">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.isApprove === 0"
            size="mini"
            type="primary"
            @click="approve(scope.row)"
            v-hasPermi="['biz:withdraw:approve']"
          ><span style="margin: 5px">审核</span></el-button>
        </template>
      </el-table-column>
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
          {{ scope.row.superiorCode?scope.row.superiorCode:'' }}/{{ scope.row.superiorName?scope.row.superiorName:'' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('提现金额')" align="center" prop="money" />
      <el-table-column :label="$t('状态')" align="center" prop="isPay" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isPay === 0">{{ $t('未打款') }}</el-tag>
          <el-tag type="success" v-if="scope.row.isPay === 1">{{ $t('已打款') }}</el-tag>
          <el-tag type="warning" v-if="scope.row.isPay === 2">{{ $t('驳回') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('驳回原因')" align="center" prop="reject" />
      <el-table-column :label="$t('收款人(姓名/账号/银行)')" align="center" prop="accountName" width="300" >
        <template slot-scope="scope">
          {{ scope.row.accountName?scope.row.accountName:'' }}/{{ scope.row.account?scope.row.account:'' }}/{{ scope.row.depositBank?scope.row.depositBank:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('消息提示')" align="center" prop="isQx" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isQx"
            :active-value=1
            :inactive-value=0
            @change="handleIsQx(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('创建时间')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('操作')" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:withdraw:edit']"
          ><span style="margin: 5px">修改</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:withdraw:remove']"
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

    <!-- 添加或修改提现对话框 -->
    <el-dialog :title="title" :visible.sync="openApprove" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('姓名')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('请输入姓名')" disabled />
        </el-form-item>
        <!--        <el-form-item :label="$t('手机号')" prop="mobile">-->
        <!--          <el-input v-model="form.mobile" :placeholder="$t('请输入手机号')" disabled />-->
        <!--        </el-form-item>-->
        <el-form-item :label="$t('唯一码')" prop="weiyima">
          <el-input v-model="form.weiyima" :placeholder="$t('请输入手机号')" disabled />
        </el-form-item>
        <el-form-item :label="$t('提现金额')" prop="money">
          <el-input v-model="form.money" :placeholder="$t('请输入提现金额')" />
        </el-form-item>
        <el-form-item :label="$t('状态')" prop="isPay">
          <el-select
            v-model="form.isPay"
            :placeholder="$t('请选择')"
            clearable
            @keyup.enter.native="handleQuery"
          >
            <el-option :label="$t('未打款')" :value="0"/>
            <el-option :label="$t('已打款')" :value="1"/>
            <el-option :label="$t('驳回')" :value="2"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('驳回原因')" prop="reject" v-show="form.isPay === 2">
          <el-input type="textarea" :rows="5" v-model="form.reject" placeholder="请输入驳回原因"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitApprove">{{ $t('确 定') }}</el-button>
        <el-button @click="cancelApprove">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改提现对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" disabled />
        </el-form-item>
        <!--        <el-form-item label="手机号" prop="mobile">-->
        <!--          <el-input v-model="form.mobile" placeholder="请输入手机号" disabled />-->
        <!--        </el-form-item>-->
        <el-form-item :label="$t('唯一码')" prop="weiyima">
          <el-input v-model="form.weiyima" :placeholder="$t('请输入手机号')" disabled />
        </el-form-item>
        <el-form-item label="提现金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入提现金额" />
        </el-form-item>
        <el-form-item label="状态" prop="isPay">
          <el-select
            v-model="form.isPay"
            placeholder="请选择"
            clearable
            @keyup.enter.native="handleQuery"
          >
            <el-option label="未打款" :value="0"/>
            <el-option label="已打款" :value="1"/>
            <el-option label="驳回" :value="2"/>
          </el-select>
        </el-form-item>
        <el-form-item label="驳回原因" prop="reject" v-show="form.isPay === 2">
          <el-input type="textarea" :rows="5" v-model="form.reject" placeholder="请输入驳回原因"/>
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <el-input v-model="form.createTime" placeholder="请输入创建时间" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addWithdraw,
  approveWithdraw,
  changeIsQx,
  delWithdraw,
  getWithdraw,
  getWithdrawStatistics,
  listWithdraw,
  updateWithdraw
} from "@/api/biz/withdraw/withdraw";
import {getDailiList, searchMember} from "@/api/biz/member/member";

export default {
  name: "Withdraw",
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
      statistics: {
        totalPaid: 0,
        totalUnpaid: 0,
        totalRefuse: 0,
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
      // 提现表格数据
      withdrawList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openApprove: false,
      // 查询参数
      queryParams: {
        dailiId: null,
        pageNum: 1,
        pageSize: 10,
        id: null,
        userId: null,
        mobile: null,
        name: null,
        superiorId: null,
        superiorCode: null,
        superiorName: null,
        money: null,
        bfb: null,
        sxf: null,
        reject: null,
        wxAccount: null,
        accountId: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        payType: null,
        isPay: null,
        isQx: null,
        deleteFlag: null,
        weiyima: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: this.$t("请选择用户"), trigger: "change" }
        ],
        money: [
          { required: true, message: this.$t("请输入提现金额"), trigger: "blur" }
        ],
        createTime: [
          { required: true, message: this.$t("请输入创建时间"), trigger: "blur" }
        ],
      },
      memberOptions: [],
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
    handleIsQx(row) {
      let newStatus = row.isQx;
      let text = row.isQx == 0 ? this.$t("关闭消息") : this.$t("打开消息");
      let oldStatus = newStatus == 0 ? 1 : 0;

      this.$modal.confirm(this.$t('确认要') + " " + text + '？').then(function () {
        return changeIsQx({"id": row.id, "isQx": newStatus});
      }).then(() => {
        this.$modal.msgSuccess(text + " " + this.$t("成功"));
      }).catch(function () {
        row.isQx = oldStatus;
      });
    },
    getWithdrawStatistics() {
      getWithdrawStatistics(this.queryParams).then(res => {
        this.statistics = res.data;
      });
    },
    querySearchMemberAsync(queryString) {
      if (queryString !== '') {
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          searchMember({ "queryString" : queryString }).then(res => {
            this.memberOptions = res.data;
          }).catch(err => {
            this.$modal.msgError(this.$t("用户数据错误"));
          })
        }, 200);
      } else {
        this.memberOptions = [];
      }
    },
    // 获取代理列表
    getDailiList() {
      getDailiList().then(response => {
        this.dailiList = response.data;
      });
    },
    /** 查询提现列表 */
    getList() {
      this.loading = true;
      listWithdraw(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.withdrawList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      this.getWithdrawStatistics();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 取消按钮
    cancelApprove() {
      this.openApprove = false;
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
        money: null,
        bfb: null,
        sxf: null,
        reject: null,
        wxAccount: null,
        accountId: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        payType: null,
        isPay: null,
        isQx: null,
        deleteFlag: null,
        isApprove: null,
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
      this.title = this.$t("添加提现");
    },
    /** 审核按钮操作 */
    approve(row) {
      this.reset();
      const id = row.id || this.ids
      getWithdraw(id).then(response => {
        this.form = response.data;
        this.form.weiyima = response.data.faMember.weiyima;
        this.openApprove = true;
        this.title = this.$t("审核提现");
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWithdraw(id).then(response => {
        this.form = response.data;
        this.form.weiyima = response.data.faMember.weiyima;
        this.open = true;
        this.title = this.$t("修改提现");
      });
    },
    /** 提交审核 */
    submitApprove() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          approveWithdraw(this.form).then(response => {
            this.$modal.msgSuccess(this.$t("成功"));
            this.openApprove = false;
            this.getList();
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWithdraw(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.open = false;
              this.getList();
            });
          } else {
            addWithdraw(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
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
      this.$modal.confirm(this.$t("确认删除") + '？').then(function () {
        return delWithdraw(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/withdraw/export', {
        ...this.queryParams
      }, `withdraw_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
