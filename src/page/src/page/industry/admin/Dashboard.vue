<template>
  <div style="background-color: #f4f6f9; display: flex; align-items: start; justify-content: center; padding: 10px 10px 10px 10px; flex-wrap: wrap;">
    <forum-component :title-label-key="'dousonVoiceCommunity'"/>
    <div
        style="background-color: #ffffff;  padding: 10px; border-radius: 5px;"
        class="todo-content"
    >
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
        </el-card>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref,} from 'vue'
import {useRoute, useRouter,} from 'vue-router'
import {httpGet,} from '@/util/HttpUtil'
import {DataResult} from '@/typing/ma/System'
import {ElMessage} from 'element-plus'
import {Store, useStore} from 'vuex'
import ForumComponent from '../component/ForumComponent.vue'

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
handleTodoList()
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
