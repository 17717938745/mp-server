<template>
  <div class="lead-h5">
    <div class="lead-header">
      <h1 class="title">{{ title }}</h1>
      <div class="tips">{{ date }}</div>
    </div>
    <div v-html="html"></div>
  </div>
</template>

<script lang="tsx">
import {reactive, toRefs} from 'vue'
import Editor from '@tinymce/tinymce-vue'

interface Prop {
  title: string
  date: string
  html: string
}

export default {
  name: 'lead-h5-view',
  props: {
    title: {
      required: true
    },
    date: {
      required: true
    },
    html: {
      required: true
    }
  },
  components: {
    Editor
  },
  setup(props: Prop) {
    const state = reactive({
      title: props.title,
      date: props.date,
      html: props.html,
    })
    return {
      ...toRefs(state),
    }
  }
}
</script>

<style lang="scss">

</style>
