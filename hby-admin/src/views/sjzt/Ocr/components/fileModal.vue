<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    height="600px"
    @close="close"
  >
    <div class="content" v-if="fileList.length == 0">
      <div class="top">
        <img :src="this.uploadImg" alt="" />
      </div>

      <div class="title">{{ row.tip }}</div>
      <div>
        {{ row.remake }}
      </div>
      <div class="bottom">
        <el-upload
          class="upload-demo"
          :limit="3"
          :show-file-list="false"
          :on-success="handleSuccess"
          :action="baseApi + api"
          :headers="headers"
        >
          <el-button
            type="primary"
            icon="el-icon-plus"
            style="margin-right: 20px; width: 150px"
            @click="handleSuccess"
          >
            {{ row.button1 }}
          </el-button>
        </el-upload>
        <el-upload
          class="upload-demo"
          :action="baseApi + api"
          :headers="headers"
          :limit="3"
          :show-file-list="false"
        >
          <el-button
            icon="el-icon-plus"
            style="width: 150px"
            @click="handleSuccess"
          >
            {{ row.button2 }}
          </el-button>
        </el-upload>
      </div>
    </div>
    <div class="list" v-if="fileList.length > 0">
      <vab-query-form :span="24">
        <vab-query-form-right-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-button native-type="submit" type="primary">
                删除选中
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button native-type="submit" type="primary">
                清空列表
              </el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        ref="multipleTable"
        :data="fileList"
        tooltip-effect="dark"
        @select="handleSelection"
        style="width: 100%; height: 300px"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="文件名" prop="name"></el-table-column>
        <el-table-column prop="size" label="原始大小"></el-table-column>
        <el-table-column prop="status" label="转换状态"></el-table-column>
        <el-table-column prop="" label="操作">
          <el-button type="text">删除</el-button>
        </el-table-column>
      </el-table>
      <div class="bottom1">
        <el-upload
          class="upload-demo"
          :limit="3"
          :show-file-list="false"
          :on-success="handleSuccess"
          :action="baseApi + api"
          :headers="headers"
        >
          <el-button
            type="primary"
            icon="el-icon-plus"
            style="margin-right: 20px; width: 150px"
            @click="handleSuccess"
          >
            {{ row.button1 }}
          </el-button>
        </el-upload>
        <el-upload
          class="upload-demo"
          :action="baseApi + api"
          :headers="headers"
          :limit="3"
          :show-file-list="false"
        >
          <el-button
            icon="el-icon-plus"
            style="width: 150px"
            @click="handleSuccess"
          >
            {{ row.button2 }}
          </el-button>
        </el-upload>
        <el-button
          type="primary"
          style="margin-left: 20px"
          @click="handleSuccess"
        >
          开始翻译
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import defaultImg from '@/assets/upload.png' //默认背景图
  import { baseURL } from '@/config'
  import store from '@/store'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '文件对比',
        render: {},
        uploadImg: defaultImg,
        row: {},
        fileList: [],
        baseApi: baseURL,
        api: '/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
      }
    },
    methods: {
      show(row) {
        this.title = row.name
        this.row = row
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
        this.fileList = []
      },
      handleSuccess(file) {
        this.fileList = [
          { name: '新建文件夹.jpg', size: '1M', status: '待翻译' },
        ]
      },
      handleSelection() {},
    },
  }
</script>

<style scoped lang="scss">
  .content {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 450px;
    background: #fafafc;
    border-radius: 5px;
  }
  .title {
    font-weight: 600;
    font-size: 20px;
    margin-bottom: 25px;
  }
  .top {
    padding: 50px 0;
  }
  .bottom {
    display: flex;
    flex-direction: row;
    text-align: center;
    margin-top: 20px;
  }
  .bottom1 {
    display: flex;
    flex-direction: row;
    margin-top: 20px;
  }
</style>
