<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel :span="6">
        <div class="table-action">
          主题展示-{{ ['一', '二', '三'][queryForm.level - 1] }}级目录
        </div>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="18">
        <el-button type="success" @click="now()">实时分析</el-button>
        <el-button type="success" @click="fenxi()">拖拽分析</el-button>
        <el-button type="success" @click="handleEdit()">新建</el-button>
        <el-button size="mini" type="primary" @click="sendModel">
          模块下发
        </el-button>
        <el-button size="mini" type="primary" @click="sendCancelModel">
          模块下发取消
        </el-button>
        <el-button size="mini" type="primary" @click="send">下发</el-button>
        <el-button size="mini" type="primary" @click="sendCancel">
          取消下发
        </el-button>
        <el-button
          v-if="queryForm.level > 1"
          native-type="submit"
          type="primary"
          @click="goBack"
        >
          返回
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="60" />
      <el-table-column
        align="center"
        label="编号"
        prop="pagecode"
        show-overflow-tooltip
        width="230px"
      >
        <template #default="{ row }">
          <el-button
            v-if="queryForm.level < 2"
            type="text"
            @click="handleLevel(row)"
          >
            {{ row.pagecode }}
          </el-button>
          <span v-else>{{ row.pagecode }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="栏目名称"
        prop="pagename"
        show-overflow-tooltip
        width="230px"
      >
        <template #default="{ row }">
          <el-button
            v-if="queryForm.level == 2"
            type="text"
            @click="handleReport(row)"
          >
            {{ row.pagename }}
          </el-button>
          <span v-else>{{ row.pagename }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="描述" prop="pageDes" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="100"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
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
    <theme-edit ref="edit" @fetch-data="fetchData" />
    <send-model ref="sendModel" @fetch-data="fetchData" />
    <sendCancelModel ref="sendCancelModel" @fetch-data="fetchData" />
    <send ref="send" @fetch-data="fetchData" />
    <sendCancel ref="sendCancel" @fetch-data="fetchData" />

    <Modal ref="Modal" />
  </div>
</template>

<script>
  import { reportlist, ssfx } from '@/api/setting/themeRepertory'
  import ThemeEdit from '@/views/setting/themeRepertory/components/ThemeEdit'
  import { pagedel } from '@/api/setting/themeRepertory'
  import SendModel from '@/views/setting/themeRepertory/components/SendModel'
  import sendCancelModel from '@/views/setting/themeRepertory/components/sendCancelModel'
  import Send from '@/views/setting/themeRepertory/components/Send'
  import sendCancel from '@/views/setting/themeRepertory/components/sendCancel'
  import Modal from './components/fenxiModal'

  export default {
    name: 'Theme',
    components: {
      Send,
      sendCancel,
      SendModel,
      sendCancelModel,
      ThemeEdit,
      Modal,
    },
    props: {
      themeType: {
        type: Number,
        default: 0,
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          type: this.themeType || 0,
          pid: undefined,
          level: 1,
        },
        queryTemp: [
          {
            pid: undefined,
          },
          {
            pid: undefined,
          },
          {
            pid: undefined,
          },
        ],
        multipleSelection: [],
        type: 1,
        pageIds: '',
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleSelectionChange(val) {
        console.warn('handleSelectionChange', val)
        this.multipleSelection = val
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      goBack() {
        const level = parseInt(this.queryForm.level) - 1
        this.queryForm.pid = this.queryTemp[level].pid
        this.queryForm.level = level
        this.fetchData()
      },
      handleLevel(row) {
        if (row) {
          console.warn('row', row)
          const level = parseInt(this.queryForm.level)
          this.queryTemp[level].pid = this.queryForm.pid
          this.queryForm.pid = row.pageid
          this.queryForm.level = this.queryForm.level + 1
        }
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          pageInfo: { tlist, totalRecord },
        } = await reportlist(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleEdit(row) {
        const { type, level, pid } = this.queryForm
        this.$refs['edit'].showEdit({ type, level, pid }, row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await pagedel({
            selectedId: row.pageid,
            type: this.queryForm.pid ? 2 : 1,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleReport() {},
      sendModel() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }
        const pageids = this.multipleSelection.map((i) => i.pageid).join(',')

        this.$refs['sendModel'].showEdit({
          ids: pageids,
          pid: this.queryForm.pid,
        })
      },
      sendCancelModel() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }
        const pageids = this.multipleSelection.map((i) => i.pageid).join(',')

        this.$refs['sendCancelModel'].showEdit({
          ids: pageids,
          pid: this.queryForm.pid,
        })
      },
      send() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }

        const pageids = this.multipleSelection.map((i) => i.pageid).join(',')

        this.$refs['send'].showEdit({
          ids: pageids,
          pid: this.queryForm.pid,
        })
      },
      sendCancel() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }

        const pageids = this.multipleSelection.map((i) => i.pageid).join(',')

        this.$refs['sendCancel'].showEdit({
          ids: pageids,
          pid: this.queryForm.pid,
        })
      },
      fenxi() {
        // this.$refs['Modal'].showEdit()
        window.open('http://129.226.215.20:8088')
        // window.open(
        //   'http://192.0.2.16:9191/organizations/402c38ef01ee49aab484ea91c504cce9/vizs'
        // )
      },
      async now() {
        const { data: res } = await ssfx()
        const url = `${res.loginUrl}?loginName=${res.name}&sign=${res.sign}&timestamp=${res.time}`
        window.open(url, '_blank')
      },
    },
  }
</script>
