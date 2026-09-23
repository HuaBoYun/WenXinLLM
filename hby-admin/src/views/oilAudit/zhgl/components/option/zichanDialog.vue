<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="right">
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">确 定</el-button>
          </vab-query-form-right-panel>
          <vab-query-form-left-panel :span="24">
            <el-input
              v-model="queryForm.assetName"
              placeholder="资产名称"
              clearable
              style="width: 250px; margin-right: 10px"
            />
            <el-input
              v-model="queryForm.assetCode"
              placeholder="资产编码"
              clearable
              style="width: 250px; margin-right: 10px"
            />
            <el-input
              v-model="queryForm.specificationType"
              placeholder="规格型号"
              clearable
              style="width: 250px; margin-right: 10px"
            />
            <el-button
              type="primary"
              @click="getExecutorList"
              style="margin-top: 10px !important"
            >
              查询
            </el-button>
            <el-button
              type="primary"
              @click="reset"
              style="margin-top: 10px !important"
            >
              重置
            </el-button>
          </vab-query-form-left-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
        >
          <el-table-column align="center" label="类别" prop="assetType" />
          <el-table-column align="center" label="资产编码" prop="assetCode" />
          <el-table-column align="center" label="资产名称" prop="assetName" />
          <el-table-column
            align="center"
            label="规格型号"
            prop="specificationType"
          />
          <el-table-column
            align="center"
            label="制造厂家"
            prop="manufacturer"
          />
          <el-table-column
            align="center"
            label="所属单位编码"
            prop="unitCode"
          />
          <el-table-column
            align="center"
            label="所属单位名称"
            prop="unitName"
          />
          <el-table-column
            align="center"
            label="车牌井号"
            prop="licensePlate"
          />
          <el-table-column align="center" label="自编号" prop="selfNum" />
          <el-table-column
            align="center"
            label="出厂编号"
            prop="factoryNumber"
          />
          <el-table-column
            align="center"
            label="出厂、建筑或完井日期"
            prop="factoryTime"
          />
          <el-table-column
            align="center"
            label="投产日期"
            prop="productionTime"
          />
          <el-table-column align="center" label="区块" prop="block" />
          <el-table-column align="center" label="区块名称" prop="blockName" />
          <el-table-column
            align="center"
            label="存放（安装）地点"
            prop="depositPlace"
          />
          <el-table-column align="center" label="计量单位" prop="measurement" />
          <el-table-column
            align="center"
            label="复合数量"
            prop="compositeQuantity"
          />
          <el-table-column
            align="center"
            label="功率能力"
            prop="powerCapacity"
          />
          <el-table-column
            align="center"
            label="技术状况名称"
            prop="technicalConditionName"
          />
          <el-table-column
            align="center"
            label="使用状态名称"
            prop="usageStatusName"
          />
          <el-table-column
            align="center"
            label="增加原因名称-台账"
            prop="addCauseName"
          />
          <el-table-column
            align="center"
            label="资金渠道名称-台账"
            prop="fundSchannelName"
          />
          <el-table-column align="center" label="增加日期" prop="addTime" />
          <el-table-column
            align="center"
            label="停产日期"
            prop="discontinuedTime"
          />
          <el-table-column align="center" label="保管人" prop="custodianName" />
          <el-table-column
            align="center"
            label="期末原值"
            prop="finalOriginalValue"
          />
          <el-table-column
            align="center"
            label="期末累计折旧"
            prop="finalCumulativeDepreciation"
          />
          <el-table-column
            align="center"
            label="期末净值"
            prop="finalNetWorth"
          />
          <el-table-column
            align="center"
            label="期末减值准备"
            prop="finalImpairmentPrepare"
          />
          <el-table-column
            align="center"
            label="在用人员"
            prop="usePeopleName"
          />
          <el-table-column
            align="center"
            label="在用部门"
            prop="useDepartmentName"
          />
          <el-table-column
            align="center"
            label="特殊事项"
            prop="specialMatter"
          />
          <el-table-column align="center" label="备注" prop="remark" />
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
      </div>
    </div>
  </el-dialog>
</template>
<script>
  import { getList } from '@/oapi/ypns_zhgl/zygl'
  export default {
    name: 'ZichanDialog',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          assetName: undefined,
          assetCode: undefined,
          specificationType: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
      }
    },
    created() {},
    methods: {
      show() {
        this.current = undefined
        this.dialogFormVisible = true
        this.getExecutorList()
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.pid = val.id
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择资产！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
      reset() {
        this.queryForm = {
          assetName: undefined,
          assetCode: undefined,
          specificationType: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
        this.getExecutorList()
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
