<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="评估标准"
      :visible.sync="dialogFormVisible"
      width="1000px"
      append-to-body
      @close="close"
      destroy-on-close
    >
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        :data="tableData"
        v-loading="listLoading"
        highlight-current-row
        @current-change="handleSelection"
      >
        <el-table-column align="center" label="名称" prop="assname" />
        <el-table-column align="center" label="描述" prop="assname" />
        <el-table-column align="center" label="创建人" prop="assname" />
        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button type="text" @click="showDetail(row)">查看明细</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <TabDetail ref="tab" />
  </div>
</template>

<script>
  import { riskMain } from '@/api/systemLog'
  import TabDetail from '@/views/workbench/contractTools/option/TabDetail.vue'
  export default {
    name: 'PlanEdit-standard',
    components: { TabDetail },
    data() {
      return {
        dialogFormVisible: false,
        tableData: [],
        listLoading: false,
        setItem: {},
      }
    },
    methods: {
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
      },
      /**
       * @description: 初始化，获取数据
       * @return {*}
       */
      showEdit(row) {
        this.dialogFormVisible = true
        riskMain({ secrectLevelId: row }).then((res) => {
          console.log('res', res)
          this.tableData = res.data.pageBean.list
        })
      },
      /**
       * @description: 选择列表数据
       * @return {*}
       */
      handleSelection(row) {
        console.log('row', row)
        this.setItem = row
      },
      /**
       * @description: 保存，回调
       * @return {*}
       */
      save() {
        console.log('this.setItem', this.setItem)
        this.$emit('handelStandard', this.setItem)
        this.close()
      },
      /**
       * @description: 打开详情页面
       * @return {*}
       */
      showDetail(row) {
        this.$refs['tab'].showEdit(row, 'detail')
      },
    },
  }
</script>

<style></style>
