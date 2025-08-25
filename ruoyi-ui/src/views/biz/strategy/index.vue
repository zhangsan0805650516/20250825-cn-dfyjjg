<template>
  <div class="app-container">

    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane :label="$t('全部')" name="all"></el-tab-pane>

      <el-tab-pane v-for="dict in dict.type.exchange_type"
                   :label="dict.label"
                   :name="dict.value"
      />
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('股票')" prop="title">
        <el-input
          v-model="queryParams.title"
          :placeholder="$t('股票名称/代码')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('推荐')" prop="zhiding">
        <el-select
          v-model="queryParams.zhiding"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('关闭')" :value="0"/>
          <el-option :label="$t('打开')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('普通交易')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('解锁')" :value="0"/>
          <el-option :label="$t('锁定')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('开启T+0')" prop="currentStatus">
        <el-select
          v-model="queryParams.currentStatus"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('关闭')" :value="0"/>
          <el-option :label="$t('开启')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('抢筹状态')" prop="qcStatus">
        <el-select
          v-model="queryParams.qcStatus"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('关闭')" :value="0"/>
          <el-option :label="$t('开启')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('大宗开关')" prop="isDz" v-if="dz_syshow === '1'">
        <el-select
          v-model="queryParams.isDz"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('关闭')" :value="0"/>
          <el-option :label="$t('打开')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('VIP调研')" prop="vipQcStatus" v-if="show_vip_trade === '1'">
        <el-select
          v-model="queryParams.vipQcStatus"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('关闭')" :value="0"/>
          <el-option :label="$t('打开')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('融券')" prop="isRq" v-if="show_securities === '1'">
        <el-select
          v-model="queryParams.isRq"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('关闭')" :value="0"/>
          <el-option :label="$t('打开')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('预约重组')" prop="yyczSwitch" v-if="yycz_show === '1'">
        <el-select
          v-model="queryParams.yyczSwitch"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('关闭')" :value="0"/>
          <el-option :label="$t('打开')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('显示开关')" prop="isHide">
        <el-select
          v-model="queryParams.isHide"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('显示')" :value="0"/>
          <el-option :label="$t('隐藏')" :value="1"/>
        </el-select>
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
          v-hasPermi="['biz:strategy:add']"
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
          v-hasPermi="['biz:strategy:edit']"
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
          v-hasPermi="['biz:strategy:remove']"
        >{{ $t('删除') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:strategy:export']"
        >{{ $t('导出') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="strategyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('操作')" align="center" class-name="small-padding fixed-width" fixed width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:strategy:edit']"
          ><span style="margin: 5px">修改</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:strategy:remove']"
          ><span style="margin: 5px">删除</span></el-button>
        </template>
      </el-table-column>
      <el-table-column :label="$t('股票')" align="center" prop="title" width="100" fixed />
      <el-table-column :label="$t('代码')" align="center" prop="allCode" width="100" fixed />
      <el-table-column :label="$t('类型')" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.exchange_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('推荐')" align="center" prop="zhiding" width="100" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.zhiding"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'zhiding')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('普通交易(解锁/锁定)')" align="center" prop="status" width="150" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'status')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('开启T+0')" align="center" prop="currentStatus" width="120">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.currentStatus"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'currentStatus')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('抢筹状态')" align="center" prop="qcStatus" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.qcStatus"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'qcStatus')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('大宗(关闭/打开)')" align="center" prop="isDz" width="120" v-if="dz_syshow === '1'">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isDz"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'isDz')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('VIP调研(关闭/打开)')" align="center" prop="vipQcStatus" width="150" v-if="show_vip_trade === '1'">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.vipQcStatus"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'vipQcStatus')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('融券(关闭/打开)')" align="center" prop="isRq" width="120" v-if="show_securities === '1'">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isRq"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'isRq')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('重组(关闭/打开)')" align="center" prop="yyczSwitch" width="120" v-if="yycz_show === '1'">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.yyczSwitch"
            :active-value=1
            :inactive-value=0
            @change="handleYyczSwitchChange(scope.row, 'yyczSwitch')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('显示/隐藏')" align="center" prop="isHide" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isHide"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'isHide')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('大宗价格')" align="center" prop="zfaPrice" width="100" v-if="dz_syshow === '1'" />
      <el-table-column :label="$t('大宗总份额')" align="center" prop="totalZfaNum" width="100" v-if="dz_syshow === '1'" />
      <el-table-column :label="$t('剩余大宗数量')" align="center" prop="zfaNum" width="100" v-if="dz_syshow === '1'" />
      <el-table-column :label="$t('大宗邀请码')" align="center" prop="conditionCode" width="100" v-if="dz_syshow === '1'" />
      <el-table-column :label="$t('VIP调研价格')" align="center" prop="vipQcPrice" width="100" v-if="show_vip_trade === '1'" />
      <el-table-column :label="$t('VIP调研邀请码')" align="center" prop="vipQcConditionCode" width="120" v-if="show_vip_trade === '1'" />
      <el-table-column :label="$t('VIP调研总份额')" align="center" prop="totalQcNum" width="120" v-if="show_vip_trade === '1'" />
      <el-table-column :label="$t('VIP调研剩余数量')" align="center" prop="leftQcNum" width="150" v-if="show_vip_trade === '1'" />
      <el-table-column :label="$t('融券可借数量')" align="center" prop="borrowNum" width="120" v-if="show_securities === '1'" />
      <el-table-column :label="$t('融券剩余数量')" align="center" prop="borrowNumLeft" width="150" v-if="show_securities === '1'" />
      <el-table-column :label="$t('融券出借天数')" align="center" prop="borrowDays" width="100" v-if="show_securities === '1'" />
      <el-table-column :label="$t('融券邀请码')" align="center" prop="borrowCode" width="100" v-if="show_securities === '1'" />
      <el-table-column :label="$t('预约重组总名额')" align="center" prop="yyczTotalQuota" width="120" v-if="yycz_show === '1'" />
      <el-table-column :label="$t('预约重组剩余名额')" align="center" prop="yyczLeftQuota" width="130" v-if="yycz_show === '1'" />
      <el-table-column :label="$t('锁仓T+N')" align="center" prop="pingDay" width="100" >
        <template slot-scope="scope">
          {{ 'T+' + scope.row.pingDay}}
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

    <!-- 添加或修改策略对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-form-item :label="$t('股票名称')" prop="title">
          <el-input v-model="form.title" :placeholder="$t('股票名称')" />
        </el-form-item>
        <el-form-item :label="$t('股票代码')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('股票代码')" />
        </el-form-item>
        <el-form-item :label="$t('代码全称')" prop="allCode">
          <el-input v-model="form.allCode" :placeholder="$t('代码全称')" />
        </el-form-item>
        <el-form-item :label="$t('类型')" prop="type">
          <el-select
            v-model="form.type"
            :placeholder="$t('请选择')"
          >
            <el-option
              v-for="dict in dict.type.exchange_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('大宗价格')" prop="zfaPrice" v-if="form.id != null && dz_syshow === '1'">
          <el-row>
            <el-col :span="24">
              <el-input v-model="form.zfaPrice" :placeholder="$t('大宗价格')" @input="changeZfaPrice" >
                <template slot="prepend">现价:{{ this.form.caiTrade }}</template>
              </el-input>
            </el-col>
            <!--            <el-col :span="12">-->
            <!--              <el-input v-model="form.zfaDiscount" :placeholder="$t('大宗折扣')" @input="changeZfaDiscount" >-->
            <!--                <template slot="prepend">折扣</template>-->
            <!--                <template slot="append">%</template>-->
            <!--              </el-input>-->
            <!--            </el-col>-->
          </el-row>
        </el-form-item>

        <el-form-item :label="$t('大宗总份额')" prop="totalZfaNum" v-if="form.id != null && dz_syshow === '1'">
          <el-input v-model="form.totalZfaNum" :placeholder="$t('大宗总份额')" />
        </el-form-item>
        <el-form-item :label="$t('剩余大宗数量')" prop="zfaNum" v-if="form.id != null && dz_syshow === '1'">
          <el-input v-model="form.zfaNum" :placeholder="$t('剩余大宗数量')" />
        </el-form-item>
        <el-form-item :label="$t('大宗邀请码')" prop="conditionCode" v-if="form.id != null && dz_syshow === '1'">
          <el-input v-model="form.conditionCode" :placeholder="$t('大宗邀请码')" />
        </el-form-item>

        <el-form-item :label="$t('VIP调研价格')" prop="vipQcPrice" v-if="form.id != null && show_vip_trade === '1'">
          <el-row>
            <el-col :span="24">
              <el-input v-model="form.vipQcPrice" :placeholder="$t('VIP调研价格')" @input="changeVipQcPrice" >
                <template slot="prepend">现价:{{ this.form.caiTrade }}</template>
              </el-input>
            </el-col>
            <!--            <el-col :span="12">-->
            <!--              <el-input v-model="form.vipQcDiscount" :placeholder="$t('VIP调研折扣')" @input="changeVipQcDiscount" >-->
            <!--                <template slot="prepend">折扣</template>-->
            <!--                <template slot="append">%</template>-->
            <!--              </el-input>-->
            <!--            </el-col>-->
          </el-row>
        </el-form-item>

        <el-form-item :label="$t('VIP调研邀请码')" prop="vipQcConditionCode" v-if="form.id != null && show_vip_trade === '1'">
          <el-input v-model="form.vipQcConditionCode" :placeholder="$t('VIP调研邀请码')" />
        </el-form-item>
        <el-form-item :label="$t('VIP调研总份额')" prop="totalQcNum" v-if="form.id != null && show_vip_trade === '1'">
          <el-input v-model="form.totalQcNum" :placeholder="$t('VIP调研总份额')" />
        </el-form-item>
        <el-form-item :label="$t('VIP调研剩余数量')" prop="leftQcNum" v-if="form.id != null && show_vip_trade === '1'">
          <el-input v-model="form.leftQcNum" :placeholder="$t('VIP调研剩余数量')" />
        </el-form-item>
        <el-form-item :label="$t('锁仓T+N')" prop="pingDay" v-if="form.id != null">
          <el-input v-model="form.pingDay" :placeholder="$t('锁仓T+N')" />
        </el-form-item>
        <el-form-item :label="$t('融券可借数量')" prop="borrowNum" v-if="form.id != null && show_securities === '1'">
          <el-input v-model="form.borrowNum" :placeholder="$t('融券可借数量')" />
        </el-form-item>
        <el-form-item :label="$t('融券剩余数量')" prop="borrowNumLeft" v-if="form.id != null && show_securities === '1'">
          <el-input v-model="form.borrowNumLeft" :placeholder="$t('融券剩余数量')" />
        </el-form-item>
        <el-form-item :label="$t('融券出借天数')" prop="borrowDays" v-if="form.id != null && show_securities === '1'">
          <el-input v-model="form.borrowDays" :placeholder="$t('融券出借天数')" />
        </el-form-item>
        <el-form-item :label="$t('融券邀请码')" prop="borrowCode" v-if="form.id != null && show_securities === '1'">
          <el-input v-model="form.borrowCode" :placeholder="$t('融券邀请码')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改策略对话框 -->
    <el-dialog :title="title" :visible.sync="openYycz" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-form-item :label="$t('股票名称')" prop="title">
          <el-input v-model="form.title" :placeholder="$t('股票名称')" disabled />
        </el-form-item>
        <el-form-item :label="$t('股票代码')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('股票代码')" disabled />
        </el-form-item>
        <el-form-item :label="$t('代码全称')" prop="allCode">
          <el-input v-model="form.allCode" :placeholder="$t('代码全称')" disabled />
        </el-form-item>
        <el-form-item :label="$t('预约重组总名额')" prop="yyczTotalQuota">
          <el-input v-model="form.yyczTotalQuota" :placeholder="$t('预约重组总名额')" />
        </el-form-item>
        <el-form-item :label="$t('预约重组剩余名额')" prop="yyczLeftQuota">
          <el-input v-model="form.yyczLeftQuota" :placeholder="$t('预约重组剩余名额')" />
        </el-form-item>
        <el-form-item :label="$t('预约重组公告')" prop="yyczNotice">
          <el-input type="textarea" :rows="3" v-model="form.yyczNotice"></el-input>
        </el-form-item>
        <el-form-item :label="$t('状态')" prop="yyczSwitch">
          <el-select
            v-model="form.yyczSwitch"
            :placeholder="$t('请选择')"
            clearable
            @keyup.enter.native="handleQuery"
          >
            <el-option :label="$t('关闭')" :value="0"/>
            <el-option :label="$t('打开')" :value="1"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormYycz">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listStrategy, getStrategy, delStrategy, addStrategy, updateStrategy, changeStockStatus, submitFormYycz } from "@/api/biz/strategy/strategy";

export default {
  name: "Strategy",
  dicts: ['exchange_type'],
  data() {
    return {
      dz_syshow: '0',
      show_vip_trade: '0',
      show_securities: '0',
      yycz_show: '0',
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
      // 策略表格数据
      strategyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openYycz: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        classifyId: null,
        title: null,
        code: null,
        allCode: null,
        prefixCode: null,
        conditionCode: null,
        today: null,
        yesterday: null,
        mostHigh: null,
        mostLow: null,
        stockNum: null,
        currentPrice: null,
        buyPrice: null,
        profitPrice: null,
        stopProfitPrice: null,
        losePrice: null,
        stopLosePrice: null,
        yield: null,
        recommendCreditMoney: null,
        selectCreditMoney: null,
        multiplying: null,
        canBuyDesc: null,
        autoRenewalDesc: null,
        allMoneyDesc: null,
        allMoney: null,
        canBuy: null,
        makeBargainNum: null,
        makeBargainMoney: null,
        cityProfit: null,
        cityClean: null,
        changeHands: null,
        minValue: null,
        isAutoMoney: null,
        cityValue: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        isRise: null,
        caiTrade: null,
        caiPricechange: null,
        caiChangepercent: null,
        caiBuy: null,
        caiSell: null,
        caiSettlement: null,
        caiOpen: null,
        caiHigh: null,
        caiLow: null,
        caiVolume: null,
        caiAmount: null,
        caiTicktime: null,
        status: null,
        type: null,
        currentStatus: null,
        qcStatus: null,
        vipQcStatus: null,
        zhiding: null,
        zsType: null,
        orderFlag: null,
        isHot: null,
        isZs: null,
        isPz: null,
        isDz: null,
        isZfa: null,
        pingDay: null,
        zfaNum: null,
        totalZfaNum: null,
        deleteFlag: null,
        isHide: null,
        isRq: null,
        yyczSwitch: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: this.$t('股票名称不能为空'), trigger: 'blur' }
        ],
        code: [
          { required: true, message: this.$t('股票代码不能为空'), trigger: 'blur' }
        ],
        allCode: [
          { required: true, message: this.$t('代码全称不能为空'), trigger: 'blur' }
        ],
        type: [
          { required: true, message: this.$t('类型不能为空'), trigger: 'change' }
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
      this.getInfoByName({"name":"dz_syshow"}).then(res => {
        this.dz_syshow = res.data.value;
      }).catch(err => {
        console.log(err);
      });
      this.getInfoByName({"name":"show_vip_trade"}).then(res => {
        this.show_vip_trade = res.data.value;
      }).catch(err => {
        console.log(err);
      });
      this.getInfoByName({"name":"show_securities"}).then(res => {
        this.show_securities = res.data.value;
      }).catch(err => {
        console.log(err);
      });
      this.getInfoByName({"name":"yycz_show"}).then(res => {
        this.yycz_show = res.data.value;
      }).catch(err => {
        console.log(err);
      });
    },
    submitFormYycz() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          submitFormYycz(this.form).then(response => {
            this.$modal.msgSuccess(this.$t("成功"));
            this.openYycz = false;
            this.getList();
          });
        }
      });
    },
    handleYyczSwitchChange(row, type) {
      this.reset();
      const id = row.id || this.ids
      getStrategy(id).then(response => {
        this.form = response.data;
        this.openYycz = true;
        this.title = this.$t("编辑预约重组");
      });
    },
    changeZfaPrice(data) {
      this.form.zfaDiscount = (this.form.zfaPrice * 100 / this.form.caiTrade).toFixed(0);
    },
    changeVipQcPrice(data) {
      this.form.vipQcDiscount = (this.form.vipQcPrice * 100 / this.form.caiTrade).toFixed(0);
    },
    changeZfaDiscount(data) {
      this.form.zfaPrice = (this.form.caiTrade * data / 100).toFixed(2);
    },
    changeVipQcDiscount(data) {
      this.form.vipQcPrice = (this.form.caiTrade * data / 100).toFixed(2);
    },
    // 股票状态修改
    handleSwitchChange(row, type) {
      let newStatus;
      let text;
      if (type == 'zhiding') {
        newStatus = row.zhiding;
        text = row.zhiding == 0 ? this.$t("关闭推荐") : this.$t("打开推荐");
      } else if (type == 'status') {
        newStatus = row.status;
        text = row.status == 0 ? this.$t("解锁") : this.$t("锁定");
      } else if (type == 'currentStatus') {
        newStatus = row.currentStatus;
        text = row.currentStatus == 0 ? this.$t("关闭当天平仓") : this.$t("打开当天平仓");
      } else if (type == 'qcStatus') {
        newStatus = row.qcStatus;
        text = row.qcStatus == 0 ? this.$t("关闭抢筹") : this.$t("打开抢筹");
      } else if (type == 'isHide') {
        newStatus = row.isHide;
        text = row.isHide == 0 ? this.$t("显示") : this.$t("隐藏");
      } else if (type == 'isPz') {
        newStatus = row.isPz;
        text = row.isPz == 0 ? this.$t("关闭杠杆") : this.$t("打开杠杆");
      } else if (type == 'isDz') {
        newStatus = row.isDz;
        text = row.isDz == 0 ? this.$t("关闭大宗") : this.$t("打开大宗");
      } else if (type == 'vipQcStatus') {
        newStatus = row.vipQcStatus;
        text = row.vipQcStatus == 0 ? this.$t("关闭VIP调研") : this.$t("打开VIP调研");
      } else if (type == 'isRq') {
        newStatus = row.isRq;
        text = row.isRq == 0 ? this.$t("关闭融券") : this.$t("打开融券");
      } else {
        return;
      }

      let oldStatus = newStatus == 0 ? 1 : 0;

      this.$modal.confirm(this.$t('确认要') + " " + text + '？').then(function () {
        return changeStockStatus({"id": row.id, "status": newStatus, "statusType": type});
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
        } else if (type == 'vipQcStatus') {
          row.vipQcStatus = oldStatus;
        } else if (type == 'isRq') {
          row.isRq = oldStatus;
        }
      });
    },
    handleClick(tab, event) {
      if ("all" == tab.name) {
        this.resetForm("queryForm");
        this.queryParams.type = null;
      } else {
        this.queryParams.type = tab.name;
      }
      this.handleQuery();
    },
    /** 查询策略列表 */
    getList() {
      this.loading = true;
      listStrategy(this.queryParams).then(response => {
        this.strategyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openYycz = false;
      this.reset();
      this.getList();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        classifyId: null,
        title: null,
        code: null,
        allCode: null,
        prefixCode: null,
        conditionCode: null,
        today: null,
        yesterday: null,
        mostHigh: null,
        mostLow: null,
        stockNum: null,
        currentPrice: null,
        buyPrice: null,
        profitPrice: null,
        stopProfitPrice: null,
        losePrice: null,
        stopLosePrice: null,
        yield: null,
        recommendCreditMoney: null,
        selectCreditMoney: null,
        multiplying: null,
        canBuyDesc: null,
        autoRenewalDesc: null,
        allMoneyDesc: null,
        allMoney: null,
        canBuy: null,
        makeBargainNum: null,
        makeBargainMoney: null,
        cityProfit: null,
        cityClean: null,
        changeHands: null,
        minValue: null,
        isAutoMoney: null,
        cityValue: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        isRise: null,
        caiTrade: null,
        caiPricechange: null,
        caiChangepercent: null,
        caiBuy: null,
        caiSell: null,
        caiSettlement: null,
        caiOpen: null,
        caiHigh: null,
        caiLow: null,
        caiVolume: null,
        caiAmount: null,
        caiTicktime: null,
        status: null,
        type: null,
        currentStatus: null,
        qcStatus: null,
        vipQcStatus: null,
        zhiding: null,
        zsType: null,
        orderFlag: null,
        isHot: null,
        isZs: null,
        isPz: null,
        isDz: null,
        isZfa: null,
        pingDay: null,
        zfaNum: null,
        zfaPrice: null,
        zfaDiscount: null,
        totalZfaNum: null,
        deleteFlag: null,
        isHide: null,
        vipQcPrice: null,
        vipQcDiscount: null,
        vipQcConditionCode: null,
        totalQcNum: null,
        leftQcNum: null,
        borrowNum: null,
        borrowNumLeft: null,
        borrowDays: null,
        borrowCode: null,
        yyczSwitch: null,
        yyczNotice: null,
        yyczTotalNum: null,
        yyczLeftNum: null,
        yyczTotalQuota: null,
        yyczLeftQuota: null,
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t("添加股票");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStrategy(id).then(response => {
        this.form = response.data;
        if (0 === this.form.caiTrade) {
          this.form.zfaDiscount = 0;
          this.form.vipQcDiscount = 0;
        } else {
          this.form.zfaDiscount = (this.form.zfaPrice * 100 / this.form.caiTrade).toFixed(0);
          this.form.vipQcDiscount = (this.form.vipQcPrice * 100 / this.form.caiTrade).toFixed(0);
        }
        this.open = true;
        this.title = this.$t("修改股票");
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStrategy(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.open = false;
              this.getList();
            });
          } else {
            addStrategy(this.form).then(response => {
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
      this.$modal.confirm(this.$t('确认删除') + '？').then(function () {
        return delStrategy(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/strategy/export', {
        ...this.queryParams
      }, `strategy_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
