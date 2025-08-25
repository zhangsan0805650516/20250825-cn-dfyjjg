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
      <el-form-item :label="$t('是否实名')" prop="isAuth">
        <el-select
          v-model="queryParams.isAuth"
          :placeholder="$t('是否实名')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('未实名')" value="0"/>
          <el-option :label="$t('审核中')" value="1"/>
          <el-option :label="$t('已实名')" value="2"/>
          <el-option :label="$t('审核驳回')" value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('是否绑卡')" prop="bankCardAuth">
        <el-select
          v-model="queryParams.bankCardAuth"
          :placeholder="$t('是否绑卡')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('未绑卡')" value="0"/>
          <el-option :label="$t('审核中')" value="1"/>
          <el-option :label="$t('已绑卡')" value="2"/>
          <el-option :label="$t('审核驳回')" value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('交易')" prop="jingzhijiaoyi">
        <el-select
          v-model="queryParams.jingzhijiaoyi"
          :placeholder="$t('交易')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('禁止')" value="1"/>
          <el-option :label="$t('打开')" value="0"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('登录')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('登录')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('禁止')" value="1"/>
          <el-option :label="$t('打开')" value="0"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('登录IP')" prop="loginIp">
        <el-input
          v-model="queryParams.loginIp"
          :placeholder="$t('请输入登录IP')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('登录时间')" prop="loginTime">
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
      <el-form-item :label="$t('注册时间')" prop="createTime">
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
      <el-form-item :label="$t('注册域名')" prop="loginDomain">
        <el-input
          v-model="queryParams.loginDomain"
          :placeholder="$t('请输入注册域名')"
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
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:member:export']"
        >{{$t('导出')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          size="mini"
        >
          <span style="color: rgb(244 28 106)">{{$t('用户资产统计')}}</span>
          {{$t('账户总余额')}}：<span style="color: #1ab394">{{ statistics.totalBalance }}</span>
          {{$t('累计总充值')}}：<span style="color: #1ab394">{{ statistics.totalRecharge }}</span>
          {{$t('累计总提现')}}：<span style="color: #1ab394">{{ statistics.totalWithdraw }}</span>
          {{$t('累计新股冻结')}}：<span style="color: #1ab394">{{ statistics.totalSg + statistics.totalPs }}</span>
          {{$t('累计持仓市值')}}：<span style="color: #1ab394">{{ statistics.totalListed + statistics.totalUnlisted }}</span>
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('操作')" align="center" class-name="small-padding fixed-width" fixed width="300" v-if="show_fundPool === '0'">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            @click="recharge(scope.row, 0)"
            v-hasPermi="['biz:member:recharge']"
          ><span style="margin: 5px">充值</span></el-button>
          <el-button
            size="mini"
            type="primary"
            @click="recharge(scope.row, 1)"
            v-hasPermi="['biz:member:recharge']"
          ><span style="margin: 5px">余额</span></el-button>
          <el-button
            size="mini"
            type="primary"
            @click="recharge(scope.row, 2)"
            v-hasPermi="['biz:member:recharge']"
          ><span style="margin: 5px">T+1锁定转入转出</span></el-button>
          <el-button
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:member:edit']"
          ><span style="margin: 5px">修改</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:member:remove']"
          ><span style="margin: 5px">删除</span></el-button>
        </template>
      </el-table-column>
      <el-table-column :label="$t('操作')" align="center" class-name="small-padding fixed-width" fixed width="400" v-if="show_fundPool === '1'">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            @click="recharge(scope.row, 0)"
            v-hasPermi="['biz:member:recharge']"
          ><span style="margin: 5px">充值</span></el-button>
          <el-button
            size="mini"
            type="primary"
            @click="recharge(scope.row, 1)"
            v-hasPermi="['biz:member:recharge']"
          ><span style="margin: 5px">余额</span></el-button>
          <el-button
            size="mini"
            type="primary"
            @click="recharge(scope.row, 2)"
            v-hasPermi="['biz:member:recharge']"
          ><span style="margin: 5px">T+1锁定转入转出</span></el-button>
          <el-button
            size="mini"
            type="primary"
            @click="recharge(scope.row, 3)"
            v-hasPermi="['biz:member:recharge']"
          ><span style="margin: 5px">资金池划转</span></el-button>
          <el-button
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:member:edit']"
          ><span style="margin: 5px">修改</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:member:remove']"
          ><span style="margin: 5px">删除</span></el-button>
        </template>
      </el-table-column>
      <el-table-column :label="$t('ID')" align="center" prop="id" :min-width="$getColumnWidth('id',memberList)" fixed />
      <el-table-column :label="$t('实名姓名')" align="center" prop="name" :min-width="$getColumnWidth('name',memberList)" />
      <el-table-column :label="$t('手机号')" align="center" prop="mobile" :min-width="$getColumnWidth('mobile',memberList)" v-if="show_mobile === '1'" />
      <!--      <el-table-column :label="$t('手机号')" align="center" prop="mobile" :min-width="$getColumnWidth('mobile',memberList)" />-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-popover trigger="click" placement="top">-->
      <!--            <p>{{ showMobileNumber }}</p>-->
      <!--            <div slot="reference" class="name-wrapper">-->
      <!--              <el-tag @click="showMobile(scope.row)">{{ scope.row.mobile }}</el-tag>-->
      <!--            </div>-->
      <!--          </el-popover>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column :label="$t('唯一码')" align="center" prop="weiyima" :min-width="$getColumnWidth('weiyima',memberList)" />
      <el-table-column :label="$t('是否实名')" align="center" prop="isAuth" :min-width="$getColumnWidth('isAuth',memberList)" >
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.isAuth == 0">
            {{$t('未实名')}}
          </el-tag>
          <el-tag  v-if="scope.row.isAuth == 1">
            {{$t('审核中')}}
          </el-tag>
          <el-tag type="success" v-if="scope.row.isAuth == 2">
            {{$t('已实名')}}
          </el-tag>
          <el-tag type="danger" v-if="scope.row.isAuth == 3">
            {{$t('审核驳回')}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('是否绑卡')" align="center" prop="isAuth" :min-width="$getColumnWidth('bankCardAuth',memberList)" >
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.bankCardAuth == 0">
            {{$t('未绑卡')}}
          </el-tag>
          <el-tag  v-if="scope.row.bankCardAuth == 1">
            {{$t('审核中')}}
          </el-tag>
          <el-tag type="success" v-if="scope.row.bankCardAuth == 2">
            {{$t('已绑卡')}}
          </el-tag>
          <el-tag type="danger" v-if="scope.row.bankCardAuth == 3">
            {{$t('审核驳回')}}
          </el-tag>
        </template>
      </el-table-column>
      <!--      <el-table-column label="唯一码" align="center" prop="weiyima" width="100" />-->
      <el-table-column :label="$t('上级(机构码/姓名)')" align="center" prop="pid" :min-width="$getColumnWidth('superiorCode',memberList) + $getColumnWidth('superiorName',memberList)" >
        <template slot-scope="scope">
          {{ scope.row.superiorCode?scope.row.superiorCode:"" }}/{{ scope.row.superiorName?scope.row.superiorName:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('交易')" align="center" prop="jingzhijiaoyi" :min-width="$getColumnWidth('jingzhijiaoyi',memberList)" v-if="checkPermi(['biz:member:lockTrade'])">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.jingzhijiaoyi"
            :active-value=0
            :inactive-value=1
            @change="handleStatusChange(scope.row, 'jiaoyi')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('登录')" align="center" prop="status" :min-width="$getColumnWidth('status',memberList)" v-if="checkPermi(['biz:member:lockLogin'])">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value=0
            :inactive-value=1
            @change="handleStatusChange(scope.row, 'denglu')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="大宗" align="center" prop="isDz" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isDz"
            :active-value=1
            :inactive-value=0
            @change="handleStatusChange(scope.row, 'dazong')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="提现" align="center" prop="withdrawSwitch" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.withdrawSwitch"
            :active-value=1
            :inactive-value=0
            @change="handleStatusChange(scope.row, 'withdraw')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('总资产')" align="center" prop="total" :min-width="$getColumnWidth('total',memberList)" />
      <el-table-column :label="$t('可用资金')" align="center" prop="balance" :min-width="$getColumnWidth('balance',memberList)" />
      <el-table-column :label="$t('占用资金')" align="center" prop="takeUp" :min-width="$getColumnWidth('takeUp',memberList)" />
      <el-table-column :label="$t('新股冻结')" align="center" prop="newStockFreeze" :min-width="$getColumnWidth('newStockFreeze',memberList)" />
      <el-table-column :label="$t('总盈亏')" align="center" prop="totalProfit" :min-width="$getColumnWidth('totalProfit',memberList)" />
      <el-table-column :label="$t('T+1锁定')" align="center" prop="freezeProfit" :min-width="$getColumnWidth('freezeProfit',memberList)" />
      <!--      <el-table-column label="当前机构号" align="center" prop="institutionNumber" width="100" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <div type="info" v-if="scope.row.superiorCode != null">-->
      <!--            - -->
      <!--          </div>-->
      <!--          <el-tag type="danger" v-if="scope.row.superiorCode == null">-->
      <!--            {{ scope.row.institutionNumber }}-->
      <!--          </el-tag>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="用户类型" align="center" prop="isSimulation" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-tag type="danger" v-if="scope.row.isSimulation == 0">-->
      <!--            真实玩家-->
      <!--          </el-tag>-->
      <!--          <el-tag type="success" v-if="scope.row.isSimulation == 1">-->
      <!--            业务员-->
      <!--          </el-tag>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="总资产" align="center" prop="propertyMoney" width="100" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <div>-->
      <!--            {{ Number(scope.row.balance) + Number(scope.row.position) }}-->
      <!--          </div>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="可用资金" align="center" prop="balance" width="100" />-->
      <!--      <el-table-column label="持仓市值" align="center" prop="cityValue" width="100" />-->
      <!--      <el-table-column label="新股冻结" align="center" prop="freezeMoney" />-->
      <!--      <el-table-column label="总盈亏" align="center" prop="positionMoney" />-->
      <!--      <el-table-column label="T+1锁定" align="center" prop="freezeProfit" />-->
      <!--      <el-table-column label="杠杆" align="center" prop="isPz" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-switch-->
      <!--            v-model="scope.row.isPz"-->
      <!--            :active-value=1-->
      <!--            :inactive-value=0-->
      <!--            @change="handleStatusChange(scope.row, 'peizi')"-->
      <!--          ></el-switch>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="大宗" align="center" prop="isDz" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-switch-->
      <!--            v-model="scope.row.isDz"-->
      <!--            :active-value=1-->
      <!--            :inactive-value=0-->
      <!--            @change="handleStatusChange(scope.row, 'dazong')"-->
      <!--          ></el-switch>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="配售" align="center" prop="isPs" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-switch-->
      <!--            v-model="scope.row.isPs"-->
      <!--            :active-value=1-->
      <!--            :inactive-value=0-->
      <!--            @change="handleStatusChange(scope.row, 'peishou')"-->
      <!--          ></el-switch>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="申购" align="center" prop="isSg" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-switch-->
      <!--            v-model="scope.row.isSg"-->
      <!--            :active-value=1-->
      <!--            :inactive-value=0-->
      <!--            @change="handleStatusChange(scope.row, 'shengou')"-->
      <!--          ></el-switch>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="指数" align="center" prop="isZs" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-switch-->
      <!--            v-model="scope.row.isZs"-->
      <!--            :active-value=1-->
      <!--            :inactive-value=0-->
      <!--            @change="handleStatusChange(scope.row, 'zhishu')"-->
      <!--          ></el-switch>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="抢筹" align="center" prop="isQc" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-switch-->
      <!--            v-model="scope.row.isQc"-->
      <!--            :active-value=1-->
      <!--            :inactive-value=0-->
      <!--            @change="handleStatusChange(scope.row, 'qiangchou')"-->
      <!--          ></el-switch>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="期货" align="center" prop="isQh" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-switch-->
      <!--            v-model="scope.row.isQh"-->
      <!--            :active-value=1-->
      <!--            :inactive-value=0-->
      <!--            @change="handleStatusChange(scope.row, 'qihuo')"-->
      <!--          ></el-switch>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column :label="$t('登录IP')" align="center" prop="loginIp" :min-width="$getColumnWidth('loginIp',memberList)" />
      <el-table-column :label="$t('登录时间')" align="center" prop="loginTime" :min-width="$getColumnWidth('loginTime',memberList)">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('注册时间')" align="center" prop="createTime" :min-width="$getColumnWidth('createTime',memberList)">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('注册域名')" align="center" prop="loginDomain" :min-width="$getColumnWidth('loginDomain',memberList)" />
      <el-table-column :label="$t('资金')" align="center" class-name="small-padding fixed-width" fixed="right" width="80">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleRowClick(scope.row)"
            v-hasPermi="['biz:member:edit']"
          >{{ $t('查看') }}</el-button>

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

    <!-- 添加或修改会员管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <!--        <el-form-item label="虚拟" prop="isSimulation">-->
        <!--          <el-select-->
        <!--            v-model="form.isSimulation"-->
        <!--            placeholder="虚拟"-->
        <!--          >-->
        <!--            <el-option label="是" value="1"/>-->
        <!--            <el-option v-if="form.id != null" label="否" value="0"/>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <el-form-item :label="$t('上级归属')" prop="dailiId">
          <el-cascader
            v-model="form.dailiId"
            :options="dailiList"
            :props="{ value: 'userId', label: 'nickName',children: 'children', emitPath: false, checkStrictly: true }"
          ></el-cascader>
        </el-form-item>
        <!--        <el-form-item :label="$t('手机号')" prop="mobile">-->
        <!--          <el-input v-model="form.mobile" :placeholder="$t('请输入手机号')" disabled />-->
        <!--        </el-form-item>-->
        <el-form-item :label="$t('唯一码')" prop="weiyima">
          <el-input v-model="form.weiyima" :placeholder="$t('请输入手机号')" disabled />
        </el-form-item>
        <!--        <el-form-item label="用户名" prop="username">-->
        <!--          <el-input v-model="form.username" placeholder="请输入用户名" />-->
        <!--        </el-form-item>-->
        <el-form-item :label="$t('实名姓名')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('请输入实名姓名')" />
        </el-form-item>
        <el-form-item :label="$t('登录密码')" prop="password">
          <el-input v-model="form.password" :placeholder="$t('请输入登录密码')" />
        </el-form-item>
        <el-form-item :label="$t('支付密码')" prop="paymentCode">
          <el-input v-model="form.paymentCode" :placeholder="$t('请输入支付密码')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 身份证认证 -->
    <el-dialog :title="title" :visible.sync="openAuth" width="600px" append-to-body>
      <el-form ref="authForm" :model="form" :rules="authRules" label-width="120px">
        <el-form-item :label="$t('认证姓名')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('请输入认证姓名')" />
        </el-form-item>
        <el-form-item :label="$t('身份证号码')" prop="idCard">
          <el-input v-model="form.idCard" :placeholder="$t('请输入身份证号码')" />
        </el-form-item>
        <el-form-item :label="$t('身份证正面照片')" prop="idCardFrontImage">
          <image-upload v-model="form.idCardFrontImage" :limit="1" />
        </el-form-item>
        <el-form-item :label="$t('身份证反面照片')" prop="idCardBackImage">
          <image-upload v-model="form.idCardBackImage" :limit="1" />
        </el-form-item>
        <el-form-item :label="$t('审核')" prop="isAuth">
          <el-select
            v-model="form.isAuth"
            :placeholder="$t('审核')"
          >
            <el-option :label="$t('未实名')" :value="0"/>
            <el-option :label="$t('审核中')" :value="1"/>
            <el-option :label="$t('已实名')" :value="2"/>
            <el-option :label="$t('审核驳回')" :value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('驳回原因')" prop="authRejectReason" v-if="form.isAuth === 3">
          <el-input v-model="form.authRejectReason" :placeholder="$t('请输入驳回原因')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAuthForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 绑定银行卡 -->
    <el-dialog :title="title" :visible.sync="openBinding" width="600px" append-to-body>
      <el-form ref="bindingForm" :model="form" :rules="bindingRules" label-width="120px">
        <el-form-item :label="$t('收款人姓名')" prop="accountName">
          <el-input v-model="form.accountName" :placeholder="$t('请输入收款人姓名')" />
        </el-form-item>
        <el-form-item :label="$t('收款人账户')" prop="account">
          <el-input v-model="form.account" :placeholder="$t('请输入收款人账户')" />
        </el-form-item>
        <el-form-item :label="$t('银行编码')" prop="depositBank">
          <el-input v-model="form.depositBank" :placeholder="$t('请输入银行编码')" />
        </el-form-item>
        <el-form-item :label="$t('审核')" prop="bankCardAuth">
          <el-select
            v-model="form.bankCardAuth"
            :placeholder="$t('审核')"
          >
            <el-option :label="$t('未绑卡')" :value="0"/>
            <el-option :label="$t('审核中')" :value="1"/>
            <el-option :label="$t('已绑卡')" :value="2"/>
            <el-option :label="$t('审核驳回')" :value="3"/>
          </el-select>
        </el-form-item>
        <!--        <el-form-item label="开户支行" prop="khzhihang">-->
        <!--          <el-input v-model="form.khzhihang" placeholder="请输入开户支行" />-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBindingForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 充值 -->
    <el-dialog :title="title" :visible.sync="openRecharge" width="600px" append-to-body>
      <el-form ref="rechargeForm" :model="form" :rules="rechargeRules" label-width="120px">
        <el-form-item :label="$t('姓名')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('请输入姓名')" disabled />
        </el-form-item>
        <!--        <el-form-item :label="$t('手机号')" prop="mobile">-->
        <!--          <el-input v-model="form.mobile" :placeholder="$t('请输入手机号')" disabled />-->
        <!--        </el-form-item>-->
        <el-form-item :label="$t('唯一码')" prop="weiyima">
          <el-input v-model="form.weiyima" :placeholder="$t('请输入手机号')" disabled />
        </el-form-item>
        <el-form-item :label="$t('余额')" prop="balance">
          <el-input v-model="form.balance" :placeholder="$t('请输入余额')" disabled />
        </el-form-item>
        <el-form-item :label="$t('资金池余额')" prop="zjcBalance" v-if="this.form.rechargeType === 3">
          <el-input v-model="form.zjcBalance" :placeholder="$t('请输入余额')" disabled />
        </el-form-item>
        <!--        <el-form-item :label="$t('锁定金额')" prop="freezeProfit">-->
        <!--          <el-input v-model="form.freezeProfit" :placeholder="$t('请输入余额')" disabled />-->
        <!--        </el-form-item>-->
        <el-form-item :label="$t('金额加减')" prop="direct">
          <el-radio v-model="form.direct" :label="0">{{ $t('加') }}</el-radio>
          <el-radio v-model="form.direct" :label="1">{{ $t('减') }}</el-radio>
        </el-form-item>
        <el-form-item :label="$t('资金池')" prop="direct" v-if="this.form.rechargeType === 3">
          <el-radio v-model="form.direct" :label="0">{{ $t('资金池划出') }}</el-radio>
          <el-radio v-model="form.direct" :label="1">{{ $t('划入资金池') }}</el-radio>
        </el-form-item>
        <el-form-item :label="$t('充值渠道')" prop="payType" v-if="this.form.rechargeType === 0">
          <el-radio v-model="form.payType" :label="4">{{ $t('三方存管') }}</el-radio>
          <el-radio v-model="form.payType" :label="1">{{ $t('支付宝') }}</el-radio>
          <el-radio v-model="form.payType" :label="2">{{ $t('对公转账') }}</el-radio>
          <el-radio v-model="form.payType" :label="3">{{ $t('现金转账') }}</el-radio>
        </el-form-item>
        <!--        <el-form-item :label="$t('类型')" prop="rechargeType">-->
        <!--          <el-radio v-model="form.rechargeType" :label="0">{{ $t('充值') }}</el-radio>-->
        <!--          <el-radio v-model="form.rechargeType" :label="1">{{ $t('调整余额') }}</el-radio>-->
        <!--          <el-radio v-model="form.rechargeType" :label="2">{{ $t('调整锁定金额') }}</el-radio>-->
        <!--        </el-form-item>-->
        <el-form-item :label="$t('金额')" prop="amount">
          <el-input v-model="form.amount" :placeholder="$t('请输入金额')" />
        </el-form-item>
        <el-form-item :label="$t('备注')" prop="remark" v-if="form.rechargeType === 0">
          <el-input v-model="form.remark" :placeholder="$t('请输入备注')" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createTimeFundPool" v-if="this.form.rechargeType === 3">
          <el-input v-model="form.createTimeFundPool" placeholder="请输入创建时间" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRecharge">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 个人资金详情 -->
    <el-dialog :title="title" :visible.sync="openMemberStatistics" width="600px" append-to-body>
      <el-form ref="fundForm" label-width="200px">
        <el-form-item :label="$t('总资产')">
          <el-input :value="memberStatistics.fund" disabled />
        </el-form-item>
        <el-form-item :label="$t('可用资金')">
          <el-input :value="memberStatistics.balance" disabled />
        </el-form-item>
        <el-form-item :label="$t('可取资金')">
          <el-input :value="memberStatistics.cash" disabled />
        </el-form-item>
        <el-form-item :label="$t('T+N冻结')">
          <el-input :value="memberStatistics.freezeProfit" disabled />
        </el-form-item>
        <el-form-item :label="$t('新股申购冻结资金')">
          <el-input :value="memberStatistics.sgFreeze" disabled />
        </el-form-item>
        <el-form-item :label="$t('新股配售冻结资金')">
          <el-input :value="memberStatistics.psFreeze" disabled />
        </el-form-item>
        <el-form-item :label="$t('已上市持仓市值')">
          <el-input :value="memberStatistics.listedHold" disabled />
        </el-form-item>
        <el-form-item :label="$t('未上市持仓市值')">
          <el-input :value="memberStatistics.unlistedHold" disabled />
        </el-form-item>
        <el-form-item :label="$t('充值总额')">
          <el-input :value="memberStatistics.recharge" disabled />
        </el-form-item>
        <el-form-item :label="$t('提现总额')">
          <el-input :value="memberStatistics.withdraw" disabled />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listMember, getMember, delMember, addMember, updateMember, changeMemberStatus,
  getDailiList, submitAuthMember, submitBindingBank, submitRecharge, getMemberStatistics,
  batchAuthMember, getMemberStatisticsSingle, getMobile } from "@/api/biz/member/member";
import { checkPermi } from "@/utils/permission";

export default {
  name: "Member",
  data() {
    return {
      show_mobile: '0',
      show_fundPool: '0',
      showMobileNumber: null,
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
    this.getList();
    this.getDailiList();
    this.getInfoByNames();
  },
  methods: {
    getInfoByNames() {
      this.getInfoByName({"name":"show_fundPool"}).then(res => {
        this.show_fundPool = res.data.value;
      }).catch(err => {
        console.log(err);
      });
      this.getInfoByName({"name":"show_mobile"}).then(res => {
        this.show_mobile = res.data.value;
      }).catch(err => {
        console.log(err);
      });
    },
    showMobile(row) {
      getMobile({"id": row.id}).then(res => {
        this.showMobileNumber = res.msg;
      });
    },
    // startTimer() {
    //   this.timerId = setInterval(() => {
    //     this.handleQuery();
    //   }, 30000); // 设置间隔为30秒
    // },
    checkPermi,
    handleRowClick(row, column, event) {
      getMemberStatisticsSingle({"id": row.id}).then(res => {
        this.memberStatistics = res.data;
        this.title = this.$t("资产");
        this.openMemberStatistics = true;
      });
    },
    getMemberStatistics() {
      let search = this.addDateRange(this.queryParams, this.dateRange1, "LoginTime");
      search = this.addDateRange(this.queryParams, this.dateRange2, "CreateTime");
      getMemberStatistics(search).then(res => {
        this.statistics = res.data;
      });
    },
    /** 查询会员管理列表 */
    getList() {
      this.loading = true;
      let search = this.addDateRange(this.queryParams, this.dateRange1, "LoginTime");
      search = this.addDateRange(this.queryParams, this.dateRange2, "CreateTime");
      listMember(search).then(response => {
        this.memberList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      this.getMemberStatistics();
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
        zjcBalance: null,
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
        withdrawSwitch: null,
        createTimeFundPool: null,
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
        if (0 === rechargeType) {
          this.title = this.$t("充值");
        } else if (1 === rechargeType) {
          this.title = this.$t("调整余额");
        } else if (2 === rechargeType) {
          this.title = this.$t("调整锁定金额");
        } else if (3 === rechargeType) {
          this.title = this.$t("资金池划转");
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
      } else if (type == 'withdraw') {
        newStatus = row.withdrawSwitch;
        text = row.withdrawSwitch == 0 ? this.$t("禁止提现") : this.$t("允许提现");
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
        } else if (type == 'withdraw') {
          row.withdrawSwitch = oldStatus;
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
