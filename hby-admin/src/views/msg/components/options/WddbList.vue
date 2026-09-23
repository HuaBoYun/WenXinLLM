<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    class="deal_dialog"
    @close="close()"
  >
    <el-table
      v-loading="listLoading"
      :data="list"
      ref="multipleTable"
      @select-all="handleSelectAll"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        align="center"
        label="序号"
        type="index"
        width="50"
      ></el-table-column>
      <el-table-column
        align="center"
        label="流程标题"
        prop="fullName"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="所属流程" prop="flowName" />
      <el-table-column align="center" label="流程版本" prop="flowVersion" />
      <el-table-column
        align="center"
        label="发起时间"
        prop="startTime"
        show-overflow-tooltip
        :formatter="formatDate"
      />
      <el-table-column align="center" label="发起人员" prop="userName" />
      <el-table-column align="center" label="审批节点" prop="nodeName" />
      <el-table-column align="center" label="紧急程度" prop="userName">
        <template slot-scope="scope">普通</template>
      </el-table-column>
      <el-table-column align="center" label="流程状态" prop="nodeName">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.status == 1">等待审核</el-tag>
          <el-tag type="success" v-else-if="scope.row.status == 2">
            审核通过
          </el-tag>
          <el-tag type="danger" v-else-if="scope.row.status == 3">
            审核驳回
          </el-tag>
          <el-tag type="info" v-else-if="scope.row.status == 4">
            流程撤回
          </el-tag>
          <el-tag type="info" v-else-if="scope.row.status == 5">
            审核终止
          </el-tag>
          <el-tag type="warning" v-else>等待提交</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="接收时间"
        prop="creatorTime"
        show-overflow-tooltip
        :formatter="formatDate"
      />
    </el-table>
    <div slot="footer">
      <el-button @click="close()">取消</el-button>
      <el-button @click="add" type="primary">批量通过</el-button>
    </div>
    <BeforSubmit
      ref="befor"
      @close="close"
      :hasSign="this.hasSign"
      :hasFreeApprover="this.hasFreeApprover"
      :isCustomCopy="this.isCustomCopy"
    />
  </el-dialog>
</template>

<script>
  import { getBatchList, batchCandidate } from '@/oapi/contract/manage'
  import { formatDate } from '@/utils/index'
  import BeforSubmit from '@/views/msg/components/options/beforSubmit.vue'

  export default {
    components: { BeforSubmit },
    data() {
      return {
        dialogFormVisible: false,
        listLoading: false,
        list: [],
        select: [],
        title: '批量审批',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        hasSign: false, //是否有签名
        hasFreeApprover: false, //是否加签
        isCustomCopy: false, //是否自定义抄送人
      }
    },
    methods: {
      add() {
        if (!this.select || !this.select.length)
          return this.$message.error('请选择审批单')
        let isDiffer = this.select.some(
          (o) =>
            o.flowVersion !== this.select[0].flowVersion ||
            o.flowId !== this.select[0].flowId
        )
        if (isDiffer) return this.$message.error('请选择相同的版本审批单')
        const firstSelect = this.select[0]
        const approversProperties = JSON.parse(firstSelect.approversProperties)
        console.log(
          'res',
          firstSelect,
          approversProperties,
          approversProperties.hasSign
        )
        // batchCandidate({flowId: firstSelect.flowId, id: firstSelect.id}) .then(res => {
        //   console.log('res', res)
        // })
        this.hasSign = approversProperties.hasSign //是否有签名
        this.hasFreeApprover = approversProperties.hasFreeApprover //是否加签
        this.isCustomCopy = approversProperties.isCustomCopy //是否自定义抄送人
        let obj = {
          flowCode: firstSelect.flowCode,
          flowId: firstSelect.flowId,
          id: firstSelect.id,
          taskList: this.select,
          batch: true,
        }
        this.$refs['befor'].show(obj, 'audit') // 批量通过
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      async showEdit() {
        this.fetchData()
        this.dialogFormVisible = true
      },
      close() {
        this.select = []
        this.dialogFormVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, totalCount },
        } = await getBatchList({ ...this.queryForm })
        this.listLoading = false
        this.total = totalCount
        this.list = list
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },
      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
    },
  }
</script>
