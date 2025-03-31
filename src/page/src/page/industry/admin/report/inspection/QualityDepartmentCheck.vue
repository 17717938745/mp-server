<template>
  <div>
   <forum-component :title-label-key="'qualityDepartmentCheck'"/>
  </div>
</template>

<script lang="tsx" setup>
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Index'
import {useRouter} from 'vue-router'
import ForumComponent from '../../../component/ForumComponent.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList

</script>

<style scoped lang="scss">
</style>
