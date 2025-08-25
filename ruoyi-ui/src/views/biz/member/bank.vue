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
      <el-form-item :label="$t('审核状态')" prop="bankCardAuth">
        <el-select
          v-model="queryParams.bankCardAuth"
          :placeholder="$t('审核状态')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('审核中')" value="1"/>
          <el-option :label="$t('审核通过')" value="2"/>
          <el-option :label="$t('审核驳回')" value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('创建时间')" prop="bindingTime">
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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('搜索')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('重置')}}</el-button>
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
          v-hasPermi="['biz:member:add']"
        >{{$t('新增')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['biz:member:edit']"
        >{{$t('修改')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['biz:member:remove']"
        >{{$t('删除')}}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('ID')" align="center" prop="id" width="50" />
      <el-table-column :label="$t('姓名')" align="center" prop="name" :min-width="$getColumnWidth('name',memberList)" />
      <el-table-column :label="$t('唯一码')" align="center" prop="weiyima" :min-width="$getColumnWidth('weiyima',memberList)" />
      <el-table-column :label="$t('手机号')" align="center" prop="mobile" :min-width="$getColumnWidth('mobile',memberList)" v-if="show_mobile === '1'">
        <template slot-scope="scope">
          {{ scope.row.mobile ? scope.row.mobile.replace(/(\d{3})(\d{4})(\d{4})/, "$1****$3") : "" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('收款人姓名')" align="center" prop="accountName" :min-width="$getColumnWidth('accountName',memberList)" />
      <el-table-column :label="$t('收款人账户')" align="center" prop="account" :min-width="$getColumnWidth('account',memberList)" />
      <el-table-column :label="$t('银行')" align="center" prop="depositBank" :min-width="$getColumnWidth('depositBank',memberList)" />
      <el-table-column :label="$t('开户支行')" align="center" prop="khzhihang" :min-width="$getColumnWidth('khzhihang',memberList)" />
      <el-table-column :label="$t('银行卡照片')" align="center" prop="cardImage" >
        <template slot-scope="scope">
          <image-preview :src="scope.row.cardImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('创建时间')" align="center" prop="bindingTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.bindingTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('审核状态')" align="center" prop="bankCardAuth" >
        <template slot-scope="scope">
          <el-tag type="" v-if="scope.row.bankCardAuth === 1">审核中</el-tag>
          <el-tag type="success" v-if="scope.row.bankCardAuth === 2">审核通过</el-tag>
          <el-tag type="danger" v-if="scope.row.bankCardAuth === 3">驳回</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('操作')" align="center" class-name="small-padding fixed-width" fixed="right" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            @click="bindingBank(scope.row)"
            v-hasPermi="['biz:member:bindingBank']"
          ><span style="margin: 5px">审核</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:member:remove']"
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

    <!-- 新增绑定银行卡 -->
    <el-dialog :title="title" :visible.sync="openAddBinding" width="600px" append-to-body>
      <el-form ref="bindingForm" :model="form" :rules="bindingRules" label-width="120px">
        <el-form-item :label="$t('用户')">
          <el-select
            v-model="form.id"
            filterable
            remote
            reserve-keyword
            :placeholder="$t('请输入姓名或唯一码')"
            :remote-method="querySearchMemberAsync"
            :loading="loading"
            @change="selectMember"
          >
            <el-option
              v-for="item in memberOptions"
              :key="item.id"
              :label="item.nickname + ' ' + item.weiyima"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('收款人姓名')" prop="accountName">
          <el-input v-model="form.accountName" :placeholder="$t('请输入收款人姓名')" />
        </el-form-item>
        <el-form-item :label="$t('收款人账户')" prop="account">
          <el-input v-model="form.account" :placeholder="$t('请输入收款人账户')" />
        </el-form-item>
        <el-form-item :label="$t('银行')" prop="depositBank">
          <el-input v-model="form.depositBank" :placeholder="$t('请输入银行')" />
        </el-form-item>
        <el-form-item label="开户支行" prop="khzhihang">
          <el-input v-model="form.khzhihang" placeholder="请输入开户支行" />
        </el-form-item>
        <el-form-item :label="$t('银行卡照片')" prop="cardImage">
          <image-upload v-model="form.cardImage" :limit="1" />
        </el-form-item>
        <el-form-item :label="$t('审核')" prop="bankCardAuth">
          <el-select
            v-model="form.bankCardAuth"
            :placeholder="$t('审核')"
          >
            <el-option :label="$t('审核中')" :value="1"/>
            <el-option :label="$t('审核通过')" :value="2"/>
            <el-option :label="$t('审核驳回')" :value="3"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBindingForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 绑定银行卡 -->
    <el-dialog :title="title" :visible.sync="openBinding" width="600px" append-to-body>
      <el-form ref="bindingForm" :model="form" :rules="bindingRules" label-width="120px">
        <!--        <el-form-item :label="$t('用户名')" prop="username">-->
        <!--          <el-input v-model="form.username" :placeholder="$t('请输入用户名')" disabled />-->
        <!--        </el-form-item>-->
        <el-form-item :label="$t('唯一码')" prop="weiyima">
          <el-input v-model="form.weiyima" :placeholder="$t('请输入用户名')" disabled />
        </el-form-item>
        <el-form-item :label="$t('收款人姓名')" prop="accountName">
          <el-input v-model="form.accountName" :placeholder="$t('请输入收款人姓名')" />
        </el-form-item>
        <el-form-item :label="$t('收款人账户')" prop="account">
          <el-input v-model="form.account" :placeholder="$t('请输入收款人账户')" />
        </el-form-item>
        <el-form-item :label="$t('银行')" prop="depositBank">
          <el-input v-model="form.depositBank" :placeholder="$t('请输入银行')" />
        </el-form-item>
        <el-form-item label="开户支行" prop="khzhihang">
          <el-input v-model="form.khzhihang" placeholder="请输入开户支行" />
        </el-form-item>
        <el-form-item :label="$t('银行卡照片')" prop="cardImage">
          <image-upload v-model="form.cardImage" :limit="1" />
        </el-form-item>
        <el-form-item :label="$t('审核')" prop="bankCardAuth">
          <el-select
            v-model="form.bankCardAuth"
            :placeholder="$t('审核')"
          >
            <el-option :label="$t('审核中')" :value="1"/>
            <el-option :label="$t('审核通过')" :value="2"/>
            <el-option :label="$t('审核驳回')" :value="3"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBindingForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  memberBankList,
  getMember,
  getDailiList,
  submitBindingBank,
  searchMember,
  delMemberBank,
} from "@/api/biz/member/member";
import {checkPermi} from "@/utils/permission";

export default {
  name: "Member",
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
      memberOptions: [],
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
      // 是否显示绑定银行卡弹出层
      openBinding: false,
      openAddBinding: false,
      // 查询参数
      queryParams: {
        orderByColumn: 'binding_time',
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
        bankCardAuth: null,
      },
      // 表单参数
      form: {},
      // 代理列表
      dailiList: [],
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
        // khzhihang: [
        //   { required: true, message: this.$t("开户支行不能为空"), trigger: "blur" }
        // ],
      },
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
    selectMember(item) {

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
    checkPermi,
    /** 查询会员管理列表 */
    getList() {
      this.loading = true;
      memberBankList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.memberList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openBinding = false;
      this.openAddBinding = false;
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
        unbindTime: null,
        bankCardAuth: null,
      };
      this.resetForm("form");
      this.resetForm("bindingForm");
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
      this.openAddBinding = true;
      this.title = this.$t("绑定银行卡");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMember(id).then(response => {
        this.form = response.data;
        this.openBinding = true;
        this.title = this.$t("绑定银行卡");
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
    /** 提交绑定银行卡按钮 */
    submitBindingForm() {
      this.$refs["bindingForm"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.form.avatar = null;
            if (this.form.idCardFrontImage.indexOf("data:image/png;base64,") !== -1) {
              this.form.idCardFrontImage = null;
            };
            if (this.form.idCardBackImage.indexOf("data:image/png;base64,") !== -1) {
              this.form.idCardBackImage = null;
            };
            submitBindingBank(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.openBinding = false;
              this.openAddBinding = false;
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
        return delMemberBank(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {
      });
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
