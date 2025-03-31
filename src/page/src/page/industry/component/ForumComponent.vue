<template>
  <div style="background-color: #f4f6f9; display: flex; align-items: start; justify-content: center; padding: 10px 10px 10px 10px; flex-wrap: wrap;">
    <div
        style="background-color: #ffffff; margin-right:40px ;border-radius: 5px; padding: 0; margin-bottom: 20px;"
        class="forum-content"
    >
      <el-affix position="top" :offset="90">
        <div class="douson-flex" style="justify-content: space-between; font-size: 20px; font-weight: 600; border-bottom: 1px solid #eeeeee; padding: 20px; background: radial-gradient(circle, #7b6262, #be8b5a, #6facb7); color: #2927cf;">
        <span>
          {{ store.state.label[props.titleLabelKey] }}
          <el-input
              v-model="forumRequest.data.title"
              @keyup.enter="handleResetForumPage"
              style="width: 240px"
              placeholder="Please Input"
              :suffix-icon="Search"
              input-style="margin-left: 5px;"
          />
        </span>
          <span>
          <el-button round :icon="EditPen" @click="formVisible = true">
            {{ store.state.label.comment }}
          </el-button>
          <el-button class="login-btn" type="primary" @click="handlePageChange" style="margin-right: 10px;" :disabled="loading" v-if="forumData.list.length < forumData.total">{{ store.state.label.loadMore }}</el-button>
        </span>
        </div>
      </el-affix>
      <ul style="overflow-y: auto;">
        <li v-for="(d, i) in (forumData.list || [])" :key="`forum-${i}`" style="border-bottom: 1px solid #eeeeee; padding: 20px;">
          <div style="display: inline-flex; align-items: center;">
            <el-icon v-if="d.userId === user.userId" style="margin-right: 5px;" @click="() => {
              forumId = d.forumId
              formVisible = true
            }">
              <Edit/>
            </el-icon>
            <h3 style="font-size: 18px;" v-html="d.title"></h3>
          </div>
          <div v-if="showAll[d.forumId]" style="margin-bottom: 15px; display: flex; align-items: center; padding-left: 5px;">
            <el-icon style="margin-right: 5px;">
              <UserFilled/>
            </el-icon>
            {{ d.userIdFormat }}
          </div>
          <div :style="{
             maxHeight: showAll[d.forumId] ? null : '120px',
             overflowY: 'hidden',
             marginBottom: 10,
             position: 'relative'
            }">
            <div v-html="d.content" class="douson-h5"></div>
          </div>
          <div style="height: 50px; display: flex; align-items: center; justify-content: space-between; cursor: pointer; color: #c4c7ce">
              <span style="display: flex; align-items: center;">
                <el-button :icon="CaretTop" :type="d.thumbsUpType === 1 ? 'success' : 'primary'" class="thumbs-up-btn" @click="handleForumThumbsUp(d, true)" plain>{{ store.state.label.agree }} {{ d.thumbsUp }}</el-button>
                <el-button :icon="CaretBottom" :type="d.thumbsUpType === 2 ? 'success' : 'primary'" class="thumbs-down-btn" @click="handleForumThumbsUp(d, false)" plain></el-button>
                <span style="margin-left: 30px; margin-right: 15px;" class="icon-text" @click="showCommentaryAll[d.forumId] = !showCommentaryAll[d.forumId]">
                  <el-icon style="margin-right: 5px;"><ChatDotSquare/></el-icon>{{ d.commentary }} 条{{ store.state.label.comment }}
                </span>
                <span v-if="user.username === 'admin' || user.userId === d.userId" style="cursor: pointer; margin-right: 15px;" class="icon-text" @click="handleDelete(d)">
                  <el-icon style="margin-right: 5px;"><Delete/></el-icon>{{ store.state.label.delete }}
                </span>
              </span>
            <span style="display: flex; align-items: center;">
                <span @click="showAll[d.forumId] = !showAll[d.forumId]" class="icon-text">
                  <span v-if="showAll[d.forumId]" class="icon-text">
                    {{ store.state.label.readSummary }} <el-icon style="margin-right: 5px;"><ArrowUp/></el-icon>
                  </span>
                  <span v-else class="icon-text">
                    {{ store.state.label.readAll }} <el-icon style="margin-right: 5px;"><ArrowDown/></el-icon>
                  </span>
                </span>
              </span>
          </div>
          <div>
            <commentary v-if="showCommentaryAll[d.forumId]" :forum-id="d.forumId"/>
          </div>
        </li>
      </ul>
    </div>
    <el-dialog :title="'Forum'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <tinymce :forum-id="forumId" :category="props.titleLabelKey" :after-success="() => {formVisible = false; handleForumPage(true);}"></tinymce>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="formVisible = false">Cancel</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {ref,} from 'vue'
