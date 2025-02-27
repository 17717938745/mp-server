<template>
  <template v-if="viewConfig.type === ValueType.Expand">
    <slot name="expand" :param="row"/>
  </template>
  <template v-else-if="viewConfig.type === ValueType.Image">
    <el-space wrap style="justify-content: center;">
      <el-image
          v-if="row[viewConfig.value] && row[viewConfig.value].length > 0"
          v-for="(t, i) in row[viewConfig.value]"
          :src="fullUrl(t.photoCompressUrl, '')"
          fit="cover"
          :preview-src-list="[fullUrl(t.photoUrl, '')]"
          :initial-index="0"
          teleported="false"
          :style="{
            width: `${155 / scale}px`,
          }"
      >
        <template #placeholder>
          <div class="image-slot">
            加载中...
          </div>
        </template>
      </el-image>
      <el-empty v-else description="Empty"
                :style="{
                  width: `${155 / scale}px`,
                  height: `${160 / scale}px`,
                  marginTop: `${30 / scale}px`,
                  marginBottom: `${30 / scale}px`,
                }"
      />
    </el-space>
  </template>
  <template v-else-if="viewConfig.type === ValueType.PictureText">
    <div>
      <h3 v-if="viewConfig.textLabel">{{ row[viewConfig.textLabel] || '--'}}</h3>
      <el-space style="justify-content: center;">
        <img
            v-if="row[viewConfig.value] && row[viewConfig.value].length > 0"
            v-for="(t, i) in row[viewConfig.value]"
            :src="fullUrl(t.photoCompressUrl, '')"
            :style="{
            height: `${viewConfig.pictureHeight || 155 / scale}px`,
          }"
        />
        <el-empty v-else description="Empty"
                  :style="{
                  width: `${155 / scale}px`,
                  height: `${160 / scale}px`,
                  marginTop: `${30 / scale}px`,
                  marginBottom: `${30 / scale}px`,
                }"
        />
      </el-space>
    </div>
  </template>
  <template v-else-if="viewConfig.type === ValueType.Text">
    <span v-if="viewConfig.highLight" class="high-light">{{ row[viewConfig.value] }}</span>
    <span v-else>{{ row[viewConfig.value] }}</span>
  </template>
  <template v-else-if="viewConfig.type === ValueType.TextArea">
    <pre v-if="viewConfig.highLight" class="high-light" style="text-align: left; white-space: pre-wrap; word-break: break-word;">{{ row[viewConfig.value] }}</pre>
    <pre v-else style="text-align: left; white-space: pre-wrap; word-break: break-word;">{{ row[viewConfig.value] }}</pre>
  </template>
  <template v-else-if="viewConfig.type === ValueType.Video">
    <el-space wrap style="justify-content: center;">
      <div v-if="row[viewConfig.value] && row[viewConfig.value].length > 0">
        <video
            v-for="t in row[viewConfig.value]" :key="t.videoUrl"
            :width="`${155 / scale}`"
            controls>
          <!--:type="`video/${temp.extension}`"-->
          <source :src="fullUrl(t.videoUrl, '')" :type="`video/mp4`">
          您的浏览器不支持 video 标签。
        </video>
      </div>
      <el-empty v-else description="Empty"
                :style="{
                  width: `${155 / scale}px`,
                  height: `${160 / scale}px`,
                }"
      />
    </el-space>
  </template>
  <template v-else-if="viewConfig.type === ValueType.TextList">
    <div
        v-for="t in row[viewConfig.value]"
        :key="t">
      {{ `${t}` }}
    </div>
  </template>
  <template v-else-if="viewConfig.type === ValueType.TagList">
    <el-space wrap style="display: flex; justify-content: center;">
      <el-tag
          v-for="t in row[viewConfig.value]"
          :key="t">
        {{ `${t}` }}
      </el-tag>
    </el-space>
  </template>
  <template v-else-if="viewConfig.type === ValueType.SerialNoList">
    <div
        v-for="t in row[viewConfig.value]"
        :key="t">
      {{ `${row.orderNo}-${row.projectSequence}-${t}` }}
    </div>
  </template>
  <template v-else-if="viewConfig.type === ValueType.DeviceRunningTime">
    <el-timeline style="max-width: 600px; margin-top: 5px;">
      <el-timeline-item
          v-for="(activity, index) in handleConvertUserDeviceList(row)"
          :key="index"
          :type="activity.type"
          :hollow="activity.hollow"
          :timestamp="activity.timestamp"
          size="normal"
      >
        <el-tag
            v-if="activity.wrongData"
            type="danger">{{ activity.content }}
        </el-tag>
        <span v-else>{{ activity.content }}</span>
      </el-timeline-item>
    </el-timeline>
  </template>
  <template v-else-if="viewConfig.type === ValueType.Valid">
    {{ row[viewConfig.value] ? '结案' : '审批中' }}
  </template>
  <template v-else-if="viewConfig.type === ValueType.HighLight">
    <el-tag type="" size="small">{{ row[viewConfig.value] }}</el-tag>
  </template>
  <template v-else-if="viewConfig.type === ValueType.Number">
    <el-tag :type="row[viewConfig.value] >= 0 ? '' : 'danger'" size="small">{{ row[viewConfig.value] }}</el-tag>
  </template>
  <template v-else-if="viewConfig.type === ValueType.NumberPositive">
    <el-tag :type="row[viewConfig.value] > 0 ? '' : 'danger'" size="small">{{ row[viewConfig.value] }}</el-tag>
  </template>
  <template v-else-if="viewConfig.type === ValueType.OrderLink">
    <el-link
        v-if="includes(roleCodeList, 'admin')"
        @click="handleJumpOrder(row)"
        type="danger"
    >
      {{ row[viewConfig.value] }}
    </el-link>
    <span v-else>
        {{ row[viewConfig.value] }}
      </span>
  </template>
  <template v-else-if="viewConfig.type === ValueType.DeviceLink">
    <el-link
        v-if="includes(roleCodeList, 'admin')"
        @click="handleJumpDevice(row)"
        type="danger"
    >
      {{ row[viewConfig.value] }}
    </el-link>
    <span v-else>
      {{ row[viewConfig.value] }}
    </span>
  </template>
  <template v-else-if="viewConfig.type === ValueType.Operator">
    <el-space wrap style="justify-content: center;">
      <slot name="operator" :param="row"/>
      <el-link
          v-if="props.handleShowDetail"
          :icon="View"
          @click="props.handleShowDetail(row)"
          class="mr10"
          type="success"
      >
        <el-tag size="small">Detail</el-tag>
      </el-link>
      <el-link
          v-if="props.idKey === 'productId' && (includes(roleCodeList, 'user') || includes(roleCodeList, 'admin'))"
          :icon="Tools"
          @click="handleReport(row)"
          class="mr10"
          type="warning"
      >
        <el-tag size="small">Start</el-tag>
      </el-link>
      <el-link
          v-if="(!handleEditShow || handleEditShow(row)) && handleEdit && (user.userId === 'admin' || !row.valid)"
          :icon="Edit"
          @click="handleEdit(row)"
          class="mr10"
          type="warning"
      >
        Edit
      </el-link>
      <el-link
          v-if="(!handleDeleteShow || handleDeleteShow(row)) && handleDelete && !row.valid && (user.userId === row.userId || user.userId === row.creator || includes(roleCodeList, 'admin'))"
          :icon="Delete"
          @click="handleDelete(row)"
          type="danger">
        Delete
      </el-link>
    </el-space>
  </template>
  <template v-else-if="viewConfig.type === ValueType.FixedText">
    <div :style="{
        maxHeight: `${props.fullMessage ? 99999 : 90}px`,
        overflowY: 'auto',
    }">{{ row[viewConfig.value] }}
    </div>
  </template>
  <template v-else-if="viewConfig.type === ValueType.Attachment">
    <el-space wrap style="justify-content: center;">
      <a
          v-if="row[viewConfig.value] && row[viewConfig.value].length > 0"
          v-for="(t, i) in row[viewConfig.value]"
          :href="fullUrl(t['url'], '')"
          :style="{
            minWidth: `${155 / scale}px`,
          }"
      >{{ t['filename'] }}</a>
      <el-empty v-else description="Empty"
                :style="{
                  width: `${155 / scale}px`,
                  height: `${160 / scale}px`,
                  marginTop: `${30 / scale}px`,
                  marginBottom: `${30 / scale}px`,
                }"
      />
    </el-space>
  </template>
  <template v-else-if="viewConfig.type === ValueType.ManagerEdit">
    <el-link
        @click="() => {
          if(viewConfig.managerEdit) {
            viewConfig.managerEdit(row)
          }
          }"
        type="danger"
    >
      {{ row[viewConfig.value] || '--' }}
    </el-link>
  </template>
  <template v-else-if="viewConfig.type === ValueType.Template">
    <slot name="template" :param="row" :viewConfig="viewConfig"/>
  </template>
  <template v-else-if="viewConfig.type === ValueType.SelectEdit">
    <span v-if="!row[props.idKey]">
        {{ row[viewConfig.value] }}
    </span>
    <span v-else-if="!props.handleUpdate">
      {{
        (viewConfig.optionList || []).reduce((p, t) => t.value === row[viewConfig.value] ? t.label : p, row[viewConfig.value]) || '--'
      }}
    </span>
    <el-link
        v-else-if="!editSelect"
        automatic-dropdown="true"
        @click="() => handleEditSelectShow(row)"
        type="warning">
      {{
        (viewConfig.optionList || []).reduce((p, t) => t.value === row[viewConfig.value] ? t.label : p, row[viewConfig.value]) || '--'
      }}
    </el-link>
    <el-select
        v-else
        v-model="formData[viewConfig.originValue || viewConfig.value]"
        @change="handleUpdateSubmit"
        filterable
        :multiple="'[object Array]' === Object.prototype.toString.call(formData[viewConfig.originValue || viewConfig.value])"
        clearable
        :placeholder="store.state.label[viewConfig.originValue || viewConfig.value]">
      <el-option
          v-for="item in (viewConfig.optionList || [])"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      />
    </el-select>
  </template>
  <template v-else-if="viewConfig.type === ValueType.NumberEdit">
    <span v-if="!row[props.idKey]">
        {{ row[viewConfig.value] }}
    </span>
    <span v-else-if="!props.handleUpdate">
        {{ row[viewConfig.value] || '--' }}
    </span>
    <el-link
        v-else-if="!editNumber"
        automatic-dropdown="true"
        @click="() => handleEditNumberShow(row)"
        type="warning">
      {{ row[viewConfig.value] || '--' }}
    </el-link>
    <el-input
        v-else
        type="number"
        v-model="formData[viewConfig.originValue || viewConfig.value]"
        @change="handleUpdateSubmit"
        :placeholder="store.state.label[viewConfig.originValue || viewConfig.value]"
        clearable
    />
  </template>
  <template v-else-if="viewConfig.type === ValueType.TextEdit">
    <span v-if="!row[props.idKey]">
        {{ row[viewConfig.value] }}
    </span>
    <span v-else-if="!props.handleUpdate">
        {{ row[viewConfig.value] || '--' }}
    </span>
    <el-link
        v-else-if="!editText"
        automatic-dropdown="true"
        @click="() => handleEditTextShow(row)"
        type="warning">
      {{ row[viewConfig.value] || '--' }}
    </el-link>
    <el-input
        v-else
        type="textarea"
        :rows=4
        v-model="formData[viewConfig.originValue || viewConfig.value]"
        @change="handleUpdateSubmit"
        :placeholder="store.state.label[viewConfig.originValue || viewConfig.value]"
        clearable
    />
  </template>
  <template v-else-if="viewConfig.type === ValueType.DateEdit">
    <span v-if="!row[props.idKey]">
        {{ row[viewConfig.value] }}
    </span>
    <span v-else-if="!props.handleUpdate">
        {{ row[viewConfig.value] || '--' }}
    </span>
    <el-link
        v-else-if="!editDate"
        automatic-dropdown="true"
        @click="() => handleEditDateShow(row)"
        type="warning">
      {{ row[viewConfig.value] || '--' }}
    </el-link>
    <el-date-picker
        v-else
        style="width: 95%;"
        type="date"
        v-model="formData[viewConfig.originValue || viewConfig.value]"
        format="YYYY-MM-DD"
        clearable
        @change="handleDateUpdateSubmit"
    >
    </el-date-picker>
  </template>
  <template v-else-if="viewConfig.type === ValueType.ValidEdit">
    <span v-if="!props.handleUpdate">
        {{ row[viewConfig.value] || '--' }}
    </span>
    <el-link
        v-else-if="!editValid"
        automatic-dropdown="true"
        @click="() => handleEditValidShow(row)"
        type="warning">
      {{ row[viewConfig.value] || '--' }}
    </el-link>
    <el-switch v-else v-model="formData[viewConfig.value]" active-text="Yes" inactive-text="No" @change="handleUpdateSubmit"/>
  </template>
  <template v-else-if="viewConfig.type === ValueType.Link">
    <el-link
        v-if="viewConfig.openLink"
        @click="viewConfig.openLink(row)"
        type="danger"
    >
      {{ row[viewConfig.value] }}
    </el-link>
    <span v-else>{{ row[viewConfig.value] }}</span>
  </template>
  <template v-else-if="viewConfig.type === null || viewConfig.type === undefined">
    <span v-if="viewConfig.highLight" class="high-light">{{ row[viewConfig.value] }}</span>
    <span v-else>{{ row[viewConfig.value] }}</span>
  </template>
