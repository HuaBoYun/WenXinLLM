<template>
  <div class="fileUpload">
    <i class="icon el-icon-paperclip" @click="onUploadFile"></i>
    <div class="fileNames" v-if="fileValue.name">
      <div class="fileItem">
        {{ fileValue.name }}
        <i class="el-icon el-icon-close fileIcon" @click="onDelete"></i>
      </div>
    </div>
    <input
      v-show="false"
      type="file"
      ref="uploadRef"
      id="fileUpload"
      :multiple="multiple"
    />
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'fileUpload',
    data() {
      return {
        fileValue: {
          name: '',
          content: '',
        },
      }
    },
    props: {
      value: {
        type: Object,
        default: () => ({
          name: '',
          content: '',
        }),
      },
      multiple: {
        type: Boolean,
        default: false,
      },
    },
    watch: {
      value: {
        deep: true,
        immediate: true,
        handler(newVal, oldVal) {
          this.fileValue = newVal || {}
          Object.assign(this.fileValue, newVal || {})
        },
      },
    },
    methods: {
      onSelectFile(event) {
        // 获取选中的文件
        var files = event.target.files
        if (files.length) {
          this.onUploadFileSuccess(files)
        }
      },
      onUploadFileSuccess(files) {
        this.listLoading = true
        const data = new FormData()
        for (let i = 0; i < files.length; i++) {
          data.append('files', files[i])
          this.fileValue.name = files[i].name
        }

        axios({
          method: 'POST',
          url: 'https://office.wenxin.example.com/api/app/get_file_contents/',
          data,
        })
          .then((resp) => {
            if (resp && resp.status == 200 && resp.data.out_list.length) {
              this.fileValue.content = resp.data.out_list[0]
              this.$emit('input', { ...this.fileValue })
            } else {
              this.$message.error(resp.statusText || '上传失败')
            }
          })
          .catch((err) => {
            this.$message.error(err || '上传失败')
          })
          .finally(() => {
            document.getElementById('fileUpload').value = ''
            this.listLoading = false
          })
      },
      onUploadFile(row) {
        this.$refs.uploadRef.click()
        document
          .getElementById('fileUpload')
          .addEventListener('change', this.onSelectFile)
      },
      onDelete() {
        Object.assign(this.fileValue, { name: '', content: '' })
        this.$emit('input', { ...this.fileValue })
      },
    },
  }
</script>

<style scoped lang="scss">
  .fileUpload {
    display: flex;
    align-items: center;

    .icon {
      cursor: pointer;
      font-size: 20px;
      font-weight: 700;
      margin-right: 10px;
    }
    .fileNames {
      display: flex;
      align-items: center;

      .fileItem {
        max-width: 130px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-right: 10px;
        position: relative;
        background: rgb(179, 216, 255);
        padding: 4px 20px 4px 12px;
        border-radius: 8px;

        .fileIcon {
          position: absolute;
          right: 5px;
          top: 5px;
          cursor: pointer;
        }
      }
    }
  }
</style>
