<template>
  <router-view/>
</template>

<script lang="tsx" setup>

</script>

<style lang="scss">
html,
body {
  margin: 0;
  padding: 0;
  height: 100vh;

  #app {
    margin: 0;
    padding: 0;
    height: 100vh;
  }
}

:deep(.el-button.is-link) {
  font-size: 14px;
}

.el-table--border,
.el-table--group,
.el-table td.el-table__cell,
.el-table th.el-table__cell.is-leaf,
.el-table__footer-wrapper td.el-table__cell {
  border: #999999 1px solid !important;
}

$containerGap: 20px;
$containerTopGap: 5px;
$containerLeftGap: 5px;
.query-container {
  margin-bottom: $containerGap;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  justify-items: center;
  flex-wrap: wrap;

  & > .el-range-editor, .el-date-editor {
    max-width: 400px;
    margin: $containerLeftGap $containerGap $containerLeftGap 0 !important;
  }

  & > .el-input,
  .el-select {
    max-width: 200px;
    margin: $containerLeftGap $containerGap $containerLeftGap 0;
  }

  .query-btn {
    display: inline-flex;
    justify-content: flex-end;
    align-items: center;
    justify-items: center;
    flex-wrap: nowrap;
    //border: 1px solid #00ffff;
    flex-grow: 2;
    margin-right: $containerGap;
  }
}

.resources-upload {
  display: flex;
  flex-direction: column;

  .resources-view {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .close-btn {
      margin-top: 5px;

      &:hover {
        background-color: #cccccc;
      }
    }
  }
}

.image-view {
  min-width: 200px;
  width: 50%;
}

.el-table {
  .row-blue {
    background-color: #97adda;
  }
  .row-light-blue {
    background-color: #409eff;
  }
  .row-red {
    background-color: #f1f15b !important;
    color: #ff0000 !important;
    font-weight: 900;
    font-size: 16px;
  }
  .cell-red {
    background-color: #f1f15b !important;
    color: #ff0000 !important;
    font-weight: 900;
    font-size: 16px;
  }
  .row-green {
    background-color: #9bc567 !important;
    //color: #ca3434 !important;
    font-weight: 900;
    font-size: 16px;
  }

  .row-done {
    background-color: #e4e7ed;
  }

  .row-error {
    background-color: #f1f15b;
  }
}

.el-tabs__item {
  font-size: 16px;
  font-weight: 400;
  letter-spacing: 0px;
  line-height: 48px;
  height: 48px;
  padding: 0 16px;
  color: rgba(102, 102, 102, 1);
}

.el-tabs__nav-wrap::after {
  background-color: rgba(232, 232, 232, 1);
}

.el-button {
  padding: 0 32px;
  line-height: 20px;
  box-sizing: border-box;
  height: 36px;

  &.is-link {
    padding: 0 0 0 10px;
  }

  &.is-text {
    padding: 0 0 0 20px;
  }
}

p, ul {
  line-height: 1;
  margin: 0;
  padding: 0;
}

body {
  --el-input-placeholder-color: rgba(196, 196, 196, 1);
  --el-text-size-placeholder: 14px;
  --el-border-width: 1px;
  --el-border-color: rgba(232, 232, 232, 1);
  --el-input-height: 36px;
  --el-border-radius-base: 4px;
  --el-input-text-color: rgba(51, 51, 51, 1);
  --el-font-size-base: 14px;
  --el-border-radius-base: 4px;
  --el-input-inner-height: 36px;
  --el-component-size: 36px;
  --el-menu-item-height: 40px;
  --el-menu-sub-item-height: 40px;
  --el-radio-input-width: 16px;
  --el-radio-input-height: 16px;
}

.el-link.el-link--info.is-underline:hover:after {
  border: none;
}

.el-table--small .el-table__cell {
  font-size: 14px;
}

.el-tabs__nav-wrap::after {
  height: 1px;
}

.el-step__title {
  font-size: 14px;

  &.is-finish {

    color: #333;
  }
}

.el-radio__inner {
  width: 16px;
  height: 16px;
}

.el-upload-list.is-disabled .el-upload-list__item-status-label {
  display: none;
}

.el-upload-list--picture-card .el-upload-list__item {
  width: 120px;
  height: 90px;
  opacity: 1;
  border-radius: 4px;
  border: 1px solid rgba(232, 232, 232, 1);
  padding: 10px;

}

.el-popover__title {
  font-size: 14px;
  line-height: 20px;
}

// el-image在el-table中穿透
.el-table__cell {
  position: static !important;
}

@media print {
  title, .el-drawer__close-btn .print-hide {
    display: none;
  }
  body {
    height: auto;
  }
}

.cursor-pointer {
  cursor: pointer;
}

.el-drawer__close-btn {
  z-index: 99999;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  color: #909399;
  font-size: 14px;
}


.description-print-border {
  border: 2px #999999 solid !important;
}

.high-light {
  background-color: #cf4d24;
  color: #eeeeee;
  font-weight: 600;
}

::-webkit-scrollbar-track-piece {
  background-color: #324157;
  -webkit-border-radius: 9px;
}

::-webkit-scrollbar-thumb:vertical {
  background-color: #9ca3ad;
  -webkit-border-radius: 9px;
}

::-webkit-scrollbar-thumb:horizontal {
  width: 8px;
  background-color: #9ca3ad;
  -webkit-border-radius: 9px;
}

::-webkit-scrollbar {
  width: 9px !important;
  height: 9px !important;
}

.el-scrollbar__bar.is-horizontal {
  height: 9px !important;
}

.el-scrollbar__bar.is-horizontal .el-scrollbar__thumb {
  height: 9px !important;
}

.el-scrollbar__bar.is-vertical .el-scrollbar__thumb {
  width: 9px !important;
}
</style>