</template>

<script lang="ts" setup>
import {ref, Ref, watch} from 'vue'
import {Delete, Edit, Minus, Tools, View} from '@element-plus/icons-vue'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import {fullUrl} from '@/util/EnvUtil'
import {includes} from '@/util/ArrayUtil'
import {useRoute, useRouter} from 'vue-router'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Industry'
import {httpPutJson} from '@/util/HttpUtil'
import {ElMessage} from 'element-plus'
import {formatDate} from '@/util/DateUtil'

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const showDetail = ref(false)

interface Config {
  idKey: string;
  viewConfig: ViewConfig;
  row: any;
  fullMessage?: boolean;
  handleEdit?: (row: any) => {};
  handleUpdate?: (row: any) => {};
  handleEditShow?: (row: any) => {};
  handleDeleteShow?: (row: any) => {};
  handleDelete?: (row: any) => {};
  handleShowDetail?: (row: any) => {};
  scale?: number;
}

const editSelect = ref(false)
const editNumber = ref(false)
const editText = ref(false)
const editDate = ref(false)
const editValid = ref(false)
const formData: Ref<any> = ref({})
const handleEditSelectShow = (row: any) => {
  editSelect.value = true
  formData.value = Object.assign({}, row)
}
const handleEditNumberShow = (row: any) => {
  editNumber.value = true
  formData.value = Object.assign({}, row)
}
const handleEditTextShow = (row: any) => {
  editText.value = true
  formData.value = Object.assign({}, row)
}
const handleEditDateShow = (row: any) => {
  editDate.value = true
  formData.value = Object.assign({}, row)
}
const handleEditValidShow = (row: any) => {
  editValid.value = true
  formData.value = Object.assign({}, row)
}
const handleDateUpdateSubmit = (v: any) => {
  const key: string = viewConfig.value.originValue || viewConfig.value.value
  formData.value[key] = formData.value[key] ? formatDate(formData.value[key], 'yyyy-MM-dd') : ''
  handleUpdateSubmit(v)
}
const handleUpdateSubmit = (v: any) => {
  if (props.handleUpdate) {
    const promise: any = props.handleUpdate(formData.value)
    if (promise && promise.then) {
      promise.then((r: any) => {
        editSelect.value = false
        editNumber.value = false
        editText.value = false
        editDate.value = false
        editValid.value = false
      })
    }
  } else {
    console.log('update func empty...')
  }
}
const handleReport = (row: any) => {
  router.push(
      {
        path: '/industry/admin/product/report',
        query: {
          productId: row.productId,
        },
      })
}

