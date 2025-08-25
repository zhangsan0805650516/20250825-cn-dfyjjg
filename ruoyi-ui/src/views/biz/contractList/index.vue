<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="手机号" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="唯一码" prop="weiyima">
        <el-input
          v-model="queryParams.weiyima"
          placeholder="请输入唯一码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="签名姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入签名姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证号" prop="idCard">
        <el-input
          v-model="queryParams.idCard"
          placeholder="请输入身份证号"
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
          v-hasPermi="['biz:contractList:add']"
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
          v-hasPermi="['biz:contractList:edit']"
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
          v-hasPermi="['biz:contractList:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:contractList:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="contractListList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一码" align="center" prop="mobile" width="180" v-if="show_mobile === '0'">
        <template slot-scope="scope">
          {{ scope.row.faMember.weiyima?scope.row.faMember.weiyima:"" }}
        </template>
      </el-table-column>
      <el-table-column label="手机号/唯一码" align="center" prop="mobile" width="180" v-if="show_mobile === '1'">
        <template slot-scope="scope">
          {{ scope.row.faMember.mobile?scope.row.faMember.mobile:"" }}/{{ scope.row.faMember.weiyima?scope.row.faMember.weiyima:"" }}
        </template>
      </el-table-column>
      <el-table-column label="签约姓名" align="center" prop="name" />
      <el-table-column label="身份证号" align="center" prop="idCard" />
      <el-table-column label="用户地址" align="center" prop="address" />
      <el-table-column label="合同名称" align="center" prop="contractName" width="200" >
        <template slot-scope="scope">
          {{ scope.row.faContractTemplate.name }}
        </template>
      </el-table-column>
      <el-table-column label="合同地址" align="center" prop="contractUrl" width="200" >
        <template slot-scope="scope">
          <el-button type="primary">
            <a :href="scope.row.contractUrl" target="_blank">/contract/info?id={{ scope.row.id }}</a>
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="signStatus" >
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.signStatus === 1">已签约</el-tag>
          <el-tag type="info" v-if="scope.row.signStatus === 0">未签约</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="签约时间" align="center" prop="signDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:contractList:edit']"
          ><span style="margin: 5px">修改</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:contractList:remove']"
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

    <!-- 添加或修改用户合同对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('用户')" prop="userId">
          <el-select
            v-model="form.userId"
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

        <el-form-item label="合同模板" prop="templateId">
          <el-select v-model="form.templateId">
            <el-option
              v-for="item in contractTemplate"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="签约姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入签约姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="用户地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入用户地址" />
        </el-form-item>
        <el-form-item label="签约时间" prop="signDate">
          <el-date-picker clearable
                          v-model="form.signDate"
                          type="date"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择签约时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('状态')" prop="signStatus">
          <el-radio v-model="form.signStatus" :label="0" >{{ $t('未签约') }}</el-radio>
          <el-radio v-model="form.signStatus" :label="1">{{ $t('已签约') }}</el-radio>
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
import { listContractList, getContractList, delContractList, addContractList, updateContractList } from "@/api/biz/contractList/contractList";
import { searchMember } from "@/api/biz/member/member";
import { listContractTemplate } from "@/api/biz/contractTemplate/contractTemplate";

export default {
  name: "ContractList",
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
      // 用户合同表格数据
      contractListList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        templateId: null,
        userId: null,
        name: null,
        idCard: null,
        address: null,
        mainContent: null,
        image: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        weigh: null,
        signStatus: null,
        signDate: null,
        deleteFlag: null,
        mobile: null,
        weiyima: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户不能为空", trigger: "change" }
        ],
        templateId: [
          { required: true, message: "合同模板不能为空", trigger: "change" }
        ],
        name: [
          { required: true, message: "签约姓名不能为空", trigger: "blur" }
        ],
        idCard: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "用户地址不能为空", trigger: "blur" }
        ],
        signDate: [
          { required: true, message: "签约时间不能为空", trigger: "blur" }
        ],
      },
      memberOptions: [],
      contractTemplate: [],
    };
  },
  created() {
    this.getList();
    this.getContractTemplate();
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
    getContractTemplate() {
      listContractTemplate().then(res => {
        this.contractTemplate = res.rows;
      }).catch(err => {
        this.$modal.msgError(this.$t("合同模板数据错误"));
      })
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
    /** 查询用户合同列表 */
    getList() {
      this.loading = true;
      listContractList(this.queryParams).then(response => {
        this.contractListList = response.rows;
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
        templateId: null,
        userId: null,
        name: null,
        idCard: null,
        address: null,
        mainContent: null,
        image: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        weigh: null,
        signStatus: 0,
        signDate: null,
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
      this.title = "添加用户合同";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getContractList(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户合同";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateContractList(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addContractList(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户合同编号为"' + ids + '"的数据项？').then(function() {
        return delContractList(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/contractList/export', {
        ...this.queryParams
      }, `contractList_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
