<template>
  <el-dialog
    v-if="dialogFormVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
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
                v-model="queryForm.gcxmjgysjhNo"
                clearable
                placeholder="序号"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.xmmc"
                clearable
                placeholder="项目名称"
              />
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="queryForm.queryYear"
                type="year"
                format="yyyy"
                value-format="yyyy"
                placeholder="年份"
              ></el-date-picker>
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
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>
    <el-card shadow="never">
      <el-table
        v-loading="listLoading"
        :data="list"
        ref="multipleTable"
        @select-all="$refs.multipleTable.clearSelection()"
        @select="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column align="center" label="序号" prop="gcxmjgysjhNo" />
        <el-table-column align="center" label="项目名称" prop="xmmc" />
        <el-table-column width="1" />
        <!-- <div v-for="(item, index) in filedNow" :key="index"></div> -->
        <el-table-column
          align="center"
          label="建设单位"
          prop="jsdw"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="项目类别"
          prop="xmlb"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="项目总投资（万元）"
          prop="xmztzje"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="项目投产时间"
          prop="xmtcsj"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="生产考核完成时间"
          prop="sckhwcsj"
          show-overflow-tooltip
          :formatter="formatDate"
        />
        <el-table-column align="center" label="专项验收">
          <el-table-column
            align="center"
            label="消防设施验收-计划完成时间"
            prop="xfssysjhwcsj"
          />
          <el-table-column
            align="center"
            label="环境保护验收-计划完成时间"
            prop="hjbhysjhwcsj"
          />
          <el-table-column
            align="center"
            label="安全设施验收-计划完成时间"
            prop="aqssysjhwcsj"
          />
          <el-table-column
            align="center"
            label="职业病防护设施验收-计划完成时间"
            prop="zybfhssysjhwcsj"
          />
          <el-table-column
            align="center"
            label="水土保持设施验收-计划完成时间"
            prop="stbcssysjhwcsj"
          />
          <el-table-column
            align="center"
            label="土地利用验收-计划完成时间"
            prop="tdlyysjhwcsj"
          />
          <el-table-column
            align="center"
            label="节能验收-计划完成时间"
            prop="jnysjhwcsj"
          />
          <el-table-column
            align="center"
            label="安全防范系统验收-计划完成时间"
            prop="aqffxtysjhwcsj"
          />
          <el-table-column
            align="center"
            label="雷电防护装置验收-计划完成时间"
            prop="ldfhzzysjhwcsj"
          />
          <el-table-column
            align="center"
            label="档案验收-计划完成时间"
            prop="daysjhwcsj"
          />
          <el-table-column
            align="center"
            label="竣工决算验收-上报审计时间"
            prop="jgjsyssbsjsj"
          />
        </el-table-column>
        <el-table-column
          align="center"
          label="项目结算验收-计划完成时间"
          prop="xmjsysjhwcsj"
        />
        <el-table-column
          align="center"
          label="初步验收验收-计划完成时间"
          prop="cbysjhwcsj"
        />
        <el-table-column
          align="center"
          label="竣工验收验收-计划完成时间"
          prop="jgysjhwcsj"
        />
        <el-table-column
          align="left"
          label="备注"
          prop="bz"
          show-overflow-tooltip
        />
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="confirm" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>
<script>
  import { getxzListPlan } from '@/oapi/audit/plan'
  import { formatDate } from '@/utils/index'
  import store from '@/store'
  const token = store.getters['user/token']
  export default {
    data() {
      return {
        headers: { token },
        list: [],
        listLoading: false,
        title: '选择工程竣工验收计划',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          gcxmjgysjhNo: '',
          queryYear: '',
          xmmc: '',
          pageNumber: 1,
          pageSize: 20,
        },
        localKey: 'oilAudit-gcgl-gcjgysjh-search',
        tableKey: 'oilAudit-gcgl-gcjgysjh-list',
        searchMore: true,
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit() {
        console.log(123123123123)
        this.fetchData()
        this.dialogFormVisible = true
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          xmmc: '',
          gcxmjgysjhNo: '',
          queryYear: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },

      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
          code,
        } = await getxzListPlan(this.queryForm)
        if (code === 1) {
          this.list = tlist || []
          this.total = totalRecord || 0
          this.listLoading = false
        }
      },
      handleSelectionChange(selection, row) {
        this.$refs.multipleTable.clearSelection()
        this.$refs.multipleTable.toggleRowSelection(row)
        this.selectedItem = row
        return
      },
      confirm() {
        if (!this.selectedItem) {
          return this.$message.error('尚未选择数据！')
        }
        this.$emit('selected', this.selectedItem)
        this.close()
      },
      close() {
        this.dialogFormVisible = false
        this.selectedItem = null
      },
    },
  }
</script>
<style lang="scss" scoped></style>