const handleShowDetail = (row: any) => {
  showDetail.value = true
}
const handleJumpOrder = (t: any) => {
  router.push(
      {
        path: '/industry/admin/product/order',
        query: {
          orderId: t.orderId,
        },
      })
}
const handleJumpDevice = (t: any) => {
  router.push(
      {
        path: '/industry/admin/product/device',
        query: {
          deviceId: t.testDevice,
        },
      })
}
const handleConvertUserDeviceList = (row: any = props.row) => {
  const l: any[] = []
  const userDeviceList: any[] = row.userDeviceList || []
  for (let i = 0; i < userDeviceList.length; i++) {
    const t = userDeviceList[i]
    const timestampEnd: string = `${t.deviceRunningEndHour}:${t.deviceRunningEndMinute}`
    const timestampStart: string = `${t.deviceRunningStartHour}:${t.deviceRunningStartMinute}`
    const end = {
      type: l.length === 0 ? '' : 'success',
      hollow: l.length === 0,
      timestamp: timestampEnd,
      content: l.length === 0 ? `Stop work(${row.deviceUseMinute}m)` : `Working(+${t.deviceRunningMinute}m)`
    }
    if (i > 0 && l[l.length - 1].timestamp === timestampEnd) {
      l[l.length - 1] = end
    } else {
      l.push(end)
    }
    l.push({
      type: i < userDeviceList.length - 1 ? 'danger' : '',
      hollow: i >= userDeviceList.length - 1,
      timestamp: timestampStart,
      wrongData: i < userDeviceList.length - 1,
      content: `Start work`
    })
  }
  return l
}
const emit = defineEmits([])
const props = withDefaults(defineProps<Config>(), {
  scale: 1,
  fullMessage: false,
})
const viewConfig = ref(props.viewConfig)
const row = ref(props.row)
const handleEdit = props.handleEdit
const handleEditShow = props.handleEditShow
const handleDeleteShow = props.handleDeleteShow
const handleDelete = props.handleDelete
const scale = ref(props.scale || 1)
defineExpose({})

watch(
    () => props.viewConfig,
    (t) => {
      viewConfig.value = Object.assign({}, t)
    },
    {
      immediate: true,
    }
)
watch(
    () => props.scale,
    (t) => {
      scale.value = t
    },
    {
      immediate: true,
    }
)
watch(
    () => props.row,
    (t) => {
      row.value = Object.assign({}, t)
    },
    {
      immediate: true,
    }
)
</script>
<style lang="scss" scoped></style>
