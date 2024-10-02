<template>
  <div style="background-color: #f4f6f9; display: flex; align-items: start; justify-content: center; padding: 10px 10px 10px 10px; flex-wrap: wrap;">
    <div
        style="background-color: #ffffff; margin-right:40px ;border-radius: 5px; padding: 0; margin-bottom: 20px;"
        class="forum-content"
    >
      <div style="font-size: 16px; border-bottom: 1px solid #eeeeee; padding: 20px;">
        {{ store.state.label.forum }}
        <el-input
            v-model="forumRequest.data.title"
            @keyup.enter="handleResetForumPage"
            style="width: 240px"
            placeholder="Please Input"
            :suffix-icon="Search"
            input-style="margin-left: 5px;"
        />
      </div>
      <ul v-infinite-scroll="handlePageChange" style="overflow-y: auto;">
        <li v-for="(d, i) in (forumData.list || [])" :key="`forum-${i}`" style="border-bottom: 1px solid #eeeeee; padding: 20px;">
          <div style="display: inline-flex; align-items: center;">
            <el-icon v-if="d.userId === user.userId" style="margin-right: 5px;" @click="router.push({
                path: '/industry/admin/forum',
                query: {
                  forumId: d.forumId
                }
              })">
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
    <div
        style="background-color: #ffffff;  padding: 10px; border-radius: 5px;"
        class="todo-content"
    >
      <div style="display: flex; justify-content: start; margin: 10px 0 10px;">
        <el-button round :icon="EditPen" @click="router.push({
          path: '/industry/admin/forum',
          query: {
            forumId: ''
          }
        })">
          {{ store.state.label.comment }}
        </el-button>
      </div>
      <div style="width: 100%; display: flex; justify-content: center;">
        <el-card style="width: 100%; max-width: 260px;">
          <template #header>
            <div class="card-header">
              <span>待办 Hàng chờ làm</span>
            </div>
          </template>
          <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto;">
            <li v-for="i in todo.list || []" :key="i" class="infinite-list-item">
              <el-link
                  @click="handleJump(i)"
                  class="mr10"
                  type="danger"
              >
                {{ i.label }}
              </el-link>
            </li>
          </ul>
          <!--          <template #footer>Footer content</template>-->
        </el-card>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref,} from 'vue'
import {useRouter, useRoute,} from 'vue-router'
import {httpGet, httpPutJson, httpDelete,} from '@/util/HttpUtil'
import {DataResult} from '@/typing/ma/System'
import {ElMessage, ElMessageBox} from 'element-plus'
import {PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {CaretTop, CaretBottom, ChatDotSquare, Delete, ArrowDown, ArrowUp, UserFilled, EditPen, Edit, Search,} from '@element-plus/icons-vue'
import {Store, useStore} from 'vuex'
import Commentary from '../component/Commentary.vue'

const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user

const router = useRouter()
const route = useRoute()
const count = ref(0)
const load = () => {
  count.value += 2
}
const todo = ref({
  count: 0,
  list: [],
})
const forumRequest = ref({
  page: {
    page: DEFAULT_PAGE,
    limit: DEFAULT_LIMIT,
  },
  data: {
    title: '',
  },
})
const forumData = ref({
  total: 0,
  list: [],
})
const showAll = ref({})
const showCommentaryAll = ref({})
const handleTodoList = () => {
  httpGet(`douson/todo/list`, {}).then(
      (res: DataResult<any>) => {
        todo.value.count = res.data.count
        todo.value.list = res.data.list || []
        ElMessage.success("Query success")
      }
  )
}
const handleJump = (t: any) => {
  if (0 === t.type) {
    handleJumpToDisqualification(t)
  } else if (1 === t.type) {
    handleJumpToEhs(t)
  } else if (2 === t.type) {
    handleJumpToImprove(t)
  } else if (3 === t.type) {
    handleJumpToMaintain(t)
  } else if (4 === t.type) {
    handleJumpToQuality(t)
  } else if (5 === t.type) {
    handleJumpToCrash(t)
  } else if (6 === t.type) {
    handleJumpToTrouble(t)
  } else {
    ElMessage.warning("Not support todo: " + JSON.stringify(t))
  }
}
const handleJumpToDisqualification = (t: any) => {
  router.push({
    path: '/industry/admin/report/disqualification',
    query: {
      disqualificationOrderId: t.id,
    }
  })
}
const handleJumpToEhs = (t: any) => {
  router.push({
    path: '/industry/admin/report/accident/event',
    query: {
      eventId: t.id,
    }
  })
}
const handleJumpToQuality = (t: any) => {
  router.push({
    path: '/industry/admin/report/accident/quality',
    query: {
      qualityId: t.id,
    }
  })
}
const handleJumpToCrash = (t: any) => {
  router.push({
    path: '/industry/admin/report/accident/crash',
    query: {
      crashId: t.id,
    }
  })
}
const handleJumpToTrouble = (t: any) => {
  router.push({
    path: '/industry/admin/report/accident/trouble',
    query: {
      troubleId: t.id,
    }
  })
}
const handleJumpToImprove = (t: any) => {
  router.push({
    path: '/industry/admin/report/accident/improve',
    query: {
      improveId: t.id,
    }
  })
}
const handleJumpToMaintain = (t: any) => {
  router.push({
    path: '/industry/admin/asset/machine/maintain',
    query: {
      maintainId: t.id,
    }
  })
}
const handlePageChange = () => {
  if (forumData.value.list.length < forumData.value.total) {
    forumRequest.value.page.page = forumRequest.value.page.page + 1
    handleForumPage()
  } else {
    console.log(`not change...`)
  }
}

const handleResetForumPage = () => {
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
      }
  )
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
      handleForumPage()
    })
  })
}
handleTodoList()
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
