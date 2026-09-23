<!--
 * @Date: 2022-03-31 15:03:53
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-07 11:18:00
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/BorrowHistory.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
    :append-to-body="true"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.userName"
              clearable
              placeholder="用户名"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.realName"
              clearable
              placeholder="真实姓名"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.companyName"
              clearable
              placeholder="公司名称"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.deptName"
              clearable
              placeholder="部门名称"
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
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
      <vab-query-form-left-panel></vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      ref="multipleTable"
      :row-key="getRowKeys"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        width="48"
        type="selection"
        :reserve-selection="true"
      ></el-table-column>
      <el-table-column
        align="center"
        label="用户名"
        prop="username"
      ></el-table-column>
      <el-table-column align="center" label="用户真实姓名" prop="realname" />
      <el-table-column align="center" label="手机" prop="miblephone" />
      <el-table-column align="center" label="固定电话" prop="fixedphone" />
      <el-table-column align="center" label="电子邮件" prop="email" />
      <el-table-column
        align="center"
        label="备注"
        prop="memo"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="所属部门"
        prop="orgname"
        show-overflow-tooltip
      />
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
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
    <ProcessList ref="process" @fetchData="fetchData" />
  </el-dialog>
</template>
<script>
  import { getGrantSystemRightStaffList, qxuser } from '@/api/setting/auth'
  import { getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'

  export default {
    name: 'BorrowHistory',
    components: { ProcessList },
    props: {
      fromType: {
        type: Number,
        default: 0,
      },
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          roleId: undefined,
          userName: undefined,
          realName: undefined,
          companyName: undefined,
          deptName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        dialogVisible: false,
        title: '取消用户授权',
        select: [],
        selectList: [],
        requireValuedata: false,
      }
    },
    created() {
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      showEdit(row) {
        this.dialogVisible = true
        // this.queryForm.roleid = 247386
        this.queryForm.roleId = row.rid
        this.queryForm.pageSize = 100
        this.fetchData()
      },

      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getGrantSystemRightStaffList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },

      close() {
        this.dialogVisible = false
        this.select = []
        this.selectList = []
      },
      resetQueryForm() {
        this.queryForm.userName = undefined
        this.queryForm.realName = undefined
        this.queryForm.companyName = undefined
        this.queryForm.deptName = undefined
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.staffid
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        this.select = selection.map((item) => item.staffid)
        this.selectList = selection
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
        this.$refs.multipleTable.toggleRowSelection(selectItem)
      },
      async handleSubmit() {
        if (this.fromType == 1) {
          this.$emit('selected', this.selectList)
          this.dialogVisible = false
          return
        }
        if (this.select.length == 0) {
          this.$message.error('请选择人员')
          return
        }
        const { msg, data } = await qxuser({
          roleid: this.queryForm.roleId,
          staffids: this.select.join(','),
        })
        // 流程校验
        if (this.requireValuedata) {
          //  查询当前是否有流程
          getFlowList({
            targetId: data.recordId,
            targetType: 'role',
            operationType: 9,
          }).then((res) => {
            if (res.data == 0) {
              // 可以提交流程
              this.$refs['process'].save(220, data.recordId)
              this.$baseMessage(
                '审批流程提交成功,请等待审批',
                'success',
                'vab-hey-message-success'
              )
              this.close()
            } else {
              // 不可以提交流程
              this.$baseMessage(
                '当前用户流程已存在,请先走审批流程',
                'error',
                'vab-hey-message-error'
              )
              return
            }
          })
        } else {
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('selected')
            this.dialogVisible = false
          }
        }
      },
    },
  }
</script>
s
