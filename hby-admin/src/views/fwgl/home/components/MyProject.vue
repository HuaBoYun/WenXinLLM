<template>
  <el-card shadow="hover">
    <template #header>
      <vab-icon icon="github-line" />
      我的开源项目
      <el-tag class="card-header-tag">爱我别走</el-tag>
    </template>
    <el-row :gutter="20">
      <el-col
        v-for="(item, index) in list"
        :key="index"
        :lg="12"
        :md="12"
        :sm="24"
        :xl="12"
        :xs="24"
      >
        <div
          class="project-card"
          :style="'background-image:url(' + item.image + ')'"
          @click="handleOpenWindow(item.url)"
        >
          <div class="project-card-title">{{ item.title }}</div>
          <div class="project-card-description">{{ item.description }}</div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
  export default {
    data() {
      return {
        list: [],
      }
    },
    methods: {
      handleOpenWindow(url) {
        window.open(url)
      },
    },
  }
</script>

<style lang="scss" scoped>
  ::v-deep {
    .el-card__body {
      padding-bottom: 0 !important;
    }
  }

  .project-card {
    position: relative;
    width: 100%;
    height: 115px;
    padding: 15px;
    margin-bottom: $base-margin;
    cursor: pointer;
    background-size: 100% 100%;
    border-radius: $base-border-radius + 2px;

    &-title,
    &-description {
      margin-right: 45px;
      font-size: 20px;
      font-weight: bold;
      color: #fff;
    }

    &-description {
      font-size: 12px;
      font-weight: normal;
      line-height: 20px;
    }
  }
</style>
