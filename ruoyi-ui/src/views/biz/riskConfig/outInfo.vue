<template>
  <div class="app-container">
    <el-table :data="riskConfigList" stripe style="width: 100%">
      <el-table-column prop="title" label="变量标题" width="200"></el-table-column>
      <el-table-column prop="value" label="变量值">
        <template slot-scope="scope">
          <el-input v-if="scope.row.type === 'string'" v-model="scope.row.value" />
          <el-input type="textarea" rows="5" v-if="scope.row.type === 'text'" v-model="scope.row.value" />
          <el-select v-if="scope.row.type === 'select'" v-model="scope.row.value" >
            <el-option :label="key" :value="value.toString()" v-for="(key, value) in JSON.parse(scope.row.content)"/>
          </el-select>
          <el-radio-group v-if="scope.row.type === 'radio'" v-model="scope.row.value">
            <el-radio :label="value" v-for="(key, value) in JSON.parse(scope.row.content)">{{ key }}</el-radio>
          </el-radio-group>
          <editor v-if="scope.row.type === 'editor'" v-model="scope.row.value" :min-height="192" :height="300"/>
          <el-switch v-if="scope.row.type === 'switch'" v-model="scope.row.value"
            active-value="1"
            inactive-value="0">
          </el-switch>
          <image-upload v-if="scope.row.type === 'image'" v-model="scope.row.value" :limit="1"/>
          <div v-if="scope.row.type === 'array'">
            <el-row :gutter="20">
              <el-col :span="8">键名</el-col>
              <el-col :span="8">键值</el-col>
            </el-row>
            <el-row :gutter="20" v-for="item in scope.row.valueList">
              <el-col :span="8" style="margin: 5px">
                <el-input v-model="item[0]" />
              </el-col>
              <el-col :span="8" style="margin: 5px">
                <el-input v-model="item[1]" />
              </el-col>
            </el-row>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="变量名" width="200"></el-table-column>
    </el-table>

    <div align="center" style="margin: 10px">
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
  </div>
</template>

<script>
import { listRiskConfig, getRiskConfig, delRiskConfig, addRiskConfig, updateRiskConfig, getConfiggroup, getConfigListByGroup, updateRiskConfigList } from "@/api/biz/riskConfig/riskConfig";

export default {
  name: "RiskConfig",
  data() {
    return {
      activeName: null,
      // 选中数组
      ids: [],
      // 风控设置表格数据
      riskConfigList: [],
      // 表单参数
      form: {},
      // 字典分类列表
      configGroup: null,
    };
  },
  created() {
    this.getConfiggroup();
  },
  methods: {
    /** tab栏切换 */
    handleClick(tab, event) {
      this.getConfigListByGroup(tab.name);
    },
    /** 获取字典分类 */
    getConfiggroup() {
      // getConfiggroup().then(response => {
      //   this.configGroup = JSON.parse(response.data[0].value);
      //   this.activeName = Object.keys(this.configGroup)[0];
      // });
      this.activeName = 'zzbank';
      this.getConfigListByGroup(this.activeName);
    },
    /** 根据分类获取字典列表 */
    getConfigListByGroup(configGroup) {
      getConfigListByGroup({"configGroup" : configGroup}).then(response => {
        this.riskConfigList = response.data;
      }).catch(() => {});
    },
    /** 提交按钮 */
    submitForm() {
      updateRiskConfigList(this.riskConfigList).then(response => {
        this.$modal.msgSuccess("修改成功");
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除风控设置编号为"' + ids + '"的数据项？').then(function() {
        return delRiskConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('biz/riskConfig/export', {
        ...this.queryParams
      }, `riskConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style>
  .el-tabs__nav {
    white-space: normal;
  }
</style>
