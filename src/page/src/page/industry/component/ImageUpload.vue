<template>
  <div class="resources-upload" tabindex="1" v-if="props.photoList">
    <el-upload
        v-show="props.photoList.length < props.maxSize"
        action="#"
        :show-file-list="false"
        :on-change="handleFileChange"
        :before-upload="handleBeforeUpload"
        :http-request="handleRequest"
        multiple
        :drag="true"
        :disabled="props.disabled"
        v-if="!props.disabled"
    >
      <div>
        <el-icon style="width: 76px; height: 76px;">
          <upload-filled style="width: 5em; height: 5em"/>
        </el-icon>
        <div class="el-upload__text">Click to upload image</div>
      </div>
    </el-upload>
    <div
        v-if="props.photoList && props.photoList.length > 0"
        v-for="(t, i) in props.photoList" :key="t.photoUrl"
        class="resources-view"
    >
      <el-image
          :src="fullUrl(t.photoCompressUrl, '')"
          fit="cover"
          :preview-src-list="[fullUrl(t.photoUrl, '')]"
          :initial-index="0"
          class="image-view"
      />
      <el-icon v-if="!props.disabled" class="close-btn" :size="28" @click="props.photoList.splice(i, 1)">
        <CircleClose/>
      </el-icon>
    </div>
    <el-empty v-else-if="props.disabled" description="Empty"/>
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
  disabled: false,
})
const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()

interface Image {
  photoUrl: string
  photoCompressUrl: string
}

interface PropType {
  photoList: Image[]
  maxSize?: number
  disabled: boolean
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
  const limit = Math.max((props.maxSize || 0) - props.photoList.length, 0)
  if (keys.length > limit) {
    ElMessage.error(`Too many upload, limit: ${props.maxSize}, remain: ${limit}, actual: ${keys.length}`)
    keys.forEach((k: any) => {
      delete fileMap[k]
    })
  } else if (keys.length > 0) {
    Promise.all(keys.map((k: any) => {
      const t = fileMap[k]
      const formData = new FormData()
      formData.set('filename', t.name)
      formData.set('file', t)
      return httpUpload(`index/img`, formData)
    }))
    .then((l: any[]) => {
      l.forEach((t: any) => {
        const d = t.data
        props.photoList.push({
          photoUrl: d.url,
          photoCompressUrl: d.compressUrl,
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
    () => props.photoList,
    (l) => {
      // console.log(`photoList: ${JSON.stringify(l)}`)
      // replaceAll(props.photoList || [], l || [])
    },
    {
      immediate: true,
    }
)
</script>
<style lang="scss">
</style>
