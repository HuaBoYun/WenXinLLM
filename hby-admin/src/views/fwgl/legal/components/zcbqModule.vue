<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="'资产保全'"
    :visible.sync="dialogFormVisible"
    append-to-body
    width="1000px"
    @close="close"
  >
    <div style="text-align: right; margin-bottom: 5px">
      <el-button type="success" @click="handleAdd">新 增</el-button>
    </div>
    <el-table :data="tableData">
      <el-table-column
        v-for="item in tableOption"
        :key="item.prop"
        :label="item.label"
        :prop="item.prop"
      ></el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <zcbqAdd
      ref="zcbqAddRef"
      @selected="handleSelected"
      @fetch="fetchData"
      :info="info"
    />
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="close">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import zcbqAdd from './zcbqAdd.vue'
  import { assetporotectList, assetporotectDelete } from '@/api/fwgl/legal'
  export default {
    components: { zcbqAdd },
    props: ['info'],
    data() {
      return {
        dialogFormVisible: false,
        tableData: [],
        tableOption: [
          {
            label: '所属纠纷',
            prop: 'disputeitem',
          },
          {
            label: '保全类型',
            prop: 'actionstage',
          },
          {
            label: '申请人',
            prop: 'apper',
          },
          // {
          //   label: '执行金额（万元）',
          //   prop: 'executamount',
          // },
        ],
      }
    },
    methods: {
      handleSelected(rowData) {
        this.tableData.unshift(rowData)
      },
      show(rowData) {
        this.dialogFormVisible = true

        this.litigationid = rowData.litigationid
        this.arbitraid = rowData.arbitraid

        this.fetchData()
      },
      handleAdd(e) {
        this.$refs['zcbqAddRef'].show(
          {
            litigationid: this.litigationid,
            arbitraid: this.arbitraid,
          },
          '新增'
        )
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.litigationid = null
        this.arbitraid = null
        this.$emit('close')
      },
      /**
       * @description: 请求数据
       * @return {*}
       */      
      async fetchData() {
        const p = {}
        if (this.litigationid) p.litigationid = this.litigationid
        if (this.arbitraid) p.arbitraid = this.arbitraid

        const res = await assetporotectList(p)

        if (res && res.date) {
          // date...
          this.tableData = res.date.tlist || []
        }
      },
      async handleDelete(row) {
        const res = await assetporotectDelete({ id: row.id })
        if (res && res.code === 1) {
          this.$message({
            type: 'success',
            message: '操作成功！',
          })
          this.fetchData()
        } else {
          this.$message({
            type: 'error',
            message: '操作失败！',
          })
        }
      },
      handleEdit(row) {
        this.$refs['zcbqAddRef'].show(
          {
            litigationid: this.litigationid,
            arbitraid: this.arbitraid,
          },
          '编辑',
          row
        )
      },
    },
  }
</script>

<style lang="less" scoped></style>
