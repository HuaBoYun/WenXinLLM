<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <div class="">
      <vab-query-form>
        <el-card shadow="never">
          <vab-query-form-top-panel :span="24">
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm.papername"
                  clearable
                  placeholder="论文名称"
                />
              </el-form-item>

              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="getExecutorList"
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
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="序号" type="index" width="50" />
        <el-table-column align="center" label="论文名称" prop="papername">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.papername }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="填报单位"
          prop="tbrgname"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="撰写人" prop="zxrname" />
        <el-table-column
          align="center"
          label="备注"
          prop="remarks"
          show-overflow-tooltip
        />
      </el-table>
      <Edit ref="edit" />
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
  </el-dialog>
</template>
<script>
  import { getLwpxSelectList } from '@/oapi/audit/lwpy'
  import { xmpysbList } from '@/oapi/audit/xmpy'
  import Edit from '@/views/oilAudit/lwpy/components/lwsbEdit.vue'
  export default {
    components: { Edit },
    name: 'ExecutorOptions',
    props: {
      isCheckout: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        data: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          papername: '',
        },
        current: undefined,
        select: [],
        noIds: [],
      }
    },
    created() {},
    methods: {
      show() {
        this.dialogFormVisible = true
        this.getExecutorList()
      },
      resetQueryForm() {
        this.queryForm = {
          papername: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.getExecutorList()
      },
      async getExecutorList() {
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getLwpxSelectList({ ...this.queryForm })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        this.setCheckedRows()
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
      confirm() {
        if (this.select && this.select.length > 0) {
          this.$emit('selected', this.select)
          this.dialogFormVisible = false
        } else {
          this.$baseMessage('请选择论文！', 'error', 'vab-hey-message-error')
        }
      },
      close() {
        this.dialogFormVisible = false
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
        }
        this.list = []
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.perid == row.perid)
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
            if (row && !this.select.some((x) => x.perid == row.perid)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.perid == row.perid)
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
                return row.perid == item.perid
              }),
              true
            )
          })
        })
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit('detail', row)
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 20%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 80%;
  }
</style>
