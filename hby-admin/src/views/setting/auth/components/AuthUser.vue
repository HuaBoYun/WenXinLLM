<template>
  <el-drawer
    :before-close="close"
    size="1000px"
    :title="title"
    :visible.sync="dialogFormVisible"
  >
    <div class="system-log-container lr-layout">
      <div class="left">
        <dep-tree :loaded-select="false" @select="handleNodeClick" />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button
              native-type="submit"
              type="primary"
              @click="handleRight()"
            >
              授权
            </el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table :data="list" @selection-change="handleSelectionChange">
          <el-table-column align="center" type="selection" width="60" />
          <el-table-column
            align="center"
            label="用户真实姓名"
            prop="realname"
          />
          <el-table-column align="center" label="所属部门" prop="orgName" />
          <el-table-column
            v-if="false"
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="$refs.cancelAuthUser.showEdit(row)"
              >
                授权
              </el-button>
            </template>
          </el-table-column>
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
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-drawer>
</template>

<script>
  import { ztNewlist, saveNewAccBookManage } from '@/api/setting/auth'
  import DepTree from '@/views/setting/org/components/DepTree'
  export default {
    name: 'AuthUser',
    components: { DepTree },
    data() {
      return {
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
        },
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '用户信息',
        dialogFormVisible: false,
        list: [],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        orgid: '',
        data: [],
        multipleSelection: [],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.dialogFormVisible = true
        this.orgid = row.pid
        // this.queryForm.pid = row.pid
        this.queryForm.acctid = row.acctid
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
        this.queryForm.pid = ''
      },
      async fetchData() {
        const {
          data: { tlist, totalRecord },
        } = await ztNewlist(this.queryForm)
        this.list = tlist
        this.total = totalRecord
      },
      handleSelectionChange(val) {
        console.warn('handleSelectionChange', val)
        this.multipleSelection = val
      },
      handleRight() {
        //
        const staffid = this.multipleSelection.map((i) => i.staffid).join(',')
        if (!staffid.length) {
          this.$message.error('请选择')
          return
        }
        saveNewAccBookManage({
          orgid: this.orgid,
          acctid: this.queryForm.acctid,
          staffid,
        }).then((res) => {
          console.log('saveNewAccBookManage', res)
          if (res.code === 1) {
            this.$message.success('已选定')
          } else {
            this.$message.error('操作失败')
          }
        })
      },
      handleNodeClick(data) {
        console.log(data)
        this.queryForm.pid = data.id
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
    width: calc(100% - 200px);
  }
</style>
