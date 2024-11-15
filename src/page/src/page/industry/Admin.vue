<template>
  <div class="main">
    <div class="content-box">
      <Industry-sidebar ref="sidebarRef" :sidebar-tree-list="sidebarTreeList"/>
      <div class="content">
        <Industry-header/>
        <Industry-tag-list></Industry-tag-list>
        <!-- <Industry-bread-crumb/>-->
        <router-view v-slot="{ Component }">
          <transition name="move" mode="out-in">
            <keep-alive :include="tagList">
              <div class="box-border">
                <component
                    class="my_container p-x-24 p-y-16 box-border"
                    :is="Component"
                />
              </div>
            </keep-alive>
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>
<script lang="tsx">
import {computed, onMounted, reactive, ref, toRefs} from "vue";
import {Store, useStore} from "vuex";
import {useRouter} from "vue-router";
import IndustryHeader from "../../component/industry/IndustryHeader.vue";
import IndustrySidebar from "../../component/industry/IndustrySidebar.vue";
import IndustryTagList from "../../component/industry/IndustryTagList.vue";
import IndustryBreadCrumb from "../../component/industry/IndustryBreadCrumb.vue";
import {menuTreeListToSidebarTreeList} from "@/util/RouterUtil";
import {StoreType} from "@/store";

const middleDeviceWidth: number = 1792;
let previousBodyWidth: number = window.innerWidth;
// noinspection JSUnusedGlobalSymbols
export default {
  name: "admin",
  components: {
    IndustryHeader,
    IndustrySidebar,
    IndustryTagList,
    IndustryBreadCrumb,
  },
  setup() {
    const router = useRouter();
    const sidebarRef = ref(null);
    const store: Store<StoreType> = useStore<StoreType>();
    const storeState: StoreType = store.state;
    const state = reactive({
      contentWidth: 0,
      sidebarTreeList: menuTreeListToSidebarTreeList(storeState.menuTreeList, [], 2),
      tagList: storeState.tagList.map((item: any) => item.name),
    });
    const collapse = computed(() => storeState.collapse);

    router.beforeEach((to, from, next) => {
      // console.log(to.path);
      const pathList: string[] = to.path.split("/").filter((temp) => temp);
      store.commit("setPathList", pathList);
      next();
    });

    onMounted(() => {
      // console.log(`sidebarRef.value.handleGetWidth(): ${sidebarRef.value.handleGetWidth()}`)
      // console.log(`sidebarRef.value.handleGetDom(): ${sidebarRef.value.handleGetDom()}`)
      if (document.body.offsetWidth <= middleDeviceWidth) {
        if (!storeState.collapse) {
          store.commit("setCollapse", true);
        }
      }
      new ResizeObserver((entries) => {
        entries.forEach((entry) => {
          if (previousBodyWidth != entry.contentRect.width) {
            if (window.innerWidth <= middleDeviceWidth) {
              if (!storeState.collapse) {
                store.commit("setCollapse", true);
              }
            } else {
              if (storeState.collapse) {
                store.commit("setCollapse", false);
              }
              previousBodyWidth = entry.contentRect.width;
            }
          }
        });
      }).observe(document.body);
    });
    return {
      ...toRefs(state),
      collapse,
      sidebarRef,
    };
  },
};
</script>

<style lang="scss">

$containerGap: 20px;
$containerTopGap: 5px;
$containerLeftGap: 5px;
$contentPadding: 5px;
$headerHeight: 50px;
$tagHeight: 30px;
$crumbHeight: 30px;
$collapseBtnWidth: 60px;
$mobileWidth: 768px;
$middleWidth: 1024px;
$largeMiddleWidth: 1536px;
$largeWidth: 2048px;
$avatarLength: 24px;

::-webkit-scrollbar-track-piece {
  background-color: #324157;
  -webkit-border-radius: 6px;
}

::-webkit-scrollbar-thumb:vertical {
  background-color: #9ca3ad;
  -webkit-border-radius: 6px;
}

::-webkit-scrollbar-thumb:horizontal {
  width: 5px;
  background-color: #9ca3ad;
  -webkit-border-radius: 6px;
}

.main {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  align-content: flex-start;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background: rgba(245, 246, 247, 1);

  .header {
    width: 100%;
    margin: 0;
    overflow-y: hidden;
    border: none;
    height: $headerHeight;
    font-size: 22px;
    color: #fff;
    display: flex;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: center;
    background-color: #fff;
    border-bottom: 1px solid #eee;

    .header-left {
      display: flex;
      align-items: center;

      .collapse-btn {
        display: flex;
        justify-content: center;
        align-items: center;
        text-align: center;
        cursor: pointer;
        width: $collapseBtnWidth;
        height: $headerHeight;

        &:hover {
          background-color: rgba(184, 203, 172, 0.6);
        }

        @media screen and (max-width: $mobileWidth) {
          visibility: hidden;
        }
      }

      .logo {
      }
    }

    .header-right {
      margin-right: $containerGap;
      display: flex;
      justify-content: flex-end;
      align-items: center;

      .user-avatar {
        margin-right: $containerGap;

        img {
          display: block;
          width: $avatarLength;
          height: $avatarLength;
          border-radius: 50%;
        }
      }

      .el-dropdown-link {
        color: #fff;
        cursor: pointer;
      }
    }
  }

  .content-box {
    transition: left 0.3s ease-in-out;
    background-color: rgba(245, 246, 247, 1);
    //background-color: #00ff80;
    width: 100vw;
    height: calc(100vh - #{$headerHeight});
    overflow-x: hidden;
    overflow-y: hidden;
    display: flex;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: flex-start;
    height: 100vh;

    ::-webkit-scrollbar {
      width: 12px;
      height: 12px;
    }

    .sidebar {
      height: 100%;
      overflow-x: hidden;
      overflow-y: auto;

      @media screen and (max-width: $middleWidth) {
        //@media screen and (max-width: $largeWidth) {
        position: fixed;
        z-index: 9999;
        top: 0;
        left: 0;
        min-width: $collapseBtnWidth;
        //height: $headerHeight;
        width: $collapseBtnWidth;
        border-right: 1px solid #ffffff;
        .el-sub-menu__title {
          display: block;
          text-align: center;
          margin-right: 400px;
        }
      }
    }

    .content {
      display: block;
      background: rgba(245, 246, 247, 1);
      width: 100%;
      // height: calc(100% - #{$headerHeight} + #{$headerHeight});
      overflow: hidden;

      @media screen and (max-width: $middleWidth) {
        margin-left: $collapseBtnWidth;
      }

      .crumb {
        height: $crumbHeight;
        overflow-y: hidden;
        padding: $containerTopGap $containerLeftGap;
        display: flex;
        align-items: center;

        .link {
          font-size: 13px;
        }
      }

      .my_container {
        box-sizing: border-box;
        background-color: #fff;
        width: 100%;
        height: calc(100vh - #{$headerHeight} - 51px - 64px);
        overflow-x: hidden;
        overflow-y: scroll;

        .color-gray {
          background-color: #dcdfe6 !important;
        }

        .mt20 {
          margin-top: 20px;
        }

        .mgb20 {
          margin-bottom: 20px;
        }

        .mr10 {
          margin-right: 10px;
        }

        .ml10 {
          left: 10px;
        }

        .search-section {
          margin-bottom: 20px;
        }
      }
    }
  }
}

.move-enter-active,
.move-leave-active {
  transition: opacity 0.1s ease;
}

.move-enter-from,
.move-leave-to {
  opacity: 0;
}
</style>
