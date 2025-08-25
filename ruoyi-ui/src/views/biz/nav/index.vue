<template>
  <div class="app-container">

    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane :label="$t('全部')" name="all"></el-tab-pane>
      <el-tab-pane :label="$t('首页菜单')" name="1"></el-tab-pane>
      <el-tab-pane :label="$t('个人中心')" name="2"></el-tab-pane>
      <el-tab-pane :label="$t('查询记录')" name="3"></el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px" label-position="right">
      <el-col :span="6">
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="queryParams.title"
            placeholder="请输入标题"
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
          v-hasPermi="['biz:nav:add']"
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
          v-hasPermi="['biz:nav:edit']"
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
          v-hasPermi="['biz:nav:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:nav:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="navList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="图片" align="center" prop="image" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.image" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="链接" align="center" prop="link" width="400" />
      <el-table-column label="类型" align="center" prop="type" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 1">首页菜单</el-tag>
          <el-tag type="success" v-if="scope.row.type === 2">个人中心</el-tag>
          <el-tag type="info" v-if="scope.row.type === 3">查询记录</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="跳转类型(0普通页面1权限页面2跳转浏览器)" align="center" prop="linkType" />-->
      <el-table-column label="顺序" align="center" prop="sort" />
      <el-table-column :label="$t('隐藏/显示')" align="center" prop="status" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value=0
            :inactive-value=1
            @change="handleSwitchChange(scope.row, 'show')"
          ></el-switch>
        </template>
      </el-table-column>
<!--      <el-table-column label="创建时间" align="center" prop="createTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:nav:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:nav:remove']"
          >删除</el-button>
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

    <!-- 添加或修改导航图标对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <image-upload v-model="form.image" :limit="1"/>
        </el-form-item>
        <el-form-item label="链接" prop="link">
          <el-input v-model="form.link" placeholder="请输入链接" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" >
            <el-option label="首页菜单" :value="1" />
            <el-option label="个人中心" :value="2" />
            <el-option label="查询记录" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="顺序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入顺序" />
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
import { listNav, getNav, delNav, addNav, updateNav, changeSwitch } from "@/api/biz/nav/nav";

export default {
  name: "Nav",
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
      // 导航图标表格数据
      navList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        title: null,
        image: null,
        link: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        type: null,
        linkType: null,
        status: null,
        sort: null,
        deleteFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "请输入标题", trigger: "blur" }
        ],
        link: [
          { required: true, message: "请输入链接", trigger: "blur" }
        ],
        type: [
          { required: true, message: "请选择类型", trigger: "change" }
        ],
        sort: [
          { required: true, message: "请输入顺序", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 显示隐藏修改
    handleSwitchChange(row, type) {
      let newStatus;
      let text;
      if (type == 'show') {
        newStatus = row.status;
        text = row.status == 0 ? this.$t("显示") : this.$t("隐藏");
      } else {
        return;
      }

      let oldStatus = newStatus == 0 ? 1 : 0;

      this.$modal.confirm(this.$t('确认要') + " " + text + '？').then(function() {
        return changeSwitch({"id" : row.id, "switchStatus" : newStatus, "switchType" : type});
      }).then(() => {
        this.$modal.msgSuccess(text + " " + this.$t("成功"));
      }).catch(function() {
        if (type == 'show') {
          row.status = oldStatus;
        }
      });
    },
    handleClick(tab, event) {
      if ("all" == tab.name) {
        this.resetForm("queryForm");
        this.queryParams.type = null;
      } else if ("1" == tab.name) {
        this.queryParams.type = 1;
      } else if ("2" == tab.name) {
        this.queryParams.type = 2;
      } else if ("3" == tab.name) {
        this.queryParams.type = 3;
      }
      this.handleQuery();
    },
    /** 查询导航图标列表 */
    getList() {
      this.loading = true;
      listNav(this.queryParams).then(response => {
        this.navList = response.rows;
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
        title: null,
        image: null,
        link: null,
        createTime: null,
        updateTime: null,
        deleteTime: null,
        type: null,
        linkType: null,
        status: null,
        sort: null,
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
      this.title = "添加导航图标";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getNav(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改导航图标";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNav(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNav(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除导航图标编号为"' + ids + '"的数据项？').then(function() {
        return delNav(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/nav/export', {
        ...this.queryParams
      }, `nav_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
