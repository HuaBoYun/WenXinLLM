<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
  export default {
    name: 'App',
  }
</script>
<style scoped lang="scss">
  ::v-deep .el-form.disabled {
    input,
    textarea {
      border: 0;
      background-color: #ffffff;
      resize: none;
    }
    .el-input__suffix {
      display: none;
    }
  }
  ::v-deep .el-card__body {
    .right-panel {
      float: right;
    }
  }
</style>
<style>
  .el-tooltip__popper {
    max-width: 700px;
  }
  .el-button--text {
    white-space: normal !important;
  }
  /* Element UI 全局禁用状态样式 */
  .el-input.is-disabled .el-input__inner,
  .el-textarea.is-disabled .el-textarea__inner,
  .el-select.is-disabled .el-input__inner,
  .el-checkbox.is-disabled,
  .el-radio.is-disabled,
  .el-switch.is-disabled,
  .el-slider.is-disabled,
  .el-date-picker.is-disabled,
  .el-time-picker.is-disabled,
  .el-range-editor.is-disabled,
  .el-radio-group.is-disabled,
  .el-range-editor.is-disabled input,
  .el-radio__input.is-disabled + span.el-radio__label,
  .vab-theme-red .el-radio__input.is-disabled.is-checked .el-radio__inner {
    background-color: #fff !important;
    color: #666 !important;
    cursor: not-allowed;
  }

  .el-dropdown-menu__item.is-disabled {
    color: #666 !important;
    cursor: not-allowed;
  }
</style>
