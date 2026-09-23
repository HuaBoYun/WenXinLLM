<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-10-23 16:50:36
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-10-23 16:52:03
 * @FilePath: \hb-admin\src\views\contract\legal\components\zxglModule.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="'执行管理'"
    :visible.sync="dialogFormVisible"
    append-to-body
    width="1200px"
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
      <el-table-column align="center" label="状态" prop="status">
        <template #default="{ row }">
          {{
            row.status == 1
              ? '审批中'
              : row.status == 2
              ? '已退回'
              : row.status == 3
              ? '已撤回'
              : row.status == 4
              ? '已终止'
              : row.status == 5
              ? '已跟踪'
              : row.status == 6
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
            :disabled="row.status"
          >
            提交审批
          </el-button>
          <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="row.status != 0 && row.status != 2 && row.status != 3"
          >
            编辑
          </el-button>
          <el-button
            type="text"
            @click="handleDelete(row)"
            :disabled="row.status"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <zxglAdd
      ref="zxglAddRef"
      @selected="handleSelected"
      @fetch="fetchData"
      :info="info"
    />
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="close">确 定</el-button>
    </template>
    <ProcessList ref="process" @fetchData="fetchData" />
  </el-dialog>
</template>

<script>
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import zxglAdd from './zxglAdd.vue'
  import { legalExecumgrList, legalExecumgrDelete } from '@/api/fwgl/legal'
  export default {
    components: { zxglAdd, ProcessList },
    props: ['info'],
    data() {
      return {
        dialogFormVisible: false,
        tableData: [],
        tableOption: [
          {
            label: '纠纷名称',
            prop: 'disputename',
          },
          {
            label: '执行案号',
            prop: 'execuno',
          },
          {
            label: '执行法院',
            prop: 'execucourt',
          },
          {
            label: '执行方式',
            prop: 'executype',
          },
          {
            label: '执行总金额（万元）',
            prop: 'execuamount',
          },
        ],
      }
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
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
        this.$refs['zxglAddRef'].show(
          {
            litigationid: this.litigationid,
            arbitraid: this.arbitraid,
          },
          '新增'
        )
      },
      handleApproval(row) {
        //提交审批
        // this.$refs['process'].save(27, row.otherFileMessageId)
        const tableId = 29
        const fromId = row.id
        this.$refs['process'].save(tableId, fromId)
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

        const res = await legalExecumgrList(p)

        if (res && res.date) {
          // date...
          this.tableData = res.date.tlist || []
        }
      },
      async handleDelete(row) {
        const res = await legalExecumgrDelete({ id: row.id })
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
        this.$refs['zxglAddRef'].show(
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
