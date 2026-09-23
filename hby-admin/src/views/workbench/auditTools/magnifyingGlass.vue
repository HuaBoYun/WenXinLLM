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
          <div @click="runrun(row)">
            <el-form>
              <el-form-item label="表名:">
                <span v-html="tableName[row.tableNum]"></span>
              </el-form-item>
              <el-form-item label="关键字:">
                <span v-html="row.highlights"></span>
              </el-form-item>
            </el-form>
          </div>
          <!-- <div>
            <span>标题:{{ row.headtext }}</span>
            <span>标题:{{ row.headtext }}</span>
          </div> -->
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
  import { fetchApi, accSearch } from '@/api/workbench/search'
  const { getList } = accSearch

  export default {
    name: 'Consult',
    // components: { LawSearch },
    data() {
      return {
        list: [],
        tag: ['标签一', '标签二', '标签三', '标签四', '标签五'],
        //表名枚举
        tableName: [
          '凭证库',
          '总分类账',
          '辅助帐信息',
          '辅助帐余额',
          '辅助帐总',
          '科目表',
          '余额表',
          '明细账',
        ],
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
        this.formData.forEach((res) => {
          const reg1 = /(TBL_ACC_BSEG\w+)|TBL_ACC_BSEG/g
          let accbseg = ''
          if (reg1.test(res.table)) {
            //
            //暂不确定此正则后续能否正常工作

            accbseg = res.table
          }
          switch (res.table) {
            //凭证库
            case 'TBL_ACC_BKPF':
              res.tableNum = 0
              break
            //总分类账
            case 'TBL_ACC_SUM':
              res.tableNum = 1

              break
            //辅助帐信息
            case 'TBL_ASS_INFO':
              res.tableNum = 2

              break
            //辅助帐余额
            case 'TBL_ASS_BAL':
              res.tableNum = 3

              break
            //辅助帐总
            case 'TBL_ASS_SUM':
              res.tableNum = 4

              break
            //科目表
            case 'TBL_ACCOUNT':
              res.tableNum = 5

              break
            //余额表
            case 'TBL_ACC_BAL':
              res.tableNum = 6

              break
            //明细账大概
            case 'TBL_ACC_BSEG' || accbseg:
              res.tableNum = 7

              break
            default:
              break
          }
        })

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
        storage.set('interiorTag', this.tag)
        // const law = storage.get('lawTag')
        //
      },
      //读取本地tag
      getLoaclTag() {
        //判断本地有无保存标签
        const law = storage.get('interiorTag')
        //
        if (law) {
          this.tag = law
        }
      },
      runrun(row) {
        // indexOf

        const reg1 = /(TBL_ACC_BSEG\w+)|TBL_ACC_BSEG/g
        let accbseg = ''
        if (reg1.test(row.table)) {
          //
          //暂不确定此正则后续能否正常工作

          accbseg = row.table
        }
        switch (row.table) {
          //凭证库
          case 'TBL_ACC_BKPF':
            // this.$router.push('/shenji/accountData/voucherLib')
            this.$router.push({
              name: 'VoucherLib',
              params: { queryData: row },
            })
            break
          //总分类账
          case 'TBL_ACC_SUM':
            // this.$router.push('/shenji/accountData/accountCate')
            this.$router.push({
              name: 'AccountCate',
              params: { queryData: row },
            })
            break
          //辅助帐信息
          case 'TBL_ASS_INFO':
            // this.$router.push('/shenji/accountData/accountAssist')
            this.$router.push({
              name: 'AccountAssist',
              params: { queryData: row },
            })
            break
          //辅助帐余额
          case 'TBL_ASS_BAL':
            // this.$router.push('/shenji/accountData/accountAssist')
            this.$router.push({
              name: 'AccountAssist',
              params: { queryData: row },
            })
            break
          //辅助帐总
          case 'TBL_ASS_SUM':
            // this.$router.push('/shenji/accountData/accountAssist')
            this.$router.push({
              name: 'AccountAssist',
              params: { queryData: row },
            })
            break
          //科目表
          case 'TBL_ACCOUNT':
            // this.$router.push('/shenji/accountData/subject')
            this.$router.push({
              name: 'Subject',
              params: { queryData: row },
            })
            break
          //余额表
          case 'TBL_ACC_BAL':
            // this.$router.push('/shenji/accountData/balance')
            this.$router.push({
              name: 'Balance',
              params: { queryData: row },
            })
            break
          // 明细账大概
          case 'TBL_ACC_BSEG' || accbseg:
            this.$router.push('/shenji/accountData/accountDetail')
            this.$router.push({
              name: 'AccountDetail',
              params: { testText: row },
            })
            break
          default:
            break
        }
      },
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
