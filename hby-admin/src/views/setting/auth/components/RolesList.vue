<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <div>
      <!-- <vab-query-form> -->
      <!-- <el-select
          v-model="userId"
          multiple
          style="width: 600px"
          @remove-tag="removeTag"
        >
          <el-option
            v-for="item in userList"
            :key="item.rid"
            :label="item.rname"
            :value="item.rid"
          ></el-option>
        </el-select> -->
      <!-- </vab-query-form> -->
      <vab-query-form>
        <vab-query-form-left-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-input
                v-model="queryForm.rname"
                clearable
                placeholder="角色名称"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
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
        </vab-query-form-left-panel>
        <vab-query-form-right-panel>
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        ref="multipleTable"
        highlight-current-row
        @select="onSelect"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="角色名称"
          prop="rname"
        ></el-table-column>
        <!-- <el-table-column
          align="center"
          label="公司名称"
          prop="orgName"
          :show-overflow-tooltip="true"
        ></el-table-column> -->
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
  </el-dialog>
</template>
<script>
  import { roleList } from '@/api/setting/auth'
  export default {
    name: 'roleList',
    props: {},
    components: {},
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          jobname: '',
          orgIds: '',
          pageNumber: 1,
          pageSize: 20,
        },
        userId: [],
        userList: [],
        current: undefined,
      }
    },
    created() {},
    methods: {
      async show(orgIds, roleList) {
        this.queryForm.orgIds = orgIds
        await this.queryData()
        if (roleList) {
          console.log('roleList', roleList)
          this.userList = roleList
          this.userId = roleList.map((item) => item.rid)
          console.log('this.userId', this.userId)
          await this.setSelection(roleList)
        }

        this.dialogFormVisible = true
      },
      handleDetail(row) {
        this.$refs['detail'].showDetail(row)
      },
      resetQueryForm() {
        this.queryForm.rname = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async queryData() {
        await this.fetchData()
        await this.setSelection(this.userList)
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await roleList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleCurrentChange2(val) {
        this.current = val
      },
      confirm() {
        if (this.userList.length == 0) {
          this.$baseMessage('请选择职位！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.userList)
        this.userList = []
        this.userId = []
        this.dialogFormVisible = false
      },
      setSelection(list) {
        this.$nextTick(() => {
          list.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.rid == item.rid
              }),
              true
            )
          })
        })
      },
      onSelect(rows, row) {
        console.log('ss', row)
        let selected = rows.length && rows.indexOf(row) !== -1
        console.log(selected) // true就是选中，0或者false是取消选中
        let list = this.userList || []
        if (selected) {
          list.push({
            ...row,
          })
        } else {
          list = list.filter((item) => item.rid !== row.rid)
        }
        const uniqueObjectsArray = list.filter(
          (obj, index, self) =>
            index === self.findIndex((t) => t.rid == obj.rid)
        )
        console.log('uniqueObjectsArray', uniqueObjectsArray)

        this.userId = uniqueObjectsArray.map((item) => item.rid)
        this.userList = uniqueObjectsArray
        this.$forceUpdate()
      },
      async removeTag(e) {
        let list = this.userList
        let id = this.userId
        list = await list.filter((item) => item && item.rid != e)
        id = await id.filter((item) => item !== e)
        this.userList = list
        this.userId = id
        this.$forceUpdate()
        await this.$refs.multipleTable.clearSelection()
        await this.setSelection(list)
      },
      close() {
        this.userId = []
        this.userList = []
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped></style>
