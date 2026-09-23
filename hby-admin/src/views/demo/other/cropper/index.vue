<template>
  <div class="cropper-container">
    <el-button type="primary" @click="openDialog">点击打开头像裁剪</el-button>
    <vab-cropper ref="vabCropper" />
  </div>
</template>

<script>
  import VabCropper from '@/extra/VabCropper'

  export default {
    name: 'Cropper',
    components: { VabCropper },
    methods: {
      openDialog() {
        this.$refs['vabCropper'].dialogVisible = true
      },
    },
  }
</script>
