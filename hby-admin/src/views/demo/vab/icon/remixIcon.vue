<template>
  <div class="remix-icon-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-form :inline="true" label-width="80px" @submit.native.prevent>
          <el-form-item label="图标名称">
            <el-input v-model="queryForm.title" />
          </el-form-item>
          <el-form-item label-width="0">
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="queryData"
            >
              查询
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col
        v-for="(item, index) in queryIcon"
        :key="index"
        :lg="2"
        :md="3"
        :sm="8"
        :xl="2"
        :xs="6"
      >
        <el-card shadow="hover" @click.native="handleCopyIcon(item, $event)">
          <vab-icon :icon="item" />
        </el-card>
        <div class="icon-text" @click="handleCopyText(item, $event)">
          {{ item }}
        </div>
      </el-col>

      <el-col :span="24">
        <el-pagination
          background
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :page-sizes="[72, 144, 216, 288]"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { getIconList } from '@/api/remixIcon'
  import clip from '@/utils/clipboard'

  export default {
    name: 'RemixIcon',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryIcon: [],
        queryForm: {
          pageNo: 1,
          pageSize: 72,
          title: '',
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        const {
          data: { list, total },
        } = await getIconList(this.queryForm)
        this.queryIcon = list
        this.total = total
      },
      handleCopyText(item, event) {
        clip(item, event)
      },
      handleCopyIcon(item, event) {
        clip(`<vab-icon icon="${item}" />`, event)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .remix-icon-container {
    ::v-deep {
      .el-card__body {
        position: relative;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        min-height: 60px;
        max-height: 60px;
        padding: $base-padding/1.4;
        cursor: pointer;

        i {
          font-size: 28px;
          color: $base-color-grey;
          text-align: center;
          pointer-events: none;
          cursor: pointer;
          transition: $base-transition;
        }

        &::after {
          position: absolute;
          bottom: -30px;
          width: 100%;
          padding: 4px 0;
          font-size: $base-font-size-small;
          color: rgb(255, 255, 255);
          text-align: center;
          content: '点击复制';
          background-color: $base-color-blue;
          transition: $base-transition;
        }

        &:hover {
          i {
            margin-top: -($base-margin / 1.2);
          }

          &::after {
            bottom: 0;
          }
        }
      }
    }

    .icon-text {
      height: 30px;
      margin-top: -15px;
      overflow: hidden;
      font-size: 12px;
      line-height: 30px;
      text-align: center;
      text-overflow: ellipsis;
      white-space: nowrap;
      cursor: pointer;
    }
  }
</style>
