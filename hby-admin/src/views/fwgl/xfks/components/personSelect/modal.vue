<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal-append-to-body="false"
    :append-to-body="true"
    width="1200px"
    :close-on-click-modal="false"
    @close="close"
    v-if="dialogVisible"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <el-tree
            ref="tree"
            :check-strictly="true"
            :data="dataTree"
            default-expand-all
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
          />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
          </vab-query-form>

          <el-table
            ref="multipleTable"
            :data="list"
            :row-key="getRowKeys"
            @selection-change="handleSelection"
            style="width: 100%"
          >
            <el-table-column
              type="selection"
              width="55"
              :reserve-selection="true"
            ></el-table-column>
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
  import { sendPerson, getDefaultId } from '@/api/fwgl/ksgl'
  export default {
    props: ['examId'],
    components: {},
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,

        dialogVisible: false,
        list: [],
        dataTree: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        reviewType: '',
        select: [],
      }
    },

    methods: {
      async showEdit(e) {
        if (e) {
          this.reviewType = e
        }

        this.dialogVisible = true
        this.current = undefined
        await this.getExecutorTree()
        await this.getExecutorList()
        this.$nextTick(async () => {
          const res = await getDefaultId({
            examId: this.examId,
          })
          if (res.data) {
            this.select = res.data.map((res1) => {
              return res1.staffId
            })

            this.setCheckedRows() //回显已勾选的数据
          }
        })
      },
      async getExecutorTree() {
        const res = await findOrganizationByTreeAllss(this.queryForm)
        this.dataTree = res
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await selectPerson(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.queryForm.pageSize = 20
        this.queryForm.pageNumber = 1
        this.getExecutorList()
        this.setCheckedRows() //回显已勾选的数据
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
        this.setCheckedRows() //回显已勾选的数据
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
        this.setCheckedRows() //回显已勾选的数据
      },

      // 3、勾选列表操作
      handleSelection(selection) {
        // selection.shift()
        this.select = selection.map((item) => item.staffid)
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.staffid
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.list.forEach((item) => {
          this.select.forEach((id) => {
            if (item.staffid === id) {
              selectItem.push(item)
            }
          })
        })

        selectItem.forEach((res) => {
          this.$refs.multipleTable.toggleRowSelection(res, true)
        })
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        if (!this.select) {
          this.$baseMessage(
            '请选择下发人员！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        sendPerson({
          examId: this.examId,
          staffId: this.select.toString(),
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('下发成功', 'success', 'vab-hey-message-success')
          }
          this.$emit('fetchData')
        })
        // 选择 复核人
        this.dialogVisible = false
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.select = []
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  // ::v-deep thead {
  //   .el-table-column--selection {
  //     .el-checkbox__inner {
  //       display: none !important;
  //     }
  //   }
  // }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
