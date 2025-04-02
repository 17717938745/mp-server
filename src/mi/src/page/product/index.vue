<template>
  <view class="content">
    <web-view :src="url"/>
  </view>
</template>

<script setup lang="ts">
import {ref} from "vue"
import {fullUrl} from "@util/EnvUtil"

const url = ref(fullUrl('/industry/product', ''))

console.log(`url: ${url.value}`)
</script>

<style lang="scss">

.content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

</style>
