<template>
  <div>
    <el-row :gutter="10">
      <el-col :lg="24" :md="24" :sm="24">
        <el-tabs type="border-card" v-model="activeTab">
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
                    :model="formData.unread"
                    @submit.native.prevent
                  >
                    <el-form-item>
                      <el-select
                        v-model="formData.unread.distributionType"
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
                        @click="fetchTabData(UNREAD)"
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
              :data="tabData.unread.list"
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
              :current-page="formData.unread.pageNumber"
              :layout="layout"
              :page-size="formData.unread.pageSize"
              :total="tabData.unread.total"
              @current-change="(val) => handlePageChange(val, UNREAD)"
              @size-change="(val) => handlePageChange(val, UNREAD, true)"
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
                    :model="formData.read"
                    @submit.native.prevent
                  >
                    <el-form-item>
                      <el-select
                        v-model="formData.read.distributionType"
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
                        @click="fetchTabData(READ)"
                      >
                        查询
                      </el-button>
                    </el-form-item>
                  </el-form>
                </vab-query-form-left-panel>
              </el-card>
            </vab-query-form>
            <el-table border :data="tabData.read.list" style="width: 100%">
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
              :current-page="formData.read.pageNumber"
              :layout="layout"
              :page-size="formData.read.pageSize"
              :total="tabData.read.total"
              @current-change="(val) => handlePageChange(val, READ)"
              @size-change="(val) => handlePageChange(val, READ, true)"
            />
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
    <PlanView ref="edit" />
    <TestPlanView ref="testplan" menuKey="TestPlan" />
    <ProjectView ref="project" />
  </div>
</template>

