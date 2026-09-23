<template>
  <el-card class="version-information" shadow="hover">
    <template #header>
      <vab-icon icon="information-line" />
      信息
      <el-tag class="card-header-tag">部署时间:{{ updateTime }}</el-tag>
    </template>
    <el-scrollbar>
      <table class="table">
        <tr>
          <td>vue</td>
          <td>{{ dependencies['vue'] }}</td>
          <td>@vue/cli</td>
          <td>{{ devDependencies['@vue/cli-service'] }}</td>
        </tr>
        <tr>
          <td>vuex</td>
          <td>{{ dependencies['vuex'] }}</td>
          <td>vue-router</td>
          <td>{{ dependencies['vue-router'] }}</td>
        </tr>
        <tr>
          <td>element-ui</td>
          <td>{{ dependencies['element-ui'] }}</td>
          <td>axios</td>
          <td>{{ dependencies['axios'] }}</td>
        </tr>
      </table>
    </el-scrollbar>
  </el-card>
</template>

<script>
  import { dependencies, devDependencies } from '~/package.json'

  export default {
    data() {
      return {
        updateTime: process.env.VUE_APP_UPDATE_TIME,
        dependencies,
        devDependencies,
      }
    },
  }
</script>

<style lang="scss" scoped>
  .version-information {
    .table {
      width: 100%;
      color: #666;
      border-collapse: collapse;
      background-color: #fff;

      td {
        position: relative;
        padding: 9px 15px;
        overflow: hidden;
        font-size: 14px;
        line-height: 20px;
        text-overflow: ellipsis;
        white-space: nowrap;
        border: 1px solid #e6e6e6;

        &:nth-child(odd) {
          width: 20%;
          text-align: right;
          background-color: #f7f7f7;
        }
      }
    }
  }
</style>
