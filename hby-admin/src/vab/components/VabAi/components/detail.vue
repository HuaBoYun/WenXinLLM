<template>
  <el-drawer
    append-to-body
    custom-class="vab-drawer"
    direction="rtl"
    size="760px"
    title="问心大模型"
    :visible.sync="drawerVisible"
  >
    <div class="top">
      <div class="input">
        <el-input
          placeholder="请在此处告诉AI你想要什么..."
          clearable
          v-model="question"
          style="width: 83%"
          type="textarea"
        ></el-input>
        <el-button
          type="primary"
          style="margin-left: 20px"
          icon="el-icon-search"
          @click="handleSearch"
        >
          生成
        </el-button>
      </div>
      <div class="input1">
        <span>场景：</span>
        <el-input
          placeholder="请输入场景"
          clearable
          v-model="sence"
          style="width: 25%"
        ></el-input>
        <span style="margin-left: 40px">长度：</span>
        <el-radio-group v-model="radio">
          <el-radio :label="3">中等</el-radio>
          <el-radio :label="6">较短</el-radio>
          <el-radio :label="9">较长</el-radio>
        </el-radio-group>
      </div>
      <el-card class="container" v-loading="loading">
        <div v-for="item in list" :key="item.que">
          <div class="question">
            <!-- <img class="avatar" :src="jpg1" /> -->
            <!-- <div class="que">{{ item.que }}</div> -->
          </div>
          <div class="answer">
            <!-- <img class="avatar1" :src="jpg2" /> -->
            <div class="ans">{{ item.ans }}</div>
          </div>
        </div>
      </el-card>
    </div>
  </el-drawer>
</template>
<script>
  import jpg1 from '@/assets/1.png'
  import jpg2 from '@/assets/2.png'
  import axios from 'axios'
  export default {
    data() {
      return {
        drawerVisible: false,
        question: '',
        list: [],
        answer: '这个问题还有待探究',
        jpg1: jpg1,
        jpg2: jpg2,
        radio: '',
        sence: '',
        loading: false,
      }
    },
    methods: {
      open() {
        this.drawerVisible = true
      },
      handleSearch() {
        if (!this.question) {
          this.$message.error('请输入问题')
          return
        }
        this.loading = true
        axios({
          url: `http://hit-mitlab.cn:7860/v1/chat/completions`,
          method: 'post',
          headers: { 'Content-Type': 'application/json' },
          data: {
            messages: [{ role: 'user', message: this.question }],
            // max_tokens: 1024,
            // temperature: 0.2,
            // top_k: 40,
            // repetition_penalty: 1.1,
            // max_tokens: 1024,
          },
        }).then((res) => {
          this.list = [{ ans: res.data.choices[1].message.content }]
          this.loading = false
        })
      },
    },
  }
</script>
<style scoped lang="scss">
  .top {
    padding: 50px 0;
  }
  .container {
    width: 100%;
    height: 560px;
    border: 1px solid #c1c4cb;
    border-radius: 10px;
    max-height: 560px;
    overflow-y: scroll;
    margin-top: 60px;
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
    width: 80%;
    padding: 10px 5px;
    border-radius: 10px;
  }
  .ans {
    background-color: #ebeff9;
    width: 100%;
    padding: 10px 5px;
    border-radius: 10px;
  }
  .input1 {
    margin-top: 20px;
  }
</style>
