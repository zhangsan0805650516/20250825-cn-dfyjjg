import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Cookies from 'js-cookie'
import elementEnLocale from 'element-ui/lib/locale/lang/en' // element-ui lang导入Element的语言包 英文
import elementZhLocale from 'element-ui/lib/locale/lang/zh-CN'// element-ui lang 导入Element的语言包 中文
import elementViLocale from 'element-ui/lib/locale/lang/vi'// element-ui lang 导入Element的语言包 越南
import enLocale from './en' // 导入项目中用到的英文语言包
import zhLocale from './zh'// 导入项目中用到的中文语言包
import viLocale from './vi'// 导入项目中用到的越南语言包
Vue.use(VueI18n)
const messages = {
  en: {
    ...enLocale,
    ...elementEnLocale
  },
  zh: {
    ...zhLocale,
    ...elementZhLocale,
  },
  vi: {
    ...viLocale,
    ...elementViLocale,
  },

}

const i18n = new VueI18n({
  locale: localStorage.getItem('language') || 'zh', // 设置当前语种，之所以放到storage中是为了避免用户手动点击刷新页面时语言被自动切换回去，所以需要把语言存起来
  messages, // 设置全局当地语言包,
  silentTranslationWarn: true,
  fallbackLocale: 'zh', //如果当前语种不存在时，默认设置当前语种
  numberFormats:{ //设置 数字本地化
    'en': {
      currency: { //添加 $
        style: 'currency', currency: 'USD'
      }
    },
    'zh': {
      currency: { //添加 ￥
        style: 'currency', currency: 'JPY', currencyDisplay: 'symbol'
      }
    },
    'vi': {
      currency: { //添加 ₫
        style: 'currency', currency: 'VND', currencyDisplay: 'symbol'
      }
    }
  },
  dateTimeFormats:{//设置 日期时间本地化
    'en': {
      short: {
        year: 'numeric', month: 'short', day: 'numeric'
      },
      long: {
        year: 'numeric', month: 'short', day: 'numeric',
        weekday: 'short', hour: 'numeric', minute: 'numeric'
      }
    },
    'zh': {
      short: {
        year: 'numeric', month: 'short', day: 'numeric'
      },
      long: {
        year: 'numeric', month: 'short', day: 'numeric',
        weekday: 'short', hour: 'numeric', minute: 'numeric'
      }
    },
    'vi': {
      short: {
        year: 'numeric', month: 'short', day: 'numeric'
      },
      long: {
        year: 'numeric', month: 'short', day: 'numeric',
        weekday: 'short', hour: 'numeric', minute: 'numeric'
      }
    }
  }
})

export default i18n
