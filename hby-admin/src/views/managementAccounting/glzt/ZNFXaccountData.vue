<template>
  <div class="system-log-container">
    <el-row :gutter="20" v-loading="loading">
      <el-col :span="2">
        <div>&nbsp;</div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo('/glzt/statement')">
            <vab-icon icon="baobiao" is-custom-svg />
            <p>报表数据</p>
          </div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo('/glzt/subject')">
            <vab-icon icon="kemu" is-custom-svg />
            <p>科目表</p>
          </div>
          <div class="line-item" @click="goTo('/glzt/balance')">
            <vab-icon icon="balance" is-custom-svg />
            <p>余额表</p>
          </div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo('/glzt/accountCate')">
            <vab-icon icon="zongzhang" is-custom-svg />
            <p>总分类账</p>
          </div>
          <div class="line-item" @click="goTo('/glzt/accountDetail')">
            <vab-icon icon="mingxi" is-custom-svg />
            <p>明细账</p>
          </div>
          <div class="line-item" @click="goTo('/glzt/accountDiary')">
            <vab-icon icon="jizhang" is-custom-svg />
            <p>日记账</p>
          </div>
          <div class="line-item" @click="goTo('/glzt/accountAssist')">
            <vab-icon icon="fuzhu" is-custom-svg />
            <p>辅助账</p>
          </div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo('/glzt/voucherLib')">
            <vab-icon icon="pingzheng" is-custom-svg />
            <p>凭证库</p>
          </div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="line-view">
          <div class="line-item" @click="goTo('/auditSjfx/businessData')">
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
        this.$router.push(page)
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
