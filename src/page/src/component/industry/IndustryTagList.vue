<template>
  <div
      class="tag m-b-24 p-x-24 h-40 d-flex bgc-fff align-items-center justify-content-between fz-14"
      v-if="tagList.length > 0"
      style="padding: 5px 5px 5px 5px; display: flex; background-color: #ffffff"
  >
    <div class="tag-left">
      <el-space>
        <el-link
            v-for="(item, index) in tagList"
            :key="index"
            :type="handleIsActive(item.path) ? 'primary' : 'info'"
            class="li p-r-24"
        >
          <span @click="handleGoTo(item.path)" class="mr-[5px]">{{ item.nameKey ? store.state.label[item.nameKey] : (item.title || item.name) }}</span>
          <el-icon @click="handleClose(index)" class="w-[10px] h-[10px]" size="10">
            <Close/>
          </el-icon>
        </el-link>
      </el-space>
    </div>
    <el-dropdown class="tag-control" @command="handleDealTag">
      <el-button size="small" type="primary" link> 标签选项</el-button>
      <template #dropdown>
        <el-dropdown-menu size="small">
          <el-dropdown-item command="other">关闭其他</el-dropdown-item>
          <el-dropdown-item command="all">关闭所有</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script lang="tsx" setup>
import {computed} from "vue"
import {Store, useStore} from "vuex"
import {onBeforeRouteUpdate, useRoute, useRouter} from "vue-router"
import {Close} from "@element-plus/icons-vue"
import {StoreType} from "../../store/Industry"

const route = useRoute();
const store: Store<StoreType> = useStore();
const storeState: StoreType = store.state;
const router = useRouter();
const tagList = computed(() => storeState.tagList);

const handleClose = (index: number) => {
      const delItem = tagList.value[index];
      store.commit("deleteTag", {index});
      const item = tagList.value[index]
          ? tagList.value[index]
          : tagList.value[index - 1];
      if (item) {
        delItem.path === route.fullPath && router.push(item.path);
      } else {
        router.push("/industry/");
      }
    },
    handleGoTo = (path: any) => {
      router.push(path);
    },
    handleIsActive = (path: any) => {
      return path === route.fullPath;
    },
    routeListToTagList = (route: any) => {
      let isExist = tagList.value.some((item) => {
        return item.path === route.fullPath;
      })
      if (!isExist) {
        if (tagList.value.length >= 8) {
          store.commit("deleteTag", {index: 0});
        }
        store.commit("addTag", {
          name: route.name,
          title: route.meta.title,
          nameKey: route.meta.nameKey,
          path: route.fullPath,
        });
      }
    },
    handleCloseAll = () => {
      store.commit("clearTagList");
    },
    handleCloseOther = () => {
      store.commit(
          "closeOtherTagList",
          tagList.value.filter((item) => {
            return item.path === route.fullPath;
          })
      );
    };
const handleDealTag = (command: string) => {
  command === "other" ? handleCloseOther() : handleCloseAll()
}

routeListToTagList(route);

onBeforeRouteUpdate((to) => {
  routeListToTagList(to)
})

router.beforeEach((to, from, next) => {
  // @ts-ignore
  document.title = `${store.state.label[to.meta.nameKey] || to.meta.title}`
  next()
})
</script>
<style scoped lang="scss">
.tag {
  overflow-y: hidden;
  background: #fff;
  z-index: 888;
  // box-shadow: 0 5px 10px #ddd;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .tag-left {
    .li {
      font-size: 12px;
      cursor: pointer;
      transition: all 0.3s ease-in;

      &.el-link--primary {
        color: #333;
      }
    }
  }

  .el-link.el-link--primary.is-underline:hover:after {
    display: none;
  }

  .el-link.is-underline:hover:after {
    display: none;
  }
}
</style>
