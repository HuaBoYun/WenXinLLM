<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div>
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-top-panel>
              <el-form
                ref="form"
                checkable
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item>
                  <el-input
                    v-model="queryForm.risknumber"
                    clearable
                    placeholder="编号"
                  />
                </el-form-item>
                <el-form-item>
                  <el-input
                    v-model="queryForm.riskname"
                    clearable
                    placeholder="名称"
                  />
                </el-form-item>
                <el-form-item>
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
                  <el-button native-type="submit" @click="fetchData('reset')">
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </vab-query-form-top-panel>
          </el-card>
        </vab-query-form>

        <el-card shadow="never" class="secondCard">
          <el-table v-loading="listLoading" :data="list">
            <el-table-column align="center" label="编号" prop="risknumber">
              <template #default="{ row }">
                <el-button type="text" @click="handleRead(row)">
                  {{ row.risknumber }}
                </el-button>
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="名称"
              prop="riskname"
              show-overflow-tooltip
            />
            <el-table-column align="center" label="机构" prop="unit" />
            <el-table-column
              align="center"
              label="当前版本"
              prop="version"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="创建时间"
              prop="riskcreatedt"
              show-overflow-tooltip
              :formatter="formatDate"
            />

            <el-table-column align="center" label="操作">
              <template #default="{}">
                <el-button type="text">查看历史版本</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          background
          class="pager"
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
  import { formatDay } from '@/utils/index'

  export default {
    name: 'Fillin',
    components: {},
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          risknumber: '',
          riskname: '',
          riskcatid: '',
          pageNo: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      // this.fetchData()
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },

      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      handleRead(row) {
        this.$refs['read'].showRead(row, this.queryForm.riskcatid)
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @param {*} type
       * @return {*}
       */
      async fetchData(type) {
        this.listLoading = true
        if (type && type == 'reset') this.$refs['form'].resetFields()
        this.queryForm.riskcatid =
          typeof type == 'number' ? type : this.queryForm.riskcatid
        const {
          data: {
            pageBean: { records, total },
          },
        } = await getCreationVersionList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
