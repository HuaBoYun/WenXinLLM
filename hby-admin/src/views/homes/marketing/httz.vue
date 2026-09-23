<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <vab-query-form>
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="请输入标题"
              suffix-icon="el-icon-search"
              style="width: 180px; margin-right: 20px"
            />
            <!-- <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button @click="fetchData('reset')" type="primary">重置</el-button>

            </el-form-item> -->
          </el-form>
        </vab-query-form-left-panel>
        <vab-query-form-right-panel :span="24">
          <el-row>
            <el-button>显示字段</el-button>
          </el-row>
        </vab-query-form-right-panel>

        <el-table v-loading="listLoading" :data="list" :show-header="false">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column align="center" label="标题" prop="name" sortable>
            <template #default="{ row }">
              <div class="row-content">
                <el-row class="title">
                  <el-col :span="6">{{ row.name }}</el-col>
                  <el-col :span="6">收款 {{ row.name1 }}</el-col>
                  <el-col :span="6">待登记 --</el-col>
                </el-row>
                <el-row class="desc">
                  <el-col :span="6">合同编号：{{ row.name2 || '--' }}</el-col>
                  <el-col :span="6">签约方：{{ row.name3 || '--' }}</el-col>
                  <el-col :span="6">起草人：{{ row.name4 || '--' }}</el-col>
                  <el-col :span="6">起草时间：{{ row.name5 || '--' }}</el-col>
                </el-row>
                <el-row class="desc">
                  <el-col :span="6">签约人：{{ row.name6 || '--' }}</el-col>
                  <el-col :span="6">经办人：{{ row.name7 || '--' }}</el-col>
                  <el-col :span="6">经办人部门：{{ row.name8 || '--' }}</el-col>
                  <el-col :span="6">生效时间：{{ row.name9 || '--' }}</el-col>
                </el-row>
                <el-row class="desc">
                  <el-col :span="6">终止时间：{{ row.name10 || '--' }}</el-col>
                  <el-col :span="6">签订时间：{{ row.name11 || '--' }}</el-col>
                  <el-col :span="6">主从属性：{{ row.name12 || '--' }}</el-col>
                  <el-col :span="6">
                    是否格式合同：{{ row.name13 || '--' }}
                  </el-col>
                </el-row>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          class="pagination"
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </vab-query-form>
    </el-card>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        activeName: 'first',
        queryForm: {
          name: '',
          pageNumber: 1,
          pageSize: 10,
        },
        total: 1,
        layout: 'total, sizes, prev, pager, next, jumper',
        listLoading: false,
        list: [
          {
            name: '附件测试',
            name1: '￥2,222',
            name2: '20221107019501',
            name3: '深圳比一比科技有限公司',
            name4: '张孝昆',
            name5: '2022-11-07 15:37',
            name6: '张孝昆',
            name7: '张孝昆',
            name8: 'Demo场景体验部',
            name9: '2022-11-07',
            name10: null,
            name11: null,
            name12: '主合同',
            name13: '否',
          },
          {
            name: '附件测试',
            name1: '￥2,222',
            name2: '20221107019501',
            name3: '深圳比一比科技有限公司',
            name4: '张孝昆',
            name5: '2022-11-07 15:37',
            name6: '张孝昆',
            name7: '张孝昆',
            name8: 'Demo场景体验部',
            name9: '2022-11-07',
            name10: null,
            name11: null,
            name12: '主合同',
            name13: '否',
          },
          {
            name: '附件测试',
            name1: '￥2,222',
            name2: '20221107019501',
            name3: '深圳比一比科技有限公司',
            name4: '张孝昆',
            name5: '2022-11-07 15:37',
            name6: '张孝昆',
            name7: '张孝昆',
            name8: 'Demo场景体验部',
            name9: '2022-11-07',
            name10: null,
            name11: null,
            name12: '主合同',
            name13: '否',
          },
          {
            name: '附件测试',
            name1: '￥2,222',
            name2: '20221107019501',
            name3: '深圳比一比科技有限公司',
            name4: '张孝昆',
            name5: '2022-11-07 15:37',
            name6: '张孝昆',
            name7: '张孝昆',
            name8: 'Demo场景体验部',
            name9: '2022-11-07',
            name10: null,
            name11: null,
            name12: '主合同',
            name13: '否',
          },
          {
            name: '附件测试',
            name1: '￥2,222',
            name2: '20221107019501',
            name3: '深圳比一比科技有限公司',
            name4: '张孝昆',
            name5: '2022-11-07 15:37',
            name6: '张孝昆',
            name7: '张孝昆',
            name8: 'Demo场景体验部',
            name9: '2022-11-07',
            name10: null,
            name11: null,
            name12: '主合同',
            name13: '否',
          },
          {
            name: '附件测试',
            name1: '￥2,222',
            name2: '20221107019501',
            name3: '深圳比一比科技有限公司',
            name4: '张孝昆',
            name5: '2022-11-07 15:37',
            name6: '张孝昆',
            name7: '张孝昆',
            name8: 'Demo场景体验部',
            name9: '2022-11-07',
            name10: null,
            name11: null,
            name12: '主合同',
            name13: '否',
          },
          {
            name: '附件测试',
            name1: '￥2,222',
            name2: '20221107019501',
            name3: '深圳比一比科技有限公司',
            name4: '张孝昆',
            name5: '2022-11-07 15:37',
            name6: '张孝昆',
            name7: '张孝昆',
            name8: 'Demo场景体验部',
            name9: '2022-11-07',
            name10: null,
            name11: null,
            name12: '主合同',
            name13: '否',
          },
        ],
      }
    },
    methods: {
      handleClick(tab, event) {},
      filterTag(value, row) {
        return row.tag === value
      },
      handleCurrentChange() {},
      handleSizeChange() {},
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }

  .row-content {
    text-align: left !important;
  }

  .row-content .title {
    color: #333;
    font-size: 16px;
    text-align: left !important;
    margin: 8px 0 !important;
  }

  .row-content .desc {
    color: #999;
    margin: 8px 0 !important;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding-right: 20px;
  }

  .row-content .other {
    display: flex;
    align-items: center;
    color: #999;
  }

  .row-content .other .other-item {
    margin-right: 20px;
    color: #999;
  }
</style>
