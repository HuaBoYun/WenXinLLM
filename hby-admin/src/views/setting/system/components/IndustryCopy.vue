<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
    :close-on-click-modal="false"
  >
    <div class="lr-layout">
      <div class="left">
        <IndustryTree
          ref="industry-tree"
          :form-data="formData"
          :selectedid="selectedid"
          @selected="handleSelected"
        />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-left-panel :span="20">
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item class="">
                <el-input
                  v-model="queryForm.flownumber"
                  clearable
                  placeholder="流程编号"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.flowname"
                  clearable
                  placeholder="流程名称"
                />
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
          </vab-query-form-left-panel>
          <vab-query-form-right-panel :span="4">
            <div class="table-action">
              <el-button type="success" @click="save">确 定</el-button>
            </div>
          </vab-query-form-right-panel>
        </vab-query-form>

        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleCurrentRowChange"
        >
          <el-table-column
            align="center"
            label="流程编号"
            prop="flownumber"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="流程名称"
            prop="flowname"
            show-overflow-tooltip
          />
          <el-table-column align="center" label="机构" prop="comName" />
          <el-table-column align="center" label="创建时间" prop="createtime" />
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
  import { copyFromIndustry, saveCopyFromIndustry } from '@/api/setting/system'
  import IndustryTree from './IndustryTree.vue'

  export default {
    name: 'IndustryCopy',
    components: { IndustryTree },
    data() {
      return {
        title: '从行业复制',
        dialogFormVisible: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        selectedid: undefined,
        queryForm: {
          flownumber: undefined,
          flowname: undefined,
          faflowid: undefined,
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        formData: {
          selectedId: undefined,
          faflowid: undefined,
          orgid: undefined,
        },
        options: [],
        currentRow: undefined,
      }
    },
    created() {},
    methods: {
      showEdit(curNode) {
        const { id } = curNode
        this.selectedid = id
        this.queryForm.faflowid = id
        this.formData.faflowid = id
        this.fetchData()
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['industry-tree'].fetchTree()
        })
      },
      close() {
        this.dialogFormVisible = false
      },
      async save() {
        const { code, msg } = await saveCopyFromIndustry(this.formData)
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
          this.close()
        }
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
        } = await copyFromIndustry(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSelected(data) {
        const { orgid } = data
        this.queryForm.pid = orgid
        this.fetchData()
      },
      handleCurrentRowChange(row) {
        this.currentRow = row
        this.formData.flowid = row.flowid
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
    width: 100%;
  }
</style>
