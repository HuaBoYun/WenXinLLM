<template>
  <div class="button-container">
    <el-card shadow="hover">
      <template #header><span>基础用法</span></template>
      <el-button>默认按钮</el-button>
      <el-button type="primary">主要按钮</el-button>
      <el-button type="success">成功按钮</el-button>
      <el-button type="info">信息按钮</el-button>
      <el-button type="warning">警告按钮</el-button>
      <el-button type="danger">危险按钮</el-button>
      <el-button plain>朴素按钮</el-button>
      <el-button plain type="primary">主要按钮</el-button>
      <el-button plain type="success">成功按钮</el-button>
      <el-button plain type="info">信息按钮</el-button>
      <el-button plain type="warning">警告按钮</el-button>
      <el-button plain type="danger">危险按钮</el-button>
      <el-button round>圆角按钮</el-button>
      <el-button round type="primary">主要按钮</el-button>
      <el-button round type="success">成功按钮</el-button>
      <el-button round type="info">信息按钮</el-button>
      <el-button round type="warning">警告按钮</el-button>
      <el-button round type="danger">危险按钮</el-button>
      <el-button circle icon="el-icon-search" />
      <el-button circle icon="el-icon-edit" type="primary" />
      <el-button circle icon="el-icon-check" type="success" />
      <el-button circle icon="el-icon-message" type="info" />
      <el-button circle icon="el-icon-star-off" type="warning" />
      <el-button circle icon="el-icon-delete" type="danger" />
    </el-card>

    <el-card shadow="hover">
      <template #header>
        <span>禁用状态</span>
      </template>
      <el-button disabled>默认按钮</el-button>
      <el-button disabled type="primary">主要按钮</el-button>
      <el-button disabled type="success">成功按钮</el-button>
      <el-button disabled type="info">信息按钮</el-button>
      <el-button disabled type="warning">警告按钮</el-button>
      <el-button disabled type="danger">危险按钮</el-button>
      <el-button disabled plain>朴素按钮</el-button>
      <el-button disabled plain type="primary">主要按钮</el-button>
      <el-button disabled plain type="success">成功按钮</el-button>
      <el-button disabled plain type="info">信息按钮</el-button>
      <el-button disabled plain type="warning">警告按钮</el-button>
      <el-button disabled plain type="danger">危险按钮</el-button>
    </el-card>

    <el-card shadow="hover">
      <template #header>
        <span>文字按钮</span>
      </template>
      <el-button type="text">文字按钮</el-button>
      <el-button disabled type="text">文字按钮</el-button>
    </el-card>

    <el-card shadow="hover">
      <template #header>
        <span>elemenr内置图标按钮</span>
      </template>
      <el-button icon="el-icon-edit" type="primary" />
      <el-button icon="el-icon-share" type="primary" />
      <el-button icon="el-icon-delete" type="primary" />
      <el-button icon="el-icon-search" type="primary">搜索</el-button>
      <el-button type="primary">
        上传
        <i class="el-icon-upload el-icon--right" />
      </el-button>
    </el-card>

    <el-card shadow="hover">
      <template #header>
        <span>自定义图标按钮</span>
      </template>
      <el-button type="primary">
        <vab-icon icon="24-hours-line" />
      </el-button>
      <el-button type="primary">
        <vab-icon icon="4k-line" />
      </el-button>
      <el-button type="primary">
        <vab-icon icon="a-b" />
      </el-button>
      <el-button type="primary">
        <vab-icon icon="account-box-line" />
        用户名
      </el-button>
    </el-card>

    <el-card shadow="hover">
      <template #header>
        <span>按钮组</span>
      </template>
      <el-button-group style="margin-right: 10px">
        <el-button icon="el-icon-arrow-left" type="primary">上一页</el-button>
        <el-button type="primary">
          下一页
          <i class="el-icon-arrow-right el-icon--right" />
        </el-button>
      </el-button-group>
      <el-button-group>
        <el-button icon="el-icon-edit" type="primary" />
        <el-button icon="el-icon-share" type="primary" />
        <el-button icon="el-icon-delete" type="primary" />
      </el-button-group>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>加载中</span>
      </template>
      <el-button :loading="true" type="primary">加载中</el-button>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>不同尺寸</span>
      </template>
      <el-button>默认按钮</el-button>
      <el-button size="medium">中等按钮</el-button>
      <el-button size="small">小型按钮</el-button>
      <el-button size="mini">超小按钮</el-button>
      <el-button round>默认按钮</el-button>
      <el-button round size="medium">中等按钮</el-button>
      <el-button round size="small">小型按钮</el-button>
      <el-button round size="mini">超小按钮</el-button>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'Button',
  }
</script>

<style lang="scss" scoped>
  .button-container {
    padding: 0 !important;
    background: $base-color-background !important;

    ::v-deep {
      .el-button {
        &:first-child {
          margin-right: 10px;
        }

        & + .el-button {
          margin-right: 10px;
          margin-bottom: 10px;
          margin-left: 0;
        }
      }
    }
  }
</style>
