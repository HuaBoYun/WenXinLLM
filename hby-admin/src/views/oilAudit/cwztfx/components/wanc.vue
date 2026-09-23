<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="visible"
      width="80%"
      :close-on-click-modal="false"
    >
      <el-row :gutter="10">
        <el-col :lg="24" :md="24" :sm="24">
          <el-table :data="list" style="width: 100%">
            <el-table-column
              align="center"
              label="项目编号"
              prop="qdcode"
            ></el-table-column>
            <el-table-column
              align="center"
              label="项目名称"
              prop="projectName"
            ></el-table-column>
            <el-table-column
              align="center"
              label="项目负责人"
              prop="projectOrderName"
            ></el-table-column>
            <el-table-column
              align="center"
              label="审计类型"
              prop="sjlxName"
            ></el-table-column>
            <el-table-column
              align="center"
              label="计划年度"
              prop="planYear"
            ></el-table-column>
            <el-table-column
              align="center"
              label="计划开始时间"
              prop="planStarttime"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="计划结束时间"
              prop="planEndtime"
              show-overflow-tooltip
            />

            <!-- <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleTransact(row)">
                  办理
                </el-button>
                <el-button
                  type="text"
                  @click="handleComplete(row)"
                  :loading="row.btnLoading"
                >
                  完成
                </el-button>
              </template>
            </el-table-column> -->
          </el-table>
          <el-pagination
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
  import { formatDay } from '@/utils/index'

  import { getDaList } from '@/oapi/audit/archives'

  export default {
    name: 'wan',
    components: {},
    data() {
      return {
        visible: false,
        title: '',
        list: [],
        list2: [],
        listLoading: true,
        btnLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        total2: 0,
        hyTotal: 0,
        xmpyTotal: 2,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },

    created() {},
    methods: {
      show(dataIndex) {
        this.visible = true
        this.fetchData(dataIndex)
      },

      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      async fetchData(index) {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getDaList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
    },
  }
</script>
<style scoped>
  h5 {
    font-size: 18px;
    margin: 2px;
    color: #333;
  }
  .el-col > div {
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .table-title {
    cursor: pointer;
  }

  .table th {
    position: relative;
  }

  .table-filter {
    position: absolute;
    border: 1px solid gainsboro;
    padding: 5px;
    left: 0;
    right: 0;
    top: 40px;
    background: white;
    min-width: 160px;
  }

  .table-filter input {
    padding: 5px;
    font-size: 14px;
    margin-right: 5px;
  }

  .table-filter .form-check {
    display: flex;
    flex-direction: column;
    text-align: left;
    font-size: 14px;
    font-weight: 400;
    padding: 5px;
  }

  .table-filter button {
    font-size: 12px;
    padding: 2px 10px;
  }

  .table-responsive {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
  }

  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .select-year {
    display: flex;
    background: aliceblue;
    padding: 5px;
  }

  .select-year > div {
    margin-right: 10px;
    padding: 2px 5px;
    cursor: pointer;
  }

  .select-year .active {
    background: #ffaf0f;
    border-radius: 20px;
    color: white;
  }
  h5 {
    margin: 0 0 10px 0;
    font-size: 17px;
  }
  .highlight {
    color: red; /* 设置文字颜色为红色 */
    font-weight: bold; /* 加粗文字 */
  }
</style>
