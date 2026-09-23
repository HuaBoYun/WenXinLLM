<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div style="text-align: right; margin-bottom: 20px">
      <el-button type="success" @click="updateStatus(1)">批量启用</el-button>
      <el-button type="success" @click="updateStatus(0)">批量禁用</el-button>
    </div>
    <el-table
      :data="list"
      @selection-change="handleSelectionChange"
      ref="multipleTable"
    >
      <el-table-column
        width="48"
        type="selection"
        :reserve-selection="true"
      ></el-table-column>
      <el-table-column
        align="center"
        label="旧字段名称"
        prop="orlName"
      ></el-table-column>
      <el-table-column
        align="center"
        label="新字段名称"
        prop="changeName"
      ></el-table-column>
      <el-table-column align="center" label="宽度" prop="componentWidth">
        <template #default="{ row }">
          {{ row.componentWidth + '%' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否必选" prop="isRequired">
        <template #default="{ row }">
          {{ row.isRequired == 1 ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否编辑展示" prop="isDetails">
        <template #default="{ row }">
          {{ row.isDetails == 1 ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否列表展示" prop="isList">
        <template #default="{ row }">
          {{ row.isList == 1 ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" prop="state">
        <template #default="{ row }">
          {{ row.state == 1 ? '启用' : '禁用' }}
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        label="排序"
        prop="sort"
      ></el-table-column>

      <el-table-column align="center" label="操作">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="changeStatus(row)">
            {{ row.state == 0 ? '启用' : '禁用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <EditDetail ref="edit" @fetch-data="fetchData"></EditDetail>
    <template #footer>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import EditDetail from './detailEdit.vue'
  import {
    getLRDetailData,
    changeEditStatus,
    updateAllStatus,
  } from '@/api/setting/auths'
  export default {
    name: 'AddFormEdit',
    components: { EditDetail },
    data() {
      return {
        form: {
          jobname: '',
        },
        rules: {
          jobname: [{ required: true, trigger: 'blur', message: '请选择表单' }],
        },
        title: '',
        dialogFormVisible: false,
        sceneId: '',
        list: [],
        current: [],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.sceneId = row.id
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.fetchData()
        })
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      async fetchData() {
        const { data } = await getLRDetailData({ sceneId: this.sceneId })
        this.list = data
      },
      save() {
        this.dialogFormVisible = false
      },
      async changeStatus(row) {
        const res = await changeEditStatus({
          id: row.id,
          state: row.state == 0 ? 1 : 0,
        })
        if (res.code == 200) {
          this.$baseMessage(
            '状态修改成功',
            'success',
            'vab-hey-message-success'
          )
          this.fetchData()
        }
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, this.sceneId)
      },
      handleSelectionChange(val) {
        this.current = val.map((res) => res.id)
      },
      async updateStatus(type) {
        if (this.current.length == 0) {
          this.$baseMessage(
            '请先勾选字段再批量操作',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        const res = await updateAllStatus({
          ids: this.current,
          status: type,
        })
        if (res.code == 200) {
          this.$baseMessage(
            '状态修改成功',
            'success',
            'vab-hey-message-success'
          )
          this.fetchData()
          this.$refs.multipleTable.clearSelection()
        }
      },
    },
  }
</script>
