<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资产名称" prop="assetName">
        <el-input
          v-model="queryParams.assetName"
          placeholder="请输入资产名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
          v-hasPermi="['biz:collectiveAsset:add']"
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
          v-hasPermi="['biz:collectiveAsset:edit']"
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
          v-hasPermi="['biz:collectiveAsset:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:collectiveAsset:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="collectiveAssetList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="id" align="center" prop="id" />-->
      <el-table-column label="资产名称" align="center" prop="assetName" />
<!--      <el-table-column label="资产编码" align="center" prop="assetCode" />-->
      <el-table-column label="资产总额" align="center" prop="assetTotalAmount" />
      <el-table-column label="售出总额" align="center" prop="sellTotalAmount" />
<!--      <el-table-column label="可赎回总额" align="center" prop="assetRedeemAmount" />-->
<!--      <el-table-column label="赎回总额" align="center" prop="redeemTotalAmount" />-->
      <el-table-column label="邀请码" align="center" prop="assetSecret" />
<!--      <el-table-column label="状态" align="center" prop="status" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.status === 0">{{ $t('未开始') }}</el-tag>-->
<!--          <el-tag type="success" v-if="scope.row.status === 1">{{ $t('已开始') }}</el-tag>-->
<!--          <el-tag type="warning" v-if="scope.row.status === 2">{{ $t('已结束') }}</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150px">
        <template slot-scope="scope">
<!--          <el-button-->
<!--            v-if="scope.row.status === 0"-->
<!--            size="mini"-->
<!--            type="primary"-->
<!--            @click="startAsset(scope.row)"-->
<!--          ><span style="margin: 5px">开始</span></el-button>-->
<!--          <el-button-->
<!--            v-if="scope.row.status === 1"-->
<!--            size="mini"-->
<!--            type="primary"-->
<!--            @click="endAsset(scope.row)"-->
<!--          ><span style="margin: 5px">结束</span></el-button>-->
          <el-button
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:collectiveAsset:edit']"
          ><span style="margin: 5px">修改</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:collectiveAsset:remove']"
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

    <!-- 添加或修改集合资产对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="资产名称" prop="assetName">
          <el-input v-model="form.assetName" placeholder="请输入资产名称" />
        </el-form-item>
<!--        <el-form-item label="资产编码" prop="assetCode">-->
<!--          <el-input v-model="form.assetCode" placeholder="请输入资产编码" />-->
<!--        </el-form-item>-->
        <el-form-item label="资产总额" prop="assetTotalAmount">
          <el-input v-model="form.assetTotalAmount" placeholder="请输入资产总额" />
        </el-form-item>
        <el-form-item label="售出总额" prop="sellTotalAmount" v-if="form.id">
          <el-input v-model="form.sellTotalAmount" placeholder="请输入售出总额" />
        </el-form-item>
<!--        <el-form-item label="可赎回总额" prop="assetRedeemAmount" v-if="form.id">-->
<!--          <el-input v-model="form.assetRedeemAmount" placeholder="请输入资产可赎回总额" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="赎回总额" prop="redeemTotalAmount" v-if="form.id">-->
<!--          <el-input v-model="form.redeemTotalAmount" placeholder="请输入赎回总额" />-->
<!--        </el-form-item>-->
        <el-form-item label="邀请码" prop="assetSecret">
          <el-input v-model="form.assetSecret" placeholder="请输入邀请码" />
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
  </div>
</template>

<script>
import { listCollectiveAsset, getCollectiveAsset, delCollectiveAsset, addCollectiveAsset, updateCollectiveAsset, startAsset, endAsset } from "@/api/biz/collectiveAsset/collectiveAsset";

export default {
  name: "CollectiveAsset",
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
      // 集合资产表格数据
      collectiveAssetList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        assetName: null,
        assetCode: null,
        assetTotalAmount: null,
        sellTotalAmount: null,
        assetRedeemAmount: null,
        redeemTotalAmount: null,
        assetSecret: null,
        status: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remarks: null,
        deleteFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 集合资产开始 */
    startAsset(row) {
      this.$modal.confirm(this.$t("确认开始") + '？').then(function() {
        return startAsset({"id":row.id});
      }).then(() => {
        this.$modal.msgSuccess(this.$t("操作成功"));
        this.getList();
      }).catch(() => {});
    },
    /** 集合资产结束 */
    endAsset(row) {
      this.$modal.confirm(this.$t("确认结束") + '？').then(function() {
        return endAsset({"id":row.id});
      }).then(() => {
        this.$modal.msgSuccess(this.$t("操作成功"));
        this.getList();
      }).catch(() => {});
    },
    /** 查询集合资产列表 */
    getList() {
      this.loading = true;
      listCollectiveAsset(this.queryParams).then(response => {
        this.collectiveAssetList = response.rows;
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
        assetName: null,
        assetCode: null,
        assetTotalAmount: null,
        sellTotalAmount: null,
        assetRedeemAmount: null,
        redeemTotalAmount: null,
        assetSecret: null,
        status: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remarks: null,
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
      this.title = "添加集合资产";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCollectiveAsset(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改集合资产";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCollectiveAsset(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCollectiveAsset(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除集合资产编号为"' + ids + '"的数据项？').then(function() {
        return delCollectiveAsset(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/collectiveAsset/export', {
        ...this.queryParams
      }, `collectiveAsset_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
