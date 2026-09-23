<template>
  <div class="system-log-container">
    <el-row :gutter="20" v-loading="loading">
      <el-col :span="2">
        <div>&nbsp;</div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo('statement')">
            <vab-icon icon="baobiao" is-custom-svg />
            <p>报表数据</p>
          </div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo('subject')">
            <vab-icon icon="kemu" is-custom-svg />
            <p>科目表</p>
          </div>
          <div class="line-item" @click="goTo('balance')">
            <vab-icon icon="balance" is-custom-svg />
            <p>余额表</p>
          </div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo('accountCate')">
            <vab-icon icon="zongzhang" is-custom-svg />
            <p>总分类账</p>
          </div>
          <div class="line-item" @click="goTo('accountDetail')">
            <vab-icon icon="mingxi" is-custom-svg />
            <p>明细账</p>
          </div>
          <div class="line-item" @click="goTo('accountDiary')">
            <vab-icon icon="jizhang" is-custom-svg />
            <p>日记账</p>
          </div>
          <div class="line-item" @click="goTo('accountAssist')">
            <vab-icon icon="fuzhu" is-custom-svg />
            <p>辅助账</p>
          </div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo('voucherLib')">
            <vab-icon icon="pingzheng" is-custom-svg />
            <p>凭证库</p>
          </div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo()">
            <vab-icon icon="yewu" is-custom-svg />
            <p>业务数据</p>
          </div>
        </div>
      </el-col>
      <el-col :span="2">
        <div>&nbsp;</div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { getSelectedBookInfo } from '@/api/workbench/accountManage'

  const routeMap = {
    compliance: {
      statement: '/compliance/statement',
      subject: '/compliance/subject',
      balance: '/compliance/balance',
      accountCate: '/compliance/accountCate',
      accountDetail: '/compliance/accountDetail',
      accountDiary: '/compliance/accountDiary',
      accountAssist: '/compliance/accountAssist',
      voucherLib: '/compliance/voucherLib',
    },
    cwsjGz: {
      statement: '/cwsjGz/statementGZ',
      subject: '/cwsjGz/subjectGZ',
      balance: '/cwsjGz/balanceGZ',
      accountCate: '/cwsjGz/accountCateGZ',
      accountDetail: '/cwsjGz/accountDetailGZ',
      accountDiary: '/cwsjGz/accountDiaryGZ',
      accountAssist: '/cwsjGz/accountAssistGZ',
      voucherLib: '/cwsjGz/voucherLibGZ',
    },
  }

  export default {
    name: 'AccountData',
    data() {
      return {
        loading: false,
      }
    },
    mounted() {
      // if (!localStorage.getItem('bookInfo')) this.getSelectedBook()
    },
    methods: {
      goTo(page) {
        if (!page) return
        const routeGroup = this.$route.path.startsWith('/cwsjGz')
          ? 'cwsjGz'
          : 'compliance'
        this.$router.push(routeMap[routeGroup][page])
      },
      async getSelectedBook() {
        this.loading = true
        const { data } = await getSelectedBookInfo()
        this.loading = false
        if (data) {
          localStorage.setItem('bookInfo', JSON.stringify(data))
        }
      },
    },
  }
</script>
<style scoped>
  .line-view {
    text-align: center;
    height: 600px;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  .line-item {
    background: #dfdfe2;
    padding: 10px;
    margin: 5px;
  }
  .line-item:hover {
    background: #17a38e;
    cursor: pointer;
    color: white;
  }
  .line-item .vab-icon {
    width: 60px;
    height: 60px;
  }
  .line-item p {
    font-size: 18px;
  }
</style>
