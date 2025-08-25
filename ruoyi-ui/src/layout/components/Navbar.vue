<template>
  <div class="navbar">

    <el-dialog
      fullscreen
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      :title="title"
      :visible.sync="openBinding"
      append-to-body
      style="text-align: center;">
      <vue-qr v-if="isBinding === 0" :text="qrUrl" :margin="0"></vue-qr>
      <div>
        <el-button type="success" @click="bindingSecret(qrUrl)">已绑定</el-button>
        <el-button type="danger" @click="logout()">退出登录</el-button>
      </div>
    </el-dialog>

    <!-- 身份证认证 -->
    <el-dialog fullscreen :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false" title="验证码" :visible.sync="openAuth" append-to-body>
      <el-form ref="authForm" :model="form" :rules="authRules" label-width="120px">
        <el-form-item :label="$t('验证码')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('请输入验证码')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAuthForm">{{ $t('确 定') }}</el-button>
        <el-button type="danger" @click="logout()">退出登录</el-button>
      </div>
    </el-dialog>

    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav"/>
    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav"/>

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <!--        <search id="header-search" class="right-menu-item" />-->

        <!--        <el-tooltip content="源码地址" effect="dark" placement="bottom">-->
        <!--          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />-->
        <!--        </el-tooltip>-->

        <!--        <el-tooltip content="文档地址" effect="dark" placement="bottom">-->
        <!--          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />-->
        <!--        </el-tooltip>-->

        <el-tooltip content="切换语言" effect="dark" placement="bottom">
          <language-select id="lang-select" class="right-menu-item hover-effect" />
        </el-tooltip>

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <!--        <el-tooltip content="布局大小" effect="dark" placement="bottom">-->
        <!--          <size-select id="size-select" class="right-menu-item hover-effect" />-->
        <!--        </el-tooltip>-->

        <audio muted="muted" ref="withdrawNoticePlayer" src="./withdrawNotice.mp3" @canplay="playAudio"></audio>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import LanguageSelect from '@/components/LanguageSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import { checkQx } from "@/api/biz/withdraw/withdraw";
import VueQr from 'vue-qr';
import { getQrcode, checkCode, checkAuthStatus, bindingSecret } from '@/api/biz/google/google';

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    LanguageSelect,
    Search,
    RuoYiGit,
    RuoYiDoc,
    VueQr,
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  data() {
    return {
      timerId: null,
      // Google验证器
      timerIdGoogle: null,
      notice: false,
      qrUrl: null,
      isBinding: null,
      title: "",
      // 是否显示认证弹出层
      openAuth: false,
      openBinding: false,
      // 表单参数
      form: {},
      // 实名表单验证
      authRules: {
        code: [
          { required: true, message: this.$t("验证码不能为空"), trigger: "blur" }
        ],
      },
    };
  },
  mounted() {
    this.startTimer();
  },
  beforeDestroy() {
    clearInterval(this.timerId); // 清除计时器
    clearInterval(this.timerIdGoogle); // 清除计时器
  },
  created() {
    this.checkAuth();
  },
  methods: {
    bindingSecret(url) {
      bindingSecret({ "url" : url }).then(res => {
        this.openBinding = false;
        this.isBinding = 1;
        this.openAuth = true;
      })
    },
    // 表单重置
    reset() {
      this.form = {
        code: null,
      };
      this.resetForm("authForm");
    },
    /** 提交实名认证按钮 */
    submitAuthForm() {
      this.$refs["authForm"].validate(valid => {
        if (valid) {
          checkCode(this.form).then(response => {
            this.$modal.msgSuccess(this.$t("成功"));
            this.openAuth = false;
            this.reset();
          });
        }
      });
    },
    checkAuth() {
      getQrcode().then(res => {
        this.qrUrl = res.data.qrCode;
        this.isBinding = res.data.isBinding;
        if (this.isBinding === 0) {
          this.openBinding = true;
        } else {
          this.checkAuthStatus();
        }
      }).catch(err => {
        console.log(err);
      })
    },
    playAudio() {
      if (!this.notice) {
        return;
      }
      this.$refs.withdrawNoticePlayer.play().catch(e => {
        console.error(e);
      });
    },
    startTimer() {
      this.timerId = setInterval(() => {
        checkQx({}).then(res => {
          if (res.data) {
            this.notice = true;
            this.playAudio();
          } else {
            this.notice = false;
          }
        }).catch(err => {

        })
      }, 10000); // 设置间隔为10秒
      this.timerIdGoogle = setInterval(() => {
        if (this.isBinding === 1) {
          this.checkAuthStatus();
        }
      }, 10000); // 设置间隔为10秒
    },
    checkAuthStatus() {
      checkAuthStatus({}).then(res => {
        if (res.data.notBinding) {
          this.openBinding = true;
        } else {
          if (!res.data.googleAuth) {
            this.openAuth = true;
          }
        }
      }).catch(err => {

      })
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          // location.href = process.env.NODE_ENV === "production" ? process.env.BASE_URL + 'index' : '/index';
          location.href = process.env.NODE_ENV === "production" ? process.env.BASE_URL : '';
        })
      }).catch(() => {
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
