<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="${comment}" prop="id">-->
<!--        <el-input-->
<!--          v-model="queryParams.id"-->
<!--          placeholder="请输入${comment}"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="导师姓名" prop="tutorName">
        <el-input
          v-model="queryParams.tutorName"
          placeholder="请输入导师姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="导师编号" prop="tutorIdNum">
        <el-input
          v-model="queryParams.tutorIdNum"
          placeholder="请输入导师编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('状态')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('状态')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('正常')" value="0"/>
          <el-option :label="$t('隐藏')" value="1"/>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="投票次数" prop="voteNums">-->
<!--        <el-input-->
<!--          v-model="queryParams.voteNums"-->
<!--          placeholder="请输入投票次数"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="金额" prop="money">-->
<!--        <el-input-->
<!--          v-model="queryParams.money"-->
<!--          placeholder="请输入金额"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="权重" prop="weigh">-->
<!--        <el-input-->
<!--          v-model="queryParams.weigh"-->
<!--          placeholder="请输入权重"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="创建时间" prop="createTime">-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.createTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择创建时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="创建者" prop="createBy">-->
<!--        <el-input-->
<!--          v-model="queryParams.createBy"-->
<!--          placeholder="请输入创建者"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="更新时间" prop="updateTime">-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.updateTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择更新时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="更新者" prop="updateBy">-->
<!--        <el-input-->
<!--          v-model="queryParams.updateBy"-->
<!--          placeholder="请输入更新者"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="备注" prop="remarks">-->
<!--        <el-input-->
<!--          v-model="queryParams.remarks"-->
<!--          placeholder="请输入备注"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="删除标记" prop="deleteFlag">-->
<!--        <el-input-->
<!--          v-model="queryParams.deleteFlag"-->
<!--          placeholder="请输入删除标记"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
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
          v-hasPermi="['biz:tutorList:add']"
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
          v-hasPermi="['biz:tutorList:edit']"
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
          v-hasPermi="['biz:tutorList:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:tutorList:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tutorListList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="导师头像" align="center" prop="tutorImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.tutorImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="导师姓名" align="center" prop="tutorName" />
      <el-table-column label="导师编号" align="center" prop="tutorIdNum" />
<!--      <el-table-column label="导师简介" align="center" prop="tutorIntro" />-->
      <el-table-column label="投票次数" align="center" prop="voteNums" />
      <el-table-column label="金额" align="center" prop="money" />
<!--      <el-table-column label="权重" align="center" prop="weigh" />-->
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 0">
            {{$t('正常')}}
          </el-tag>
          <el-tag type="info" v-if="scope.row.status == 1">
            {{$t('隐藏')}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="创建者" align="center" prop="createBy" />-->
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="更新者" align="center" prop="updateBy" />-->
<!--      <el-table-column label="备注" align="center" prop="remarks" />-->
<!--      <el-table-column label="删除标记" align="center" prop="deleteFlag" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['biz:tutorList:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:tutorList:remove']"
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

    <!-- 添加或修改导师对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="导师头像" prop="tutorImage">
          <image-upload v-model="form.tutorImage" :limit="1" />
        </el-form-item>
        <el-form-item label="导师姓名" prop="tutorName">
          <el-input v-model="form.tutorName" placeholder="请输入导师姓名" />
        </el-form-item>
        <el-form-item label="导师编号" prop="tutorIdNum">
          <el-input v-model="form.tutorIdNum" placeholder="请输入导师编号" />
        </el-form-item>
        <el-form-item label="金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入金额" />
        </el-form-item>
        <el-form-item label="导师简介" prop="tutorIntro">
          <el-input v-model="form.tutorIntro" type="textarea" placeholder="请输入内容" rows="5" />
        </el-form-item>
<!--        <el-form-item label="投票次数" prop="voteNums">-->
<!--          <el-input v-model="form.voteNums" placeholder="请输入投票次数" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="权重" prop="weigh">-->
<!--          <el-input v-model="form.weigh" placeholder="请输入权重" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="备注" prop="remarks">-->
<!--          <el-input v-model="form.remarks" placeholder="请输入备注" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="删除标记" prop="deleteFlag">-->
<!--          <el-input v-model="form.deleteFlag" placeholder="请输入删除标记" />-->
<!--        </el-form-item>-->
        <el-form-item label="状态" prop="status">
          <el-radio v-model="form.status" :label="0">正常</el-radio>
          <el-radio v-model="form.status" :label="1">隐藏</el-radio>
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
import { listTutorList, getTutorList, delTutorList, addTutorList, updateTutorList } from "@/api/biz/tutorList/tutorList";

export default {
  name: "TutorList",
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
      // 导师表格数据
      tutorListList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        tutorImage: null,
        tutorName: null,
        tutorIdNum: null,
        tutorIntro: null,
        voteNums: null,
        money: null,
        weigh: null,
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
    /** 查询导师列表 */
    getList() {
      this.loading = true;
      listTutorList(this.queryParams).then(response => {
        this.tutorListList = response.rows;
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
        tutorImage: null,
        tutorName: null,
        tutorIdNum: null,
        tutorIntro: null,
        voteNums: null,
        money: null,
        weigh: null,
        status: 0,
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
      this.title = "添加导师";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTutorList(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改导师";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTutorList(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTutorList(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除导师编号为"' + ids + '"的数据项？').then(function() {
        return delTutorList(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/tutorList/export', {
        ...this.queryParams
      }, `tutorList_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
