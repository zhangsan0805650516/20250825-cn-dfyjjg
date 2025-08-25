<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
      <el-tab-pane :label="key" :name="value" v-for="(key, value) in configGroup">
        <el-table :data="riskConfigList" stripe style="width: 100%">
          <el-table-column prop="title" label="变量标题" width="200">
            <template slot-scope="scope" v-if="scope.row.name != 'ps_nums' || !psNumsHide">
              {{ scope.row.title }}
            </template>
          </el-table-column>
          <el-table-column prop="value" label="变量值">
            <template slot-scope="scope" v-if="scope.row.name != 'ps_nums' || !psNumsHide">
              <el-input v-if="scope.row.type === 'string'" v-model="scope.row.value" />
              <el-input type="textarea" rows="5" v-if="scope.row.type === 'text'" v-model="scope.row.value" />
              <el-select v-if="scope.row.type === 'select'" v-model="scope.row.value" >
                <el-option :label="key" :value="value.toString()" v-for="(key, value) in JSON.parse(scope.row.content)"/>
              </el-select>
              <el-radio-group v-if="scope.row.type === 'radio'" v-model="scope.row.value" @change="changeRadio(scope.row)">
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
          <el-table-column prop="name" label="变量名" width="200">
            <template slot-scope="scope" v-if="scope.row.name != 'ps_nums' || !psNumsHide">
              {{ scope.row.name }}
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <div align="center" style="margin: 10px">
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>

    </el-tabs>
  </div>
</template>

<script>
import {
  listRiskConfig,
  delRiskConfig,
  addRiskConfig,
  updateRiskConfig,
  getConfiggroup,
  getConfigListByGroup,
  updateRiskConfigList,
  getTitle
} from "@/api/biz/riskConfig/riskConfig";

export default {
  name: "RiskConfig",
  data() {
    return {
      psNumsHide: null,
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
    this.getInfoByNames();
  },
  methods: {
    changeRadio(item) {
      if (item.name == "pskqsrsgs") {
        if (item.value == 0) {
          this.psNumsHide = true;
        } else if (item.value == 1) {
          this.psNumsHide = false;
        }
      }
    },
    getInfoByNames() {
      this.getInfoByName({"name":"pskqsrsgs"}).then(res => {
        if(res.data.value == 0) {
          this.psNumsHide = true;
        } else if (res.data.value == 1) {
          this.psNumsHide = false;
        }
      }).catch(err => {
        console.log(err);
      })
    },
    /** tab栏切换 */
    handleClick(tab, event) {
      this.getConfigListByGroup(tab.name);
      this.getInfoByNames("pskqsrsgs");
    },
    /** 获取字典分类 */
    getConfiggroup() {
      getConfiggroup().then(response => {
        this.configGroup = JSON.parse(response.data[0].value);
        this.activeName = Object.keys(this.configGroup)[0];
        this.getConfigListByGroup(this.activeName);
      });
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
