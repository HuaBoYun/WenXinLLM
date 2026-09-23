<template>
  <div class="icon-selector-container">
    <el-popover popper-class="icon-selector-popper" trigger="hover" width="292">
      <template #reference>
        <el-button>
          <vab-icon :icon="icon" />
          图标选择器
          <vab-icon icon="arrow-down-s-line" />
        </el-button>
      </template>
      <vab-icon-selector @handle-icon="handleIcon" />
    </el-popover>
  </div>
</template>

<script>
  import VabIconSelector from '@/extra/VabIconSelector'

  export default {
    name: 'IconSelector',
    components: { VabIconSelector },
    data() {
      return {
        icon: '24-hours-fill',
      }
    },
    methods: {
      handleIcon(item) {
        this.icon = item
      },
    },
  }
</script>
