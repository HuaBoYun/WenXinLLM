<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="待处理" name="first">
          <vab-query-form>
            <el-card shadow="never">
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
                  <el-button type="primary">列表</el-button>
                  <el-button>置为已办事项</el-button>
                  <el-button>设为星标</el-button>

                  <el-dropdown style="margin-left: 10px">
                    <el-button>
                      更多操作
                      <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item>取消星标</el-dropdown-item>
                      <el-dropdown-item>XXXX</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </el-row>
              </vab-query-form-right-panel>

              <el-table v-loading="listLoading" :data="list">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column type="index" width="50"></el-table-column>
                <el-table-column
                  align="center"
                  width="400"
                  label="标题"
                  prop="name"
                  sortable
                />
                <el-table-column
                  prop="tag"
                  label="标签"
                  width="100"
                  :filters="[
                    { text: '星标', value: '星标' },
                    { text: '非星标', value: '非星标' },
                  ]"
                  :filter-method="filterTag"
                  filter-placement="bottom-end"
                >
                  <template slot-scope="scope">
                    <el-tag
                      :type="scope.row.tag === '星标' ? 'success' : 'primary'"
                      disable-transitions
                    >
                      {{ scope.row.tag }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  label="消息类型"
                  prop="name1"
                  sortable
                />
                <el-table-column
                  align="center"
                  label="模块来源"
                  prop="name2"
                  sortable
                />
                <el-table-column
                  align="center"
                  label="发起人"
                  prop="name3"
                  sortable
                />
                <el-table-column
                  align="center"
                  label="接收时间"
                  prop="name4"
                  sortable
                />
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
            </el-card>
          </vab-query-form>
        </el-tab-pane>
        <el-tab-pane label="已处理" name="second">配置管理</el-tab-pane>
      </el-tabs>
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
            name: '666绩效考核有加、减分指标结果值需要您录入数据，请处理',
            name1: '处理类',
            name2: '绩效考核',
            name3: '张孝昆',
            name4: '2023-02-02 11:46',
            tag: '星标',
          },
        ],
      }
    },
    methods: {
      handleClick(tab, event) {},
      filterTag(value, row) {
        return row.tag === value
      },
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
</style>
