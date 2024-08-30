<!--
 * @Author: your name
 * @Date: 2024-03-05 15:43:17
 * @LastEditTime: 2024-07-25 17:23:20
 * @LastEditors: liushengzhuang shengzhuang.liu@leadbank.com.cn
 * @Description: In User Settings Edit
 * @FilePath: \dmmp_page\src\component\lead\LeadDialog.vue
-->
<!--  -->
<template>
  <div>
    <!-- 弹窗提示 -->
    <van-dialog v-model:show="show" :show-confirm-button="false">
      <div class="pt-[25px] relative text-[#19191E]">
        <div class="absolute top-0 right-0 p-[15px]">
          <img
            :src="fullUrl('/third/img/dialogClose.svg', '')"
            alt=""
            class="w-[20px] h-[20px]"
            @click="handleHideDialog"
            v-if="showDialogClose"
          />
        </div>
        <p class="fz18 fw5 textcenter" v-if="title">{{ title }}</p>
        <p
          :class="[
            'fz15 mt20 leading-[25px]  px-[20px] ',
            hasLineBreak ? 'text-left' : 'text-center',
          ]"
          ref="messageContainer"
        >
          {{ message }}
        </p>
        <div class="flexBox mt-[25px] bt_0_5 text-[15px] box-border">
          <div
            class="flex-1 text-center py-[17px] border-r-[0.5px] border-[#DCDCDC]"
            @click="handleCancel"
            v-if="showCancelButton"
          >
            <span :class="`text-[${cancelButtonColor}]`"> {{ cancelButtonText }}</span>
          </div>
          <div
            v-if="showConfirmButton"
            :class="`flex-1 text-center py-[17px] text-[#DC2828] ${
              showCancelButton ? 'bl_0_5' : ''
            }`"
            @click="handConfirm"
          >
            <span :class="`text-[${confirmButtonColor}]`"> {{ confirmButtonText }}</span>
          </div>
        </div>
      </div>
    </van-dialog>
  </div>
</template>

<script lang="ts" setup>
  import { computed, nextTick, onMounted, ref, watch, watchEffect } from 'vue';
  import {fullUrl} from '@/util/EnvUtil'

  interface IProps {
    show: boolean;
    title?: string;
    width?: string;
    theme?: null;
    message?: string;
    overlay?: boolean;
    callback?: null;
    teleport?: string;
    className?: string;
    allowHtml?: boolean;
    lockScroll?: boolean;
    transition?: undefined;
    beforeClose?: null;
    overlayClass?: string;
    overlayStyle?: undefined;
    messageAlign?: string;
    cancelButtonText?: string;
    cancelButtonColor?: string;
    cancelButtonDisabled?: boolean;
    confirmButtonText?: string;
    confirmButtonColor?: string;
    confirmButtonDisabled?: boolean;
    showConfirmButton?: boolean;
    showCancelButton?: boolean;
    closeOnPopstate?: boolean;
    closeOnClickOverlay?: boolean;
    showDialogClose?: boolean;
  }
  const emit = defineEmits(['close', 'confirm', 'update:show']);
  const props = withDefaults(defineProps<IProps>(), {
    show: false,
    title: '提示',
    cancelButtonColor: '#19191E',
    confirmButtonColor: '#DC2828',
    confirmButtonText: '取消',
    cancelButtonTextz: '确定',
    showConfirmButton: true,
    showCancelButton: false,
    showDialogClose: false,
  });
  console.log('props', props);
  const handleCancel = () => {
    emit('close');
  };
  const handConfirm = () => {
    emit('confirm');
  };
  const handleHideDialog = () => {
    emit('update:show', false);
  };
  const messageContainer = ref(null);
  const hasLineBreak = ref(false);
  const LINE_HEIGHT = 25;

  watchEffect(() => {
    if (props.show && messageContainer.value) {
      // console.log("执行", messageContainer.value.clientHeight, LINE_HEIGHT);
      hasLineBreak.value = messageContainer.value.clientHeight > LINE_HEIGHT;
    }
  });
</script>
<style lang="scss" scoped></style>
