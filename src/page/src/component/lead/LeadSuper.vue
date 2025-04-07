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
        <ul>
          <li>admin</li>
          <ul>
            <li>
              <button style="background:#f5f5f5;padding:0 5px;" @click="handleLoginNoPassword">admin</button>
            </li>
          </ul>
          <li>其他</li>
          <ul>
            <li>
              <button style="background:#f5f5f5;padding:0 5px;" @click="handleLoginNoPassword">...</button>
            </li>
          </ul>
        </ul>
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
import SwitchEnv from './LeadSwitchEnv.vue'
import {httpPutJson} from '../../util/HttpUtil'
import {toast} from './toast'

const props = defineProps({
  onMerchantIdChange: {
    type: Function,
    default: (v: string) => {
      console.log('super.merchantId: ' + v)
    },
  },
})
const pathList = ref([
  {
    value: '/industry/',
    label: 'Douson（后台管理）',
  },
  {
    value: '/industry/admin/dashboard',
    label: '首页',
  },
  {
    value: '/industry/admin/system/user',
    label: '用户管理',
  },
  {
    value: '/industry/admin/system/account',
    label: '人员管理',
  },
  {
    value: '/industry/admin/product/param',
    label: '参数管理',
  },
])
const router = useRouter()
const route = useRoute()

const handleGoto = (path: string) => {
  location.href = path
}
const handleLoginNoPassword = (e: any) => {
  const username = e && e.value ? e.value : e.currentTarget ? e.currentTarget.innerText : ''
  httpPutJson(`system/sign-in`, {
    username: username,
    passwordEncrypt: username,
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
