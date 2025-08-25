<template>
  <div class="app-container">
<!--    <el-form ref="form" :model="form" :rules="rules" label-width="100px">-->
<!--      <el-form-item label="协议内容">-->
<!--        <editor v-model="form.content" :min-height="192" :height="300"/>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--    <el-form ref="form" :model="form" :rules="rules" label-width="100px">-->
<!--      <el-form-item label="关于我们">-->
<!--        <editor v-model="form.aboutContent" :min-height="192" :height="300"/>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="登录协议">
        <editor v-model="form.loginContent" :min-height="192" :height="300"/>
      </el-form-item>
    </el-form>
<!--    <el-form ref="form" :model="form" :rules="rules" label-width="100px">-->
<!--      <el-form-item label="免责声明">-->
<!--        <editor v-model="form.mzsmContent" :min-height="192" :height="300"/>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
    <div align="center">
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
  </div>
</template>

<script>
import { listAgreement, getAgreement, delAgreement, addAgreement, updateAgreement, getAgreementData } from "@/api/biz/agreement/agreement";

export default {
  name: "Agreement",
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
      // 关闭通道协议表格数据
      agreementList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        content: null,
        aboutContent: null,
        loginContent: null,
        createTime: null,
        updateTime: null,
        guanbiContent: null,
        mzsmContent: null,
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
    this.getAgreementData();
  },
  methods: {
    /** 获取数据 */
    getAgreementData() {
      this.reset();
      getAgreementData().then(response => {
        this.form = response.data;
      });
    },
    /** 查询关闭通道协议列表 */
    getList() {
      this.loading = true;
      listAgreement(this.queryParams).then(response => {
        this.agreementList = response.rows;
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
        content: null,
        aboutContent: null,
        loginContent: null,
        mzsmContent: null,
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
      this.title = "添加关闭通道协议";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAgreement(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改关闭通道协议";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAgreement(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              // this.open = false;
              this.getAgreementData();
            });
          }
          // else {
          //   addAgreement(this.form).then(response => {
          //     this.$modal.msgSuccess("新增成功");
          //     this.open = false;
          //     this.getList();
          //   });
          // }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除关闭通道协议编号为"' + ids + '"的数据项？').then(function() {
        return delAgreement(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/agreement/export', {
        ...this.queryParams
      }, `agreement_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
