<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div class="content">
      <div class="left">
        <iframe
          frameborder="0"
          style="width: 100%; height: 100%"
          :src="render.ret_file1"
        ></iframe>
      </div>
      <div class="right">
        <iframe
          frameborder="0"
          width="100%"
          height="100%"
          :src="render.ret_file2"
        ></iframe>
      </div>
    </div>
    <div class="bottom">
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
    </div>
  </el-dialog>
</template>

<script>
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '文件对比',
        render: {},
      }
    },
    methods: {
      show() {
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
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
  }
  .left {
    width: 50%;
    background-color: #fff;
    border: 1px dotted #c2c2c2;
  }
  .right {
    flex: 1;
    background-color: #fff;
    border: 1px dotted #c2c2c2;
    margin-left: 20px;
  }
  .bottom {
    text-align: center;
    margin-top: 20px;
  }
</style>