import {useRoute, useRouter,} from 'vue-router'
import {httpDelete, httpGet, httpPutJson,} from '@/util/HttpUtil'
import {DataResult, PageResult} from '@/typing/ma/System'
import {ElMessage, ElMessageBox} from 'element-plus'
import {DEFAULT_PAGE,} from '@/typing/Common'
import {ArrowDown, ArrowUp, CaretBottom, CaretTop, ChatDotSquare, Delete, Edit, EditPen, Search, UserFilled,} from '@element-plus/icons-vue'
import {Store, useStore} from 'vuex'
import Commentary from './Commentary.vue'
import Tinymce from './Tinymce.vue'

interface PropType {
  titleLabelKey?: string
}

const props = withDefaults(defineProps<PropType>(), {
  titleLabelKey: 'dousonVoiceCommunity',
})
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const router = useRouter()
const route = useRoute()
const formVisible = ref(false)
const forumId = ref(route.query.forumId || '')
const forumRequest = ref({
  page: {
    page: DEFAULT_PAGE,
    limit: 5,
  },
  data: {
    title: '',
    category: props.titleLabelKey,
  },
})
const forumData = ref({
  total: 0,
  list: [],
})
const showAll = ref({})
const showCommentaryAll = ref({})
const loading = ref(false)
const handlePageChange = () => {
  if (!loading.value && forumData.value.list.length < forumData.value.total) {
    loading.value = true
    forumRequest.value.page.page = forumRequest.value.page.page + 1
    handleForumPage()
  } else {
    console.log(`not change...`)
  }
  loading.value = false
}

const handleResetForumPage = () => {
  forumRequest.value.page.page = 1
  handleForumPage(true)
}
const handleForumPage = (reset: boolean = false) => {
  httpGet(`forum/page`, forumRequest.value).then(
      (res: PageResult<any>) => {
        forumData.value.total = res.total
        if (reset) {
          forumData.value.list = res.list || []
        } else {
          forumData.value.list.push(...(res.list || []))
        }
        ElMessage.success("Query success")
        loading.value = false
      }
  ).catch(r => {
    loading.value = false
  })
}
const handleForumThumbsUp = (t: any, thumbsUp: boolean) => {
  const param = {
    thumbsUp: thumbsUp,
    forumId: t.forumId
  }
  httpPutJson(`forum/thumbs-up/merge`, param).then(
      (res: DataResult<any>) => {
        t.thumbsUp += res.data.thumbsUpChange
        t.thumbsUpType = res.data.thumbsUpType
        ElMessage.success("Thumbs up success")
      }
  )
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('forum', {
      forumId: row.forumId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handleForumPage(true)
    })
  })
}
handleForumPage()
</script>

<style scoped>

.infinite-list {
  padding: 0;
  margin: 0;
  list-style: none;
}

.infinite-list .infinite-list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  margin: 10px;
  color: #eeeeee;
}

.infinite-list .infinite-list-item + .list-item {
  margin-top: 10px;
}

.thumbs-up-btn {
  padding: 5px 10px;
}

.thumbs-down-btn {
  padding: 5px 10px;
}

.icon-text {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  height: 30px;
}

.forum-content {
  width: 1024px;
  max-width: 90vw;
}

.todo-content {
  width: 256px;
}

@media (max-width: 1024px) {
  .forum-content {
    width: 512px;
    max-width: 95vw;
  }
}
</style>
