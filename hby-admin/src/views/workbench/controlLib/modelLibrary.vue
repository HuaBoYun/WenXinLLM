<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <industry-tree></industry-tree>
      </div>
      <div class="right">
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
                  v-model="queryForm.articletitle"
                  clearable
                  placeholder="文章标题"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.aruticleauther"
                  clearable
                  placeholder="文章作者"
                />
              </el-form-item>
              <!-- <el-form-item>
                <el-input
                  v-model="queryForm.name"
                  clearable
                  placeholder="对方谈判人"
                />
              </el-form-item> -->
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
          <vab-query-form-right-panel>
            <el-button
              type="success"
              @click="handleAdd"
              v-if="hasAuth('MXKadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
          <el-table-column align="center" label="文章标题" prop="articletitle">
            <template #default="{ row }">
              <el-button
                @click="handleView(row, true)"
                type="text"
                v-if="hasAuth('MXKdetail')"
              >
                {{ row.articletitle }}
              </el-button>
              <div v-else>{{ row.articletitle }}</div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="文章作者"
            prop="aruticleauther"
          />
          <el-table-column
            align="center"
            label="发布日期"
            prop="publishtime"
            :formatter="formatDate"
            sortable="custom"
          />
          <el-table-column
            align="center"
            label="文章状态"
            prop="articlestatus"
          />
          <!-- <el-table-column align="center" label="是否协商一致" prop="data" />
          <el-table-column align="center" label="创建日期" prop="data" /> -->
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
                v-if="hasAuth('MXKedit')"
              >
                修改
              </el-button>
              <el-button
                type="text"
                @click="handleEdit2(row)"
                v-if="hasAuth('MXKdelete')"
              >
                删除
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
      </div>
    </div>

    <LcdyEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { UTCformat } from '@/utils/index'
  import { getOtherarticlePageList } from '@/api/workbench/auditTools'
  import { doDelete } from '@/api/table'
  import LcdyEdit from '../contractTools/KnowLedgeEdit.vue'
  import IndustryTree from '../contractTools/IndustryTree.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Consult',
    components: { LcdyEdit, IndustryTree },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          articletitle: '',
          aruticleauther: '',
          orgid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
            this.sortFlag = 'asc'
        } else if (order === 'descending') {
            this.sortFlag = 'desc'
        } else {
        this.sortFlag = ''
        }
        await this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return UTCformat(data)
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
        } = await getOtherarticlePageList({...this.queryForm,sortFields: this.sortFields,
          sortFlag: this.sortFlag})
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleView(row, flag) {
        this.$refs['edit'].showEdit(row, flag)
      },
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
    width: 75%;
  }
</style>
