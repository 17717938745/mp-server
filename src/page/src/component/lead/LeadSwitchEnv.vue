<template>
  <div v-show="show" style="overflow-y: scroll;" class="content">
    <h1>环境重置</h1>
    <div>
      <button style="background-color: #f5f5f5;padding: 0 5px;" @click="handleClearAll">重置</button>
      <button type="button" @click="handleSwitchPrd">生产</button>
    </div>
    <h1>接口地址：</h1>
    <div class="select-input">
      <select id="urlPrefix" v-model="urlPrefix" @change="handleUrlPrefixChange" class="select-input-item">
        <option v-for="data in urlPrefixList" :value="data.value">{{ data.label }}（{{ data.value }}）</option>
      </select>
      <input type="text" v-model="urlPrefix" @blur="handleUrlPrefixChange" placeholder="请输入接口地址" class="select-input-item"/>
    </div>
    <div>
      <button style="background-color: #f5f5f5;padding: 0 5px;" @click="handleGetDeviceIdAndSalt">获取</button>
      <button style="background-color: #f5f5f5;padding: 0 5px;" @click="handleSetDeviceIdAndSalt">设置</button>
      <label class="env-header">获取/设置设备id、salt：</label>
    </div>
    <div class="select-input">
      <input id="deviceId" type="text" v-model="deviceId" class="select-input-item" placeholder="请输入设备id"/>
      <input id="salt" type="text" v-model="salt" class="select-input-item" placeholder="请输入salt"/>
    </div>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, toRefs} from 'vue'
import {clearAllCache, getMerchantId, getMerchantIdList, getProfile, getProfileList, getServiceId, getServiceIdList, getUrlPrefix, getUrlPrefixList, setMerchantId, setServiceId, setUrlPrefix, URL_PREFIX_PRD, URL_PREFIX_SIT, URL_PREFIX_TEST2, URL_PREFIX_UAT,} from '../../util/EnvUtil'
import {getDeviceId, getSalt, setDeviceId, setSalt} from "../../util/StorageUtil"
import {useRoute} from 'vue-router'
import {clearHttp} from "../../util/HttpUtil";

const route = useRoute()
const props = defineProps({
  env: {
    type: String,
    default: '',
  },
  onMerchantIdChange: {
    type: Function,
    default: (v: string) => {
      console.log('merchantId: ' + v)
    },
  },
})
const a = getProfileList()
const b = getUrlPrefixList()
const c = getServiceIdList()
const d = getMerchantIdList()
const state = reactive<{
  show: boolean
  serviceId: string[][]
  profileList: any[]
  urlPrefixList: any[]
  merchantIdList: any[]
  serviceIdList: any[]
  profile: string
  urlPrefix: string
  deviceId: string
  merchantId: string
  salt: string
}>({
  show: true,
  profileList: a,
  urlPrefixList: b,
  serviceIdList: c,
  merchantIdList: d,
  profile: '',
  urlPrefix: '',
  serviceId: [],
  deviceId: '',
  merchantId: '',
  salt: ''
})

const {
  show,
  profileList,
  urlPrefixList,
  serviceIdList,
  merchantIdList,
  profile,
  urlPrefix,
  serviceId,
  merchantId,
  deviceId,
  salt,
} = {...toRefs(state)}

const handleUrlPrefixChange = () => {
  setUrlPrefix(state.urlPrefix)
}
const handleMerhantIdChange = () => {
  setMerchantId(state.merchantId)
  props.onMerchantIdChange(state.merchantId)
}
const handleServiceIdChange = () => {
  console.log(state.serviceId.map(t => t.join('-')).join(','))
  setServiceId(state.serviceId.map(t => t.join('-')).join(','))
}
const handleClearAll = () => {
  clearAllCache()
  handleInitValue()
}
const handleSwitchPrd = () => {
  clearAllCache()
  setUrlPrefix('https://douson.natapp4.cc/')
  handleInitValue()
}
const handleAddService = () => {
  state.serviceId.push(["", ""])
  handleServiceIdChange()
}
const handleRemoveService = (index: number) => {
  state.serviceId.splice(index, 1)
  handleServiceIdChange()
}
const handleInitValue = () => {
  state.profile = getProfile()
  state.urlPrefix = getUrlPrefix()
  state.serviceId = getServiceId().split(',').filter(t => t && t.length > 0).map(t => {
    const index = t.indexOf('-')
    if (index >= 0) {
      return [t.substring(0, index), t.substring(index + 1)]
    } else {
      return [t, '']
    }
  })
  handleGetDeviceIdAndSalt()
  handleGetMerchantId()
  clearHttp()
}
const handleGetDeviceIdAndSalt = () => {
  state.deviceId = getDeviceId()
  state.salt = getSalt()
}
const handleGetMerchantId = () => {
  state.merchantId = getMerchantId()
}
const handleSetDeviceIdAndSalt = () => {
  setDeviceId(state.deviceId)
  setSalt(state.salt)
}
handleInitValue()
</script>

<style lang="scss" scoped>

.env-header {
  color: #666666;
  font-weight: 600;
  font-size: 16px;
}

.env-control {
  padding: 5px 5px 5px;

  button {
    margin: 5px 5px 0 0;
  }
}

.content {
  padding-top: 5px;
  padding-bottom: 10px;
  margin-bottom: 5px;

  button {
    margin: 5px;
  }

  select, option, input {
    height: 30px;
    border: 1px solid #e3e3e3;
    vertical-align: middle;
    margin: 0;
    padding: 0;
  }

  div:nth-child(1) {
    padding: 5px 10px;
  }

  div:nth-child(n+1) {
    padding: 5px 5px 5px;
  }

  .select-input {
    display: flex;
    justify-content: space-between;

    .select-input-item {
      width: 49%;
    }
  }

}
</style>
