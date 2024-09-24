<template>
  <div
      class="lead-h5"
  >
    <div class="lead-header whitespace-break-spaces">
      <h1 class="title whitespace-break-spaces">{{ title }}</h1>
      <div class="tips">{{ date }}</div>
    </div>
    <div v-html="html" class="w-full whitespace-break-spaces"></div>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, toRefs} from 'vue'

interface PropType {
  title: string;
  date: string;
  html: string;
}

const props = withDefaults(defineProps<PropType>(), {})

const state = reactive({
  title: props.title,
  date: props.date,
  html: props.html,
})
const {
  title,
  date,
  html,
} =  {
  ...toRefs(state),
}
</script>

<style lang="scss">

.noScrollBar {
  white-space: nowrap;

  &::-webkit-scrollbar {
    width: 0 !important;
    display: none;
  }

  -ms-overflow-style: none;
}
</style>
