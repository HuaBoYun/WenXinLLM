<template>
  <div v-loading="loading">
    <div
      style="
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 10px;
      "
      v-if="options.needTitle"
    >
      <h3 class="title">文件对比</h3>
      <el-button type="success" size="mini" @click="exscCompare">
        开始比对
      </el-button>
    </div>
    <div style="display: flex; align-items: center" v-if="options.type">
      <h4 class="title" style="flex: 1">
        {{ options.type === '1' ? '我方审批完成确定稿预览' : '原始的预览' }}
      </h4>
      <h4 class="title" style="flex: 1">
        {{
          options.type === '1'
            ? '对方用印后的PDF（图片）版本预览'
            : '修改后的预览'
        }}
      </h4>
      <div style="width: 26%"></div>
    </div>
    <div class="content">
      <div class="left">
        <iframe
          frameborder="0"
          style="width: 100%; height: 100%"
          :src="tempRender.ret_file1"
        ></iframe>
      </div>
      <div class="right">
        <iframe
          frameborder="0"
          width="100%"
          height="100%"
          :src="tempRender.ret_file2"
        ></iframe>
      </div>
      <div class="diff-list" v-if="options.needDiffList">
        <el-tabs v-model="activeName" @tab-click="handleClick" class="tab1">
          <el-tab-pane label="差异点(17)" name="first"></el-tab-pane>
          <el-tab-pane label="已忽略(0)" name="second"></el-tab-pane>
        </el-tabs>
        <el-tabs
          v-model="activeName"
          type="card"
          @tab-click="handleClick"
          class=""
        >
          <el-tab-pane label="全部(17)" name="first"></el-tab-pane>
          <el-tab-pane label="删除(2)" name="second"></el-tab-pane>
          <el-tab-pane label="新增(10)" name="third"></el-tab-pane>
          <el-tab-pane label="修改(5)" name="fourth"></el-tab-pane>
        </el-tabs>
        <div class="diff-point-list">
          <div
            class="point-item"
            v-for="(item, index) in pointList"
            :key="item.id"
          >
            <div class="point-title">
              <div :class="'text ' + item.type">
                {{ index + 1 }}.{{ item.typeStr }}
              </div>
              <div class="text blue">
                <i class="el-icon el-icon-view"></i>
                忽略
              </div>
            </div>
            <div class="point-content">
              <p>原文: {{ item.before }}</p>
              <p>差异: {{ item.after }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- <div class="bottom">
      <el-button-group>
        <el-button
          type="primary"
          size="mini"
          icon="el-icon-circle-plus-outline"
        ></el-button>
        <el-button type="success" size="mini" icon="el-icon-rank"></el-button>
        <el-button
          type="primary"
          size="mini"
          icon="el-icon-remove-outline"
        ></el-button>
      </el-button-group>
      <el-button-group style="margin: 0 10px">
        <el-button type="primary" size="mini" icon="el-icon-arrow-up">
          最顶
        </el-button>
        <el-button type="primary" size="mini" icon="el-icon-arrow-left">
          上一差异
        </el-button>
        <el-button type="primary" size="mini" icon="el-icon-arrow-right">
          下一差异
        </el-button>
        <el-button type="primary" size="mini" icon="el-icon-arrow-down">
          最底
        </el-button>
      </el-button-group>
      <el-button-group style="margin: 0 10px 0 0">
        <el-button type="primary" size="mini">双侧同步翻页</el-button>
        <el-button type="primary" size="mini">复制</el-button>
      </el-button-group>

      <el-button type="default" size="mini">汇总</el-button>
      <el-button type="success" size="mini">新增</el-button>
      <el-button type="warning" size="mini">更替</el-button>
      <el-button type="danger" size="mini">删除</el-button>
    </div> -->
  </div>
</template>

<script>
  import axios from 'axios'
  export default {
    props: {
      render: {
        type: Object,
        default: () => ({}),
      },
    },
    data() {
      return {
        loading: false,
        tempRender: {},
        options: {},
        pointList: [
          { id: 1, typeStr: '修改', type: 'warning', before: '0', after: '1' },
          { id: 1, typeStr: '修改', type: 'warning', before: '1', after: '0' },
          {
            id: 2,
            typeStr: '新增',
            type: 'success',
            before: '',
            after: '医疗',
          },
          {
            id: 3,
            typeStr: '删除',
            type: 'danger',
            before: '>99.0%',
            after: '',
          },
          { id: 4, typeStr: '删除', type: 'danger', before: '0', after: '' },
        ],
      }
    },
    mounted() {
      console.log('this.$route', this.$route)
      this.options = this.$route.query || {}

      if (this.render && this.render.ret_file1) {
        this.tempRender = this.render
      }

      if (this.options && this.options.ori_file1) {
        this.tempRender.ret_file1 = this.options.ori_file1
        this.tempRender.ret_file2 = this.options.ori_file2
      }
    },
    methods: {
      exscCompare() {
        this.loading = true
        axios({
          url: `https://office.wenxin.example.com/api/app/filescompare/${this.$route.query.id}/exec_compare/`,
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
        })
          .then((k) => {
            console.log('exec_compare', k)
            if (k.data.task_id) {
              this.getStatus(k.data.task_id)
            }
          })
          .catch((err) => {
            console.log('err', err)
            this.loading = false
          })
      },
      getStatus(id) {
        let interval = null
        interval = setInterval(() => {
          axios({
            url: `https://office.wenxin.example.com/api/app/filescompare/${this.$route.query.id}/exec_compare/?task_id=${id}`,
            method: 'GET',
          })
            .then((res) => {
              if (res.data.status === 'SUCCESS') {
                clearInterval(interval)
                axios({
                  url: `https://office.wenxin.example.com/api/app/filescompare/${this.$route.query.id}/`,
                  method: 'get',
                  headers: { 'Content-Type': 'application/json' },
                })
                  .then((k) => {
                    if (k.data.id) {
                      this.tempRender = k.data
                      if (k.data.status == '0') {
                        this.tempRender.ret_file1 = this.tempRender.ret_file1
                        this.tempRender.ret_file2 = this.tempRender.ret_file2
                      }
                    }
                  })
                  .finally(() => {
                    this.loading = false
                  })
              }
            })
            .catch((err) => {
              console.log('err', err)
              clearInterval(interval)
              this.loading = false
            })
        }, 2000)
      },
    },
  }
</script>

<style scoped lang="scss">
  .content {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 550px;
    background: #f6f8f9;
  }
  .left {
    flex: 1;
    background-color: #fff;
    border: 1px dotted #c2c2c2;
  }
  .right {
    flex: 1;
    background-color: #fff;
    border: 1px dotted #c2c2c2;
    margin-left: 20px;
  }
  .diff-list {
    width: 26%;
    background: #fff;
    margin-left: 20px;
    box-shadow: -2px 0px 4px 0 rgba(42, 60, 79, 0.1);
  }
  .bottom {
    text-align: center;
    margin-top: 20px;
  }

  :deep(.diff-list .tab1 .el-tabs__item) {
    width: 50% !important;
    text-align: center !important;
  }

  :deep(.diff-list .tab1 .el-tabs__nav) {
    width: 100% !important;
    padding: 10px;
  }

  .diff-list {
    .diff-point-list {
      padding: 10px 14px;
      height: 420px;
      overflow-y: auto;
      .point-item {
        border: 1px solid rgba(0, 0, 0, 0.1);
        margin-bottom: 10px;
        .point-title {
          display: flex;
          justify-content: space-between;
          padding: 14px 20px;
          background: #efefef;
          border-bottom: 1px solid rgba(0, 0, 0, 0.1);
        }
        .point-content {
          padding: 10px 20px;
          p {
          }
        }
      }
    }
  }

  .warning {
    color: #e6a23c;
  }

  .blue {
    color: #409eff;
  }

  .danger {
    color: #f56c6c;
  }

  .success {
    color: #67c23a;
  }
</style>
