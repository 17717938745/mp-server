<template>
  <div class="resources-upload" v-if="props.fileList">
    <el-upload
        v-show="props.fileList.length < props.maxSize"
        action="#"
        :show-file-list="false"
        :on-change="handleFileChange"
        :before-upload="handleBeforeUpload"
        :http-request="handleRequest"
        multiple
        :drag="true"
    >
      <div>
        <el-icon style="width: 76px; height: 76px">
          <upload-filled style="width: 5em; height: 5em"/>
        </el-icon>
        <div class="el-upload__text">Click to upload file</div>
      </div>
    </el-upload>
    <div
        v-for="(t, i) in props.fileList" :key="t.url"
        class="resources-view"
    >
      <a
          :href="fullUrl(t.url, '')"
      >{{ t.filename }}</a>
      <el-icon class="close-btn" :size="28" @click="props.fileList.splice(i, 1)">
        <CircleClose/>
      </el-icon>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Industry'
import {watch,} from 'vue'
import {CircleClose, UploadFilled,} from '@element-plus/icons-vue'
import {ElMessage, UploadFile, UploadFiles} from 'element-plus'
import {fullUrl} from '@/util/EnvUtil'
import {httpUpload} from '@/util/HttpUtil'

const props = withDefaults(defineProps<PropType>(), {
  maxSize: 4,
})
const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()

interface File {
  url: string
  filename: string
  fileId: string
}

interface PropType {
  fileList: File[]
  maxSize?: number
}

defineExpose({})

const fileMap: any = {}
const handleFileChange = (file: UploadFile, fileList: UploadFiles) => {
  console.log('file change, length: ' + fileList.length)
}
const handleBeforeUpload = (file: UploadFile) => {
  console.log('before uplaod file: ' + file.uid)
  fileMap[file.uid] = file
  return file
}
const handleRequest = (d: any) => {
  const keys = Object.keys(fileMap)
  const limit = Math.max((props.maxSize || 0) - props.fileList.length, 0)
  if (keys.length > limit) {
    ElMessage.error(`Too many upload, limit: ${props.maxSize}, remain: ${limit}, actual: ${keys.length}`)
    keys.forEach((k: any) => {
      delete fileMap[k]
    })
  } else if (keys.length > 0) {
    Promise.all(keys.map((k: any) => {
      const t = fileMap[k]
      const formData = new FormData()
      formData.set("filename", t.name)
      formData.set("file", t)
      return httpUpload(`index/file`, formData)
    }))
    .then((l: any[]) => {
      l.forEach((t: any) => {
        const d = t.data
        props.fileList.push({
          url: d.url,
          fileId: d.fileId,
          filename: d.filename,
        })
      })
      return Promise.resolve()
    })
    .catch((err) => {
      ElMessage.success(`Upload failed`)
      return Promise.reject()
    })
    keys.forEach((k: any) => {
      delete fileMap[k]
    })
  }
}
watch(
    () => props.fileList,
    (l) => {
      // console.log(`fileList: ${JSON.stringify(l)}`)
      // replaceAll(props.fileList || [], l || [])
    },
    {
      immediate: true,
    }
)
</script>
<style lang="scss">
</style>
