<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('实名姓名')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="$t('请输入实名姓名')"
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
      <el-form-item :label="$t('密码')" prop="searchPassword">
        <el-input
          type="password"
          v-model="queryParams.searchPassword"
          :placeholder="$t('请输入密码')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('搜索')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('重置')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          size="mini"
        >
          <span style="color: rgb(244 28 106)">{{$t('剩余搜索次数：')}}</span>
          <span style="color: #1ab394">{{ searchTimesLeft }}次</span>
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="memberList">
      <el-table-column :label="$t('ID')" align="center" prop="id" :min-width="$getColumnWidth('id',memberList)" fixed />
      <el-table-column :label="$t('实名姓名')" align="center" prop="name" :min-width="$getColumnWidth('name',memberList)" />
      <el-table-column :label="$t('手机号')" align="center" prop="mobile" :min-width="$getColumnWidth('mobile',memberList)" />
      <el-table-column :label="$t('唯一码')" align="center" prop="weiyima" :min-width="$getColumnWidth('weiyima',memberList)" />
      <el-table-column :label="$t('上级(机构码/姓名)')" align="center" prop="pid" :min-width="$getColumnWidth('superiorCode',memberList) + $getColumnWidth('superiorName',memberList)" >
        <template slot-scope="scope">
          {{ scope.row.superiorCode?scope.row.superiorCode:"" }}/{{ scope.row.superiorName?scope.row.superiorName:"" }}
        </template>
      </el-table-column>
    </el-table>

<!--    <pagination-->
<!--      v-show="total>0"-->
<!--      :total="total"-->
<!--      :page.sync="queryParams.pageNum"-->
<!--      :limit.sync="queryParams.pageSize"-->
<!--      @pagination="getSearchList"-->
<!--    />-->

  </div>
</template>

<script>
import { getSearchList, getSearchTimesLeft } from "@/api/biz/searchMember/searchMember";

