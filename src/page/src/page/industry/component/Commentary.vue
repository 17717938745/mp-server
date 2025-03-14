<template>
  <div style="width: 100%;">
    <div>
      <el-input
          v-if="formData.forumId"
          ref="commentaryRef"
          v-model="formData.content"
          style="width: 100%;"
          size="large"
          :placeholder="commentaryPlaceHolder"
          :suffix-icon="ChatDotSquare"
          :disabled="submitDisable"
          @keyup.enter="handleMerge"
      />
    </div>
    <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto; position: relative; width: 100%;">
      <li v-for="(d, i) in (commentaryData.list || [])" :key="i" class="infinite-list-item" style="position: relative; width: 100%;">
        <h3 class="icon-text" style="font-size: 16px;">
          <el-icon style="margin-right: 5px;">
            <UserFilled/>
          </el-icon>
          {{ d.userIdFormat }}
        </h3>
        <div style="width: 100%; margin-bottom: 10px; font-size: 15px;">{{ d.content }}</div>
        <div style="color: #aaaaaa; width: 100%; display: flex; justify-content: space-between; font-size: 13px;">
          <span class="icon-text">{{ d.createdTimeFormat }}</span>
          <span style="display: flex; align-items: center;">
            <span v-if="user.username === 'admin' || user.userId === d.userId" style="cursor: pointer; margin-right: 5px;" class="icon-text" @click="handleDelete(d)">
              <el-icon style="margin-right: 5px;"><Delete/></el-icon> 删除
            </span>
            <span style="cursor: pointer; margin-right: 5px;" class="icon-text" @click="props.prepareWriteCommentary ? props.prepareWriteCommentary(d) : handlePrepareWriteCommentary(d)">
              <el-icon style="margin-right: 5px;"><ChatDotSquare/></el-icon> 评论
            </span>
          </span>
        </div>
        <div :style="{
          paddingLeft: 8 * (props.level + 1) + 'px',
          width: '100%',
        }">
          <commentary v-if="(d.children || []).length > 0" :children="d.children || []" :level="props.level + 1" :prepare-write-commentary="props.prepareWriteCommentary ? props.prepareWriteCommentary : handlePrepareWriteCommentary" :after-write-commentary="props.afterWriteCommentary ? props.afterWriteCommentary : handleCommentaryTree"/>
        </div>
      </li>
    </ul>
  </div>
</template>

<!--suppress TypeScriptUnresolvedFunction, JSUnusedGlobalSymbols, TypeScriptUnresolvedVariable -->
<script lang="tsx" setup>
import {onMounted, reactive, ref, toRefs, watch, watchEffect,} from 'vue'
import {Store, useStore} from 'vuex'
import {useRouter} from 'vue-router'
import {httpGet, httpPutJson, httpDelete,} from '@/util/HttpUtil'
import {ElMessage, ElMessageBox} from 'element-plus'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {DataResult, PageResult} from '@/typing/ma/System'
import {StoreType} from '@/store/Industry'
import Commentary from './Commentary.vue'
import {CaretTop, Delete, ChatDotSquare, ArrowDown, ArrowUp, UserFilled, EditPen, Edit,} from '@element-plus/icons-vue'

interface PropType {
  forumId?: string
  level?: number
  children?: any[]
  prepareWriteCommentary?: Function
  afterWriteCommentary?: Function
}

const commentaryRef = ref(null)
const commentaryPlaceHolder = ref('写评论')
const props = withDefaults(defineProps<PropType>(), {
  forumId: '',
  level: 0,
  children: [],
})


const store: Store<StoreType> = useStore()
const storeState: StoreType = store.state
const user = store.state.user
const router = useRouter()
const formData = ref({
  forumId: props.forumId,
  parentId: '',
  content: '',
})
const commentaryData = ref({
  total: 0,
  list: props.children,
})
const submitDisable = ref(false)
onMounted(() => {
})
const count = ref(0)
const load = () => {
  count.value += 2
}
const handlePrepareWriteCommentary = (d: any) => {
      formData.value.parentId = d.commentaryId
      commentaryPlaceHolder.value = `To ${d.userIdFormat}：`
      commentaryRef.value.focus()
      commentaryRef.value.select()
    },
    handleGetCommentary = (children: any[], commentaryId: string) => {
      for (let i = 0; i < children.length; i++) {
        if (children[i].commentaryId === commentaryId) {
          return children[i];
        }
        if ((children[i].children || []).length > 0) {
          const r = handleGetCommentary(children[i].children, commentaryId)
          if (r) {
            return r
          }
        }
      }
      return null
    },
    handleMerge = () => {
      if (formData.value.content && !submitDisable.value) {
        submitDisable.value = true
        httpPutJson(`forum/commentary`, formData.value).then((r: any) => {
          ElMessage.success("Merge success")
          submitDisable.value = false
          formData.value.content = ''
          const parent = handleGetCommentary(commentaryData.value.list, formData.value.parentId)
          if (parent && parent.children) {
            parent.children.push(r.data)
          }
          /*if (props.afterWriteCommentary) {
            props.afterWriteCommentary(r)
          } else {
            handleCommentaryTree()
          }*/
        })
        .catch((r: any) => {
          submitDisable.value = false
        })
      }
    },
    handleDelete = (row: any) => {
      ElMessageBox.confirm('Confirm Delete?', 'Tips', {
        type: 'warning',
      }).then(() => {
        httpDelete('forum/commentary', {
          forumId: row.forumId,
          commentaryId: row.commentaryId,
        })
        .then(() => {
          ElMessage.success('Delete success')
          formData.value.content = ''
          const parent = handleGetCommentary(commentaryData.value.list, row.parentId)
          const list = parent && parent.children ? parent.children :  commentaryData.value.list
          if (list) {
            for (let i = 0; i < list.length; i++) {
              const t = list[i]
              if (t.commentaryId === row.commentaryId) {
                list.splice(i, 1)
                break
              }
            }
          }
          /*if (props.afterWriteCommentary) {
            props.afterWriteCommentary(r)
          } else {
            handleCommentaryTree()
          }*/
        })
      })
    },
    handleCommentaryTree = () => {
      if (formData.value.forumId) {
        httpGet("forum/commentary/tree", {
          page: {
            page: DEFAULT_PAGE,
            // limit: DEFAULT_LIMIT,
            limit: 99999,
          },
          data: {
            forumId: formData.value.forumId,
          }
        })
        .then((result: PageResult<any>) => {
          ElMessage.success("Query success")
          commentaryData.value.total = result.total || 0
          commentaryData.value.list = result.list || []
        })
      }
    }
watch(
    () => props.forumId,
    (t: string) => {
      formData.value.forumId = t
      handleCommentaryTree()
    },
    {
      immediate: true,
    }
)
</script>

<style lang="scss">
.infinite-list {
  // height: 300px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.infinite-list .infinite-list-item {
  display: flex;
  flex-wrap: wrap;
  align-items: start;
  justify-content: start;
  padding: 0;
}

.infinite-list .infinite-list-item + .list-item {
  margin-top: 10px;
}

.icon-text {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  height: 30px;
}
</style>
