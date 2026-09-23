<template>
  <div class="system-log-container">
    <div class="search">
      <div>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.keyWord"
              clearable
              placeholder="请输入关键字"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              搜索
            </el-button>
            <el-button type="success" @click="handleAdd">保存关键字</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="label-margin-bottom">
        <el-tag>{{ tag[0] }}</el-tag>
        <el-tag type="success">{{ tag[1] }}</el-tag>
        <el-tag type="info">{{ tag[2] }}</el-tag>
        <el-tag type="warning">{{ tag[3] }}</el-tag>
        <el-tag type="danger">{{ tag[4] }}</el-tag>
      </div>
    </div>
    <el-table
      :data="formData"
      v-loading="listLoading"
      v-if="formData.length > 0"
    >
      <el-table-column>
        <template #default="{ row }">
          <div v-html="row.bodyinfo" @click="runrun(row)"></div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNum"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  import storage from '@/utils/localStorage'
  // import LawSearch from './components/SearchTable/LawSearch.vue'
  import { fetchApi, lawSearch } from '@/api/workbench/search'
  const { getList } = lawSearch

  export default {
    name: 'Consult',
    // components: { LawSearch },
    data() {
      return {
        list: [],
        tag: ['标签一', '标签二', '标签三', '标签四', '标签五'],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          keyWord: '',
          pageNum: 1,
          pageSize: 20,
        },
        formData: [],
      }
    },
    created() {
      // this.fetchData()
    },
    mounted() {
      this.getLoaclTag()
    },
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNum = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNum = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        //
        const { rows, total } = await fetchApi(getList, this.queryForm)
        this.formData = rows
        this.total = total
        this.listLoading = false
      },

      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleAdd() {
        //截取长度
        let str = ''
        if (this.queryForm.keyWord.length > 10) {
          str = this.queryForm.keyWord.slice(0, 10)
          str += '...'
        } else {
          str = this.queryForm.keyWord
        }
        //
        //实现标签队列
        this.tag.push(str)
        this.tag.shift()
        //存入localStorage
        storage.set('lawTag', this.tag)
        // const law = storage.get('lawTag')
        //
      },
      //读取本地tag
      getLoaclTag() {
        //判断本地有无保存标签
        const law = storage.get('lawTag')

        if (law) {
          this.tag = law
        }
      },
      runrun(row) {},
    },
  }
</script>
<style scoped>
  .label-margin-bottom {
    margin-bottom: 15px;
  }
  .search {
    text-align: center;
  }
</style>
