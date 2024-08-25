<template>
  <div>
    <div class="lead-local">
      <ul style="cursor: pointer;">
        <li class="lead-link" v-for="(t,i) in pathList" :key="i" @click="handleGoto(t.value)">{{ t.label }}</li>
      </ul>
    </div>
    <div style="background-color: #6ecbcb;margin-top: 10px;">
      <h1>免密登录</h1>
      <div>
        <h3>测试1套</h3>
        <ul>
          <li>自营</li>
          <ul>
            <li>
              <button style="background:#f5f5f5;padding:0 5px;" @click="handleLoginNoPassword">18720230209</button>
              /z111111/z123456
              私募持仓账号
            </li>
          </ul>
          <li>市民卡</li>
          <ul>
            <li>
              <button style="background:#f5f5f5;padding:0 5px;" @click="handleLoginNoPassword">18168085576</button>
              /aaa111/111aaa
              自动创建的账号
            </li>
          </ul>
        </ul>
      </div>
      <div>
        <h3>所有环境、所有渠道</h3>
        <ul>
          <li>
            <button style="background:#f5f5f5;padding:0 5px;" @click="handleLoginNoPassword">17717066234</button>
            /aaa111/111aaa
          </li>
        </ul>
        <div>
          <input placeholder="请输入手机号或者会员号" type="text" value="" ref="mobileInput"/>
          <select v-model="directLogin" class="select-input-item">
            <option :value="false">手机登录</option>
            <option :value="true">直接登录</option>
          </select>
          <button style="background:#f5f5f5;padding:0 5px;" @click="handleLoginNoPassword(mobileInput)">登录</button>
        </div>
      </div>
    </div>
    <div style="background-color: #8aab66;margin-top: 10px;">
      <switch-env :env="route.query.env" :onMerchantIdChange="onMerchantIdChange"/>
    </div>
  </div>
</template>

<script lang="tsx" setup>
import {useRoute, useRouter} from 'vue-router'
import {ref} from 'vue'
import {getSalt} from '../../util/StorageUtil'
import SwitchEnv from './LeadSwitchEnv.vue'
import {httpPostJson} from '../../util/HttpUtil'
import {toast} from './toast'
import {sha1Hex} from '../../util/CipherUtil'

const props = defineProps({
  onMerchantIdChange: {
    type: Function,
    default: (v: string) => {
      console.log('super.merchantId: ' + v)
    },
  },
})
const mobileInput = ref(null)
const directLogin = ref(false)
const pathList = ref([
  {
    value: '/ma/',
    label: 'ma（后台管理）',
  },
  {
    value: '/html5/',
    label: 'html5（移动端交易平台）',
  },
  {
    value: '/html5/index/setting',
    label: 'html5（个人页面）',
  },
  {
    value: '/html5/bill/annualReport',
    label: 'html5（年度账单）',
  },
  {
    value: '/website/',
    label: 'website（官网）',
  },
  {
    value: '/pdf?pdfPath=%2Fsrc%2Fasset%2Fpdf.pdf',
    label: 'pdf（pdf阅读）',
  },
  {
    value: '/report/h5/001',
    label: 'report（移动端静态页面）',
  },
  {
    value: '/wechat/',
    label: 'wechat（微信登录/注册跳转页面）',
  },
])
const router = useRouter()
const route = useRoute()

const handleGoto = (path: string) => {
  location.href = path
}
const handleLoginNoPassword = (e: any) => {
  const mobile = e && e.value ? e.value : e.currentTarget ? e.currentTarget.innerText : ''
  console.log(`mobile: ${mobile}`)
  httpPostJson(`provider/helper/login`, {
    mobile: mobile,
    mobileSalt: sha1Hex(mobile + getSalt()),
    directLogin: directLogin.value
  }).then(r => {
    toast('登录成功')
  })
}
</script>

<style lang="scss" scoped>
.lead-local {
  display: flex;
  justify-content: center;
  padding-top: 50px;
  background-color: #c0b9ac;
}

.lead-link {
  font-size: 32px;

  &:hover {
    background-color: #e0bc30;
    color: #f8f7f5;
  }
}
</style>
