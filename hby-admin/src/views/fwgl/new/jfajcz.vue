<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-input
              v-model="queryForm.disputeno"
              clearable
              style="width: 140px; margin-right: 20px"
            />

            <el-input
              v-model="queryForm.disputeitem"
              clearable
              style="width: 140px; margin-right: 20px"
              placeholder="纠纷名称"
            />

            <el-input
              v-model="queryForm.contractname"
              clearable
              style="width: 140px; margin-right: 20px"
              placeholder="合同名称"
            />

            <el-select
              v-model="queryForm.disputetype"
              clearable
              style="width: 140px; margin-right: 20px"
              placeholder="纠纷类型"
            >
              <el-option value="一般纠纷">一般纠纷</el-option>
              <el-option value="重大纠纷">重大纠纷</el-option>
            </el-select>

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
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>
    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel :span="24">
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="登记编号" prop="disputeno">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, true)">
              {{ row.disputeno }}
            </el-button>
          </template>
        </el-table-column>

        <el-table-column align="center" label="纠纷名称" prop="disputeitem" />
        <!-- <el-table-column
            align="center"
            label="合同名称"
            prop="contractname"
            v-if="item.name === '合同名称'"
          /> -->
        <el-table-column align="center" label="纠纷类型" prop="disputetype" />

        <el-table-column align="center" label="是否紧急" prop="isuegent">
          <template slot-scope="scope">
            <span>{{ scope.row.isuegent === 1 ? '是' : '否' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="诉讼地位" prop="whethersued">
          <template slot-scope="scope">
            <span>{{ scope.row.whethersued === 1 ? '起诉' : '被诉' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="办结时间" prop="lastdealdate" />
        <el-table-column align="center" label="状态" prop="disputestatus">
          <template #default="{ row }">
            {{
              row.disputestatus == 1
                ? '审批中'
                : row.disputestatus == 2
                ? '已退回'
                : row.disputestatus == 3
                ? '已撤销'
                : row.disputestatus == 4
                ? '已终止'
                : row.disputestatus == 5
                ? '已跟踪'
                : row.disputestatus == 6
                ? '已完成'
                : '未审批'
            }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleApproval(row)"
              :disabled="!!row.disputestatus"
            >
              提交审批
            </el-button>

            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="
                row.disputestatus != 0 &&
                row.disputestatus != 2 &&
                row.disputestatus != 3
              "
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="!!row.disputestatus"
            >
              删除
            </el-button>
            <!-- <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="handleApproval(row)">
                  原告审批
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleApproval(row)">
                  被告审批
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown> -->
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <DisputeEdit ref="edit" @fetch-data="queryData" />
  </div>
</template>

<script>
  import { getcaseInformationList, Removecase } from '@/api/fwgl/legal'
  import DisputeEdit from '@/views/fwgl/new/components/DisputeEdit.vue'
  export default {
    name: '',
    components: {
      DisputeEdit,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 10,
          flowid: '698855',
        },
      }
    },
    created() {
      this.fetchData()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(20, row.disputeid)
      },
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 10,
          flowid: '698855',
        }
      },
      /**
       * @description: 重置并请求
       * @return {*}
       */      
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 请求数据
       * @return {*}
       */      
      async fetchData() {
        this.listLoading = true
        const {
          date: { tlist, totalRecord },
        } = await getcaseInformationList(this.queryForm)
        this.list = tlist.map((i) => {
          return {
            ...i,
            isUegent: i.isuegent,
            jbstaff: i.realname,
            zxstaffname: i.username,
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row, flag = false) {
        this.$refs['edit'].showEdit(row, flag)
      },
      handleDetail(row) {
        this.$refs['edit'].showDetail(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await Removecase({ disputeId: row.disputeid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    // background: #f6f8f9 !important;
    padding: 0 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
