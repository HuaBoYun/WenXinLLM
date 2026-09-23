<template>
  <div class="link-container">
    <el-card shadow="hover">
      <template #header>
        <span>基础用法</span>
      </template>
      <el-link href="https://element.eleme.io" target="_blank">
        默认链接
      </el-link>
      <el-link type="primary">主要链接</el-link>
      <el-link type="success">成功链接</el-link>
      <el-link type="warning">警告链接</el-link>
      <el-link type="danger">危险链接</el-link>
      <el-link type="info">信息链接</el-link>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>禁用状态</span>
      </template>
      <el-link disabled>默认链接</el-link>
      <el-link disabled type="primary">主要链接</el-link>
      <el-link disabled type="success">成功链接</el-link>
      <el-link disabled type="warning">警告链接</el-link>
      <el-link disabled type="danger">危险链接</el-link>
      <el-link disabled type="info">信息链接</el-link>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>下划线</span>
      </template>
      <el-link :underline="false">无下划线</el-link>
      <el-link>有下划线</el-link>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>图标</span>
      </template>
      <el-link icon="el-icon-edit">编辑</el-link>
      <el-link>
        查看
        <i class="el-icon-view el-icon--right" />
      </el-link>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'Link',
  }
</script>

<style lang="scss" scoped>
  .link-container {
    padding: 0 !important;
    background: $base-color-background !important;

    ::v-deep {
      .el-link {
        margin-bottom: 10px;

        &:first-child {
          margin-right: 10px;
        }

        & + .el-link {
          margin-right: 10px;
          margin-left: 0;
        }
      }
    }
  }
</style>
