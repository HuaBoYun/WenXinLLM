<template>
  <el-upload
    class="upload-demo"
    action="https://jsonplaceholder.typicode.com/posts/"
    :on-preview="handlePreview"
    :on-remove="handleRemove"
    :before-remove="beforeRemove"
    multiple
    :limit="3"
    :on-exceed="handleExceed"
    :file-list="fileList"
  >
    <el-button size="small" type="primary">添加附件</el-button>
  </el-upload>
</template>
<script>
  export default {
    name: 'Attachment',
    data() {
      return {
        fileList: [
          {
            name: 'food.jpeg',
            url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
          },
          {
            name: 'food2.jpeg',
            url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
          },
        ],
      }
    },
    methods: {
      /**
       * @description: 移除文件
       * @param {*} file
       * @param {*} fileList
       * @return {*}
       */      
      handleRemove(file, fileList) {},
      /**
       * @description: 预览文件
       * @param {*} file
       * @return {*}
       */      
      handlePreview(file) {},
      /**
       * @description: 
       * @param {*} files
       * @param {*} fileList
       * @return {*}
       */      
      handleExceed(files, fileList) {
        this.$message.warning(
          `当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
            files.length + fileList.length
          } 个文件`
        )
      },
      /**
       * @description: 移除文件前操作
       * @param {*} file
       * @param {*} fileList
       * @return {*}
       */      
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${file.name}？`)
      },
    },
  }
</script>
<style></style>
