<template>
  <div>
    welcome to <a href="https://vn.douson.cn/">https://vn.douson.cn/</a>
    <el-row :gutter="20" justify="center">
      <el-col
          :xs="24"
          :sm="24"
          :md="{ span: 12, offset: 0 }"
          :lg="{ span: 11, offset: 0 }"
          :xl="{ span: 10, offset: 0 }"
      >
        <el-card style="max-width: 260px">
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
      </el-col>
      <el-col
          :xs="24"
          :sm="24"
          :md="{ span: 12, offset: 0 }"
          :lg="{ span: 11, offset: 2 }"
          :xl="{ span: 10, offset: 4 }"
      >
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import {ref,} from 'vue'
import {useRouter, useRoute,} from 'vue-router'
import {httpGet} from '@/util/HttpUtil'
import {DataResult} from '@/typing/ma/System'
import {ElMessage} from 'element-plus'

const router = useRouter()
const route = useRoute()
const count = ref(0)
const load = () => {
  // count.value += 2
}
const todo = ref({
  count: 0,
  list: [],
})
httpGet(`douson/todo/list`, {}).then(
    (res: DataResult<any>) => {
      todo.value.count = res.data.count
      todo.value.list = res.data.list || []
      ElMessage.success("Query success")
    }
)
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
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}

.chart {
}

.infinite-list {
  height: 300px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.infinite-list .infinite-list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  background: var(--el-color-primary-light-9);
  margin: 10px;
  color: var(--el-color-primary);
}

.infinite-list .infinite-list-item + .list-item {
  margin-top: 10px;
}
</style>
