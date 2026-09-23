<template>
  <div>
    <el-row :gutter="10">
      <el-col :lg="24" :md="24" :sm="24">
        <el-tabs type="border-card" v-model="activeName1">
          <el-tab-pane
            :label="tabLabel1"
            name="first"
            style="overflow-y: scroll"
          >
            <vab-query-form>
              <el-card shadow="never">
                <vab-query-form-left-panel :span="24">
                  <el-form
                    ref="form"
                    checkable
                    :inline="true"
                    label-width="0"
                    :model="queryForm"
                    @submit.native.prevent
                  >
                    <el-form-item>
                      <el-select
                        v-model="queryForm.distributionType"
                        placeholder="请选择类型"
                        clearable
                      >
                        <el-option
                          v-for="item in typeData"
                          :key="item.textValue"
                          :label="item.textName"
                          :value="item.textValue"
                        ></el-option>
                      </el-select>
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
                  </el-form>
                </vab-query-form-left-panel>
                <vab-query-form-right-panel :span="24">
                  <el-button type="primary" @click="allSure">
                    批量确认
                  </el-button>
                </vab-query-form-right-panel>
              </el-card>
            </vab-query-form>
            <el-table
              border
              :data="list"
              style="width: 100%"
              @select-all="handleSelectAll"
              @select="handleSelection"
              ref="multipleTable"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column
                align="center"
                label="序号"
                width="60"
                type="index"
              ></el-table-column>
              <el-table-column
                align="center"
                label="通知内容"
                prop="distributionTitle"
              ></el-table-column>
              <el-table-column
                align="center"
                label="类型"
                prop="distributionType"
              >
                <template #default="{ row }">
                  <span>
                    {{ type[row.distributionType] }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="下发人"
                prop="createStaffName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="下发时间"
                prop="createTime"
                show-overflow-tooltip
                :formatter="formatDate"
              />

              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleEdit(row)">
                    详情
                  </el-button>
                  <el-button type="text" @click="handleOK(row)">
                    确认接收
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
          </el-tab-pane>
          <el-tab-pane
            :label="tabLabel2"
            name="second"
            style="overflow-y: scroll"
          >
            <vab-query-form>
              <el-card shadow="never">
                <vab-query-form-left-panel :span="24">
                  <el-form
                    ref="form"
                    checkable
                    :inline="true"
                    label-width="0"
                    :model="queryForm2"
                    @submit.native.prevent
                  >
                    <el-form-item>
                      <el-select
                        v-model="queryForm2.distributionType"
                        placeholder="请选择类型"
                        clearable
                      >
                        <el-option
                          v-for="item in typeData"
                          :key="item.textValue"
                          :label="item.textName"
                          :value="item.textValue"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                    <el-form-item>
                      <el-button
                        icon="el-icon-search"
                        native-type="submit"
                        type="primary"
                        @click="fetchData2"
                      >
                        查询
                      </el-button>
                    </el-form-item>
                  </el-form>
                </vab-query-form-left-panel>
              </el-card>
            </vab-query-form>
            <el-table border :data="list2" style="width: 100%">
              <el-table-column
                align="center"
                label="序号"
                width="60"
                type="index"
              ></el-table-column>
              <el-table-column
                align="center"
                label="通知内容"
                prop="distributionTitle"
              ></el-table-column>
              <el-table-column
                align="center"
                label="类型"
                prop="distributionType"
              >
                <template #default="{ row }">
                  <span>
                    {{ type[row.distributionType] }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="下发人"
                prop="createStaffName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="下发时间"
                prop="createTime"
                show-overflow-tooltip
                :formatter="formatDate"
              />

              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleEdit(row)">
                    详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              background
              :current-page="queryForm2.pageNumber"
              :layout="layout"
              :page-size="queryForm2.pageSize"
              :total="total2"
              @current-change="handleCurrentChange2"
              @size-change="handleSizeChange2"
            />
          </el-tab-pane>
          <el-tab-pane
            :label="tabLabel3"
            name="third"
            style="overflow-y: scroll"
            v-if="isHytz"
          >
            <HYTZ @HYtotal="handleHYtotal"></HYTZ>
          </el-tab-pane>
          <el-tab-pane
            :label="tabLabel4"
            name="four"
            style="overflow-y: scroll"
            v-if="isXmpy"
          >
            <XMPYTable @XMPYtotal="handleXMPYtotal"></XMPYTable>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
    <NoticeInfo ref="ZNSJSJTZS" />
    <IndexView ref="SJJGWS" />
    <SchemeInfo
      v-if="showSchemeInfo"
      ref="SchemeInfo"
      @closeDialog="closeDialog"
    />
    <ProjectDataInfo ref="ProjectDataInfo" />
    <IndexEdit ref="edit" />
  </div>
</template>

<script>
  import { formatDay } from '@/utils/index'
  import { imPlementOrderDetail } from '@/api/audit/implement'
  import {
    getDistributionListPage,
    modifyDistributionInfo,
    getTypeData,
    getAllTypeData,
  } from '@/oapi/setting/system'
  import NoticeInfo from '@/views/audit/prepare/components/NoticeInfo.vue'
  import IndexView from '@/views/audit/report/components/IndexView'
  import { reportDetail } from '@/api/audit/report'
  import SchemeInfo from '@/views/audit/rectify/components/SchemeInfo'
  import { getRectificationPlanDetail } from '@/api/zgzz/index.js'
  import ProjectDataInfo from '@/views/audit/prepare/components/ProjectDataInfo.vue'
  import IndexEdit from '@/views/audit/project/components/IndexEditNew.vue'
  export default {
    props: {
      //个人操作台消息通知项目评优是否展示
      isXmpy: {
        type: Boolean,
        default: false,
      },
      //个人操作台消息通知会议通知是否展示
      isHytz: {
        type: Boolean,
        default: false,
      },
      // 智能审计和敏捷审计的动态查询参数
      moduleType: {
        type: String,
        default: 'yqns',
      },
    },
    name: 'hoem',
    components: {
      NoticeInfo,
      IndexView,
      SchemeInfo,
      ProjectDataInfo,
      IndexEdit,
    },
    data() {
      return {
        list: [],
        list2: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        total2: 0,
        hyTotal: 0,
        xmpyTotal: 2,
        queryForm: {
          isread: '0',
          moduleType: this.moduleType,
          pageNumber: 1,
          pageSize: 20,
          distributionType: '',
        },
        queryForm2: {
          isread: '1',
          moduleType: this.moduleType,
          pageNumber: 1,
          pageSize: 20,
          distributionType: '',
        },
        type: {
          ZNSJSJTZS: '审计通知',
          SJJGWS: '审计结果文书',
          ZGFA: '整改方案',
          XMZL: '项目资料',
          RWFP: '任务分配',
        },
        effectDetail: {},
        jumpUrl: {
          XMZL: '/implement/projectData',
          ZNSJSJTZS: '/implement/notice',
          SJJGWS: '/implement/index',
          ZGFA: '/audit/rectify/index',
          RWFP: '/project/index',
        },
        activeName1: 'first',
        typeData: [
          { textValue: 'ZNSJSJTZS', textName: '审计通知' },
          { textValue: 'SJJGWS', textName: '审计结果文书' },
          { textValue: 'ZGFA', textName: '整改方案' },
          { textValue: 'XMZL', textName: '项目资料' },
          { textValue: 'RWFP', textName: '任务分配' },
        ],
        select: [],
        showSchemeInfo: false,
      }
    },
    computed: {
      tabLabel1() {
        return (
          <span>
            未阅事项<span class="highlight">{'(' + this.total + ')'} </span>
          </span>
        )
      },
      tabLabel2() {
        return (
          <span>
            已阅事项<span class="highlight">{'(' + this.total2 + ')'} </span>
          </span>
        )
      },
      tabLabel3() {
        return (
          <span>
            会议通知<span class="highlight">{'(' + this.hyTotal + ')'} </span>
          </span>
        )
      },
      tabLabel4() {
        return (
          <span>
            项目评优<span class="highlight">{'(' + this.xmpyTotal + ')'} </span>
          </span>
        )
      },
    },
    created() {
      this.fetchData()
      this.fetchData2()
    },
    methods: {
      handleEdit(row) {
        if (row.distributionType == 'ZNSJSJTZS') {
          this.$nextTick(async () => {
            this.$refs['ZNSJSJTZS'].showEdit({ adviceid: row.formId }, true)
          })
        } else if (row.distributionType == 'SJJGWS') {
          this.$nextTick(async () => {
            const data = await reportDetail({ reportid: row.formId })
            await this.$refs['SJJGWS'].showEdit('detail', data.data)
          })
        } else if (row.distributionType == 'ZGFA') {
          this.$nextTick(async () => {
            this.showSchemeInfo = true
            this.$nextTick(async () => {
              const res = await getRectificationPlanDetail({
                planId: row.formId,
              })
              res.data.showModels = { reimpl: true, valua: true }
              await this.$refs['SchemeInfo'].showEdit('detail', res.data)
            })
          })
        } else if (row.distributionType == 'XMZL') {
          this.$nextTick(async () => {
            this.$refs['ProjectDataInfo'].showEdit({ id: row.formId }, true)
          })
        } else if (row.distributionType == 'RWFP') {
          this.$nextTick(async () => {
            this.$refs['edit'].showEdit({ projectId: row.formId }, true)
          })
        }
      },
      async handleOK(row) {
        const res = await modifyDistributionInfo({
          isread: 1,
          distributionId: row.distributionId,
        })
        if (res.code == 1) {
          this.$message.success(res.msg)
          this.$router.push(this.jumpUrl[row.distributionType])
          this.fetchData()
          this.fetchData2()
        }
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getDistributionListPage(this.queryForm)
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

      async fetchData2() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getDistributionListPage(this.queryForm2)
        this.list2 = tlist
        this.total2 = totalRecord
        this.listLoading = false
      },
      handleSizeChange2(val) {
        this.queryForm2.pageSize = val
        this.fetchData2()
      },
      handleCurrentChange2(val) {
        this.queryForm2.pageNumber = val
        this.fetchData2()
      },
      handleHYtotal(val) {
        this.hyTotal = val
      },
      handleXMPYtotal(val) {
        this.xmpyTotal = val
      },
      handleSelection(val, row) {
        const i = this.select.findIndex(
          (x) => x.distributionId == row.distributionId
        )
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (
              row &&
              !this.select.some((x) => x.distributionId == row.distributionId)
            ) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex(
              (x) => x.distributionId == row.distributionId
            )
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.distributionId == item.distributionId
              }),
              true
            )
          })
        })
      },
      async allSure() {
        const info = this.select.filter(
          (res) => res.distributionType !== this.select[0].distributionType
        )
        if (info.length > 0) {
          this.$message.error('请选择相同类型的数据')
          return
        }
        const ids = this.select.map((res) => res.distributionId).toString()

        const arr = await getAllTypeData({ ids })
        if (arr.code == 1) {
          this.$message.success(arr.msg)
          this.$router.push(this.jumpUrl[this.select[0].distributionType])
          this.fetchData()
          this.fetchData2()
        }
      },
      closeDialog() {
        this.showSchemeInfo = false
      },
    },
  }
