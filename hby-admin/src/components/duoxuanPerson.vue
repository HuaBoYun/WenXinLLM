<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="modal"
    width="1200px"
    :close-on-click-modal="false"
    append-to-body
    @close="handleDialogClose"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <DeepTree @select="handleNodeClick" v-if="!noOwnCompany" />
          <DepTreeNoOwnCompany @select="handleNodeClick" v-if="noOwnCompany" />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="handleCancel">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
            <div v-if="select.length > 0">
              <el-tag type="info" v-for="item in select" :key="item.staffid">
                {{ item.realname }}
              </el-tag>
            </div>
            <vab-query-form-left-panel>
              <el-input
                v-model="queryForm.realname"
                placeholder="请输入用户名"
                clearable
                style="width: 50%; margin-right: 10px"
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
            ref="multipleTable"
            style="width: 100%"
            @select="handleSelection"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              label="用户真实名"
              prop="realname"
            ></el-table-column>
            <el-table-column prop="orgname" label="所属部门"></el-table-column>
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
    </div>
  </el-dialog>
</template>
<script>
  import {
    findOrganizationByTreeAllss,
    selectPerson,
  } from '@/api/audit/project'
  import DeepTree from '@/components/DepTree.vue'
  import DepTreeNoOwnCompany from '@/components/DepTreeNoOwnCompany.vue'

  export default {
    props: {
      modal: {
        type: Boolean,
        default: false,
      },
      multiple: {
        type: Boolean,
        default: false,
      },
      formSecrectId: {
        type: String,
        default: '',
      },
      noOwnCompany: {
        type: Boolean,
        default: false,
      },
    },
    components: { DeepTree, DepTreeNoOwnCompany },
    data() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      const curOrgId = userInfo.currentOrg.orgid
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        curOrgId,
        dialogVisible: false,
        list: [],
        dataTree: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          isLeaf: (data) => !data.isParent,
        },
        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
          realname: undefined,
        },
        current: undefined,
        reviewType: '',
        select: [],
      }
    },
    methods: {
      showEdit(e) {
        if (e) {
          this.reviewType = e
        }
        this.dialogVisible = true
        this.current = undefined
        this.realname = undefined
        this.getExecutorList()
        this.select = []
      },

      async getExecutorList() {
        if (this.queryForm.realname) {
          this.queryForm.pageSize = 20
          this.queryForm.pageNumber = 1
        }
        this.listLoading = true
        this.queryForm.formSecrectId = this.formSecrectId

        // if (this.formSecrectId) {
        // } else {
        //   delete this.queryForm.formSecrectId
        // }
        const {
          data: { list, total },
        } = await selectPerson(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.queryForm.pageSize = 20
        this.queryForm.pageNumber = 1
        const selectedRows = this.select
        this.getExecutorList().then(() => {
          selectedRows.forEach((row) => {
            const matchedRow = this.list.find(
              (item) => item.staffid === row.staffid
            )
            if (matchedRow) {
              this.$refs.multipleTable.toggleRowSelection(matchedRow, true)
            }
          })
        })
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        if (this.select.length == 0) {
          this.$baseMessage('请选择人员！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('projectManage', this.select)
        this.handleDialogClose()
        this.dialogVisible = false
      },
      reset() {
        this.queryForm.realname = ''
        const selectedRows = [...this.select] // 保存当前已选择的项目
        this.getExecutorList().then(() => {
          // 重新勾选已选择的行
          this.$refs.multipleTable.clearSelection() // 先清空所有选择
          selectedRows.forEach((row) => {
            const matchedRow = this.list.find(
              (item) => item.staffid === row.staffid
            )
            if (matchedRow) {
              this.$refs.multipleTable.toggleRowSelection(matchedRow, true)
            }
          })
        })
      },
      handleSelection(val, row) {
        if (!this.multiple) {
          // 单选模式
          if (val.length > 1) {
            let del = val.shift()
            this.$refs.multipleTable.toggleRowSelection(del, false)
          }

          // 单选模式下，如果选中了新的行，清空之前的选择
          if (val.includes(row)) {
            this.select = [row]
          } else {
            this.select = []
          }
        } else {
          // 多选模式
          // 如果是选中操作
          if (val.includes(row)) {
            // 确保不重复添加
            if (!this.select.some((item) => item.staffid === row.staffid)) {
              this.select.push(row)
            }
          } else {
            // 如果是取消选中操作
            this.select = this.select.filter(
              (item) => item.staffid !== row.staffid
            )
          }
        }
      },
      /**
       * @description: 处理弹窗关闭事件，清空相关信息
       * @return {*}
       */
      handleDialogClose() {
        // 清空选中的人员
        this.select = []
        // 清空查询表单
        this.queryForm.realname = undefined
        // this.queryForm.orgid = undefined
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        // 清空表格选择
        if (this.$refs.multipleTable) {
          this.$refs.multipleTable.clearSelection()
        }
        // 清空列表数据
        this.list = []
        this.total = 0
        // 清空其他相关数据
        this.current = undefined
        this.reviewType = ''
      },
      /**
       * @description: 处理取消按钮点击事件
       * @return {*}
       */
      handleCancel() {
        this.dialogVisible = false
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

  .lr-layout > .left {
    width: 350px;
    border-right: 1px solid ghostwhite;
    margin-right: 30px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
