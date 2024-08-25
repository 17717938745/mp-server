<template>
  <Transition name="up">
    <div class="toasts" v-show="visible">
      <span>{{ text }}</span>
<!--      <span class="close" @click="handleHide">x</span>-->
    </div>
  </Transition>
</template>
<script lang="ts" setup>
import {ref, onMounted} from "vue";

const props = defineProps({
  text: {
    type: String,
    default: ''
  }
})
const visible = ref(false)
onMounted(() => {
  visible.value = true
})
const handleHide = () => {
  visible.value = false
}
</script>
<style scoped lang="scss">
.toasts {
  max-width: 95vw;
  min-width: 80px;
  min-height: 36px;
  position: fixed;
  z-index: 99999999;
  left: 50%;
  bottom: 46%;
  border: 1px solid #e4e4e4;
  background: rgba(17, 17, 17, 0.7);
  box-shadow: 0px 10px 50px 0px;
  color: #fff;
  border-radius: 18px;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  transform: translateX(-50%) translateY(-50%);
  padding: 8px;
  line-height: 20px;
  white-space: pre;

  .close {
    position: absolute;
    right: 5px;
    top: 5px;
    font-size: 30px;

    &:hover {
      cursor: pointer;
    }
  }
}

.up-enter-from {
  bottom: 0;
  opacity: 0;
}

.up-enter-active {
  transition: all 0.5s;
}

.up-enter-to {
  opacity: 1;
  bottom: 46%;
}
</style>
