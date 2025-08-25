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
      <el-form-item :label="$t('股票名称')" prop="name">
        <el-input
          v-model="queryParams.stockName"
          :placeholder="$t('请输入股票名称')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('股票代码')" prop="allCode">
        <el-input
          v-model="queryParams.allCode"
          :placeholder="$t('请输入股票代码')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('类型')" prop="stockType">
        <el-select
          v-model="queryParams.stockType"
          :placeholder="$t('请选择')"
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
      <el-form-item :label="$t('状态')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('状态')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('已卖出')" value="1"/>
          <el-option :label="$t('持有')" value="0"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('买入时间')" prop="buyTime">
        <el-date-picker
          clearable
          v-model="dateRange1"
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
      <el-form-item :label="$t('卖出时间')" prop="sellTime">
        <el-date-picker
          clearable
          v-model="dateRange2"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['biz:stockHoldDetail:add']"
        >{{ $t('新增') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['biz:stockHoldDetail:edit']"
        >{{ $t('修改') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['biz:stockHoldDetail:remove']"
        >{{ $t('删除') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:stockHoldDetail:export']"
        >{{ $t('导出') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          size="mini"
        >
          {{ $t('信用金总额') }}：<span style="color: #1ab394">{{ statistics.totalCost }}</span>
          {{ $t('盈亏总额') }}：<span style="color: #1ab394">{{ statistics.totalProfitLose }}</span>
          {{ $t('买入手续费') }}：<span style="color: #1ab394">{{ statistics.totalBuyFee }}</span>
          {{ $t('卖出手续费') }}：<span style="color: #1ab394">{{ statistics.totalSellFee }}</span>
          {{ $t('印花税') }}：<span style="color: #1ab394">{{ statistics.totalStampDuty }}</span>
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockHoldDetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
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
      <el-table-column :label="$t('上级(机构码/姓名)')" align="center" prop="pid" width="150">
        <template slot-scope="scope">
          {{ scope.row.pidCode?scope.row.pidCode:"" }}/{{ scope.row.pidName?scope.row.pidName:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('股票/代码')" align="center" prop="stock" width="150">
        <template slot-scope="scope">
          {{ scope.row.stockName?scope.row.stockName:"" }}/{{ scope.row.allCode?scope.row.allCode:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('类型')" align="center" prop="stockType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.exchange_type" :value="scope.row.stockType"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('锁仓')" align="center" prop="isLock" >
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.isLock === 0">{{ $t('正常') }}</el-tag>
          <el-tag type="danger" v-if="scope.row.isLock === 1">{{ $t('锁仓') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('买入价格')" align="center" prop="buyPrice" />
      <el-table-column :label="$t('买入手续费')" align="center" prop="buyPoundage" width="100" />
      <el-table-column :label="$t('买入时间')" align="center" prop="buyTime" width="150" />
      <el-table-column :label="$t('卖出价格')" align="center" prop="sellPrice"/>
      <el-table-column :label="$t('卖出手续费')" align="center" prop="sellPoundage" width="100" />
      <el-table-column :label="$t('印花税')" align="center" prop="sellStampDuty" />
      <el-table-column :label="$t('卖出时间')" align="center" prop="sellTime" width="150" />
      <el-table-column :label="$t('当前价格')" align="center" prop="currentPrice" />
      <el-table-column :label="$t('已购股数')" align="center" prop="tradingNumber" />
      <el-table-column :label="$t('盈亏')" align="center" prop="profitLose" />
      <el-table-column :label="$t('市值')" align="center" prop="marketValue" width="100">
        <template slot-scope="scope">
          {{ (scope.row.buyPrice * scope.row.tradingNumber).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('收益率')" align="center" prop="profitRate" >
        <template slot-scope="scope">
          {{ scope.row.profitRate ? scope.row.profitRate : "0" }}%
        </template>
      </el-table-column>
      <el-table-column :label="$t('状态')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status === 0">{{ $t('持有') }}</el-tag>
          <el-tag type="danger" v-if="scope.row.status === 1">{{ $t('已卖出') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('本金')" align="center" prop="cost" width="100">
        <template slot-scope="scope">
          {{ (scope.row.holdNumber * scope.row.average).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="T+N" align="center" prop="freezeDaysLeft" />
      <el-table-column :label="$t('操作')" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="danger"
            @click="openClosingPosition(scope.row)"
            v-hasPermi="['biz:stockHoldDetail:closingPosition']"
          ><span style="margin: 5px">强平</span></el-button>
          <el-button
            v-if="scope.row.status === 0 && scope.row.isLock === 0"
            size="mini"
            type="primary"
            @click="lockHold(scope.row)"
            v-hasPermi="['biz:stockHoldDetail:changeLockStatus']"
          ><span style="margin: 5px">锁仓</span></el-button>
          <el-button
            v-if="scope.row.status === 0 && scope.row.isLock === 1"
            size="mini"
            type="primary"
            @click="unLockHold(scope.row)"
            v-hasPermi="['biz:stockHoldDetail:changeLockStatus']"
          ><span style="margin: 5px">解锁</span></el-button>
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="success"
            @click="openTN(scope.row)"
            v-hasPermi="['biz:stockHoldDetail:openTN']"
          ><span style="margin: 5px">T+N</span></el-button>
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--            v-hasPermi="['biz:stockHoldDetail:edit']"-->
          <!--          >修改</el-button>-->
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            @click="handleDelete(scope.row)"-->
          <!--            v-hasPermi="['biz:stockHoldDetail:remove']"-->
          <!--          >删除</el-button>-->
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

    <!-- 添加或修改持仓汇总对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('用户')">
          <el-select
            v-model="form.memberId"
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
        <el-form-item :label="$t('股票')">
          <el-select
            v-model="form.stockId"
            filterable
            remote
            reserve-keyword
            :placeholder="$t('请输入股票名称或代码')"
            :remote-method="querySearchStockAsync"
            :loading="loading"
            @change="selectStock"
          >
            <el-option
              v-for="item in stockOptions"
              :key="item.id"
              :label="item.title + ' ' + item.code"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('持有数量')" prop="holdNumber">
          <el-input v-model="form.holdNumber" :placeholder="$t('请输入持有数量')" />
        </el-form-item>
        <el-form-item :label="$t('均价')" prop="average">
          <el-input v-model="form.average" :placeholder="$t('请输入均价')" />
        </el-form-item>
        <el-form-item :label="$t('交易时间')" prop="createTime">
          <el-date-picker
            clearable
            v-model="form.createTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 强平输入密码 -->
    <el-dialog :title="title" :visible.sync="closingPositionOpen" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="closingPositionRules" label-width="200px">
        <el-form-item :label="$t('密码')" prop="forceClosePassword">
          <el-input v-model="form.forceClosePassword" :placeholder="$t('请输入密码')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closingPosition">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 调整T+N -->
    <el-dialog :title="title" :visible.sync="TNOpen" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="TNRules" label-width="200px">
        <el-form-item :label="$t('T+N')" prop="freezeDaysLeft">
          <el-input v-model="form.freezeDaysLeft" :placeholder="$t('请输入T+N')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="changeTN">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listStockHoldDetail, getStockHoldDetail, delStockHoldDetail, addStockHoldDetail, updateStockHoldDetail, changeLockStatus, changeTN, getHoldStatistics } from "@/api/biz/stockHoldDetail/stockHoldDetail";
import { closingPositionDetail } from "@/api/biz/stockTrading/stockTrading";
import { searchMember, getDailiList } from "@/api/biz/member/member";
import { searchStock } from "@/api/biz/strategy/strategy";

export default {
  name: "StockHoldDetail",
  dicts: ['exchange_type'],
  data() {
    return {
      show_mobile: '0',
      statistics: {
        totalCost: 0,
        totalProfitLose: 0,
        totalBuyFee: 0,
        totalSellFee: 0,
        totalStampDuty: 0,
      },
      // 日期范围
      dateRange1: [],
      dateRange2: [],
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
      dailiList: [],
      activeName: "all",
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
      // 持仓明细表格数据
      stockHoldDetailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      closingPositionOpen: false,
      TNOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        holdId: null,
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
        resourceType: null,
        createTime: null,
        updateTime: null,
        status: null,
        deleteFlag: null,
        remark: null,
        holdType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        memberId: [
          { required: true, message: "请选择用户", trigger: "change" }
        ],
        stockId: [
          { required: true, message: "请选择股票", trigger: "change" }
        ],
        holdNumber: [
          { required: true, message: "请输入持有数量", trigger: "blur" }
        ],
        average: [
          { required: true, message: "请输入均价", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "请选择交易时间", trigger: "change" }
        ],
      },
      closingPositionRules: {

      },
      TNRules: {
        freezeDaysLeft: [
          { required: true, message: "请输入T+N天数", trigger: "blur" }
        ],
      },
      memberOptions: [],
      stockOptions: [],
    };
  },
  created() {
    this.queryParams.holdType = this.$route.query.holdType;
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
    getHoldStatistics() {
      getHoldStatistics(this.queryParams).then(res => {
        this.statistics = res.data;
      });
    },
    lockHold(row) {
      this.$modal.confirm(this.$t('确认要锁仓吗') + '？').then(function () {
        return changeLockStatus({"id": row.id, "isLock": 1});
      }).then(res => {
        this.$modal.msgSuccess(this.$t("成功"));
        this.getList();
      }).catch(err => {
        // this.$modal.msgError(err.msg);
      })
    },
    unLockHold(row) {
      this.$modal.confirm(this.$t('确认要解锁吗') + '？').then(function () {
        return changeLockStatus({"id": row.id, "isLock": 0});
      }).then(res => {
        this.$modal.msgSuccess(this.$t("成功"));
        this.getList();
      }).catch(err => {
        // this.$modal.msgError(err.msg);
      })
    },
    selectMember(item) {

    },
    selectStock(item) {

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
    querySearchStockAsync(queryString) {
      if (queryString !== '') {
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          searchStock({ "queryString" : queryString }).then(res => {
            this.stockOptions = res.data;
          }).catch(err => {
            this.$modal.msgError(this.$t("股票数据错误"));
          })
        }, 200);
      } else {
        this.stockOptions = [];
      }
    },
    closingPosition() {
      closingPositionDetail(this.form).then(res => {
        this.$modal.msgSuccess(this.$t("成功"));
        this.closingPositionOpen = false;
        this.getList();
      }).catch(err => {
        this.$modal.msgError(err.msg);
      });
    },
    changeTN() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          changeTN(this.form).then(response => {
            this.$modal.msgSuccess(this.$t("成功"));
            this.TNOpen = false;
            this.getList();
          }).catch(err => {
            this.$modal.msgError(err.msg);
          });
        }
      });
    },
    /** 查询持仓明细列表 */
    getList() {
      this.loading = true;
      let search = this.addDateRange(this.queryParams, this.dateRange1, "BuyTime");
      search = this.addDateRange(this.queryParams, this.dateRange2, "SellTime");
      listStockHoldDetail(search).then(response => {
        this.stockHoldDetailList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      this.getHoldStatistics();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.closingPositionOpen = false;
      this.TNOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        holdId: null,
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
        resourceType: null,
        createTime: null,
        updateTime: null,
        status: null,
        holdType: null,
        deleteFlag: null,
        remark: null,
        forceClosePassword: null,
        freezeDaysLeft: null
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
      this.dateRange1 = [];
      this.dateRange2 = [];
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
      this.title = this.$t("添加持仓");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStockHoldDetail(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t("修改持仓");
      });
    },
    openClosingPosition(row) {
      this.reset();
      const id = row.id || this.ids
      getStockHoldDetail(id).then(response => {
        this.form = response.data;
        this.closingPositionOpen = true;
        this.title = this.$t("强制平仓");
      });
    },
    openTN(row) {
      this.reset();
      const id = row.id || this.ids
      getStockHoldDetail(id).then(response => {
        this.form = response.data;
        this.TNOpen = true;
        this.title = this.$t("调整T+N");
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStockHoldDetail(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.open = false;
              this.getList();
            });
          } else {
            this.form.holdType = this.$route.query.holdType;
            addStockHoldDetail(this.form).then(response => {
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
      this.$modal.confirm(this.$t("确认删除") + '？').then(function() {
        return delStockHoldDetail(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/stockHoldDetail/export', {
        ...this.queryParams
      }, `stockHoldDetail_${new Date().getTime()}.xlsx`)
    },
    // 获取代理列表
    getDailiList()
    {
      getDailiList().then(response => {
        this.dailiList = response.data;
      });
    }
  }
};
</script>
