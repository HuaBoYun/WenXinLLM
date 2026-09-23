<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      v-if="dialogFormVisible"
    >
      <vab-query-form>
        <vab-query-form-left-panel>
          <span></span>
        </vab-query-form-left-panel>
        <vab-query-form-right-panel>
          <el-button
            type="success"
            @click="handleZZOpenPerson('主评人')"
            v-if="!isActive"
          >
            添加主评人
          </el-button>
          <el-button
            type="success"
            @click="handleCCOpenPerson('参评人')"
            v-if="!isActive"
          >
            添加参评人
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-col :span="24">
        <el-divider>
          项目名称: {{ this.assessname }} &emsp;&emsp;&emsp;评价编号:
          {{ this.assessid }}
        </el-divider>
      </el-col>
      <el-table
        :data="tableData"
        ref="multipleTable"
        :row-key="getRowKeys"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          width="48"
          type="selection"
          :selectable="selectable"
          :reserve-selection="true"
        ></el-table-column>
        <el-table-column align="center" label="评价要素" prop="elementname" />
        <el-table-column
          align="center"
          label="适用性"
          prop="suitable"
          #default="{ row }"
        >
          <template>
            <el-checkbox
              v-model="row.suitable"
              @change="handleSuitableChange(row.suitable, row)"
              :disabled="isActive"
            >
              适用
            </el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="主评人" prop="realname" />
        <el-table-column align="center" label="参评人" prop="canpingrens">
          <template #default="{ row }">
            <el-button type="text" @click="openScoreModal(row)">
              {{
                row.canpingrens
                  .map(
                    (res) => res.realname + '(' + (res.assweight || 0) + '%)'
                  )
                  .toString()
              }}
            </el-button>
          </template>
        </el-table-column>
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
        <el-button @click="close">关 闭</el-button>
      </template>
    </el-dialog>
    <ZZSelectPerson ref="ZZSelectPerson" @fetchData="fetchData" />
    <CCSelectPerson ref="CCSelectPerson" @fetchData="fetchData" />
    <CCScoreModal ref="CCScoreModal" @fetchData="fetchData" />
  </div>
</template>

<script>
  import {
    getAuthorization2ModalData,
    updateShiYongXing,
    deletecanpingUsers,
  } from '@/api/internal/project'
  import CCScoreModal from '@/views/internal/evaluationManagement/components/CCScoreModal.vue'
  import CCSelectPerson from '@/views/internal/evaluationManagement/components/CCSelectPerson.vue'
  import ZZSelectPerson from '@/views/internal/evaluationManagement/components/ZZSelectPerson.vue'
  export default {
    name: 'AuthorizationModal1',
    components: { ZZSelectPerson, CCSelectPerson, CCScoreModal },
    data() {
      return {
        isActive: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 3,
        tableData: [],
        title: '评价发起人',
        dialogFormVisible: false,
        assId: '', // 用于请求授权列表
        orgId: '', // 用于请求授权列表
        assessname: '',
        assessid: '',
        select: [], //用于保存授权要素的id
        selectAll: [], //用于保存授权要素的信息以判断
        secrectLevelId: '',
      }
    },
    created() {},
    methods: {
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
          data: {
            pageBean,
            project: { assessname, assessid },
          },
        } = await getAuthorization2ModalData({
          ...this.queryForm,
          orgId: +this.orgId,
          assId: +this.assId,
        })

        this.tableData = pageBean.list.map((x) => {
          x.suitable = x.suitable === '1'
          return x
        })
        this.total = pageBean.total
        this.listLoading = false
        this.assessname = assessname
        this.assessid = assessid
        this.setCheckedRows() //回显已勾选的数据
      },
      showEdit(row, isActive) {
        this.dialogFormVisible = true
        this.assId = row.assId
        this.orgId = row.orgId
        this.secrectLevelId = row.secrectLevelId
        this.isActive = isActive
        this.fetchData()
      },
      handleSuitableChange(val, row) {
        console.log('val', val)
        console.log('row', row)
        updateShiYongXing({
          markId: row.assmarkid,
          type: String(Number(val)),
        }).then((res) => {
          // Success msg
        })
        if (!val) {
          deletecanpingUsers(row.assmarkid).then((res) => {
            console.log(res)
            if (res.code == 200) {
              this.fetchData()
            }
          })
        }
      },
      close() {
        this.dialogFormVisible = false
        this.select = []
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.assmarkid
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        selection.shift()
        this.select = selection.map((item) => item.assmarkid)
        this.selectAll = selection
        // console.log(this.selectAll)
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.tableData.forEach((item) => {
          this.select.forEach((id) => {
            if (item.assmarkid === id) {
              selectItem.push(item)
            }
          })
        })
        this.$refs.multipleTable.toggleRowSelection(selectItem)
      },
      handleZZOpenPerson(name) {
        if (this.select.length === 0) {
          this.$baseMessage(
            '请先选择评价要素',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        const info = {
          z: this.select.length > 0 ? this.select.toString() : '',
          name,
          orgId: this.orgId,
          secrectLevelId: this.secrectLevelId,
        }
        this.$refs['ZZSelectPerson'].showEdit(info)
      },
      handleCCOpenPerson(name) {
        if (this.select.length === 0) {
          this.$baseMessage(
            '请先选择评价要素',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        // console.log(this.select)
        const info = {
          z: this.select.length > 0 ? this.select.toString() : '',
          name,
          orgId: this.orgId,
          secrectLevelId: this.secrectLevelId,
        }
        this.$refs['CCSelectPerson'].showEdit(info)
      },
      openScoreModal(info) {
        if (this.isActive) {
          this.$refs['CCScoreModal'].showEdit(info, true)
        } else {
          this.$refs['CCScoreModal'].showEdit(info)
        }
      },
      //判断是否可选参评人
      selectable(row, index) {
        // console.log(row, 'row', index, 'index')
        // console.log(row.suitable, 'rowsuitable')
        if (row.suitable) {
          return true
        } else {
          return false
        }
      },
    },
  }
</script>
<style scoped lang="scss">
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
