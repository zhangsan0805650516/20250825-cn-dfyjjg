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
      <el-form-item label="用户姓名" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="userMobile">
        <el-input
          v-model="queryParams.userMobile"
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
      <el-form-item label="导师姓名" prop="tutorName">
        <el-input
          v-model="queryParams.tutorName"
          placeholder="请输入导师姓名"
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
          <el-option :label="$t('冻结中')" value="0"/>
          <el-option :label="$t('已解冻')" value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('类型')" prop="voteType">
        <el-select
          v-model="queryParams.voteType"
          :placeholder="$t('类型')"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option :label="$t('用户')" value="0"/>
          <el-option :label="$t('后台')" value="1"/>
        </el-select>
      </el-form-item>
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
          v-hasPermi="['biz:tutorVote:add']"
        >新增</el-button>
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="success"-->
      <!--          plain-->
      <!--          icon="el-icon-edit"-->
      <!--          size="mini"-->
      <!--          :disabled="single"-->
      <!--          @click="handleUpdate"-->
      <!--          v-hasPermi="['biz:tutorVote:edit']"-->
      <!--        >修改</el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['biz:tutorVote:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['biz:tutorVote:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          size="mini"
          :disabled="multiple"
          @click="handleUnfreeze"
        >解冻</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tutorVoteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="${comment}" align="center" prop="id" />-->
      <el-table-column :label="$t('姓名/唯一码')" align="center" prop="member" width="250" v-if="show_mobile === '0'">
        <template slot-scope="scope">
          {{ scope.row.faMember.name?scope.row.faMember.name:"" }}/{{ scope.row.faMember.weiyima?scope.row.faMember.weiyima:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('姓名/手机/唯一码')" align="center" prop="member" width="250" v-if="show_mobile === '1'">
        <template slot-scope="scope">
          {{ scope.row.faMember.name?scope.row.faMember.name:"" }}/{{ scope.row.mobile?scope.row.mobile.replace(/(\d{3})(\d{4})(\d{4})/, "$1****$3"):"" }}/{{ scope.row.faMember.weiyima?scope.row.faMember.weiyima:"" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('上级(机构码/姓名)')" align="center" prop="pid" width="150" >
        <template slot-scope="scope">
          {{ scope.row.faMember?scope.row.faMember.superiorCode:'' }}/{{ scope.row.faMember?scope.row.faMember.superiorName:'' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('导师姓名')" align="center" prop="tutor" width="150">
        <template slot-scope="scope">
          {{ scope.row.faTutorList?scope.row.faTutorList.tutorName:'' }}
        </template>
      </el-table-column>
      <el-table-column label="金额" align="center" prop="money" />
      <!--      <el-table-column label="权重" align="center" prop="weigh" />-->
      <el-table-column label="类型" align="center" prop="voteType" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.voteType == 0">
            {{$t('用户')}}
          </el-tag>
          <el-tag type="info" v-if="scope.row.voteType == 1">
            {{$t('后台')}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.status == 0">
            {{$t('冻结中')}}
          </el-tag>
          <el-tag v-if="scope.row.status == 1">
            {{$t('已解冻')}}
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
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="success"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--            v-hasPermi="['biz:tutorVote:edit']"-->
          <!--          ><span style="margin: 5px">修改</span></el-button>-->
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="primary"
            @click="unfreeze(scope.row)"
          ><span style="margin: 5px">解冻</span></el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            v-hasPermi="['biz:tutorVote:remove']"
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

    <!-- 添加或修改投票对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('用户')">
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
              :label="item.weiyima + ' ' + item.nickname"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('导师')">
          <el-select
            v-model="form.tutorId"
            filterable
            remote
            reserve-keyword
            :placeholder="$t('请输入姓名或编号')"
            :remote-method="querySearchTutorAsync"
            :loading="loading"
            @change="selectTutor"
          >
            <el-option
              v-for="item in tutorOptions"
              :key="item.id"
              :label="item.tutorIdNum + ' ' + item.tutorName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入金额" />
        </el-form-item>
        <el-form-item label="投票次数" prop="voteNums">
          <el-input v-model="form.voteNums" placeholder="请输入投票次数" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio v-model="form.status" :label="1">已解冻</el-radio>
          <el-radio v-model="form.status" :label="0">冻结中</el-radio>
        </el-form-item>
        <!--        <el-form-item label="权重" prop="weigh">-->
        <!--          <el-input v-model="form.weigh" placeholder="请输入权重" />-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="备注" prop="remarks">-->
        <!--          <el-input v-model="form.remarks" placeholder="请输入备注" />-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="删除标记" prop="deleteFlag">-->
        <!--          <el-input v-model="form.deleteFlag" placeholder="请输入删除标记" />-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMemberVote">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTutorVote, getTutorVote, delTutorVote, addTutorVote, updateTutorVote, submitMemberVote, unfreeze, batchUnfreeze } from "@/api/biz/tutorVote/tutorVote";
import {searchMember} from "@/api/biz/member/member";
import {searchTutor} from "@/api/biz/tutorList/tutorList";

export default {
  name: "TutorVote",
  data() {
    return {
      show_mobile: '0',
      memberOptions: [],
      tutorOptions: [],
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
      // 投票表格数据
      tutorVoteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        userId: null,
        tutorId: null,
        money: null,
        weigh: null,
        status: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remarks: null,
        deleteFlag: null,
        userName:null,
        userMobile:null,
        tutorName:null,
        voteType:null,
        weiyima:null,
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
    /** 解冻按钮操作 */
    unfreeze(row) {
      // const that = this;
      // let flag = false;
      this.$modal.confirm('确认解冻？').then(function() {
        // if (1 === row.voteType) {
        //   that.$modal.msgError("后台加票，无法解冻");
        // } else {
        //   flag = true;
        return unfreeze(row);
        // }
      }).then(() => {
        // if (flag) {
          this.getList();
          this.$modal.msgSuccess("操作成功");
        // }
      }).catch(() => {});
    },
    selectMember(item) {

    },
    selectTutor(item) {

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
    querySearchTutorAsync(queryString) {
      if (queryString !== '') {
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          searchTutor({ "queryString" : queryString }).then(res => {
            this.tutorOptions = res.data;
          }).catch(err => {
            this.$modal.msgError(this.$t("导师数据错误"));
          })
        }, 200);
      } else {
        this.tutorOptions = [];
      }
    },
    /** 查询投票列表 */
    getList() {
      this.loading = true;
      listTutorVote(this.queryParams).then(response => {
        this.tutorVoteList = response.rows;
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
        userId: null,
        tutorId: null,
        money: null,
        weigh: null,
        status: 1,
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
      this.title = "添加投票";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTutorVote(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改投票";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTutorVote(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTutorVote(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 增加用户投票 */
    submitMemberVote() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          submitMemberVote(this.form).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除投票编号为"' + ids + '"的数据项？').then(function() {
        return delTutorVote(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/tutorVote/export', {
        ...this.queryParams
      }, `tutorVote_${new Date().getTime()}.xlsx`)
    },
    /** 解冻按钮操作 */
    handleUnfreeze(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('确认批量解冻？').then(function() {
        return batchUnfreeze({"ids":ids});
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("解冻成功");
      }).catch(() => {});
    },
  }
};
</script>
