<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    append-to-body
  >
    <div class="lr-layout">
      <!-- <div class="left" :style="{ display: 'none' }">
        <type-tree @select="fetchData" />
      </div> -->
      <div class="right">
        <el-table id="heat-table2" v-loading="listLoading" :data="list">
          <el-table-column
            align="center"
            class-name="is-group"
            prop="rilevel"
            width="200"
          />
          <el-table-column align="center" label="1(很低)" prop="poss1">
            <template #default="{ row }">
              <div
                :class="calCount(row.riskLevelMapping.poss1)"
                @click="
                  row.riskLevelMapping.count1 > 0 && handlePlanDetail1(row)
                "
              >
                {{
                  row.riskLevelMapping.count1 > 0
                    ? row.riskLevelMapping.count1
                    : ''
                }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="2(较低)" prop="poss2">
            <template #default="{ row }">
              <div
                :class="calCount(row.riskLevelMapping.poss2)"
                @click="
                  row.riskLevelMapping.count2 > 0 && handlePlanDetail2(row)
                "
              >
                {{
                  row.riskLevelMapping.count2 > 0
                    ? row.riskLevelMapping.count2
                    : ''
                }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="3(中等)" prop="poss3">
            <template #default="{ row }">
              <div
                :class="calCount(row.riskLevelMapping.poss3)"
                @click="
                  row.riskLevelMapping.count3 > 0 && handlePlanDetail3(row)
                "
              >
                {{
                  row.riskLevelMapping.count3 > 0
                    ? row.riskLevelMapping.count3
                    : ''
                }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="4(较高)" prop="poss4">
            <template #default="{ row }">
              <div
                :class="calCount(row.riskLevelMapping.poss4)"
                @click="
                  row.riskLevelMapping.count4 > 0 && handlePlanDetail4(row)
                "
              >
                {{
                  row.riskLevelMapping.count4 > 0
                    ? row.riskLevelMapping.count4
                    : ''
                }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="5(很高)" prop="poss5">
            <template #default="{ row }">
              <div
                :class="calCount(row.riskLevelMapping.poss5)"
                @click="
                  row.riskLevelMapping.count5 > 0 && handlePlanDetail5(row)
                "
              >
                {{
                  row.riskLevelMapping.count5 > 0
                    ? row.riskLevelMapping.count5
                    : ''
                }}
              </div>
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-pagination
          background
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        /> -->
      </div>
    </div>
    <TaskEdit ref="edit" />
  </el-dialog>
</template>
<script>
  import { pgjgRtList } from '@/api/risk/riskEvents'
  import { doDelete } from '@/api/table'
  import TypeTree from './typeTree.vue'
  import TaskEdit from './TaskEdit.vue'

  export default {
    name: 'HeatMap',
    components: { TypeTree, TaskEdit },
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        queryForm: {
          planId: '',
          riskcatid: '',
          pageNo: 1,
          pageSize: 10,
        },
        list: [
          { name: '5(很高)' },
          { name: '4(较高)' },
          { name: '3(中等)' },
          { name: '2(较低)' },
          { name: '1(很低)' },
        ],
        listLoading: false,
        dialogFormVisible: false,
      }
    },
    // created() {
    //   this.fetchData()
    // },
    methods: {
      calCount(poss) {
        if (poss == 1) {
          return 'calCount1'
        } else if (poss == 2) {
          return 'calCount2'
        } else if (poss == 3) {
          return 'calCount3'
        } else if (poss == 4) {
          return 'calCount4'
        } else if (poss == 5) {
          return 'calCount5'
        } else {
          return 'calCount1'
        }
      },
      showHeatMap(row) {
        this.dialogFormVisible = true
        this.queryForm.planId = row.assplanid
        this.queryData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData(row) {
        this.listLoading = true
        if (row && row.riskcatid) {
          this.queryForm.riskcatid = row.riskcatid
        }
        const {
          data: { infludegrees, total },
        } = await pgjgRtList(this.queryForm)

        console.log('records', infludegrees)

        this.list = infludegrees.reverse()
        this.total = total || 0
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handlePreview() {},
      handleReport() {},
      handleExport() {},
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      close() {
        this.dialogFormVisible = false
      },
      handlePlanDetail1(row) {
        if (this.queryForm.planId) {
          this.$refs['edit'].showEdit({
            assplanid: this.queryForm.planId,
            riskids: row.riskLevelMapping.risk1Ids,
          })
        }
      },
      handlePlanDetail2(row) {
        if (this.queryForm.planId) {
          this.$refs['edit'].showEdit({
            assplanid: this.queryForm.planId,
            riskids: row.riskLevelMapping.risk2Ids,
          })
        }
      },
      handlePlanDetail3(row) {
        if (this.queryForm.planId) {
          this.$refs['edit'].showEdit({
            assplanid: this.queryForm.planId,
            riskids: row.riskLevelMapping.risk3Ids,
          })
        }
      },
      handlePlanDetail4(row) {
        if (this.queryForm.planId) {
          this.$refs['edit'].showEdit({
            assplanid: this.queryForm.planId,
            riskids: row.riskLevelMapping.risk4Ids,
          })
        }
      },
      handlePlanDetail5(row) {
        if (this.queryForm.planId) {
          this.$refs['edit'].showEdit({
            assplanid: this.queryForm.planId,
            riskids: row.riskLevelMapping.risk5Ids,
          })
        }
      },
    },
  }
</script>
<style lang="scss">
  // .el-table thead.is-group th.el-table__cell {
  //   background: #fff;
  // }

  #heat-table2 thead tr:first-of-type th:first-of-type:before {
    content: '发生频率';
    text-align: center;
    position: absolute;
    width: 195px;
    height: 1px;
    bottom: 30px;
    right: 0;
  }

  #heat-table2 thead tr:first-of-type th:first-of-type:after {
    content: '严重程度';
    text-align: center;
    position: absolute;
    width: 73px;
    top: 20px;
    left: 0;
  }

  #heat-table2 thead tr:first-of-type th:first-of-type .cell {
    position: absolute;
    top: 0;
    left: 0;
    width: 152px;
    height: 1px;
    background-color: #ebeef5;
    display: block;
    text-align: center;
    transform: rotate(16deg);
    transform-origin: top left;
    -ms-transform: rotate(16deg);
    -ms-transform-origin: top left;
    -webkit-transform: rotate(16deg);
    -webkit-transform-origin: top left;
  }
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
    width: 100%;
  }

  .calCount1 {
    position: relative;
    cursor: pointer;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #52ffb7;
    }
  }
  .calCount2 {
    cursor: pointer;
    position: relative;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #33d73b;
    }
  }
  .calCount3 {
    cursor: pointer;
    position: relative;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ffb500;
    }
  }
  .calCount4 {
    cursor: pointer;
    position: relative;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ff7f00;
    }
  }
  .calCount5 {
    cursor: pointer;
    position: relative;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #e92129;
    }
  }
</style>
