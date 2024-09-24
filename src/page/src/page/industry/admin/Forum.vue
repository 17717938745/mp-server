<template>
  <div>
    <tinymce :forum-id="route.query.forumId"></tinymce>
  </div>
</template>

<script lang="ts" setup>
import {useRoute, useRouter,} from 'vue-router'
import Tinymce from '../component/Tinymce.vue'

const router = useRouter()
const route = useRoute()

</script>

<style scoped>

</style>
