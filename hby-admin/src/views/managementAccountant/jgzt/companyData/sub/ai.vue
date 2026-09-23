<template>
  <div>
    <div class="top">
      <el-card class="container">
        <div v-for="item in list" :key="item.que">
          <div class="question">
            <img class="avatar" :src="jpg1" />
            <div class="que">{{ item.que }}</div>
          </div>
          <div class="answer">
            <img class="avatar1" :src="jpg2" />
            <div class="ans">{{ item.ans }}</div>
          </div>
        </div>
      </el-card>
      <div class="input">
        <el-input
          placeholder="请输入问题"
          clearable
          v-model="question"
          style="width: 90%"
        ></el-input>
        <el-button
          type="primary"
          style="margin-left: 20px"
          icon="el-icon-search"
          @click="handleSearch"
        >
          搜索
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import jpg1 from '@/assets/1.png'
  import jpg2 from '@/assets/2.png'
  export default {
    data() {
      return {
        question: '',
        list: [],
        answer: '这个问题还有待探究',
        jpg1: jpg1,
        jpg2: jpg2,
      }
    },

    methods: {
      handleSearch() {
        if (!this.question) {
          this.$message.error('请输入问题')
          return
        }
        const info = { que: this.question, ans: this.answer }
        this.list.push(info)
      },
    },
  }
</script>
<style scoped lang="scss">
  .top {
    padding: 50px;
  }
  .container {
    width: 100%;
    height: 500px;
    border: 1px solid #c1c4cb;
    border-radius: 10px;
    max-height: 500px;
    overflow-y: scroll;
  }
  .question {
    display: flex;
    flex-direction: row-reverse;
    width: 100%;
    align-items: center;
  }
  .answer {
    display: flex;
    align-items: left;
    width: 100%;
    align-items: center;
    margin: 10px 0;
  }
  .avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-left: 20px;
  }
  .avatar1 {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 20px;
  }
  .que {
    background-color: #d7e2f6;
    width: 87%;
    padding: 10px 5px;
    border-radius: 10px;
  }
  .ans {
    background-color: #ebeff9;
    width: 87%;
    padding: 10px 5px;
    border-radius: 10px;
  }
</style>