<script>
  import { formatDay } from '@/utils/index'
  import {
    getDistributionListPage,
    modifyDistributionInfo,
    getAllTypeData,
  } from '@/oapi/setting/system'
  import PlanView from '@/views/internal/internalTest/components/PlanView'
  import TestPlanView from '@/views/internal/internalTest/components/TestPlanView'
  import ProjectView from '@/views/internal/evaluationManagement/components/ProjectView'
  export default {
    name: 'hoem',
    components: {
      PlanView,
      TestPlanView,
      ProjectView,
    },
    data() {
      return {
        // 常量定义
        READ: 'read',
        UNREAD: 'unread',
        // 统一管理数据结构
        tabData: {
          // 未读数据
          unread: {
            list: [],
            total: 0,
          },
          // 已读数据
          read: {
            list: [],
            total: 0,
          },
        },
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        // 统一的查询表单对象
        formData: {
          // 未读查询
          unread: {
            isread: '0',
            moduleType: 'nkhg',
            pageNumber: 1,
            pageSize: 20,
            distributionType: '',
          },
          // 已读查询
          read: {
            isread: '1',
            moduleType: 'nkhg',
            pageNumber: 1,
            pageSize: 20,
            distributionType: '',
          },
        },
        type: {
          CSFA: '处室任务分派',
          CSRW: '测试方案',
          JTCSJH: '集团测试计划',
          PJLX: '评价立项',
        },
        jumpUrl: {
          CSRW: '/internalTest/task',
          CSFA: '/internalTest/plan',
          JTCSJH: '/internalTest/plan',
          PJLX: '/evaluationManagement/score',
        },
        activeTab: 'first',
        typeData: [
          { textValue: 'CSRW', textName: '测试方案' },
          { textValue: 'CSFA', textName: '处室任务分派' },
          { textValue: 'JTCSJH', textName: '集团测试计划' },
          { textValue: 'PJLX', textName: '评价立项' },
        ],
        select: [],
      }
    },
    computed: {
      tabLabel1() {
        return (
          <span>
            未阅事项
            <span class="highlight">
              {'(' + this.tabData.unread.total + ')'}{' '}
            </span>
          </span>
        )
      },
      tabLabel2() {
        return (
          <span>
            已阅事项
            <span class="highlight">
              {'(' + this.tabData.read.total + ')'}{' '}
            </span>
          </span>
        )
      },
    },
    created() {
      this.fetchTabData(this.UNREAD)
      this.fetchTabData(this.READ)
    },
    methods: {
      /**
       * 查看详情
       * @param {Object} row - 行数据
       */
      handleEdit(row) {
        if (row.distributionType == 'JTCSJH') {
          this.$refs['testplan'].showEdit({ id: row.formId }, true)
        } else if (row.distributionType == 'PJLX') {
          this.$refs['project'].showEdit({ assid: row.formId }, true)
        } else {
          this.$refs['edit'].showEdit({ testplanid: row.formId }, true)
        }
      },

      /**
       * 确认接收
       * @param {Object} row - 行数据
       */
      async handleOK(row) {
        const res = await modifyDistributionInfo({
          isread: 1,
          distributionId: row.distributionId,
        })
        if (res.code == 1) {
          this.$message.success(res.msg)
          this.$router.push(this.jumpUrl[row.distributionType])
          // 刷新两个列表数据
          this.fetchTabData(this.UNREAD)
          this.fetchTabData(this.READ)
        }
      },

      /**
       * 格式化日期
       * @param {Object} row - 行数据
       * @param {Object} column - 列配置
       * @returns {String} 格式化后的日期字符串
       */
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },

      /**
       * 通用数据获取方法
       * @param {String} type - 数据类型（'read'或'unread'）
       */
      async fetchTabData(type) {
        this.listLoading = true
        const params = this.formData[type]

        const {
          data: { tlist, totalRecord },
        } = await getDistributionListPage(params)

        if (type === this.UNREAD) {
          this.tabData.unread.list = tlist
          this.tabData.unread.total = totalRecord
          // 获取未读数据后，如果有选中项则恢复选中状态
          if (this.select.length > 0) {
            this.$nextTick(() => this.setCheckedRows())
          }
        } else {
          this.tabData.read.list = tlist
          this.tabData.read.total = totalRecord
        }

        this.listLoading = false
      },

      /**
       * 通用分页变化处理
       * @param {Number} val - 页码或每页条数
       * @param {String} type - 数据类型（'read'或'unread'）
       * @param {Boolean} isSize - 是否为页面大小变更（默认为false，表示页码变更）
       */
      handlePageChange(val, type, isSize = false) {
        const form = this.formData[type]
        if (isSize) {
          form.pageSize = val
        } else {
          form.pageNumber = val
        }
        this.fetchTabData(type)
      },

      /**
       * 单选处理
       * @param {Boolean} val - 是否选中
       * @param {Object} row - 行数据
       */
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

      /**
       * 全选处理
       * @param {Array} val - 已选中的行
       */
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
          this.tabData.unread.list.map((row) => {
            const i = this.select.findIndex(
              (x) => x.distributionId == row.distributionId
            )
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      /**
       * 翻页后恢复已勾选的数据
       * 在表格数据加载完成后调用，恢复之前已选中的行
       */
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            const foundRow = this.tabData.unread.list.find(
              (item) => item.distributionId === row.distributionId
            )
            if (foundRow) {
              this.$refs.multipleTable.toggleRowSelection(foundRow, true)
            }
          })
        })
      },
      /**
       * 批量确认处理
       * 检查选中项的有效性，确认是否为同一类型
       * 调用API进行批量处理，完成后跳转到相应页面
       */
      async allSure() {
        // 检查是否有选中的项目
        if (!this.select.length) {
          this.$message.warning('请选择需要确认的项目')
          return
        }
        // 确保所有选中项都是同一类型
        const info = this.select.filter(
          (res) => res.distributionType !== this.select[0].distributionType
        )
        if (info.length > 0) {
          this.$message.error('请选择相同类型的数据')
          return
        }
        // 获取所有选中项的ID，并转换为逗号分隔的字符串
        const ids = this.select.map((res) => res.distributionId).toString()

        try {
          // 调用批量确认API
          const arr = await getAllTypeData({ ids })
          if (arr.code == 1) {
            this.$message.success(arr.msg)
            // 跳转到对应的页面
            this.$router.push(this.jumpUrl[this.select[0].distributionType])
            // 刷新两个列表数据
            this.fetchTabData(this.UNREAD)
            this.fetchTabData(this.READ)
          } else {
            this.$message.error(arr.msg || '操作失败')
          }
        } catch (error) {
          console.error('批量确认失败:', error)
          this.$message.error('批量确认失败，请稍后重试')
        }
      },
    },
  }
</script>
<style scoped>
  h5 {
    font-size: 18px;
    margin: 0 0 10px 0;
    color: #333;
  }

  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .highlight {
    color: red; /* 设置文字颜色为红色 */
    font-weight: bold; /* 加粗文字 */
  }
</style>
