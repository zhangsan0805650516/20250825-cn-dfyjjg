<template>
  <div class="app-container">

    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane :label="$t('全部')" name="all"></el-tab-pane>
      <el-tab-pane :label="$t('显示')" name="show"></el-tab-pane>
      <el-tab-pane :label="$t('隐藏')" name="hide"></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('股票名称')" prop="name">
        <el-input
          v-model="queryParams.name"
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
      <el-form-item :label="$t('申购状态')" prop="sgSwitch">
        <el-select
          v-model="queryParams.sgSwitch"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('关闭')" :value="0"/>
          <el-option :label="$t('打开')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('配售状态')" prop="xxSwitch">
        <el-select
          v-model="queryParams.xxSwitch"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('关闭')" :value="0"/>
          <el-option :label="$t('打开')" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('显示开关')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('隐藏')" :value="0"/>
          <el-option :label="$t('显示')" :value="1"/>
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
          v-hasPermi="['biz:shengou:add']"
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
          v-hasPermi="['biz:shengou:edit']"
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
          v-hasPermi="['biz:shengou:remove']"
        >{{ $t('删除') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:shengou:export']"
        >{{ $t('导出') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shengouList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('操作')" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:shengou:edit']"
          ><span style="margin: 5px">修改</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:shengou:remove']"
          ><span style="margin: 5px">删除</span></el-button>
        </template>
      </el-table-column>
      <el-table-column label="股票代码" align="center" prop="allCode" width="100" />
      <el-table-column :label="$t('股票名称')" align="center" prop="name" />
<!--      <el-table-column :label="$t('股票代码')" align="center" prop="code" />-->
      <el-table-column label="类型" align="center" prop="sgType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.exchange_type" :value="scope.row.sgType"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('申购代码')" align="center" prop="sgCode" />
<!--      <el-table-column label="申购代码" align="center" prop="sgCode" />-->
      <el-table-column label="申购上限(万股)" align="center" prop="sgLimit" width="120"/>
      <el-table-column :label="$t('订单上限')" align="center" prop="ddLimit" />
      <el-table-column :label="$t('发行价格')" align="center" prop="fxPrice" />
      <el-table-column :label="$t('发行市盈率')" align="center" prop="fxRate" width="100" />
      <el-table-column :label="$t('申购日期')" align="center" prop="sgDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sgDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('上市日期')" align="center" prop="ssDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.ssDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('隐藏/显示')" align="center" prop="status" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'show')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('申购状态')" align="center" prop="sgSwitch" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.sgSwitch"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'sg')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('剩余申购数量')" align="center" prop="sgNums" width="100" />
      <el-table-column :label="$t('配售价格')" align="center" prop="psPrice" width="100" />
      <el-table-column :label="$t('配售状态')" align="center" prop="xxSwitch" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.xxSwitch"
            :active-value=1
            :inactive-value=0
            @change="handleSwitchChange(scope.row, 'xx')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('剩余配售数量')" align="center" prop="xxNums" width="100" />
      <el-table-column :label="$t('配售密钥')" align="center" prop="content" />
<!--      <el-table-column label="配售周期" align="center" prop="tn" />-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改新股列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="股票代码" prop="allCode">
          <el-input v-model="form.allCode" placeholder="请输入股票代码" />
        </el-form-item>
        <el-form-item :label="$t('股票名称')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('请输入股票名称')" />
        </el-form-item>
        <el-form-item :label="$t('类型')" prop="sgType">
          <el-select
            v-model="form.sgType"
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
        <el-form-item label="申购代码" prop="sgCode">
          <el-input v-model="form.sgCode" placeholder="请输入申购代码" />
        </el-form-item>
        <el-form-item :label="$t('申购上限(万股)')" prop="sgLimit">
          <el-input v-model="form.sgLimit" :placeholder="$t('请输入申购上限')" />
        </el-form-item>
        <el-form-item :label="$t('订单上限')" prop="ddLimit">
          <el-input v-model="form.ddLimit" :placeholder="$t('请输入订单上限')" />
        </el-form-item>
        <el-form-item :label="$t('发行价格')" prop="fxPrice">
          <el-input v-model="form.fxPrice" :placeholder="$t('请输入发行价格')" />
        </el-form-item>
        <el-form-item :label="$t('发行市盈率')" prop="fxRate">
          <el-input v-model="form.fxRate" :placeholder="$t('请输入发行市盈率')" />
        </el-form-item>
        <el-form-item :label="$t('申购日期')" prop="sgDate">
          <el-date-picker clearable
                          v-model="form.sgDate"
                          type="date"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          :placeholder="$t('请选择申购日期')">
          </el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('上市日期')" prop="ssDate">
          <el-date-picker clearable
                          v-model="form.ssDate"
                          type="date"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          :placeholder="$t('请选择上市日期')">
          </el-date-picker>
        </el-form-item>
<!--        <el-form-item :label="$t('手动设置申购日期')" prop="sgDateN">-->
<!--          <el-date-picker clearable-->
<!--                          v-model="form.sgDateN"-->
<!--                          type="date"-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          :placeholder="$t('请选择手动设置申购日期')">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
        <el-form-item :label="$t('配售价格')">
          <el-input v-model="form.psPrice" :placeholder="$t('请输入配售价格')" />
        </el-form-item>
        <el-form-item :label="$t('配售密钥')">
          <el-input v-model="form.content" :placeholder="$t('请输入配售密钥')" />
        </el-form-item>
<!--        <el-form-item label="配售周期" prop="tn">-->
<!--          <el-input v-model="form.tn" placeholder="请输入配售周期" />-->
<!--        </el-form-item>-->
        <el-form-item :label="$t('剩余申购数量')" prop="sgNums">
          <el-input v-model="form.sgNums" :placeholder="$t('请输入剩余申购数量')" />
        </el-form-item>
        <el-form-item :label="$t('剩余配售数量')" prop="xxNums">
          <el-input v-model="form.xxNums" :placeholder="$t('请输入剩余配售数量')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

    <!-- 修改申购配售状态/交易所 -->
    <el-dialog :title="title" :visible.sync="openExchange" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rulesExchange" label-width="150px">
        <el-form-item label="申购代码" prop="sgCode">
          <el-input v-model="form.sgCode" placeholder="请输入申购代码" disabled />
        </el-form-item>
        <el-form-item label="股票名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入股票名称" disabled />
        </el-form-item>
        <el-form-item label="交易所" prop="sgExchange" v-if="form.sgOrPs === 0">
          <el-radio v-model="form.sgExchange" :label="1">NSE</el-radio>
          <el-radio v-model="form.sgExchange" :label="2">BSE</el-radio>
        </el-form-item>
        <el-form-item label="交易所" prop="xxExchange" v-if="form.sgOrPs === 1">
          <el-radio v-model="form.xxExchange" :label="1">NSE</el-radio>
          <el-radio v-model="form.xxExchange" :label="2">BSE</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitExchange">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listShengou, getShengou, delShengou, addShengou, updateShengou, changeSwitch, submitExchange } from "@/api/biz/shengou/shengou";
import {searchStock} from "@/api/biz/strategy/strategy";

export default {
  name: "Shengou",
  dicts: ['exchange_type'],
  data() {
    return {
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
      // 新股列表表格数据
      shengouList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openExchange: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        code: null,
        allCode: null,
        name: null,
        sgCode: null,
        fxNum: null,
        wsfxNum: null,
        sgLimit: null,
        ddLimit: null,
        dgLimit: null,
        fxPrice: null,
        fxRate: null,
        hyRate: null,
        sgDateInt: null,
        sgDate: null,
        sgDateXq: null,
        zqRate: null,
        zqNo: null,
        zqJkDate: null,
        ssDateInt: null,
        ssDate: null,
        dxValue: null,
        ztNum: null,
        createTime: null,
        updateTime: null,
        status: null,
        sgSwitch: null,
        xxSwitch: null,
        sgSwitchTime: null,
        xxSwitchTime: null,
        content: null,
        sgNums: null,
        xxNums: null,
        sgType: null,
        tn: null,
        sgDateN: null,
        deleteFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: this.$t("请选择股票"), trigger: 'change' }
        ],
        // fxPrice: [
        //   { required: true, message: this.$t("发行价格不能为空"), trigger: "blur" }
        // ],
        // fxRate: [
        //   { required: true, message: this.$t("发行市盈率不能为空"), trigger: "blur" }
        // ],
      },
      rulesExchange: {
      },
      stockOptions: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    selectStock(item) {

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
    handleClick(tab, event) {
      if ("all" == tab.name) {
        this.resetForm("queryForm");
      } else if ("show" == tab.name) {
        this.queryParams.status = 1;
      } else if ("hide" == tab.name) {
        this.queryParams.status = 0;
      }
      this.handleQuery();
    },
    // 申购配售状态修改
    handleSwitchChange(row, type) {
      let newStatus;
      let text;
      if (type == 'sg') {
        newStatus = row.sgSwitch;
        text = row.sgSwitch == 0 ? this.$t("关闭申购") : this.$t("打开申购");
        // if (newStatus == 1) {
        //   this.reset();
        //   const id = row.id || this.ids
        //   getShengou(id).then(response => {
        //     this.form = response.data;
        //     this.form.sgOrPs = 0;
        //     this.form.newStatus = newStatus;
        //     this.form.type = type;
        //     this.openExchange = true;
        //     this.title = text;
        //   });
        //   return;
        // }
      } else if (type == 'xx') {
        newStatus = row.xxSwitch;
        text = row.xxSwitch == 0 ? this.$t("关闭配售") : this.$t("打开配售");
        // if (newStatus == 1) {
        //   this.reset();
        //   const id = row.id || this.ids
        //   getShengou(id).then(response => {
        //     this.form = response.data;
        //     this.form.sgOrPs = 1;
        //     this.form.newStatus = newStatus;
        //     this.form.type = type;
        //     this.openExchange = true;
        //     this.title = text;
        //   });
        //   return;
        // }
      } else if (type == 'show') {
        newStatus = row.status;
        text = row.status == 0 ? this.$t("隐藏") : this.$t("显示");
      } else {
        return;
      }

      let oldStatus = newStatus == 0 ? 1 : 0;

      this.$modal.confirm(this.$t('确认要') + " " + text + '？').then(function() {
        return changeSwitch({"id" : row.id, "switchStatus" : newStatus, "switchType" : type});
      }).then(() => {
        this.$modal.msgSuccess(text + " " + this.$t("成功"));
      }).catch(function() {
        if (type == 'sg') {
          row.sgSwitch = oldStatus;
        } else if (type == 'xx') {
          row.xxSwitch = oldStatus;
        } else if (type == 'show') {
          row.status = oldStatus;
        }
      });
    },
    /** 查询新股列表列表 */
    getList() {
      this.loading = true;
      listShengou(this.queryParams).then(response => {
        this.shengouList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openExchange = false;
      this.reset();
      this.getList();
    },
    // 表单重置
    reset() {
      this.form = {
        type: null,
        newStatus: null,
        id: null,
        code: null,
        allCode: null,
        name: null,
        sgCode: null,
        fxNum: null,
        wsfxNum: null,
        sgLimit: null,
        ddLimit: null,
        dgLimit: null,
        fxPrice: null,
        fxRate: null,
        hyRate: null,
        sgDateInt: null,
        sgDate: null,
        sgDateXq: null,
        zqRate: null,
        zqNo: null,
        zqJkDate: null,
        ssDateInt: null,
        ssDate: null,
        dxValue: null,
        ztNum: null,
        createTime: null,
        updateTime: null,
        status: null,
        sgSwitch: null,
        sgExchange: null,
        xxSwitch: null,
        xxExchange: null,
        sgSwitchTime: null,
        xxSwitchTime: null,
        content: null,
        sgNums: null,
        xxNums: null,
        sgType: null,
        tn: null,
        sgDateN: null,
        deleteFlag: null,
        sgOrPs: null,
        psPrice: null,
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
      this.title = this.$t("添加新股");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getShengou(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t("修改新股");
      });
    },
    // 提交修改申购配售
    submitExchange() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          submitExchange({"id" : this.form.id, "switchStatus" : this.form.newStatus, "switchType" : this.form.type, "sgOrPs": this.form.sgOrPs, "sgExchange": this.form.sgExchange, "xxExchange": this.form.xxExchange }).then(response => {
            this.$modal.msgSuccess(this.$t("成功"));
            this.openExchange = false;
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
            updateShengou(this.form).then(response => {
              this.$modal.msgSuccess(this.$t("成功"));
              this.open = false;
              this.getList();
            });
          } else {
            addShengou(this.form).then(response => {
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
        return delShengou(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t("成功"));
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/shengou/export', {
        ...this.queryParams
      }, `shengou_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
