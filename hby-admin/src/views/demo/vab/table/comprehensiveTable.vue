<template>
  <div class="comprehensive-table-container">
    <el-alert
      :closable="false"
      show-icon
      title="三级路由【缓存路由、固定表格高度、并根据窗口大小自适应】的示例。详情页请点击操作列按钮，支持tab多开并高亮左侧菜单"
      type="success"
    />
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="49px"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item v-show="!fold" label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item v-show="!fold" label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item v-show="!fold" label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item v-show="!fold" label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item v-show="!fold" label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item v-show="!fold" label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item v-show="!fold" label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item v-show="!fold" label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item v-show="!fold" label="标题">
            <el-input v-model="queryForm.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="handleQuery"
            >
              查询
            </el-button>
            <el-button type="text" @click="handleFold">
              <span v-if="fold">展开</span>
              <span v-else>合并</span>
              <vab-icon
                class="vab-dropdown"
                :class="{ 'vab-dropdown-active': fold }"
                icon="arrow-up-s-line"
              />
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel :span="24">
        <el-button icon="el-icon-plus" type="primary" @click="handleAdd">
          添加
        </el-button>
        <el-button icon="el-icon-delete" type="danger" @click="handleDelete">
          删除
        </el-button>
        <el-button type="primary" @click="handleMessage">
          $baseMessage
        </el-button>
        <el-button type="primary" @click="handleAlert">$baseAlert</el-button>
        <el-button type="primary" @click="handleConfirm">
          $baseConfirm
        </el-button>
        <el-button type="primary" @click="handleNotify">$baseNotify</el-button>
        <el-button
          icon="el-icon-info"
          type="primary"
          @click="handleDetailStayTable"
        >
          打开详情页后停留在本页（不常用）
        </el-button>
        <el-badge class="item" value="New">
          <el-button
            icon="el-icon-info"
            style="margin: 0 0 10px 0 !important"
            type="primary"
            @click="handleDetail"
          >
            详情页支持tab多开并高亮左侧菜单
          </el-button>
        </el-badge>
      </vab-query-form-left-panel>
    </vab-query-form>

    <el-table
      ref="tableSort"
      v-loading="listLoading"
      border
      :data="list"
      :height="height"
      @selection-change="setSelectRows"
      @sort-change="tableSortChange"
    >
      <el-table-column
        align="center"
        show-overflow-tooltip
        type="selection"
        width="55"
      />
      <el-table-column
        align="center"
        label="序号"
        show-overflow-tooltip
        width="55"
      >
        <template #default="{ $index }">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="标题"
        prop="title"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="作者"
        prop="author"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="评级">
        <template #default="{ row }">
          <el-rate v-model="row.rate" disabled />
        </template>
      </el-table-column>
      <el-table-column align="center" label="头像">
        <template #default="{ row }">
          <el-image
            v-if="imgShow"
            :preview-src-list="imageList"
            :src="row.img"
          />
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        label="点击量"
        prop="pageViews"
        show-overflow-tooltip
        sortable
      />
      <el-table-column
        align="center"
        label="开关"
        prop="switch"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <el-tooltip
            :content="row.switch === 0 ? '点击开启' : '点击关闭'"
            :enterable="false"
            placement="top"
          >
            <el-switch v-model="row.switch" />
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" show-overflow-tooltip>
        <template #default="{ row }">
          <el-tooltip
            class="item"
            :content="row.status"
            effect="dark"
            placement="top-start"
          >
            <el-tag :type="row.status | statusFilter">
              {{ row.status }}
            </el-tag>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="时间"
        prop="datetime"
        show-overflow-tooltip
        width="200"
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="130"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">详情</el-button>
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-image
          class="vab-data-empty"
          :src="require('@/assets/empty_images/data_empty.png')"
        />
      </template>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <table-edit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { doDelete, getList } from '@/api/table'
  import TableEdit from './components/TableEdit'

  export default {
    name: 'ComprehensiveTable',
    components: {
      TableEdit,
    },
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: 'success',
          draft: 'gray',
          deleted: 'danger',
        }
        return statusMap[status]
      },
    },
    data() {
      return {
        fold: false,
        height: this.$baseTableHeight(3) - 30,
        imgShow: true,
        list: [],
        imageList: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        selectRows: '',
        queryForm: {
          pageNo: 1,
          pageSize: 10,
        },
      }
    },
    beforeMount() {
      window.addEventListener('resize', this.handleHeight)
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.handleHeight)
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleFold() {
        this.fold = !this.fold
        this.handleHeight()
      },
      handleHeight() {
        if (this.fold) this.height = this.$baseTableHeight(2) - 47
        else this.height = this.$baseTableHeight(3) - 30
      },
      tableSortChange() {
        const imageList = []
        this.$refs.tableSort.tableData.forEach((item) => {
          imageList.push(item.img)
        })
        this.imageList = imageList
      },
      setSelectRows(val) {
        this.selectRows = val
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        if (row.id) {
          this.$baseConfirm('你确定要删除当前项吗', null, async () => {
            const { msg } = await doDelete({ ids: row.id })
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          })
        } else {
          if (this.selectRows.length > 0) {
            const ids = this.selectRows.map((item) => item.id).join()
            this.$baseConfirm('你确定要删除选中项吗', null, async () => {
              const { msg } = await doDelete({ ids: ids })
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              await this.fetchData()
            })
          } else {
            this.$baseMessage('未选中任何行', 'error', 'vab-hey-message-error')
          }
        }
      },
      handleDetailStayTable() {
        if (this.selectRows.length === 1) {
          const loading = this.$baseLoading()
          this.$router
            .push({
              path: '/vab/table/detail',
              query: this.selectRows[0],
            })
            .then(() => {
              setTimeout(() => {
                this.$router.push('/vab/table/comprehensiveTable')
              }, 500)
              setTimeout(() => {
                loading.close()
              }, 1500)
            })
        } else {
          this.$baseMessage(
            '请选择一行进行详情页跳转并停留到表格页',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      handleDetail(row) {
        if (row.id)
          this.$router.push({
            path: '/vab/table/detail',
            query: row,
          })
        else {
          if (this.selectRows.length === 1) {
            this.$router.push({
              path: '/vab/table/detail',
              query: this.selectRows[0],
            })
          } else {
            this.$baseMessage(
              '请选择一行进行详情页跳转',
              'error',
              'vab-hey-message-error'
            )
          }
        }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      handleQuery() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        const imageList = []
        list.forEach((item) => {
          imageList.push(item.img)
        })
        this.imageList = imageList
        this.total = total
        this.listLoading = false

        setTimeout(() => {
          this.toggleSelection([this.list[0]])
        }, 0)
      },
      handleMessage() {
        this.$baseMessage('test1', 'success', 'vab-hey-message-success')
      },
      handleAlert() {
        this.$baseAlert('11')
        this.$baseAlert('11', '自定义标题', () => {
          /* 可以写回调; */
        })
        this.$baseAlert('11', null, () => {
          /* 可以写回调; */
        })
      },
      handleConfirm() {
        this.$baseConfirm(
          '你确定要执行该操作?',
          null,
          () => {
            /* 可以写回调; */
          },
          () => {
            /* 可以写回调; */
          }
        )
      },
      handleNotify() {
        this.$baseNotify('测试消息提示', 'test', 'success', 'bottom-right')
      },
      toggleSelection(rows) {
        if (rows) {
          rows.forEach((row) => {
            this.$refs.tableSort.toggleRowSelection(row)
          })
        } else {
          this.$refs.tableSort.clearSelection()
        }
      },
    },
  }
</script>
