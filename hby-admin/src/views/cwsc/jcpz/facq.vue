<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <fa-tree @select="handleSelect" />
      </div>
      <div class="right">
        <el-card shadow="never" class="secondCard">
          <el-table :data="list" style="width: 100%">
            <el-table-column prop="recordname" label="名称" align="center" />
            <el-table-column prop="status" label="状态" align="center">
              <template #default="{ row }">
                <div class="ing" v-if="row.iscompleted === 1">进行中</div>
                <div class="done" v-if="row.iscompleted === 2">已完成</div>
                <div class="danger" v-if="row.iscompleted === 0">未开始</div>
                <!-- </el-tag> -->
              </template>
            </el-table-column>
            <el-table-column prop="startdate" label="开始时间" align="center" />
            <el-table-column prop="enddate" label="结束时间" align="center" />
            <el-table-column align="center" label="操作" width="120">
              <template #default="{ row }">
                <el-button type="text" @click="handleStop(row)">停止</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
  import { userList, exportUserInfoList } from '@/api/setting/auth'
  import FaTree from './components/faTree.vue'
  import { hasAuth } from '@/utils'
  import { getFaGatherStatus, stopFaGatherSub } from '@/api/cwsc'

  export default {
    name: 'Facq',
    components: {
      FaTree,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          isAll: 0,
          realname: '',
          username: '',
          orgid: '',
          pageNumber: 1,
          pageSize: 10,
        },
      }
    },
    created() {
      // this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      resetQueryForm() {
        this.queryForm = {
          isAll: 0,
          realname: '',
          username: '',
          pageNumber: 1,
          pageSize: 10,
        }
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await userList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
          isAll: this.queryForm.isAll ? 1 : 0,
        })
        this.list = list.map((i) => {
          return {
            ...i,
            orgname: i.orgname,
          }
        })
        this.total = total
        this.listLoading = false
      },

      handleAdd() {
        this.$refs['edit'].showEdit({}, false)
      },
      handlePwdEdit(row) {
        this.$refs['editpwd'].showEdit(row)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      leftList(org) {
        // console.log(org)
        this.queryForm.orgid = org.id
        this.fetchData()
      },
      handleSelect(item) {
        this.list = []
        this.renderId = item.fid
        getFaGatherStatus({ fid: item.fid }).then((res) => {
          const { data } = res
          if (data.length > 0) {
            this.list = data
          }
        })
      },
      handleStop(row) {
        stopFaGatherSub({ recordid: row.recordid }).then((res) => {
          this.$message.success('停止成功')
          getFaGatherStatus({ fid: this.renderId }).then((res) => {
            const { data } = res
            if (data.length > 0) {
              this.list = data
            }
          })
        })
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 30%;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .right {
    flex: 1;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
  .ing {
    color: #409eff;
    &::before {
      content: '';
      display: inline-block;
      width: 10px;
      height: 10px;
      background-color: #409eff;
      border-radius: 50%;
      margin-right: 5px;
    }
  }
  .done {
    color: #67c23a;
    &::before {
      content: '';
      display: inline-block;
      width: 10px;
      height: 10px;
      background-color: #67c23a;
      border-radius: 50%;
      margin-right: 5px;
    }
  }
  .wait {
    color: #909399;
    &::before {
      content: '';
      display: inline-block;
      width: 10px;
      height: 10px;
      background-color: #909399;
      border-radius: 50%;
      margin-right: 5px;
    }
  }
  .danger {
    color: #f56c6c;
    &::before {
      content: '';
      display: inline-block;
      width: 10px;
      height: 10px;
      background-color: #f56c6c;
      border-radius: 50%;
      margin-right: 5px;
    }
  }
</style>
