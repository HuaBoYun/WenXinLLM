<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.xmname"
                clearable
                placeholder="论文名称"
                v-if="item.name === '论文名称'"
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
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
      <vab-query-form-right-panel :span="24">
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">新建</el-button>
        <el-button type="success" @click="handleSend">上报</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
      >
        <el-table-column type="selection" width="55" :selectable="selected" />
        <el-table-column align="center" label="序号" type="index" width="50" />
        <el-table-column align="center" label="论文名称" prop="papername">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.papername }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="填报单位"
            v-if="item.name === '填报单位'"
            prop="tbrgname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="撰写人"
            v-if="item.name === '撰写人'"
            prop="zxrname"
          />
          <el-table-column
            align="center"
            label="备注"
            v-if="item.name === '备注'"
            prop="remarks"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
            prop="status"
          >
            <template #default="{ row }">
              {{ row.status == 0 ? '未上报' : '已上报' }}
            </template>
          </el-table-column>
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
              :disabled="row.status == 1"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="row.status == 1"
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
    <Edit ref="edit" @fetch-data="fetchData" />
    <selectPeopels ref="people" @selected="selectPerson" />
  </div>
</template>

<script>
  import { getLwsbList, deleteLwsb, lwsbXfry } from '@/oapi/audit/lwpy'
  import Edit from '@/views/oilAudit/lwpy/components/lwsbEdit.vue'
  import { parseTime } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import selectPeopels from '@/views/oilAudit/xmpy/components/selectPeopels.vue'

  export default {
    name: 'lwsb',
    components: { Edit, filterSearch, filterTable, selectPeopels },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          xmname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '填报单位' },
          { name: '撰写人' },
          { name: '备注' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-lwpy-lwsb-search',
        tableKey: 'oilAudit-lwpy-lwsb-list',
        searchMore: true,
        projectNoticeId: '',
        select: [],
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [{ name: '论文名称', key: 'xmname' }]
      },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      resetQueryForm() {
        this.queryForm = {
          xmname: '',
          pageNumber: 1,
          pageSize: 20,
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
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getLwsbList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.select = []
        this.setCheckedRows()
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit('detail', row)
      },
      async handleEdit(row) {
        await this.$refs['edit'].showEdit('edit', row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteLwsb({
            perid: row.perid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            this.fetchData()
          }
        })
      },
      xiafa(row) {
        this.$refs['people'].showEdit()
        this.projectNoticeId = row.id
      },
      selectPerson(val) {
        const arr = val.map((res) => {
          return {
            distributeId: res.staffid,
            projectNoticeId: this.projectNoticeId,
          }
        })
      },
      handleSend() {
        if (this.select && this.select.length > 0) {
          let ids = this.select.map((res) => res.perid).join(',')
          lwsbXfry({ perid: ids }).then((res) => {
            if (res.code == 1) {
              this.$baseMessage(res.msg, 'success')
              this.fetchData()
            }
          })
        } else {
          this.$baseMessage(
            '请选择需要上报的数据',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      // 勾选条件
      selected(row, index) {
        if (row.status == '0') {
          return true
        } else {
          return false
        }
      },
      //选择
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.perid == row.perid)
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
            if (row && !this.select.some((x) => x.perid == row.perid)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.perid == row.perid)
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
                return row.perid == item.perid
              }),
              true
            )
          })
        })
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .margin-b0 {
    margin-bottom: 0;
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
</style>
