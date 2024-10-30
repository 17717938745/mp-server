<template>
  <div class="resources-upload" v-if="props.videoList">
    <el-upload
        v-show="props.videoList.length < props.maxSize"
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
        <div class="el-upload__text">Click to upload video</div>
      </div>
    </el-upload>
    <div v-for="(temp, i) in props.videoList" :key="temp.videoUrl"
         class="resources-view"
         style="width: 100%;"
    >
      <video width="320" style="max-width: 100%;" controls>
        <!--:type="`video/${temp.extension}`"-->
        <source :src="fullUrl(temp.videoUrl, '')"
                :type="`video/mp4`"
        >
        Your browser not support vidio tag
      </video>
      <el-icon class="close-btn" :size="28" @click="props.videoList.splice(i, 1)">
        <CircleClose/>
      </el-icon>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Industry'
import {watch} from 'vue'
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

interface Video {
  videoUrl: string
  videoCompressUrl: string
  extension: string
}

interface PropType {
  videoList: Video[]
  maxSize?: number
}

defineExpose({})

const fileMap: any = {}
const handleFileChange = (file: UploadFile, fileList: UploadFiles) => {
  console.log('file change, length: ' + fileList.length)
}
const handleBeforeUpload = (file: UploadFile) => {
  console.log('before upload file: ' + file.uid)
  fileMap[file.uid] = file
  return file
}
const handleRequest = (d: any) => {
  const keys = Object.keys(fileMap)
  const limit = Math.max((props.maxSize || 0) - props.videoList.length, 0)
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
      return httpUpload(`index/video`, formData)
    }))
    .then((l: any[]) => {
      l.forEach((t: any) => {
        const d = t.data
        props.videoList.push({
          videoUrl: d.url,
          videoCompressUrl: d.compressUrl,
          extension: d.extension,
        })
      })
      return Promise.resolve()
    })
    .catch((err) => {
      ElMessage.error(`Upload failed`)
      return Promise.reject()
    })
    keys.forEach((k: any) => {
      delete fileMap[k]
    })
  }
}
watch(
    () => props.videoList,
    (l) => {
      // console.log(`videoList: ${JSON.stringify(l)}`)
      // replaceAll(props.videoList || [], l || [])
    },
    {
      immediate: true,
    }
)
</script>
<style lang="scss">
</style>
