<template>
  <view>
    <view>
      <view>
        <view>total: {{ researchTotal }}</view>
        <view
          >用户: {{ account.viewPdf ? "可以" : "不可以" }}阅读pdf
          <view> </view>
        </view>
        <view>用户: {{ account.viewDigest ? "可以" : "不可以" }}阅读摘要</view>
        <view>list:</view>
        <view>
          <view v-for="(t, i) in researchList">
            {{ t.title }}
            <button @click="handleDetail(t.id)">明细</button>
          </view>
        </view>
        <button @click="handleGotoLogin">跳转登录</button>
      </view>
    </view>
    <view>
      <uni-grid
        v-if="researchType != null"
        :column="researchType.researchTypeList.length"
        :highlight="true"
        @change="handleChange"
      >
        <uni-grid-item
          v-for="(item, index) in researchType.researchTypeList"
          :index="index"
          :key="index"
        >
          <text>{{ item.label }}</text>
          <uni-list v-if="item.children">
            <uni-list-item
              v-for="(t, i) in item.children"
              :title="t.label"
              clickable
              @click="handleSearchPage(t.value)"
              :key="i"
            />
          </uni-list>
        </uni-grid-item>
      </uni-grid>
    </view>
  </view>
</template>

<script lang="ts" setup>
interface ResearchTypeItem {
  value: string;
  label: string;
  children: ResearchTypeItem[];
}

interface ResearchType {
  researchTypeList: ResearchTypeItem[];
  defaultResearchType: string;
}

import { httpGet, httpPutJson } from "@util/HttpUtil";
import { DataResult, PageDataResult, PageResult } from "../../src/typing/ma/System";
import { ref } from "vue";
import uniCard from "@dcloudio/uni-ui/lib/uni-card/uni-card.vue";
import uniGrid from "@dcloudio/uni-ui/lib/uni-grid/uni-grid.vue";
import uniGridItem from "@dcloudio/uni-ui/lib/uni-grid-item/uni-grid-item.vue";
import uniList from "@dcloudio/uni-ui/lib/uni-list/uni-list.vue";
import uniListItem from "@dcloudio/uni-ui/lib/uni-list-item/uni-list-item.vue";

const researchType = ref<ResearchType>(null);

const pageParam = ref({
  page: {
    page: 1,
    limit: 5,
  },
  data: {
    researchType: "",
  },
});
const researchTotal = ref(0);
const researchList = ref([]);
const account = ref({});
const handleSearchPage = (researchType: string) => {
  pageParam.value.data.researchType = researchType;
  httpGet(`provider/mini/research/page`, pageParam.value).then(
    (res: PageDataResult<any, any>) => {
      researchTotal.value = res.total;
      researchList.value = res.list;
      account.value = res.data;
    }
  );
};
const handleGetType = () => {
  httpGet(`provider/mini/research/type`, {}).then((res: DataResult<ResearchType>) => {
    researchType.value = res.data;
  });
};
handleGetType();

const handleChange = (e) => {
  let { index } = e.detail;
  uni.showToast({
    title: `查询`,
    icon: "none",
  });
};
const handleDetail = (id: string) => {
  if (account.value.viewPdf) {
    uni.navigateTo({
      url: "pdf?id=" + id,
    });
  } else if (account.value.viewDigest) {
    uni.navigateTo({
      url: "digest?id=" + id,
    });
  } else {
    uni.showToast({
      title: `您没有查询权限`,
      icon: "none",
    });
  }
};
const handleGotoLogin = () => {
  uni.navigateTo({
    url: "login",
  });
};
</script>

<style></style>
