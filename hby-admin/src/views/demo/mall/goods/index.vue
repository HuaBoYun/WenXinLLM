<template>
  <div class="goods-container">
    <el-alert :closable="false" show-icon :title="title" type="success" />
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input v-model="queryForm.title" placeholder="请输入商品名称" />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="handleQuery"
            >
              查询
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-row :gutter="20">
      <el-col
        v-for="(item, index) in list"
        :key="index"
        :lg="8"
        :md="8"
        :sm="8"
        :xl="6"
        :xs="24"
      >
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <div class="goods-card-body">
            <div class="goods-tag-group">
              <el-tag hit>{{ item.company }}</el-tag>
            </div>
            <div class="goods-image-group">
              <div class="goods-image">
                <vab-icon v-if="item.icon" :icon="item.icon" />
                <el-image
                  v-if="item.image || item.svg"
                  :class="{ isSvg: item.svg }"
                  fit="cover"
                  :src="item.image || item.svg"
                />
              </div>
            </div>
            <div class="goods-title">{{ item.title }}</div>
            <div class="goods-price">
              <el-tag hit type="warning">{{ item.price }}</el-tag>
              <el-tag v-for="label in item.label" :key="label" hit>
                {{ label }}
              </el-tag>
            </div>
            <div class="goods-description">
              {{ item.description }}
            </div>
            <div class="goods-detail">
              <a
                :href="item.url + '?hmsr=admin-pro&hmpl=&hmcu=&hmkw=&hmci='"
                target="_blank"
              >
                <el-button type="text">查看详情</el-button>
              </a>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script>
  import { getList } from '@/api/goods'

  export default {
    name: 'Goods',
    data() {
      return {
        queryForm: {
          pageNo: 1,
          pageSize: 20,
          title: '',
        },
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: unescape(
          '\u7269\u6599\u5546\u57ce\u7269\u6599\u7531\u7b2c\u4e09\u65b9\u63d0\u4f9b\uff0c\u0076\u0075\u0065\u002d\u0061\u0064\u006d\u0069\u006e\u002d\u0062\u0065\u0061\u0075\u0074\u0069\u0066\u0075\u006c\u4f5c\u8005\u4e0d\u63d0\u4f9b\u6280\u672f\u652f\u6301\uff0c\u4e0d\u5bf9\u7269\u6599\u8d28\u91cf\u8d1f\u8d23\uff0c\u6bcf\u4e00\u4f4d\u5f00\u53d1\u8005\u90fd\u53ef\u4ee5\u8054\u7cfb\u4f5c\u8005\u63d0\u4f9b\u4f18\u8d28\u7684\u7b2c\u4e09\u65b9\u7269\u6599\uff0c\u7ecf\u8fc7\u5ba1\u6838\u540e\u53ef\u4e0a\u7ebf\u7269\u6599\u5e02\u573a\u3002'
        ),
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
      handleQuery() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
      },
    },
  }
</script>

<style lang="scss" scoped>
  .goods-container {
    .goods-card-body {
      position: relative;
      padding: $base-padding;
      cursor: pointer;

      .goods-tag-group {
        position: absolute;
        top: 0;
        right: 0;
        z-index: 9;
        padding: $base-padding * 1.5;
      }

      .goods-image-group {
        height: 200px;
        overflow: hidden;
        background: linear-gradient(to right, #0f2027, #203a43, #2c5364);

        .goods-image {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 100%;
          height: 200px;

          .isSvg {
            padding: 100px;
          }

          i {
            font-size: 100px;
            color: $base-color-white;
          }

          transition: all ease-in-out 0.3s;

          &:hover {
            transform: scale(1.1);
          }
        }
      }

      .goods-title {
        margin: $base-margin 0 8px 0;
        overflow: hidden;
        font-size: 16px;
        font-weight: bold;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .goods-price {
        margin: 15px 0 0;
        overflow: hidden;
        font-size: 14px;
        text-overflow: ellipsis;
        white-space: nowrap;

        s {
          color: #c5c8ce;
        }
      }

      .goods-description {
        display: -webkit-box;
        height: 45px;
        margin-top: $base-margin - 5px;
        overflow: hidden;
        font-size: 12px;
        line-height: 20px;
        color: #767676;
        text-overflow: ellipsis;
        box-orient: vertical;
        line-clamp: 2;
      }

      .goods-detail {
        float: right;
        margin-bottom: $base-margin - 9px;
      }
    }
  }
</style>
