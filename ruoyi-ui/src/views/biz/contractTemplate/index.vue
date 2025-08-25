<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px" label-position="right">
      <el-col :span="6">
        <el-form-item label="合同名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入合同名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
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
          v-hasPermi="['biz:contractTemplate:add']"
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
          v-hasPermi="['biz:contractTemplate:edit']"
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
          v-hasPermi="['biz:contractTemplate:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:contractTemplate:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="contractTemplateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同名称" align="center" prop="name" />
<!--      <el-table-column label="关键字" align="center" prop="keywords" />-->
<!--      <el-table-column label="描述" align="center" prop="description" />-->
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status === 0">正常</el-tag>
          <el-tag type="info" v-if="scope.row.status === 1">隐藏</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:contractTemplate:edit']"
          ><span style="margin: 5px">修改</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:contractTemplate:remove']"
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

    <!-- 添加或修改合同模板对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="合同名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入合同名称" />
        </el-form-item>
<!--        <el-form-item label="图片" prop="image">-->
<!--          <image-upload v-model="form.image"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="关键字" prop="keywords">-->
<!--          <el-input v-model="form.keywords" placeholder="请输入关键字" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="描述" prop="description">-->
<!--          <el-input v-model="form.description" placeholder="请输入描述" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="自定义名称" prop="diyName">-->
<!--          <el-input v-model="form.diyName" placeholder="请输入自定义名称" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="权重" prop="weigh">-->
<!--          <el-input v-model="form.weigh" placeholder="请输入权重" />-->
<!--        </el-form-item>-->
        <el-form-item label="内容">
          <editor v-model="form.mainContent" :min-height="192" :height="600" />
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
import { listContractTemplate, getContractTemplate, delContractTemplate, addContractTemplate, updateContractTemplate } from "@/api/biz/contractTemplate/contractTemplate";

export default {
  name: "ContractTemplate",
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
      // 合同模板表格数据
      contractTemplateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        pid: null,
        name: null,
        image: null,
        keywords: null,
        description: null,
        diyName: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        weigh: null,
        status: null,
        mainContent: null,
        deleteFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        pid: [
          { required: true, message: "父ID不能为空", trigger: "blur" }
        ],
        weigh: [
          { required: true, message: "权重不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询合同模板列表 */
    getList() {
      this.loading = true;
      listContractTemplate(this.queryParams).then(response => {
        this.contractTemplateList = response.rows;
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
        pid: null,
        name: null,
        image: null,
        keywords: null,
        description: null,
        diyName: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        weigh: null,
        status: null,
        mainContent: null,
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
      this.title = "添加合同模板";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getContractTemplate(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改合同模板";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateContractTemplate(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addContractTemplate(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除合同模板编号为"' + ids + '"的数据项？').then(function() {
        return delContractTemplate(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/contractTemplate/export', {
        ...this.queryParams
      }, `contractTemplate_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
