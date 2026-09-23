<template>
  <el-dialog
    title="风险信息"
    :visible.sync="dialogVisible"
    width="1000px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
    @close="close"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="right">
          <vab-query-form>
            <vab-query-form-left-panel :span="18">
              <el-form
                ref="form"
                checkable
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item>
                  <el-input
                    v-model="queryForm.risknumber"
                    clearable
                    placeholder="风险编号"
                    style="width: 140px; margin-right: 20px"
                  />
                  <el-form-item></el-form-item>
                  <el-input
                    v-model="queryForm.riskname"
                    clearable
                    placeholder="风险名称"
                    style="width: 140px; margin-right: 20px"
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
                <el-form-item>
                  <el-button
                    type="primary"
                    native-type="submit"
                    @click="resetQueryForm"
                  >
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </vab-query-form-left-panel>
            <vab-query-form-right-panel :span="6">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
          </vab-query-form>

          <el-table
            v-loading="listLoading"
            :data="list"
            ref="multipleTable"
            @select="handleSelection"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              align="center"
              label="风险编号"
              prop="risknumber"
            />
            <el-table-column align="center" label="风险名称" prop="riskname" />
            <el-table-column align="center" label="风险描述" prop="riskdes" />
            <el-table-column
              align="center"
              label="创建时间"
              prop="riskcreatedt"
            ></el-table-column>
          </el-table>

          <el-pagination
            background
            :current-page="queryForm.pageNo"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import { getCreationList } from '@/api/risk'
  import { UTCformat } from '@/utils'

  export default {
    data() {
      return {
        dialogVisible: false,
        dataTree: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          status: 6,
          pageNo: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
        listLoading: false,
        list: [],
        riskcatid: '',
      }
    },
    methods: {
      showEdit(id, type) {
        this.dialogVisible = true
        this.current = undefined
        this.riskcatid = id
        this.getExecutorList(this.riskcatid, type)
      },
      save() {
        if (!this.current) {
          this.$baseMessage('请选择风险信息', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.multipleSelection)
        this.dialogVisible = false
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.getExecutorList(this.riskcatid)
      },
      fetchData() {
        this.getExecutorList(this.riskcatid)
      },
      resetQueryForm() {
        this.queryForm = {
          risknumber: '',
          riskname: '',
          status: 6,
          pageNo: 1,
          pageSize: 20,
        }
        this.getExecutorList(this.riskcatid)
      },
      async getExecutorList(riskcatid, type = '') {
        this.listLoading = true
        this.queryForm.riskcatid =
          typeof riskcatid == 'string' || typeof riskcatid == 'number'
            ? riskcatid
            : ''
        const {
          data: {
            pageBean: { list, total },
          },
        } = await getCreationList({ ...this.queryForm, iscurrentversion: type })
        list.map((v) => {
          v.riskcreatedt = UTCformat(v.riskcreatedt)
          return v
        })
        this.list = list
        this.total = total

        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList(this.riskcatid)
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .lr-layout {
    display: flex;
  }

  // .lr-layout > .left {
  //   width: 200px;
  //   border-right: 1px solid ghostwhite;
  //   margin-right: 100px;
  //   padding-right: 10px;
  // }

  .lr-layout > .right {
    flex: 1;
  }
</style>
