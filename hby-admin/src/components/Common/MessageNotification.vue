<template>
  <div>
    <el-row :gutter="10">
      <el-col :lg="24" :md="24" :sm="24">
        <el-tabs type="border-card" v-model="activeTab" @tab-click="handleTabClick">
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
                      <el-button
                        native-type="submit"
                        type="primary"
                        @click="resetForm"
                      >
                        重置
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
              :page-sizes="pageSizes"
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
                      <el-button
                        native-type="submit"
                        type="primary"
                        @click="resetForm"
                      >
                        重置
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
              :page-sizes="pageSizes"
              :total="tabData.read.total"
              @current-change="(val) => handlePageChange(val, READ)"
              @size-change="(val) => handlePageChange(val, READ, true)"
            />
          </el-tab-pane>
          <el-tab-pane
            :label="tabLabel3"
            name="third"
            style="overflow-y: scroll"
            v-if="showHytz"
          >
            <slot name="hytz-content"></slot>
          </el-tab-pane>
          <el-tab-pane
            :label="tabLabel4"
            name="four"
            style="overflow-y: scroll"
            v-if="showXmpy"
          >
            <slot name="xmpy-content"></slot>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { formatDay } from '@/utils/index'
  import {
    getDistributionListPage,
    modifyDistributionInfo,
    getAllTypeData,
    getTypeData,
  } from '@/oapi/setting/system'

  export default {
    name: 'MessageNotification',
    props: {
      // 模块类型，用于区分不同业务模块
      moduleType: {
        type: String,
        required: true,
      },
      // 类型映射表
      typeMapping: {
        type: Object,
        required: true,
      },
      // 跳转URL映射表
      jumpUrlMapping: {
        type: Object,
        required: true,
      },
      // 是否显示会议通知标签页
      showHytz: {
        type: Boolean,
        default: false,
      },
      // 是否显示项目评优标签页
      showXmpy: {
        type: Boolean,
        default: false,
      },
      // 会议通知总数
      hyTotal: {
        type: Number,
        default: 0,
      },
      // 项目评优总数
      xmpyTotal: {
        type: Number,
        default: 0,
      },
      // 类型数据，如果不提供则自动获取
      customTypeData: {
        type: Array,
        default: null,
      },
      lazyRead: {
        type: Boolean,
        default: true,
      },
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
            moduleType: this.moduleType,
            pageNumber: 1,
            pageSize: 10,
            distributionType: '',
          },
          // 已读查询
          read: {
            isread: '1',
            moduleType: this.moduleType,
            pageNumber: 1,
            pageSize: 10,
            distributionType: '',
          },
        },
        activeTab: 'first',
        typeData: [],
        select: [],
        pageSizes: [10, 20, 30, 40, 50],
      }
    },
    computed: {
      // 类型映射表
      type() {
        return this.typeMapping
      },
      // 跳转URL映射表
      jumpUrl() {
        return this.jumpUrlMapping
      },
      tabLabel1() {
        return `未阅事项(${this.tabData.unread.total})`
      },
      tabLabel2() {
        return `已阅事项(${this.tabData.read.total})`
      },
      tabLabel3() {
        return `会议通知(${this.hyTotal})`
      },
      tabLabel4() {
        return `项目评优(${this.xmpyTotal})`
      },
    },
    created() {
      // 默认只取未阅列表；已阅列表等用户切换到“已阅事项”时再请求，减少首页/模块切换冗余接口。
      this.fetchTabData(this.UNREAD)
      if (!this.lazyRead) this.fetchTabData(this.READ)

      // 如果没有提供自定义类型数据，则从后端获取
      if (this.customTypeData.length == 0) {
        this.fetchTypeData()
      } else {
        this.typeData = this.customTypeData
      }
    },
    methods: {
      resetForm() {
        this.formData.unread = {
          isread: '0',
          moduleType: this.moduleType,
          pageNumber: 1,
          pageSize: 20,
          distributionType: '',
        }
        this.formData.read = {
          isread: '1',
          moduleType: this.moduleType,
          pageNumber: 1,
          pageSize: 20,
          distributionType: '',
        }
        this.fetchTabData(this.UNREAD)
        if (!this.lazyRead || this.activeTab === 'second') {
          this.fetchTabData(this.READ)
        }
      },
      handleTabClick() {
        if (this.activeTab === 'second') {
          this.fetchTabData(this.READ)
        }
      },
      /**
       * 获取类型数据
       */
      async fetchTypeData() {
        try {
          const res = await getTypeData()
          if (res && res.data) {
            this.typeData = res.data
          }
        } catch (error) {
          console.error('获取类型数据失败:', error)
        }
      },

      /**
       * 查看详情
       * @param {Object} row - 行数据
       */
      handleEdit(row) {
        // 通过事件将详情查看操作传递给父组件处理
        this.$emit('view-detail', row)
      },

      /**
       * 确认接收
       * @param {Object} row - 行数据
       */
      async handleOK(row) {
        try {
          const res = await modifyDistributionInfo({
            isread: 1,
            distributionId: row.distributionId,
          })

          if (res.code == 1) {
            this.$message.success(res.msg)
            // 跳转到对应的页面
            this.$router.push(this.jumpUrl[row.distributionType])
            // 刷新两个列表数据
            this.fetchTabData(this.UNREAD)
            this.fetchTabData(this.READ)
            // 通知父组件确认接收成功
            this.$emit('confirm-received', row)
          }
        } catch (error) {
          console.error('确认接收失败:', error)
          this.$message.error('确认接收失败，请稍后重试')
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
        try {
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
        } catch (error) {
          console.error('获取数据失败:', error)
          this.$message.error('获取数据失败，请稍后重试')
        } finally {
          this.listLoading = false
        }
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
            // 通知父组件批量确认成功
            this.$emit('batch-confirm', this.select)
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
