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
      <el-form-item label="集合资产" prop="assetName">
        <el-input
          v-model="queryParams.assetName"
          placeholder="请输入集合资产"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('状态')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('请选择')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('待审核')" :value="0"/>
          <el-option :label="$t('成功')" :value="1"/>
          <el-option :label="$t('驳回')" :value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!--    <el-row :gutter="10" class="mb8">-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          plain-->
    <!--          icon="el-icon-plus"-->
    <!--          size="mini"-->
    <!--          @click="handleAdd"-->
    <!--          v-hasPermi="['biz:assetRecord:add']"-->
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
    <!--          v-hasPermi="['biz:assetRecord:edit']"-->
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
    <!--          v-hasPermi="['biz:assetRecord:remove']"-->
    <!--        >删除</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="warning"-->
    <!--          plain-->
    <!--          icon="el-icon-download"-->
    <!--          size="mini"-->
    <!--          @click="handleExport"-->
    <!--          v-hasPermi="['biz:assetRecord:export']"-->
    <!--        >导出</el-button>-->
    <!--      </el-col>-->
    <!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
    <!--    </el-row>-->

    <el-table v-loading="loading" :data="assetRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="id" align="center" prop="id" />-->
      <el-table-column :label="$t('姓名/唯一码')" align="center" prop="member" width="250" v-if="show_mobile === '0'">
        <template slot-scope="scope">
          {{ scope.row.faMember.name?scope.row.faMember.name:"" }}/{{ scope.row.faMember.weiyima?scope.row.faMember.weiyima:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('姓名/手机/唯一码')" align="center" prop="member" width="250" v-if="show_mobile === '1'">
        <template slot-scope="scope">
          {{ scope.row.faMember.name?scope.row.faMember.name:"" }}/{{ scope.row.faMember.mobile?scope.row.faMember.mobile:"" }}/{{ scope.row.faMember.weiyima?scope.row.faMember.weiyima:"" }}
        </template>
      </el-table-column>
      <el-table-column label="集合资产" align="center" prop="asset" >
        <template slot-scope="scope">
          {{ scope.row.faCollectiveAsset.assetName?scope.row.faCollectiveAsset.assetName:"" }}
        </template>
      </el-table-column>
      <el-table-column label="金额" align="center" prop="amount" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0">{{ $t('待审核') }}</el-tag>
          <el-tag type="success" v-if="scope.row.status === 1">{{ $t('成功') }}</el-tag>
          <el-tag type="warning" v-if="scope.row.status === 2">{{ $t('驳回') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="驳回原因" align="center" prop="rejectReason" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <!--      <el-table-column label="创建者" align="center" prop="createBy" />-->
      <!--      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">-->
      <!--        <template slot-scope="scope">-->
      <!--          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column label="更新者" align="center" prop="updateBy" />-->
      <!--      <el-table-column label="备注" align="center" prop="remarks" />-->
      <!--      <el-table-column label="删除标记" align="center" prop="deleteFlag" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="primary"
            @click="approve(scope.row)"
          ><span style="margin: 5px">审核</span></el-button>
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--            v-hasPermi="['biz:assetRecord:edit']"-->
          <!--          >修改</el-button>-->
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:assetRecord:remove']"
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

    <!-- 添加或修改集合资产记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户id" prop="memberId">
          <el-input v-model="form.memberId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="资产id" prop="assetId">
          <el-input v-model="form.assetId" placeholder="请输入资产id" />
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入金额" />
        </el-form-item>
        <el-form-item label="买入时间" prop="buyTime">
          <el-date-picker clearable
                          v-model="form.buyTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择买入时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="卖出时间" prop="sellTime">
          <el-date-picker clearable
                          v-model="form.sellTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择卖出时间">
          </el-date-picker>
        </el-form-item>
        <!--        <el-form-item label="备注" prop="remarks">-->
        <!--          <el-input v-model="form.remarks" placeholder="请输入备注" />-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="删除标记" prop="deleteFlag">-->
        <!--          <el-input v-model="form.deleteFlag" placeholder="请输入删除标记" />-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改充值对话框 -->
    <el-dialog :title="title" :visible.sync="openApprove" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('姓名')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('请输入姓名')" disabled />
        </el-form-item>
        <el-form-item :label="$t('手机号')" prop="mobile">
          <el-input v-model="form.mobile" :placeholder="$t('请输入手机号')" disabled />
        </el-form-item>
        <el-form-item :label="$t('唯一码')" prop="weiyima">
          <el-input v-model="form.weiyima" :placeholder="$t('请输入手机号')" disabled />
        </el-form-item>
        <el-form-item :label="$t('集合资产')" prop="assetName">
          <el-input v-model="form.assetName" :placeholder="$t('请输入手机号')" disabled />
        </el-form-item>
        <el-form-item :label="$t('赎回金额')" prop="amount">
          <el-input v-model="form.amount" :placeholder="$t('请输入充值金额')" disabled />
        </el-form-item>
        <el-form-item :label="$t('状态')" prop="status">
          <el-select
            v-model="form.status"
            :placeholder="$t('请选择')"
            clearable
            @keyup.enter.native="handleQuery"
          >
            <el-option :label="$t('待审核')" :value="0"/>
            <el-option :label="$t('成功')" :value="1"/>
            <el-option :label="$t('驳回')" :value="2"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('驳回原因')" prop="rejectReason" v-show="form.status === 2">
          <el-input v-model="form.rejectReason" :placeholder="$t('请输入驳回原因')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitApprove">{{ $t('确 定') }}</el-button>
        <el-button @click="cancel">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listAssetRecord, getAssetRecord, delAssetRecord, addAssetRecord, updateAssetRecord, approveRedeem } from "@/api/biz/assetRecord/assetRecord";

export default {
  name: "AssetRecord",
  data() {
    return {
      show_mobile: '0',
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
      // 集合资产记录表格数据
      assetRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openApprove: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        memberId: null,
        assetId: null,
        amount: null,
        buyTime: null,
        sellTime: null,
        status: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remarks: null,
        deleteFlag: null,
        type: null,
        name: null,
        mobile: null,
        weiyima: null,
        assetName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.queryParams.type = this.$route.query.type;
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
    /** 提交审核 */
    submitApprove() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          approveRedeem(this.form).then(response => {
            this.$modal.msgSuccess(this.$t("成功"));
            this.openApprove = false;
            this.getList();
          });
        }
      });
    },
    /** 审核按钮操作 */
    approve(row) {
      this.reset();
      const id = row.id || this.ids
      getAssetRecord(id).then(response => {
        this.form = response.data;

        this.form.name = response.data.faMember.name;
        this.form.mobile = response.data.faMember.mobile;
        this.form.weiyima = response.data.faMember.weiyima;
        this.form.assetName = response.data.faCollectiveAsset.assetName;

        this.openApprove = true;
        this.title = "审核赎回";
      });
    },
    /** 查询集合资产记录列表 */
    getList() {
      this.loading = true;
      listAssetRecord(this.queryParams).then(response => {
        this.assetRecordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openApprove = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        memberId: null,
        assetId: null,
        amount: null,
        buyTime: null,
        sellTime: null,
        status: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remarks: null,
        deleteFlag: null,
        name: null,
        mobile: null,
        weiyima: null,
        assetName: null,
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
      this.title = "添加集合资产记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAssetRecord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改集合资产记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAssetRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAssetRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除集合资产记录编号为"' + ids + '"的数据项？').then(function() {
        return delAssetRecord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/assetRecord/export', {
        ...this.queryParams
      }, `assetRecord_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
