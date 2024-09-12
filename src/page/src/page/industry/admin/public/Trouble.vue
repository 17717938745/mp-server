<template>
  <div style="display: flex; flex-direction: column; align-items: center; justify-content: flex-start; margin-bottom: 20px; position: absolute; width: 100%; top: 20px;" id="printContainer">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 595px;">
      <h1 style="text-align: center;">
        <span style="font-size: 36px;">
          量具损坏报告 Dụng cụ đo bị hỏng Accident Report
            <el-icon style="vertical-align: middle" @click="handleCopyLink">
              <document-copy/>
            </el-icon>
            <el-icon v-if="!data.valid" class="print-hide" style="vertical-align: middle" @click="handleEdit">
              <edit/>
            </el-icon>
        </span>
      </h1>
      <el-descriptions
          :column="1"
          style="width: 100%;"
          border
          id="printDescription"
      >`
        <el-descriptions-item
            v-for="(viewConfig, i) in handleColumnConfigList(columnConfigList, [], true, false, [], [])"
            :key="viewConfig.value"
            :label="viewConfig.labelKey ? store.state.label[viewConfig.labelKey] : viewConfig.label"
            align="center"
            label-class-name="trouble-print-label description-print-border"
            class-name="description-print-border"
        >
          <view-content
              :idKey="idKey"
              :viewConfig="viewConfig"
              :row="data"
          >
            <template #expand="row">
              <slot name="expand" :param="row.param"/>
            </template>
          </view-content>
        </el-descriptions-item>
      </el-descriptions>
    </div>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%"
               :close-on-click-modal="false"
    >
      <el-form
          :rules="formRuleList"
          :model="data"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="reportDate" :label="store.state.label.reportDate">
          <el-date-picker
              type="date"
              v-model="data.reportDate"
              format="YYYY-MM-DD"
              @change="data.reportDate = formatDate(data.reportDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="userId" :label="store.state.label.user">
          <el-select v-model="data.userId" clearable filterable placeholder="Please select">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="directLeader" :label="store.state.label.directLeader">
          <el-select v-model="data.directLeader" clearable filterable placeholder="Please select">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="accidentDescribe" :label="store.state.label.accidentQualityDescribe">
          <el-input type="textarea" :rows=4 v-model="data.accidentDescribe"/>
        </el-form-item>
        <el-form-item prop="photoList" :label="`${store.state.label.qualityPhoto}(${(formRuleList['photoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="data.photoList" :maxSize="Number(`${(formRuleList['photoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="reason" :label="store.state.label.troubleReason">
          <el-select v-model="data.reasonList"
                     filterable
                     multiple
                     allow-create
                     clearable
                     :placeholder="store.state.label.troubleReason"
                     class="search-item">
            <el-option
                v-for="item in config.troubleReasonList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="solve" :label="store.state.label.qualitySolve">
          <el-input type="textarea" :rows=4 v-model="data.solve"/>
        </el-form-item>
        <el-form-item prop="improveDescribe" :label="store.state.label.improveDescribe">
          <el-input type="textarea" :rows=4 v-model="data.improveDescribe"/>
        </el-form-item>
        <el-form-item prop="opinion" :label="store.state.label.qualityOpinion">
          <el-input type="textarea" :rows=4 v-model="data.opinion"/>
        </el-form-item>
        <el-form-item prop="valid" :label="store.state.label.valid">
          <el-checkbox v-model="data.valid" name="valid" :disabled="!includes(roleCodeList, 'admin')">
            Done
          </el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleMerge">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, ref, Ref, toRefs,} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store'
import {ElMessage} from 'element-plus'
import {useRoute, useRouter} from 'vue-router'
import {httpGet, httpPutJson} from '@/util/HttpUtil'
import {DataResult, ListResult} from '@/typing/ma/System'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewContent from '../../component/ViewContent.vue'
import {includes} from '@/util/ArrayUtil'
import {handleColumnConfigList} from '@/util/IndustryUtil'
import {DocumentCopy, Edit,} from '@element-plus/icons-vue'
import {dousonFullUrl} from '@/util/EnvUtil'
import {formatDate} from "@/util/DateUtil";
import ImageUpload from '../../component/ImageUpload.vue'
import FileUpload from '../../component/FileUpload.vue'

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const idKey = 'troubleId'
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'reportDate', labelKey: 'date', width: 100,},
  {value: 'equipmentNo', labelKey: 'equipmentNo', width: 80,},
  {value: 'toolDescribe', labelKey: 'toolDescribe', width: 156,},
  {value: 'userIdFormat', labelKey: 'partyUser', width: 116,},
  {value: 'directLeaderFormat', labelKey: 'directLeader', width: 88,},
  {value: 'accidentDescribe', labelKey: 'accidentQualityDescribe', width: 276, showOverflow: true,},
  {value: 'reasonFormat', labelKey: 'troubleReason', width: 276, showOverflow: true,},
  {value: 'solve', labelKey: 'qualitySolve', width: 276, showOverflow: true,},
  {value: 'improveDescribe', labelKey: 'improveDescribe', width: 276, showOverflow: true,},
  {value: 'opinion', labelKey: 'qualityOpinion', width: 128,},
  {value: 'valid', labelKey: 'valid', width: 100, type: ValueType.Valid},
  {value: 'photoList', labelKey: 'qualityPhoto', width: 128, type: ValueType.Image,},
])
const data = ref({
  reportDate: formatDate(new Date(), 'yyyy-MM-dd'),
  userId: '',
  directLeader: '',
  accidentDescribe: '',
  reason: '',
  reasonList: [],
  solve: '',
  improveDescribe: '',
  opinion: '',
  valid: false,
  photoList: [],
  fileList: [],
  improvePhotoList: [],
  improveFileList: [],
})
const state = reactive({
  userConfigList: new Array<any>(),
  formVisible: false,
  formSave: false,
  config: {
    troubleReasonList: [],
  },
  formRuleList: {
    reportDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    userId: [{required: true, message: 'Please check', trigger: 'blur'}],
    directLeader: [{required: true, message: 'Please check', trigger: 'blur'}],
    accidentDescribe: [{required: true, message: 'Please check', trigger: 'blur'}],
    reasonList: [{required: false, type: 'array', message: 'Please check', min: 1, max: 999}],
    photoList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 6}],
  },
})
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'troubleReason',
    ]
  }),
  httpGet(`system/user/config/list`, {}),
]).then((l: any) => {
  state.config = l[0].data
  userOptionList.value = (l[1].list || []).map((t: any) => {
    return {
      value: t.userId,
      label: t.name,
    }
  })
  handleGet()
})
const handleGet = () => {
  httpGet(`douson/trouble`, {troubleId: route.query.troubleId}).then(
      (res: DataResult<any>) => {
        data.value = res.data || {}
        ElMessage.success("Query success")
        setTimeout(() => {
          const heightPx = (document.getElementById('printDescription')?.offsetHeight || 1024) + 450 + 'px'
          const printContainer = document.getElementById('printContainer')
          if (printContainer) {
            printContainer.style.height = heightPx
          }
        }, 1000)
      }
  )
}
handleGet()
const handleEdit = () => {
  state.formVisible = true
}
const handleMerge = () => {
  formRef.value.validate((valid: any, fields: any) => {
    if (valid) {
      handleUpdate(data.value)
    } else {
      ElMessage.error('Please check input')
    }
  })
}
const handleUpdate = (row: any) => {
  return httpPutJson('douson/admin/trouble', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handleGet()
  })
}
const handleCopyLink = () => {
  const text = dousonFullUrl(`/industry/public/trouble?troubleId=${route.query.troubleId}`)
  // 添加一个input元素放置需要的文本内容
  const input = document.createElement('input')
  input.value = text
  document.body.appendChild(input)
  // 选中并复制文本到剪切板
  input.select()
  document.execCommand('copy')
  // 移除input元素
  document.body.removeChild(input)
  ElMessage.success('Copy success')
}
const {
  userConfigList,
  formVisible,
  formSave,
  config,
  formRuleList,
} = {
  ...toRefs(state),
}
</script>

<style lang="scss">
.trouble-print-label {
  width: 139px;
}

@media print {
  title, .el-drawer__close-btn .print-hide {
    display: none;
  }
  body {
    height: auto;
  }
}
</style>
