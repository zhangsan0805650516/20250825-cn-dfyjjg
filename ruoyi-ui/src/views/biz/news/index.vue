<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-col :span="6">-->
<!--        <el-form-item label="展示方式" prop="showMode">-->
<!--          <el-select-->
<!--            v-model="queryParams.showMode"-->
<!--            placeholder="展示方式"-->
<!--            clearable-->
<!--            @keyup.enter.native="handleQuery"-->
<!--          >-->
<!--            <el-option label="垂直排列，略缩图显示大图" :value="1"/>-->
<!--            <el-option label="垂直排列，图文混排" :value="2"/>-->
<!--            <el-option label="垂直排列，略缩图显示多图" :value="3"/>-->
<!--            <el-option label="水平排列，左图右文" :value="4"/>-->
<!--            <el-option label="水平排列，右图左文" :value="5"/>-->
<!--            <el-option label="垂直排列，无略缩图，主标题+副标题显示" :value="6"/>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--      </el-col>-->
<!--      <el-col :span="6">-->
<!--        <el-form-item label="新闻id" prop="newsId">-->
<!--          <el-input-->
<!--            v-model="queryParams.newsId"-->
<!--            placeholder="请输入新闻id"-->
<!--            clearable-->
<!--            @keyup.enter.native="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
<!--      </el-col>-->
      <el-col :span="6">
        <el-form-item label="新闻标题" prop="newsTitle">
          <el-input
            v-model="queryParams.newsTitle"
            placeholder="请输入新闻标题"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="新闻摘要" prop="newsAbstract">
          <el-input
            v-model="queryParams.newsAbstract"
            placeholder="新闻摘要"
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
          v-hasPermi="['biz:news:add']"
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
          v-hasPermi="['biz:news:edit']"
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
          v-hasPermi="['biz:news:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:news:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="newsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="展示方式" align="center" prop="showMode" >-->
<!--        <template slot-scope="scope">-->
<!--          <span v-if="scope.row.showMode === 1">垂直排列，略缩图显示大图</span>-->
<!--          <span v-if="scope.row.showMode === 2">垂直排列，图文混排</span>-->
<!--          <span v-if="scope.row.showMode === 3">垂直排列，略缩图显示多图</span>-->
<!--          <span v-if="scope.row.showMode === 4">水平排列，左图右文</span>-->
<!--          <span v-if="scope.row.showMode === 5">水平排列，右图左文</span>-->
<!--          <span v-if="scope.row.showMode === 6">垂直排列，无略缩图，主标题+副标题显示</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="新闻id" align="center" prop="newsId" width="180"/>-->
      <el-table-column label="新闻标题" align="center" prop="newsTitle" :show-overflow-tooltip="true" />
      <el-table-column label="新闻摘要" align="center" prop="newsAbstract" :show-overflow-tooltip="true" />
      <el-table-column label="新闻内容" align="center" prop="newsContent" :show-overflow-tooltip="true" />
      <el-table-column label="新闻图片" align="center" prop="newsImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.newsImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" align="center" prop="newsTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.newsTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:news:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:news:remove']"
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

    <!-- 添加或修改新闻对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
<!--        <el-form-item label="展示方式" prop="showMode">-->
<!--          <el-select v-model="form.showMode" placeholder="请选择" >-->
<!--            <el-option label="垂直排列，略缩图显示大图" :value="1"/>-->
<!--            <el-option label="垂直排列，图文混排" :value="2"/>-->
<!--            <el-option label="垂直排列，略缩图显示多图" :value="3"/>-->
<!--            <el-option label="水平排列，左图右文" :value="4"/>-->
<!--            <el-option label="水平排列，右图左文" :value="5"/>-->
<!--            <el-option label="垂直排列，无略缩图，主标题+副标题显示" :value="6"/>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="新闻id" prop="newsId">-->
<!--          <el-input v-model="form.newsId" placeholder="请输入新闻id" />-->
<!--        </el-form-item>-->
        <el-form-item label="新闻标题" prop="newsTitle">
          <el-input v-model="form.newsTitle" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="新闻摘要" prop="newsAbstract">
          <el-input v-model="form.newsAbstract" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="新闻内容">
          <editor v-model="form.newsContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="新闻图片" prop="newsImage">
          <image-upload v-model="form.newsImage"/>
        </el-form-item>
        <el-form-item label="发布时间" prop="newsTime">
          <el-date-picker clearable
            v-model="form.newsTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择发布时间">
          </el-date-picker>
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
import { listNews, getNews, delNews, addNews, updateNews } from "@/api/biz/news/news";

export default {
  name: "News",
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
      // 新闻表格数据
      newsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        catalogId: null,
        showMode: null,
        newsId: null,
        newsTitle: null,
        newsAbstract: null,
        newsContent: null,
        newsImage: null,
        newsTime: null,
        createTime: null,
        updateTime: null,
        deleteFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        showMode: [
          { required: true, message: "展示方式", trigger: "change" }
        ],
        newsId: [
          { required: true, message: "新闻id", trigger: "blur" }
        ],
        newsTitle: [
          { required: true, message: "新闻标题", trigger: "blur" }
        ],
        newsAbstract: [
          { required: true, message: "新闻摘要", trigger: "blur" }
        ],
        newsContent: [
          { required: true, message: "新闻内容", trigger: "blur" }
        ],
        newsTime: [
          { required: true, message: "发布时间", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询新闻列表 */
    getList() {
      this.loading = true;
      listNews(this.queryParams).then(response => {
        this.newsList = response.rows;
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
        catalogId: null,
        showMode: null,
        newsId: null,
        newsTitle: null,
        newsAbstract: null,
        newsContent: null,
        newsImage: null,
        newsTime: null,
        createTime: null,
        updateTime: null,
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
      this.title = "添加新闻";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getNews(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改新闻";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNews(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNews(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除新闻编号为"' + ids + '"的数据项？').then(function() {
        return delNews(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/news/export', {
        ...this.queryParams
      }, `news_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
