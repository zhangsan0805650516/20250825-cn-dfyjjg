<template>
  <el-dropdown trigger="click" @command="changeLanguage">
    <div>
      <svg-icon class-name="language-icon" icon-class="language" />
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item v-for="item of languageOptions" :key="item.value" :command="item.value">
        {{ item.label }}
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>

import i18n from "@/utils/i18n/i18n";

export default {
  data() {
    return {
      languageOptions: [
        { label: '中文', value: 'cn' },
        { label: 'English', value: 'en' },
        // { label: 'Tiếng Việt', value: 'vi' }
      ]
    }
  },
  computed: {
    language() {
      return localStorage.getItem('language');
    }
  },
  methods: {
    changeLanguage(event) {
      i18n.locale = event // 改变为中文
      localStorage.setItem('language', event)//在localStorage中存入设置
    },
  }
}
</script>
