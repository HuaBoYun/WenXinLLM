<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-card class="el-card-d" shadow="always">
        <el-timeline infinite-scroll-disabled="disabled">
          <div v-if="pagemessages.length > 0">
            <el-timeline-item
              v-for="(item, index) in pagemessages"
              :key="index"
              :timestamp="item.createDate"
              placement="top"
            >
              <el-card class="el-card-m">
                <span class="el-card-m-content">{{ item.content }}</span>
                <div />
                <span class="el-card-m-nick-name">
                  {{ item.nickName }} 提交于 {{ item.createTime }}
                </span>
              </el-card>
            </el-timeline-item>
          </div>
          <div v-else>
            <el-timeline-item placement="top">
              <el-card class="el-card-m">
                <p class="el-card-m-nick-name">没有任何留言</p>
              </el-card>
            </el-timeline-item>
          </div>
        </el-timeline>
        <el-pagination
          background
          :current-page="currentPage"
          :page-size="pagesize"
          layout="prev, pager, next"
          :total="pagemessages.length"
          :hide-on-single-page="true"
          @current-change="handleCurrentChange"
        />
      </el-card>
    </el-row>
    <!-- <div class="el-card-messages">
      <el-input v-model="nickName" size="mini" class="message-nick-name">
        <template slot="prepend">昵称：</template>
      </el-input>
      <el-input
        slot="prepend"
        v-model="message"
        type="textarea"
        :rows="2"
        class="message-text"
        placeholder="输入留言"
        maxlength="200"
      />

      <el-button
        type="info"
        round
        class="submit-message"
        size="mini"
        @click="submitMessage"
      >
        留言
      </el-button>
    </div> -->
  </el-dialog>
</template>

<script>
  import { getMsgById } from '@/api/fwgl/pfpx'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '详细',
        nickName: '',
        message: '',
        pagesize: 3,
        currentPage: 1,
        pagemessages: [
          {
            createTime: '2022-10-24',
            createDate: '2022-10-24 10:00:00',
            nickName: '测试1',
            content: '测试内容！！！',
          },
          {
            createTime: '2022-10-24',
            createDate: '2022-10-24 11:00:00',
            nickName: '测试2',
            content: '测试内容！！！',
          },
          {
            createTime: '2022-10-24',
            createDate: '2022-10-24 12:00:00',
            nickName: '测试3',
            content: '测试内容！！！',
          },
          {
            createTime: '2022-10-24',
            createDate: '2022-10-24 13:00:00',
            nickName: '测试4',
            content: '测试内容！！！',
          },
        ],
      }
    },
    created() {},
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} row 编辑或详情数据
       * @return {*}
       */      
      showEdit(row) {
        // this.getInfo(row.id)
        this.dialogFormVisible = true
      },
      async getInfo(id) {
        getMsgById({ id }).then((res) => {
          this.pagemessages = res.data
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
      },
      // 翻页
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.currentPage = val
        this.pagemessages = []
      },
    },
  }
</script>

<style scoped>
  .el-card-d {
    float: left;
    margin-top: 20px;
    margin-left: 10%;
    width: 80%;
    height: 500px;
    background: rgb(252, 250, 250);
  }

  .el-card-m {
    height: 100px;
  }

  .el-card-m-content {
    display: block;
    font-weight: bold;
  }

  .el-card-m-nick-name {
    display: block;
    font-size: x-small;
    margin-top: 15px;
    color: gray;
  }

  .el-card-messages {
    float: left;
    margin-top: 20px;
    margin-left: 10%;
    width: 70%;
  }

  .message-nick-name {
    width: 50%;
  }
  .message-text {
    margin-top: 10px;
    display: block;
    width: 50%;
  }

  .submit-message {
    margin-top: 10px;
    width: 80px;
    background: rgb(235, 245, 247);
    color: cadetblue;
    text-align: center;
    letter-spacing: 20px;
  }
</style>
