<template>
  <el-dialog
    :close-on-click-modal="false"
    size="800px"
    :title="title"
    :visible.sync="dialogFormVisible"
  >
    <div class="system-log-container lr-layout">
      <div class="left">
        <dep-tree :loaded-select="false" @select="handleNodeClick" />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-left-panel :span="16">
            <el-select
              v-model="userId"
              multiple
              style="width: 400px"
              @remove-tag="removeTag"
            >
              <el-option
                v-for="item in userList"
                :key="item.staffid"
                :label="item.realname"
                :value="item.staffid"
              ></el-option>
            </el-select>
          </vab-query-form-left-panel>
          <vab-query-form-right-panel :span="8">
            <el-button native-type="submit" type="primary" @click="save()">
              确定
            </el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          ref="multipleTable"
          :data="list"
          highlight-current-row
          @selection-change="handleSelectionChange"
          @select="onSelect"
          @select-all="selectAll"
        >
          <el-table-column align="center" type="selection" width="60" />
          <el-table-column
            align="center"
            label="用户真实姓名"
            prop="realname"
          />
          <el-table-column align="center" label="所属部门" prop="orgName" />
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
      <el-button @click="close">取消</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { themeUsersList, themeUserSave } from '@/api/setting/themeRepertory'
import DepTree from '@/views/setting/org/components/DepTree'
export default {
  name: 'Send',
  components: { DepTree },
  data() {
    return {
      queryForm: {
        id: '',
        start: 0,
        pageNo: 1,
        pageSize: 20,
      },
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      title: '下发',
      dialogFormVisible: false,
      list: [],
      defaultProps: {
        children: 'children',
        label: 'label',
      },
      orgid: '',
      data: [],
      userId: [],
      userList: [],
      multipleSelection: [],
      pid: -1,
    }
  },
  created() {},
  methods: {
    handleSelectionChange(val) {
      // console.warn('handleSelectionChange', val)
      // this.multipleSelection = val
    },
    setSelection(list) {
      this.$nextTick(() => {
        list.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(
            this.list.find((item) => {
              return row.staffid == item.staffid
            }),
            true
          )
        })
      })
    },
    showEdit(row) {
      this.dialogFormVisible = true
      this.queryForm.id = row.ids
      this.pid = row.pid
      this.fetchData()
    },
    close() {
      this.dialogFormVisible = false
      this.queryForm.pid = ''
      this.userList = []
      this.userId = []
      this.pid = -1
    },
    async fetchData() {
      const {
        data: { tlist, totalRecord },
      } = await themeUsersList(this.queryForm)
      this.list = tlist
      this.total = totalRecord
    },
    save() {
      if (this.userList.length == 0) {
        this.$message.error('请选择')
        return
      } 
      const staffids = this.userList.map((i) => i.staffid).join(',')

      themeUserSave({
        staffIds: staffids,
        pageIds: this.queryForm.id,
        fatherId: !this.pid ? '' : this.pid,
        type: !this.pid ? 1 : 2,
      }).then((res) => {
        if (res.code == '1') {
          this.close()
          this.$message.success('成功')
        } else {
          this.$message.error('操作失败')
        }
      })
    },
    //禁用全选框
    selectAll() {
      this.$refs.multipleTable.clearSelection()
    },
    handleNodeClick(data) {
      this.queryForm.pid = data.id
      this.fetchData()
    },
    //两数组取不相同
    getNewArr(a, b) {
      const arr = [...a, ...b]
      const newArr = arr.filter((item) => {
        return !(a.includes(item) && b.includes(item))
      })
      return newArr
    },
    // 选择
    onSelect(rows, row) {
      let selected = rows.length && rows.indexOf(row) !== -1

      let list = this.userList || []
      if (selected) {
        list.push(row)
      } else {
        list = list.filter((item) => item.staffid !== row.staffid)
      }
      this.userId = list.map((item) => item.staffid)
      this.userList = list
      this.$forceUpdate()
    },
    // 移除
    async removeTag(e) {
      let list = this.userList
      let id = this.userId
      list = await list.filter((item) => item && item.staffid != e)
      id = await id.filter((item) => item !== e)
      this.userList = list
      this.userId = id
      this.$forceUpdate()
      await this.$refs.multipleTable.clearSelection()
      await this.setSelection(list)
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