</script>
<style scoped>
  h5 {
    font-size: 18px;
    margin: 2px;
    color: #333;
  }
  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .table-title {
    cursor: pointer;
  }

  .table th {
    position: relative;
  }

  .table-filter {
    position: absolute;
    border: 1px solid gainsboro;
    padding: 5px;
    left: 0;
    right: 0;
    top: 40px;
    background: white;
    min-width: 160px;
  }

  .table-filter input {
    padding: 5px;
    font-size: 14px;
    margin-right: 5px;
  }

  .table-filter .form-check {
    display: flex;
    flex-direction: column;
    text-align: left;
    font-size: 14px;
    font-weight: 400;
    padding: 5px;
  }

  .table-filter button {
    font-size: 12px;
    padding: 2px 10px;
  }

  .table-responsive {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
  }

  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .select-year {
    display: flex;
    background: aliceblue;
    padding: 5px;
  }

  .select-year > div {
    margin-right: 10px;
    padding: 2px 5px;
    cursor: pointer;
  }

  .select-year .active {
    background: #ffaf0f;
    border-radius: 20px;
    color: white;
  }
  h5 {
    margin: 0 0 10px 0;
    font-size: 17px;
  }
  .highlight {
    color: red; /* 设置文字颜色为红色 */
    font-weight: bold; /* 加粗文字 */
  }
</style>
