<template>
  <div class="system-log-container lr-layout">
    <div class="lr-layout">
      <div class="left">
        <dep-tree ref="leftlist" @select="handleSelectTree" :isAll="true" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="24">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.virtualname"
                    clearable
                    placeholder="虚拟组织名称"
                    v-if="item.name === '虚拟组织名称'"
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
                  <el-button
                    native-type="submit"
                    type="primary"
                    @click="resetSearch"
                  >
                    重置
                  </el-button>
                </el-form-item>
                <el-form-item>
                  <el-tooltip
                    class="item"
                    effect="dark"
                    content="搜索筛选"
                    placement="top"
                  >
                    <el-popover placement="left" trigger="click">
                      <filter-search
                        v-if="true"
                        :list="searchAll"
                        :name="localKey"
                        @updateSearchShow="initSearch"
                      />
                      <el-button slot="reference" style="height: 32px">
                        <vab-icon icon="filter" :is-custom-svg="true" />
                      </el-button>
                    </el-popover>
                  </el-tooltip>
                </el-form-item>
                <el-form-item>
                  <span
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
                    @click="showMore"
                  >
                    <span>{{ searchMore ? '收起' : '展开' }}</span>
                    <i class="el-icon-arrow-down"></i>
                  </span>
                </el-form-item>
              </el-form>
            </vab-query-form-left-panel>
          </el-card>
        </vab-query-form>
        <el-card shadow="never" class="secondCard">
          <vab-query-form-right-panel class="option-row">
            <el-tooltip
              class="item"
              effect="dark"
              content="表格筛选"
              placement="top"
            >
              <el-popover placement="right" trigger="click">
                <filter-table
                  :list="filedAll"
                  :name="tableKey"
                  @updateTableShow="initTable"
                />
                <!-- <i class="el-icon-delete" slot="reference"></i> -->
                <el-button
                  slot="reference"
                  icon="el-icon-s-grid"
                  class="biaoge"
                  style="margin-bottom: 10px; margin-right: 10px"
                ></el-button>
              </el-popover>
            </el-tooltip>

            <el-button
              type="success"
              @click="handleAdd"
              v-if="hasAuth('XNZJadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column align="center" label="机构编号" prop="orgnumber">
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row, true)">
                  {{ row.orgnumber }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column width="1" />
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="机构名称"
                prop="orgname"
                v-if="item.name === '机构名称'"
              />
              <el-table-column
                align="center"
                label="机构简介"
                prop="orgmeno"
                show-overflow-tooltip
                v-if="item.name === '机构简介'"
              />
              <el-table-column
                align="center"
                label="虚拟组织名称"
                prop="virtualname"
                show-overflow-tooltip
                v-if="item.name === '虚拟组织名称'"
              />
              <el-table-column
                align="center"
                label="机构备注"
                prop="memo"
                show-overflow-tooltip
                v-if="item.name === '机构备注'"
              />
            </div>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  v-if="hasAuth('XNZJmod')"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handleDel(row)"
                  v-if="hasAuth('XNZJdel')"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
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
    </div>

    <!-- 虚拟组织管理对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="handleDialogClose"
    >
      <div class="virtual-org-container">
        <div class="virtual-org-list">
          <div
            v-for="(item, index) in virtualOrgList"
            :key="index"
            class="virtual-org-item"
          >
            <el-input
              v-model="item.name"
              placeholder="请输入虚拟组织名称"
              class="org-input"
              :disabled="isReadOnly"
            />
            <el-button
              v-if="!isEditMode && !isReadOnly"
              type="danger"
              icon="el-icon-delete"
              size="small"
              @click="removeVirtualOrg(index)"
              :disabled="virtualOrgList.length === 1"
            ></el-button>
          </div>
        </div>
        <el-button
          v-if="!isEditMode && !isReadOnly"
          type="primary"
          icon="el-icon-plus"
          @click="addVirtualOrg"
        >
          添加虚拟组织
        </el-button>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button v-if="!isReadOnly" type="primary" @click="handleConfirm">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {
    vritualList,
    vritualDelete,
    vritualAdd,
    vritualEdit,
  } from '@/api/setting/org'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import DepTree from '@/views/setting/org/components/DepTree'
  import { hasAuth } from '@/utils'
  export default {
    components: {
      filterSearch,
      filterTable,
      DepTree,
    },
    data() {
      return {
        queryForm: {
          virtualname: '',
          orgid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        listLoading: false,
        list: [],
        originalList: [], // 保存原始数据结构用于编辑
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-org-dep-search',
        tableKey: 'setting-org-dep-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '机构名称' },
          { name: '机构简介' },
          { name: '虚拟组织名称' },
          { name: '机构备注' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        // 对话框相关数据
        dialogVisible: false,
        dialogTitle: '',
        virtualOrgList: [{ name: '' }],
        currentEditRow: null,
        isEditMode: false,
        isReadOnly: false, // 控制只读模式
      }
    },
    created() {
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      async fetchData() {
        this.listLoading = true
        const {
          data: { records, total },
        } = await vritualList({
          ...this.queryForm,
        })
        // 保留原始数据结构用于编辑，同时创建展开的数据用于表格显示
        this.originalList = records
        this.list = records.map((v) => {
          return { ...v, ...v.org, _original: v }
        })
        this.total = total
        this.listLoading = false
      },
      handleSelectTree(org) {
        this.queryForm.orgid = org.id
        this.fetchData()
      },
      handleAdd() {
        if (!this.queryForm.orgid) {
          this.$message.warning('请先选择左侧组织架构')
          return
        }
        this.dialogTitle = '新建虚拟组织'
        this.isEditMode = false
        this.virtualOrgList = [{ name: '' }]
        this.currentEditRow = null
        this.dialogVisible = true
      },
      handleEdit(row, isReadOnly = false) {
        if (isReadOnly) {
          this.dialogTitle = '查看虚拟组织'
          this.isEditMode = false
          this.isReadOnly = true
        } else {
          this.dialogTitle = '修改虚拟组织'
          this.isEditMode = true
          this.isReadOnly = false
        }
        this.currentEditRow = row._original || row
        // 解析现有的虚拟组织名称
        if (row.virtualname) {
          // 按逗号分割虚拟组织名称
          const virtualNames = row.virtualname
            .split(',')
            .map((name) => name.trim())
            .filter((name) => name !== '')
          if (virtualNames.length > 0) {
            this.virtualOrgList = virtualNames.map((name) => ({ name }))
          } else {
            this.virtualOrgList = [{ name: '' }]
          }
        } else {
          this.virtualOrgList = [{ name: '' }]
        }
        this.dialogVisible = true
      },
      handleDel(row) {
        this.$confirm('确定删除吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          vritualDelete({ fid: row.fid }).then((res) => {
            if (res.code == 1) {
              this.$message({
                message: '删除成功',
                type: 'success',
              })
              this.fetchData()
            }
          })
        })
      },
      getFiled() {
        let fields = [{ name: '虚拟组织名称', key: 'virtualname' }]
        return fields
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = ''
              }
            }
          })

          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      // 动态表格开始
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
      },
      showMore() {
        this.searchMore = !this.searchMore
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.initTable()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.initTable()
      },
      resetSearch() {
        this.queryForm = {
          virtualname: '',
          orgid: this.queryForm.orgid, // 保留orgid不被重置
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      // 对话框相关方法
      addVirtualOrg() {
        this.virtualOrgList.push({ name: '' })
      },
      removeVirtualOrg(index) {
        if (this.virtualOrgList.length > 1) {
          this.virtualOrgList.splice(index, 1)
        }
      },
      handleDialogClose() {
        this.virtualOrgList = [{ name: '' }]
        this.currentEditRow = null
        this.isEditMode = false
        this.isReadOnly = false
      },
      async handleConfirm() {
        // 验证输入
        console.log(
          '🚀 ~ handleConfirm ~ this.virtualOrgList:',
          this.virtualOrgList
        )
        const validOrgList = this.virtualOrgList.filter(
          (item) => item.name.trim() !== ''
        )
        if (validOrgList.length === 0) {
          this.$message.warning('请至少输入一个虚拟组织名称')
          return
        }

        try {
          let saveData

          if (this.isEditMode) {
            // 编辑模式：传递整行数据，只更新virtualname
            saveData = {
              ...this.currentEditRow,
              virtualname: validOrgList
                .map((item) => item.name.trim())
                .join(','),
            }
          } else {
            // 新建模式：直接使用voiList数组
            saveData = validOrgList.map((item) => ({
              orgid: this.queryForm.orgid,
              virtualname: item.name.trim(),
            }))
          }

          // 调用API
          if (this.isEditMode) {
            await vritualEdit(saveData)
          } else {
            await vritualAdd(saveData)
          }

          this.$message.success(this.isEditMode ? '修改成功' : '新建成功')
          this.dialogVisible = false
          this.fetchData() // 刷新列表
        } catch (error) {
          this.$message.error('操作失败：' + (error.message || '未知错误'))
        }
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
    width: 250px;
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

  // 对话框样式
  .virtual-org-container {
    .virtual-org-list {
      max-height: 300px;
      overflow-y: auto;

      .virtual-org-item {
        display: flex;
        align-items: center;
        margin-bottom: 10px;

        .org-input {
          flex: 1;
          margin-right: 10px;
        }
      }
    }

    .add-btn {
      width: 100%;
      margin-top: 10px;
    }
  }

  .dialog-footer {
    text-align: right;
  }
</style>