export default {
  name: "Member",
  data() {
    return {
      searchTimesLeft: 0,
      showMobileNumber: null,
      // 日期范围
      dateRange1: [],
      dateRange2: [],
      timerId: null,
      statistics: {
        totalBalance: 0,
        totalRecharge: 0,
        totalWithdraw: 0,
        totalSg: 0,
        totalPs: 0,
        totalListed: 0,
        totalUnlisted: 0,
      },
      memberStatistics: {
        totalBalance: 0,
        totalRecharge: 0,
        totalWithdraw: 0,
        totalSg: 0,
        totalPs: 0,
        totalListed: 0,
        totalUnlisted: 0,
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
      // 会员管理表格数据
      memberList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示认证弹出层
      openAuth: false,
      // 是否显示绑定银行卡弹出层
      openBinding: false,
      openRecharge: false,
      openUpdateBalance: false,
      openUpdateFreezeProfit: false,
      openMemberStatistics: false,
      // 查询参数
      queryParams: {
        bankCardAuth: null,
        orderByColumn: 'id',
        isAsc: 'descending',
        pageNum: 1,
        pageSize: 10,
        id: null,
        groupId: null,
        username: null,
        nickname: null,
        password: null,
        salt: null,
        saltCode: null,
        email: null,
        mobile: null,
        avatar: null,
        level: null,
        gender: null,
        bio: null,
        qq: null,
        money: null,
        score: null,
        successions: null,
        maxSuccessions: null,
        prevTime: null,
        loginTime: null,
        loginIp: null,
        loginFailure: null,
        loginFailureCode: null,
        paymentCode: null,
        joinIp: null,
        joinTime: null,
        createTime: null,
        updateTime: null,
        token: null,
        status: null,
        verification: null,
        propertyMoney: null,
        balance: null,
        positionMoney: null,
        freezeMoney: null,
        allEarningsRate: null,
        transactionNum: null,
        yesterdayProfit: null,
        cityValue: null,
        superiorName: null,
        superiorCode: null,
        superiorId: null,
        institutionNumber: null,
        isSimulation: null,
        position: null,
        delayMoney: null,
        sgFreezeMoney: null,
        dailiId: null,
        jingzhijiaoyi: null,
        isPz: null,
        isDz: null,
        isPs: null,
        isSg: null,
        isZs: null,
        isQc: null,
        isQh: null,
        isInform: null,
        freezeProfit: null,
        zxyz: null,
        weiyima: null,
        name: null,
        idCard: null,
        idCardFrontImage: null,
        idCardBackImage: null,
        authTime: null,
        isAuth: null,
        authRejectReason: null,
        authRejectTime: null,
        depositBank: null,
        khzhihang: null,
        accountName: null,
        account: null,
        cardImage: null,
        isDefault: null,
        accountType: null,
        bindingTime: null,
        unbindTime: null,
        loginDomain: null,
        searchPassword: null,
      },
      // 表单参数
      form: {},
      // 代理列表
      dailiList: [],
      // 表单校验
      rules: {
        dailiId: [
          { required: true, message: this.$t("请选择上级归属"), trigger: 'change' }
        ],
        weiyima: [
          { required: true, message: this.$t("唯一码不能为空"), trigger: "blur" }
        ],
        mobile: [
          { required: true, message: this.$t("手机号不能为空"), trigger: "blur" }
        ],
        username: [
          { required: true, message: this.$t("用户名不能为空"), trigger: "blur" }
        ],
        nickname: [
          { required: true, message: this.$t("昵称不能为空"), trigger: "blur" }
        ],
        password: [
          { required: true, message: this.$t("密码不能为空"), trigger: "blur" }
        ],
        // name: [
        //   { required: true, message: this.$t("实名姓名不能为空"), trigger: "blur" }
        // ],
        paymentCode: [
          { required: true, message: this.$t("支付密码不能为空"), trigger: "blur" }
        ],
      },
      // 实名表单验证
      authRules: {
        name: [
          { required: true, message: this.$t("认证姓名不能为空"), trigger: "blur" }
        ],
        idCard: [
          { required: true, message: this.$t("身份证号码不能为空"), trigger: "blur" }
        ],
      },
      // 绑定银行卡表单验证
      bindingRules: {
        accountName: [
          { required: true, message: this.$t("收款人姓名不能为空"), trigger: "blur" }
        ],
        account: [
          { required: true, message: this.$t("收款人账户不能为空"), trigger: "blur" }
        ],
        depositBank: [
          { required: true, message: this.$t("银行不能为空"), trigger: "blur" }
        ],
        khzhihang: [
          { required: true, message: this.$t("开户支行不能为空"), trigger: "blur" }
        ],
      },
      // 充值表单验证
      rechargeRules: {
        amount: [
          { required: true, message: this.$t("金额不能为空"), trigger: "blur" }
        ],
        direct: [
          { required: true, message: this.$t("操作不能为空"), trigger: "change" }
        ],
        rechargeType: [
          { required: true, message: this.$t("操作类型不能为空"), trigger: "change" }
        ],
      },
      // 余额表单验证
      updateBalanceRules: {
        amount: [
          { required: true, message: this.$t("金额不能为空"), trigger: "blur" }
        ],
        direct: [
          { required: true, message: this.$t("操作不能为空"), trigger: "change" }
        ],
      },
      // T+1锁定转入转出
      updateFreezeProfitRules: {
        amount: [
          { required: true, message: this.$t("金额不能为空"), trigger: "blur" }
        ],
        direct: [
          { required: true, message: this.$t("操作不能为空"), trigger: "change" }
        ],
      },
    };
  },
  // mounted() {
  //   this.startTimer();
  // },
  // beforeDestroy() {
  //   clearInterval(this.timerId); // 清除计时器
  // },
  created() {
    // this.getSearchList();
    // this.getDailiList();
    this.getSearchTimesLeft();
  },
  methods: {
    getSearchTimesLeft() {
      getSearchTimesLeft().then(res => {
        this.searchTimesLeft = res.data;
      })
    },
    // showMobile(row) {
    //   getMobile({"id": row.id}).then(res => {
    //     this.showMobileNumber = res.msg;
    //   });
    // },
    // startTimer() {
    //   this.timerId = setInterval(() => {
    //     this.handleQuery();
    //   }, 30000); // 设置间隔为30秒
    // },
    /** 查询会员管理列表 */
    getSearchList() {
      this.loading = true;
      let search = this.addDateRange(this.queryParams, this.dateRange1, "LoginTime");
      search = this.addDateRange(this.queryParams, this.dateRange2, "CreateTime");
      getSearchList(search).then(response => {
        this.memberList = response.data;
        this.getSearchTimesLeft();
      }).catch(err => {
        this.memberList = [];
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openAuth = false;
      this.openBinding = false;
      this.openRecharge = false;
      this.openUpdateBalance = false;
      this.openUpdateFreezeProfit = false;
      this.openMemberStatistics = false;
      this.form.rechargeType = null;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        direct: null,
        rechargeType: null,
        id: null,
        groupId: null,
        username: null,
        nickname: null,
        password: null,
        salt: null,
        saltCode: null,
        email: null,
        mobile: null,
        avatar: null,
        level: null,
        gender: null,
        bio: null,
        qq: null,
        money: null,
        score: null,
        successions: null,
        maxSuccessions: null,
        prevTime: null,
        loginTime: null,
        loginIp: null,
        loginFailure: null,
        loginFailureCode: null,
        paymentCode: null,
        joinIp: null,
        joinTime: null,
        createTime: null,
        updateTime: null,
        token: null,
        status: null,
        verification: null,
        propertyMoney: null,
        balance: null,
        positionMoney: null,
        freezeMoney: null,
        allEarningsRate: null,
        transactionNum: null,
        yesterdayProfit: null,
        cityValue: null,
        superiorName: null,
        superiorCode: null,
        superiorId: null,
        institutionNumber: null,
        isSimulation: null,
        position: null,
        delayMoney: null,
        sgFreezeMoney: null,
        dailiId: null,
        jingzhijiaoyi: null,
        isPz: null,
        isDz: null,
        isPs: null,
        isSg: null,
        isZs: null,
        isQc: null,
        isQh: null,
        isInform: null,
        freezeProfit: null,
        zxyz: null,
        weiyima: null,
        name: null,
        idCard: null,
        idCardFrontImage: null,
        idCardBackImage: null,
        authTime: null,
        isAuth: null,
        authRejectReason: null,
        authRejectTime: null,
        depositBank: null,
        khzhihang: null,
        account: null,
        accountName: null,
        cardImage: null,
        isDefault: null,
        accountType: null,
        bindingTime: null,
        unbindTime: null
      };
      this.resetForm("form");
      this.resetForm("authForm");
      this.resetForm("bindingForm");
      this.resetForm("rechargeForm");
      this.resetForm("updateBalanceForm");
      this.resetForm("updateFreezeProfitForm");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getSearchList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange1 = [];
      this.dateRange2 = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 排序操作
    handleSortChange(column) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t("添加会员");
      // this.form.isSimulation = "1";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMember(id).then(response => {
        this.form = response.data;
        this.form.password = "******";
        this.form.paymentCode = "******";
        this.open = true;
        this.title = this.$t("修改会员");
      });
    },
    /** 身份证认证 */
    authMember(row) {
      this.reset();
      const id = row.id || this.ids
      getMember(id).then(response => {
        this.form = response.data;
        this.form.password = "******";
        this.openAuth = true;
        this.title = this.$t("身份证认证");
      });
    },
    /** 绑定银行卡 */
    bindingBank(row) {
      this.reset();
      const id = row.id || this.ids
      getMember(id).then(response => {
        this.form = response.data;
        this.openBinding = true;
        this.title = this.$t("绑定银行卡");
      });
    },
    /** 充值 */
    recharge(row, rechargeType) {
      this.reset();
      const id = row.id || this.ids
      getMember(id).then(response => {
        this.form = response.data;
        this.form.direct = 0;
        this.form.payType = 4;
        this.form.rechargeType = rechargeType;
        this.openRecharge = true;
        if (0 == rechargeType) {
          this.title = this.$t("充值");
        } else if (1 == rechargeType) {
          this.title = this.$t("调整余额");
        } else if (2 == rechargeType) {
          this.title = this.$t("调整锁定金额");
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMember(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.open = false;
              this.getList();
            });
          } else {
            addMember(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 提交实名认证按钮 */
    submitAuthForm() {
      this.$refs["authForm"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            submitAuthMember(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.openAuth = false;
              this.getList();
            });
          } else {
            this.$modal.msgError(this.$t("失败"));
          }
        }
      });
    },
    /** 提交绑定银行卡按钮 */
    submitBindingForm() {
      this.$refs["bindingForm"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            submitBindingBank(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.openBinding = false;
              this.getList();
            });
          } else {
            this.$modal.msgError(this.$t("失败"));
          }
        }
      });
    },
    /** 提交充值按钮 */
    submitRecharge() {
      this.$refs["rechargeForm"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            submitRecharge(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.openRecharge = false;
              this.getList();
            });
          } else {
            this.$modal.msgError(this.$t("失败"));
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(this.$t('确认删除') + '？').then(function () {
        return delMember(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/member/export', {
        ...this.queryParams
      }, `member_${new Date().getTime()}.xlsx`)
    },
    // 批量审核身份认证
    batchAuthMember(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(this.$t('确认批量审核通过') + '？').then(function () {
        return batchAuthMember({ "ids" : ids });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {
      });
    },
    /** 导出银行卡按钮操作 */
    exportBankInfo() {
      this.download('biz/member/exportBankInfo', {
        ...this.queryParams
      }, `bank_${new Date().getTime()}.xlsx`)
    },
    // 用户状态修改
    handleStatusChange(row, type) {
      let newStatus;
      let text;
      if (type == 'jiaoyi') {
        newStatus = row.jingzhijiaoyi;
        text = row.jingzhijiaoyi == 0 ? this.$t("恢复交易") : this.$t("禁止交易");
      } else if (type == 'denglu') {
        newStatus = row.status;
        text = row.status == 0 ? this.$t("恢复登录") : this.$t("禁止登录");
      } else if (type == 'peizi') {
        newStatus = row.isPz;
        text = row.isPz == 0 ? this.$t("关闭配资") : this.$t("打开配资");
      } else if (type == 'dazong') {
        newStatus = row.isDz;
        text = row.isDz == 0 ? this.$t("关闭大宗") : this.$t("打开大宗");
      } else if (type == 'peishou') {
        newStatus = row.isPs;
        text = row.isPs == 0 ? this.$t("关闭配售") : this.$t("打开配售");
      } else if (type == 'shengou') {
        newStatus = row.isSg;
        text = row.isSg == 0 ? this.$t("关闭申购") : this.$t("打开申购");
      } else if (type == 'zhishu') {
        newStatus = row.isZs;
        text = row.isZs == 0 ? this.$t("关闭申购") : this.$t("打开申购");
      } else if (type == 'qiangchou') {
        newStatus = row.isQc;
        text = row.isQc == 0 ? this.$t("关闭抢筹") : this.$t("打开抢筹");
      } else if (type == 'qihuo') {
        newStatus = row.isQh;
        text = row.isQh == 0 ? this.$t("关闭期货") : this.$t("打开期货");
      } else {
        return;
      }

      let oldStatus = newStatus == 0 ? 1 : 0;

      this.$modal.confirm(this.$t('确认要') + " " + text + '？').then(function () {
        return changeMemberStatus({"id": row.id, "status": newStatus, "statusType": type});
      }).then(() => {
        this.$modal.msgSuccess(text + " " + this.$t("成功"));
      }).catch(function () {
        if (type == 'jiaoyi') {
          row.jingzhijiaoyi = oldStatus;
        } else if (type == 'denglu') {
          row.status = oldStatus;
        } else if (type == 'peizi') {
          row.isPz = oldStatus;
        } else if (type == 'dazong') {
          row.isDz = oldStatus;
        } else if (type == 'peishou') {
          row.isPs = oldStatus;
        } else if (type == 'shengou') {
          row.isSg = oldStatus;
        } else if (type == 'zhishu') {
          row.isZs = oldStatus;
        } else if (type == 'qiangchou') {
          row.isQc = oldStatus;
        } else if (type == 'qihuo') {
          row.isQh = oldStatus;
        }
      });
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "authMember":
          this.authMember(row);
          break;
        case "bindingBank":
          this.bindingBank(row);
          break;
        case "recharge":
          this.recharge(row);
          break;
        case "updateBalance":
          this.updateBalance(row);
          break;
        case "updateFreezeProfit":
          this.updateFreezeProfit(row);
          break;
        default:
          break;
      }
    },
    // 获取代理列表
    getDailiList()
    {
      getDailiList().then(response => {
        this.dailiList = response.data;
      });
    }
  }
}
;
</script>
