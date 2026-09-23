<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <form-list />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-top-panel>
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm.name"
                  clearable
                  placeholder="方案编号"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.name"
                  clearable
                  placeholder="方案名称"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.name"
                  clearable
                  placeholder="创建人"
                />
              </el-form-item>
              <el-form-item>
                <el-select v-model="queryForm.name" placeholder="请选择状态">
                  <el-option label="区域一" value="shanghai" />
                  <el-option label="区域二" value="beijing" />
                </el-select>
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
            </el-form>
          </vab-query-form-top-panel>
          <vab-query-form-left-panel>
            <span></span>
          </vab-query-form-left-panel>
          <vab-query-form-right-panel>
            <el-button type="success" @click="handleAdd">新建</el-button>
            <el-button type="primary" @click="handleAdd">导出</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table v-loading="listLoading" :data="list">
          <el-table-column
            align="center"
            label="方案编号"
            prop="data"
            width="100"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit">
                {{ row.data }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="方案名称" prop="data" />
          <el-table-column
            align="center"
            label="创建人"
            prop="data"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="创建日期"
            prop="data"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            align="center"
            label="截止日期"
            prop="data"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            align="center"
            label="整改责任人"
            prop="data"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="data"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="$refs['projectDataInfo'].showEdit()"
              >
                修改
              </el-button>
              <el-button type="text" @click="handleEdit2(row)">修改</el-button>
              <el-dropdown style="margin-left: 10px">
                <el-button type="text">更多</el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>下发</el-dropdown-item>
                  <el-dropdown-item>启动</el-dropdown-item>
                  <el-dropdown-item>关闭</el-dropdown-item>
                  <el-dropdown-item>删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
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
    </div>
    <scheme-info ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import FormList from '../components/FormList.vue'
  import SchemeInfo from './components/SchemeInfo'

  export default {
    name: 'Scheme',
    components: { SchemeInfo, FormList },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
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
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
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
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 90%;
  }
</style>
