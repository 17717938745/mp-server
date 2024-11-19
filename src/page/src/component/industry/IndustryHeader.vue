<template>
  <div class="header">
    <div class="header-left">
      <a href="http://192.168.9.14:8081/#/main-dashboard2" style="color: #10bf8a; display: flex; align-items: center;">
        <img :src="logo" alt="" style="height: 30px; margin-right: 10px;"/>
        http://192.168.9.14:8081/#/main-dashboard2
      </a>
    </div>
    <el-space class="header-right">
      <span style="color: #303133; margin-right: 10px;font-size: 14px;">
      Welcome, deal <span style="color: #ff976a; font-size: 18px;"> {{ `${username}` }}</span>!
        </span>
      <span v-if="todo.count > 0" @click="handleJumpToDashboard">
        <el-badge :value="todo.count" class="item">
          <el-button>Todo list</el-button>
        </el-badge>
      </span>
      <el-dropdown trigger="click"
                   @command="handleSwitchLanguage"
                   class="cursor-pointer"
      >
        <span style="display: flex;">
          {{ handleLanguageLabel() }}
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item v-for="(d, i) in languageList" divided :command="d.value">{{ d.label }}</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="el-dropdown-link" style="display: flex;">
          <div class="user-avatar">
            <img :src="Avatar" alt="avatar"/>
          </div>
          <img :src="arrowDownSVG" alt=""/>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item divided command="modifyPassword">Modify password</el-dropdown-item>
            <el-dropdown-item v-if="includes(roleCodeList, 'admin')" divided command="clearCache">Clear cache</el-dropdown-item>
            <el-dropdown-item divided command="sign">Logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-space>
    <el-dialog title="Modify password" v-model="modifyPasswordModal" width="60%">
      <el-form :rules="formRuleList" :model="formData" ref="formRef" label-width="128px">
        <el-form-item prop="oldPassword" label="Old password">
          <el-input type="password" v-model="formData.oldPassword"/>
        </el-form-item>
        <el-form-item prop="password" label="New password">
          <el-input type="password" v-model="formData.password"/>
        </el-form-item>
        <el-form-item prop="passwordConfirm" label="New password Confirm">
          <el-input type="password" v-model="formData.passwordConfirm" @keyup.enter="handleModifyPassword"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="modifyPasswordModal = false">Cancel</el-button>
          <el-button type="primary" @click="handleModifyPassword">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script lang="tsx" setup>
import {ElMessage} from 'element-plus'
import arrowDownSVG from '../../asset/img/arrow-down.svg'
import Avatar from '../../asset/img/img.jpg'
import {ref} from 'vue'
import {Store, useStore} from 'vuex'
import {useRoute, useRouter} from 'vue-router'
import {StoreType} from '@/store/Industry'
import {getFullUri} from '@/util/RouterUtil'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {includes} from '@/util/ArrayUtil'
import {sm4Encrypt} from '@/util/SecurityUtil'
import {DataResult, PageResult} from "@/typing/ma/System";

const languageList = [
  {
    value: 'CHS',
    label: '中文',
  },
  {
    value: 'THA',
    label: 'Tiếng Việt',
  },
  {
    value: 'CHS|THA',
    label: '中文+Tiếng Việt',
  },
]
const route = useRoute()
const router = useRouter()
const store: Store<StoreType> = useStore()
const roleCodeList = store.state.roleCodeList
const modifyPasswordModal = ref(false)
const user = store.state.user
const username = user.username
router.beforeEach((to, from, next) => {
  console.log('from.path', from.path)
  next()
})
const textKey = ref(store.state.textKey)
const formRuleList = ref({
  oldPassword: [{required: true, message: "Please Input", trigger: "blur"}],
  password: [{required: true, message: "Please Input", trigger: "blur"}],
  passwordConfirm: [{required: true, message: "Please Input", trigger: "blur"}],
});
const formData = ref({
  oldPassword: '',
  password: '',
  passwordConfirm: '',
})
const handleLanguageLabel = () => {
  const arr = languageList.filter(t => textKey.value === t.value)
  return arr.length > 0 ? arr[0].label : 'null'
}
const handleSwitchLanguage = (v: any) => {
  textKey.value = v
  store.commit("setTextKey", v)
}
const formRef = ref(null)
const formAutoFocusRef = ref(null)
const handleCommand = (command: string) => {
  if (command === 'clearCache') {
    httpDelete('system/cache').then(() => {
      ElMessage.success('clear success')
    })
  } else if (command === 'modifyPassword') {
    modifyPasswordModal.value = true
  } else if (command === 'sign') {
    httpDelete('system/sign-out').then(() => {
      store.commit("clearMenuTreeList")
      router.push(`/industry/sign?goBack=${getFullUri()}`)
    })
  }
}
const todo = ref({
  count: 0,
  list: [],
})
httpGet(`douson/todo/list`, {}).then(
    (res: DataResult<any>) => {
      todo.value.count = res.data.count
      todo.value.list = res.data.list || []
      ElMessage.success("Query success")
    }
)
const handleJumpToDashboard = () => {
  router.push({
    path: '/industry/admin/dashboard',
    query: {}
  })
}
const handleModifyPassword = () => {
  // @ts-ignore
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (formData.value.password !== formData.value.passwordConfirm) {
        ElMessage.error('Please check your new password')
      } else {
        httpPutJson('system/user/password', {
          oldPassword: sm4Encrypt(formData.value.oldPassword || ''),
          password: sm4Encrypt(formData.value.password || ''),
        }).then(() => {
          modifyPasswordModal.value = false
          ElMessage.success('success')
        })
      }
    }
  })
}
</script>
<style scoped>


</style>
